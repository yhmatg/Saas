package com.common.esimrfid.core.bean.assetdetail;

import java.util.List;

public class UpdateAssetsPara {
    private List<String> astIds;
    private String loc_id;

    public List<String> getAstIds() {
        return astIds;
    }

    public void setAstIds(List<String> astIds) {
        this.astIds = astIds;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }
}
