package com.common.esimrfid.contract.home;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.home.CompanyInfo;
import com.common.esimrfid.core.bean.update.UpdateVersion;

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
