package com.common.esimrfid.contract.home;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.invdetailbeans.InventoryDetail;

import java.util.List;

public interface InvDetailContract {
    interface View extends AbstractView {
        void handleInvDetails(List<InventoryDetail> InvDetail);

        void handelUploadResult(BaseResponse baseResponse);

        void handelFinishInvorder(BaseResponse baseResponse);

        void uploadInvDetails(List<InventoryDetail> inventoryDetails);
    }

    interface Presenter extends AbstractPresenter<View> {

        void fetchAllInvDetails( String orderId,boolean online);

        void upLoadInvDetails(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails, String uid);

        void findLocalInvDetailByInvid(String invId);

        void finishInvOrder(String orderId,String uid, String remark);

        void updateLocalInvDetailsState(String orderId,List<InventoryDetail> inventoryDetails);

        void updateLocalInvDetailState(String orderId,InventoryDetail inventoryDetail);
    }
}
