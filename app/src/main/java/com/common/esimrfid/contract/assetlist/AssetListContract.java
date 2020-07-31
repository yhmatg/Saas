package com.common.esimrfid.contract.assetlist;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;

import java.util.List;

public interface AssetListContract {
    interface View extends AbstractView {
        void handlefetchPageAssetsInfos(List<AssetsListItemInfo> assetsInfos);
    }

    interface Presenter extends AbstractPresenter<View> {
        void fetchPageAssetsInfos(Integer size, Integer page, String patternName, int currentSize);
    }
}
