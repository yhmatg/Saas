package com.common.esimrfid.core.bean.nanhua.invdetailbeans;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import java.io.Serializable;
import java.util.Date;

@Entity
public class AssetsInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    //need
    private String ast_barcode;
    //need
    private String ast_brand;
    //need
    private String ast_code;
    //need
    private String ast_epc_code;
    //need
    private String ast_model;
    //need
    private String ast_name;
    //need
    private double ast_price;
    //need
    @Embedded
    private AssetsStatus ast_status;
    //need
    @Embedded(prefix = "used_")
    private AssetsStatus ast_used_status;
    //need
    private Date ast_instore_date;
    //need
    private String ast_img_url;
    //need
    private Integer ast_material;
    //need
    private String ast_measuring_unit;
    //need
    private String ast_sn;
    //need
    private Date ast_buy_date;
    @Ignore
    private String id;
    @Ignore
    private String org_belongcrop_id;
    @Ignore
    private String org_belongdept_id;
    //need
    @Embedded
    private AssetsType type_info;
    //need
    @Embedded
    private SysUser user_info;
    //need
    @Embedded
    private AssetsStoreResult store_info;
    //need
    @Embedded
    private AssetsLocationResult loc_info;
    //need
    @Embedded
    private OrgnizationInfo org_belongdept;

    public Date getAst_instore_date() {
        return ast_instore_date;
    }

    public void setAst_instore_date(Date ast_instore_date) {
        this.ast_instore_date = ast_instore_date;
    }

    public Date getAst_buy_date() {
        return ast_buy_date;
    }

    public void setAst_buy_date(Date ast_buy_date) {
        this.ast_buy_date = ast_buy_date;
    }

    public OrgnizationInfo getOrg_belongdept() {
        return org_belongdept;
    }

    public void setOrg_belongdept(OrgnizationInfo org_belongdept) {
        this.org_belongdept = org_belongdept;
    }

    public double getAst_price() {
        return ast_price;
    }

    public void setAst_price(double ast_price) {
        this.ast_price = ast_price;
    }

    public AssetsLocationResult getLoc_info() {
        return loc_info;
    }

    public void setLoc_info(AssetsLocationResult loc_info) {
        this.loc_info = loc_info;
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

    public String getAst_code() {
        return ast_code;
    }

    public void setAst_code(String ast_code) {
        this.ast_code = ast_code;
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

    public AssetsStatus getAst_status() {
        return ast_status;
    }

    public void setAst_status(AssetsStatus ast_status) {
        this.ast_status = ast_status;
    }

    public AssetsStatus getAst_used_status() {
        return ast_used_status;
    }

    public void setAst_used_status(AssetsStatus ast_used_status) {
        this.ast_used_status = ast_used_status;
    }

    public String getAst_img_url() {
        return ast_img_url;
    }

    public void setAst_img_url(String ast_img_url) {
        this.ast_img_url = ast_img_url;
    }

    public Integer getAst_material() {
        return ast_material;
    }

    public void setAst_material(Integer ast_material) {
        this.ast_material = ast_material;
    }

    public String getAst_measuring_unit() {
        return ast_measuring_unit;
    }

    public void setAst_measuring_unit(String ast_measuring_unit) {
        this.ast_measuring_unit = ast_measuring_unit;
    }

    public String getAst_sn() {
        return ast_sn;
    }

    public void setAst_sn(String ast_sn) {
        this.ast_sn = ast_sn;
    }

    public AssetsType getType_info() {
        return type_info;
    }

    public void setType_info(AssetsType type_info) {
        this.type_info = type_info;
    }

    public SysUser getUser_info() {
        return user_info;
    }

    public void setUser_info(SysUser user_info) {
        this.user_info = user_info;
    }

    public AssetsStoreResult getStore_info() {
        return store_info;
    }

    public void setStore_info(AssetsStoreResult store_info) {
        this.store_info = store_info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrg_belongcrop_id() {
        return org_belongcrop_id;
    }

    public void setOrg_belongcrop_id(String org_belongcrop_id) {
        this.org_belongcrop_id = org_belongcrop_id;
    }

    public String getOrg_belongdept_id() {
        return org_belongdept_id;
    }

    public void setOrg_belongdept_id(String org_belongdept_id) {
        this.org_belongdept_id = org_belongdept_id;
    }
}