package com.common.esimrfid.contract.home;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.home.AssetLocNmu;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.common.esimrfid.core.bean.update.UpdateVersion;

import java.util.List;

public interface HomeConstract {
    interface View extends AbstractView {
        void handleAssetsNmbDiffLocation(List<AssetLocNmu> assetLocations);

        void handleAssetsNmbDiffStatus(AssetStatusNum assetStatus);

        void handelCheckoutVersion(UpdateVersion updateInfo);

        void handleLogin();
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsNmbDiffLocation();

        void getAssetsNmbDiffStatus();

        void checkUpdateVersion();

        void fetchAllIvnOrders( String userId,boolean online);

        void fetchLatestAssets();

        void getDataAuthority(String id);

        void fetchLatestPageAssets(Integer size, Integer page);

        void login(UserInfo userInfo);
    }
}
