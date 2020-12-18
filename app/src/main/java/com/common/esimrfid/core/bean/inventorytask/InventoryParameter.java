package com.common.esimrfid.core.bean.inventorytask;

import java.util.Date;
import java.util.List;

public class InventoryParameter {
    private String inv_name;
    private String inv_assigner_id;
    private String inv_assigner_name;
    private Date inv_exptbegin_date ;
    private Date inv_exptfinish_date;
    private List<String> inv_used_corp_filter;
    private List<String> inv_belong_corp_filter;
    private List<String> inv_used_dept_filter;
    private List<String> inv_type_filter;
    private List<String> inv_loc_filter;
    private boolean createByApp = true;
    private int inv_emp_switch;

    public String getInv_name() {
        return inv_name;
    }

    public void setInv_name(String inv_name) {
        this.inv_name = inv_name;
    }

    public String getInv_assigner_id() {
        return inv_assigner_id;
    }

    public void setInv_assigner_id(String inv_assigner_id) {
        this.inv_assigner_id = inv_assigner_id;
    }

    public String getInv_assigner_name() {
        return inv_assigner_name;
    }

    public void setInv_assigner_name(String inv_assigner_name) {
        this.inv_assigner_name = inv_assigner_name;
    }

    public Date getInv_exptbegin_date() {
        return inv_exptbegin_date;
    }

    public void setInv_exptbegin_date(Date inv_exptbegin_date) {
        this.inv_exptbegin_date = inv_exptbegin_date;
    }

    public Date getInv_exptfinish_date() {
        return inv_exptfinish_date;
    }

    public void setInv_exptfinish_date(Date inv_exptfinish_date) {
        this.inv_exptfinish_date = inv_exptfinish_date;
    }

    public List<String> getInv_used_corp_filter() {
        return inv_used_corp_filter;
    }

    public void setInv_used_corp_filter(List<String> inv_used_corp_filter) {
        this.inv_used_corp_filter = inv_used_corp_filter;
    }

    public List<String> getInv_belong_corp_filter() {
        return inv_belong_corp_filter;
    }

    public void setInv_belong_corp_filter(List<String> inv_belong_corp_filter) {
        this.inv_belong_corp_filter = inv_belong_corp_filter;
    }

    public List<String> getInv_used_dept_filter() {
        return inv_used_dept_filter;
    }

    public void setInv_used_dept_filter(List<String> inv_used_dept_filter) {
        this.inv_used_dept_filter = inv_used_dept_filter;
    }

    public List<String> getInv_type_filter() {
        return inv_type_filter;
    }

    public void setInv_type_filter(List<String> inv_type_filter) {
        this.inv_type_filter = inv_type_filter;
    }

    public List<String> getInv_loc_filter() {
        return inv_loc_filter;
    }

    public void setInv_loc_filter(List<String> inv_loc_filter) {
        this.inv_loc_filter = inv_loc_filter;
    }

    public boolean isCreateByApp() {
        return createByApp;
    }

    public void setCreateByApp(boolean createByApp) {
        this.createByApp = createByApp;
    }

    public int getInv_emp_switch() {
        return inv_emp_switch;
    }

    public void setInv_emp_switch(int inv_emp_switch) {
        this.inv_emp_switch = inv_emp_switch;
    }
}
