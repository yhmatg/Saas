package com.common.esimrfid.core.bean.nanhua.xfxj;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

//盘点列表
@Entity
public class XfResultInventoryOrder {
    @PrimaryKey
    @NonNull
    private String id;
    private String inv_code;
    private String inv_name;
    private String inv_person_name;
    private String xjDate;
    private String xjTime;
    private Integer inv_finish_count;
    private Integer inv_total_count;
    //0未完成 1已完成 2已逾期
    private Integer inv_status;

    public XfResultInventoryOrder(@NonNull String id, String inv_code, String inv_name, String inv_person_name, String xjDate, String xjTime, Integer inv_finish_count, Integer inv_total_count, Integer inv_status) {
        this.id = id;
        this.inv_code = inv_code;
        this.inv_name = inv_name;
        this.inv_person_name = inv_person_name;
        this.xjDate = xjDate;
        this.xjTime = xjTime;
        this.inv_finish_count = inv_finish_count;
        this.inv_total_count = inv_total_count;
        this.inv_status = inv_status;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getInv_code() {
        return inv_code;
    }

    public void setInv_code(String inv_code) {
        this.inv_code = inv_code;
    }

    public String getInv_name() {
        return inv_name;
    }

    public void setInv_name(String inv_name) {
        this.inv_name = inv_name;
    }

    public String getInv_person_name() {
        return inv_person_name;
    }

    public void setInv_person_name(String inv_person_name) {
        this.inv_person_name = inv_person_name;
    }

    public String getXjDate() {
        return xjDate;
    }

    public void setXjDate(String xjDate) {
        this.xjDate = xjDate;
    }

    public String getXjTime() {
        return xjTime;
    }

    public void setXjTime(String xjTime) {
        this.xjTime = xjTime;
    }

    public Integer getInv_finish_count() {
        return inv_finish_count;
    }

    public void setInv_finish_count(Integer inv_finish_count) {
        this.inv_finish_count = inv_finish_count;
    }

    public Integer getInv_total_count() {
        return inv_total_count;
    }

    public void setInv_total_count(Integer inv_total_count) {
        this.inv_total_count = inv_total_count;
    }

    public Integer getInv_status() {
        return inv_status;
    }

    public void setInv_status(Integer inv_status) {
        this.inv_status = inv_status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XfResultInventoryOrder)) return false;
        XfResultInventoryOrder that = (XfResultInventoryOrder) o;
        return getId().equals(that.getId()) &&
                getInv_code().equals(that.getInv_code());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getInv_code());
    }
}
