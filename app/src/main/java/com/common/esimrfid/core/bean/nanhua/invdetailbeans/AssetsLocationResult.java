package com.common.esimrfid.core.bean.nanhua.invdetailbeans;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity
public class AssetsLocationResult implements Serializable {

    //need
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ind_assets_loca_id")
    private String id;
    @Ignore
    private Date create_date;
    @Ignore
    private Date update_date;

    private String loc_name;

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

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }
    public Date getUpdate_date() {
        return update_date;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }
    public String getLoc_name() {
        return loc_name;
    }
}
