package com.common.xfxj.utils.logger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.widget.Toast;

import com.xuexiang.xlog.crash.ICrashHandler;
import com.xuexiang.xlog.crash.OnCrashListener;
import com.xuexiang.xlog.crash.XCrash;

public class MyCrashListener implements OnCrashListener {
    @Override
    public void onCrash(Context context, ICrashHandler crashHandler, Throwable throwable) {
        if(isDebug(context)){
            CrashInfo crashInfo = CrashUtils.parseCrash(context, throwable)
                    .setCrashLogFilePath(crashHandler.getCrashLogFile().getPath());
            Intent intent = new Intent(context, CrashActivity.class);
            intent.putExtra(CrashActivity.KEY_CRASH_INFO, crashInfo);
            intent.putExtra(CrashActivity.KEY_MAIL_INFO, XCrash.getPrepareMailInfo());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        Toast.makeText(context, "程序异常日志路径：/storage/emulated/0/Android/data/com.common.xfxj/cache/crash_log", Toast.LENGTH_LONG).show();
        crashHandler.setIsHandledCrash(true);
    }

    private boolean isDebug(Context context){
        return context.getApplicationInfo()!=null&&
                (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)!=0;
    }
}
