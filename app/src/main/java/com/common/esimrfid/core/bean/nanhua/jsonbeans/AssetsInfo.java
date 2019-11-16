package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

//资产详情
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
    private String ast_img_url;
    private Integer ast_material;
    private String ast_model;
    private String ast_name;
    private String ast_sn;
    private Integer ast_status;
    private Integer ast_used_status;
    private Date create_date;
    private String creator_id;
    private String id;
    private String loc_id;
    @Embedded
    private LocInfo loc_info;
    private String org_belongcorp_id;
    @Embedded(prefix = "morg_useddept_")
    private OrgUseddept org_useddept;
    private String org_useddept_id;
    private String type_id;
    @Embedded
    private TypeInfo type_info;
    private Date update_date;
    private String user_id;
    @Embedded(prefix = "user_info_")
    private UserInfo user_info;

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

    public String getAst_sn() {
        return ast_sn;
    }

    public void setAst_sn(String ast_sn) {
        this.ast_sn = ast_sn;
    }

    public Integer getAst_status() {
        return ast_status;
    }

    public void setAst_status(Integer ast_status) {
        this.ast_status = ast_status;
    }

    public Integer getAst_used_status() {
        return ast_used_status;
    }

    public void setAst_used_status(Integer ast_used_status) {
        this.ast_used_status = ast_used_status;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
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

    public LocInfo getLoc_info() {
        return loc_info;
    }

    public void setLoc_info(LocInfo loc_info) {
        this.loc_info = loc_info;
    }

    public String getOrg_belongcorp_id() {
        return org_belongcorp_id;
    }

    public void setOrg_belongcorp_id(String org_belongcorp_id) {
        this.org_belongcorp_id = org_belongcorp_id;
    }

    public OrgUseddept getOrg_useddept() {
        return org_useddept;
    }

    public void setOrg_useddept(OrgUseddept org_useddept) {
        this.org_useddept = org_useddept;
    }

    public String getOrg_useddept_id() {
        return org_useddept_id;
    }

    public void setOrg_useddept_id(String org_useddept_id) {
        this.org_useddept_id = org_useddept_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public TypeInfo getType_info() {
        return type_info;
    }

    public void setType_info(TypeInfo type_info) {
        this.type_info = type_info;
    }
    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfo user_info) {
        this.user_info = user_info;
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

    public static class OrgUseddept {
        /**
         * id : 6990f769cefc11e9abc50242ac110002
         * org_name : 学习用品部门
         */
        @PrimaryKey
        @NonNull
        private String id;
        private String org_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrg_name() {
            return org_name;
        }

        public void setOrg_name(String org_name) {
            this.org_name = org_name;
        }
    }

    public static class TypeInfo {
        /**
         * type_code : 00040002
         * type_img_url :
         * type_name : 电视机
         */

        private String type_code;
        private String type_img_url;
        private String type_name;

        public String getType_code() {
            return type_code;
        }

        public void setType_code(String type_code) {
            this.type_code = type_code;
        }

        public String getType_img_url() {
            return type_img_url;
        }

        public void setType_img_url(String type_img_url) {
            this.type_img_url = type_img_url;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }

    public static class UserInfo {
        /**
         * id : 862514b87d3a42a6b709aa08667cc677
         * user_name : test
         * user_real_name : test
         */
        @PrimaryKey
        @NonNull
        private String id;
        private String user_name;
        private String user_real_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_real_name() {
            return user_real_name;
        }

        public void setUser_real_name(String user_real_name) {
            this.user_real_name = user_real_name;
        }
    }
}
