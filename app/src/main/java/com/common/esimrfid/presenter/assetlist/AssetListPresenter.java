package com.common.esimrfid.presenter.assetlist;

import android.util.Log;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetlist.AssetListContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;
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
        addSubscribe(Observable.concat(getLocalAssetsObservable(size, patternName, currentSize), mDataManager.fetchPageAssetsList(size, page, patternName))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<AssetsListPage>(mView, false) {
                    @Override
                    public void onNext(AssetsListPage assetsInfoPage) {
                        mView.dismissDialog();
                        if(assetsInfoPage.isLocal()){
                            mView.handlefetchPageAssetsInfos(assetsInfoPage.getList());
                        }else {
                            if(page <= assetsInfoPage.getPages()){
                                mView.handlefetchPageAssetsInfos(assetsInfoPage.getList());
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
                List<AssetsListItemInfo> assetList = DbBank.getInstance().getAssetsAllInfoDao().searchPageLocalAssetListByPara(size, patternName, currentSize);
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                } else {
                    BaseResponse<AssetsListPage> invOrderResponse = new BaseResponse<>();
                    AssetsListPage assetsInfoPage = new AssetsListPage();
                    assetsInfoPage.setList(assetList);
                    invOrderResponse.setResult(assetsInfoPage);
                    assetsInfoPage.setLocal(true);
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
