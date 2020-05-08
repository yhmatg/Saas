package com.common.esimrfid.presenter.home;

import android.util.Log;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.HomeConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.home.CompanyInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.bean.update.UpdateVersion;
import com.common.esimrfid.core.http.exception.ResultIsNullException;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter extends BasePresenter<HomeConstract.View> implements HomeConstract.Presenter {
    private DataManager mDataManager;
    private String TAG = "HomePresenter";

    public HomePresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void getAssetsNmbDiffLocation() {
        addSubscribe(mDataManager.getAssetsNmbDiffLocation()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<HashMap<String, Integer>>(mView, false) {
                    @Override
                    public void onNext(HashMap<String, Integer> stringIntegerHashMap) {
                        mView.handleAssetsNmbDiffLocation(stringIntegerHashMap);
                    }
                }));
    }

    @Override
    public void getAssetsNmbDiffStatus() {
        addSubscribe(mDataManager.getAssetsNmbDiffStatus()
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleResult())
        .subscribeWith(new BaseObserver<AssetStatusNum>(mView, false) {
            @Override
            public void onNext(AssetStatusNum assetStatusNum) {
                mView.handleAssetsNmbDiffStatus(assetStatusNum);
            }
        }));
    }

    @Override
    public void checkUpdateVersion() {
        addSubscribe(mDataManager.updateVersion()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<UpdateVersion>(mView, false) {
                    @Override
                    public void onNext(UpdateVersion updateInfo) {
                        mView.handelCheckoutVersion(updateInfo);
                    }
                }));
    }

    @Override
    public void getCompanyInfo() {
        addSubscribe(mDataManager.getCompanyInfo()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<CompanyInfo>(mView, false) {
                    @Override
                    public void onNext(CompanyInfo companyInfo) {
                        mView.handleGetCompanyInfo(companyInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!(e instanceof ResultIsNullException)) {
                            super.onError(e);
                        }
                    }
                }));
    }

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

    @Override
    public void getAssetsInfoById(String assetsId) {
        mView.showDialog("loading...");
        //addSubscribe(mDataManager.fetchWriteAssetsInfo(assetsId)
        addSubscribe(Observable.concat(getLocalAssetsObservable(assetsId),mDataManager.fetchWriteAssetsInfo(assetsId))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<AssetsInfo>>(mView, false) {
                    @Override
                    public void onNext(List<AssetsInfo> assetsInfos) {
                        if(StringUtils.isEmpty(assetsId) && CommonUtils.isNetworkConnected()){
                            DbBank.getInstance().getAssetsinfoDao().deleteAllData();
                            DbBank.getInstance().getAssetsinfoDao().insertItems(assetsInfos);
                        }
                        mView.dismissDialog();
                    }
                    @Override
                    public void onError(Throwable e){
                        mView.dismissDialog();
                    }
                }));
    }

    public Observable<BaseResponse<List<AssetsInfo>>> getLocalAssetsObservable(String para) {
        Observable<BaseResponse<List<AssetsInfo>>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<List<AssetsInfo>>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<List<AssetsInfo>>> emitter) throws Exception {
                List<AssetsInfo> newestOrders = DbBank.getInstance().getAssetsinfoDao().findLocalAssetsByPara(para);
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                    Log.e(TAG, "network get data");
                } else {
                    Log.e(TAG, "newestOrders======" + newestOrders);
                    BaseResponse<List<AssetsInfo>> invOrderResponse = new BaseResponse<>();
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
