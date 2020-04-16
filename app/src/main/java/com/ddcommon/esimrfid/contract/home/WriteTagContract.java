package com.ddcommon.esimrfid.contract.home;

import com.ddcommon.esimrfid.base.presenter.AbstractPresenter;
import com.ddcommon.esimrfid.base.view.AbstractView;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;

import java.util.List;

public interface WriteTagContract {
    interface View extends AbstractView {
        void handleAssetsById(List<AssetsInfo> assetsInfos);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsInfoById(String assetsId);
    }
}
