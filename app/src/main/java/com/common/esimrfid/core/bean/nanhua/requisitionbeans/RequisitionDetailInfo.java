package com.common.esimrfid.core.bean.nanhua.requisitionbeans;

import java.util.Date;
import java.util.List;

public class RequisitionDetailInfo {
    private CreatorBean creator;
    private String odr_code;
    private Date odr_date;
    private String odr_status;
    private String id;
    private List<RequisitionAssetInfo> assetsInfo;
    private List<ItemsBean> req_detail;

    public List<RequisitionAssetInfo> getAssetsInfo() {
        return assetsInfo;
    }

    public void setAssetsInfo(List<RequisitionAssetInfo> assetsInfo) {
        this.assetsInfo = assetsInfo;
    }
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

    public RequisitionItemInfo.ReqUserBean getReqUser() {
        return reqUser;
    }

    public void setReqUser(RequisitionItemInfo.ReqUserBean reqUser) {
        this.reqUser = reqUser;
    }

    private RequisitionItemInfo.ReqUserBean reqUser;

    public List<ItemsBean> getReq_detail() {
        return req_detail;
    }

    public void setReq_detail(List<ItemsBean> items) {
        this.req_detail = items;
    }

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

    public static class ReqDetailBean {
        /**
         * name : 用途
         * id : 1
         * type : text
         * value : 办公
         */

        private String name;
        private String id;
        private String type;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class ItemsBean {
        /**
         * code : string
         * formFields : [{"id":"string","name":"string","type":"string","value":"string"}]
         * name : string
         */

        private String code;
        private String name;
        private List<ReqDetailBean> formFields;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ReqDetailBean> getFormFields() {
            return formFields;
        }

        public void setFormFields(List<ReqDetailBean> formFields) {
            this.formFields = formFields;
        }
    }
}
