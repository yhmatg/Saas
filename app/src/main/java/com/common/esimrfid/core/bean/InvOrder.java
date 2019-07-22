package com.common.esimrfid.core.bean;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity
public class InvOrder implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String invCode;
    private int invStatus;
    private String invRemark;
    private Date invExptfinishDate;
    private Date createDate;
    private Date updateDate;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getInvCode() {
        return invCode;
    }

    public void setInvCode(String invCode) {
        this.invCode = invCode;
    }

    public int getInvStatus() {
        return invStatus;
    }

    public void setInvStatus(int invStatus) {
        this.invStatus = invStatus;
    }

    public Date getInvExptfinishDate() {
        return invExptfinishDate;
    }

    public void setInvExptfinishDate(Date invExptfinishDate) {
        this.invExptfinishDate = invExptfinishDate;
    }

    public String getInvRemark() {
        return invRemark;
    }

    public void setInvRemark(String invRemark) {
        this.invRemark = invRemark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getInvStatusString() {
        String statusString="";
        switch(invStatus){
            case 0:
                statusString="初始创建";
                break;
            case 1:
                statusString="处理中";
                break;
            case 2:
                statusString="已完成";
                break;
        }
        return statusString;
    }
}
