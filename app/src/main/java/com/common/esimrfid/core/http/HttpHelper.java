package com.common.esimrfid.core.http;

import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.UserInfo;
import com.common.esimrfid.core.bean.nanhua.UserLoginResponse;
import com.common.esimrfid.core.bean.nanhua.invdetailbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author yhm
 * @date 2017/11/27
 */

public interface HttpHelper {

    Observable<BaseResponse<UserLoginResponse>> login(UserInfo userInfo);

    Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(String userId);

    Observable<BaseResponse<ResultInventoryDetail>>  fetchAllInvDetails(String orderId);

    Observable<BaseResponse> uploadInvDetails(String orderId, List<String> invDetails);

    Observable<BaseResponse> finishInvOrder(String orderId, String uid, String remark);
}
