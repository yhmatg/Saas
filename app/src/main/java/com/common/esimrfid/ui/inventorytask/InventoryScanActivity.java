package com.common.esimrfid.ui.inventorytask;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.InventoryStatus;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.presenter.home.InvDetailPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.ui.home.AssetLocationNum;
import com.common.esimrfid.ui.home.BaseDialog;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class InventoryScanActivity extends BaseActivity<InvDetailPresenter> implements InvDetailContract.View {

    public static final String INV_ID = "inv_id";
    @BindView(R.id.iv_big_round)
    ImageView mBigRound;
    @BindView(R.id.iv_mid_round)
    ImageView mMidRound;
    @BindView(R.id.iv_radar)
    ImageView mRadarView;
    @BindView(R.id.bt_start_scan)
    Button mScanButton;
    @BindView(R.id.rv_assets_location)
    RecyclerView mLocationRecycleview;
    ScanAssetsAdapter mAdapter;
    //位置资产数量信息
    ArrayList<AssetLocationNum> assetLocations = new ArrayList<>();
    //没有盘点过的数据
    private ArrayList<String> notScannedEpcList = new ArrayList<>();
    //盘点资产
    ArrayList<InventoryDetail> assetList = new ArrayList<>();
    //地点和对应的资产数目
    HashMap<String, ArrayList<InventoryDetail>> locationMap = new HashMap<>();
    //所有扫描盘点到的数据
    private List<InventoryDetail> mUpdateInvDataList = new ArrayList<>();
    //最近一次扫描盘点到的数据
    private List<InventoryDetail> mResentUpdateInvDataList = new ArrayList<>();
    private String mInvId;
    IEsimUhfService esimUhfService = null;
    private Animation mRadarAnim, mMidAnim, mBigAnim;
    private Boolean canRfid = true;
    @Override
    public InvDetailPresenter initPresenter() {
        return new InvDetailPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        initRfidAndEventbus();
        if (getIntent() != null) {
            mInvId = getIntent().getStringExtra(INV_ID);
        }
        mAdapter = new ScanAssetsAdapter(assetLocations, this);
        mLocationRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mLocationRecycleview.setAdapter(mAdapter);
        mPresenter.fetchAllInvDetails(mInvId, false);
        initAnim();
    }

    private void initAnim() {
        mRadarAnim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRadarAnim.setFillAfter(true); // 设置保持动画最后的状态
        mRadarAnim.setDuration(2000); // 设置动画时间
        mRadarAnim.setRepeatCount(Animation.INFINITE);//设置动画重复次数 无限循环
        mRadarAnim.setInterpolator(new LinearInterpolator());
        mRadarAnim.setRepeatMode(Animation.RESTART);

        mMidAnim = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mMidAnim.setFillAfter(true); // 设置保持动画最后的状态
        mMidAnim.setDuration(2000); // 设置动画时间
        mMidAnim.setRepeatCount(Animation.INFINITE);//设置动画重复次数 无限循环
        mMidAnim.setInterpolator(new LinearInterpolator());
        mMidAnim.setRepeatMode(Animation.RESTART);

        mBigAnim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mBigAnim.setFillAfter(true); // 设置保持动画最后的状态
        mBigAnim.setDuration(500); // 设置动画时间
        mBigAnim.setRepeatCount(Animation.INFINITE);//设置动画重复次数 无限循环
        mBigAnim.setInterpolator(new LinearInterpolator());
        mBigAnim.setRepeatMode(Animation.RESTART);
    }

    public void startAnim() {
        mBigRound.startAnimation(mBigAnim);
        mMidRound.startAnimation(mMidAnim);
        mRadarView.startAnimation(mRadarAnim);
    }

    public void stopAnim() {
        mBigRound.clearAnimation();
        mMidRound.clearAnimation();
        mRadarView.clearAnimation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inv_scan;
    }

    @Override
    protected void initToolbar() {
    }

    @OnClick({R.id.bt_start_scan, R.id.title_back})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start_scan:
                if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                    esimUhfService.startStopScanning();
                } else {
                    ToastUtils.showShort(R.string.not_connect_prompt);
                }
                break;
            case R.id.title_back:
                finish();
                break;
        }
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
                stopAnim();
                break;
            case UhfMsgType.UHF_START:
                mScanButton.setText(R.string.stop_inv);
                startAnim();
                mResentUpdateInvDataList.clear();
                break;
            case UhfMsgType.UHF_STOP:
                mScanButton.setText(R.string.start_inv);
                stopAnim();
                //跟新盘点状态到数据库
                //盘点到新数据才更新到数据库
                if (mResentUpdateInvDataList.size() > 0) {
                    mAdapter.notifyDataSetChanged();
                    mPresenter.updateLocalInvDetailsState(mInvId, mResentUpdateInvDataList);
                }
                break;
        }
    }

    //处理盘点到的数据
    private void handleEpc(String epc) {
        if (epc != null && notScannedEpcList.contains(epc)) {
            for (int i = 0; i < assetList.size(); i++) {
                InventoryDetail inventoryDetail = assetList.get(i);
                if (epc.equals(inventoryDetail.getAssetsInfos().getAst_epc_code())) {
                    notScannedEpcList.remove(epc);
                    inventoryDetail.getInvdt_status().setCode(InventoryStatus.FINISH_NOT_SUBMIT.getIndex());
                    mResentUpdateInvDataList.add(inventoryDetail);
                    String loc_name = inventoryDetail.getAssetsInfos().getLoc_info().getLoc_name();
                    for (AssetLocationNum assetLocation : assetLocations) {
                        if (loc_name.equals(assetLocation.getLocation())) {
                            int progress = assetLocation.getProgress() + 1;
                            assetLocation.setProgress(progress);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    if(notScannedEpcList.size() == 0){
                        showConfirmDialog();
                        if(esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null && esimUhfService.isStart()){
                            esimUhfService.stopScanning();
                        }
                    }
                    break;
                }
            }
        }
    }

    //处理盘点单中的具体资产
    @Override
    public void handleInvDetails(ResultInventoryDetail mInventoryDetail) {
        assetList.clear();
        assetList.addAll(mInventoryDetail.getDetailResults());
        for (InventoryDetail inventoryDetail : assetList) {

            //String locName = inventoryDetail.getAssetsInfos().getLoc_info().getLoc_name();
            String locName = inventoryDetail.getAssetsInfos().getLoc_info() == null ? "未分配" : inventoryDetail.getAssetsInfos().getLoc_info().getLoc_name();
            //没有盘点过的数据
            if (inventoryDetail.getInvdt_status().getCode() == InventoryStatus.INIT.getIndex()) {
                notScannedEpcList.add(inventoryDetail.getAssetsInfos().getAst_epc_code());
            }
            //资产按地点分类
            if (!locationMap.containsKey(locName)) {
                ArrayList<InventoryDetail> details = new ArrayList<>();
                details.add(inventoryDetail);
                locationMap.put(locName, details);
            } else {
                ArrayList<InventoryDetail> inventoryDetails = locationMap.get(locName);
                inventoryDetails.add(inventoryDetail);
            }
        }

        Set<Map.Entry<String, ArrayList<InventoryDetail>>> entries = locationMap.entrySet();
        for (Map.Entry<String, ArrayList<InventoryDetail>> entry : entries) {
            AssetLocationNum assetLocationNum = new AssetLocationNum();
            assetLocationNum.setLocation(entry.getKey());
            ArrayList<InventoryDetail> invdetails = entry.getValue();
            assetLocationNum.setNumber(invdetails.size());
            int notInvedNum = 0;
            for (InventoryDetail invdetail : invdetails) {
                if (invdetail.getInvdt_status().getCode() == InventoryStatus.INIT.getIndex()) {
                    notInvedNum++;
                }
            }
            assetLocationNum.setProgress(invdetails.size() - notInvedNum);
            assetLocations.add(assetLocationNum);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void handelUploadResult(BaseResponse baseResponse) {

    }

    @Override
    public void handelFinishInvorder(BaseResponse baseResponse) {

    }

    @Override
    public void uploadInvDetails(List<InventoryDetail> inventoryDetails) {

    }

    private void initRfidAndEventbus() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null ){
            esimUhfService.setEnable(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null && esimUhfService.isStart()){
            esimUhfService.stopScanning();
        }
        if(esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null ){
            esimUhfService.setEnable(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(canRfid){
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

    public void showConfirmDialog(){
        BaseDialog baseDialog = new BaseDialog(this, R.style.BaseDialog, R.layout.finish_confirm_dialog);
        TextView context = baseDialog.findViewById(R.id.alert_context);
        Button btSure = baseDialog.findViewById(R.id.bt_confirm);
        context.setText(R.string.scan_all_confirm);
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseDialog.dismiss();
            }
        });
        baseDialog.show();
    }
}
