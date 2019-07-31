package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.FragCheckFinishContract;
import com.common.esimrfid.core.DataManager;

public class FragCheckFinishPressnter extends BasePresenter<FragCheckFinishContract.View> implements FragCheckFinishContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public FragCheckFinishPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }


}
