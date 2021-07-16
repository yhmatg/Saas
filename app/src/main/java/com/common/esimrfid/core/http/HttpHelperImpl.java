package com.common.esimrfid.core.http;


import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.assetdetail.NewAssetRepairPara;
import com.common.esimrfid.core.bean.assetdetail.UpdateAssetsPara;
import com.common.esimrfid.core.bean.inventorytask.AssetUploadParameter;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.CreateInvResult;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.InventoryParameter;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.inventorytask.TitleAndLogoResult;
import com.common.esimrfid.core.bean.nanhua.home.AssetLocNmu;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfoPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DataAuthority;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryOrderPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyPageAssets;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.core.bean.update.UpdateVersion;
import com.common.esimrfid.core.http.api.GeeksApis;
import com.common.esimrfid.core.http.client.RetrofitClient;

import java.util.List;

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
        return mGeeksApis.fetchAllInvDetails(orderId,"1");
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
    public Observable<BaseResponse<AssetsAllInfo>> fetchAssetsInfo(String astId, String astCode) {
        return mGeeksApis.fetchAssetsInfo(astId, astCode);
    }


    @Override
    public Observable<BaseResponse<UpdateVersion>> updateVersion() {
        return mGeeksApis.updateVersion();
    }


    @Override
    public Observable<BaseResponse<List<AssetRepair>>> fetchAssetRepair(String astid, String astCode) {
        return mGeeksApis.fetchAssetRepair(astid, astCode);
    }

    @Override
    public Observable<BaseResponse<List<MangerUser>>> getAllEmpUsers() {
        return mGeeksApis.getAllEmpUsers();
    }

    @Override
    public Observable<BaseResponse> uploadInvAssets(String orderId, String uid, List<AssetUploadParameter> invDetails) {
        return mGeeksApis.uploadInvAssets(orderId, uid, invDetails);
    }

    @Override
    public Observable<BaseResponse> finishInvAssets(String orderId, String uid, List<AssetUploadParameter> invDetails) {
        return mGeeksApis.finishInvAssets(orderId, uid, invDetails);
    }

    @Override
    public Observable<BaseResponse<AssetsInfoPage>> fetchPageAssetsInfos(Integer size, Integer page, String patternName) {
        return mGeeksApis.fetchPageAssetsInfos(size, page, patternName);
    }

    @Override
    public Observable<BaseResponse> createNewRepairOrder(NewAssetRepairPara repariPara) {
        return mGeeksApis.createNewRepairOrder(repariPara);
    }

    @Override
    public Observable<BaseResponse<AssetsListPage>> fetchPageAssetsList(Integer size, Integer page, String patternName, String userRealName,String conditions) {
        return mGeeksApis.fetchPageAssetsList(size, page, patternName,userRealName,conditions);
    }

    @Override
    public Observable<BaseResponse<DataAuthority>> getDataAuthority(String id) {
        return mGeeksApis.getDataAuthority(id);
    }

    @Override
    public Observable<BaseResponse<List<AssetLocNmu>>> getAssetsNmbDiffLocation() {
        return mGeeksApis.getAssetsNmbDiffLocation();
    }

    @Override
    public Observable<BaseResponse<AssetsAllInfo>> fetchAssetsInfoWithAuth(String astId, String astCode) {
        return mGeeksApis.fetchAssetsInfoWithAuth(astId, astCode);
    }

    @Override
    public Observable<BaseResponse<List<CompanyBean>>> getAllAuthCompany(Integer type) {
        return mGeeksApis.getAllAuthCompany(type);
    }

    @Override
    public Observable<BaseResponse<InventoryOrderPage>> fetchAllIvnOrdersPage(Integer size, Integer page, String userId) {
        return mGeeksApis.fetchAllIvnOrdersPage(size, page, userId);
    }

    @Override
    public Observable<BaseResponse<LatestModifyPageAssets>> fetchLatestAssetsPage(String lastTime, Integer size, Integer page) {
        return mGeeksApis.fetchLatestAssetsPage(lastTime, size, page);
    }

    @Override
    public Observable<BaseResponse> updateAssetProp(UpdateAssetsPara updateAssetsPara) {
        return mGeeksApis.updateAssetProp(updateAssetsPara);
    }

    @Override
    public Observable<BaseResponse<TitleAndLogoResult>> getTitleAndLogo(String tenantid, String configKey) {
        return mGeeksApis.getTitleAndLogo(tenantid,configKey);
    }

    @Override
    public Observable<BaseResponse<List<AssetResume>>> fetchAssetResume(String astid, String astCode) {
        return mGeeksApis.fetchAssetResume(astid, astCode);
    }

}
