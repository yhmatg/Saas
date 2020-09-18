package com.common.xfxj.core.bean.nanhua.jsonbeans;

import java.util.List;

public class LatestModifyAssets {
    private List<AssetsAllInfo> modified;
    private List<AssetsAllInfo> removed;

    public List<AssetsAllInfo> getModified() {
        return modified;
    }

    public void setModified(List<AssetsAllInfo> modified) {
        this.modified = modified;
    }

    public List<AssetsAllInfo> getRemoved() {
        return removed;
    }

    public void setRemoved(List<AssetsAllInfo> removed) {
        this.removed = removed;
    }
}
