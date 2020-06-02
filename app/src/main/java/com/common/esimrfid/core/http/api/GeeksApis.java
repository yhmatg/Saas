package com.common.esimrfid.core.http.api;

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
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author yhm
 * @date 2018/2/12
 */

public interface GeeksApis {

    //登录
    //@param UserInfo 用户账号 用户密码
    //@return 用户基本信息,token

    @POST("user-server/userauth/loginwithinfo")
    Observable<BaseResponse<UserLoginResponse>> login(@Body UserInfo userInfo);

    //获取盘点数据
    //@param userId 用户userId
    //@return 用户分配的盘点任务列表

    @GET("inventory-server/inventoryorders/unpage")
    Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(@Query("user_id")String userId);

    //获取盘点条目详情
    //@param orderId 盘点单id
    //@return 盘点单详情

    @GET("inventory-server/inventoryorders/{orderId}/detail")
    Observable<BaseResponse<ResultInventoryDetail>>  fetchAllInvDetails(@Path("orderId") String orderId);

    //完成盘点携带资产编号id
    //@param orderId 盘点单id
    //@param uid 用户id
    //@param invDetails 已盘点资产id集合finishwithinfo
    //@return 操作结果

    @POST("inventory-server/inventoryorders/{orderId}/finishwithinfo")
    Observable<BaseResponse> finishInvOrderWithAsset(@Path("orderId")String orderId,@Query("uid")String uid,@Body List<String> invDetails);

    //盘点数据上传
    //@param orderId 盘点单id
    //@param invDetails 已盘点资产id集合
    //@param uid 用户id
    //@return 操作结果

    @POST("inventory-server/inventoryorders/{orderId}/commit")
    Observable<BaseResponse> uploadInvDetails(@Path("orderId")String orderId,@Body List<String> invDetails ,@Query("uid")String uid);

    //获取不同位置下资产数量
    //@return 位置和对应资产数目

    @GET("/assets-server/assets/countbyloc")
    Observable<BaseResponse<HashMap<String,Integer>>> getAssetsNmbDiffLocation();

    //获取不同状态的资产数量
    //@return 不同状态下的资产数目

    @GET("/assets-server/assets/countbystatus")
    Observable<BaseResponse<AssetStatusNum>> getAssetsNmbDiffStatus();

    //查询所有管理员
    //@return 所有管理员信息

    @GET("/user-server/sysusers/querymanager/unpage")
    Observable<BaseResponse<List<MangerUser>>> getAllManagerUsers();

    //查询所有公司
    //@return 所有公司信息

    @GET("/user-server/orgs/corps")
    Observable<BaseResponse<List<CompanyBean>>> getAllCompany();

    //获取公司下的所有部门
    //@param comId 公司id
    //@return 公司下所有部门

    @GET("/user-server/orgs/{comId}/subs")
    Observable<BaseResponse<List<DepartmentBean>>> getAllDeparts(@Path("comId")String comId);

    //获取所有资产类型
    //@return
    //所有资产类型
    @GET("/assets-server/assetstypes")
    Observable<BaseResponse<List<AssetsType>>> getAllAssetsType();

    //获取所有资产位置
    //@return 所有资产位置

    @GET("/assets-server/locations")
    Observable<BaseResponse<List<AssetsLocation>>> getAllAssetsLocation();

    //创建盘点单
    //@param inv_name 盘点单名称（必要）
    //@param inv_assigner_id 盘点人（必要）
    //@param inv_exptfinish_date 预期完成时间（必要）
    //@param inv_used_corp_filter 使用公司
    //@param inv_belong_corp_filter 所属公司
    //@param inv_used_dept_filter 使用部门
    //@param inv_type_filter 资产分类
    //@param inv_loc_filter 存放地点
    //@return 新建盘点单详情

    @POST("/inventory-server/inventoryorders")
    Observable<BaseResponse<CreateInvResult>> createNewInventory(@Body InventoryParameter invpara);
	
	//模糊查询资产详情（写入标签）
    //@param patternName 资产过滤信息
    //@return 资产列表

    @GET("/assets-server/assets/unpage")
    //@GET("/assets-server/assets/findforapp")
    Observable<BaseResponse<List<AssetsInfo>>>fetchWriteAssetsInfos(@Query("pattern_name")String patternName);

    //根据Epc查询资产详情（资产查找）
    //@param Epcs EPC集合
    //@return 资产信息列表

    @POST("/assets-server/assets/byrfids")
    Observable<BaseResponse<List<AssetsInfo>>> fetchScanAssets(@Body Set<String> Epcs);

    //根据资产id或者二维码获取资产详情
    //@param ast_id 资产id ast_code 资产二维码
    //@return 资产详情信息
   
	 @GET("/assets-server/assets/detail")
    Observable<BaseResponse<AssetsDetailsInfo>> fetchAssetsInfo(@Query("ast_id") String astId,@Query("ast_code") String astCode);

    //版本更新
    //@return 版本更新详情

    @GET("/tagorder-server/appversions/latest")
    Observable<BaseResponse<UpdateVersion>> updateVersion();

    //获取公司信息详情
    //@return 公司详情信息

    @GET("/user-server/sysusers/selectCurrentOrg")
    Observable<BaseResponse<CompanyInfo>> getCompanyInfo();
	
	 //根据资产id查询资产履历
    @GET("assets-server/assets/astoptrecord")
    Observable<BaseResponse<List<AssetResume>>> fetchAssetResume(@Query("ast_id") String astid,@Query("ast_code") String astCode);

	//根据资产id或者二维码获取资产维修信息
	 @GET("/assets-server/assets/reprecords/unpage")
    Observable<BaseResponse<List<AssetRepair>>> fetchAssetRepair(@Query("ast_id") String astid,@Query("ast_code") String astCode);

    //新建报修单
    /**
     *
     * @param repairParameter
     * @return
     */
    @POST("/assets-server/repairs/bymanager")
    Observable<BaseResponse> createNewRepairOrder(@Body AssetRepairParameter repairParameter);
}
