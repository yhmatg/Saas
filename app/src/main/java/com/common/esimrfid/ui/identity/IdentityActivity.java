package com.common.esimrfid.ui.identity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.NewSpeedataUhfServiceImpl;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.uhf.ZebraUhfServiceImpl;
import com.common.esimrfid.ui.assetsearch.AssetsDetailsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class IdentityActivity extends BaseActivity {
    private static final String ASSETS_CODE = "assets_code";
    private static final String WHERE_FROM = "where_from";
    IEsimUhfService esimUhfService = null;
    @BindView(R.id.title_content)
    TextView mTitle;
    private String from;

    @Override
    public AbstractPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.ast_idntity);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        from = intent.getStringExtra(WHERE_FROM);
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        if( esimUhfService instanceof NewSpeedataUhfServiceImpl){
            SystemProperties.set("persist.sys.PistolKey", "scan");
        }else if(esimUhfService instanceof ZebraUhfServiceImpl){
            if(!((ZebraUhfServiceImpl) esimUhfService).isTc20()){
                ((ZebraUhfServiceImpl) esimUhfService).setPressScan(true);
            }
        }
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("com.se4500.onDecodeComplete");
        mFilter.addAction("com.esimScanner.ACTION");
        registerReceiver(receiver,mFilter);
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
        if( esimUhfService instanceof NewSpeedataUhfServiceImpl){
            SystemProperties.set("persist.sys.PistolKey", "uhf");
        }else if(esimUhfService instanceof ZebraUhfServiceImpl){
            if(((ZebraUhfServiceImpl) esimUhfService).isTc20()){
                ((ZebraUhfServiceImpl)esimUhfService).setScanEnable(false);
            }else {
                ((ZebraUhfServiceImpl) esimUhfService).setPressScan(false);
            }
        }
        EventBus.getDefault().unregister(this);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.se4500.onDecodeComplete".equals(action)) {
                String data = intent.getStringExtra("se4500");
                byte[] bytes = intent.getByteArrayExtra("se4500_byte");
                if (data != null) {
                    Log.e("BindTagActivity","stringdata====" + data);
                    startDetailActivity(data);
                }
            }else if("com.esimScanner.ACTION".equals(action)){
                String data = intent.getStringExtra("com.symbol.datawedge.data_string");
                startDetailActivity(data);
            }
        }
    };

    public void startDetailActivity(String assetsCode){
        Intent intent=new Intent();
        intent.putExtra(ASSETS_CODE,assetsCode);
        intent.putExtra(WHERE_FROM,from);
        intent.setClass(this, AssetsDetailsActivity.class);
        startActivity(intent);
        if("AssetRepairActivity".equals(from)){
            finish();
        }
    }

}
