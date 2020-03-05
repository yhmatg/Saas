package com.common.esimrfid.presenter.home;

import android.util.Log;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.InvOperateStatus;
import com.common.esimrfid.core.bean.emun.InventoryStatus;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.dao.ResultInventoryOrderDao;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class InvOrderPressnter extends BasePresenter<InvOrderContract.View> implements InvOrderContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public InvOrderPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }


    //获取盘点数据
    @Override
    public void fetchAllIvnOrders(String userId, boolean online) {
        mView.showDialog("loading...");
        if (!CommonUtils.isNetworkConnected()) {
            online = false;
        }
        addSubscribe(Observable.concat(getLocalInOrderObservable(online), mDataManager.fetchAllIvnOrders(userId))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .observeOn(Schedulers.io())
                .flatMap(new Function<List<ResultInventoryOrder>, ObservableSource<List<ResultInventoryOrder>>>() {
                    @Override
                    public ObservableSource<List<ResultInventoryOrder>> apply(List<ResultInventoryOrder> resultInventoryOrders) throws Exception {
                        ArrayList<String> unInvedRemoteOrders = new ArrayList<>();
                        for (ResultInventoryOrder resultInventoryOrder : resultInventoryOrders) {
                             if(resultInventoryOrder.getInv_status() == 10){
                                 unInvedRemoteOrders.add(resultInventoryOrder.getId());
                             }
                        }
                        //根据服务端没有盘点完场的盘点单，获取本地没有盘点完场的盘点单，替换服务端中未完成的盘点单（本地可能做过盘点任务，但是数据没有上传）
                        List<ResultInventoryOrder> localOrders = DbBank.getInstance().getResultInventoryOrderDao().findInvOrders();
                        List<ResultInventoryOrder> notInvedLocalOrders = DbBank.getInstance().getResultInventoryOrderDao().findNotInvedInvOrders(unInvedRemoteOrders);
                        //本地同步服务端已经删除的数据
                        List<ResultInventoryOrder> tempLocal = new ArrayList<>();
                        tempLocal.addAll(localOrders);
                        tempLocal.removeAll(resultInventoryOrders);
                        //数据库同步删除盘点单
                        DbBank.getInstance().getResultInventoryOrderDao().deleteItems(tempLocal);
                        //数据库同步删除盘点单下的资产
                        List<String> deleteIds = new ArrayList<>();
                        for (int i = 0; i < tempLocal.size(); i++) {
                            deleteIds.add(tempLocal.get(i).getId());
                            //DbBank.getInstance().getInventoryDetailDao().deleteLocalInvDetailByInvid(tempLocal.get(i).getId());
                        }
                        DbBank.getInstance().getInventoryDetailDao().deleteLocalInvDetailByInvids(deleteIds);
                        //本地数据和服务器数据的交集，服务端删除盘点单，本地同步跟新显示
                        notInvedLocalOrders.retainAll(resultInventoryOrders);
                        //服务端新增的数据
                        resultInventoryOrders.removeAll(notInvedLocalOrders);
                        List<ResultInventoryOrder> tempRemount = new ArrayList<>();
                        tempRemount.addAll(resultInventoryOrders);
                        tempRemount.addAll(notInvedLocalOrders);
                        DbBank.getInstance().getResultInventoryOrderDao().insertItems(resultInventoryOrders);
                        return Observable.just(tempRemount);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<List<ResultInventoryOrder>>(mView, false) {
                    @Override
                    public void onNext(List<ResultInventoryOrder> resultInventoryOrders) {
                        Log.e("yhmaaaaaaa", "resultInventoryOrders===" + resultInventoryOrders.size());
                        mView.dismissDialog();
                        mView.showInvOrders(resultInventoryOrders);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
    }

    //检查本地是否有盘点过未提交的数据
    //暂未用到
    @Override
    public void checkLocalDataState() {
        addSubscribe(Observable.create(new ObservableOnSubscribe<List<ResultInventoryOrder>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<ResultInventoryOrder>> emitter) throws Exception {
                        List<ResultInventoryOrder> newestOrders = DbBank.getInstance().getResultInventoryOrderDao().findInvOrders();
                        emitter.onNext(newestOrders);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new BaseObserver<List<ResultInventoryOrder>>(mView, false) {
                            @Override
                            public void onNext(List<ResultInventoryOrder> resultInventoryOrders) {
                                boolean locaLeftUpload = false;
                                if (resultInventoryOrders != null) {
                                    for (ResultInventoryOrder resultInventoryOrder : resultInventoryOrders) {
                                        if (resultInventoryOrder.getOpt_status() != null && resultInventoryOrder.getOpt_status() == InvOperateStatus.MODIFIED_BUT_NOT_SUBMIT.getIndex()) {
                                            locaLeftUpload = true;
                                        }
                                    }
                                }
                            }
                        })

        );
    }

    public Observable<BaseResponse<List<ResultInventoryOrder>>> getLocalInOrderObservable(final boolean online) {
        Observable<BaseResponse<List<ResultInventoryOrder>>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<List<ResultInventoryOrder>>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<List<ResultInventoryOrder>>> emitter) throws Exception {
                List<ResultInventoryOrder> newestOrders = DbBank.getInstance().getResultInventoryOrderDao().findInvOrders();
                if (online || newestOrders.isEmpty()) {
                    emitter.onComplete();
                    Log.e(TAG, "network get data");
                } else {
                    Log.e(TAG, "newestOrders======" + newestOrders);
                    BaseResponse<List<ResultInventoryOrder>> invOrderResponse = new BaseResponse<>();
                    invOrderResponse.setResult(newestOrders);
                    invOrderResponse.setCode("200000");
                    invOrderResponse.setMessage("成功");
                    invOrderResponse.setSuccess(true);
                    emitter.onNext(invOrderResponse);
                }
            }
        });
        return invOrderObservable;
    }

    //网络获取盘点数据
    @Override
    public void fetchAllInvDetails(String orderId, boolean online) {
        //mView.showDialog("loading...");
        if (!CommonUtils.isNetworkConnected()) {
            online = false;
        }
        addSubscribe(Observable.concat(getLocalInvDetailsObservable(orderId, online), mDataManager.fetchAllInvDetails(orderId))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ResultInventoryDetail>() {
                    @Override
                    public void accept(ResultInventoryDetail resultInventoryDetail) throws Exception {
                        if (resultInventoryDetail.getDetailResults() != null) {
                            //保存盘点单资产
                            DbBank.getInstance().getInventoryDetailDao().insertItems(resultInventoryDetail.getDetailResults());
                        }
                    }
                })
                //本地远程除盘点状态同步 1116
                /*.flatMap(new Function<ResultInventoryDetail, ObservableSource<ResultInventoryDetail>>() {
                    @Override
                    public ObservableSource<ResultInventoryDetail> apply(ResultInventoryDetail resultInventoryDetail) throws Exception {
                        //本地数据库列表
                        List<InventoryDetail> localInvDetailsByInvid = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailByInvid(orderId);
                        //更新出盘点状态的其他盘点数据
                        List<InventoryDetail> finalData = handleLocalAndRemountData(localInvDetailsByInvid, resultInventoryDetail.getDetailResults());
                        DbBank.getInstance().getInventoryDetailDao().insertItems(finalData);
                        resultInventoryDetail.setDetailResults(finalData);
                        return Observable.just(resultInventoryDetail);
                    }
                })*/.observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResultInventoryDetail>(mView, false) {
                    @Override
                    public void onNext(ResultInventoryDetail resultInventoryDetail) {
                        //mView.dismissDialog();
                        mView.handleInvDetails(resultInventoryDetail);
                    }
                }));
    }

    //本地获取盘点数据
    public Observable<BaseResponse<ResultInventoryDetail>> getLocalInvDetailsObservable(String orderId, final boolean online) {
        Observable<BaseResponse<ResultInventoryDetail>> localInvDetailObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<ResultInventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<ResultInventoryDetail>> emitter) throws Exception {
                List<InventoryDetail> localInvDetailsByInvid = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailByInvid(orderId);
                ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(orderId);
                if (online && localInvDetailsByInvid.size() == 0) {
                    emitter.onComplete();
                } else {
                    BaseResponse<ResultInventoryDetail> localInvDetailResponse = new BaseResponse<>();
                    ResultInventoryDetail resultInventoryDetail = new ResultInventoryDetail();
                    resultInventoryDetail.setDetailResults(localInvDetailsByInvid);
                    //20191219 start 从盘点单条目中获取相关属性信息
                    resultInventoryDetail.setInv_total_count(invOrderByInvId.getInv_total_count());
                    resultInventoryDetail.setInv_finish_count(invOrderByInvId.getInv_finish_count());
                    resultInventoryDetail.setCreate_date(invOrderByInvId.getCreate_date());
                    resultInventoryDetail.setInv_exptfinish_date(invOrderByInvId.getInv_exptfinish_date());
                    resultInventoryDetail.setId(invOrderByInvId.getId());
                    //20191219 end
                    localInvDetailResponse.setResult(resultInventoryDetail);
                    localInvDetailResponse.setCode("200000");
                    localInvDetailResponse.setMessage("成功");
                    localInvDetailResponse.setSuccess(true);
                    emitter.onNext(localInvDetailResponse);
                }
            }
        });
        return localInvDetailObservable;
    }

    //上传盘点数据到服务器
    @Override
    public void upLoadInvDetails(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails, String uid) {
        addSubscribe(mDataManager.uploadInvDetails(orderId, invDetails, uid)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleBaseResponse())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                            //上传盘点条目到数据库后，更新父条目ResultInventoryOrder状态
                            ResultInventoryOrderDao resultInventoryOrderDao = DbBank.getInstance().getResultInventoryOrderDao();
                            ResultInventoryOrder invOrderByInvId = resultInventoryOrderDao.findInvOrderByInvId(orderId);
                            invOrderByInvId.setOpt_status(InvOperateStatus.MODIFIED_AND_SUBMIT_BUT_NOT_FINISHED.getIndex());
                            int orderStatus = 10;
                            invOrderByInvId.setInv_status(orderStatus);
                            //更新盘点单完成上传和没有提交数目
                            //1223 start
                           /* Integer finishCount = invOrderByInvId.getInv_finish_count() + invDetails.size();
                            invOrderByInvId.setInv_finish_count(finishCount);*/
                            //1223 end
                            //modify bug 253 20191230 start
                            int  notSubmitCount = invOrderByInvId.getInv_notsubmit_count() == null ? 0 : invOrderByInvId.getInv_notsubmit_count() - invDetails.size();
                            if(notSubmitCount < 0){
                                notSubmitCount = 0;
                            }
                            //modify bug 253 20191230 end
                            invOrderByInvId.setInv_notsubmit_count(notSubmitCount);
                            resultInventoryOrderDao.updateItem(invOrderByInvId);
                            //跟新盘点子条目ResultInventoryDetail的盘点提交状态
                            // 暂定 本地盘点和已经上传的区分
                            for (InventoryDetail inventoryDetail : inventoryDetails) {
                                inventoryDetail.getInvdt_status().setCode(InventoryStatus.FINISH.getIndex());
                            }
                            DbBank.getInstance().getInventoryDetailDao().updateItems(inventoryDetails);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        mView.handelUploadResult(baseResponse);
                    }
                }));
    }

    @Override
    public void finishInvOrderWithAsset(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails, String uid) {
        addSubscribe(mDataManager.finishInvOrderWithAsset(orderId, uid, invDetails)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleBaseResponse())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                            //上传盘点条目到数据库后，更新父条目ResultInventoryOrder状态
                            ResultInventoryOrderDao resultInventoryOrderDao = DbBank.getInstance().getResultInventoryOrderDao();
                            ResultInventoryOrder invOrderByInvId = resultInventoryOrderDao.findInvOrderByInvId(orderId);
                            invOrderByInvId.setOpt_status(InvOperateStatus.FINISHED.getIndex());
                            int orderStatus = 11;
                            invOrderByInvId.setInv_status(orderStatus);
                            //更新盘点单完成上传和没有提交数目
                            //1223 start
                           /* Integer finishCount = invOrderByInvId.getInv_finish_count() + invDetails.size();
                            invOrderByInvId.setInv_finish_count(finishCount);*/
                            //1223 end
                            //modify bug 253 20191230 start
                            int notSubmitCount = invOrderByInvId.getInv_notsubmit_count() == null ? 0 : invOrderByInvId.getInv_notsubmit_count() - invDetails.size();
                            if(notSubmitCount < 0){
                                notSubmitCount = 0;
                            }
                            //modify bug 253 20191230 end
                            invOrderByInvId.setInv_notsubmit_count(notSubmitCount);
                            resultInventoryOrderDao.updateItem(invOrderByInvId);
                            //跟新盘点子条目ResultInventoryDetail的盘点提交状态
                            // 暂定 本地盘点和已经上传的区分
                            for (InventoryDetail inventoryDetail : inventoryDetails) {
                                inventoryDetail.getInvdt_status().setCode(InventoryStatus.FINISH.getIndex());
                            }
                            DbBank.getInstance().getInventoryDetailDao().updateItems(inventoryDetails);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        mView.handelFinishInvOrder(baseResponse);
                    }
                }));
    }


}
