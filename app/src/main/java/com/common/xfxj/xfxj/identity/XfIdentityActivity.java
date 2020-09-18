package com.common.xfxj.xfxj.identity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.common.xfxj.R;
import com.common.xfxj.app.EsimAndroidApp;
import com.common.xfxj.base.activity.BaseActivity;
import com.common.xfxj.base.presenter.AbstractPresenter;
import com.common.xfxj.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.xfxj.core.room.DbBank;
import com.common.xfxj.uhf.IEsimUhfService;
import com.common.xfxj.uhf.NewSpeedataUhfServiceImpl;
import com.common.xfxj.uhf.UhfMsgEvent;
import com.common.xfxj.uhf.UhfMsgType;
import com.common.xfxj.uhf.UhfTag;
import com.common.xfxj.uhf.XinLianUhfServiceImp;
import com.common.xfxj.uhf.ZebraUhfServiceImpl;
import com.common.xfxj.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class XfIdentityActivity extends BaseActivity {
    private static final String ASSETS_CODE = "assets_code";
    public static final String INV_ID = "inv_id";
    public static final String LOC_IC = "loc_id";
    IEsimUhfService esimUhfService = null;
    @BindView(R.id.title_content)
    TextView mTitle;
    private String mInvId;
    private String mLocId;
    private Boolean canRfid = true;
    private String scanTagEpc = null;//扫描到的Epc
    private List<XfInventoryDetail> xInventoryDetail = new ArrayList<>();
    private List<String> astCodes = new ArrayList<>();
    private String data;
    private  XfInventoryDetail scannedDetail;

    @Override
    public AbstractPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.ast_idntity);
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        mInvId = intent.getStringExtra(INV_ID);
        mLocId = intent.getStringExtra(LOC_IC);
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        if (esimUhfService instanceof XinLianUhfServiceImp || esimUhfService instanceof NewSpeedataUhfServiceImpl) {
            SystemProperties.set("persist.sys.PistolKey", "scan");
        } else if (esimUhfService instanceof ZebraUhfServiceImpl) {
            if (!((ZebraUhfServiceImpl) esimUhfService).isTc20OrMc33()) {
                ((ZebraUhfServiceImpl) esimUhfService).setPressScan(true);
            }
        }
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("com.se4500.onDecodeComplete");
        mFilter.addAction("com.esimScanner.ACTION");
        registerReceiver(receiver, mFilter);
        //地区盘点页面使用
        xInventoryDetail = DbBank.getInstance().getXfInventoryDetailDao().findXInventoryDetail(mInvId,mLocId);
        astCodes.add("detail001");
        astCodes.add("detail002");
        astCodes.add("detail003");
        astCodes.add("detail004");
        astCodes.add("detail005");
        astCodes.add("detail006");
        astCodes.add("detail007");
        astCodes.add("detail008");
        astCodes.add("detail009");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_identity;
    }

    @Override
    protected void initToolbar() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.SCAN_DATA:
                UhfTag scanTag = (UhfTag) uhfMsgEvent.getData();
                String barcode = scanTag.getBarcode();
                startDetailActivity(barcode);
                break;
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag = (UhfTag) uhfMsgEvent.getData();
                scanTagEpc = uhfTag.getEpc();
                if(scannedDetail != null && xInventoryDetail.contains(scannedDetail)&& scanTagEpc.equals(scannedDetail.getAst_epc_code())){
                    esimUhfService.stopScanning();
                    startDetailActivity(data);
                    scannedDetail = null;
                }
                break;
        }
    }

    @OnClick({R.id.title_back})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (esimUhfService instanceof XinLianUhfServiceImp || esimUhfService instanceof NewSpeedataUhfServiceImpl) {
            SystemProperties.set("persist.sys.PistolKey", "uhf");
        } else if (esimUhfService instanceof ZebraUhfServiceImpl) {
            if (((ZebraUhfServiceImpl) esimUhfService).isTc20OrMc33()) {
                ((ZebraUhfServiceImpl) esimUhfService).setScanEnable(false);
            } else {
                ((ZebraUhfServiceImpl) esimUhfService).setPressScan(false);
            }
        }
        EventBus.getDefault().unregister(this);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.se4500.onDecodeComplete".equals(action)) {
                data = intent.getStringExtra("se4500");
                byte[] bytes = intent.getByteArrayExtra("se4500_byte");
                if (data != null) {
                    if(!astCodes.contains(data)){
                        ToastUtils.showShort("非系统内的标签数据");
                        return;
                    }
                    Log.e("BindTagActivity", "stringdata====" + data);
                    if("XfInvAssetLocActivity".equals(EsimAndroidApp.activityFrom)){
                        List<XfInventoryDetail> xInventoryItemDetail = DbBank.getInstance().getXfInventoryDetailDao().findXInventoryItemDetail(data);
                        if(xInventoryItemDetail.size() > 0){
                            scannedDetail = xInventoryItemDetail.get(0);
                        }
                        esimUhfService.startScanning();
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                if(esimUhfService.isStart()){
                                    esimUhfService.stopScanning();
                                    ToastUtils.showShort("rfid未扫描到标签");
                                }

                            }
                        }, 5000);
                    }else {
                        startDetailActivity(data);
                    }

                }
            } else if ("com.esimScanner.ACTION".equals(action)) {
                String data = intent.getStringExtra("com.symbol.datawedge.data_string");
                startDetailActivity(data);
            }
        }
    };

    public void startDetailActivity(String assetsCode) {
        Intent intent = new Intent();
        intent.putExtra(ASSETS_CODE, assetsCode);
        intent.putExtra(INV_ID, mInvId);
        intent.setClass(this, XfAssetsDetailsActivity.class);
        startActivity(intent);
        finish();
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
        if(esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null ){
            esimUhfService.setEnable(false);
        }
    }

}
