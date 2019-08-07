package com.common.esimrfid.core.bean.nanhua.invdetailbeans;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class AssetsStatus {
//    NORMAL("正常", 0),
//    AST_DESTORY("损坏",1),
//    MAINTAIN("维修中",2);


    // 成员变量
    @Ignore
    private String name;
    //need
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ind_assets_code")
    private Integer code;
    @Ignore
    private Integer index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
