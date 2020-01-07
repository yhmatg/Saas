package com.common.esimrfid.core.bean.nanhua.home;

import java.util.Date;

public class CompanyInfo {

    /**
     * create_date : 1578029093000
     * id : 5ef8f7bd2de911eaa0530242ac130003
     * org_code : 0001
     * org_isleaf : 0
     * org_name : 测试公司
     * org_type : 1
     * tenant_id : tenantid0018
     * update_date : 1578000293000
     */

    private Date create_date;
    private String id;
    private String org_code;
    private int org_isleaf;
    private String org_name;
    private int org_type;
    private String tenant_id;
    private Date update_date;

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public int getOrg_isleaf() {
        return org_isleaf;
    }

    public void setOrg_isleaf(int org_isleaf) {
        this.org_isleaf = org_isleaf;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public int getOrg_type() {
        return org_type;
    }

    public void setOrg_type(int org_type) {
        this.org_type = org_type;
    }

    public String getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }
}
