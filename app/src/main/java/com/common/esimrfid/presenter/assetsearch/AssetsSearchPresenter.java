package com.common.esimrfid.presenter.assetsearch;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetsearch.AssetsSearchContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        addSubscribe(dataManager.fetchScanAssets(mTempEpcs)
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
        addSubscribe(dataManager.fetchWriteAssetsInfo(assetsId)
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
}
