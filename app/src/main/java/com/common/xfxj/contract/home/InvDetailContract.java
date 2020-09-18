package com.common.xfxj.contract.home;


import com.common.xfxj.base.presenter.AbstractPresenter;
import com.common.xfxj.base.view.AbstractView;
import com.common.xfxj.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.xfxj.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.xfxj.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.xfxj.core.bean.nanhua.xfxj.XfInventoryDetail;

import java.util.List;

public interface InvDetailContract {
    interface View extends AbstractView {
        void handleInvDetails(ResultInventoryDetail mInventoryDetail);

        void handelUploadResult(BaseResponse baseResponse);

        void handelFinishInvorder(BaseResponse baseResponse);

        void uploadInvDetails(List<InventoryDetail> inventoryDetails);

        void handleXfInvDetails(List<XfInventoryDetail> inventoryDetails);
    }

    interface Presenter extends AbstractPresenter<View> {

        void fetchAllInvDetails( String orderId,boolean online);

        void upLoadInvDetails(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails, String uid);

        void findLocalInvDetailByInvid(String invId);

        void updateLocalInvDetailsState(String orderId,List<InventoryDetail> inventoryDetails);

        void uploadLocalInvDetailState(String orderId,String uid);

        void fetchXfAllInvDetails(String orderId);
    }
}
