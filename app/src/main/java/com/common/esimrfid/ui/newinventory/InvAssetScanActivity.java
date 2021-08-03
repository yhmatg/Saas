package com.common.esimrfid.ui.newinventory;

import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvAssetLocContract;
import com.common.esimrfid.core.bean.inventorytask.EpcBean;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.presenter.home.InvAssetsLocPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.ui.home.BaseDialog;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InvAssetScanActivity extends BaseActivity<InvAssetsLocPresenter> implements InvAssetLocContract.View {

    public static final String INV_ID = "inv_id";
    public static final String LOC_IC = "loc_id";
    public static final String LOC_Name = "loc_name";
    @BindView(R.id.iv_radar)
    ImageView mRadarView;
    @BindView(R.id.tv_in_scan)
    TextView mInScan;
    @BindView(R.id.bt_start_scan)
    Button mScanButton;
    @BindView(R.id.ast_all_num)
    TextView mAllNum;
    @BindView(R.id.ast_in_num)
    TextView mInNum;
    @BindView(R.id.ast_out_num)
    TextView mOutNum;
    @BindView(R.id.tv_loc_name)
    TextView mAreaName;
    IEsimUhfService esimUhfService = null;
    private Animation mRadarAnim;
    private String mInvId;
    private String mLocId;
    private String mLocName;
    private Boolean canRfid = true;
    //这个区域下所有的资产
    List<InventoryDetail> mInventoryDetails = new ArrayList<>();
    //已盘和盘亏的资产
    List<InventoryDetail> mInvedDetails = new ArrayList<>();
    //盘盈
    List<InventoryDetail> mMoreDetails = new ArrayList<>();
    //epc和资产盘点条目
    HashMap<String, InventoryDetail> epcInvBean = new HashMap<>();
    //所有盘盈的epc
    List<String> allMoreEpcs = new ArrayList<>();
    //盘点单中每次盘点到的资产条目
    List<InventoryDetail> oneInvDetails = new ArrayList<>();
    //每次盘盈的epc
    HashSet<String> oneMoreInvEpcs = new HashSet<>();
    private BaseDialog baseDialog;
    private ArrayList<EpcBean> allEpcBeans = new ArrayList<>();
    private String userId;
    @Override
    public InvAssetsLocPresenter initPresenter() {
        return new InvAssetsLocPresenter();
    }

    @Override
    protected void initEventAndData() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initRfidAndEventbus();
        if (getIntent() != null) {
            mInvId = getIntent().getStringExtra(INV_ID);
            mLocId = getIntent().getStringExtra(LOC_IC);
            mLocName = getIntent().getStringExtra(LOC_Name);
        }
        userId = getUserLoginResponse().getUserinfo().getId();
        mAreaName.setText(mLocName);
        mPresenter.fetchAllInvDetails(mInvId, mLocId);
        mPresenter.getAllAssetEpcs();
        initAnim();
    }

    private void initRfidAndEventbus() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inv_scan_new;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
            esimUhfService.setEnable(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null && esimUhfService.isStart()) {
            esimUhfService.stopScanning();
        }
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
            esimUhfService.setEnable(false);
        }
    }

    @OnClick({R.id.title_back, R.id.bt_start_scan})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.bt_start_scan:
                if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                    esimUhfService.startStopScanning();
                } else {
                    ToastUtils.showShort(R.string.not_connect_prompt);
                }
                break;

        }
    }

    @Override
    public void handleInvDetails(List<InventoryDetail> inventoryDetails) {
        mInvedDetails.clear();
        mMoreDetails.clear();
        epcInvBean.clear();
        mInventoryDetails.clear();
        allMoreEpcs.clear();
        for (InventoryDetail inventoryDetail : inventoryDetails) {
            if (inventoryDetail.getInvdt_status().getCode() == 1 || inventoryDetail.getInvdt_status().getCode() == 10) {
                mInvedDetails.add(inventoryDetail);
                mInventoryDetails.add(inventoryDetail);
            } else if (inventoryDetail.getInvdt_status().getCode() == 2) {
                mMoreDetails.add(inventoryDetail);
                allMoreEpcs.add(inventoryDetail.getAst_epc_code());
            } else if (inventoryDetail.getInvdt_status().getCode() == 0) {
                mInventoryDetails.add(inventoryDetail);
            }
            epcInvBean.put(inventoryDetail.getAst_epc_code(), inventoryDetail);
        }
        mAllNum.setText(String.valueOf(mInventoryDetails.size()));
        mInNum.setText(String.valueOf(mInvedDetails.size()));
        mOutNum.setText(String.valueOf(mMoreDetails.size()));
    }

    @Override
    public void handleAllAssetEpcs(List<EpcBean> allEpcs) {
        allEpcBeans.clear();
        allEpcBeans.addAll(allEpcs);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        String epc = null;
        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag = (UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getEpc();
                handleEpc(epc);
                break;
            case UhfMsgType.UHF_CONNECT:
                break;
            case UhfMsgType.UHF_DISCONNECT:
                mScanButton.setText(R.string.start_inv);
                mInScan.setVisibility(View.GONE);
                stopAnim();
                break;
            case UhfMsgType.UHF_START:
                mScanButton.setText(R.string.stop_inv);
                mInScan.setVisibility(View.VISIBLE);
                oneInvDetails.clear();
                oneMoreInvEpcs.clear();
                startAnim();
                break;
            case UhfMsgType.UHF_STOP:
                mScanButton.setText(R.string.start_inv);
                mInScan.setVisibility(View.GONE);
                mPresenter.handleOneScanned(oneInvDetails, oneMoreInvEpcs, mLocId, mLocName, mInvId,userId);
                stopAnim();
                break;
        }
    }

    //处理盘点到的数据
    private void handleEpc(String epc) {
        EpcBean epcBean = new EpcBean(epc);
        InventoryDetail inventoryDetail = epcInvBean.get(epc);
        if (inventoryDetail != null && inventoryDetail.getInvdt_status().getCode() == 0) {
            if (!mInvedDetails.contains(inventoryDetail)) {
                inventoryDetail.getInvdt_status().setCode(10);
                inventoryDetail.setNeedUpload(true);
                mInvedDetails.add(inventoryDetail);
                oneInvDetails.add(inventoryDetail);
            }
        } else if (inventoryDetail == null && allEpcBeans.contains(epcBean) && !allMoreEpcs.contains(epc)) {
            allMoreEpcs.add(epc);
            oneMoreInvEpcs.add(epc);
        }
        mInNum.setText(String.valueOf(mInvedDetails.size()));
        mOutNum.setText(String.valueOf(allMoreEpcs.size()));
        if (mInvedDetails.size() == mInventoryDetails.size()) {
            showConfirmDialog();
            if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null && esimUhfService.isStart()) {
                esimUhfService.stopScanning();
            }
        }
    }

    public void startAnim() {
        mRadarView.startAnimation(mRadarAnim);
    }

    public void stopAnim() {
        mRadarView.clearAnimation();
    }

    private void initAnim() {
        mRadarAnim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRadarAnim.setFillAfter(true); // 设置保持动画最后的状态
        mRadarAnim.setDuration(2000); // 设置动画时间
        mRadarAnim.setRepeatCount(Animation.INFINITE);//设置动画重复次数 无限循环
        mRadarAnim.setInterpolator(new LinearInterpolator());
        mRadarAnim.setRepeatMode(Animation.RESTART);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (canRfid) {
            if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                if (keyCode == esimUhfService.getDownKey()) { //扳机建扫描
                    esimUhfService.startStopScanning();
                }
            } else if (keyCode == Utils.getDiffDownKey()) {
                ToastUtils.showShort(R.string.not_connect_prompt);
            }
            canRfid = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        canRfid = true;
        return super.onKeyUp(keyCode, event);
    }

    public void showConfirmDialog() {
        if (baseDialog == null) {
            baseDialog = new BaseDialog(this, R.style.BaseDialog, R.layout.finish_confirm_dialog);
            TextView context = baseDialog.findViewById(R.id.alert_context);
            Button btSure = baseDialog.findViewById(R.id.bt_confirm);
            context.setText(R.string.scan_all_confirm);
            btSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseDialog.dismiss();
                }
            });
            if (!baseDialog.isShowing()) {
                baseDialog.show();
            }
        } else {
            if (!baseDialog.isShowing()) {
                baseDialog.show();
            }
        }


    }
}
