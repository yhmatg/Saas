package com.ddcommon.esimrfid.contract.assetsearch;

import com.ddcommon.esimrfid.base.presenter.AbstractPresenter;
import com.ddcommon.esimrfid.base.view.AbstractView;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;

import java.util.List;
import java.util.Set;

public interface AssetsSearchContract {
    interface View extends AbstractView {
        void handleScanAssets(List<AssetsInfo> assetsSearchInfos);//处理epc扫描到的标签
        void handleSearchAssets(List<AssetsInfo> searchInfos);//模糊查询资产
    }

    interface Presenter extends AbstractPresenter<View> {
        void getScanAssetsByEpc(Set<String> Epcs);//根据Epc得到资产详情
        void getSearchAssetsById(String assetsId);
    }
}
