package com.common.esimrfid.presenter.assetsearch;

import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetsearch.AssetsSearchContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyAssets;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyPageAssets;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;
import com.common.esimrfid.core.dao.AssetsAllInfoDao;
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

public class AssetsSearchPresenter extends BasePresenter<AssetsSearchContract.View> implements AssetsSearchContract.Presenter {
    
    public AssetsSearchPresenter() {
        super();
    }

    @Override
    public void getSearchAssetsById(String assetsId) {
        addSubscribe(getSearchLocalAssetsObservable(assetsId)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<List<SearchAssetsInfo>>(mView,false) {
                    @Override
                    public void onNext(List<SearchAssetsInfo> searchAssets) {
                        mView.handleSearchAssets(searchAssets);
                    }
                }));
    }

    @Override
    public void getAllAssetsForSearch() {
        addSubscribe(getLocalAssetsEpcsObservable()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<List<SearchAssetsInfo>>(mView,false) {
                    @Override
                    public void onNext(List<SearchAssetsInfo> searchAssets) {
                        mView.handGetAllAssetsForSearch(searchAssets);
                    }
                }));
    }

    @Override
    public void fetchLatestAssets() {
        if(CommonUtils.isNetworkConnected()){
            addSubscribe(DataManager.getInstance().fetchLatestAssets(DataManager.getInstance().getLatestSyncTime())
                    .compose(RxUtils.handleResult())
                    .subscribeOn(Schedulers.io())
                    .doOnNext(new Consumer<LatestModifyAssets>() {
                        @Override
                        public void accept(LatestModifyAssets latestModifyAssets) throws Exception {
                            AssetsAllInfoDao assetsAllInfoDao = DbBank.getInstance().getAssetsAllInfoDao();
                            if(latestModifyAssets.getModified() != null && latestModifyAssets.getModified().size() > 0){
                                assetsAllInfoDao.insertItems(latestModifyAssets.getModified());
                            }
                            if(latestModifyAssets.getRemoved()!= null && latestModifyAssets.getRemoved().size() > 0){
                                assetsAllInfoDao.deleteItems(latestModifyAssets.getRemoved());
                            }
                            DataManager.getInstance().setLatestSyncTime(String.valueOf(System.currentTimeMillis() - 60000));
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

    //分页查询资产
    @Override
    public void fetchPageAssetsInfos(Integer size, String patternName, int currentSize) {
        addSubscribe(getLocalPageAssetsObservable(size, patternName, currentSize)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<List<SearchAssetsInfo>>(mView,false) {
                    @Override
                    public void onNext(List<SearchAssetsInfo> searchAssets) {
                        mView.handleFetchPageAssetsInfos(searchAssets);
                    }
                }));
    }

    //获取本地所有资产epc
    public Observable<List<SearchAssetsInfo>> getLocalAssetsEpcsObservable() {
        Observable<List<SearchAssetsInfo>> baseResponseObservable = Observable.create(new ObservableOnSubscribe<List<SearchAssetsInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchAssetsInfo>> emitter) throws Exception {
                List<SearchAssetsInfo> allAssetEpcs = DbBank.getInstance().getAssetsAllInfoDao().getAllAssetForSearch();
                emitter.onNext(allAssetEpcs);
            }
        });
        return baseResponseObservable;
    }


    //模糊搜索本地资产
    public Observable<List<SearchAssetsInfo>> getSearchLocalAssetsObservable(String param) {
        Observable<List<SearchAssetsInfo>> baseResponseObservable = Observable.create(new ObservableOnSubscribe<List<SearchAssetsInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchAssetsInfo>> emitter) throws Exception {
                List<SearchAssetsInfo> allAssetEpcs = DbBank.getInstance().getAssetsAllInfoDao().searchLocalAssetsByPara(param,EsimAndroidApp.getDataAuthority().getAuth_corp_scope(),EsimAndroidApp.getDataAuthority().getAuth_dept_scope(),EsimAndroidApp.getDataAuthority().getAuth_type_scope(),EsimAndroidApp.getDataAuthority().getAuth_loc_scope());
                emitter.onNext(allAssetEpcs);
            }
        });
        return baseResponseObservable;
    }


    public Observable<List<SearchAssetsInfo>> getLocalPageAssetsObservable(Integer size, String patternName, int currentSize) {
        Observable<List<SearchAssetsInfo>> invOrderObservable = Observable.create(new ObservableOnSubscribe<List<SearchAssetsInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchAssetsInfo>> emitter) throws Exception {
                List<SearchAssetsInfo> newestOrders = DbBank.getInstance().getAssetsAllInfoDao().searchPageLocalAssetsByPara(size, patternName, currentSize,EsimAndroidApp.getDataAuthority().getAuth_corp_scope(),EsimAndroidApp.getDataAuthority().getAuth_dept_scope(),EsimAndroidApp.getDataAuthority().getAuth_type_scope(),EsimAndroidApp.getDataAuthority().getAuth_loc_scope());
                emitter.onNext(newestOrders);
            }
        });
        return invOrderObservable;
    }

    @Override
    public void fetchLatestPageAssets(Integer size, Integer page) {
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
                                DataManager.getInstance().setLatestSyncTime(String.valueOf(System.currentTimeMillis() - 60000));
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<LatestModifyPageAssets>(mView, false) {
                        @Override
                        public void onNext(LatestModifyPageAssets latestModifyPageAssets) {
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
