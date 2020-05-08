package com.common.esimrfid.contract.home;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;

import java.util.List;

public interface WriteTagContract {
    interface View extends AbstractView {
        void handleAssetsById(List<AssetsInfo> assetsInfos);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsInfoById(String assetsId);
    }
}