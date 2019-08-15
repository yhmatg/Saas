package com.common.esimrfid.ui.cardsearch;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.ScanFridContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.invscannbeans.AssetsInfo;
import com.common.esimrfid.presenter.home.ScanRfidPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanRfidActivity extends BaseActivity<ScanRfidPresenter> implements ScanFridContract.View {

    public static final String TAG = "ScanRfidActivity";
    @BindView(R.id.imgTitleLeft)
    ImageView imgTitleLeft;
    @BindView(R.id.open_or_stop)
    ImageView mOpenOrStop;
    @BindView(R.id.clear_btn)
    ImageView mClearBtn;
    @BindView(R.id.error_page)
    LinearLayout errorPage;
    @BindView(R.id.empty_page)
    LinearLayout emptyPage;
    @BindView(R.id.rv_inv_assetsInfos)
    RecyclerView mRvInfos;
    @BindView(R.id.tvTitleCenter)
    TextView tvTitleCenter;
    private ScanRfidAdapter mScanAdapter;
    private List<AssetsInfo> mAssetsInfos = new ArrayList<>();
    Set<String> scanEpcs = new HashSet<>();
    IEsimUhfService esimUhfService=null;

    @Override
    protected void initEventAndData() {
        tvTitleCenter.setText("查找产品");
        initRfidAndEventbus();
        mScanAdapter = new ScanRfidAdapter(this,mAssetsInfos);
        mRvInfos.setLayoutManager(new LinearLayoutManager(this));
        mRvInfos.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRvInfos.setAdapter(mScanAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_frid;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public ScanRfidPresenter initPresenter() {
        return new ScanRfidPresenter(DataManager.getInstance());
    }

    @OnClick({R.id.imgTitleLeft,R.id.open_or_stop,R.id.clear_btn})
    void performClick(View view){
        switch (view.getId()){
            case R.id.imgTitleLeft:
                finish();
                break;
            case R.id.open_or_stop:
                esimUhfService.startStopScanning();
                break;
            case R.id.clear_btn:
                mAssetsInfos.clear();
                mScanAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        Log.d(TAG, "handleEPC: "+uhfMsgEvent.toString());
        String epc=null;
        switch(uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag=(UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getEpc();
                Log.d(TAG, "epc=====: "+epc);
                handleEpc(epc);
                break;
            case UhfMsgType.UHF_CONNECT:
                break;
            case UhfMsgType.UHF_DISCONNECT:
                break;
            case UhfMsgType.UHF_START:
                scanEpcs.clear();
                mOpenOrStop.setImageResource(R.drawable.stopicon);
                break;
            case UhfMsgType.UHF_STOP:
                mOpenOrStop.setImageResource(R.drawable.openicon);
                mPresenter.fetchScanAssetsInfons(scanEpcs);
                break;
        }
    }

    public void handleEpc(String epc){
        scanEpcs.add(epc);
    }

    private void initRfidAndEventbus() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void handleAssetsInfons(List<AssetsInfo> assetsInfos) {
        mAssetsInfos.clear();
        mAssetsInfos.addAll(assetsInfos);
        mScanAdapter.notifyDataSetChanged();
    }
}
