package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.InvOperateStatus;
import com.common.esimrfid.core.bean.emun.InventoryStatus;
import com.common.esimrfid.core.bean.emun.OrderStatusEm;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.invdetailbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.invdetailbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.OrderStatus;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;
import com.common.esimrfid.core.dao.ResultInventoryOrderDao;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

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
        addSubscribe(Observable.concat(getLocalInvDetailsObservable(orderId,online),mDataManager.fetchAllInvDetails(orderId))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ResultInventoryDetail>() {
                    @Override
                    public void accept(ResultInventoryDetail resultInventoryDetail) throws Exception {
                        if (resultInventoryDetail.getDetailResults() != null) {
                            DbBank.getInstance().getInventoryDetailDao().insertItems(resultInventoryDetail.getDetailResults());
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResultInventoryDetail>(mView, false) {
                    @Override
                    public void onNext(ResultInventoryDetail resultInventoryDetail) {
                        mView.handleInvDetails(resultInventoryDetail.getDetailResults());
                    }
                }));
    }

    //上传盘点数据到服务器
    @Override
    public void upLoadInvDetails(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails) {
        addSubscribe(mDataManager.uploadInvDetails(orderId, invDetails)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleBaseResponse())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        if(baseResponse.isSuccess()){
                            //上传盘点条目到数据库后，更新父条目ResultInventoryOrder状态
                            ResultInventoryOrderDao resultInventoryOrderDao = DbBank.getInstance().getResultInventoryOrderDao();
                            ResultInventoryOrder invOrderByInvId = resultInventoryOrderDao.findInvOrderByInvId(orderId);
                            invOrderByInvId.setOpt_status(InvOperateStatus.MODIFIED_AND_SUBMIT_BUT_NOT_FINISHED.getIndex());
                            OrderStatus orderStatus = new OrderStatus();
                            orderStatus.setCode(OrderStatusEm.PROCESSING.getIndex());
                            orderStatus.setIndex(OrderStatusEm.PROCESSING.getIndex());
                            orderStatus.setName(OrderStatusEm.PROCESSING.getName());
                            invOrderByInvId.setInv_status(orderStatus);
                            resultInventoryOrderDao.updateItem(invOrderByInvId);
                            //跟新盘点子条目ResultInventoryDetail的盘点提交状态
                            // 暂定 本地盘点和已经上传的区分
                            for (InventoryDetail inventoryDetail : inventoryDetails) {
                                inventoryDetail.getInvdt_status().setCode(InventoryStatus.FINISH.getIndex());
                            }
                            DbBank.getInstance().getInventoryDetailDao().updateItems(inventoryDetails);
                        }else {

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

    //完成盘点后，状态上传到服务器
    @Override
    public void finishInvOrder(String orderId, String uid, String remark) {
        addSubscribe(mDataManager.finishInvOrder(orderId, uid, remark)
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleBaseResponse())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        if(baseResponse.isSuccess()){
                            ResultInventoryOrderDao resultInventoryOrderDao = DbBank.getInstance().getResultInventoryOrderDao();
                            ResultInventoryOrder invOrderByInvId = resultInventoryOrderDao.findInvOrderByInvId(orderId);
                            invOrderByInvId.setOpt_status(InvOperateStatus.FINISHED.getIndex());
                            OrderStatus orderStatus = new OrderStatus();
                            orderStatus.setCode(OrderStatusEm.FINISH.getIndex());
                            orderStatus.setIndex(OrderStatusEm.FINISH.getIndex());
                            orderStatus.setName(OrderStatusEm.FINISH.getName());
                            invOrderByInvId.setInv_status(orderStatus);
                            resultInventoryOrderDao.updateItem(invOrderByInvId);
                        }else {
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
            @Override
            public void onNext(BaseResponse baseResponse) {
                mView.handelFinishInvorder(baseResponse);
            }
        }));
    }

    //一次完整扫描后数据库跟新盘点状态
    @Override
    public void updateLocalInvDetailsState(String orderId,List<InventoryDetail> inventoryDetails) {
        if(inventoryDetails.size() == 0){
            return;
        }
        addSubscribe(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                DbBank.getInstance().getInventoryDetailDao().updateItems(inventoryDetails);
                ResultInventoryOrderDao resultInventoryOrderDao = DbBank.getInstance().getResultInventoryOrderDao();
                ResultInventoryOrder invOrderByInvId = resultInventoryOrderDao.findInvOrderByInvId(orderId);
                invOrderByInvId.setOpt_status(InvOperateStatus.MODIFIED_BUT_NOT_SUBMIT.getIndex());
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setCode(OrderStatusEm.PROCESSING.getIndex());
                orderStatus.setIndex(OrderStatusEm.PROCESSING.getIndex());
                orderStatus.setName(OrderStatusEm.PROCESSING.getName());
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
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setCode(OrderStatusEm.PROCESSING.getIndex());
                orderStatus.setIndex(OrderStatusEm.PROCESSING.getIndex());
                orderStatus.setName(OrderStatusEm.PROCESSING.getName());
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
    public Observable<BaseResponse<ResultInventoryDetail>> getLocalInvDetailsObservable(String orderId, final boolean online){
        Observable<BaseResponse<ResultInventoryDetail>> localInvDetailObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<ResultInventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<ResultInventoryDetail>> emitter) throws Exception {
                List<InventoryDetail> localInvDetailsByInvid = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailByInvid(orderId);
                if (online || localInvDetailsByInvid.size() == 0) {
                    emitter.onComplete();
                } else {
                    BaseResponse<ResultInventoryDetail> localInvDetailResponse = new BaseResponse<>();
                    ResultInventoryDetail resultInventoryDetail = new ResultInventoryDetail();
                    resultInventoryDetail.setDetailResults(localInvDetailsByInvid);
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
}
