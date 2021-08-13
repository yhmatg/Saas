package com.common.esimrfid.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.settings.SettingConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.presenter.settings.SettingPresenter;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingConstract.View {
    @BindView(R.id.title_back)
    ImageView mBackImg;
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.tv_version)
    TextView mVersion;
    @BindView(R.id.function_setting)
    LinearLayout right;

    @Override
    public SettingPresenter initPresenter() {
        return new SettingPresenter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.setting_title);
        String versionName = getAppVersionName(this);
        if(!StringUtils.isEmpty(versionName)){
            mVersion.setText("v" + versionName);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.bt_loginout, R.id.title_back,R.id.function_setting,R.id.sync_data})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.bt_loginout:
                DataManager.getInstance().setLoginStatus(false);
                EsimAndroidApp.getInstance().exitActivitys();
                startLoginActivity();
                //System.exit(0);
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.function_setting:
                if(CommonUtils.isNormalClick()){
                    startActivity(new Intent(this,FunctionActivity.class));
                }
                break;
            case R.id.sync_data:
                mPresenter.fetchLatestPageAssets(500, 1);
                break;
        }
    }

    public String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionName;
    }
}
