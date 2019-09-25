package com.common.esimrfid.core.bean.nanhua.requisitionbeans;

import java.util.Date;

public class RequisitionItemInfo {

    /**
     * assetsInfo : []
     * creator : {"create_date":1565750047000,"id":"ljh_id","tenant_id":"tenantid0001","update_date":1566528532000,"user_empcode":"","user_gender":"女","user_mobile":"15678595632","user_name":"ljh","user_password":"e10adc3949ba59abbe56e057f20f883e","user_real_name":"Lisa","user_status":"0"}
     * creator_id : ljh_id
     * id : 367045f947c111e9aa47b052166c710d
     * odr_code : LY201903160006
     * odr_date : 1552723078000
     * odr_status : 资管员审批
     * odr_task_id : 37541be3-47c1-11e9-bc38-c85b762eb9b9
     * odr_task_name : 资管员审批
     * odr_transactor_id : ljh_id
     * odr_type : 领用单
     * reqUser : {"create_date":1565750047000,"id":"ljh_id","tenant_id":"tenantid0001","update_date":1566528532000,"user_empcode":"","user_gender":"女","user_mobile":"15678595632","user_name":"ljh","user_password":"e10adc3949ba59abbe56e057f20f883e","user_real_name":"Lisa","user_status":"0"}
     * req_usedept_id : hua_liao_ke
     * req_user_id : ljh_id
     * transactor : {"create_date":1565750047000,"id":"ljh_id","tenant_id":"tenantid0001","update_date":1566528532000,"user_empcode":"","user_gender":"女","user_mobile":"15678595632","user_name":"ljh","user_password":"e10adc3949ba59abbe56e057f20f883e","user_real_name":"Lisa","user_status":"0"}
     */

    private CreatorBean creator;
    private String odr_code;
    private Date odr_date;
    private String odr_status;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CreatorBean getCreator() {
        return creator;
    }

    public void setCreator(CreatorBean creator) {
        this.creator = creator;
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

    public String getOdr_status() {
        return odr_status;
    }

    public void setOdr_status(String odr_status) {
        this.odr_status = odr_status;
    }

    public ReqUserBean getReqUser() {
        return reqUser;
    }

    public void setReqUser(ReqUserBean reqUser) {
        this.reqUser = reqUser;
    }

    private ReqUserBean reqUser;

    public static class CreatorBean {
        /**
         * create_date : 1565750047000
         * id : ljh_id
         * tenant_id : tenantid0001
         * update_date : 1566528532000
         * user_empcode :
         * user_gender : 女
         * user_mobile : 15678595632
         * user_name : ljh
         * user_password : e10adc3949ba59abbe56e057f20f883e
         * user_real_name : Lisa
         * user_status : 0
         */

        private String user_name;
        private String user_real_name;

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

    public static class ReqUserBean {
        /**
         * create_date : 1565750047000
         * id : ljh_id
         * tenant_id : tenantid0001
         * update_date : 1566528532000
         * user_empcode :
         * user_gender : 女
         * user_mobile : 15678595632
         * user_name : ljh
         * user_password : e10adc3949ba59abbe56e057f20f883e
         * user_real_name : Lisa
         * user_status : 0
         */

        private String user_name;
        private String user_real_name;

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
