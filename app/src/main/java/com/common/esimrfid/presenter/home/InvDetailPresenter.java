package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvDetailContract;
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
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InvDetailPresenter extends BasePresenter<InvDetailContract.View> implements InvDetailContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public InvDetailPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    //网络获取盘点数据
    @Override
    public void fetchAllInvDetails(String orderId, boolean online) {
        mView.showDialog("loading...");
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
                            //add 2020/02/17 start
                            ResultInventoryOrderDao resultInventoryOrderDao = DbBank.getInstance().getResultInventoryOrderDao();
                            ResultInventoryOrder resultInvOrderByInvId = resultInventoryOrderDao.findInvOrderByInvId(orderId);
                            resultInvOrderByInvId.setInv_finish_count(resultInventoryDetail.getInv_finish_count());
                            resultInventoryOrderDao.updateItem(resultInvOrderByInvId);
                            //add 2020/02/17 end
                            //
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
                        mView.dismissDialog();
                        mView.handleInvDetails(resultInventoryDetail);
                    }
                }));
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
                            //1223 start 扫描到就算已经盘点
                           /* Integer finishCount = invOrderByInvId.getInv_finish_count() + invDetails.size();
                            invOrderByInvId.setInv_finish_count(finishCount);*/
                            //1223 end
                            //modify bug 253 20191230 start
                            int notSubmitCount = invOrderByInvId.getInv_notsubmit_count() - invDetails.size();
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

    //本地获取所有本地InventoryDetail数据
    @Override
    public void findLocalInvDetailByInvid(String invId) {
        addSubscribe(Observable.create((ObservableOnSubscribe<List<InventoryDetail>>) new ObservableOnSubscribe<List<InventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<List<InventoryDetail>> emitter) throws Exception {
                List<InventoryDetail> localInvDetails = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailByInvid(invId);
                emitter.onNext(localInvDetails);
            }
        }).compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<List<InventoryDetail>>(mView, false) {
                    @Override
                    public void onNext(List<InventoryDetail> inventoryDetails) {
                        mView.uploadInvDetails(inventoryDetails);
                    }
                }));
    }

    //一次完整扫描后数据库跟新盘点状态
    @Override
    public void updateLocalInvDetailsState(String orderId, List<InventoryDetail> inventoryDetails) {
        if (inventoryDetails.size() == 0) {
            return;
        }
        addSubscribe(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                DbBank.getInstance().getInventoryDetailDao().updateItems(inventoryDetails);
                ResultInventoryOrderDao resultInventoryOrderDao = DbBank.getInstance().getResultInventoryOrderDao();
                ResultInventoryOrder invOrderByInvId = resultInventoryOrderDao.findInvOrderByInvId(orderId);
                invOrderByInvId.setOpt_status(InvOperateStatus.MODIFIED_BUT_NOT_SUBMIT.getIndex());
                int orderStatus = 10;
                invOrderByInvId.setInv_status(orderStatus);
                //1223 start
                //更新完成盘点数目
                Integer finishCount = invOrderByInvId.getInv_finish_count() + inventoryDetails.size();
                invOrderByInvId.setInv_finish_count(finishCount);
                //1223 end
                //更新盘点单未提交数量
                int oldNotSubmit = invOrderByInvId.getInv_notsubmit_count() == null ? 0 : invOrderByInvId.getInv_notsubmit_count();
                Integer notSubmitCount = oldNotSubmit + inventoryDetails.size();
                invOrderByInvId.setInv_notsubmit_count(notSubmitCount);
                resultInventoryOrderDao.updateItem(invOrderByInvId);
            }
        }).compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<String>(mView, false) {
                    @Override
                    public void onNext(String orderId) {

                    }
                }));
    }

    //更新单个盘点数据 暂未用到
    @Override
    public void updateLocalInvDetailState(String orderId, InventoryDetail inventoryDetail) {
        addSubscribe(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                DbBank.getInstance().getInventoryDetailDao().updateItem(inventoryDetail);
                ResultInventoryOrderDao resultInventoryOrderDao = DbBank.getInstance().getResultInventoryOrderDao();
                ResultInventoryOrder invOrderByInvId = resultInventoryOrderDao.findInvOrderByInvId(orderId);
                invOrderByInvId.setOpt_status(InvOperateStatus.MODIFIED_BUT_NOT_SUBMIT.getIndex());
                int orderStatus = 10;
                invOrderByInvId.setInv_status(orderStatus);
                resultInventoryOrderDao.updateItem(invOrderByInvId);
            }
        }).compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<String>(mView, false) {
                    @Override
                    public void onNext(String orderId) {

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
    //除盘点状态外，资产的其他属性使用服务器上数据（用户可能修改过）
    public List<InventoryDetail> handleLocalAndRemountData(List<InventoryDetail> local, List<InventoryDetail> remount) {
        ArrayList<InventoryDetail> tempData = new ArrayList<>();
        if (local.size() == 0) {
            tempData.addAll(remount);
        } else {
            HashMap<String, InventoryDetail> hasMap = new HashMap<>();
            for (int i = 0; i < remount.size(); i++) {
                hasMap.put(remount.get(i).getId(), remount.get(i));
            }
            for (int i = 0; i < local.size(); i++) {
                InventoryDetail localDetail = local.get(i);
                InventoryDetail remountDetail = hasMap.get(localDetail.getId());
                if (remountDetail != null) {
                    remountDetail.getInvdt_status().setCode(localDetail.getInvdt_status().getCode());
                    tempData.add(remountDetail);
                }
            }
        }
        return tempData;
    }
}
