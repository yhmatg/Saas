package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import java.util.Objects;

public class AssetsListItemInfo {

    private String ast_barcode;
    private String ast_name;
    private String id;
    private String loc_info_loc_name;
    private String userinfo_user_real_name;
    private double ast_price;

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

    public String getLoc_info_loc_name() {
        return loc_info_loc_name;
    }

    public void setLoc_info_loc_name(String loc_info_loc_name) {
        this.loc_info_loc_name = loc_info_loc_name;
    }

    public String getUserinfo_user_real_name() {
        return userinfo_user_real_name;
    }

    public void setUserinfo_user_real_name(String userinfo_user_real_name) {
        this.userinfo_user_real_name = userinfo_user_real_name;
    }

    public double getAst_price() {
        return ast_price;
    }

    public void setAst_price(double ast_price) {
        this.ast_price = ast_price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetsListItemInfo)) return false;
        AssetsListItemInfo that = (AssetsListItemInfo) o;
        return getAst_barcode().equals(that.getAst_barcode()) &&
                getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAst_barcode(), getId());
    }
}
