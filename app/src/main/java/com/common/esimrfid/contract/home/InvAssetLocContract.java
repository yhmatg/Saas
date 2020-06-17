package com.common.esimrfid.contract.home;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;

import java.util.List;

public interface InvAssetLocContract {
    interface View extends AbstractView {
        void handleInvDetails(List<InventoryDetail> inventoryDetails);
    }

    interface Presenter extends AbstractPresenter<View> {
        void fetchAllInvDetails( String orderId,String locId);

    }
}
