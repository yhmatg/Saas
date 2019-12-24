package com.common.esimrfid.core.bean.nanhua.jsonbeans;

/**
 * 资产详细信息（all）
 */
public class AssetsDetailsInfo {
    /**
     * ast_barcode : 201912190192
     * ast_brand : www
     * ast_buy_date : 1576339200000
     * ast_epc_code : E20157672247817741090202
     * ast_expiration_months : 22
     * ast_img_url :
     * ast_material : 0
     * ast_measuring_unit : 123
     * ast_model : www
     * ast_name : wang
     * ast_price : 100000
     * ast_remark : abc
     * ast_req_date : 1576771200000
     * ast_sn : 456
     * ast_source : 购入
     * ast_status : 1
     * ast_used_status : 1
     * create_date : 1576722478000
     * creator : {"create_date":1573120126000,"id":"caddb5d8014311eab97300163e086c26","tenant_id":"tenantid0001","update_date":1575665586000,"user_mobile":"18408002929","user_name":"admin","user_password":"e10adc3949ba59abbe56e057f20f883e","user_real_name":"用户003","user_status":"0"}
     * creator_id : caddb5d8014311eab97300163e086c26
     * id : 9e55e4fb17d34870a24dd41f5b449a7b
     * loc_id : 20ec55960f6611eaabcf00163e0a6695
     * loc_info : {"id":"20ec55960f6611eaabcf00163e0a6695","loc_name":"位置2"}
     * manager_id : caddb5d8014311eab97300163e086c26
     * org_belongcorp : {"id":"cadd4999014311eab97300163e086c26","org_name":"企业试用"}
     * org_belongcorp_id : cadd4999014311eab97300163e086c26
     * org_usedcorp : {"id":"cadd4999014311eab97300163e086c26","org_name":"企业试用"}
     * org_usedcorp_id : cadd4999014311eab97300163e086c26
     * org_useddept : {"id":"7ce3d05214ee11eaa0530242ac130003","org_name":"蜀国"}
     * org_useddept_id : 7ce3d05214ee11eaa0530242ac130003
     * type_id : 4f958d831be711eaa0530242ac130003
     * type_info : {"id":"4f958d831be711eaa0530242ac130003","type_name":"新类型"}
     * update_date : 1576694144000
     * updater : {"create_date":1573120126000,"id":"caddb5d8014311eab97300163e086c26","tenant_id":"tenantid0001","update_date":1575665586000,"user_mobile":"18408002929","user_name":"admin","user_password":"e10adc3949ba59abbe56e057f20f883e","user_real_name":"用户003","user_status":"0"}
     * updater_id : caddb5d8014311eab97300163e086c26
     * user_id : f98470d686f54585986b45938c5bf532
     * user_info : {"create_date":1576656840000,"id":"f98470d686f54585986b45938c5bf532","tenant_id":"tenantid0001","update_date":1576628040000,"user_age":"","user_avatar":"","user_email":"575983443@qq.com","user_empcode":"21212","user_gender":"","user_mobile":"15579818972","user_name":"15579818972","user_real_name":"周杰伦","user_status":"0"}
     * warranty_id : 0af28fcc703a4c35bc508b659036114d
     * warranty_info : {"id":"0af28fcc703a4c35bc508b659036114d","supplier_mobile":"13312344321","supplier_name":"sss","supplier_person":"weed","war_enddate":1577116800000,"war_message":"enenen"}
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
    private int ast_price;
    private String ast_remark;
    private long ast_req_date;
    private String ast_sn;
    private String ast_source;
    private int ast_status;
    private int ast_used_status;
    private long create_date;
    private CreatorBean creator;
    private String creator_id;
    private String id;
    private String loc_id;
    private LocInfoBean loc_info;
    private String manager_id;
    private OrgBelongcorpBean org_belongcorp;
    private String org_belongcorp_id;
    private OrgUsedcorpBean org_usedcorp;
    private String org_usedcorp_id;
    private OrgUseddeptBean org_useddept;
    private String org_useddept_id;
    private String type_id;
    private TypeInfoBean type_info;
    private long update_date;
    private UpdaterBean updater;
    private String updater_id;
    private String user_id;
    private UserInfoBean user_info;
    private String warranty_id;
    private WarrantyInfoBean warranty_info;

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

    public int getAst_price() {
        return ast_price;
    }

    public void setAst_price(int ast_price) {
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

    public CreatorBean getCreator() {
        return creator;
    }

    public void setCreator(CreatorBean creator) {
        this.creator = creator;
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

    public LocInfoBean getLoc_info() {
        return loc_info;
    }

    public void setLoc_info(LocInfoBean loc_info) {
        this.loc_info = loc_info;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public OrgBelongcorpBean getOrg_belongcorp() {
        return org_belongcorp;
    }

    public void setOrg_belongcorp(OrgBelongcorpBean org_belongcorp) {
        this.org_belongcorp = org_belongcorp;
    }

    public String getOrg_belongcorp_id() {
        return org_belongcorp_id;
    }

    public void setOrg_belongcorp_id(String org_belongcorp_id) {
        this.org_belongcorp_id = org_belongcorp_id;
    }

    public OrgUsedcorpBean getOrg_usedcorp() {
        return org_usedcorp;
    }

    public void setOrg_usedcorp(OrgUsedcorpBean org_usedcorp) {
        this.org_usedcorp = org_usedcorp;
    }

    public String getOrg_usedcorp_id() {
        return org_usedcorp_id;
    }

    public void setOrg_usedcorp_id(String org_usedcorp_id) {
        this.org_usedcorp_id = org_usedcorp_id;
    }

    public OrgUseddeptBean getOrg_useddept() {
        return org_useddept;
    }

    public void setOrg_useddept(OrgUseddeptBean org_useddept) {
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

    public TypeInfoBean getType_info() {
        return type_info;
    }

    public void setType_info(TypeInfoBean type_info) {
        this.type_info = type_info;
    }

    public long getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(long update_date) {
        this.update_date = update_date;
    }

    public UpdaterBean getUpdater() {
        return updater;
    }

    public void setUpdater(UpdaterBean updater) {
        this.updater = updater;
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

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public String getWarranty_id() {
        return warranty_id;
    }

    public void setWarranty_id(String warranty_id) {
        this.warranty_id = warranty_id;
    }

    public WarrantyInfoBean getWarranty_info() {
        return warranty_info;
    }

    public void setWarranty_info(WarrantyInfoBean warranty_info) {
        this.warranty_info = warranty_info;
    }

    public static class CreatorBean {
        /**
         * create_date : 1573120126000
         * id : caddb5d8014311eab97300163e086c26
         * tenant_id : tenantid0001
         * update_date : 1575665586000
         * user_mobile : 18408002929
         * user_name : admin
         * user_password : e10adc3949ba59abbe56e057f20f883e
         * user_real_name : 用户003
         * user_status : 0
         */

