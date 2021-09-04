package com.common.esimrfid.contract.settings;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;

public interface SettingConstract {
    interface View extends AbstractView {
        void showUpdateProgressDialog();

        void updateProgress(int progress,int total);

        void dismissProgressDialog();
    }

    interface Presenter extends AbstractPresenter<View> {

        void fetchLatestPageAssets(Integer size, Integer page);

        void clearAllData();

    }
}
