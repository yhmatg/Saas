package com.common.xfxj.core.bean.nanhua.jsonbeans;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;
import android.support.annotation.NonNull;

import java.util.Objects;

//一条盘点资产详情
@Entity
public class InventoryDetail {

    /**
     * ast_barcode : 200810000091
     * ast_brand :
     * ast_buy_date : 1596816000000
     * ast_epc_code : E22020081019275169510202
     * ast_id : e203aa6bd2524e5fbfbcb8994bfd30b2
     * ast_material : 0
     * ast_measuring_unit :
     * ast_model :
     * ast_name : 权限分类-电器
     * ast_remark :
     * ast_source : 购入
     * ast_status : 0
     * ast_used_status : 0
     * create_date : 1597111452000
     * creator_id : 6edbaf66d6a84e3a86ac6cb9f4e006e7
     * id : 0863d4c3919b46a8b44fa9d3f24a7c15
     * inv_id : f3802bc7db7611eabdc3000c29a8812b
     * invdt_sign : 标签丢失
     * invdt_status : {"code":0,"index":0,"name":"未盘点"}
     * loc_id : f7cf4aa1d85111ea891c000c29a8812b
     * loc_name : 1楼
     * manager_id : 6edbaf66d6a84e3a86ac6cb9f4e006e7
     * manager_name : 周
     * org_belongcorp_id : 82710adc25d94171a9566cd65701104f
     * org_belongcorp_name : bbbbb
     * org_usedcorp_id : 82710adc25d94171a9566cd65701104f
     * org_usedcorp_name : bbbbb
     * org_useddept_id :
     * type_id : c1472153d85b11ea891c000c29a8812b
     * type_name : 权限分类-电器
     * update_date : 1597141530000
     * updater_id : 6edbaf66d6a84e3a86ac6cb9f4e006e7
     */

    private String ast_barcode;
    private String ast_brand;
    private long ast_buy_date;
    private String ast_epc_code;
    private String ast_id;
    private int ast_material;
    private String ast_measuring_unit;
    private String ast_model;
    private String ast_name;
    private String ast_remark;
    private String ast_source;
    private int ast_status;
    private int ast_used_status;
    private long create_date;
    private String creator_id;
    @PrimaryKey
    @NonNull
    private String id;
    private String inv_id;
    private String invdt_sign;
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded
    private InvdtStatus invdt_status;
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
    private String type_id;
    private String type_name;
    private long update_date;
    private String updater_id;
    private String user_id;
    private String user_name;
    private String invdt_plus_loc_id;
    private String invdt_plus_loc_name;
    //是否已经提交过
    private Boolean needUpload = false;

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

    public String getAst_id() {
        return ast_id;
    }

    public void setAst_id(String ast_id) {
        this.ast_id = ast_id;
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

    public String getAst_remark() {
        return ast_remark;
    }

    public void setAst_remark(String ast_remark) {
        this.ast_remark = ast_remark;
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

    public String getInv_id() {
        return inv_id;
    }

    public void setInv_id(String inv_id) {
        this.inv_id = inv_id;
    }

    public String getInvdt_sign() {
        return invdt_sign;
    }

    public void setInvdt_sign(String invdt_sign) {
        this.invdt_sign = invdt_sign;
    }

    public InvdtStatus getInvdt_status() {
        return invdt_status;
    }

    public void setInvdt_status(InvdtStatus invdt_status) {
        this.invdt_status = invdt_status;
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

    public static class InvdtStatus {
        /**
         * code : 0
         * index : 0
         * name : 未盘点
         */

        private int code;
        private int index;
        private String name;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getInvdt_plus_loc_id() {
        return invdt_plus_loc_id;
    }

    public void setInvdt_plus_loc_id(String invdt_plus_loc_id) {
        this.invdt_plus_loc_id = invdt_plus_loc_id;
    }

    public String getInvdt_plus_loc_name() {
        return invdt_plus_loc_name;
    }

    public void setInvdt_plus_loc_name(String invdt_plus_loc_name) {
        this.invdt_plus_loc_name = invdt_plus_loc_name;
    }

    public Boolean getNeedUpload() {
        return needUpload;
    }

    public void setNeedUpload(Boolean needUpload) {
        this.needUpload = needUpload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryDetail)) return false;
        InventoryDetail that = (InventoryDetail) o;
        return getAst_id().equals(that.getAst_id()) &&
                getId().equals(that.getId()) &&
                getInv_id().equals(that.getInv_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAst_id(), getId(), getInv_id());
    }
}

