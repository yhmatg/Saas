package com.common.esimrfid.core.http.api;

import com.common.esimrfid.core.bean.BaseResponse;
import com.common.esimrfid.core.bean.CorpInfo;
import com.common.esimrfid.core.bean.InvDetail;
import com.common.esimrfid.core.bean.InvOrder;
import com.common.esimrfid.core.bean.SignatureCard;
import com.common.esimrfid.core.bean.UserInfo;
import com.common.esimrfid.core.bean.UserLoginResponse;

import java.util.List;

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

    String HOST = "http://172.16.63.26:9090";

    @POST("sysuser/login")
    Observable<BaseResponse<UserLoginResponse>> login(@Body UserInfo userInfo);

    @GET("api/v1/inventoryorders")
    Observable<BaseResponse<List<InvOrder>>> fetchAllIvnOrders();

    @GET("api/v1/inventoryorders/app/{orderId}/detail")
    Observable<BaseResponse<List<InvDetail>>>  fetchAllInvDetails(@Path("orderId") String orderId);

    @GET("api/v1/card/info/querynopage")
    Observable<BaseResponse<List<SignatureCard>>> fetchAllSignatureCards(@Query("corpAccount") String corpAccount);

    @POST("api/v1/inventoryorders/{orderId}/invdetail")
    Observable<BaseResponse> uploadInvDetails(@Body List<InvDetail> invDetails, @Path("orderId") String orderId);

    @POST("api/v1/inventoryorders/{orderId}/invorder")
    Observable<BaseResponse> finishInvOrder(@Path("orderId") String orderId);

    @GET("api/v1/card/info/query/condition")
    Observable<BaseResponse<CorpInfo>> findCorpInfoByAll(@Query("corpName") String corpName, @Query("corpAccount") String corpAccount, @Query("cardCode") String cardCode);


}
