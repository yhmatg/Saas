package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import java.util.List;

public class RequisitionDetailInfo {

    /**
     * assetsInfo : [{"ast_barcode":"","ast_brand":"test","ast_code":"ZC201908080002","ast_depreciation_date":1559664000000,"ast_epc_code":"5A43323031393038303830303032","ast_expiration_date":1591286400000,"ast_expiration_months":"12","ast_img_url":"","ast_material":0,"ast_measuring_unit":"","ast_model":"123","ast_name":"热污染","ast_price":1200,"ast_remark":"","ast_sn":"","ast_status":0,"ast_used_status":0,"create_date":1565236207000,"creator_id":"admin_id","id":"0b243eddc04e43628e2ab565bd67b577","manager_id":"admin_id","org_belongcorp":{"id":"fasVSVrdsvvad","org_name":"南华医院"},"org_belongcorp_id":"fasVSVrdsvvad","org_belongdept_id":"xin_nao_ke","org_useddept_id":"","type_id":"2fb31aeeb7fc11e9a4ae0242ac110002","type_info":{"id":"2fb31aeeb7fc11e9a4ae0242ac110002","type_code":"0004","type_name":"新类型"},"update_date":1573717704000,"updater_id":"admin_id","user_id":"","user_info":{"id":""}}]
     * create_date : 1573784893000
     * creator : {"create_date":1555397980000,"id":"admin_id","tenant_id":"tenantid0001","update_date":1569467842000,"user_gender":"","user_mobile":"13656453456","user_name":"admin","user_password":"e10adc3949ba59abbe56e057f20f883e","user_real_name":"张三","user_status":"0","wx_userid":"oCMk75PYcynKzlNMG3hH70vs8W-Q"}
     * creator_id : admin_id
     * id : 1
     * odr_code : LY001
     * odr_date : 1573784759000
     * odr_remark : 领用单
     * odr_status : 已完成
     * odr_transactor_id : admin_id
     * odr_type : 领用单
     * org_usedcorp : {"id":"fasVSVrdsvvad","org_name":"南华医院"}
     * org_useddept : {"id":"32f6f0ae074e11ea8f82258b4dde74a9","org_name":"研发部"}
     * reqUser : {"create_date":1555397980000,"id":"admin_id","tenant_id":"tenantid0001","update_date":1569467842000,"user_gender":"","user_mobile":"13656453456","user_name":"admin","user_password":"e10adc3949ba59abbe56e057f20f883e","user_real_name":"张三","user_status":"0","wx_userid":"oCMk75PYcynKzlNMG3hH70vs8W-Q"}
     * req_return_time : 1573784880000
     * req_usecomp_id : fasVSVrdsvvad
     * req_usedept_id : 32f6f0ae074e11ea8f82258b4dde74a9
     * req_user_id : admin_id
     * update_date : 1573784900000
     * updater : {"create_date":1555397980000,"id":"admin_id","tenant_id":"tenantid0001","update_date":1569467842000,"user_gender":"","user_mobile":"13656453456","user_name":"admin","user_password":"e10adc3949ba59abbe56e057f20f883e","user_real_name":"张三","user_status":"0","wx_userid":"oCMk75PYcynKzlNMG3hH70vs8W-Q"}
     * updater_id : admin_id
     */

    private long create_date;
    private Creator creator;
    private String id;
    private String odr_code;
    private long odr_date;
    private String odr_remark;
    private String odr_status;
    private ReqUser reqUser;
    private List<RequisitionAssetInfo> assetsInfo;

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOdr_code() {
        return odr_code;
    }

    public void setOdr_code(String odr_code) {
        this.odr_code = odr_code;
    }

    public long getOdr_date() {
        return odr_date;
    }

    public void setOdr_date(long odr_date) {
        this.odr_date = odr_date;
    }

    public String getOdr_remark() {
        return odr_remark;
    }

    public void setOdr_remark(String odr_remark) {
        this.odr_remark = odr_remark;
    }

    public String getOdr_status() {
        return odr_status;
    }

    public void setOdr_status(String odr_status) {
        this.odr_status = odr_status;
    }

    public ReqUser getReqUser() {
        return reqUser;
    }

    public void setReqUser(ReqUser reqUser) {
        this.reqUser = reqUser;
    }

    public List<RequisitionAssetInfo> getAssetsInfo() {
        return assetsInfo;
    }

    public void setAssetsInfo(List<RequisitionAssetInfo> assetsInfo) {
        this.assetsInfo = assetsInfo;
    }

    public static class Creator {
        /**
         * create_date : 1555397980000
         * id : admin_id
         * tenant_id : tenantid0001
         * update_date : 1569467842000
         * user_gender :
         * user_mobile : 13656453456
         * user_name : admin
         * user_password : e10adc3949ba59abbe56e057f20f883e
         * user_real_name : 张三
         * user_status : 0
         * wx_userid : oCMk75PYcynKzlNMG3hH70vs8W-Q
         */
        private String id;
        private String user_mobile;
        private String user_name;
        private String user_real_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    }

    public static class OrgUsedcorp {
        /**
         * id : fasVSVrdsvvad
         * org_name : 南华医院
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

    public static class OrgUseddept {
        /**
         * id : 32f6f0ae074e11ea8f82258b4dde74a9
         * org_name : 研发部
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

    public static class ReqUser {
        /**
         * create_date : 1555397980000
         * id : admin_id
         * tenant_id : tenantid0001
         * update_date : 1569467842000
         * user_gender :
         * user_mobile : 13656453456
         * user_name : admin
         * user_password : e10adc3949ba59abbe56e057f20f883e
         * user_real_name : 张三
         * user_status : 0
         * wx_userid : oCMk75PYcynKzlNMG3hH70vs8W-Q
         */
        private String id;
        private String user_mobile;
        private String user_name;
        private String user_real_name;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    }
}
