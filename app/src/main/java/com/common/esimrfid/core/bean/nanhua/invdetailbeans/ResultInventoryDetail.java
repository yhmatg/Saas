package com.common.esimrfid.core.bean.nanhua.invdetailbeans;

import com.common.esimrfid.core.bean.nanhua.inventorybeans.OrderStatus;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class ResultInventoryDetail {

    private Date create_date;
    private String creator_id;
    private String id;
    private String inv_assigner_id;
    private Set<String> inv_aststatus_filter;
    private String inv_code;
    private String inv_creator_id;
    private Set<String> inv_dept_filter;
    private List<InventoryDetail> detailResults;
    private Date inv_exptfinish_date;
    private Set<String> inv_loc_filter;
    private OrderStatus inv_status;
    private Integer inv_total_count;
    private Set<String> inv_type_filter;
    private Date update_date;
    private String updater_id;

    public Date getUpdate_date() {
        return update_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getInv_exptfinish_date() {
        return inv_exptfinish_date;
    }

    public void setInv_exptfinish_date(Date inv_exptfinish_date) {
        this.inv_exptfinish_date = inv_exptfinish_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInv_assigner_id() {
        return inv_assigner_id;
    }

    public void setInv_assigner_id(String inv_assigner_id) {
        this.inv_assigner_id = inv_assigner_id;
    }

    public Set<String> getInv_aststatus_filter() {
        return inv_aststatus_filter;
    }

    public void setInv_aststatus_filter(Set<String> inv_aststatus_filter) {
        this.inv_aststatus_filter = inv_aststatus_filter;
    }

    public String getInv_code() {
        return inv_code;
    }

    public void setInv_code(String inv_code) {
        this.inv_code = inv_code;
    }

    public String getInv_creator_id() {
        return inv_creator_id;
    }

    public void setInv_creator_id(String inv_creator_id) {
        this.inv_creator_id = inv_creator_id;
    }

    public Set<String> getInv_dept_filter() {
        return inv_dept_filter;
    }

    public void setInv_dept_filter(Set<String> inv_dept_filter) {
        this.inv_dept_filter = inv_dept_filter;
    }

    public List<InventoryDetail> getDetailResults() {
        return detailResults;
    }

    public void setDetailResults(List<InventoryDetail> detailResults) {
        this.detailResults = detailResults;
    }


    public Set<String> getInv_loc_filter() {
        return inv_loc_filter;
    }

    public void setInv_loc_filter(Set<String> inv_loc_filter) {
        this.inv_loc_filter = inv_loc_filter;
    }

    public OrderStatus getInv_status() {
        return inv_status;
    }

    public void setInv_status(OrderStatus inv_status) {
        this.inv_status = inv_status;
    }

    public Integer getInv_total_count() {
        return inv_total_count;
    }

    public void setInv_total_count(Integer inv_total_count) {
        this.inv_total_count = inv_total_count;
    }

    public Set<String> getInv_type_filter() {
        return inv_type_filter;
    }

    public void setInv_type_filter(Set<String> inv_type_filter) {
        this.inv_type_filter = inv_type_filter;
    }

    public String getUpdater_id() {
        return updater_id;
    }

    public void setUpdater_id(String updater_id) {
        this.updater_id = updater_id;
    }
}
