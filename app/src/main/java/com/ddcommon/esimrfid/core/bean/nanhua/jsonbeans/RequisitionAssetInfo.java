package com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans;

import java.util.Objects;

//领用资产详情
public class RequisitionAssetInfo {
    /**
     * ast_brand : z423424
     * ast_buy_date : 1569945600000
     * ast_code : E014
     * ast_epc_code : 45303134
     * ast_expiration_months :
     * ast_instore_date : 1552891577000
     * ast_material : 0
     * ast_model : z423424
     * ast_name : 桌子
     * ast_price : 0.0
     * ast_salvage_value : 0.0
     * ast_sn :
     * ast_status : 0
     * ast_used_status : 2
     * create_date : 1552891577000
     * creator_id : yyj_id
     * id : 29f896c7824a4eb38f93468848319ea2
     * org_belongcorp_id :
     * org_useddept_id : 6990f769cefc11e9abc50242ac110002
     * type_id : 2d11f6b9454311e9aa47b052166c710d
     * type_info : {"id":"2d11f6b9454311e9aa47b052166c710d","type_code":"00040002","type_name":"电视机","type_superid":"769fa05b454211e9aa47b052166c710d"}
     * update_date : 1569395144000
     * updater_id : admin_id
     * user_id : admin_id
     * user_info : {"id":"admin_id"}
     */
    private String ast_barcode;
    private String ast_epc_code;
    private String ast_img_url;
    private String ast_model;
    private String ast_name;
    private Integer ast_used_status;

    public String getAst_barcode() {
        return ast_barcode;
    }

    public void setAst_barcode(String ast_barcode) {
        this.ast_barcode = ast_barcode;
    }

    public String getAst_epc_code() {
        return ast_epc_code;
    }

    public void setAst_epc_code(String ast_epc_code) {
        this.ast_epc_code = ast_epc_code;
    }

    public String getAst_img_url() {
        return ast_img_url;
    }

    public void setAst_img_url(String ast_img_url) {
        this.ast_img_url = ast_img_url;
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

    public Integer getAst_used_status() {
        return ast_used_status;
    }

    public void setAst_used_status(Integer ast_used_status) {
        this.ast_used_status = ast_used_status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequisitionAssetInfo)) return false;
        RequisitionAssetInfo that = (RequisitionAssetInfo) o;
        return getAst_barcode().equals(that.getAst_barcode()) &&
                getAst_epc_code().equals(that.getAst_epc_code());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAst_barcode(), getAst_epc_code());
    }
}
