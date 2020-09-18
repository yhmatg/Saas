package com.common.xfxj.ui.newinventory;

import com.contrarywind.interfaces.IPickerViewData;

public class AssetTag implements IPickerViewData {

    private String tagName;
    public AssetTag(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String getPickerViewText() {
        return tagName;
    }
}
