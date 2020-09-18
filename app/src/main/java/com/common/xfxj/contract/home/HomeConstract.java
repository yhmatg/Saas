package com.common.xfxj.contract.home;

import com.common.xfxj.base.presenter.AbstractPresenter;
import com.common.xfxj.base.view.AbstractView;
import com.common.xfxj.core.bean.nanhua.home.AssetLocNmu;
import com.common.xfxj.core.bean.nanhua.home.AssetStatusNum;
import com.common.xfxj.core.bean.update.UpdateVersion;

import java.util.List;

public interface HomeConstract {
    interface View extends AbstractView {
        void handleAssetsNmbDiffLocation(List<AssetLocNmu> assetLocations);

        void handleAssetsNmbDiffStatus(AssetStatusNum assetStatus);

        void handelCheckoutVersion(UpdateVersion updateInfo);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAssetsNmbDiffLocation();

        void getAssetsNmbDiffStatus();

        void checkUpdateVersion();

        void fetchAllIvnOrders( String userId,boolean online);

        void fetchLatestAssets();

        void getDataAuthority(String id);
    }
}
