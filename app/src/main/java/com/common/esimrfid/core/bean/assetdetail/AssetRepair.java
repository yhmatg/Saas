package com.common.esimrfid.core.bean.assetdetail;

public class AssetRepair {
    /**
     * create_date : 1584948883000
     * creator : {"user_mobile":"13635302912","user_name":"testadmin","user_real_name":"超级管理员"}
     * creator_id : 94b27def173811eaabcf00163e0a6695
     * id : 51e0e12900be4352a2bbff09a7882a77
     * maintain_discription : 电脑损坏
     * maintain_finish_date : 1584948910000
     * maintain_price : 100.0
     * odr_code : WX20200323000006
     * odr_date : 1584948883000
     * odr_status : 审批中
     * odr_transactor_id : 94b27def173811eaabcf00163e0a6695
     * odr_type : 员工申请报修
     * repUser : {"user_mobile":"18621521588","user_name":"18621521588","user_real_name":"杜甫"}
     * rep_user_id : 830191273e654f599d4a9e577adc2ff3
     * transactor : {"user_mobile":"13635302912","user_name":"testadmin","user_real_name":"超级管理员"}
     * updater : {"user_mobile":"13635302912","user_name":"testadmin","user_real_name":"超级管理员"}
     * updater_id : 94b27def173811eaabcf00163e0a6695
     */

    private long create_date;
    private CreatorBean creator;
    private String id;
    private long maintain_finish_date;
    private double maintain_price;
    private String odr_code;
    private long odr_date;
    private String odr_status;
    private String odr_remark;
    private RepUserBean repUser;
    private TransactorBean transactor;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getMaintain_finish_date() {
        return maintain_finish_date;
    }

    public void setMaintain_finish_date(long maintain_finish_date) {
        this.maintain_finish_date = maintain_finish_date;
    }

    public double getMaintain_price() {
        return maintain_price;
    }

    public void setMaintain_price(double maintain_price) {
        this.maintain_price = maintain_price;
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

    public String getOdr_status() {
        return odr_status;
    }

    public void setOdr_status(String odr_status) {
        this.odr_status = odr_status;
    }

    public RepUserBean getRepUser() {
        return repUser;
    }

    public void setRepUser(RepUserBean repUser) {
        this.repUser = repUser;
    }

    public TransactorBean getTransactor() {
        return transactor;
    }

    public void setTransactor(TransactorBean transactor) {
        this.transactor = transactor;
    }

    public String getOdr_remark() {
        return odr_remark;
    }

    public void setOdr_remark(String odr_remark) {
        this.odr_remark = odr_remark;
    }

    public static class CreatorBean {
        /**
         * user_mobile : 13635302912
         * user_name : testadmin
         * user_real_name : 超级管理员
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

    public static class RepUserBean {
        /**
         * 报修人
         * user_mobile : 18621521588
         * user_name : 18621521588
         * user_real_name : 杜甫
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

    public static class TransactorBean {
        /**
         * 处理人
         * user_mobile : 13635302912
         * user_name : testadmin
         * user_real_name : 超级管理员
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
