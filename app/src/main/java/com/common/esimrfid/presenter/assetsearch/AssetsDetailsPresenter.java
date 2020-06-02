package com.common.esimrfid.presenter.assetsearch;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;
import java.util.Observable;

public class AssetsDetailsPresenter extends BasePresenter<AssetsDetailsContract.View> implements AssetsDetailsContract.Presenter {
    private DataManager mDataManager;
    public AssetsDetailsPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager=dataManager;
    }

    @Override
    public void getAssetsDetailsById(String astId,String astCode) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.fetchAssetsInfo(astId,astCode)
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

    @Override
    public void getAssetsResumeById(String astId,String astCode) {
        mView.showDialog("loading...");
       addSubscribe(mDataManager.fetchAssetResume(astId,astCode)
       .compose(RxUtils.rxSchedulerHelper())
       .compose(RxUtils.handleResult())
       .subscribeWith(new BaseObserver<List<AssetResume>>(mView, false) {
           @Override
           public void onNext(List<AssetResume> assetResumes) {
               mView.dismissDialog();
               mView.handleAssetsResume(assetResumes);
           }
       }));
    }

    @Override
    public void getAssetsRepairById(String astid,String astCode) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.fetchAssetRepair(astid,astCode)
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleResult())
        .subscribeWith(new BaseObserver<List<AssetRepair>>(mView, false) {
            @Override
            public void onNext(List<AssetRepair> assetRepairs) {
                mView.dismissDialog();
                mView.handleAssetsRepair(assetRepairs);
            }
        }));
    }

}
