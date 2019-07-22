package com.common.esimrfid.core;
import com.common.esimrfid.core.bean.BaseResponse;
import com.common.esimrfid.core.bean.CorpInfo;
import com.common.esimrfid.core.bean.InvDetail;
import com.common.esimrfid.core.bean.InvOrder;
import com.common.esimrfid.core.bean.SignatureCard;
import com.common.esimrfid.core.bean.UserInfo;
import com.common.esimrfid.core.bean.UserLoginResponse;
import com.common.esimrfid.core.http.HttpHelper;
import com.common.esimrfid.core.http.HttpHelperImpl;
import com.common.esimrfid.core.prefs.PreferenceHelper;
import com.common.esimrfid.core.prefs.PreferenceHelperImpl;

import java.util.List;

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

    public static DataManager getInstance(){
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
    public Observable<BaseResponse<UserLoginResponse>> login(UserInfo userInfo) {
        return mHttpHelper.login(userInfo);
    }

    @Override
    public Observable<BaseResponse<List<InvOrder>>> fetchAllInvOrders() {
        return mHttpHelper.fetchAllInvOrders();
    }

    @Override
    public Observable<BaseResponse<List<InvDetail>>> fetchAllInvDetails(String orderId) {
        return mHttpHelper.fetchAllInvDetails(orderId);
    }

    @Override
    public Observable<BaseResponse<List<SignatureCard>>> fetchAllSignatureCards(String corpAccount) {
        return mHttpHelper.fetchAllSignatureCards(corpAccount);
    }

    @Override
    public Observable<BaseResponse> uploadInvDetails(List<InvDetail> invDetails, String orderId) {
        return mHttpHelper.uploadInvDetails(invDetails,orderId);
    }

    @Override
    public Observable<BaseResponse> finishInvOrder(String orderId) {
        return mHttpHelper.finishInvOrder(orderId);
    }

    @Override
    public Observable<BaseResponse<CorpInfo>> findCorpInfoByAll(String corpName, String corpAccount, String cardCode) {
        return mHttpHelper.findCorpInfoByAll(corpName,corpAccount,cardCode);
    }
}
