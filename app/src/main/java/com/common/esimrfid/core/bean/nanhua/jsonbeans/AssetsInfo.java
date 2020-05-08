package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//资产详情
@Entity
public class AssetsInfo {

    /**
     * ast_brand :
     * ast_code : dsdsds
     * ast_epc_code : 647364736473
     * ast_material : 0
     * ast_model :
     * ast_name : 笔记本电脑
     * ast_sn :
     * ast_status : 0
     * ast_used_status : 2
     * create_date : 1573615599000
     * creator_id : admin_id
     * id : b8b39374709c4345bcba7781550f07b0
     * loc_id : 1
     * loc_info : {"loc_code":"0004","loc_name":"16号楼行政部"}
     * org_belongcorp_id :
     * org_useddept : {"id":"6990f769cefc11e9abc50242ac110002","org_name":"学习用品部门"}
     * org_useddept_id : 6990f769cefc11e9abc50242ac110002
     * type_id : un_assigned
     * update_date : 1573627276000
     * user_id : 862514b87d3a42a6b709aa08667cc677
     * user_info : {"id":"862514b87d3a42a6b709aa08667cc677","user_name":"test","user_real_name":"test"}
     */

    private String ast_brand;
    private String ast_barcode;
    private String ast_epc_code;
    private String ast_model;
    private String ast_name;
    @PrimaryKey
    @NonNull
    private String id;
    @Embedded
    private LocInfo loc_info;

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

    public LocInfo getLoc_info() {
        return loc_info;
    }

    public void setLoc_info(LocInfo loc_info) {
        this.loc_info = loc_info;
    }

    public static class LocInfo {
        /**
         * loc_code : 0004
         * loc_name : 16号楼行政部
         */
        @PrimaryKey
        private String loc_code;
        private String loc_name;

        public String getLoc_code() {
            return loc_code;
        }

        public void setLoc_code(String loc_code) {
            this.loc_code = loc_code;
        }

        public String getLoc_name() {
            return loc_name;
        }

        public void setLoc_name(String loc_name) {
            this.loc_name = loc_name;
        }
    }
}