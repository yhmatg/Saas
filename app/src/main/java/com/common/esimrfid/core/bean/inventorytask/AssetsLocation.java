package com.common.esimrfid.core.bean.inventorytask;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.Objects;

public class AssetsLocation implements IPickerViewData {

    /**
     * create_date : 1574674185000
     * id : 1e38e4520f6611eaabcf00163e0a6695
     * loc_code : 0001
     * loc_imgs : []
     * loc_isleaf : 1
     * loc_latlng : []
     * loc_name : 位置1
     * loc_remark :
     * update_date : 1574645385000
     */

    private String id;
    private String loc_name;
    private String loc_superid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public String getLoc_superid() {
        return loc_superid;
    }

    public void setLoc_superid(String loc_superid) {
        this.loc_superid = loc_superid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetsLocation)) return false;
        AssetsLocation that = (AssetsLocation) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String getPickerViewText() {
        return loc_name;
    }
}
