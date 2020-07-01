package com.common.esimrfid.contract.assetsearch;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;

import java.util.List;
import java.util.Set;

public interface AssetsSearchContract {
    interface View extends AbstractView {
        void handleSearchAssets(List<SearchAssetsInfo> searchInfos);
        void handGetAllAssetsForSearch(List<SearchAssetsInfo> searchInfos);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getSearchAssetsById(String param);
        //获取所有的资产（资产搜索查找使用）
        void getAllAssetsForSearch();
    }
}
