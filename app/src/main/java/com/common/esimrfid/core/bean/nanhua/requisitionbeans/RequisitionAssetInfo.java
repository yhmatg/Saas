package com.common.esimrfid.core.bean.nanhua.requisitionbeans;

import java.util.Objects;

public class RequisitionAssetInfo {

    /**
     * ast_code : ZC00020001201903110008
     * ast_epc_code : 5A433030303230303031323031393033313130303038
     * ast_img_url :
     * ast_model : T450
     * ast_name : 笔记本电脑
     * ast_used_status : {"code":1,"index":1,"name":"在库"}
     * type_info : {"id":"b68932b73b4711e9aa47b052166c710d","type_name":"IT资产"}
     */

    private String ast_code;
    private String ast_epc_code;
    private String ast_img_url;
    private String ast_model;
    private String ast_name;
    private AstUsedStatusBean ast_used_status;
    private TypeInfoBean type_info;
    /**
     * ast_status : {"code":0,"index":0,"name":"正常"}
     */

    private AstUsedStatusBean ast_status;


    public String getAst_code() {
        return ast_code;
    }

    public void setAst_code(String ast_code) {
        this.ast_code = ast_code;
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

    public AstUsedStatusBean getAst_used_status() {
        return ast_used_status;
    }

    public void setAst_used_status(AstUsedStatusBean ast_used_status) {
        this.ast_used_status = ast_used_status;
    }

    public TypeInfoBean getType_info() {
        return type_info;
    }

    public void setType_info(TypeInfoBean type_info) {
        this.type_info = type_info;
    }

    public AstUsedStatusBean getAst_status() {
        return ast_status;
    }

    public void setAst_status(AstUsedStatusBean ast_status) {
        this.ast_status = ast_status;
    }

    public static class AstUsedStatusBean {
        /**
         * code : 1
         * index : 1
         * name : 在库
         */

        private int code;
        private int index;
        private String name;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TypeInfoBean {
        /**
         * id : b68932b73b4711e9aa47b052166c710d
         * type_name : IT资产
         */

        private String id;
        private String type_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequisitionAssetInfo)) return false;
        RequisitionAssetInfo that = (RequisitionAssetInfo) o;
        return Objects.equals(getAst_code(), that.getAst_code()) &&
                Objects.equals(getAst_epc_code(), that.getAst_epc_code());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAst_code(), getAst_epc_code());
    }

}
