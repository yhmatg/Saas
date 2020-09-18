package com.common.xfxj.core.bean.nanhua.jsonbeans;

import java.util.Objects;

public class SearchAssetsInfo {

    private String ast_brand;
    private String ast_barcode;
    private String ast_epc_code;
    private String ast_model;
    private String ast_name;
    private String id;
    private String loc_name;

    public String getAst_brand() {
        return ast_brand;
    }

    public void setAst_brand(String ast_brand) {
        this.ast_brand = ast_brand;
    }

    public String getAst_barcode() {
        return ast_barcode;
    }

    public void setAst_barcode(String ast_barcode) {
        this.ast_barcode = ast_barcode;
    }

    public String getAst_epc_code() {
        return ast_epc_code;
    }

    public void setAst_epc_code(String ast_epc_code) {
        this.ast_epc_code = ast_epc_code;
    }

    public String getAst_model() {
        return ast_model;
    }

    public void setAst_model(String ast_model) {
        this.ast_model = ast_model;
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

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchAssetsInfo)) return false;
        SearchAssetsInfo that = (SearchAssetsInfo) o;
        return getAst_barcode().equals(that.getAst_barcode()) &&
                getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAst_barcode(), getId());
    }
}
