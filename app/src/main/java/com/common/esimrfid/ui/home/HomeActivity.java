package com.common.esimrfid.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.allenliu.versionchecklib.core.http.AllenHttp;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.HomeConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.core.bean.update.UpdateVersion;
import com.common.esimrfid.presenter.home.HomePresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.NewSpeedataUhfServiceImpl;
import com.common.esimrfid.uhf.RodinbellUhfServiceImpl;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.ZebraUhfServiceImpl;
import com.common.esimrfid.ui.assetinventory.AssetInventoryActivity;
import com.common.esimrfid.ui.assetsearch.AssetsSearchActivity;
import com.common.esimrfid.ui.inventorytask.InventoryTaskActivity;
import com.common.esimrfid.ui.login.LoginActivity;
import com.common.esimrfid.ui.tagwrite.WriteTagActivity;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomeConstract.View {
    @BindView(R.id.tv_username)
    TextView mUserName;
    @BindView(R.id.all_number)
    TextView mAllAssets;
    @BindView(R.id.inuse_number)
    TextView mInuseAssets;
    @BindView(R.id.free_number)
    TextView mFreeAssets;
    @BindView(R.id.rv_assets_location)
    RecyclerView mLocationRecycle;
    @BindString(R.string.welcom)
    String welcom;
    private MaterialDialog updateDialog;
    ArrayList<AssetLocationNum> mAstLocaionNum = new ArrayList<>();
    int maxAssetNum = 0;
    private LocationAssetAdapter locationAssetAdapter;
    @BindString(R.string.def_update_content)
    String defUpdateContent;
    @Override
    public HomePresenter initPresenter() {
        return new HomePresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        checkUserSatus();
        initRfid();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        locationAssetAdapter = new LocationAssetAdapter(mAstLocaionNum, this, maxAssetNum);
        mLocationRecycle.setLayoutManager(new LinearLayoutManager(this));
        mLocationRecycle.setAdapter(locationAssetAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getAssetsNmbDiffLocation();
        mPresenter.getAssetsNmbDiffStatus();
    }

    //检查登录状态，未登录跳转登录界面
    private void checkUserSatus() {
        UserLoginResponse uerLogin = DataManager.getInstance().getUserLoginResponse();
        boolean loginStatus = DataManager.getInstance().getLoginStatus();
        if (loginStatus) {
            mPresenter.checkUpdateVersion();
            EsimAndroidApp.getInstance().setUserLoginResponse(uerLogin);
            mUserName.setText(welcom + uerLogin.getUserinfo().getUser_real_name());
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initToolbar() {

    }

    //初始化连接rfid
    private void initRfid() {
        EsimAndroidApp.getInstance().initRfid();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEPC(UhfMsgEvent uhfMsgEvent) {

        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.UHF_CONNECT:

                break;
            case UhfMsgType.UHF_DISCONNECT:

                break;
        }

    }

    @OnClick({R.id.inv_task, R.id.ast_inv, R.id.ast_search, R.id.write_tag, R.id.home_setting})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.inv_task:
                startActivity(new Intent(this, InventoryTaskActivity.class));
                break;
            case R.id.ast_inv:
                startActivity(new Intent(this, AssetInventoryActivity.class));
                break;
            case R.id.ast_search:
                startActivity(new Intent(this, AssetsSearchActivity.class));
                break;
            case R.id.write_tag:
                startActivity(new Intent(this, WriteTagActivity.class));
                break;
            case R.id.home_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EsimAndroidApp.getIEsimUhfService() != null) {
            EsimAndroidApp.getIEsimUhfService().closeRFID();
            EsimAndroidApp.setIEsimUhfService(null);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void handleAssetsNmbDiffLocation(HashMap<String, Integer> assetLocations) {
        Set<Map.Entry<String, Integer>> entries = assetLocations.entrySet();
        mAstLocaionNum.clear();
        for (Map.Entry<String, Integer> entry : entries) {
            AssetLocationNum assetLocationNum = new AssetLocationNum();
            assetLocationNum.setLocation(entry.getKey());
            assetLocationNum.setNumber(entry.getValue());
            if (entry.getValue() > maxAssetNum) {
                maxAssetNum = entry.getValue();
            }
            mAstLocaionNum.add(assetLocationNum);
        }
        locationAssetAdapter.setmMaxAssetNum(maxAssetNum);
        locationAssetAdapter.notifyDataSetChanged();

    }

    @Override
    public void handleAssetsNmbDiffStatus(AssetStatusNum assetStatus) {
        mAllAssets.setText(String.valueOf(assetStatus.getTotal()));
        mInuseAssets.setText(String.valueOf(assetStatus.getInused()));
        mFreeAssets.setText(String.valueOf(assetStatus.getInstore()));
    }

    @Override
    public void handelCheckoutVersion(UpdateVersion updateInfo) {
        if (getAppVersionCode(this) < updateInfo.getApp_version_code() || !getAppVersionName(this).equals(updateInfo.getApp_version())) {
            thirdUpdate(updateInfo);
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

    public static long getAppVersionCode(Context context) {
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

    public void dismissFinishDialog() {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
    }

    public void thirdUpdate(UpdateVersion updateInfo) {
        String UpdateContent = updateInfo.getApp_upgrade_message();
        if (StringUtils.isEmpty(UpdateContent)){
            UpdateContent = defUpdateContent;
        }
        DownloadBuilder builder = AllenVersionChecker
                .getInstance()
                .downloadOnly(UIData.create()
                        .setDownloadUrl(updateInfo.getApp_download_url())
                        .setTitle("版本更新")
                        .setContent(UpdateContent)
                );
        if(1 == updateInfo.getApp_must_upgrade() ){
            builder.setCustomVersionDialogListener(createCustomDialogOne());
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    finish();
                }
            });
        }else {
            builder.setCustomVersionDialogListener(createCustomDialogTwo());
        }
        builder.setShowNotification(false);
        builder.setForceRedownload(true);
        builder.setNotificationBuilder(
                NotificationBuilder.create()
                        .setRingtone(true)
                        .setIcon(R.drawable.update)
                        .setTicker("custom_ticker")
                        .setContentTitle("一芯资产管理")
                        .setContentText(getString(R.string.update_message))
        );
        builder.setDownloadAPKPath(Environment.getExternalStorageDirectory().toString() + "/Download/");
        builder.setCustomDownloadingDialogListener(new CustomDownloadingDialogListener() {
            @Override
            public Dialog getCustomDownloadingDialog(Context context, int progress, UIData versionBundle) {
                View loadingView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.downloading_layout, null);
                Dialog loadingDialog = new AlertDialog.Builder(HomeActivity.this).setTitle("").setView(loadingView).create();
                loadingDialog.setCancelable(true);
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        AllenHttp.getHttpClient().dispatcher().cancelAll();
                    }
                });
                return loadingDialog;
            }

            @Override
            public void updateUI(Dialog dialog, int progress, UIData versionBundle) {
                ProgressBar pb = (ProgressBar) dialog.findViewById(com.allenliu.versionchecklib.R.id.pb);
                TextView tvProgress = (TextView) dialog.findViewById(com.allenliu.versionchecklib.R.id.tv_progress);
                tvProgress.setText(String.format(getString(com.allenliu.versionchecklib.R.string.versionchecklib_progress), progress));
                pb.setProgress(progress);
            }
        });
        builder.executeMission(this);
    }

    private CustomVersionDialogListener createCustomDialogOne() {
        return new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.must_update_dialog);
                TextView textView = baseDialog.findViewById(R.id.update_content);
                textView.setText(versionBundle.getContent());
                baseDialog.setCanceledOnTouchOutside(false);
                baseDialog.setCancelable(false);
                return baseDialog;
            }
        };
    }

    private CustomVersionDialogListener createCustomDialogTwo() {
        return (context, versionBundle) -> {
            BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.update_version_dialog);
            TextView textView = baseDialog.findViewById(R.id.update_content);
            textView.setText(versionBundle.getContent());
            baseDialog.setCanceledOnTouchOutside(false);
            baseDialog.setCancelable(false);
            return baseDialog;
        };
    }

}
