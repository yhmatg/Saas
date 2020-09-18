package com.common.xfxj.contract.assetsearch;

import com.common.xfxj.base.presenter.AbstractPresenter;
import com.common.xfxj.base.view.AbstractView;
import com.common.xfxj.core.bean.assetdetail.AssetRepair;
import com.common.xfxj.core.bean.assetdetail.AssetResume;
import com.common.xfxj.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.xfxj.core.bean.nanhua.xfxj.XfInventoryDetail;

import java.util.List;

public interface AssetsDetailsContract {
    interface View extends AbstractView {
        void handleAssetsDetails(AssetsAllInfo assetsAllInfo);
        void handleAssetsResume(List<AssetResume> data);
        void handleAssetsRepair(List<AssetRepair> assetRepairs);
        void handleAssetsNoDetail();
        void handleSetOneAssetInved(Boolean result);
        void handleInvItemDetail(XfInventoryDetail invItemDetail);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsDetailsById(String astId,String astCode,String whereFrom);
        void getAssetsResumeById(String astId,String astCode);
        void getAssetsRepairById(String astid,String astCode);
        //手动将一个资产设置未已经盘点状态
        void setOneAssetInved(String sign,String invId, String locId, String astId);

        void getXAssetsDetailsById(String invId,String invItemId);
    }
}
