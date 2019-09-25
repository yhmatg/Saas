package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.RequisitionContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.requisitionbeans.RequisitionItemInfo;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;

public class RequisitionPressnter extends BasePresenter<RequisitionContract.View> implements RequisitionContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public RequisitionPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void fetchAllRequisitions() {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.fetchAllRequisitions()
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleResult())
        .subscribeWith(new BaseObserver<List<RequisitionItemInfo>>(mView,false) {
            @Override
            public void onNext(List<RequisitionItemInfo> requisitionItemInfos) {
                mView.dismissDialog();
                mView.handleRequisitionsItems(requisitionItemInfos);
            }
        }));
    }
}
