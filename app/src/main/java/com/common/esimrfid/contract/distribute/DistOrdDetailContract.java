package com.common.esimrfid.contract.distribute;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.assetdetail.AssetFilterParameter;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DistributeOrderDetail;

import java.util.List;

public interface DistOrdDetailContract {
    interface View extends AbstractView {
        void handleGetDistOrdDetail(DistributeOrderDetail distributeOrderDetail);

        void handleFetchPageAssetsInfos(List<AssetsListItemInfo> assetsInfos);

        void handleConfirmDistributeAsset(BaseResponse baseResponse);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getDistOrdDetail(String id);

        void fetchPageAssetsInfos(Integer size, Integer page, String patternName, String userRealName, int currentSize, AssetFilterParameter conditions);

        void confirmDistributeAsset(DistributeOrderDetail distributeOrderDetail);
    }
}
