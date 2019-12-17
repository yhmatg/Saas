package com.common.esimrfid.core.bean.inventorytask;

import com.contrarywind.interfaces.IPickerViewData;

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

    @Override
    public String getPickerViewText() {
        return loc_name;
    }
}
