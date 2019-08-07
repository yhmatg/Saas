package com.common.esimrfid.contract.home;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;

public interface FragCheckFinishedContract {
    interface View extends AbstractView {
    }

    interface Presenter extends AbstractPresenter<View> {
        void  fetchAllIvnOrders(String userId);
    }
}
