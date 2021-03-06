package com.common.esimrfid.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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
    private MaterialDialog progressDialog;
    private ProgressBar progressBar;
    private TextView tvProgress;

    @Override
    public SettingPresenter initPresenter() {
        return new SettingPresenter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.setting_title);
        String versionName = getAppVersionName(this);
        long appVersionCode = getAppVersionCode(this);
        if (!StringUtils.isEmpty(versionName)) {
            mVersion.setText("v" + versionName +"(" + appVersionCode +")");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.bt_loginout, R.id.title_back, R.id.function_setting, R.id.clear_data,R.id.sync_data})
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
                if (CommonUtils.isNormalClick()) {
                    startActivity(new Intent(this, FunctionActivity.class));
                }
                break;
            case R.id.clear_data:
                mPresenter.clearAllData();
                break;
            case R.id.sync_data:
                mPresenter.fetchLatestPageAssets(1000, 1);
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

    public long getAppVersionCode(Context context) {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionCode;
    }

    @Override
    public void showUpdateProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        } else {
            View contentView = LayoutInflater.from(this).inflate(R.layout.downloading_layout, null);
            progressBar = contentView.findViewById(R.id.pb);
            tvProgress = contentView.findViewById(R.id.tv_progress);
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .customView(contentView, false);
            progressDialog = builder.show();
            progressDialog.setCanceledOnTouchOutside(false);
            Window window = progressDialog.getWindow();
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public void updateProgress(int progress, int total) {
        float finalProgress = (float)progress / total * 100;
        tvProgress.setText(String.format(getString(R.string.versionchecklib_progress), (int)finalProgress));
        progressBar.setProgress((int)finalProgress);
    }

    @Override
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
