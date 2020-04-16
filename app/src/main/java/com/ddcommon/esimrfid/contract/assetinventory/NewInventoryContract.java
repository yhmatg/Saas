package com.ddcommon.esimrfid.contract.assetinventory;

import com.ddcommon.esimrfid.base.presenter.AbstractPresenter;
import com.ddcommon.esimrfid.base.view.AbstractView;
import com.ddcommon.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.ddcommon.esimrfid.core.bean.inventorytask.AssetsType;
import com.ddcommon.esimrfid.core.bean.inventorytask.CompanyBean;
import com.ddcommon.esimrfid.core.bean.inventorytask.CreateInvResult;
import com.ddcommon.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.ddcommon.esimrfid.core.bean.inventorytask.InventoryParameter;
import com.ddcommon.esimrfid.core.bean.inventorytask.MangerUser;
import java.util.List;

public interface NewInventoryContract {
    interface View extends AbstractView {
        void handleAllManagerUsers(List<MangerUser> mangerUsers);

        void handleAllCompany(List<CompanyBean> companyBeans);

        void handleAllDeparts(List<DepartmentBean> departmentBeans);

        void handleAllAssetsType(List<AssetsType> assetsTypes);

        void handleAllAssetsLocation(List<AssetsLocation> assetsLocations);

        void handlecreateNewInventory(CreateInvResult createInvResult);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAllManagerUsers();

        void getAllCompany();

        void getAllDeparts(String comId);

        void getAllAssetsType();

        void getAllAssetsLocation();

        void createNewInventory(InventoryParameter invpara);
    }
}
