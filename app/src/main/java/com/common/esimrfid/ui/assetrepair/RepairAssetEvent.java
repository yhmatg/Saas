package com.common.esimrfid.ui.assetrepair;

import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;

import java.util.ArrayList;
import java.util.List;

public class RepairAssetEvent {
    private List<AssetsListItemInfo> mSelectedData = new ArrayList<>();

    public RepairAssetEvent(List<AssetsListItemInfo> mSelectedData) {
        this.mSelectedData = mSelectedData;
    }

    public List<AssetsListItemInfo> getmSelectedData() {
        return mSelectedData;
    }

    public void setmSelectedData(List<AssetsListItemInfo> mSelectedData) {
        this.mSelectedData = mSelectedData;
    }
}
