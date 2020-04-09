package com.common.esimrfid.core.bean.inventorytask;

import com.contrarywind.interfaces.IPickerViewData;

public class MangerUser implements IPickerViewData {

    /**
     * create_date : 1575361229000
     * id : 1af070deba5c48f2b05ff79aa805de24
     * organization : {"org_code":"00010001","org_isleaf":0,"org_name":"蜀国","org_superid":"cadd4999014311eab97300163e086c26","org_type":0}
     * sysRole : {"menu_ids":[101,201,203,204,205],"role_code":0,"role_name":"测试","role_status":0}
     * tenant_id : tenantid0001
     * update_date : 1575331867000
     * user_email : 15555@qq.com
     * user_empcode : 000100032
     * user_name : 21
     * user_real_name : 用户003
     * user_status : 0
     */

    private String id;
    private String user_name;
    private String user_real_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_real_name() {
        return user_real_name;
    }

    public void setUser_real_name(String user_real_name) {
        this.user_real_name = user_real_name;
    }

    @Override
    public String getPickerViewText() {
        return user_real_name;
    }
}
