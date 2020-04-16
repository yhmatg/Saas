package com.ddcommon.esimrfid.core.bean.nanhua;


public class UserLoginResponse {
    private UserInfo userinfo;
    private String token;

    public UserInfo getSysUser() {
        return userinfo;
    }

    public void setSysUser(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
