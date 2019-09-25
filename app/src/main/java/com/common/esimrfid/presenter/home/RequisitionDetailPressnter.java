package com.common.esimrfid.presenter.home;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.RequisitionDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.requisitionbeans.RequisitionAssetInfo;
import com.common.esimrfid.core.bean.nanhua.requisitionbeans.RequisitionDetailInfo;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;
import java.util.Set;

public class RequisitionDetailPressnter extends BasePresenter<RequisitionDetailContract.View> implements RequisitionDetailContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public RequisitionDetailPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void getAssetsByEpcs(Set<String> ecps) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.getRequisitionInfons(ecps)
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleResult())
        .subscribeWith(new BaseObserver<List<RequisitionAssetInfo>>(mView,false) {
            @Override
            public void onNext(List<RequisitionAssetInfo> requisitionAssetInfos) {
                mView.dismissDialog();
                mView.handleAssetsByEpcs(requisitionAssetInfos);
            }
        }));
    }

    @Override
    public void getAssetByAssetId(String assetId) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.fetchRequestAssetsInfos(assetId)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<RequisitionAssetInfo>>(mView,false) {
                    @Override
                    public void onNext(List<RequisitionAssetInfo> requisitionAssetInfos) {
                        mView.dismissDialog();
                        mView.handleAssetsByAssetId(requisitionAssetInfos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissDialog();
                        ToastUtils.showShort(R.string.not_find_asset);
                    }
                }));
    }

    @Override
    public void getFinishedAssetDetaisl(String odrId) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.fetchRequsitionDetailByid(odrId)
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleResult())
        .subscribeWith(new BaseObserver<RequisitionDetailInfo>(mView,false) {
            @Override
            public void onNext(RequisitionDetailInfo requisitionDetailInfo) {
                mView.dismissDialog();
                mView.handleFinishedAssets(requisitionDetailInfo);
            }
        }));
    }

    @Override
    public void uploadResAssets(String requestId, List<String> epcs) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.uploadResAssets(requestId,epcs)
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleBaseResponse())
        .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
            @Override
            public void onNext(BaseResponse baseResponse) {
                mView.dismissDialog();
                mView.handleUploadResAssets(baseResponse);
            }
        }));
    }
}
