package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import android.arch.persistence.room.Embedded;

import java.util.Objects;

public class AssetsListItemInfo {

    private String ast_barcode;
    private String ast_name;
    private String id;
    @Embedded(prefix = "loc_info_")
    private LocInfo loc_info;
    @Embedded(prefix = "userinfo_")
    private UserInfo user_info;
    private double ast_price;
    private int ast_used_status;
    private long ast_buy_date;

    public String getAst_barcode() {
        return ast_barcode;
    }

    public void setAst_barcode(String ast_barcode) {
        this.ast_barcode = ast_barcode;
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

    public double getAst_price() {
        return ast_price;
    }

    public void setAst_price(double ast_price) {
        this.ast_price = ast_price;
    }

    public int getAst_used_status() {
        return ast_used_status;
    }

    public void setAst_used_status(int ast_used_status) {
        this.ast_used_status = ast_used_status;
    }

    public LocInfo getLoc_info() {
        return loc_info;
    }

    public void setLoc_info(LocInfo loc_info) {
        this.loc_info = loc_info;
    }

    public UserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfo user_info) {
        this.user_info = user_info;
    }

    public long getAst_buy_date() {
        return ast_buy_date;
    }

    public void setAst_buy_date(long ast_buy_date) {
        this.ast_buy_date = ast_buy_date;
    }

    public static class LocInfo {
        /**
         * loc_code : 0004
         * loc_name : 16号楼行政部
         */
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

    public static class UserInfo {
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
        private String id;
        private String user_real_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_real_name() {
            return user_real_name;
        }

        public void setUser_real_name(String user_real_name) {
            this.user_real_name = user_real_name;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetsListItemInfo)) return false;
        AssetsListItemInfo that = (AssetsListItemInfo) o;
        return getAst_barcode().equals(that.getAst_barcode()) &&
                getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAst_barcode(), getId());
    }
}
