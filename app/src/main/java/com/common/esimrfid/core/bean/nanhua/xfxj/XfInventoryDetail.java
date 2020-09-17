package com.common.esimrfid.core.bean.nanhua.xfxj;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * 资产详细信息（all）
 */
@Entity
public class XfInventoryDetail {
    @PrimaryKey
    @NonNull
    private String id;
    private String inv_id;
    private String ast_id;
    private String ast_name;
    private String ast_barcode;
    private String ast_brand;
    private String ast_epc_code;
    private String loc_name;
    private String org_belongcorp_name;
    private String xjPerson;
    private String xjDate;
    private String xjResult;
    private String weixiuCode;
    private String weixiuPerson;
    private String weixiuDate;
    private String weixiuContent;
    //0正常 1异常
    private int zhiJia;
    private int yaLi;
    private int qianFen;
    private int qingJie;
    private int youXiaoQi;
    private int result;
    //0未巡检 1已巡检
    private int inv_status;
    private String remark;
    //资产报修使用
    @Ignore
    private boolean isSelected;

    public XfInventoryDetail(@NonNull String id, String inv_id, String ast_id, String ast_name, String ast_barcode, String ast_brand, String ast_epc_code, String loc_name, String org_belongcorp_name, String xjPerson, String xjDate, String xjResult, String weixiuCode, String weixiuPerson, String weixiuDate, String weixiuContent, int zhiJia, int yaLi, int qianFen, int qingJie, int youXiaoQi, int result, int inv_status, String remark) {
        this.id = id;
        this.inv_id = inv_id;
        this.ast_id = ast_id;
        this.ast_name = ast_name;
        this.ast_barcode = ast_barcode;
        this.ast_brand = ast_brand;
        this.ast_epc_code = ast_epc_code;
        this.loc_name = loc_name;
        this.org_belongcorp_name = org_belongcorp_name;
        this.xjPerson = xjPerson;
        this.xjDate = xjDate;
        this.xjResult = xjResult;
        this.weixiuCode = weixiuCode;
        this.weixiuPerson = weixiuPerson;
        this.weixiuDate = weixiuDate;
        this.weixiuContent = weixiuContent;
        this.zhiJia = zhiJia;
        this.yaLi = yaLi;
        this.qianFen = qianFen;
        this.qingJie = qingJie;
        this.youXiaoQi = youXiaoQi;
        this.result = result;
        this.inv_status = inv_status;
        this.remark = remark;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getInv_id() {
        return inv_id;
    }

    public void setInv_id(String inv_id) {
        this.inv_id = inv_id;
    }

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

    public String getAst_epc_code() {
        return ast_epc_code;
    }

    public void setAst_epc_code(String ast_epc_code) {
        this.ast_epc_code = ast_epc_code;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public String getOrg_belongcorp_name() {
        return org_belongcorp_name;
    }

    public void setOrg_belongcorp_name(String org_belongcorp_name) {
        this.org_belongcorp_name = org_belongcorp_name;
    }

    public int getZhiJia() {
        return zhiJia;
    }

    public void setZhiJia(int zhiJia) {
        this.zhiJia = zhiJia;
    }

    public int getYaLi() {
        return yaLi;
    }

    public void setYaLi(int yaLi) {
        this.yaLi = yaLi;
    }

    public int getQianFen() {
        return qianFen;
    }

    public void setQianFen(int qianFen) {
        this.qianFen = qianFen;
    }

    public int getQingJie() {
        return qingJie;
    }

    public void setQingJie(int qingJie) {
        this.qingJie = qingJie;
    }

    public int getYouXiaoQi() {
        return youXiaoQi;
    }

    public void setYouXiaoQi(int youXiaoQi) {
        this.youXiaoQi = youXiaoQi;
    }

    public int getInv_status() {
        return inv_status;
    }

    public void setInv_status(int inv_status) {
        this.inv_status = inv_status;
    }

    public String getAst_name() {
        return ast_name;
    }

    public void setAst_name(String ast_name) {
        this.ast_name = ast_name;
    }

    public String getAst_id() {
        return ast_id;
    }

    public void setAst_id(String ast_id) {
        this.ast_id = ast_id;
    }

    /*  private int zhiJia;
      private int yaLi;
      private int qianFen;
      private int qingJie;
      private int youXiaoQi;*/

    public String getXjPerson() {
        return xjPerson;
    }

    public void setXjPerson(String xjPerson) {
        this.xjPerson = xjPerson;
    }

    public String getXjDate() {
        return xjDate;
    }

    public void setXjDate(String xjDate) {
        this.xjDate = xjDate;
    }

    public String getXjResult() {
        return xjResult;
    }

    public void setXjResult(String xjResult) {
        this.xjResult = xjResult;
    }

    public String getWeixiuCode() {
        return weixiuCode;
    }

    public void setWeixiuCode(String weixiuCode) {
        this.weixiuCode = weixiuCode;
    }

    public String getWeixiuPerson() {
        return weixiuPerson;
    }

    public void setWeixiuPerson(String weixiuPerson) {
        this.weixiuPerson = weixiuPerson;
    }

    public String getWeixiuDate() {
        return weixiuDate;
    }

    public void setWeixiuDate(String weixiuDate) {
        this.weixiuDate = weixiuDate;
    }

    public String getWeixiuContent() {
        return weixiuContent;
    }

    public void setWeixiuContent(String weixiuContent) {
        this.weixiuContent = weixiuContent;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getResult() {
        if (zhiJia == 0 && yaLi == 0 && qianFen == 0 && qingJie == 0 && youXiaoQi == 0) {
            result = 0;
        }else {
            result = 1;
        }
        return result;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XfInventoryDetail)) return false;
        XfInventoryDetail that = (XfInventoryDetail) o;
        return getId().equals(that.getId()) &&
                getInv_id().equals(that.getInv_id()) &&
                getAst_barcode().equals(that.getAst_barcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getInv_id(), getAst_barcode());
    }
}
