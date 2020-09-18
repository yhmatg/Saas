package com.common.xfxj.core.bean.assetdetail;

import java.util.Date;
import java.util.List;

public class AssetRepairParameter {
    private String rep_user_id;
    private String odr_transactor_id;
    private double maintain_price;
    private String odr_remark;
    private Date odr_date;
    private List<String> ast_ids;
    private String odr_type = "维修单";

    public String getRep_user_id() {
        return rep_user_id;
    }

    public void setRep_user_id(String rep_user_id) {
        this.rep_user_id = rep_user_id;
    }

    public String getOdr_transactor_id() {
        return odr_transactor_id;
    }

    public void setOdr_transactor_id(String odr_transactor_id) {
        this.odr_transactor_id = odr_transactor_id;
    }

    public double getMaintain_price() {
        return maintain_price;
    }

    public void setMaintain_price(double maintain_price) {
        this.maintain_price = maintain_price;
    }

    public String getOdr_remark() {
        return odr_remark;
    }

    public void setOdr_remark(String odr_remark) {
        this.odr_remark = odr_remark;
    }

    public Date getOdr_date() {
        return odr_date;
    }

    public void setOdr_date(Date odr_date) {
        this.odr_date = odr_date;
    }

    public List<String> getAst_ids() {
        return ast_ids;
    }

    public void setAst_ids(List<String> ast_ids) {
        this.ast_ids = ast_ids;
    }

    @Override
    public String toString() {
        return "{" +
                "\"ast_ids\":" + getListString() +
                ",\"odr_transactor_id\":\"" + odr_transactor_id + '\"' +
                ",\"odr_type\":\"" + odr_type + '\"' +
                ",\"rep_user_id\":\"" + rep_user_id + '\"' +
                ",\"odr_remark\":\"" + odr_remark + '\"' +
                ",\"maintain_price\":\"" + maintain_price + '\"' +
                ",\"odr_date\":" + odr_date.getTime() +
                '}';
    }

    private String getListString() {
        String astIdString = "[";
        for (int i = 0; i < ast_ids.size(); i++) {
            String astId = ast_ids.get(i);
            astId = "\"" + astId + "\"";
            if (0 < ast_ids.size() - 1) {
                astId += ',';
            }
            astIdString += astId;
        }
        astIdString = astIdString + "]";
        return astIdString;
    }
}
