package com.common.esimrfid.contract.distribute;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DistributeOrder;

import java.util.List;

public interface DistributeOrderContract {
    interface View extends AbstractView {
        void handleDistributeOrderPage(List<DistributeOrder> distributeOrders);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getDistributeOrderPage(String patternName, String conditions, Integer page, Integer size);
    }
}
