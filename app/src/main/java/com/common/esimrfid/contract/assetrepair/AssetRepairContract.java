package com.common.esimrfid.contract.assetrepair;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.CreateInvResult;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.InventoryParameter;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.ui.assetrepair.AssetRepairParameter;

import java.util.List;

public interface AssetRepairContract {
    interface View extends AbstractView {
        void handleAllManagerUsers(List<MangerUser> mangerUsers);

        void handleCreateNewRepairOrder(BaseResponse baseResponse);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAllManagerUsers();

        void createNewRepairOrder(AssetRepairParameter repairParameter);
    }
}
