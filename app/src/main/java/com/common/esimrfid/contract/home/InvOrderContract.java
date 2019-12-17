package com.common.esimrfid.contract.home;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;

import java.util.List;

public interface InvOrderContract {
    interface View extends AbstractView {
        void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders);
    }

    interface Presenter extends AbstractPresenter<View> {
        void  fetchAllIvnOrders( String userId,boolean online);

        void checkLocalDataState();
    }
}