        private long create_date;
        private String id;
        private String tenant_id;
        private long update_date;
        private String user_mobile;
        private String user_name;
        private String user_password;
        private String user_real_name;
        private String user_status;

        public long getCreate_date() {
            return create_date;
        }

        public void setCreate_date(long create_date) {
            this.create_date = create_date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTenant_id() {
            return tenant_id;
        }

        public void setTenant_id(String tenant_id) {
            this.tenant_id = tenant_id;
        }

        public long getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(long update_date) {
            this.update_date = update_date;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_password() {
            return user_password;
        }

        public void setUser_password(String user_password) {
            this.user_password = user_password;
        }

        public String getUser_real_name() {
            return user_real_name;
        }

        public void setUser_real_name(String user_real_name) {
            this.user_real_name = user_real_name;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }
    }

    public static class LocInfoBean {
        /**
         * id : 20ec55960f6611eaabcf00163e0a6695
         * loc_name : 位置2
         */

        private String id;
        private String loc_name;

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
    }

    public static class OrgBelongcorpBean {
        /**
         * id : cadd4999014311eab97300163e086c26
         * org_name : 企业试用
         */

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

    public static class OrgUsedcorpBean {
        /**
         * id : cadd4999014311eab97300163e086c26
         * org_name : 企业试用
         */

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

    public static class OrgUseddeptBean {
        /**
         * id : 7ce3d05214ee11eaa0530242ac130003
         * org_name : 蜀国
         */

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

    public static class TypeInfoBean {
        /**
         * id : 4f958d831be711eaa0530242ac130003
         * type_name : 新类型
         */

        private String id;
        private String type_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }

    public static class UpdaterBean {
        /**
         * create_date : 1573120126000
         * id : caddb5d8014311eab97300163e086c26
         * tenant_id : tenantid0001
         * update_date : 1575665586000
         * user_mobile : 18408002929
         * user_name : admin
         * user_password : e10adc3949ba59abbe56e057f20f883e
         * user_real_name : 用户003
         * user_status : 0
         */

        private long create_date;
        private String id;
        private String tenant_id;
        private long update_date;
        private String user_mobile;
        private String user_name;
        private String user_password;
        private String user_real_name;
        private String user_status;

        public long getCreate_date() {
            return create_date;
        }

        public void setCreate_date(long create_date) {
            this.create_date = create_date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTenant_id() {
            return tenant_id;
        }

        public void setTenant_id(String tenant_id) {
            this.tenant_id = tenant_id;
        }

        public long getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(long update_date) {
            this.update_date = update_date;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_password() {
            return user_password;
        }

        public void setUser_password(String user_password) {
            this.user_password = user_password;
        }

        public String getUser_real_name() {
            return user_real_name;
        }

        public void setUser_real_name(String user_real_name) {
            this.user_real_name = user_real_name;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }
    }

    public static class UserInfoBean {
        /**
         * create_date : 1576656840000
         * id : f98470d686f54585986b45938c5bf532
         * tenant_id : tenantid0001
         * update_date : 1576628040000
         * user_age :
         * user_avatar :
         * user_email : 575983443@qq.com
         * user_empcode : 21212
         * user_gender :
         * user_mobile : 15579818972
         * user_name : 15579818972
         * user_real_name : 周杰伦
         * user_status : 0
         */

        private long create_date;
        private String id;
        private String tenant_id;
        private long update_date;
        private String user_age;
        private String user_avatar;
        private String user_email;
        private String user_empcode;
        private String user_gender;
        private String user_mobile;
        private String user_name;
        private String user_real_name;
        private String user_status;

        public long getCreate_date() {
            return create_date;
        }

        public void setCreate_date(long create_date) {
            this.create_date = create_date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTenant_id() {
            return tenant_id;
        }

        public void setTenant_id(String tenant_id) {
            this.tenant_id = tenant_id;
        }

        public long getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(long update_date) {
            this.update_date = update_date;
        }

        public String getUser_age() {
            return user_age;
        }

        public void setUser_age(String user_age) {
            this.user_age = user_age;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_empcode() {
            return user_empcode;
        }

        public void setUser_empcode(String user_empcode) {
            this.user_empcode = user_empcode;
        }

        public String getUser_gender() {
            return user_gender;
        }

        public void setUser_gender(String user_gender) {
            this.user_gender = user_gender;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
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

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }
    }

    public static class WarrantyInfoBean {
        /**
         * id : 0af28fcc703a4c35bc508b659036114d
         * supplier_mobile : 13312344321
         * supplier_name : sss
         * supplier_person : weed
         * war_enddate : 1577116800000
         * war_message : enenen
         */

        private String id;
        private String supplier_mobile;
        private String supplier_name;
        private String supplier_person;
        private long war_enddate;
        private String war_message;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    }
}
