package com.common.xfxj.xfxj.repair;


import com.common.xfxj.core.bean.nanhua.xfxj.XfInventoryDetail;

import java.util.ArrayList;
import java.util.List;

public class XfRepairAssetEvent {
    private List<XfInventoryDetail> mSelectedData = new ArrayList<>();

    public XfRepairAssetEvent(List<XfInventoryDetail> mSelectedData) {
        this.mSelectedData = mSelectedData;
    }

    public List<XfInventoryDetail> getmSelectedData() {
        return mSelectedData;
    }

    public void setmSelectedData(List<XfInventoryDetail> mSelectedData) {
        this.mSelectedData = mSelectedData;
    }
}
