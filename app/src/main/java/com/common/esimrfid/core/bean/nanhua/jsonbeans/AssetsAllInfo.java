package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * 资产详细信息（all）
 */
@Entity
public class AssetsAllInfo {

    /**
     * ast_barcode : 200810000871
     * ast_brand : 苹果
     * ast_buy_date : 1596988800000
     * ast_epc_code : E22020081056726747150202
     * ast_expiration_months : 12
     * ast_img_url :
     * ast_material : 0
     * ast_measuring_unit : 台
     * ast_model : MacBookPro
     * ast_name : 笔记本电脑
     * ast_price : 12344.0
     * ast_remark : 1111
     * ast_req_date : 1597075200000
     * ast_sn : 2313131
     * ast_source : 购入
     * ast_status : 1
     * ast_used_status : 1
     * create_date : 1597048131000
     * creator_id : 85ddbc5649cb4131adfa1db222fd9d9b
     * id : 76deb3c55ae946728c95ab953115f1b3
     * loc_id : 56e85ccab06111ea891c000c29a8812b
     * loc_name : 一楼
     * manager_id : 85ddbc5649cb4131adfa1db222fd9d9b
     * manager_name : 周
     * org_belongcorp_id : 66fa07447fcb449eb251db1427e9ec1b
     * org_belongcorp_name : 一芯1
     * org_usedcorp_id : 66fa07447fcb449eb251db1427e9ec1b
     * org_usedcorp_name : 一芯1
     * org_useddept_id : 0cf1c7574e6541a1983b5e134e5d302d
     * org_useddept_name : 财务部123
     * supplier_mobile : 15511112222
     * supplier_name : 苹果
     * supplier_person : 小张
     * type_id : a4bd3fa8a6314a0f905e3dc77b040de6
     * type_name : 笔记本1
     * update_date : 1597048131000
     * updater_id : 85ddbc5649cb4131adfa1db222fd9d9b
     * user_id : 5c4ad11c9b7f43a28c46adfa752a24ba
     * user_name : 小名
     * war_enddate : 1597161600000
     * war_message : 嘛嘛嘛麻麻
     * warranty_id : 688d122a3bff43d2bb4937df3b578341
     */

    private String ast_barcode;
    private String ast_brand;
    private long ast_buy_date;
    private String ast_epc_code;
    private String ast_expiration_months;
    private String ast_img_url;
    private int ast_material;
    private String ast_measuring_unit;
    private String ast_model;
    private String ast_name;
    private double ast_price;
    private String ast_remark;
    private long ast_req_date;
    private String ast_sn;
    private String ast_source;
    private int ast_status;
    private int ast_used_status;
    private long create_date;
    private String creator_id;
    @PrimaryKey
    @NonNull
    private String id;
    private String loc_id;
    private String loc_name;
    private String manager_id;
    private String manager_name;
    private String org_belongcorp_id;
    private String org_belongcorp_name;
    private String org_usedcorp_id;
    private String org_usedcorp_name;
    private String org_useddept_id;
    private String org_useddept_name;
    private String supplier_mobile;
    private String supplier_name;
    private String supplier_person;
    private String type_id;
    private String type_name;
    private long update_date;
    private String updater_id;
    private String user_id;
    private String user_name;
    private long war_enddate;
    private String war_message;
    private String warranty_id;
    @Ignore
    private HashMap<String, String> ast_append_info;
    @Ignore
    private long ast_instore_date;
    @Ignore
    private int print_count;

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

    public long getAst_buy_date() {
        return ast_buy_date;
    }

    public void setAst_buy_date(long ast_buy_date) {
        this.ast_buy_date = ast_buy_date;
    }

    public String getAst_epc_code() {
        return ast_epc_code;
    }

    public void setAst_epc_code(String ast_epc_code) {
        this.ast_epc_code = ast_epc_code;
    }

    public String getAst_expiration_months() {
        return ast_expiration_months;
    }

    public void setAst_expiration_months(String ast_expiration_months) {
        this.ast_expiration_months = ast_expiration_months;
    }

    public String getAst_img_url() {
        return ast_img_url;
    }

    public void setAst_img_url(String ast_img_url) {
        this.ast_img_url = ast_img_url;
    }

