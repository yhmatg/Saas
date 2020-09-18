package com.common.xfxj.core.bean.nanhua.xfxj;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * 资产详细信息（all）
 */
@Entity
public class XfAssetsAllInfo {
    @PrimaryKey
    @NonNull
    private String id;
    private String ast_barcode;
    private String ast_brand;
    private String ast_epc_code;
    private String loc_name;
    private String org_belongcorp_name;

    public XfAssetsAllInfo(@NonNull String id, String ast_barcode, String ast_brand, String ast_epc_code, String loc_name, String org_belongcorp_name) {
        this.id = id;
        this.ast_barcode = ast_barcode;
        this.ast_brand = ast_brand;
        this.ast_epc_code = ast_epc_code;
        this.loc_name = loc_name;
        this.org_belongcorp_name = org_belongcorp_name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAst_barcode() {
        return ast_barcode;
    }

    public void setAst_barcode(String ast_barcode) {
        this.ast_barcode = ast_barcode;
    }

    public String getAst_brand() {
        return ast_brand;
    }

    public void setAst_brand(String ast_brand) {
        this.ast_brand = ast_brand;
    }

    public String getAst_epc_code() {
        return ast_epc_code;
    }

    public void setAst_epc_code(String ast_epc_code) {
        this.ast_epc_code = ast_epc_code;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public String getOrg_belongcorp_name() {
        return org_belongcorp_name;
    }

    public void setOrg_belongcorp_name(String org_belongcorp_name) {
        this.org_belongcorp_name = org_belongcorp_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XfAssetsAllInfo)) return false;
        XfAssetsAllInfo that = (XfAssetsAllInfo) o;
        return getId().equals(that.getId()) &&
                getAst_barcode().equals(that.getAst_barcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAst_barcode());
    }
}
