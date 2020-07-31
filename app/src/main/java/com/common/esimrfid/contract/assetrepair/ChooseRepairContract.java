package com.common.esimrfid.contract.assetrepair;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;

import java.util.List;

public interface ChooseRepairContract {
    interface View extends AbstractView {
        void handleAssetsById(List<AssetsInfo> assetsInfos);
        void handleAllAssetsByOpt(List<AssetsInfo> assetsInfos);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsInfoById(String assetsId);
        void getAllAssetsByOpt(String optType,String patternName);
    }
}
