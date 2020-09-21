package com.common.esimrfid.core;

import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetRepairParameter;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.assetdetail.NewAssetRepairPara;
import com.common.esimrfid.core.bean.inventorytask.AssetUploadParameter;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.CreateInvResult;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.InventoryParameter;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.home.AssetLocNmu;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.home.CompanyInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfoPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DataAuthority;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryOrderPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyAssets;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyPageAssets;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.core.bean.update.UpdateVersion;
import com.common.esimrfid.core.http.HttpHelper;
import com.common.esimrfid.core.http.HttpHelperImpl;
import com.common.esimrfid.core.prefs.PreferenceHelper;
import com.common.esimrfid.core.prefs.PreferenceHelperImpl;

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
    public void setLatestSyncTime(String time) {
        mPreferenceHelper.setLatestSyncTime(time);
    }

    @Override
    public String getLatestSyncTime() {
        return mPreferenceHelper.getLatestSyncTime();
    }

    @Override
    public void setDataAuthority(DataAuthority dataAuthority) {
        mPreferenceHelper.setDataAuthority(dataAuthority);
    }

    @Override
    public DataAuthority getDataAuthority() {
        return mPreferenceHelper.getDataAuthority();
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
    public Observable<BaseResponse<AssetsAllInfo>> fetchAssetsInfo(String astId, String astCode) {
        return mHttpHelper.fetchAssetsInfo(astId, astCode);
    }


    @Override
    public Observable<BaseResponse<UpdateVersion>> updateVersion() {
        return mHttpHelper.updateVersion();
    }

    @Override
    public Observable<BaseResponse<CompanyInfo>> getCompanyInfo() {
        return mHttpHelper.getCompanyInfo();
    }

    @Override
    public Observable<BaseResponse> createNewRepairOrder(AssetRepairParameter repairParameter) {
        return mHttpHelper.createNewRepairOrder(repairParameter);
    }

    @Override
    public Observable<BaseResponse<List<AssetResume>>> fetchAssetResume(String astid, String astCode) {
        return mHttpHelper.fetchAssetResume(astid, astCode);
    }


    @Override
    public Observable<BaseResponse<List<AssetRepair>>> fetchAssetRepair(String astid, String astCode) {
        return mHttpHelper.fetchAssetRepair(astid, astCode);
    }

    @Override
    public Observable<BaseResponse<List<MangerUser>>> getAllEmpUsers() {
        return mHttpHelper.getAllEmpUsers();
    }

    @Override
    public Observable<BaseResponse<AssetsInfoPage>> getAllAssetsByOptPage(String optType, Integer size, Integer page, String patternName) {
        return mHttpHelper.getAllAssetsByOptPage(optType, size, page, patternName);
    }

    @Override
    public Observable<BaseResponse<List<AssetsInfo>>> getAllAssetsByOpt(String optType, String patternName) {
        return mHttpHelper.getAllAssetsByOpt(optType, patternName);
    }

    @Override
    public Observable<BaseResponse> uploadInvAssets(String orderId, String uid, List<AssetUploadParameter> invDetails) {
        return mHttpHelper.uploadInvAssets(orderId, uid, invDetails);
    }

    @Override
    public Observable<BaseResponse> finishInvAssets(String orderId, String uid, List<AssetUploadParameter> invDetails) {
        return mHttpHelper.finishInvAssets(orderId, uid, invDetails);
    }

    @Override
    public Observable<BaseResponse<List<AssetsAllInfo>>> fetchAllAssetsInfos(String patternName) {
        return mHttpHelper.fetchAllAssetsInfos(patternName);
    }

    @Override
    public Observable<BaseResponse<LatestModifyAssets>> fetchLatestAssets(String lastTime) {
        return mHttpHelper.fetchLatestAssets(lastTime);
    }

    @Override
    public Observable<BaseResponse<AssetsInfoPage>> fetchPageAssetsInfos(Integer size, Integer page, String patternName) {
        return mHttpHelper.fetchPageAssetsInfos(size, page, patternName);
    }

    @Override
    public Observable<BaseResponse> createNewRepairOrder(NewAssetRepairPara repariPara) {
        return mHttpHelper.createNewRepairOrder(repariPara);
    }

    @Override
    public Observable<BaseResponse<AssetsListPage>> fetchPageAssetsList(Integer size, Integer page, String patternName, String userRealName,String conditions) {
        return mHttpHelper.fetchPageAssetsList(size, page, patternName,userRealName,conditions);
    }

    @Override
    public Observable<BaseResponse<DataAuthority>> getDataAuthority(String id) {
        return mHttpHelper.getDataAuthority(id);
    }

    @Override
    public Observable<BaseResponse<List<AssetLocNmu>>> getAssetsNmbInDiffLocation() {
        return mHttpHelper.getAssetsNmbInDiffLocation();
    }

    @Override
    public Observable<BaseResponse<AssetsAllInfo>> fetchAssetsInfoWithAuth(String astId, String astCode) {
        return mHttpHelper.fetchAssetsInfoWithAuth(astId, astCode);
    }

    @Override
    public Observable<BaseResponse<List<CompanyBean>>> getAllAuthCompany(Integer type) {
        return mHttpHelper.getAllAuthCompany(type);
    }

    @Override
    public Observable<BaseResponse<InventoryOrderPage>> fetchAllIvnOrdersPage(Integer size, Integer page, String userId) {
        return mHttpHelper.fetchAllIvnOrdersPage(size, page, userId);
    }

    @Override
    public Observable<BaseResponse<LatestModifyPageAssets>> fetchLatestAssetsPage(String lastTime, Integer size, Integer page) {
        return mHttpHelper.fetchLatestAssetsPage(lastTime, size, page);
    }
}
