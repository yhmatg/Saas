package com.common.esimrfid.contract.assetrepair;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.xfxj.XfInventoryDetail;

import java.util.List;

public interface ChooseRepairContract {
    interface View extends AbstractView {
        void handlePageAssetsByOpt(List<AssetsInfo> assetsInfos);
        void handleAllAssetsByOpt(List<AssetsInfo> assetsInfos);
        void handleXfInvDetails(List<XfInventoryDetail> inventoryDetails);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAllAssetsByOpt(String optType,String patternName);
        void getAllAssetsByOpt(Integer size, Integer page,String optType,String patternName);
        void fetchXfAllInvDetails(String para);
    }
}
