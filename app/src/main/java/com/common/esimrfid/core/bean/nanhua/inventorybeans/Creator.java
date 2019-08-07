package com.common.esimrfid.core.bean.nanhua.inventorybeans;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Creator {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ind_creator_id")
    String id;
    @ColumnInfo(name = "ind_creator_user_name")
    String user_name;
    @ColumnInfo(name = "ind_creator_user_real_name")
    String user_real_name;

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
}
