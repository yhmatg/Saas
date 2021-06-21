package com.common.esimrfid.ui.distribute;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.Constants;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.distribute.DistOrdDetailContract;
import com.common.esimrfid.core.bean.assetdetail.AssetFilterParameter;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DistTypeDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DistributeOrderDetail;
import com.common.esimrfid.presenter.distribute.DistOrderDetailPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.NewSpeedataUhfServiceImpl;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.uhf.XinLianUhfServiceImp;
import com.common.esimrfid.uhf.ZebraUhfServiceImpl;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.utils.Utils;
import com.multilevel.treelist.Node;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class DistOrderDetailActivity extends BaseActivity<DistOrderDetailPresenter> implements DistOrdDetailContract.View, DistributeTypeAdapter.OnItemClickListener, AssetsDistributeAdapter.OnDeleteClickListener {
    private static String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";
    private static String START_SCAN_ACTION = "com.geomobile.se4500barcode";
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.rv_dist_type)
    RecyclerView mDistTypeRecycle;
    @BindView(R.id.ll_option)
    LinearLayout mLlOption;
    @BindView(R.id.bt_dist_confirm)
    Button mDistConfirm;
    @BindView(R.id.bt_scan_add)
    Button mScanAdd;
    @BindView(R.id.bt_rfid_add)
    Button mRfidAdd;
    private int currentPage = 1;
    private int pageSize = 100;
    private ArrayList<DistTypeDetail> typeList = new ArrayList<>();
    private DistributeTypeAdapter typeAdapter;
    private String distributeOrderId;
    private boolean distOrderIsFinish;
    private HashMap<String, AssetsListItemInfo> barcodeAndAsset = new HashMap<>();
    private HashMap<String, AssetsListItemInfo> epcAndAsset = new HashMap<>();
    private HashMap<String, DistTypeDetail> typeIdAndTypeDetail = new HashMap<>();
    private AssetFilterParameter conditions = new AssetFilterParameter();
    private View dialogView;
    private Dialog selectAssetDialog;
    private List<AssetsListItemInfo> selectedAssets = new ArrayList<>();
    private RecyclerView distAssetRecycle;
    private AssetsDistributeAdapter distAssetAdapter;
    private ImageView mBackImg;
    private TextView dialogTitle;
    private IEsimUhfService esimUhfService = null;
    DistributeOrderDetail mDistributeOrderDetail;
    private boolean isScanMode = true;
    private Boolean canRfid = true;
    private DistTypeDetail mDistTypeDetail;

    @Override
    public DistOrderDetailPresenter initPresenter() {
        return new DistOrderDetailPresenter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText("派发任务详单");
        enableScan();
        Intent intent = getIntent();
        distributeOrderId = intent.getStringExtra(Constants.DIST_ORDER_ID);
        distOrderIsFinish = intent.getBooleanExtra(Constants.DIST_ORDER_IS_FINISH,true);
        typeAdapter = new DistributeTypeAdapter(this, typeList,distOrderIsFinish);
        typeAdapter.setmOnItemClickListener(this);
        mDistTypeRecycle.setLayoutManager(new LinearLayoutManager(this));
        mDistTypeRecycle.setAdapter(typeAdapter);
        if (!StringUtils.isEmpty(distributeOrderId)) {
            mPresenter.getDistOrdDetail(distributeOrderId);
        }
        //初始化dialog布局
        distAssetAdapter = new AssetsDistributeAdapter(this, selectedAssets,distOrderIsFinish);
        distAssetAdapter.setOnDeleteClickListener(this);
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_dist_asset_layout, null);
        distAssetRecycle = (RecyclerView) dialogView.findViewById(R.id.dist_asset_recycle);
        distAssetRecycle.setLayoutManager(new LinearLayoutManager(this));
        distAssetRecycle.setAdapter(distAssetAdapter);
        mTitle = dialogView.findViewById(R.id.title_content);
        mTitle.setText("派发资产明细");
        mBackImg = dialogView.findViewById(R.id.title_back);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAssetDialog.dismiss();
            }
        });
        if(distOrderIsFinish){
            mLlOption.setVisibility(View.GONE);
        }
        //初始化rfid
        EventBus.getDefault().register(this);
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        if (esimUhfService instanceof XinLianUhfServiceImp || esimUhfService instanceof NewSpeedataUhfServiceImpl) {
            SystemProperties.set("persist.sys.PistolKey", "scan");
            SystemProperties.set("persisy.sys.scankeydisable", "true");
        } else if (esimUhfService instanceof ZebraUhfServiceImpl) {
            if (!((ZebraUhfServiceImpl) esimUhfService).isTc20OrMc33()) {
                ((ZebraUhfServiceImpl) esimUhfService).setPressScan(true);
            }
        }
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(RECE_DATA_ACTION);
        mFilter.addAction("com.esimScanner.ACTION");
        registerReceiver(receiver, mFilter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dist_detail;
    }

    @Override
    protected void initToolbar() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        String epc = null;
        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.SCAN_DATA:
                UhfTag scanTag = (UhfTag) uhfMsgEvent.getData();
                String barcode = scanTag.getBarcode();
                handleScannedOrRfidStr(barcode, false);
                break;
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag = (UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getEpc();
                handleScannedOrRfidStr(epc, true);
                break;
            case UhfMsgType.UHF_CONNECT:
                break;
            case UhfMsgType.UHF_DISCONNECT:
                break;
            case UhfMsgType.UHF_START:
                mRfidAdd.setText("射频扫描中");
                break;
            case UhfMsgType.UHF_STOP:
                mRfidAdd.setText("射频扫描添加");
                break;
        }
    }

    @OnClick({R.id.title_back, R.id.bt_dist_confirm, R.id.bt_scan_add, R.id.bt_rfid_add})
    void perform(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.bt_dist_confirm:
                if(isAllAssetAdded()){
                    mPresenter.confirmDistributeAsset(mDistributeOrderDetail);
                }else {
                    ToastUtils.showShort("用户申领的资产数量与派发的资产数量不匹配，请您核查!");
                }
                break;
            case R.id.bt_scan_add:
                if (!isScanMode) {
                    enableScan();
                }
                sendBroadcasts("com.geomobile.se4500barcodestop");
                SystemProperties.set("persist.sys.scanstopimme", "true");
                SystemClock.sleep(20L);
                SystemProperties.set("persist.sys.scanstopimme", "false");
                sendBroadcasts("com.geomobile.se4500barcode");
                break;
            case R.id.bt_rfid_add:
                if (isScanMode) {
                    disableScan();
                }
                if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                    esimUhfService.startStopScanning();
                } else {
                    ToastUtils.showShort(R.string.not_connect_prompt);
                }
                break;
        }
    }

    @Override
    public void handleGetDistOrdDetail(DistributeOrderDetail distributeOrderDetail) {
        mDistributeOrderDetail = distributeOrderDetail;
        List<DistTypeDetail> typeDetails = distributeOrderDetail.getDetail();
        typeList.clear();
        typeList.addAll(typeDetails);
        typeAdapter.notifyDataSetChanged();
        conditions.clearData();
        typeIdAndTypeDetail.clear();
        ArrayList<Node> selectAssetsTypes = new ArrayList<>();
        ArrayList<String> userIds = new ArrayList<>();
        for (DistTypeDetail typeDetail : typeList) {
            Node node = new Node(typeDetail.getType_id(), "-1", typeDetail.getType_name());
            selectAssetsTypes.add(node);
            if(!userIds.contains(typeDetail.getRet_store_user_id())){
                userIds.add(typeDetail.getRet_store_user_id());
            }
            typeIdAndTypeDetail.put(typeDetail.getType_id(), typeDetail);
            if(!distOrderIsFinish){
                typeDetail.setNotAdd(typeDetail.getAmount());
            }
        }
        conditions.setmSelectAssetsTypes(selectAssetsTypes);
        conditions.setmSelectUserIds(userIds);
        ArrayList<Node> selectAssetsStatus = new ArrayList<>();
        selectAssetsStatus.add(new Node("0", "-1", "闲置"));
        conditions.setmSelectAssetsStatus(selectAssetsStatus);
        mPresenter.fetchPageAssetsInfos(pageSize, currentPage, "", "", 0, conditions);
    }

    @Override
    public void handleFetchPageAssetsInfos(List<AssetsListItemInfo> assetsInfos) {
        for (AssetsListItemInfo assetsInfo : assetsInfos) {
            barcodeAndAsset.put(assetsInfo.getAst_barcode(), assetsInfo);
            epcAndAsset.put(assetsInfo.getAst_epc_code(), assetsInfo);
        }
    }

    @Override
    public void handleConfirmDistributeAsset(BaseResponse baseResponse) {
        if ("200000".equals(baseResponse.getCode())) {
            ToastUtils.showShort("派发资产成功");
            finish();
        } else {
            ToastUtils.showShort("派发资产失败");
        }
    }

    @Override
    public void onItemClicked(DistTypeDetail distTypeDetail) {
        selectedAssets.clear();
        selectedAssets.addAll(distTypeDetail.getSubList());
        mDistTypeDetail = distTypeDetail;
        //selectedAssets = distTypeDetail.getSubList();
        distAssetAdapter.notifyDataSetChanged();
        showFilterDialog();
    }

    public void showFilterDialog() {
        if (selectAssetDialog != null) {
            selectAssetDialog.show();
        } else {
            selectAssetDialog = new Dialog(this);
            selectAssetDialog.setContentView(dialogView);
            selectAssetDialog.show();
            Window window = selectAssetDialog.getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.getDecorView().setBackgroundColor(Color.WHITE);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(layoutParams);
            }
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.se4500.onDecodeComplete".equals(action)) {
                String data = intent.getStringExtra("se4500");
                byte[] bytes = intent.getByteArrayExtra("se4500_byte");
                if (data != null) {
                    Log.e("BindTagActivity", "stringdata====" + data);
                    handleScannedOrRfidStr(data, false);
                }
            } else if ("com.esimScanner.ACTION".equals(action)) {
                String data = intent.getStringExtra("com.symbol.datawedge.data_string");
                handleScannedOrRfidStr(data, false);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(receiver);
        disableScan();
    }

    private void handleScannedOrRfidStr(String ecpOrBarcode, boolean isEpc) {
        AssetsListItemInfo assetsListItemInfo;
        if (isEpc) {
            assetsListItemInfo = epcAndAsset.get(ecpOrBarcode);
        } else {
            assetsListItemInfo = barcodeAndAsset.get(ecpOrBarcode);
        }
        if (assetsListItemInfo != null) {
            String type_id = assetsListItemInfo.getType_id();
            DistTypeDetail distTypeDetail = typeIdAndTypeDetail.get(type_id);
            List<AssetsListItemInfo> assetsListItemInfos = distTypeDetail.getSubList();
            if (assetsListItemInfos != null && !assetsListItemInfos.contains(assetsListItemInfo)) {
                String id = assetsListItemInfo.getId();
                if(!StringUtils.isEmpty(id)){
                    assetsListItemInfo.setAst_id(id);
                    assetsListItemInfo.setId(null);
                }
                assetsListItemInfo.setOd_id(mDistributeOrderDetail.getId());
                assetsListItemInfos.add(assetsListItemInfo);
                distTypeDetail.setAlreadyAdd(assetsListItemInfos.size());
                int notAdd = distTypeDetail.getAmount() - assetsListItemInfos.size();
                distTypeDetail.setNotAdd(notAdd >= 0 ? notAdd :0);
                typeAdapter.notifyDataSetChanged();
            }
        }else {
            ToastUtils.showShort("添加的资产与用户申领的资产不一致");
        }
    }

    @Override
    public void onDeleteClick(AssetsListItemInfo assetsInfo) {
        mDistTypeDetail.getSubList().remove(assetsInfo);
        Integer alreadyAdd = mDistTypeDetail.getAlreadyAdd();
        Integer amount = mDistTypeDetail.getAmount();
        int notAdded;
        Integer finalAlreadyAdd = alreadyAdd - 1;
        if(finalAlreadyAdd >= amount){
            notAdded = 0;
        }else {
            notAdded = amount - finalAlreadyAdd;
        }
        mDistTypeDetail.setAlreadyAdd(finalAlreadyAdd);

        mDistTypeDetail.setNotAdd(notAdded);
        typeAdapter.notifyDataSetChanged();
    }

    public void enableScan() {
        if (esimUhfService instanceof XinLianUhfServiceImp || esimUhfService instanceof NewSpeedataUhfServiceImpl) {
            SystemProperties.set("persist.sys.PistolKey", "scan");
        } else if (esimUhfService instanceof ZebraUhfServiceImpl) {
            if (!((ZebraUhfServiceImpl) esimUhfService).isTc20OrMc33()) {
                ((ZebraUhfServiceImpl) esimUhfService).setPressScan(true);
            }
        }
        isScanMode = true;
    }

    public void disableScan() {
        if (esimUhfService instanceof XinLianUhfServiceImp || esimUhfService instanceof NewSpeedataUhfServiceImpl) {
            SystemProperties.set("persist.sys.PistolKey", "uhf");
        } else if (esimUhfService instanceof ZebraUhfServiceImpl) {
            if (((ZebraUhfServiceImpl) esimUhfService).isTc20OrMc33()) {
                ((ZebraUhfServiceImpl) esimUhfService).setScanEnable(false);
            } else {
                ((ZebraUhfServiceImpl) esimUhfService).setPressScan(false);
            }
        }
        isScanMode = false;
    }

    private void sendBroadcasts(String s) {
        Intent intent = new Intent();
        intent.setAction(s);
        sendBroadcast(intent);
    }

    private boolean isAllAssetAdded() {
        boolean isAllAdded = true;
        for (Map.Entry<String, DistTypeDetail> next : typeIdAndTypeDetail.entrySet()) {
            DistTypeDetail value = next.getValue();
            if (!value.getAmount().equals(value.getAlreadyAdd())) {
                isAllAdded = false;
            }
        }
        return isAllAdded;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (canRfid && !isScanMode) {
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
        if(!isScanMode){
            canRfid = true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止rfid
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null && esimUhfService.isStart()) {
            esimUhfService.stopScanning();
        }
        //停止条码扫描
        if(isScanMode){
            sendBroadcasts("com.geomobile.se4500barcodestop");
            SystemProperties.set("persist.sys.scanstopimme", "true");
        }
    }
}
