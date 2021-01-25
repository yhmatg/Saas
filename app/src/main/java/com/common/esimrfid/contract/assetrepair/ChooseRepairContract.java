package com.common.esimrfid.contract.assetrepair;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;

import java.util.List;

public interface ChooseRepairContract {
    interface View extends AbstractView {
        void handlePageAssetsByOpt(List<AssetsListItemInfo> assetsInfos);
    }

    interface Presenter extends AbstractPresenter<View> {
        void fetchPageAssetsInfos(Integer size, Integer page, String patternName, String userRealName, String conditions);
    }
}
