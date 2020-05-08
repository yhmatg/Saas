package com.common.esimrfid.contract.assetsearch;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;

public interface AssetsDetailsContract {
    interface View extends AbstractView {
        void handleAssetsDetails(AssetsDetailsInfo assetsDetailsInfo);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsDetailsById(String astId);
    }
}
