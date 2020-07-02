package com.common.esimrfid.core.bean.inventorytask;

import java.util.Objects;

public class EpcBean {
    private String ast_epc_code;

    public EpcBean(String ast_epc_code) {
        this.ast_epc_code = ast_epc_code;
    }

    public String getAst_epc_code() {
        return ast_epc_code;
    }

    public void setAst_epc_code(String ast_epc_code) {
        this.ast_epc_code = ast_epc_code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EpcBean)) return false;
        EpcBean epcBean = (EpcBean) o;
        return getAst_epc_code().equals(epcBean.getAst_epc_code());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAst_epc_code());
    }
}
