package com.common.esimrfid.core.bean.nanhua.invdetailbeans;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
public class InventoryDetail implements Serializable {

    //need
    @PrimaryKey
    @NonNull
    private String id;
    //need
    @Embedded
    private AssetsInfo assetsInfos;
    //need
    private String ast_id;
    //need
    private String inv_id;
    //need
    @Embedded
    private AssetsCheckStatus invdt_status;
    //need
    private String invdt_remark;
    //need
    private Date create_date;
    //need
    private Date update_date;

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public AssetsInfo getAssetsInfos() {
        return assetsInfos;
    }

    public void setAssetsInfos(AssetsInfo assetsInfos) {
        this.assetsInfos = assetsInfos;
    }

    public String getInvdt_remark() {
        return invdt_remark;
    }

    public void setInvdt_remark(String invdt_remark) {
        this.invdt_remark = invdt_remark;
    }

    public AssetsInfo getAssets_info() {
        return assetsInfos;
    }

    public void setAssets_info(AssetsInfo assetsInfos) {
        this.assetsInfos = assetsInfos;
    }

    public String getAst_id() {
        return ast_id;
    }

    public void setAst_id(String ast_id) {
        this.ast_id = ast_id;
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

    public AssetsCheckStatus getInvdt_status() {
        return invdt_status;
    }

    public void setInvdt_status(AssetsCheckStatus invdt_status) {
        this.invdt_status = invdt_status;
    }

    public static class ManagerUser {

        private String id;
        private String mgr_mobile;
        private String mgr_name;
        private String mgr_passwd;
        private long reg_date;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setMgr_mobile(String mgr_mobile) {
            this.mgr_mobile = mgr_mobile;
        }
        public String getMgr_mobile() {
            return mgr_mobile;
        }

        public void setMgr_name(String mgr_name) {
            this.mgr_name = mgr_name;
        }
        public String getMgr_name() {
            return mgr_name;
        }

        public void setMgr_passwd(String mgr_passwd) {
            this.mgr_passwd = mgr_passwd;
        }
        public String getMgr_passwd() {
            return mgr_passwd;
        }

        public void setReg_date(long reg_date) {
            this.reg_date = reg_date;
        }
        public long getReg_date() {
            return reg_date;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryDetail that = (InventoryDetail) o;
        return id.equals(that.id) &&
                ast_id.equals(that.ast_id) &&
                inv_id.equals(that.inv_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ast_id, inv_id);
    }
}
