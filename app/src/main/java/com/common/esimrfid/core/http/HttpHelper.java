package com.common.esimrfid.core.http;

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

/**
 * @author yhm
 * @date 2017/11/27
 */

public interface HttpHelper {

    Observable<BaseResponse<UserLoginResponse>> login(UserInfo userInfo);

    Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(String userId);

    Observable<BaseResponse<ResultInventoryDetail>> fetchAllInvDetails(String orderId);

    Observable<BaseResponse> uploadInvDetails(String orderId, List<String> invDetails, String uid);

    Observable<BaseResponse> finishInvOrder(String orderId, String uid, String remark);

    Observable<BaseResponse<List<AssetsInfo>>> fetchScanAssetsInfons(Set<String> ecps);

    Observable<BaseResponse<List<RequisitionItemInfo>>> fetchAllRequisitions();

    Observable<BaseResponse<List<RequisitionAssetInfo>>> getRequisitionInfons(Set<String> ecps);

    Observable<BaseResponse<List<RequisitionAssetInfo>>> fetchRequestAssetsInfos(String patternName);

    Observable<BaseResponse<RequisitionDetailInfo>> fetchRequsitionDetailByid(String odrId);

    Observable<BaseResponse> uploadResAssets(String requestId, List<String> epcs);
}
