package com.ddcommon.esimrfid.contract.home;

import com.ddcommon.esimrfid.base.presenter.AbstractPresenter;
import com.ddcommon.esimrfid.base.view.AbstractView;
import com.ddcommon.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.ddcommon.esimrfid.core.bean.nanhua.home.CompanyInfo;
import com.ddcommon.esimrfid.core.bean.update.UpdateVersion;

import java.util.HashMap;

public interface HomeConstract {
    interface View extends AbstractView {
        void handleAssetsNmbDiffLocation(HashMap<String,Integer> assetLocations);

        void handleAssetsNmbDiffStatus(AssetStatusNum assetStatus);

        void handelCheckoutVersion(UpdateVersion updateInfo);

        void handleGetCompanyInfo(CompanyInfo companyInfo);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsNmbDiffLocation();

        void getAssetsNmbDiffStatus();

        void checkUpdateVersion();

        void getCompanyInfo();

        void  fetchAllIvnOrders( String userId,boolean online);

        void getAssetsInfoById(String assetsId);
    }
}
