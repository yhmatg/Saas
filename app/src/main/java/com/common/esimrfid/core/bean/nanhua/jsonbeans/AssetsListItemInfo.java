package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import android.arch.persistence.room.Ignore;

import java.util.Objects;

public class AssetsListItemInfo {

    private String ast_barcode;
    private String ast_name;
    private String id;
    private String loc_name;
    private String user_name;
    private double ast_price;
    private int ast_used_status;
    private long ast_buy_date;
    @Ignore
    private String type_name;
    //资产报修使用
    @Ignore
    private boolean isSelected;
    @Ignore
    private String ast_model;
    @Ignore
    private String ast_brand;
    @Ignore
    private String org_useddept_name;
    @Ignore
    private String ast_epc_code;
    @Ignore
    private String type_id;
    @Ignore
    private String od_id;
    @Ignore
    private String ast_id;

    public String getAst_barcode() {
        return ast_barcode;
    }

    public void setAst_barcode(String ast_barcode) {
        this.ast_barcode = ast_barcode;
    }

    public String getAst_name() {
        return ast_name;
    }

    public void setAst_name(String ast_name) {
        this.ast_name = ast_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAst_price() {
        return ast_price;
    }

    public void setAst_price(double ast_price) {
        this.ast_price = ast_price;
    }

    public int getAst_used_status() {
        return ast_used_status;
    }

    public void setAst_used_status(int ast_used_status) {
        this.ast_used_status = ast_used_status;
    }


    public long getAst_buy_date() {
        return ast_buy_date;
    }

    public void setAst_buy_date(long ast_buy_date) {
        this.ast_buy_date = ast_buy_date;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAst_model() {
        return ast_model;
    }

    public void setAst_model(String ast_model) {
        this.ast_model = ast_model;
    }

    public String getAst_brand() {
        return ast_brand;
    }

    public void setAst_brand(String ast_brand) {
        this.ast_brand = ast_brand;
    }

    public String getOrg_useddept_name() {
        return org_useddept_name;
    }

    public void setOrg_useddept_name(String org_useddept_name) {
        this.org_useddept_name = org_useddept_name;
    }

    public String getAst_epc_code() {
        return ast_epc_code;
    }

    public void setAst_epc_code(String ast_epc_code) {
        this.ast_epc_code = ast_epc_code;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getOd_id() {
        return od_id;
    }

    public void setOd_id(String od_id) {
        this.od_id = od_id;
    }

    public String getAst_id() {
        return ast_id;
    }

    public void setAst_id(String ast_id) {
        this.ast_id = ast_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetsListItemInfo)) return false;
        AssetsListItemInfo that = (AssetsListItemInfo) o;
        return Objects.equals(getAst_barcode(), that.getAst_barcode()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getAst_id(), that.getAst_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAst_barcode(), getId(), getAst_id());
    }
}
