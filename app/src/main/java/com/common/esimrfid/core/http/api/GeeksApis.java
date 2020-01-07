package com.common.esimrfid.core.http.api;

import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.CreateInvResult;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.InventoryParameter;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.home.CompanyInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionAssetInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionDetailInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionItemInfo;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.update.UpdateVersion;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author yhm
 * @date 2018/2/12
 */

public interface GeeksApis {

    String HOST = "http://172.16.63.25:12000"; //佳航25，言娇35，梦伟32

    //登录
    @POST("user-server/userauth/loginwithinfo")
    Observable<BaseResponse<UserLoginResponse>> login(@Body UserInfo userInfo);

    //获取盘点数据
    //@GET("inventory-server/inventoryorders")
    @GET("inventory-server/inventoryorders/unpage")
    Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(@Query("user_id")String userId);

    //获取盘点条目详情
    @GET("inventory-server/inventoryorders/{orderId}/detail")
    Observable<BaseResponse<ResultInventoryDetail>>  fetchAllInvDetails(@Path("orderId") String orderId);

    //完成盘点
    @POST("inventory-server/inventoryorders/{orderId}/finish")
    Observable<BaseResponse> finishInvOrder(@Path("orderId")String orderId,@Query("uid")String uid,@Query("finish_remark")String remark);

    //完成盘点携带资产编号id
    @POST("inventory-server/inventoryorders/{orderId}/finishwithinfo")
    Observable<BaseResponse> finishInvOrderWithAsset(@Path("orderId")String orderId,@Query("uid")String uid,@Body List<String> invDetails);

    //盘点数据上传
    @POST("inventory-server/inventoryorders/{orderId}/commit")
    Observable<BaseResponse> uploadInvDetails(@Path("orderId")String orderId,@Body List<String> invDetails ,@Query("uid")String uid);

    //获取查询数据
    @POST("assets-server/assets/byrfids")
    Observable<BaseResponse<List<AssetsInfo>>> fetchScanAssetsInfons(@Body Set<String> ecps);

    //获取领用单列表
    @GET("/assets-server/requisitions/unpage")
    Observable<BaseResponse<List<RequisitionItemInfo>>> fetchAllRequisitions();

    //通过单号获取领用单详情
    @GET("/assets-server/requisitions/{odrId}")
    Observable<BaseResponse<RequisitionDetailInfo>> fetchRequsitionDetailByid(@Path("odrId")String odrId);

    //模糊查询资产详情
    @GET("/assets-server/assets/unpage")
    Observable<BaseResponse<List<RequisitionAssetInfo>>> fetchRequestAssetsInfos(@Query("pattern_name")String patternName);

    //确认领用资产后上传 id 领用单id
    @POST("/assets-server/requisitions/{id}/reqAssets")
    Observable<BaseResponse> uploadResAssets(@Path("id")String requestId,@Body List<String> epcs);

    //根据epc获取资产详情
    @POST("assets-server/assets/byrfids")
    Observable<BaseResponse<List<RequisitionAssetInfo>>> getRequisitionInfons(@Body Set<String> ecps);

    //获取不同位置下资产数量
    @GET("/assets-server/assets/countbyloc")
    Observable<BaseResponse<HashMap<String,Integer>>> getAssetsNmbDiffLocation();

    //获取不同状态的资产数量
    @GET("/assets-server/assets/countbystatus")
    Observable<BaseResponse<AssetStatusNum>> getAssetsNmbDiffStatus();

    //查询所有管理员
    @GET("/user-server/sysusers/querymanager/unpage")
    //@GET("/user-server/emps/all/unpage")
    Observable<BaseResponse<List<MangerUser>>> getAllManagerUsers();

    //查询所有公司
    @GET("/user-server/orgs/corps")
    Observable<BaseResponse<List<CompanyBean>>> getAllCompany();

    //获取公司下的所有部门
    @GET("/user-server/orgs/{comId}/subs")
    Observable<BaseResponse<List<DepartmentBean>>> getAllDeparts(@Path("comId")String comId);

    //获取所有资产类型
    @GET("/assets-server/assetstypes")
    Observable<BaseResponse<List<AssetsType>>> getAllAssetsType();

    //获取所有资产位置
    @GET("/assets-server/locations")
    Observable<BaseResponse<List<AssetsLocation>>> getAllAssetsLocation();

    //创建盘点单
    @POST("/inventory-server/inventoryorders")
    Observable<BaseResponse<CreateInvResult>> createNewInventory(@Body InventoryParameter invpara);
	
	//模糊查询资产详情（写入标签）
    @GET("/assets-server/assets/unpage")
    Observable<BaseResponse<List<AssetsInfo>>>fetchWriteAssetsInfos(@Query("pattern_name")String patternName);

    //根据Epc查询资产详情（资产查找）
    @POST("/assets-server/assets/byrfids")
    Observable<BaseResponse<List<AssetsInfo>>> fetchScanAssets(@Body Set<String> Epcs);

    @GET("/assets-server/assets/{astid}")
    Observable<BaseResponse<AssetsDetailsInfo>> fetchAssetsInfoById(@Path("astid") String astid);

    //版本更新
    @GET("/tagorder-server/appversions/latest")
    Observable<BaseResponse<UpdateVersion>> updateVersion();

    //获取公司信息详情
    @GET("/user-server/orgs/selectCurrentOrg")
    Observable<BaseResponse<CompanyInfo>> getCompanyInfo();
}
