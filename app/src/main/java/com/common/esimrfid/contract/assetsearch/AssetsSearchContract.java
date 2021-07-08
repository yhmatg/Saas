package com.common.esimrfid.contract.assetsearch;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;

import java.util.List;

public interface AssetsSearchContract {
    interface View extends AbstractView {
        void handleSearchAssets(List<SearchAssetsInfo> searchInfos);
        void handGetAllAssetsForSearch(List<SearchAssetsInfo> searchInfos);
        void handleFetchPageAssetsInfos(List<SearchAssetsInfo> assetsInfos);
    }

    interface Presenter extends AbstractPresenter<View> {
        //获取所有的资产（资产搜索查找使用）
        void getAllAssetsForSearch();

        void fetchPageAssetsInfos(Integer size, String patternName,int currentSize);

        void fetchLatestPageAssets(Integer size, Integer page);
    }
}
