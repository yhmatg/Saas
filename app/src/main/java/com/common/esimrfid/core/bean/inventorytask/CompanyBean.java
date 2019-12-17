package com.common.esimrfid.core.bean.inventorytask;

import com.contrarywind.interfaces.IPickerViewData;

public class CompanyBean implements IPickerViewData {

    /**
     * create_date : 1575360604000
     * id : 4fa06ed815a411eaa0530242ac130003
     * org_code : 000100010001
     * org_isleaf : 1
     * org_name : 21
     * org_superid : 7ce3d05214ee11eaa0530242ac130003
     * org_type : 1
     * tenant_id : tenantid0001
     * update_date : 1575331804000
     */

    private String id;
    private String org_name;
    private String org_superid;
    private int org_type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_superid() {
        return org_superid;
    }

    public void setOrg_superid(String org_superid) {
        this.org_superid = org_superid;
    }

    public int getOrg_type() {
        return org_type;
    }

    public void setOrg_type(int org_type) {
        this.org_type = org_type;
    }

    @Override
    public String getPickerViewText() {
        return org_name;
    }
}
