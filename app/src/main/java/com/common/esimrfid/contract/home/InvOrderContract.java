package com.common.esimrfid.contract.home;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.InvOrder;

import java.util.List;

public interface InvOrderContract {
    interface View extends AbstractView {
        void loadInvOrders(List<InvOrder> invOrders);
    }

    interface Presenter extends AbstractPresenter<View> {
        void fetchAllIvnOrders();

        void downloadInvOrders(String orderId);
    }
}
