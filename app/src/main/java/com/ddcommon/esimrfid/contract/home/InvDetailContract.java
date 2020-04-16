package com.ddcommon.esimrfid.contract.home;


import com.ddcommon.esimrfid.base.presenter.AbstractPresenter;
import com.ddcommon.esimrfid.base.view.AbstractView;
import com.ddcommon.esimrfid.core.bean.nanhua.BaseResponse;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;

import java.util.List;

public interface InvDetailContract {
    interface View extends AbstractView {
        void handleInvDetails(ResultInventoryDetail mInventoryDetail);

        void handelUploadResult(BaseResponse baseResponse);

        void handelFinishInvorder(BaseResponse baseResponse);

        void uploadInvDetails(List<InventoryDetail> inventoryDetails);
    }

    interface Presenter extends AbstractPresenter<View> {

        void fetchAllInvDetails( String orderId,boolean online);

        void upLoadInvDetails(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails, String uid);

        void findLocalInvDetailByInvid(String invId);

        void updateLocalInvDetailsState(String orderId,List<InventoryDetail> inventoryDetails);

        void updateLocalInvDetailState(String orderId,InventoryDetail inventoryDetail);
    }
}
