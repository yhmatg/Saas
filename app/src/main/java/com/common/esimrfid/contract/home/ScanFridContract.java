package com.common.esimrfid.contract.home;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.invscannbeans.AssetsInfo;

import java.util.List;
import java.util.Set;

public interface ScanFridContract {
    interface View extends AbstractView {
        void handleAssetsInfons(List<AssetsInfo> assetsInfos);
    }

    interface Presenter extends AbstractPresenter<View> {
        void fetchScanAssetsInfons( Set<String> ecps);
    }
}
