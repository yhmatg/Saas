package com.common.esimrfid.presenter.assetsearch;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetsearch.AssetsSearchContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
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
    public void getScanAssetsByEpc(Set<String> Epcs) {
        mView.showDialog("loading...");
        Set<String> mTempEpcs = new HashSet<>();
        mTempEpcs.addAll(Epcs);
        //addSubscribe(dataManager.fetchScanAssets(mTempEpcs)
        addSubscribe(Observable.concat(getLocalAssetsObservable(mTempEpcs),dataManager.fetchScanAssets(mTempEpcs))
        .compose(RxUtils.handleResult())
        .compose(RxUtils.rxSchedulerHelper())
        .subscribeWith(new BaseObserver<List<AssetsInfo>>(mView, false) {
            @Override
            public void onNext(List<AssetsInfo> assetsInfos) {
                mView.dismissDialog();
                mView.handleScanAssets(assetsInfos);
            }
            @Override
            public void onError(Throwable e){
                mView.dismissDialog();
                ToastUtils.showShort(R.string.not_get_epc);
            }
        }));
    }

    @Override
    public void getSearchAssetsById(String assetsId) {
        mView.showDialog("loading...");
        //addSubscribe(dataManager.fetchWriteAssetsInfo(assetsId)
        addSubscribe(Observable.concat(getLocalAssetsObservable(assetsId),dataManager.fetchWriteAssetsInfo(assetsId))
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleResult())
        .subscribeWith(new BaseObserver<List<AssetsInfo>>(mView, false) {
            @Override
            public void onNext(List<AssetsInfo> assetsInfos) {
                mView.dismissDialog();
                mView.handleSearchAssets(assetsInfos);
            }
//            @Override
//            public void onError(Throwable e){
//                mView.dismissDialog();
//                ToastUtils.showShort(R.string.not_find_asset);
//            }
        }));
    }

    public Observable<BaseResponse<List<AssetsInfo>>> getLocalAssetsObservable(String para) {
        Observable<BaseResponse<List<AssetsInfo>>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<List<AssetsInfo>>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<List<AssetsInfo>>> emitter) throws Exception {
                List<AssetsInfo> newestOrders = DbBank.getInstance().getAssetsinfoDao().findLocalAssetsByPara(para);
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                } else {
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

    public Observable<BaseResponse<List<AssetsInfo>>> getLocalAssetsObservable(Set<String> epcs) {
        Observable<BaseResponse<List<AssetsInfo>>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<List<AssetsInfo>>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<List<AssetsInfo>>> emitter) throws Exception {
                List<AssetsInfo> newestOrders = DbBank.getInstance().getAssetsinfoDao().findLocalAssetsByEpcs(epcs);
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                } else {
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
