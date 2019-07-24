package com.common.esimrfid.contract.home;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.InvDetail;

import java.util.List;

public interface InvDetailContract {
    interface View extends AbstractView {
        void loadInvDetailsNet(List<InvDetail> invdetails);

        void finishSelf();

        void uploadSuccess();

        void initLocalCommitDetails(List<InvDetail> invdetails);

        void uploadLocalSucess();

    }

    interface Presenter extends AbstractPresenter<View> {

        void fetchAllInvDetails(String orderId);

        void uploadInvDetails(List<InvDetail> invDetails, String orderId);

        void finishInvOrder(String orderId);

        void getLocaCommitedDetails(String orderId);

    }
}
