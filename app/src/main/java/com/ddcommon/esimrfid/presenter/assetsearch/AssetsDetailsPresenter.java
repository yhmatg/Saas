package com.ddcommon.esimrfid.presenter.assetsearch;

import com.ddcommon.esimrfid.base.presenter.BasePresenter;
import com.ddcommon.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.ddcommon.esimrfid.core.DataManager;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;
import com.ddcommon.esimrfid.utils.RxUtils;
import com.ddcommon.esimrfid.widget.BaseObserver;

public class AssetsDetailsPresenter extends BasePresenter<AssetsDetailsContract.View> implements AssetsDetailsContract.Presenter {
    private DataManager mDataManager;
    public AssetsDetailsPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager=dataManager;
    }

    @Override
    public void getAssetsDetailsById(String astId) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.fetchAssetsInfo(astId)
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleResult())
        .subscribeWith(new BaseObserver<AssetsDetailsInfo>(mView, false) {
            @Override
            public void onNext(AssetsDetailsInfo assetsDetailsInfo) {
                mView.dismissDialog();
                mView.handleAssetsDetails(assetsDetailsInfo);
            }
        }));
    }
}
