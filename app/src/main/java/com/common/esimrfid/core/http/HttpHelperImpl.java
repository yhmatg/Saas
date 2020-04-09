package com.common.esimrfid.core.http;


import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.CreateInvResult;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.InventoryParameter;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.home.CompanyInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.core.bean.update.UpdateVersion;
import com.common.esimrfid.core.http.api.GeeksApis;
import com.common.esimrfid.core.http.client.RetrofitClient;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;

/**
 * 对外隐藏进行网络请求的实现细节
 *
 * @author yhm
 * @date 2017/11/27
 */

public class HttpHelperImpl implements HttpHelper {

    private GeeksApis mGeeksApis;

    private volatile static HttpHelperImpl INSTANCE = null;


    private HttpHelperImpl(GeeksApis geeksApis) {
        mGeeksApis = geeksApis;
    }

    public static HttpHelperImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpHelperImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpHelperImpl(RetrofitClient.getInstance().create(GeeksApis.class));
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<BaseResponse<UserLoginResponse>> login(UserInfo userInfo) {
        return mGeeksApis.login(userInfo);
    }

    @Override
    public Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(String userId) {
        return mGeeksApis.fetchAllIvnOrders(userId);
    }

    @Override
    public Observable<BaseResponse<ResultInventoryDetail>> fetchAllInvDetails(String orderId) {
        return mGeeksApis.fetchAllInvDetails(orderId);
    }

    @Override
    public Observable<BaseResponse> uploadInvDetails(String orderId, List<String> invDetails, String uid) {
        return mGeeksApis.uploadInvDetails(orderId, invDetails, uid);
    }

    @Override
    public Observable<BaseResponse<HashMap<String, Integer>>> getAssetsNmbDiffLocation() {
        return mGeeksApis.getAssetsNmbDiffLocation();
    }

    @Override
    public Observable<BaseResponse<AssetStatusNum>> getAssetsNmbDiffStatus() {
        return mGeeksApis.getAssetsNmbDiffStatus();
    }

    @Override
    public Observable<BaseResponse<List<MangerUser>>> getAllManagerUsers() {
        return mGeeksApis.getAllManagerUsers();
    }

    @Override
    public Observable<BaseResponse<List<CompanyBean>>> getAllCompany() {
        return mGeeksApis.getAllCompany();
    }

    @Override
    public Observable<BaseResponse<List<DepartmentBean>>> getAllDeparts(String comId) {
        return mGeeksApis.getAllDeparts(comId);
    }

    @Override
    public Observable<BaseResponse<List<AssetsType>>> getAllAssetsType() {
        return mGeeksApis.getAllAssetsType();
    }

    @Override
    public Observable<BaseResponse<List<AssetsLocation>>> getAllAssetsLocation() {
        return mGeeksApis.getAllAssetsLocation();
    }

    @Override
    public Observable<BaseResponse<CreateInvResult>> createNewInventory(InventoryParameter invpara) {
        return mGeeksApis.createNewInventory(invpara);
    }
	
	@Override
    public Observable<BaseResponse<List<AssetsInfo>>> fetchWriteAssetsInfo(String patternName) {
        return mGeeksApis.fetchWriteAssetsInfos(patternName);
    }

    @Override
    public Observable<BaseResponse> finishInvOrderWithAsset(String orderId, String uid, List<String> invDetails) {
        return mGeeksApis.finishInvOrderWithAsset(orderId,uid,invDetails);
    }

    @Override
    public Observable<BaseResponse<List<AssetsInfo>>> fetchScanAssets(Set<String> Epcs) {
        return mGeeksApis.fetchScanAssets(Epcs);
    }

    @Override
    public Observable<BaseResponse<AssetsDetailsInfo>> fetchAssetsInfo(String astId) {
        return mGeeksApis.fetchAssetsInfoById(astId);
    }

    @Override
    public Observable<BaseResponse<UpdateVersion>> updateVersion() {
        return mGeeksApis.updateVersion();
    }

    @Override
    public Observable<BaseResponse<CompanyInfo>> getCompanyInfo() {
        return mGeeksApis.getCompanyInfo();
    }

}
