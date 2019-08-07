package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.FragCheckWaitingContract;
import com.common.esimrfid.core.DataManager;

public class FragCheckFinishedPressnter extends BasePresenter<FragCheckWaitingContract.View> implements FragCheckWaitingContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public FragCheckFinishedPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }


    @Override
    public void fetchAllIvnOrders(String userId) {
    }
}
