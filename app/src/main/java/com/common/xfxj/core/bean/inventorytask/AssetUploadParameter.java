package com.common.xfxj.core.bean.inventorytask;

public class AssetUploadParameter {
    private String invdt_sign;
    private Integer invdt_status;
    private String invdt_plus_loc_id;
    private String ast_id;

    public AssetUploadParameter() {
    }

    public AssetUploadParameter(String invdt_sign, Integer invdt_status, String invdt_plus_loc_id, String ast_id) {
        this.invdt_sign = invdt_sign;
        this.invdt_status = invdt_status;
        this.invdt_plus_loc_id = invdt_plus_loc_id;
        this.ast_id = ast_id;
    }

    public String getInvdt_sign() {
        return invdt_sign;
    }

    public void setInvdt_sign(String invdt_sign) {
        this.invdt_sign = invdt_sign;
    }

    public Integer getInvdt_status() {
        return invdt_status;
    }

    public void setInvdt_status(Integer invdt_status) {
        this.invdt_status = invdt_status;
    }

    public String getInvdt_plus_loc_id() {
        return invdt_plus_loc_id;
    }

    public void setInvdt_plus_loc_id(String invdt_plus_loc_id) {
        this.invdt_plus_loc_id = invdt_plus_loc_id;
    }

    public String getAst_id() {
        return ast_id;
    }

    public void setAst_id(String ast_id) {
        this.ast_id = ast_id;
    }
}
