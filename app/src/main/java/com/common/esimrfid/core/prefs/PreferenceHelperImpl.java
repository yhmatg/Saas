package com.common.esimrfid.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.common.esimrfid.app.Constants;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.utils.GsonUtil;
import com.google.gson.Gson;


/**
 * @author yhm
 * @date 2017/11/27
 */

public class PreferenceHelperImpl implements PreferenceHelper {

    private final SharedPreferences mPreferences;
    private volatile static PreferenceHelperImpl INSTANCE = null;

    private PreferenceHelperImpl() {
        mPreferences = EsimAndroidApp.getInstance().getSharedPreferences(Constants.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static PreferenceHelperImpl getInstance(){
        if (INSTANCE == null) {
            synchronized (PreferenceHelperImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PreferenceHelperImpl();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void setLoginAccount(String account) {
        mPreferences.edit().putString(Constants.ACCOUNT, account).apply();
    }

    @Override
    public void setLoginPassword(String password) {
        mPreferences.edit().putString(Constants.PASSWORD, password).apply();
    }

    @Override
    public String getLoginAccount() {
        return mPreferences.getString(Constants.ACCOUNT, "");
    }

    @Override
    public String getLoginPassword() {
        return mPreferences.getString(Constants.PASSWORD, "");
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        mPreferences.edit().putBoolean(Constants.LOGIN_STATUS, isLogin).commit();
    }

    @Override
    public boolean getLoginStatus() {
        return mPreferences.getBoolean(Constants.LOGIN_STATUS, false);
    }

    @Override
    public void setCookie(String domain, String cookie) {
        mPreferences.edit().putString(domain, cookie).apply();
    }

    @Override
    public String getCookie(String domain) {
        return mPreferences.getString(Constants.COOKIE, "");
    }


    @Override
    public boolean getAutoCacheState() {
        return mPreferences.getBoolean(Constants.AUTO_CACHE_STATE, true);
    }

    @Override
    public void setAutoCacheState(boolean b) {
        mPreferences.edit().putBoolean(Constants.AUTO_CACHE_STATE, b).apply();
    }


    @Override
    public void saveHostUrl(String hostUrl) {
        mPreferences.edit().putString(Constants.HOSTURL, hostUrl).apply();
    }

    @Override
    public String getHostUrl() {
        return mPreferences.getString(Constants.HOSTURL, "https://www.assettag.vip");
    }

    @Override
    public void saveOpenSound(boolean isOpen) {
        mPreferences.edit().putBoolean(Constants.OPENSOUND, isOpen).apply();
    }

    @Override
    public boolean getOpenSound() {
        return mPreferences.getBoolean(Constants.OPENSOUND, true);
    }

    @Override
    public void setToken(String token) {
        mPreferences.edit().putString(Constants.TOKEN, token).apply();
    }

    @Override
    public String getToken() {
        return mPreferences.getString(Constants.TOKEN, "");
    }

    @Override
    public void setUserLoginResponse(UserLoginResponse userLoginResponse) {
        mPreferences.edit().putString(Constants.USERLOGINRESPONSE,new Gson().toJson(userLoginResponse)).apply();
    }

    @Override
    public UserLoginResponse getUserLoginResponse() {
        String userLoginResponse = mPreferences.getString(Constants.USERLOGINRESPONSE, "");
        if (!"".equals(userLoginResponse)){
            return GsonUtil.GsonToBean(userLoginResponse, UserLoginResponse.class);
        }else {
            return null;
        }
    }

    @Override
    public void removeUserLoginResponse() {
        mPreferences.edit().remove(Constants.USERLOGINRESPONSE).commit();
    }

    @Override
    public void setOpenBeeper(boolean isOpen) {
        mPreferences.edit().putBoolean(Constants.OPEN,isOpen).apply();
    }

    @Override
    public boolean getOpenBeeper() {
        return mPreferences.getBoolean(Constants.OPEN,false);
    }

    @Override
    public void setSledBeeper(boolean isSledBeeper) {
        mPreferences.edit().putBoolean(Constants.SLEDBEEPER,isSledBeeper).apply();
    }

    @Override
    public boolean getSledBeeper() {
        return mPreferences.getBoolean(Constants.SLEDBEEPER,false);
    }

    @Override
    public void setHostBeeper(boolean isHostBeeper) {
        mPreferences.edit().putBoolean(Constants.HOSTBEEPER,isHostBeeper).apply();
    }

    @Override
    public boolean getHostBeeper() {
        return mPreferences.getBoolean(Constants.HOSTBEEPER,false);
    }


}
