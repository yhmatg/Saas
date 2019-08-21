package com.common.esimrfid.presenter.home;

import android.util.Log;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.InvOperateStatus;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
    public void fetchAllIvnOrders(String userId,boolean online) {
        mView.showDialog("loading...");
        if(!CommonUtils.isNetworkConnected()){
            online = false;
        }
        addSubscribe(Observable.concat(getLocalInOrderObservable(online),mDataManager.fetchAllIvnOrders(userId))
               .compose(RxUtils.rxSchedulerHelper())
               .compose(RxUtils.handleResult())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<List<ResultInventoryOrder>>() {
                    @Override
                    public void accept(List<ResultInventoryOrder> resultInventoryOrders) throws Exception {
                        if(resultInventoryOrders != null){
                            DbBank.getInstance().getResultInventoryOrderDao().insertItems(resultInventoryOrders);
                        }
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
                        if(resultInventoryOrders != null){
                            for (ResultInventoryOrder resultInventoryOrder : resultInventoryOrders) {
                                if (resultInventoryOrder.getOpt_status() != null && resultInventoryOrder.getOpt_status() == InvOperateStatus.MODIFIED_BUT_NOT_SUBMIT.getIndex()){
                                    locaLeftUpload = true;
                                }
                            }
                        }
                        mView.loadOrUpdateData(locaLeftUpload);
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



}
