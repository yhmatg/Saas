package com.common.xfxj.xfxj;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.common.xfxj.R;
import com.common.xfxj.app.EsimAndroidApp;
import com.common.xfxj.base.activity.BaseActivity;
import com.common.xfxj.contract.home.HomeConstract;
import com.common.xfxj.core.DataManager;
import com.common.xfxj.core.bean.nanhua.home.AssetLocNmu;
import com.common.xfxj.core.bean.nanhua.home.AssetStatusNum;
import com.common.xfxj.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.xfxj.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.xfxj.core.bean.nanhua.xfxj.XfResultInventoryOrder;
import com.common.xfxj.core.bean.update.UpdateVersion;
import com.common.xfxj.core.room.DbBank;
import com.common.xfxj.presenter.home.HomePresenter;
import com.common.xfxj.uhf.UhfMsgEvent;
import com.common.xfxj.uhf.UhfMsgType;
import com.common.xfxj.ui.home.AssetLocationNum;
import com.common.xfxj.ui.home.SettingActivity;
import com.common.xfxj.utils.SettingBeepUtil;
import com.common.xfxj.utils.ToastUtils;
import com.common.xfxj.xfxj.assetinventory.XfAssetInventoryActivity;
import com.common.xfxj.xfxj.identity.XfIdentityActivity;
import com.common.xfxj.xfxj.repair.XfAssetRepairActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class XfHomeActivity extends BaseActivity<HomePresenter> implements HomeConstract.View {
    @BindView(R.id.tv_username)
    TextView mUserName;
    @BindView(R.id.all_number)
    TextView mAllAssets;
    @BindView(R.id.inuse_number)
    TextView mInuseAssets;
    @BindView(R.id.free_number)
    TextView mFreeAssets;
    @BindString(R.string.welcom)
    String welcom;
    private MaterialDialog updateDialog;
    ArrayList<AssetLocationNum> mAstLocaionNum = new ArrayList<>();
    int maxAssetNum = 0;
    @BindString(R.string.def_update_content)
    String defUpdateContent;
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
        initRfid();
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
        //初始化数据库数据
        initDatabaseData();
    }

    private void initDatabaseData() {
        //初始化盘点单数据
        ArrayList<XfResultInventoryOrder> xfResultInventoryOrders = new ArrayList<>();
        XfResultInventoryOrder xfResultInventoryOrderOne = new XfResultInventoryOrder("invId001", "invCode001", "巡检任务1", "巡检人1", "9月10日", "15:00", 0, 3, 0);
        XfResultInventoryOrder xfResultInventoryOrderTwo = new XfResultInventoryOrder("invId002", "invCode002", "巡检任务2", "巡检人2", "9月11日", "15:00", 0, 3, 1);
        XfResultInventoryOrder xfResultInventoryOrderThree = new XfResultInventoryOrder("invId003", "invCode003", "巡检任务3", "巡检人3", "9月11日", "15:00", 0, 3, 2);
        xfResultInventoryOrders.add(xfResultInventoryOrderOne);
        xfResultInventoryOrders.add(xfResultInventoryOrderTwo);
        xfResultInventoryOrders.add(xfResultInventoryOrderThree);
        DbBank.getInstance().getXfResultInventoryOrderDao().deleteAllData();
        DbBank.getInstance().getXfResultInventoryOrderDao().insertItems(xfResultInventoryOrders);
        //初始化盘点单详情
        ArrayList<XfInventoryDetail> xfInventoryDetails = new ArrayList<>();
        XfInventoryDetail xfInventoryDetailOne = new XfInventoryDetail("detail001", "invId001", "ast001","detail001","detail001", "品牌1", "100000000000000000000001", "一楼", "公司1", "张三","9月12日","正常","w0001","张三","9月10日","说明1",0,0, 0, 0, 0, 0, 0,"");
        XfInventoryDetail xfInventoryDetailTwo = new XfInventoryDetail("detail002", "invId001", "ast002","detail002","detail002", "品牌1", "100000000000000000000002", "二楼", "公司1", "张三","9月12日","正常","w0001","张三","9月10日","说明1",0, 0, 0, 0, 0, 0,0,"");
        XfInventoryDetail xfInventoryDetailThree = new XfInventoryDetail("detail003", "invId001", "ast003","detail003","detail003", "品牌1", "100000000000000000000003", "三楼", "公司1", "张三","9月12日","正常","w0001","张三","9月10日","说明1",0, 0, 0, 0, 0, 0,0,"");
        XfInventoryDetail xfInventoryDetailFour = new XfInventoryDetail("detail004", "invId002", "ast004","detail004","detail004", "品牌1", "100000000000000000000004", "三楼", "公司1", "张三","9月12日","正常","w0001","张三","9月10日","说明1",0, 0, 0, 0, 0, 0,0,"");
        XfInventoryDetail xfInventoryDetailFive = new XfInventoryDetail("detail005", "invId002", "ast005","detail005","detail005", "品牌1", "100000000000000000000005", "三楼", "公司1", "张三","9月12日","正常","w0001","张三","9月10日","说明1",0, 0, 0, 0, 0, 0,0,"");
        XfInventoryDetail xfInventoryDetailSix = new XfInventoryDetail("detail006", "invId002", "ast006","detail006","detail006", "品牌1", "100000000000000000000006", "三楼", "公司1", "张三","9月12日","正常","w0001","张三","9月10日","说明1",0, 0, 0, 0, 0, 0,0,"");
        XfInventoryDetail xfInventoryDetailSeven = new XfInventoryDetail("detail007", "invId003", "ast007","detail007","detail007", "品牌1", "100000000000000000000007", "三楼", "公司1", "张三","9月12日","正常","w0001","张三","9月10日","说明1",0, 0, 0, 0, 0, 0,0,"");
        XfInventoryDetail xfInventoryDetailEight = new XfInventoryDetail("detail008", "invId003", "ast008","detail008","detail008", "品牌1", "100000000000000000000008", "三楼", "公司1", "张三","9月12日","正常","w0001","张三","9月10日","说明1",0, 0, 0, 0, 0, 0,0,"");
        XfInventoryDetail xfInventoryDetailNine = new XfInventoryDetail("detail009", "invId003", "ast009","detail009","detail009", "品牌1", "100000000000000000000009", "三楼", "公司1", "张三","9月12日","正常","w0001","张三","9月10日","说明1",0, 0, 0, 0, 0, 0,0,"");
        xfInventoryDetails.add(xfInventoryDetailOne);
        xfInventoryDetails.add(xfInventoryDetailTwo);
        xfInventoryDetails.add(xfInventoryDetailThree);
        xfInventoryDetails.add(xfInventoryDetailFour);
        xfInventoryDetails.add(xfInventoryDetailFive);
        xfInventoryDetails.add(xfInventoryDetailSix);
        xfInventoryDetails.add(xfInventoryDetailSeven);
        xfInventoryDetails.add(xfInventoryDetailEight);
        xfInventoryDetails.add(xfInventoryDetailNine);
        DbBank.getInstance().getXfInventoryDetailDao().deleteAllData();
        DbBank.getInstance().getXfInventoryDetailDao().insertItems(xfInventoryDetails);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.xf_activity_home;
    }

    @Override
    protected void initToolbar() {

    }

    //初始化连接rfid
    private void initRfid() {
        if (!"ESUR-H600".equals(Build.MODEL)) {
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

    @OnClick({R.id.inv_task, R.id.ast_inv, R.id.ast_search, R.id.write_tag, R.id.ast_identity, R.id.ast_repair, R.id.ast_list, R.id.ll_need_task, R.id.ll_overdue, R.id.ll_finish})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.inv_task:

                break;
            case R.id.ast_inv:

                break;
            case R.id.ast_search:

                break;
            case R.id.write_tag:

                break;
            case R.id.ast_identity:
                if (isNormalClick()) {
                    startActivity(new Intent(this, XfAssetRepairActivity.class));
                }
                break;
            case R.id.ast_repair:
                if (isNormalClick()) {
                    startActivity(new Intent(this, XfIdentityActivity.class));
                }
                break;
            case R.id.ast_list:
                if (isNormalClick()) {
                    startActivity(new Intent(this, SettingActivity.class));
                }
                break;
            case R.id.ll_need_task:
                if (isNormalClick()) {
                    startActivity(new Intent(this, XfAssetInventoryActivity.class));
                }
                break;
            case R.id.ll_overdue:
                if (isNormalClick()) {
                    startActivity(new Intent(this, XfAssetInventoryActivity.class));
                }
                break;
            case R.id.ll_finish:
                if (isNormalClick()) {
                    startActivity(new Intent(this, XfAssetInventoryActivity.class));
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
    }

    @Override
    public void handleAssetsNmbDiffStatus(AssetStatusNum assetStatus) {
    }

    @Override
    public void handelCheckoutVersion(UpdateVersion updateInfo) {

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

    public void dismissFinishDialog() {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
    }

    public boolean isNormalClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
