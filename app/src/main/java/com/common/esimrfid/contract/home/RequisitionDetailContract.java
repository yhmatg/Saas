package com.common.esimrfid.contract.home;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.requisitionbeans.RequisitionAssetInfo;
import com.common.esimrfid.core.bean.nanhua.requisitionbeans.RequisitionDetailInfo;

import java.util.List;
import java.util.Set;

public interface RequisitionDetailContract {
    interface View extends AbstractView {
        void handleAssetsByEpcs(List<RequisitionAssetInfo> assets);

        void handleAssetsByAssetId(List<RequisitionAssetInfo> assets);

        void handleFinishedAssets(RequisitionDetailInfo requisitionDetailInfo);

        void handleUploadResAssets(BaseResponse result);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsByEpcs(Set<String> ecps);

        void getAssetByAssetId(String assetId);

        void getFinishedAssetDetaisl(String odrId);

        void uploadResAssets(String requestId, List<String> epcs);
    }
}
