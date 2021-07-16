package com.common.esimrfid.contract.home;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;

public interface InvDetailContract {
    interface View extends AbstractView {
        void handleInvDetails(ResultInventoryDetail mInventoryDetail);

        void handelUploadResult(BaseResponse baseResponse);
    }

    interface Presenter extends AbstractPresenter<View> {

        void fetchAllInvDetails( String orderId,boolean online);

        void uploadLocalInvDetailState(String orderId,String uid);
    }
}
