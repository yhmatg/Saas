package com.common.esimrfid.core.http.interceptor;

import android.util.Log;

import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.utils.StringUtils;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
//网络接口拦截器，用户登录界面修改默认的URL，每次网络请求都会拦截,使用用户设置的最新URL
public class AppendUrlIntercepter implements Interceptor {
    private String baseUrl;
    public AppendUrlIntercepter(String baseUrl){
        this.baseUrl=baseUrl;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = DataManager.getInstance().getToken();
        String cacheHost= DataManager.getInstance().getHostUrl();

        Request oldRequest = chain.request();
        HttpUrl.Builder builder = oldRequest
                .url()
                .newBuilder();
        //add 20190729 start
        builder.addQueryParameter("token",token);
        //add 20190729 start
        HttpUrl httpUrl = oldRequest.url();
        if (!StringUtils.isEmpty(cacheHost) && !baseUrl.equals(cacheHost)) {
            if(cacheHost.startsWith("https")){
                builder.scheme("https");
            }else {
                builder.scheme("http");
            }
            cacheHost=cacheHost.replaceAll("http(s)?://","");
            String[] split = cacheHost.split(":");
            if(split.length == 1){
                String cacheH = split[0];
                builder.host(cacheH);
            }else if(split.length == 2){
                String cacheH = split[0];
                String cacheP = split[1];
                builder.host(cacheH).port(Integer.valueOf(cacheP));
            }
        }
        Log.e("AppendUrlIntercepter","builder.build()===" + builder.build());
        Request newRequest = oldRequest
                .newBuilder()
                //.addHeader("Authorization",token)
                .url(builder.build())
                .build();
        return chain.proceed(newRequest);
    }
}