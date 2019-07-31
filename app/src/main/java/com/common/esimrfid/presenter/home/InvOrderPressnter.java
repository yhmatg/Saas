package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.DataManager;

public class InvOrderPressnter extends BasePresenter<InvOrderContract.View> implements InvOrderContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public InvOrderPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }


}
