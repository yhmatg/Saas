package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import java.util.ArrayList;
import java.util.List;

public class DataAuthority {

    /**
     * id : 2323
     * user_id : 113
     * tenant_id : 1234
     * auth_corp_scope : ["1","2"]
     * auth_dept_scope : ["1","2"]
     * auth_type_scope : ["1","2"]
     * auth_loc_scope : ["1","2"]
     */

    private String id;
    private String user_id;
    private String tenant_id;
    private List<String> auth_corp_scope = new ArrayList<>();
    private List<String> auth_dept_scope = new ArrayList<>();
    private AuthTypeScope auth_type_scope;
    private AuthLocScope auth_loc_scope;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
    }

    public List<String> getAuth_corp_scope() {
        return auth_corp_scope;
    }

    public void setAuth_corp_scope(List<String> auth_corp_scope) {
        this.auth_corp_scope = auth_corp_scope;
    }

    public List<String> getAuth_dept_scope() {
        return auth_dept_scope;
    }

    public void setAuth_dept_scope(List<String> auth_dept_scope) {
        this.auth_dept_scope = auth_dept_scope;
    }

    public AuthTypeScope getAuth_type_scope() {
        return auth_type_scope;
    }

    public void setAuth_type_scope(AuthTypeScope auth_type_scope) {
        this.auth_type_scope = auth_type_scope;
    }

    public AuthLocScope getAuth_loc_scope() {
        return auth_loc_scope;
    }

    public void setAuth_loc_scope(AuthLocScope auth_loc_scope) {
        this.auth_loc_scope = auth_loc_scope;
    }

    public static class AuthTypeScope {
        private List<String> general = new ArrayList<>();

        public List<String> getGeneral() {
            return general;
        }

        public void setGeneral(List<String> general) {
            this.general = general;
        }
    }

    public static class AuthLocScope {
        private List<String> general = new ArrayList<>();

        public List<String> getGeneral() {
            return general;
        }

        public void setGeneral(List<String> general) {
            this.general = general;
        }
    }
}
