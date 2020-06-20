package com.common.esimrfid.contract.home;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;

import java.util.List;

public interface InvOrderContract {
    interface View extends AbstractView {
        void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders);

        void handleInvDetails(ResultInventoryDetail mInventoryDetail);

        void handelUploadResult(BaseResponse baseResponse);

        void handelFinishInvOrder(BaseResponse baseResponse);

        void handleNotInvAssetLeftStatus(List<InventoryDetail> resultInventoryOrders);
    }

    interface Presenter extends AbstractPresenter<View> {
        void  fetchAllIvnOrders( String userId,boolean online);

        void fetchAllInvDetails( String orderId,boolean online);

        void upLoadInvDetails(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails, String uid);

        void finishInvOrderWithAsset(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails, String uid);

        void uploadLocalInvDetailState(String orderId,String uid);

        void finishLocalInvDetailStat(String orderId,String uid);

        void getNotInvAssetLeftStatus(String orderId);
    }
}
