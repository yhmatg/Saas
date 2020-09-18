package com.common.xfxj.contract.assetsearch;

import com.common.xfxj.base.presenter.AbstractPresenter;
import com.common.xfxj.base.view.AbstractView;
import com.common.xfxj.core.bean.nanhua.jsonbeans.SearchAssetsInfo;

import java.util.List;

public interface AssetsSearchContract {
    interface View extends AbstractView {
        void handleSearchAssets(List<SearchAssetsInfo> searchInfos);
        void handGetAllAssetsForSearch(List<SearchAssetsInfo> searchInfos);
        void handleFetchPageAssetsInfos(List<SearchAssetsInfo> assetsInfos);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getSearchAssetsById(String param);
        //获取所有的资产（资产搜索查找使用）
        void getAllAssetsForSearch();
        void fetchLatestAssets();
        void fetchPageAssetsInfos(Integer size, String patternName,int currentSize);
    }
}
