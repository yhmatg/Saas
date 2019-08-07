package com.common.esimrfid.core.bean.nanhua.invdetailbeans;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity
public class AssetsStoreResult implements Serializable {

    @Ignore
    private Date create_date;
    //need
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ind_assets_store_id")
    private String id;
    @Ignore
    private String sto_code;
    @Ignore
    private int sto_isleaf;
    //need
    private String sto_name;
    @Ignore
    private String sto_remark;
    @Ignore
    private String sto_superid;
    @Ignore
    private Date update_date;
    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
    public Date getCreate_date() {
        return create_date;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setSto_code(String sto_code) {
        this.sto_code = sto_code;
    }
    public String getSto_code() {
        return sto_code;
    }

    public void setSto_isleaf(int sto_isleaf) {
        this.sto_isleaf = sto_isleaf;
    }
    public int getSto_isleaf() {
        return sto_isleaf;
    }

    public void setSto_name(String sto_name) {
        this.sto_name = sto_name;
    }
    public String getSto_name() {
        return sto_name;
    }

    public void setSto_remark(String sto_remark) {
        this.sto_remark = sto_remark;
    }
    public String getSto_remark() {
        return sto_remark;
    }

    public void setSto_superid(String sto_superid) {
        this.sto_superid = sto_superid;
    }
    public String getSto_superid() {
        return sto_superid;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }
    public Date getUpdate_date() {
        return update_date;
    }
}
