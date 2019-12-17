package com.common.esimrfid.core.bean.inventorytask;

import java.util.Date;

public class CreateInvResult {

    /**
     * create_date : 1575515768311
     * creator_id : caddb5d8014311eab97300163e086c26
     * id : 9496c30d170d11eaabcf00163e0a6695
     * inv_assigner_id : 07043df511c511eaabcf00163e0a6695
     * inv_code : PD201912050016
     * inv_creator_id : caddb5d8014311eab97300163e086c26
     * inv_exptfinish_date : 1574825478000
     * inv_finish_count : 0
     * inv_name : admin
     * inv_status : 10
     * inv_total_count : 100
     * update_date : 1575515768311
     * updater_id : caddb5d8014311eab97300163e086c26
     */

    private Date create_date;
    private String creator_id;
    private String id;
    private String inv_assigner_id;
    private String inv_code;
    private String inv_creator_id;
    private Date inv_exptfinish_date;
    private Integer inv_finish_count;
    private String inv_name;
    private Integer inv_status;
    private Integer inv_total_count;
    private Date update_date;
    private String updater_id;

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
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

    public Date getInv_exptfinish_date() {
        return inv_exptfinish_date;
    }

    public void setInv_exptfinish_date(Date inv_exptfinish_date) {
        this.inv_exptfinish_date = inv_exptfinish_date;
    }

    public Integer getInv_finish_count() {
        return inv_finish_count;
    }

    public void setInv_finish_count(Integer inv_finish_count) {
        this.inv_finish_count = inv_finish_count;
    }

    public String getInv_name() {
        return inv_name;
    }

    public void setInv_name(String inv_name) {
        this.inv_name = inv_name;
    }

    public Integer getInv_status() {
        return inv_status;
    }

    public void setInv_status(Integer inv_status) {
        this.inv_status = inv_status;
    }

    public Integer getInv_total_count() {
        return inv_total_count;
    }

    public void setInv_total_count(Integer inv_total_count) {
        this.inv_total_count = inv_total_count;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getUpdater_id() {
        return updater_id;
    }

    public void setUpdater_id(String updater_id) {
        this.updater_id = updater_id;
    }
}
