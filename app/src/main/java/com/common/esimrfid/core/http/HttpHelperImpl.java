package com.common.esimrfid.core.http;



import com.common.esimrfid.core.bean.BaseResponse;
import com.common.esimrfid.core.bean.CorpInfo;
import com.common.esimrfid.core.bean.InvDetail;
import com.common.esimrfid.core.bean.InvOrder;
import com.common.esimrfid.core.bean.SignatureCard;
import com.common.esimrfid.core.bean.UserInfo;
import com.common.esimrfid.core.bean.UserLoginResponse;
import com.common.esimrfid.core.http.api.GeeksApis;
import com.common.esimrfid.core.http.client.RetrofitClient;
import java.util.List;
import io.reactivex.Observable;

/**
 * 对外隐藏进行网络请求的实现细节
 *
 * @author yhm
 * @date 2017/11/27
 */

public class HttpHelperImpl implements HttpHelper {

    private GeeksApis mGeeksApis;

    private volatile static HttpHelperImpl INSTANCE = null;


    private HttpHelperImpl(GeeksApis geeksApis) {
        mGeeksApis = geeksApis;
    }

    public static HttpHelperImpl getInstance(){
        if (INSTANCE == null) {
            synchronized (HttpHelperImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpHelperImpl(RetrofitClient.getInstance().create(GeeksApis.class));
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    ////add yhm 20190707 start

    @Override
    public Observable<BaseResponse<UserLoginResponse>> login(UserInfo userInfo) {
        return mGeeksApis.login(userInfo);
    }

    @Override
    public Observable<BaseResponse<List<InvOrder>>> fetchAllInvOrders() {
        return mGeeksApis.fetchAllIvnOrders();
    }

    @Override
    public Observable<BaseResponse<List<InvDetail>>> fetchAllInvDetails(String orderId) {
        return mGeeksApis.fetchAllInvDetails(orderId);
    }

    @Override
    public Observable<BaseResponse<List<SignatureCard>>> fetchAllSignatureCards(String corpAccount) {
        return mGeeksApis.fetchAllSignatureCards(corpAccount);
    }

    @Override
    public Observable<BaseResponse> uploadInvDetails(List<InvDetail> invDetails, String orderId) {
        return mGeeksApis.uploadInvDetails(invDetails,orderId);
    }

    @Override
    public Observable<BaseResponse> finishInvOrder(String orderId) {
        return mGeeksApis.finishInvOrder(orderId);
    }

    @Override
    public Observable<BaseResponse<CorpInfo>> findCorpInfoByAll(String corpName, String corpAccount, String cardCode) {
        return mGeeksApis.findCorpInfoByAll(corpName,corpAccount,cardCode);
    }




}
