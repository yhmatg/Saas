package com.common.esimrfid.core.http.api;

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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(@Query("user_id") String userId);

    //获取盘点数据（分页）
    //@param userId 用户userId
    //@return 用户分配的盘点任务列表

    @GET("inventory-server/inventoryorders")
    Observable<BaseResponse<InventoryOrderPage>> fetchAllIvnOrdersPage(@Query("size") Integer size, @Query("page") Integer page, @Query("user_id") String userId);

    //获取盘点条目详情
    //@param orderId 盘点单id
    //@return 盘点单详情

    @GET("inventory-server/inventoryorders/{orderId}/detail/unpage")
    Observable<BaseResponse<ResultInventoryDetail>> fetchAllInvDetails(@Path("orderId") String orderId, @Query("my_tasks") String myTask);

    //获取不同位置下资产数量(数据权限版本后使用)
    //@return 位置和对应资产数目

    @GET("assets-server/dashboard/countbyloc")
    Observable<BaseResponse<List<AssetLocNmu>>> getAssetsNmbDiffLocation();

    //获取不同状态的资产数量
    //@return 不同状态下的资产数目
    //@GET("/assets-server/assets/countbystatus")(修改数据权限前使用)
    //修改数据权限后使用
    @GET("assets-server/dashboard/countbystatus")
    Observable<BaseResponse<AssetStatusNum>> getAssetsNmbDiffStatus();

    //查询所有管理员
    //@return 所有管理员信息

    @GET("user-server/sysusers/querymanager/unpage")
    Observable<BaseResponse<List<MangerUser>>> getAllManagerUsers();

    //查询所有公司
    //@return 所有公司信息

    @GET("user-server/orgs/corps")
    Observable<BaseResponse<List<CompanyBean>>> getAllCompany();

    //获取公司下的所有部门
    //@param comId 公司id
    //@return 公司下所有部门

    @GET("user-server/orgs/{comId}/subs")
    Observable<BaseResponse<List<DepartmentBean>>> getAllDeparts(@Path("comId") String comId);

    //获取所有资产类型
    //@return
    //所有资产类型
    @GET("assets-server/assetstypes")
    Observable<BaseResponse<List<AssetsType>>> getAllAssetsType();

    //获取所有资产位置
    //@return 所有资产位置

    @GET("assets-server/locations")
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

    @POST("inventory-server/inventoryorders/createbyapp")
    Observable<BaseResponse<CreateInvResult>> createNewInventory(@Body InventoryParameter invpara);

    //模糊查询资产详情（写入标签）分页
    //@param patternName 资产过滤信息
    //@return 资产列表

    @GET("assets-server/assets")
    Observable<BaseResponse<AssetsInfoPage>> fetchPageAssetsInfos(@Query("size") Integer size, @Query("page") Integer page, @Query("pattern_name") String patternName);

    //模糊查询资产详情（写入标签）分页
    //@param patternName 资产过滤信息
    //@return 资产列表

    @GET("assets-server/assets/multiconditions")
    Observable<BaseResponse<AssetsListPage>> fetchPageAssetsList(@Query("size") Integer size, @Query("page") Integer page, @Query("pattern_name") String patternName, @Query("user_real_name") String userRealName, @Query("conditions") String conditions);

    //根据资产id或者二维码获取资产详情
    //@param ast_id 资产id ast_code 资产二维码
    //@return 资产详情信息

    @GET("assets-server/assets/detail")
    Observable<BaseResponse<AssetsAllInfo>> fetchAssetsInfo(@Query("ast_id") String astId, @Query("ast_barcode") String astCode);

    //版本更新
    //@return 版本更新详情

    @GET("tagorder-server/appversions/latest")
    Observable<BaseResponse<UpdateVersion>> updateVersion();

    //根据资产id查询资产履历
    @GET("assets-server/assets/astoptrecord")
    Observable<BaseResponse<List<AssetResume>>> fetchAssetResume(@Query("ast_id") String astid, @Query("ast_barcode") String astCode);

    //根据资产id或者二维码获取资产维修信息
    @GET("assets-server/assets/reprecords/unpage")
    Observable<BaseResponse<List<AssetRepair>>> fetchAssetRepair(@Query("ast_id") String astid, @Query("ast_barcode") String astCode);

    //获取所有员工
    @GET("user-server/emps/unpage")
    Observable<BaseResponse<List<MangerUser>>> getAllEmpUsers();

    //新盘点数据上传
    @POST("inventory-server/inventoryorders/{inv_id}/commit/new")
    Observable<BaseResponse> uploadInvAssets(@Path("inv_id") String orderId, @Query("uid") String uid, @Body List<AssetUploadParameter> invReq);

    //新完成盘点数据上传
    @POST("inventory-server/inventoryorders/{id}/finishwithinfo/new")
    Observable<BaseResponse> finishInvAssets(@Path("id") String orderId, @Query("uid") String uid, @Body List<AssetUploadParameter> invReq);

    //根据时间戳获取变动更新资产(分页)
    @GET("assets-server/assets/lastupdate")
    Observable<BaseResponse<LatestModifyPageAssets>> fetchLatestAssetsPage(@Query("lasttime") String lastTime, @Query("size") Integer size, @Query("page") Integer page);

    //新建报修单和审批流程相关
    @POST("assets-server/bussiness/apply/REPAIR")
    Observable<BaseResponse> createNewRepairOrder(@Body NewAssetRepairPara repariPara);

    //查询管理员权限范围
    @GET("user-server/sysusers/{id}/authscope")
    Observable<BaseResponse<DataAuthority>> getDataAuthority(@Path("id") String id);

    //根据资产id或者二维码获取资产详情
    //@param ast_id 资产id ast_code 资产二维码
    //@return 资产详情信息

    @GET("assets-server/assets/detail/withauth")
    Observable<BaseResponse<AssetsAllInfo>> fetchAssetsInfoWithAuth(@Query("ast_id") String astId, @Query("ast_barcode") String astCode);

    /**
     * 查询数据授权使用公司
     *
     * @param type 1:公司 0:部门
     * @return 数据授权使用公司
     */
    @GET("user-server/orgs/authOrgs")
    Observable<BaseResponse<List<CompanyBean>>> getAllAuthCompany(@Query("org_type") Integer type);

    //批量更新资产位置新
    @PUT("assets-server/general/assets/updatebatch")
    Observable<BaseResponse> updateAssetProp(@Body UpdateAssetsPara updateAssetsPara);

    //动态获取标题和logo
    @GET("user-server/globalconfigs")
    Observable<BaseResponse<TitleAndLogoResult>> getTitleAndLogo(@Query("tenantid") String tenantid, @Query("config_key") String configKey);

    //资产查找界面扫描使用
    @POST("assets-server/assets/byrfids")
    Observable<BaseResponse<List<SearchAssetsInfo>>> fetchScanAssetsEpc(@Body Set<String> epc);

    //资产查找界面模糊查询使用
    @GET("assets-server/assets/multiconditions")
    Observable<BaseResponse<SearchAssetsInfoPage>> fetchPageFilterAssetsList(@Query("size") Integer size, @Query("page") Integer page, @Query("pattern_name") String patternName);
}
