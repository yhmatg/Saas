package com.common.esimrfid.contract.assetsearch;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;

import java.util.List;

public interface AssetsDetailsContract {
    interface View extends AbstractView {
        void handleAssetsDetails(AssetsDetailsInfo assetsDetailsInfo);
        void handleAssetsResume(List<AssetResume> data);
        void handleAssetsRepair(List<AssetRepair> assetRepairs);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsDetailsById(String astId,String astCode);
        void getAssetsResumeById(String astId,String astCode);
        void getAssetsRepairById(String astid,String astCode);
    }
}
