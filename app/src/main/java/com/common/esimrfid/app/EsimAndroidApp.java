package com.common.esimrfid.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.common.esimrfid.BuildConfig;
import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.utils.Utils;
import com.common.esimrfid.utils.logger.MyCrashListener;
import com.common.esimrfid.utils.logger.TxtFormatStrategy;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.xuexiang.xlog.XLog;
import com.xuexiang.xlog.crash.CrashHandler;

import java.util.ArrayList;

/**
 * @author yhm
 * @date 2017/11/27
 */
//public class EsimAndroidApp extends Application implements HasActivityInjector {
public class EsimAndroidApp extends Application {


    private static EsimAndroidApp instance;
    private RefWatcher refWatcher;
    private UserLoginResponse mUserLoginResponse;
    private static IEsimUhfService mIEsimUhfService ;
    private ArrayList<BaseActivity> activities = new ArrayList<>();

    public static synchronized EsimAndroidApp getInstance() {
        return instance;
    }

    public static RefWatcher getRefWatcher(Context context) {
        EsimAndroidApp application = (EsimAndroidApp) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        refWatcher = LeakCanary.install(this);
        instance = this;
        initLogger();
        Utils.init(this);
        //崩溃日志保存到本地
        ///storage/emulated/0/Android/data/com.common.esimrfid/cache/crash_log
        XLog.init(this);
        CrashHandler.getInstance().setOnCrashListener(new MyCrashListener());

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }


    private void initLogger() {
        //DEBUG版本才打控制台log
        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder().
                    tag(getString(R.string.app_name)).build()));
        }
        //把log存到本地
        Logger.addLogAdapter(new DiskLogAdapter(TxtFormatStrategy.newBuilder().
                tag(getString(R.string.app_name)).build(getPackageName(), getString(R.string.app_name))));
    }

    public void setUserLoginResponse(UserLoginResponse userLoginResponse) {
        mUserLoginResponse = userLoginResponse;
    }

    public UserLoginResponse getUserLoginResponse() {
        return mUserLoginResponse;
    }

    public static IEsimUhfService getIEsimUhfService(){
        return mIEsimUhfService;
    }

    public static void setIEsimUhfService(IEsimUhfService iEsimUhfService){
        mIEsimUhfService = iEsimUhfService;
    }

    public void addActivity(BaseActivity activity){
        activities.add(activity);
    }

    public void removeActivity(BaseActivity activity){
        activities.remove(activity);
    }

    public void exitActivitys(){
        for (int i = 0; i < activities.size(); i++) {
            activities.get(i).finish();
        }
    }

}
