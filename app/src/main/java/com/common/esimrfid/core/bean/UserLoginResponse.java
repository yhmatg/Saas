package com.common.esimrfid.core.bean;


public class UserLoginResponse {
    private UserInfo user;
    private String token;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
