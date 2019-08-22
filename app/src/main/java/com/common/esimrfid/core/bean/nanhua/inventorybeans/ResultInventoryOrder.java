package com.common.esimrfid.core.bean.nanhua.inventorybeans;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class ResultInventoryOrder implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    @Embedded
    private Assigner assigner;
    private Date create_date;//Date
    @Embedded
    private Creator creator;
    private String creator_id;
    private String inv_assigner_id;
    private String inv_code;
    private String inv_creator_id;
    @Ignore
    private Set<String> inv_dept_filter;
    private Date inv_exptfinish_date;
    private Integer inv_finish_count;
    private String inv_remark;
    private String finish_remark;
    @Embedded
    private OrderStatus inv_status;
    private Integer inv_total_count;
    private Date update_date;
    @Embedded
    private Updater updater;
    private String updater_id;
    private Integer opt_status;

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

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getFinish_remark() {
        return finish_remark;
    }

    public void setFinish_remark(String finish_remark) {
        this.finish_remark = finish_remark;
    }

    public Assigner getAssigner() {
        return assigner;
    }

    public void setAssigner(Assigner assigner) {
        this.assigner = assigner;
    }


    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
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

    public Set<String> getInv_dept_filter() {
        return inv_dept_filter;
    }

    public void setInv_dept_filter(Set<String> inv_dept_filter) {
        this.inv_dept_filter = inv_dept_filter;
    }


    public Integer getInv_finish_count() {
        return inv_finish_count;
    }

    public void setInv_finish_count(Integer inv_finish_count) {
        this.inv_finish_count = inv_finish_count;
    }

    public String getInv_remark() {
        return inv_remark;
    }

    public void setInv_remark(String inv_remark) {
        this.inv_remark = inv_remark;
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
    public Updater getUpdater() {
        return updater;
    }

    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    public String getUpdater_id() {
        return updater_id;
    }

    public void setUpdater_id(String updater_id) {
        this.updater_id = updater_id;
    }

    public Integer getOpt_status() {
        return opt_status;
    }

    public void setOpt_status(Integer opt_status) {
        this.opt_status = opt_status;
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