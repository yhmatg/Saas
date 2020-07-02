package com.common.esimrfid.app;


import android.graphics.Color;

import java.io.File;


/**
 * @author yhm
 * @date 2017/11/27
 */

public class Constants {

    static final String BUGLY_ID = "a29fb52485";

    public static final String MY_SHARED_PREFERENCE = "my_shared_preference";

    /**
     * url
     */
    public static final String COOKIE = "Cookie";

    /**
     * Path
     */
    public static final String PATH_DATA = EsimAndroidApp.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";


    /**
     * Tab colors
     */
    public static final int[] TAB_COLORS = new int[]{
            Color.parseColor("#90C5F0"),
            Color.parseColor("#91CED5"),
            Color.parseColor("#F88F55"),
            Color.parseColor("#C0AFD0"),
            Color.parseColor("#E78F8F"),
            Color.parseColor("#67CCB7"),
            Color.parseColor("#F6BC7E")
    };

    public static final long DOUBLE_INTERVAL_TIME = 2000;

    /**
     * Shared Preference key
     */

    public static final String LOGIN_STATUS = "login_status";

    public static final String AUTO_CACHE_STATE = "auto_cache_state";

    //提交服务器成功
    public static final String CODE_SUC = "200000";

    //提交本地成功
    public static final String CODE_LOCAL_SUC = "200001";

    public static final String CODE_FAIL = "2000FF";

    public static final String ACCOUNT = "account";

    public static final String PASSWORD = "password";

    public static final String HOSTURL = "hostUrl";

    public static final String OPENSOUND = "openSound";

    public static final String TOKEN = "token";

    public static final String USERLOGINRESPONSE = "userLoginResponse";

    public static final String OPEN = "open";

    public static final String SLEDBEEPER = "sledBeeper";

    public static final String HOSTBEEPER = "hostBeeper";

    public static final String LATEST_SYNC_ASSETS_TIME = "latest_update_time";
    //add yhm 20190707 end


}
