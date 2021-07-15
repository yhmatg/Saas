package com.common.esimrfid.presenter.home;

import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.HomeConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.inventorytask.TitleAndLogoResult;
import com.common.esimrfid.core.bean.nanhua.home.AssetLocNmu;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DataAuthority;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyPageAssets;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
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
        addSubscribe(DataManager.getInstance().getAssetsNmbDiffLocation()
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
                .doOnNext(new Consumer<List<ResultInventoryOrder>>() {
                    @Override
                    public void accept(List<ResultInventoryOrder> resultInventoryOrders) throws Exception {
                        //2.4.5版本盘点单列表没有已盘点，未盘点数量，统一使用服务端的盘点单列表数据据
                        List<ResultInventoryOrder> localOrders = DbBank.getInstance().getResultInventoryOrderDao().findInvOrders();
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

    private Observable<BaseResponse<List<ResultInventoryOrder>>> getLocalInOrderObservable(final boolean online) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<List<ResultInventoryOrder>>>() {
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
                            if (dataAuthority.getAuth_type_scope().getGeneral().size() == 0) {
                                dataAuthority.getAuth_type_scope().getGeneral().add("allData");
                            }
                            if (dataAuthority.getAuth_loc_scope().getGeneral().size() == 0) {
                                dataAuthority.getAuth_loc_scope().getGeneral().add("allData");
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
       /* if (page == 1) {
            if(mView != null){
                mView.showDialog("loading...");
            }
        }*/
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().fetchLatestAssetsPage(DataManager.getInstance().getLatestSyncTime(), size, page)
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
                                DataManager.getInstance().setLatestSyncTime(String.valueOf(System.currentTimeMillis() - 60000));
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<LatestModifyPageAssets>(mView, false) {
                        @Override
                        public void onNext(LatestModifyPageAssets latestModifyPageAssets) {
                            /*int pageNum = latestModifyPageAssets.getPageNum();
                            int pages = latestModifyPageAssets.getPages();
                            if (pageNum + 1 > pages) {
                                if(mView != null){
                                    mView.dismissDialog();
                                }
                            }*/
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            /*if(mView != null){
                                mView.dismissDialog();
                            }*/
                        }
                    }));
        }
    }

    @Override
    public void login(UserInfo userInfo) {
        final String passWord = userInfo.getUser_password();
        final String userName = userInfo.getUser_name();
        //userInfo.setUser_password(Md5Util.getMD5(passWord));
        addSubscribe(DataManager.getInstance().login(userInfo)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<UserLoginResponse>() {
                    @Override
                    public void accept(UserLoginResponse userLoginResponse) throws Exception {
                        //保存UserLoginResponse到sp
                        //不同管理员分配的盘点任务不一样，盘点相关的数据需要清除
                        UserLoginResponse localUserLogin = DataManager.getInstance().getUserLoginResponse();
                        if (localUserLogin != null && !userLoginResponse.getUserinfo().getId().equals(localUserLogin.getUserinfo().getId())) {
                            DbBank.getInstance().getAssetsAllInfoDao().deleteAllData();
                            DbBank.getInstance().getInventoryDetailDao().deleteAllData();
                            DbBank.getInstance().getResultInventoryOrderDao().deleteAllData();
                            DataManager.getInstance().setLatestSyncTime("0");
                        }
                        DataManager.getInstance().setUserLoginResponse(userLoginResponse);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<UserLoginResponse>(mView,
                        false) {
                    @Override
                    public void onNext(UserLoginResponse userLoginResponse) {
                        //取消提示框
                        mView.dismissDialog();
                        setLoginAccount(userInfo.getUser_name());
                        setLoginPassword(passWord);
                        setToken(userLoginResponse.getToken());
                        DataManager.getInstance().setLoginStatus(true);
                        DataManager.getInstance().setOnline(true);
                        mView.handleLogin();
                    }

                    @Override
                    protected void onStart() {
                        super.onStart();
                        //显示提示框
                        mView.showDialog("请稍等...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //取消提示框
                        mView.dismissDialog();
                    }
                }));
    }

    @Override
    public void getTitleAndLogo(String tenantid, String configKey) {
        addSubscribe(DataManager.getInstance().getTitleAndLogo(tenantid, configKey)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<BaseResponse<TitleAndLogoResult>>(mView, false) {

                    @Override
                    public void onNext(BaseResponse<TitleAndLogoResult> titleAndLogoResponse) {
                        TitleAndLogoResult result = titleAndLogoResponse.getResult();
                        mView.handleTitleAndLogo(result);
                    }
                }));

    }
}
