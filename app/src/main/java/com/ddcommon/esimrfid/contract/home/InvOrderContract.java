package com.ddcommon.esimrfid.contract.home;


import com.ddcommon.esimrfid.base.presenter.AbstractPresenter;
import com.ddcommon.esimrfid.base.view.AbstractView;
import com.ddcommon.esimrfid.core.bean.nanhua.BaseResponse;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;

import java.util.List;

public interface InvOrderContract {
    interface View extends AbstractView {
        void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders);

        void handleInvDetails(ResultInventoryDetail mInventoryDetail);

        void handelUploadResult(BaseResponse baseResponse);

        void handelFinishInvOrder(BaseResponse baseResponse);
    }

    interface Presenter extends AbstractPresenter<View> {
        void  fetchAllIvnOrders( String userId,boolean online);

        void fetchAllInvDetails( String orderId,boolean online);

        void upLoadInvDetails(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails, String uid);

        void finishInvOrderWithAsset(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails, String uid);

        void checkLocalDataState();
    }
}
