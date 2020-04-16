package com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans;

import java.util.Date;

public class RequisitionItemInfo {
    /**
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

    private Date create_date;
    private Creator creator;
    private String id;
    private String odr_code;
    private Date odr_date;
    private String odr_remark;
    private String odr_status;
    private ReqUser reqUser;

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
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

    public Date getOdr_date() {
        return odr_date;
    }

    public void setOdr_date(Date odr_date) {
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
