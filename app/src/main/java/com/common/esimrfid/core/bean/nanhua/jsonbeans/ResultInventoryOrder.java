package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

//盘点列表
@Entity
public class ResultInventoryOrder {

    /**
     * assigner : {"id":"ast_mgr_id","user_name":"astMgr","user_real_name":"资管员"}
     * create_date : 1573616054000
     * creator : {"id":"admin_id","user_name":"admin","user_real_name":"张三"}
     * creator_id : admin_id
     * id : 75bdeeee05c611ea8f82258b4dde74a9
     * inv_assigner_id : ast_mgr_id
     * inv_beDate_corp_filter : []
     * inv_code : PD201911130005
     * inv_creator_id : admin_id
     * inv_exptfinish_date : 1575043200000
     * inv_finish_count : 0
     * inv_loc_filter : []
     * inv_name : Z
     * inv_remark : Z
     * inv_status : 10
     * inv_total_count : 2
     * inv_type_filter : []
     * inv_used_corp_filter : ["fasVSVrdsvvad"]
     * inv_used_dept_filter : []
     * update_date : 1573616054000
     * updater : {"id":"admin_id","user_name":"admin","user_real_name":"张三"}
     * updater_id : admin_id
     */
    private Date create_date;
    @PrimaryKey
    @NonNull
    private String id;
    private String inv_code;
    private Date inv_exptfinish_date;
    private Date inv_finish_date;
    private Integer inv_finish_count;
    private Integer inv_notsubmit_count = 0;
    private String inv_name;
    private Integer inv_status;
    private Integer inv_total_count;
    //本地添加的盘点单状态，非服务器返回
    private Integer opt_status;
    private String inv_assigner_id;
    private String inv_assigner_name;
    private String inv_creator_id;
    private String inv_creator_name;

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


    public String getInv_code() {
        return inv_code;
    }

    public void setInv_code(String inv_code) {
        this.inv_code = inv_code;
    }

    public Date getInv_exptfinish_date() {
        return inv_exptfinish_date;
    }

    public void setInv_exptfinish_date(Date inv_exptfinish_date) {
        this.inv_exptfinish_date = inv_exptfinish_date;
    }

    public Date getInv_finish_date() {
        return inv_finish_date;
    }

    public void setInv_finish_date(Date inv_finish_date) {
        this.inv_finish_date = inv_finish_date;
    }

    public Integer getInv_finish_count() {
        return inv_finish_count;
    }

    public void setInv_finish_count(Integer inv_finish_count) {
        this.inv_finish_count = inv_finish_count;
    }

    public Integer getInv_notsubmit_count() {
        return inv_notsubmit_count;
    }

    public void setInv_notsubmit_count(Integer inv_notsubmit_count) {
        this.inv_notsubmit_count = inv_notsubmit_count;
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

    public Integer getOpt_status() {
        return opt_status;
    }

    public void setOpt_status(Integer opt_status) {
        this.opt_status = opt_status;
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

    public String getInv_creator_id() {
        return inv_creator_id;
    }

    public void setInv_creator_id(String inv_creator_id) {
        this.inv_creator_id = inv_creator_id;
    }

    public String getInv_creator_name() {
        return inv_creator_name;
    }

    public void setInv_creator_name(String inv_creator_name) {
        this.inv_creator_name = inv_creator_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultInventoryOrder)) return false;
        ResultInventoryOrder that = (ResultInventoryOrder) o;
        return getId().equals(that.getId()) &&
                getInv_code().equals(that.getInv_code());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getInv_code());
    }
}
