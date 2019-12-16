package com.common.esimrfid.contract.home;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;

import java.util.HashMap;

public interface HomeConstract {
    interface View extends AbstractView {
        void handleAssetsNmbDiffLocation(HashMap<String,Integer> assetLocations);

        void handleAssetsNmbDiffStatus(AssetStatusNum assetStatus);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsNmbDiffLocation();

        void getAssetsNmbDiffStatus();
    }
}
