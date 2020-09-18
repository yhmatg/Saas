package com.common.xfxj.ui.inventorytask;

import com.common.xfxj.core.bean.nanhua.jsonbeans.InventoryDetail;

import java.util.List;
import java.util.Objects;

public class InvLocationBean {
    private String invId;
    private String locNmme;
    private String locId;
    private int allNum;
    private int notInvNum;
    private int invNum;
    private int moreInvNum;
    private int lessInvNum;
    List<InventoryDetail> mInventoryDetails;

    public InvLocationBean() {

    }

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    public String getLocNmme() {
        return locNmme;
    }

    public void setLocNmme(String locNmme) {
        this.locNmme = locNmme;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public float getProgress() {
        return (float)(allNum - notInvNum) / allNum * 100;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getNotInvNum() {
        return notInvNum;
    }

    public void setNotInvNum(int notInvNum) {
        this.notInvNum = notInvNum;
    }

    public int getInvNum() {
        return invNum;
    }

    public void setInvNum(int invNum) {
        this.invNum = invNum;
    }

    public int getMoreInvNum() {
        return moreInvNum;
    }

    public void setMoreInvNum(int moreInvNum) {
        this.moreInvNum = moreInvNum;
    }

    public int getLessInvNum() {
        return lessInvNum;
    }

    public void setLessInvNum(int lessInvNum) {
        this.lessInvNum = lessInvNum;
    }

    public List<InventoryDetail> getmInventoryDetails() {
        return mInventoryDetails;
    }

    public void setmInventoryDetails(List<InventoryDetail> mInventoryDetails) {
        this.mInventoryDetails = mInventoryDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvLocationBean)) return false;
        InvLocationBean that = (InvLocationBean) o;
        return getLocNmme().equals(that.getLocNmme()) &&
                getLocId().equals(that.getLocId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLocNmme(), getLocId());
    }
}
