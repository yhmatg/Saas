package com.common.esimrfid.core.http;

import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
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
import com.common.esimrfid.ui.assetrepair.AssetRepairParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import retrofit2.http.Body;

/**
 * @author yhm
 * @date 2017/11/27
 */

public interface HttpHelper {

    Observable<BaseResponse<UserLoginResponse>> login(UserInfo userInfo);

    Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(String userId);

    Observable<BaseResponse<ResultInventoryDetail>> fetchAllInvDetails(String orderId);

    Observable<BaseResponse> uploadInvDetails(String orderId, List<String> invDetails, String uid);

    Observable<BaseResponse<HashMap<String, Integer>>> getAssetsNmbDiffLocation();

    Observable<BaseResponse<AssetStatusNum>> getAssetsNmbDiffStatus();

    Observable<BaseResponse<List<MangerUser>>> getAllManagerUsers();

    Observable<BaseResponse<List<CompanyBean>>> getAllCompany();

    Observable<BaseResponse<List<DepartmentBean>>> getAllDeparts(String comId);

    Observable<BaseResponse<List<AssetsType>>> getAllAssetsType();

    Observable<BaseResponse<List<AssetsLocation>>> getAllAssetsLocation();

    Observable<BaseResponse<CreateInvResult>> createNewInventory(InventoryParameter invpara);

    Observable<BaseResponse<List<AssetsInfo>>> fetchWriteAssetsInfo(String patternName);

    Observable<BaseResponse> finishInvOrderWithAsset(String orderId, String uid, List<String> invDetails);

    Observable<BaseResponse<List<AssetsInfo>>> fetchScanAssets(Set<String> Epcs);

    Observable<BaseResponse<AssetsDetailsInfo>> fetchAssetsInfo(String astId);

    Observable<BaseResponse<UpdateVersion>> updateVersion();

    Observable<BaseResponse<CompanyInfo>> getCompanyInfo();

    Observable<BaseResponse<AssetsDetailsInfo>> getAstInfo(String astCode);

    Observable<BaseResponse> createNewRepairOrder(AssetRepairParameter repairParameter);

	Observable<BaseResponse<List<AssetResume>>> fetchAssetResume(String astId);

    Observable<BaseResponse<List<AssetRepair>>> fetchAssetRepair(String astid);
}
