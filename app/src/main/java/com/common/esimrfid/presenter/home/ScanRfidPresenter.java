package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.ScanFridContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScanRfidPresenter extends BasePresenter<ScanFridContract.View> implements ScanFridContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public ScanRfidPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void fetchScanAssetsInfons(Set<String> ecps) {
        mView.showDialog("loading");
        //防止并发修改异常ConcurrentModificationException
        Set<String> mTempEpcs = new HashSet<>();
        mTempEpcs.addAll(ecps);
        addSubscribe(mDataManager.fetchScanAssetsInfons(mTempEpcs)
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleResult())
        .subscribeWith(new BaseObserver<List<AssetsInfo>>(mView, false) {
            @Override
            public void onNext(List<AssetsInfo> assetsInfos) {
                mView.dismissDialog();
                mView.handleAssetsInfons(assetsInfos);
            }
        }));
    }
}
