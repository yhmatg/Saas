package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.HomeConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.update.UpdateVersion;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.HashMap;

public class HomePresenter extends BasePresenter<HomeConstract.View> implements HomeConstract.Presenter {
    private DataManager mDataManager;
    private String TAG = "HomePresenter";

    public HomePresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void getAssetsNmbDiffLocation() {
        addSubscribe(mDataManager.getAssetsNmbDiffLocation()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<HashMap<String, Integer>>(mView, false) {
                    @Override
                    public void onNext(HashMap<String, Integer> stringIntegerHashMap) {
                        mView.handleAssetsNmbDiffLocation(stringIntegerHashMap);
                    }
                }));
    }

    @Override
    public void getAssetsNmbDiffStatus() {
        addSubscribe(mDataManager.getAssetsNmbDiffStatus()
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
        addSubscribe(mDataManager.updateVersion()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<UpdateVersion>(mView, false) {
                    @Override
                    public void onNext(UpdateVersion updateInfo) {
                        mView.handelCheckoutVersion(updateInfo);
                    }
                }));
    }
}
