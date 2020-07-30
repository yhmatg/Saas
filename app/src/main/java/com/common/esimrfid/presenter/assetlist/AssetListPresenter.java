package com.common.esimrfid.presenter.assetlist;

import android.util.Log;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetlist.AssetListContract;
import com.common.esimrfid.contract.home.WriteTagContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListPage;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class AssetListPresenter extends BasePresenter<AssetListContract.View> implements AssetListContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "WriteTagPresenter";

    public AssetListPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    //分页
    @Override
    public void fetchPageAssetsInfos(Integer size, Integer page, String patternName, int currentSize) {
        mView.showDialog("loading...");
        //addSubscribe(mDataManager.fetchWriteAssetsInfo(assetsId)
        addSubscribe(Observable.concat(getLocalAssetsObservable(size, patternName, currentSize), mDataManager.fetchPageAssetsInfos(size, page, patternName))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<AssetsListPage>(mView, false) {
                    @Override
                    public void onNext(AssetsListPage assetsListPage) {
                        mView.dismissDialog();
                        if(assetsListPage.isLocal()){
                            mView.handlefetchPageAssetsInfos(assetsListPage.getList());
                        }else {
                            if(page <= assetsListPage.getPages()){
                                mView.handlefetchPageAssetsInfos(assetsListPage.getList());
                            }else {
                                mView.handlefetchPageAssetsInfos(new ArrayList<>());
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissDialog();
                        ToastUtils.showShort(R.string.not_find_asset);
                    }
                }));
    }

    public Observable<BaseResponse<AssetsListPage>> getLocalAssetsObservable(Integer size, String patternName, int currentSize) {
        Observable<BaseResponse<AssetsListPage>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<AssetsListPage>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<AssetsListPage>> emitter) throws Exception {
                List<AssetsInfo> newestOrders = DbBank.getInstance().getAssetsAllInfoDao().findPageLocalAssetsByPara(size, patternName, currentSize);
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                    Log.e(TAG, "network get data");
                } else {
                    Log.e(TAG, "newestOrders======" + newestOrders);
                    BaseResponse<AssetsListPage> invOrderResponse = new BaseResponse<>();
                    AssetsListPage assetsListPage = new AssetsListPage();
                    assetsListPage.setList(newestOrders);
                    invOrderResponse.setResult(assetsListPage);
                    assetsListPage.setLocal(true);
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
