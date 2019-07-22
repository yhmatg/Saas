package com.common.esimrfid.core.http;
import com.common.esimrfid.core.bean.BaseResponse;
import com.common.esimrfid.core.bean.CorpInfo;
import com.common.esimrfid.core.bean.InvDetail;
import com.common.esimrfid.core.bean.InvOrder;
import com.common.esimrfid.core.bean.SignatureCard;
import com.common.esimrfid.core.bean.UserInfo;
import com.common.esimrfid.core.bean.UserLoginResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author yhm
 * @date 2017/11/27
 */

public interface HttpHelper {

    Observable<BaseResponse<UserLoginResponse>> login(UserInfo userInfo);

    Observable<BaseResponse<List<InvOrder>>> fetchAllInvOrders();

    Observable<BaseResponse<List<InvDetail>>> fetchAllInvDetails(String orderId);

    Observable<BaseResponse<List<SignatureCard>>> fetchAllSignatureCards(String corpAccount);

    Observable<BaseResponse> uploadInvDetails(List<InvDetail> invDetails, String orderId);

    Observable<BaseResponse> finishInvOrder(String orderId);

    Observable<BaseResponse<CorpInfo>> findCorpInfoByAll(String corpName, String corpAccount, String cardCode);

}
