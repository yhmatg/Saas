package com.common.esimrfid.contract.batchedit;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.assetdetail.UpdateAssetsPara;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;

import java.util.List;

public interface BatchEditContract {
    interface View extends AbstractView {
        void handGetAllAssetsForSearch(List<SearchAssetsInfo> searchInfos);

        void handleAllAssetsLocation(List<AssetsLocation> assetsLocations);

        void handelUpdateAssetLoc(BaseResponse resultResponse);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAllAssetsForSearch();

        void fetchLatestPageAssets(Integer size, Integer page);

        void getAllAssetsLocation();

        void updateAssetLoc(List<String> astIds, String loc);

        void updateAssetProp(UpdateAssetsPara updateAssetsPara);
    }
}
