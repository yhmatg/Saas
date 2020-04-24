package com.ddcommon.esimrfid.core;

import com.ddcommon.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.ddcommon.esimrfid.core.bean.inventorytask.AssetsType;
import com.ddcommon.esimrfid.core.bean.inventorytask.CompanyBean;
import com.ddcommon.esimrfid.core.bean.inventorytask.CreateInvResult;
import com.ddcommon.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.ddcommon.esimrfid.core.bean.inventorytask.InventoryParameter;
import com.ddcommon.esimrfid.core.bean.inventorytask.MangerUser;
import com.ddcommon.esimrfid.core.bean.nanhua.BaseResponse;
import com.ddcommon.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.ddcommon.esimrfid.core.bean.nanhua.home.CompanyInfo;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.ddcommon.esimrfid.core.bean.update.UpdateVersion;
import com.ddcommon.esimrfid.core.http.HttpHelper;
import com.ddcommon.esimrfid.core.http.HttpHelperImpl;
import com.ddcommon.esimrfid.core.prefs.PreferenceHelper;
import com.ddcommon.esimrfid.core.prefs.PreferenceHelperImpl;

import java.util.HashMap;
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
    public void setOpenBeeper(boolean isOpen) {
        mPreferenceHelper.setOpenBeeper(isOpen);
    }

    @Override
    public boolean getOpenBeeper() {
        return mPreferenceHelper.getOpenBeeper();
    }

    @Override
    public void setSledBeeper(boolean isSledBeeper) {
        mPreferenceHelper.setSledBeeper(isSledBeeper);
    }

    @Override
    public boolean getSledBeeper() {
        return mPreferenceHelper.getSledBeeper();
    }

    @Override
    public void setHostBeeper(boolean isHostBeeper) {
        mPreferenceHelper.setHostBeeper(isHostBeeper);
    }

    @Override
    public boolean getHostBeeper() {
        return mPreferenceHelper.getHostBeeper();
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
    public Observable<BaseResponse<HashMap<String, Integer>>> getAssetsNmbDiffLocation() {
        return mHttpHelper.getAssetsNmbDiffLocation();
    }

    @Override
    public Observable<BaseResponse<AssetStatusNum>> getAssetsNmbDiffStatus() {
        return mHttpHelper.getAssetsNmbDiffStatus();
    }

    @Override
    public Observable<BaseResponse<List<MangerUser>>> getAllManagerUsers() {
        return mHttpHelper.getAllManagerUsers();
    }

    @Override
    public Observable<BaseResponse<List<CompanyBean>>> getAllCompany() {
        return mHttpHelper.getAllCompany();
    }

    @Override
    public Observable<BaseResponse<List<DepartmentBean>>> getAllDeparts(String comId) {
        return mHttpHelper.getAllDeparts(comId);
    }

    @Override
    public Observable<BaseResponse<List<AssetsType>>> getAllAssetsType() {
        return mHttpHelper.getAllAssetsType();
    }

    @Override
    public Observable<BaseResponse<List<AssetsLocation>>> getAllAssetsLocation() {
        return mHttpHelper.getAllAssetsLocation();
    }

    @Override
    public Observable<BaseResponse<CreateInvResult>> createNewInventory(InventoryParameter invpara) {
        return mHttpHelper.createNewInventory(invpara);
    }
	
	@Override
    public Observable<BaseResponse<List<AssetsInfo>>> fetchWriteAssetsInfo(String patternName) {
        return mHttpHelper.fetchWriteAssetsInfo(patternName);
    }

    @Override
    public Observable<BaseResponse> finishInvOrderWithAsset(String orderId, String uid, List<String> invDetails) {
        return mHttpHelper.finishInvOrderWithAsset(orderId, uid, invDetails);
    }

    @Override
    public Observable<BaseResponse<List<AssetsInfo>>> fetchScanAssets(Set<String> Epcs) {
        return mHttpHelper.fetchScanAssets(Epcs);
    }

    @Override
    public Observable<BaseResponse<AssetsDetailsInfo>> fetchAssetsInfo(String astId) {
        return mHttpHelper.fetchAssetsInfo(astId);
    }

    @Override
    public Observable<BaseResponse<UpdateVersion>> updateVersion() {
        return mHttpHelper.updateVersion();
    }

    @Override
    public Observable<BaseResponse<CompanyInfo>> getCompanyInfo() {
        return mHttpHelper.getCompanyInfo();
    }
}