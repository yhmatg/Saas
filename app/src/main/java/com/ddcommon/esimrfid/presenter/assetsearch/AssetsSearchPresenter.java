package com.ddcommon.esimrfid.presenter.assetsearch;

import com.ddcommon.esimrfid.R;
import com.ddcommon.esimrfid.base.presenter.BasePresenter;
import com.ddcommon.esimrfid.contract.assetsearch.AssetsSearchContract;
import com.ddcommon.esimrfid.core.DataManager;
import com.ddcommon.esimrfid.core.bean.nanhua.BaseResponse;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.ddcommon.esimrfid.core.room.DbBank;
import com.ddcommon.esimrfid.utils.CommonUtils;
import com.ddcommon.esimrfid.utils.RxUtils;
import com.ddcommon.esimrfid.utils.StringUtils;
import com.ddcommon.esimrfid.utils.ToastUtils;
import com.ddcommon.esimrfid.widget.BaseObserver;

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
                if(StringUtils.isEmpty(assetsId) && CommonUtils.isNetworkConnected()){
                    DbBank.getInstance().getAssetsinfoDao().deleteAllData();
                    DbBank.getInstance().getAssetsinfoDao().insertItems(assetsInfos);
                }
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
