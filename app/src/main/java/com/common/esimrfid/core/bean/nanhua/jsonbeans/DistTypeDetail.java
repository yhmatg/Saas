package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DistTypeDetail {
    private String id;
    private String class_id;
    private String class_name;
    private String type_id;
    private String type_name;
    private String remark;
    private Integer amount;
    private List<AssetsListItemInfo> subList = new ArrayList<>();
    private Integer alreadyAdd = 0;
    private Integer notAdd = 0;
    private String ret_store_user_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<AssetsListItemInfo> getSubList() {
        return subList;
    }

    public void setSubList(List<AssetsListItemInfo> subList) {
        this.subList = subList;
    }

    public Integer getAlreadyAdd() {
        return alreadyAdd;
    }

    public void setAlreadyAdd(Integer alreadyAdd) {
        this.alreadyAdd = alreadyAdd;
    }

    public Integer getNotAdd() {
        return notAdd;
    }

    public void setNotAdd(Integer notAdd) {
        this.notAdd = notAdd;
    }

    public float getProgress() {
        return (float) alreadyAdd / amount * 100;
    }

    public String getRet_store_user_id() {
        return ret_store_user_id;
    }

    public void setRet_store_user_id(String ret_store_user_id) {
        this.ret_store_user_id = ret_store_user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DistTypeDetail)) return false;
        DistTypeDetail that = (DistTypeDetail) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
