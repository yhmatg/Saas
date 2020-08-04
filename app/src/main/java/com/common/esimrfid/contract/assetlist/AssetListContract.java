package com.common.esimrfid.contract.assetlist;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.assetdetail.AssetFilterParameter;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;

import java.util.List;

public interface AssetListContract {
    interface View extends AbstractView {
        void handlefetchPageAssetsInfos(List<AssetsListItemInfo> assetsInfos);

        void handleAllManagerUsers(List<MangerUser> mangerUsers);

        void handleAllCompany(List<CompanyBean> companyBeans);

        void handleAllDeparts(List<DepartmentBean> departmentBeans);

        void handleAllAssetsType(List<AssetsType> assetsTypes);

        void handleAllAssetsLocation(List<AssetsLocation> assetsLocations);
    }

    interface Presenter extends AbstractPresenter<View> {
        void fetchPageAssetsInfos(Integer size, Integer page, String patternName, String userRealName, int currentSize, AssetFilterParameter conditions);

        void getAllManagerUsers();

        void getAllCompany();

        void getAllDeparts(String comId);

        void getAllAssetsType();

        void getAllAssetsLocation();
    }
}
