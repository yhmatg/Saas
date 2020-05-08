package com.common.esimrfid.presenter.assetsearch;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

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
