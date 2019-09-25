package com.common.esimrfid.core.http.api;

import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.UserInfo;
import com.common.esimrfid.core.bean.nanhua.UserLoginResponse;
import com.common.esimrfid.core.bean.nanhua.invdetailbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;
import com.common.esimrfid.core.bean.nanhua.invscannbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.requisitionbeans.RequisitionAssetInfo;
import com.common.esimrfid.core.bean.nanhua.requisitionbeans.RequisitionDetailInfo;
import com.common.esimrfid.core.bean.nanhua.requisitionbeans.RequisitionItemInfo;

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
    @GET("inventory-server/inventoryorders")
    Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(@Query("user_id")String userId);

    //获取盘点条目详情
    @GET("inventory-server/inventoryorders/{orderId}/detail")
    Observable<BaseResponse<ResultInventoryDetail>>  fetchAllInvDetails(@Path("orderId") String orderId);

    //完成盘点
    @POST("inventory-server/inventoryorders/{orderId}/finish")
    Observable<BaseResponse> finishInvOrder(@Path("orderId")String orderId,@Query("uid")String uid,@Query("finish_remark")String remark);

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
    @GET("/assets-server/requisitions/{odrId}/detail")
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

}
