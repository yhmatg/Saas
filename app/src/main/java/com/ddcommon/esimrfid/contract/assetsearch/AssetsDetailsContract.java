package com.ddcommon.esimrfid.contract.assetsearch;

import com.ddcommon.esimrfid.base.presenter.AbstractPresenter;
import com.ddcommon.esimrfid.base.view.AbstractView;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;

public interface AssetsDetailsContract {
    interface View extends AbstractView {
        void handleAssetsDetails(AssetsDetailsInfo assetsDetailsInfo);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsDetailsById(String astId);
    }
}
