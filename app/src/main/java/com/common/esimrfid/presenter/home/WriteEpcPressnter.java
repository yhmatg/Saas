package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.WriteEpcContract;
import com.common.esimrfid.core.DataManager;

public class WriteEpcPressnter extends BasePresenter<WriteEpcContract.View> implements WriteEpcContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public WriteEpcPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

}
