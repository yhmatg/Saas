package com.common.esimrfid.core.http;



import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.UserInfo;
import com.common.esimrfid.core.bean.nanhua.UserLoginResponse;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;
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

    @Override
    public Observable<BaseResponse<UserLoginResponse>> login(UserInfo userInfo) {
        return mGeeksApis.login(userInfo);
    }

    @Override
    public Observable<BaseResponse<List<ResultInventoryOrder>>> fetchAllIvnOrders(String userId) {
        return mGeeksApis.fetchAllIvnOrders(userId);
    }


}