    public int getAst_material() {
        return ast_material;
    }

    public void setAst_material(int ast_material) {
        this.ast_material = ast_material;
    }

    public String getAst_measuring_unit() {
        return ast_measuring_unit;
    }

    public void setAst_measuring_unit(String ast_measuring_unit) {
        this.ast_measuring_unit = ast_measuring_unit;
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

    public double getAst_price() {
        return ast_price;
    }

    public void setAst_price(double ast_price) {
        this.ast_price = ast_price;
    }

    public String getAst_remark() {
        return ast_remark;
    }

    public void setAst_remark(String ast_remark) {
        this.ast_remark = ast_remark;
    }

    public long getAst_req_date() {
        return ast_req_date;
    }

    public void setAst_req_date(long ast_req_date) {
        this.ast_req_date = ast_req_date;
    }

    public String getAst_sn() {
        return ast_sn;
    }

    public void setAst_sn(String ast_sn) {
        this.ast_sn = ast_sn;
    }

    public String getAst_source() {
        return ast_source;
    }

    public void setAst_source(String ast_source) {
        this.ast_source = ast_source;
    }

    public int getAst_status() {
        return ast_status;
    }

    public void setAst_status(int ast_status) {
        this.ast_status = ast_status;
    }

    public int getAst_used_status() {
        return ast_used_status;
    }

    public void setAst_used_status(int ast_used_status) {
        this.ast_used_status = ast_used_status;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getOrg_belongcorp_id() {
        return org_belongcorp_id;
    }

    public void setOrg_belongcorp_id(String org_belongcorp_id) {
        this.org_belongcorp_id = org_belongcorp_id;
    }

    public String getOrg_belongcorp_name() {
        return org_belongcorp_name;
    }

    public void setOrg_belongcorp_name(String org_belongcorp_name) {
        this.org_belongcorp_name = org_belongcorp_name;
    }

    public String getOrg_usedcorp_id() {
        return org_usedcorp_id;
    }

    public void setOrg_usedcorp_id(String org_usedcorp_id) {
        this.org_usedcorp_id = org_usedcorp_id;
    }

    public String getOrg_usedcorp_name() {
        return org_usedcorp_name;
    }

    public void setOrg_usedcorp_name(String org_usedcorp_name) {
        this.org_usedcorp_name = org_usedcorp_name;
    }

    public String getOrg_useddept_id() {
        return org_useddept_id;
    }

    public void setOrg_useddept_id(String org_useddept_id) {
        this.org_useddept_id = org_useddept_id;
    }

    public String getOrg_useddept_name() {
        return org_useddept_name;
    }

    public void setOrg_useddept_name(String org_useddept_name) {
        this.org_useddept_name = org_useddept_name;
    }

    public String getSupplier_mobile() {
        return supplier_mobile;
    }

    public void setSupplier_mobile(String supplier_mobile) {
        this.supplier_mobile = supplier_mobile;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_person() {
        return supplier_person;
    }

    public void setSupplier_person(String supplier_person) {
        this.supplier_person = supplier_person;
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

    public long getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(long update_date) {
        this.update_date = update_date;
    }

    public String getUpdater_id() {
        return updater_id;
    }

    public void setUpdater_id(String updater_id) {
        this.updater_id = updater_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public long getWar_enddate() {
        return war_enddate;
    }

    public void setWar_enddate(long war_enddate) {
        this.war_enddate = war_enddate;
    }

    public String getWar_message() {
        return war_message;
    }

    public void setWar_message(String war_message) {
        this.war_message = war_message;
    }

    public String getWarranty_id() {
        return warranty_id;
    }

    public void setWarranty_id(String warranty_id) {
        this.warranty_id = warranty_id;
    }

    public HashMap<String, String> getAst_append_info() {
        return ast_append_info;
    }

    public void setAst_append_info(HashMap<String, String> ast_append_info) {
        this.ast_append_info = ast_append_info;
    }

    public long getAst_instore_date() {
        return ast_instore_date;
    }

    public void setAst_instore_date(long ast_instore_date) {
        this.ast_instore_date = ast_instore_date;
    }

    public int getPrint_count() {
        return print_count;
    }

    public void setPrint_count(int print_count) {
        this.print_count = print_count;
    }
}
