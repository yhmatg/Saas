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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.common.esimrfid.core.bean.nanhua.home.AssetLocNmu;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DataAuthority;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.core.bean.update.UpdateVersion;
import com.common.esimrfid.presenter.home.HomePresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.NewSpeedataUhfServiceImpl;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.ZebraUhfServiceImpl;
import com.common.esimrfid.ui.assetinventory.AssetInventoryActivity;
import com.common.esimrfid.ui.assetrepair.AssetRepairActivity;
import com.common.esimrfid.ui.assetsearch.AssetsSearchActivity;
import com.common.esimrfid.ui.astlist.AssetListActivity;
import com.common.esimrfid.ui.batchedit.BatchEditActivity;
import com.common.esimrfid.ui.identity.IdentityActivity;
import com.common.esimrfid.ui.inventorytask.InventoryTaskActivity;
import com.common.esimrfid.ui.login.LoginActivity;
import com.common.esimrfid.ui.tagwrite.WriteTagActivity;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.SettingBeepUtil;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    @BindView(R.id.company_name)
    TextView mCompanyName;
    @BindView(R.id.batch_edit)
    TextView batchEdit;
    @BindView(R.id.rv_assets_location)
    RecyclerView mLocationRecycle;
    @BindString(R.string.welcom)
    String welcom;
    @BindView(R.id.loction_layout)
    LinearLayout loctionLayout;
    private MaterialDialog updateDialog;
    ArrayList<AssetLocationNum> mAstLocaionNum = new ArrayList<>();
    int maxAssetNum = 0;
    private LocationAssetAdapter locationAssetAdapter;
    @BindString(R.string.def_update_content)
    String defUpdateContent;
    IEsimUhfService esimUhfService = null;
    private boolean isCommonClose = false;
    private boolean isFirstInstall;
    private final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;
    private UserLoginResponse uerLogin;

    @Override
    public HomePresenter initPresenter() {
        return new HomePresenter();
    }

    @Override
    protected void initEventAndData() {
        String projectName = getString(R.string.projectName);
        if ("zsbank".equals(projectName)) {
            batchEdit.setVisibility(View.VISIBLE);
        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            isFirstInstall = true;
            finish();
            return;
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        SettingBeepUtil.setOpen(DataManager.getInstance().getOpenBeeper());
        SettingBeepUtil.setSledOpen(DataManager.getInstance().getSledBeeper());
        SettingBeepUtil.setHostOpen(DataManager.getInstance().getHostBeeper());
        checkUserSatus();
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        //兼容不同固件模块的设备
        String locFirmVersion = DataManager.getInstance().getFirmwareVersion();
        if (esimUhfService instanceof NewSpeedataUhfServiceImpl && StringUtils.isEmpty(locFirmVersion)) {
            String firmwareVersion = ((NewSpeedataUhfServiceImpl) esimUhfService).getFirmwareVersion();
            if(!StringUtils.isEmpty(firmwareVersion)){
                if ("1.4.24".equals(firmwareVersion)) {
                    boolean setResult = ((NewSpeedataUhfServiceImpl) esimUhfService).setWorkAndWaitTime(0, 0, true);
                    if(setResult){
                        DataManager.getInstance().setFirmwareVersion("1.4.24");
                    }
                } else {
                    boolean setResult = ((NewSpeedataUhfServiceImpl) esimUhfService).setWorkAndWaitTime(200, 200, true);
                    if(setResult){
                        DataManager.getInstance().setFirmwareVersion("1.3.5");
                    }
                }
            }
        }
        locationAssetAdapter = new LocationAssetAdapter(mAstLocaionNum, this, maxAssetNum);
        mLocationRecycle.setLayoutManager(new LinearLayoutManager(this));
        mLocationRecycle.setAdapter(locationAssetAdapter);
        if (Build.MODEL.contains("TC20") || Build.MODEL.contains("MC33")) {
            loctionLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getAssetsNmbDiffLocation();
        mPresenter.getAssetsNmbDiffStatus();
        if (uerLogin.getUserinfo().isSuperManagerUser()) {
            DataAuthority dataAuthority = new DataAuthority();
            dataAuthority.getAuth_corp_scope().add("allData");
            dataAuthority.getAuth_dept_scope().add("allData");
            dataAuthority.getAuth_type_scope().add("allData");
            dataAuthority.getAuth_loc_scope().add("allData");
            DataManager.getInstance().setDataAuthority(dataAuthority);
            EsimAndroidApp.setDataAuthority(dataAuthority);
        } else {
            mPresenter.getDataAuthority(uerLogin.getUserinfo().getId());
        }
        mPresenter.fetchLatestPageAssets(500, 1);
    }

    //检查登录状态，未登录跳转登录界面
    private void checkUserSatus() {
        uerLogin = DataManager.getInstance().getUserLoginResponse();
        boolean loginStatus = DataManager.getInstance().getLoginStatus();
        if (loginStatus) {
            initRfid();
            mPresenter.checkUpdateVersion();
            EsimAndroidApp.getInstance().setUserLoginResponse(uerLogin);
            if (uerLogin.getUserinfo().getUser_real_name() != null) {
                mUserName.setText(welcom + uerLogin.getUserinfo().getUser_real_name());
            }
            if (uerLogin.getUserinfo().getCorpInfo() != null && uerLogin.getUserinfo().getCorpInfo().getOrg_name() != null) {
                //mCompanyName.setText(uerLogin.getUserinfo().getCorpInfo().getOrg_name());
            }
            mPresenter.fetchAllIvnOrders(uerLogin.getUserinfo().getId(), true);
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
        if (esimUhfService instanceof ZebraUhfServiceImpl) {
            showConnectDialog();
        }
        EsimAndroidApp.getInstance().initRfid();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEPC(UhfMsgEvent uhfMsgEvent) {

        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.UHF_CONNECT:
                break;
            case UhfMsgType.UHF_DISCONNECT:
                if (!isCommonClose) {
                    ToastUtils.showShort(R.string.not_connect_prompt);
                }
                break;
            case UhfMsgType.UHF_DISMISS_DIALOG:
                dismissFinishDialog();
                break;
            case UhfMsgType.UHF_CONNECT_FAIL:
                ToastUtils.showShort(R.string.rfid_connect_fail);
                break;
            case UhfMsgType.SETTING_SOUND_FAIL:
                ToastUtils.showShort(R.string.sound_setting_fail);
                break;
            case UhfMsgType.SETTING_POWER_SUCCESS:
                ToastUtils.showShort(R.string.save_newinv_succ);
                break;
            case UhfMsgType.SETTING_POWER_FAIL:
                ToastUtils.showShort(R.string.save_newinv_fail);
                break;
        }

    }

    @OnClick({R.id.inv_task, R.id.ast_inv, R.id.ast_search, R.id.write_tag, R.id.home_setting, R.id.ast_identity,
            R.id.ast_repair, R.id.ast_list, R.id.batch_edit})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.inv_task:
                if (CommonUtils.isNormalClick()) {
                    startActivity(new Intent(this, InventoryTaskActivity.class));
                }
                break;
            case R.id.ast_inv:
                if (CommonUtils.isNormalClick()) {
                    startActivity(new Intent(this, AssetInventoryActivity.class));
                }
                break;
            case R.id.ast_search:
                if (CommonUtils.isNormalClick()) {
                    startActivity(new Intent(this, AssetsSearchActivity.class));
                }
                break;
            case R.id.write_tag:
                if (CommonUtils.isNormalClick()) {
                    startActivity(new Intent(this, WriteTagActivity.class));
                }

                break;
            case R.id.home_setting:
                if (CommonUtils.isNormalClick()) {
                    startActivity(new Intent(this, SettingActivity.class));
                }
                break;
            case R.id.ast_identity:
                if (CommonUtils.isNormalClick()) {
                    startActivity(new Intent(this, IdentityActivity.class));
                }
                break;
            case R.id.ast_repair:
                if (CommonUtils.isNormalClick()) {
                    startActivity(new Intent(this, AssetRepairActivity.class));
                }
                break;
            case R.id.ast_list:
                if (CommonUtils.isNormalClick()) {
                    startActivity(new Intent(this, AssetListActivity.class));
                }
                break;
            case R.id.batch_edit:
                if (CommonUtils.isNormalClick()) {
                    startActivity(new Intent(this, BatchEditActivity.class));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFirstInstall) {
            isFirstInstall = false;
            return;
        }
        dismissFinishDialog();
        if (EsimAndroidApp.getIEsimUhfService() != null) {
            isCommonClose = true;
            EsimAndroidApp.getIEsimUhfService().closeRFID();
            EsimAndroidApp.setIEsimUhfService(null);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void handleAssetsNmbDiffLocation(List<AssetLocNmu> assetLocations) {
        mAstLocaionNum.clear();
        for (AssetLocNmu assetLocation : assetLocations) {
            AssetLocationNum assetLocationNum = new AssetLocationNum();
            assetLocationNum.setLocation(assetLocation.getField_name());
            assetLocationNum.setNumber(assetLocation.getField_count());
            if (assetLocation.getField_count() > maxAssetNum) {
                maxAssetNum = assetLocation.getField_count();
            }
            mAstLocaionNum.add(assetLocationNum);
        }
        Collections.sort(mAstLocaionNum, new Comparator<AssetLocationNum>() {
            @Override
            public int compare(AssetLocationNum o1, AssetLocationNum o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });
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
            if (getAppVersionCode(this) == 5 && updateInfo.getApp_version_code() == 6) {
                DataManager.getInstance().setLatestSyncTime("0");
            }
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
        if (StringUtils.isEmpty(UpdateContent)) {
            UpdateContent = defUpdateContent;
        }
        DownloadBuilder builder = AllenVersionChecker
                .getInstance()
                .downloadOnly(UIData.create()
                        .setDownloadUrl(updateInfo.getApp_download_url())
                        .setTitle(updateInfo.getApp_version())
                        .setContent(UpdateContent)
                );
        if (1 == updateInfo.getApp_must_upgrade()) {
            builder.setCustomVersionDialogListener(createCustomDialogOne());
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    finish();
                }
            });
        } else {
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
        builder.setDownloadAPKPath(Environment.getExternalStorageDirectory().toString() + "/Download/Esim/");
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
                ProgressBar pb = (ProgressBar) dialog.findViewById(R.id.pb);
                TextView tvProgress = (TextView) dialog.findViewById(R.id.tv_progress);
                tvProgress.setText(String.format(getString(R.string.versionchecklib_progress), progress));
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
                TextView version = baseDialog.findViewById(R.id.version_num);
                String str = versionBundle.getContent();
                String[] strArry = str.split("[；]");
                String content = "";
                for (int i = 0; i < strArry.length; i++) {
                    content = content + strArry[i];
                    if (i != strArry.length - 1) {
                        content = content + "\n";
                    }
                }
                textView.setText(content);
                String remoteVersion = versionBundle.getTitle();
                if (!StringUtils.isEmpty(remoteVersion)) {
                    version.setText("v" + remoteVersion);
                }
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
            TextView version = baseDialog.findViewById(R.id.version_num);
            String str = versionBundle.getContent();
            String[] strArry = str.split("[；]");
            String content = "";
            for (int i = 0; i < strArry.length; i++) {
                content = content + strArry[i];
                if (i != strArry.length - 1) {
                    content = content + "\n";
                }
            }
            textView.setText(content);
            String remoteVersion = versionBundle.getTitle();
            if (!StringUtils.isEmpty(remoteVersion)) {
                version.setText("v" + remoteVersion);
            }
            baseDialog.setCanceledOnTouchOutside(false);
            baseDialog.setCancelable(false);
            return baseDialog;
        };
    }

    public void showConnectDialog() {
        if (updateDialog != null) {
            updateDialog.show();
        } else {
            View contentView = LayoutInflater.from(this).inflate(R.layout.connect_loading_dialog, null);
            updateDialog = new MaterialDialog.Builder(this)
                    .customView(contentView, false)
                    .canceledOnTouchOutside(false)
                    .cancelable(false)
                    .show();
            Window window = updateDialog.getWindow();
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}
