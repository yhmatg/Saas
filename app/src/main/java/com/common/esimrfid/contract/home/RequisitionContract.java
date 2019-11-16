package com.common.esimrfid.contract.home;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionItemInfo;

import java.util.List;

public interface RequisitionContract {
    interface View extends AbstractView {
        void handleRequisitionsItems(List<RequisitionItemInfo> requisitionItemInfos);
    }

    interface Presenter extends AbstractPresenter<View> {
        void fetchAllRequisitions();
    }
}
