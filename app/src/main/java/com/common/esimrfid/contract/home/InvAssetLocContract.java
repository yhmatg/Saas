package com.common.esimrfid.contract.home;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;

import java.util.HashSet;
import java.util.List;

public interface InvAssetLocContract {
    interface View extends AbstractView {
        void handleInvDetails(List<InventoryDetail> inventoryDetails);
    }

    interface Presenter extends AbstractPresenter<View> {
        void fetchAllInvDetails(String orderId, String locId);

        void handleOneScanned(List<InventoryDetail> oneInvDetails, HashSet<String> oneMoreInvEpcs, String locId, String locName, String invId);

        void setOneLessAssetInv(InventoryDetail oneLessAsset);
    }
}
