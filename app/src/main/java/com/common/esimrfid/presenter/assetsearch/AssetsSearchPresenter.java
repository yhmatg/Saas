package com.common.esimrfid.presenter.assetsearch;

import android.util.Log;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetsearch.AssetsSearchContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyAssets;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;
import com.common.esimrfid.core.dao.AssetsAllInfoDao;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class AssetsSearchPresenter extends BasePresenter<AssetsSearchContract.View> implements AssetsSearchContract.Presenter {
    private DataManager dataManager;
    public AssetsSearchPresenter(DataManager dataManager) {
        super(dataManager);
        this.dataManager=dataManager;
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
            addSubscribe(dataManager.fetchLatestAssets(dataManager.getLatestSyncTime())
                    .compose(RxUtils.rxSchedulerHelper())
                    .compose(RxUtils.handleResult())
                    .subscribeWith(new BaseObserver<LatestModifyAssets>(mView, false) {
                        @Override
                        public void onNext(LatestModifyAssets latestModifyAssets) {
                            AssetsAllInfoDao assetsAllInfoDao = DbBank.getInstance().getAssetsAllInfoDao();
                            if(latestModifyAssets.getModified() != null && latestModifyAssets.getModified().size() > 0){
                                assetsAllInfoDao.insertItems(latestModifyAssets.getModified());
                            }
                            if(latestModifyAssets.getRemoved()!= null && latestModifyAssets.getRemoved().size() > 0){
                                assetsAllInfoDao.deleteItems(latestModifyAssets.getRemoved());
                            }
                            dataManager.setLatestSyncTime(String.valueOf(System.currentTimeMillis()));
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
                List<SearchAssetsInfo> allAssetEpcs = DbBank.getInstance().getAssetsAllInfoDao().searchLocalAssetsByPara(param);
                emitter.onNext(allAssetEpcs);
            }
        });
        return baseResponseObservable;
    }


    public Observable<List<SearchAssetsInfo>> getLocalPageAssetsObservable(Integer size, String patternName, int currentSize) {
        Observable<List<SearchAssetsInfo>> invOrderObservable = Observable.create(new ObservableOnSubscribe<List<SearchAssetsInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchAssetsInfo>> emitter) throws Exception {
                List<SearchAssetsInfo> newestOrders = DbBank.getInstance().getAssetsAllInfoDao().searchPageLocalAssetsByPara(size, patternName, currentSize);
                emitter.onNext(newestOrders);
            }
        });
        return invOrderObservable;
    }
}
