package com.common.esimrfid.core.bean.assetdetail;

public class AssetResume {
    /**
     * ast_barcode : 202005207086
     * ast_id : 153a53afcbb74961af9a412f154dee6e
     * ast_name : 测试994
     * create_date : 1590043786000
     * creator_id : 100a8c361af711eaabcf00163e0a6695
     * creator_name : 超级管理员
     * id : 912806a60d3440dbaf2d801e9e2eca64
     * opt_details : [{"changefrom":"2020-01-01","changeto":"2020-04-24","fieldname":"购入日期"},{"changefrom":"","changeto":"1","fieldname":"预计使用月数"}]
     * opt_module : 资产管理
     * opt_operation : 修改资产
     * transactor_id : 100a8c361af711eaabcf00163e0a6695
     * transactor_name : 超级管理员
     */

    private String ast_id;
    private long create_date;
    private String id;
    private String opt_details;
    private String opt_operation;
    private String transactor_name;

    public String getAst_id() {
        return ast_id;
    }

    public void setAst_id(String ast_id) {
        this.ast_id = ast_id;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpt_details() {
        return opt_details;
    }

    public void setOpt_details(String opt_details) {
        this.opt_details = opt_details;
    }

    public String getOpt_operation() {
        return opt_operation;
    }

    public void setOpt_operation(String opt_operation) {
        this.opt_operation = opt_operation;
    }

    public String getTransactor_name() {
        return transactor_name;
    }

    public void setTransactor_name(String transactor_name) {
        this.transactor_name = transactor_name;
    }
}
