package com.common.esimrfid.presenter.home;

import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.HomeConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.home.AssetLocNmu;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DataAuthority;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyAssets;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyPageAssets;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.bean.update.UpdateVersion;
import com.common.esimrfid.core.dao.AssetsAllInfoDao;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter extends BasePresenter<HomeConstract.View> implements HomeConstract.Presenter {
    private String TAG = "HomePresenter";

    public HomePresenter() {
        super();
    }

    @Override
    public void getAssetsNmbDiffLocation() {
        addSubscribe(DataManager.getInstance().getAssetsNmbInDiffLocation()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<AssetLocNmu>>(mView, false) {
                    @Override
                    public void onNext(List<AssetLocNmu> assetLocNmus) {
                        mView.handleAssetsNmbDiffLocation(assetLocNmus);
                    }
                }));
    }

    @Override
    public void getAssetsNmbDiffStatus() {
        addSubscribe(DataManager.getInstance().getAssetsNmbDiffStatus()
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
        addSubscribe(DataManager.getInstance().updateVersion()
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
    public void fetchAllIvnOrders(String userId, boolean online) {
        //mView.showDialog("loading...");
        if (!CommonUtils.isNetworkConnected()) {
            online = false;
        }
        addSubscribe(Observable.concat(getLocalInOrderObservable(online), DataManager.getInstance().fetchAllIvnOrders(userId))
                .compose(RxUtils.handleResult())
                .subscribeOn(Schedulers.io())
                /*.flatMap(new Function<List<ResultInventoryOrder>, ObservableSource<List<ResultInventoryOrder>>>() {
                    @Override
                    public ObservableSource<List<ResultInventoryOrder>> apply(List<ResultInventoryOrder> resultInventoryOrders) throws Exception {
                        ArrayList<String> unInvedRemoteOrders = new ArrayList<>();
                        for (ResultInventoryOrder resultInventoryOrder : resultInventoryOrders) {
                            if (resultInventoryOrder.getInv_status() == 10) {
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
                })*/
                .doOnNext(new Consumer<List<ResultInventoryOrder>>() {
                    @Override
                    public void accept(List<ResultInventoryOrder> resultInventoryOrders) throws Exception {
                        ArrayList<String> unInvedRemoteOrders = new ArrayList<>();
                        for (ResultInventoryOrder resultInventoryOrder : resultInventoryOrders) {
                            if (resultInventoryOrder.getInv_status() == 10) {
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
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<List<ResultInventoryOrder>>(mView, false) {
                    @Override
                    public void onNext(List<ResultInventoryOrder> resultInventoryOrders) {
                        //mView.dismissDialog();
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
                } else {
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
    public void fetchLatestAssets() {
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().fetchLatestAssets(DataManager.getInstance().getLatestSyncTime())
                    .compose(RxUtils.handleResult())
                    .subscribeOn(Schedulers.io())
                    .doOnNext(new Consumer<LatestModifyAssets>() {
                        @Override
                        public void accept(LatestModifyAssets latestModifyAssets) throws Exception {
                            AssetsAllInfoDao assetsAllInfoDao = DbBank.getInstance().getAssetsAllInfoDao();
                            if (latestModifyAssets.getModified() != null && latestModifyAssets.getModified().size() > 0) {
                                assetsAllInfoDao.insertItems(latestModifyAssets.getModified());
                            }
                            if (latestModifyAssets.getRemoved() != null && latestModifyAssets.getRemoved().size() > 0) {
                                assetsAllInfoDao.deleteItems(latestModifyAssets.getRemoved());
                            }
                            DataManager.getInstance().setLatestSyncTime(String.valueOf(System.currentTimeMillis() - 600000));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<LatestModifyAssets>(mView, false) {
                        @Override
                        public void onNext(LatestModifyAssets latestModifyAssets) {

                        }
                    }));
        }
    }

    //获取管理员权限范围
    @Override
    public void getDataAuthority(String id) {
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().getDataAuthority(id)
                    .compose(RxUtils.rxSchedulerHelper())
                    .compose(RxUtils.handleResult())
                    .subscribeWith(new BaseObserver<DataAuthority>(mView, false) {
                        @Override
                        public void onNext(DataAuthority dataAuthority) {
                            if (dataAuthority.getAuth_corp_scope().size() == 0) {
                                dataAuthority.getAuth_corp_scope().add("allData");
                            }
                            if (dataAuthority.getAuth_dept_scope().size() == 0) {
                                dataAuthority.getAuth_dept_scope().add("allData");
                            }
                            if (dataAuthority.getAuth_type_scope().size() == 0) {
                                dataAuthority.getAuth_type_scope().add("allData");
                            }
                            if (dataAuthority.getAuth_loc_scope().size() == 0) {
                                dataAuthority.getAuth_loc_scope().add("allData");
                            }
                            DataManager.getInstance().setDataAuthority(dataAuthority);
                            EsimAndroidApp.setDataAuthority(dataAuthority);
                        }
                    }));
        } else {
            DataAuthority dataAuthoritye = DataManager.getInstance().getDataAuthority();
            EsimAndroidApp.setDataAuthority(dataAuthoritye);
        }
    }

    @Override
    public void fetchLatestPageAssets(Integer size, Integer page) {
        if (page == 1) {
            mView.showDialog("loading...");
        }
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().fetchLatestAssetsPage(DataManager.getInstance().getLatestSyncTime(),size,page)
                    .compose(RxUtils.handleResult())
                    .subscribeOn(Schedulers.io())
                    .doOnNext(new Consumer<LatestModifyPageAssets>() {
                        @Override
                        public void accept(LatestModifyPageAssets latestModifyPageAssets) throws Exception {
                            int pageNum = latestModifyPageAssets.getPageNum();
                            int pages = latestModifyPageAssets.getPages();
                            AssetsAllInfoDao assetsAllInfoDao = DbBank.getInstance().getAssetsAllInfoDao();
                            if (latestModifyPageAssets.getModified() != null && latestModifyPageAssets.getModified().size() > 0) {
                                assetsAllInfoDao.insertItems(latestModifyPageAssets.getModified());
                            }
                            if (latestModifyPageAssets.getRemoved() != null && latestModifyPageAssets.getRemoved().size() > 0) {
                                assetsAllInfoDao.deleteItems(latestModifyPageAssets.getRemoved());
                            }
                            if (pageNum + 1 <= pages) {
                                fetchLatestPageAssets(size, pageNum + 1);
                            } else {
                                DataManager.getInstance().setLatestSyncTime(String.valueOf(System.currentTimeMillis() - 600000));
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<LatestModifyPageAssets>(mView, false) {
                        @Override
                        public void onNext(LatestModifyPageAssets latestModifyPageAssets) {
                            int pageNum = latestModifyPageAssets.getPageNum();
                            int pages = latestModifyPageAssets.getPages();
                            if (pageNum + 1 > pages) {
                                mView.dismissDialog();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mView.dismissDialog();
                        }
                    }));
        }
    }
}
