package com.common.esimrfid.contract.home;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;

import java.util.List;

public interface InvOrderContract {
    interface View extends AbstractView {
        void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders);

        void handleInvDetails(ResultInventoryDetail mInventoryDetail);

        void handelUploadResult(BaseResponse baseResponse);

        void handelFinishInvOrder(BaseResponse baseResponse);

        void handleNotInvAssetLeftStatus(Boolean isAllInved);
    }

    interface Presenter extends AbstractPresenter<View> {
        void fetchAllIvnOrders(String userId, boolean online);

        void fetchAllIvnOrdersPage(Integer size, Integer page, int currentSize, String userId, boolean online);

        void fetchAllInvDetails(String orderId, boolean online);

        void uploadLocalInvDetailState(String orderId, String uid);

        void finishLocalInvDetailStat(String orderId, String uid);

        void getNotInvAssetLeftStatus(String orderId);
    }
}
