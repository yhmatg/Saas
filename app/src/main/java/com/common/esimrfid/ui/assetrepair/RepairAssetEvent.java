package com.common.esimrfid.ui.assetrepair;

import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;

import java.util.ArrayList;
import java.util.List;

public class RepairAssetEvent {
    private List<AssetsInfo> mSelectedData = new ArrayList<>();

    public RepairAssetEvent(List<AssetsInfo> mSelectedData) {
        this.mSelectedData = mSelectedData;
    }

    public List<AssetsInfo> getmSelectedData() {
        return mSelectedData;
    }

    public void setmSelectedData(List<AssetsInfo> mSelectedData) {
        this.mSelectedData = mSelectedData;
    }
}
