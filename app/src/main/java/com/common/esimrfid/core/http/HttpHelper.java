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
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfoPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.core.bean.update.UpdateVersion;

import java.util.List;
import java.util.Set;

import io.reactivex.Observable;

/**
 * @author yhm
 * @date 2017/11/27
 */

public interface HttpHelper {

    Observable<BaseResponse<UserLoginResponse>> login(UserInfo userInfo);

    Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(String userId);

    Observable<BaseResponse<ResultInventoryDetail>> fetchAllInvDetails(String orderId);

    Observable<BaseResponse<AssetStatusNum>> getAssetsNmbDiffStatus();

    Observable<BaseResponse<List<MangerUser>>> getAllManagerUsers();

    Observable<BaseResponse<List<CompanyBean>>> getAllCompany();

    Observable<BaseResponse<List<DepartmentBean>>> getAllDeparts(String comId);

    Observable<BaseResponse<List<AssetsType>>> getAllAssetsType();

    Observable<BaseResponse<List<AssetsLocation>>> getAllAssetsLocation();

    Observable<BaseResponse<CreateInvResult>> createNewInventory(InventoryParameter invpara);

    Observable<BaseResponse<AssetsAllInfo>> fetchAssetsInfo(String astId, String astCode);

    Observable<BaseResponse<UpdateVersion>> updateVersion();

    Observable<BaseResponse<List<AssetResume>>> fetchAssetResume(String astid, String astCode);

    Observable<BaseResponse<List<AssetRepair>>> fetchAssetRepair(String astid, String astCode);

    Observable<BaseResponse<List<MangerUser>>> getAllEmpUsers();

    Observable<BaseResponse> uploadInvAssets(String orderId, String uid, List<AssetUploadParameter> invDetails);

    Observable<BaseResponse> finishInvAssets(String orderId, String uid, List<AssetUploadParameter> invDetails);

    Observable<BaseResponse<AssetsInfoPage>> fetchPageAssetsInfos(Integer size, Integer page, String patternName);

    Observable<BaseResponse> createNewRepairOrder(NewAssetRepairPara repariPara);

    Observable<BaseResponse<AssetsListPage>> fetchPageAssetsList(Integer size, Integer page, String patternName, String userRealName, String conditions);

    Observable<BaseResponse<DataAuthority>> getDataAuthority(String id);

    Observable<BaseResponse<List<AssetLocNmu>>> getAssetsNmbDiffLocation();

    Observable<BaseResponse<AssetsAllInfo>> fetchAssetsInfoWithAuth(String astId, String astCode);

    Observable<BaseResponse<List<CompanyBean>>> getAllAuthCompany(Integer type);

    Observable<BaseResponse<InventoryOrderPage>> fetchAllIvnOrdersPage(Integer size, Integer page, String userId);

    Observable<BaseResponse<LatestModifyPageAssets>> fetchLatestAssetsPage(String lastTime, Integer size, Integer page);

    Observable<BaseResponse> updateAssetProp(UpdateAssetsPara updateAssetsPara);

    Observable<BaseResponse<TitleAndLogoResult>> getTitleAndLogo(String tenantid, String configKey);

    Observable<BaseResponse<List<SearchAssetsInfo>>> fetchScanAssetsEpc(Set<String> epc);

    Observable<BaseResponse<SearchAssetsInfoPage>> fetchPageFilterAssetsList(Integer size, Integer page, String patternName);
}
