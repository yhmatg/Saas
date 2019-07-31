package com.common.esimrfid.core.http.api;

import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.UserInfo;
import com.common.esimrfid.core.bean.nanhua.UserLoginResponse;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author yhm
 * @date 2018/2/12
 */

public interface GeeksApis {

    String HOST = "http://172.16.63.25:12000";

    //登录
    @POST("user-server/userauth/loginwithinfo")
    Observable<BaseResponse<UserLoginResponse>> login(@Body UserInfo userInfo);

    //获取盘点数据
    @GET("inventory-server/inventoryorders")
    Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(@Query("userId")String userId);

}
