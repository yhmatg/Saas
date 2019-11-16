package com.common.esimrfid.core;

import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionAssetInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionDetailInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionItemInfo;
import com.common.esimrfid.core.http.HttpHelper;
import com.common.esimrfid.core.http.HttpHelperImpl;
import com.common.esimrfid.core.prefs.PreferenceHelper;
import com.common.esimrfid.core.prefs.PreferenceHelperImpl;

import java.util.List;
import java.util.Set;

import io.reactivex.Observable;

/**
 * @author yhm
 * @date 2017/11/27
 */

public class DataManager implements HttpHelper, PreferenceHelper {

    private HttpHelper mHttpHelper;
    private PreferenceHelper mPreferenceHelper;
    private volatile static DataManager INSTANCE = null;

    private DataManager(HttpHelper httpHelper, PreferenceHelper preferencesHelper) {
        mHttpHelper = httpHelper;
        mPreferenceHelper = preferencesHelper;
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DataManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataManager(HttpHelperImpl.getInstance(), PreferenceHelperImpl.getInstance());
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
        mPreferenceHelper.setLoginAccount(account);
    }

    @Override
    public void setLoginPassword(String password) {
        mPreferenceHelper.setLoginPassword(password);
    }

    @Override
    public String getLoginAccount() {
        return mPreferenceHelper.getLoginAccount();
    }

    @Override
    public String getLoginPassword() {
        return mPreferenceHelper.getLoginPassword();
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        mPreferenceHelper.setLoginStatus(isLogin);
    }

    @Override
    public boolean getLoginStatus() {
        return mPreferenceHelper.getLoginStatus();
    }

    @Override
    public void setCookie(String domain, String cookie) {
        mPreferenceHelper.setCookie(domain, cookie);
    }

    @Override
    public String getCookie(String domain) {
        return mPreferenceHelper.getCookie(domain);
    }


    @Override
    public boolean getAutoCacheState() {
        return mPreferenceHelper.getAutoCacheState();
    }


    @Override
    public void setAutoCacheState(boolean b) {
        mPreferenceHelper.setAutoCacheState(b);
    }

    @Override
    public void saveHostUrl(String hostUrl) {
        mPreferenceHelper.saveHostUrl(hostUrl);
    }

    @Override
    public String getHostUrl() {
        return mPreferenceHelper.getHostUrl();
    }

    @Override
    public void saveOpenSound(boolean isOpen) {
        mPreferenceHelper.saveOpenSound(isOpen);
    }

    @Override
    public boolean getOpenSound() {
        return mPreferenceHelper.getOpenSound();
    }

    @Override
    public void setToken(String token) {
        mPreferenceHelper.setToken(token);
    }

    @Override
    public String getToken() {
        return mPreferenceHelper.getToken();
    }

    @Override
    public void setUserLoginResponse(UserLoginResponse userLoginResponse) {
        mPreferenceHelper.setUserLoginResponse(userLoginResponse);
    }

    @Override
    public UserLoginResponse getUserLoginResponse() {
        return mPreferenceHelper.getUserLoginResponse();
    }

    @Override
    public void removeUserLoginResponse() {
        mPreferenceHelper.removeUserLoginResponse();
    }


    @Override
    public Observable<BaseResponse<UserLoginResponse>> login(UserInfo userInfo) {
        return mHttpHelper.login(userInfo);
    }

    @Override
    public Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(String userId) {
        return mHttpHelper.fetchAllIvnOrders(userId);
    }

    @Override
    public Observable<BaseResponse<ResultInventoryDetail>> fetchAllInvDetails(String orderId) {
        return mHttpHelper.fetchAllInvDetails(orderId);
    }

    @Override
    public Observable<BaseResponse> uploadInvDetails(String orderId, List<String> invDetails, String uid) {
        return mHttpHelper.uploadInvDetails(orderId, invDetails, uid);
    }

    @Override
    public Observable<BaseResponse> finishInvOrder(String orderId, String uid, String remark) {
        return mHttpHelper.finishInvOrder(orderId, uid, remark);
    }

    @Override
    public Observable<BaseResponse<List<AssetsInfo>>> fetchScanAssetsInfons(Set<String> ecps) {
        return mHttpHelper.fetchScanAssetsInfons(ecps);
    }

    @Override
    public Observable<BaseResponse<List<RequisitionItemInfo>>> fetchAllRequisitions() {
        return mHttpHelper.fetchAllRequisitions();
    }

    @Override
    public Observable<BaseResponse<List<RequisitionAssetInfo>>> getRequisitionInfons(Set<String> ecps) {
        return mHttpHelper.getRequisitionInfons(ecps);
    }

    @Override
    public Observable<BaseResponse<List<RequisitionAssetInfo>>> fetchRequestAssetsInfos(String patternName) {
        return mHttpHelper.fetchRequestAssetsInfos(patternName);
    }

    @Override
    public Observable<BaseResponse<RequisitionDetailInfo>> fetchRequsitionDetailByid(String odrId) {
        return mHttpHelper.fetchRequsitionDetailByid(odrId);
    }

    @Override
    public Observable<BaseResponse> uploadResAssets(String requestId, List<String> epcs) {
        return mHttpHelper.uploadResAssets(requestId, epcs);
    }
}
