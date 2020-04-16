package com.ddcommon.esimrfid.ui.assetsearch;


import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddcommon.esimrfid.R;
import com.ddcommon.esimrfid.app.EsimAndroidApp;
import com.ddcommon.esimrfid.base.activity.BaseActivity;
import com.ddcommon.esimrfid.contract.assetsearch.AssetsSearchContract;
import com.ddcommon.esimrfid.core.DataManager;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.ddcommon.esimrfid.presenter.assetsearch.AssetsSearchPresenter;
import com.ddcommon.esimrfid.uhf.IEsimUhfService;
import com.ddcommon.esimrfid.uhf.UhfMsgEvent;
import com.ddcommon.esimrfid.uhf.UhfMsgType;
import com.ddcommon.esimrfid.uhf.UhfTag;
import com.ddcommon.esimrfid.utils.ToastUtils;
import com.ddcommon.esimrfid.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class AssetsSearchActivity extends BaseActivity<AssetsSearchPresenter> implements AssetsSearchContract.View {

    private static final String TAG = "AssetsSearchActivity";
    @BindView(R.id.titleLeft)
    ImageView titleLeft;
    @BindView(R.id.edit_search)
    EditText editText;
    @BindView(R.id.search_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.search_ast)
    ImageView search;
    @BindView(R.id.image_scan)
    ImageView image_scan;
    @BindView(R.id.empty_page)
    LinearLayout empty_page;
    @BindView(R.id.tv_tips)
    TextView tips;
    @BindView(R.id.scan_num)
    TextView scanNmuber;
    @BindString(R.string.zero_str)
    String stringNum;
    private AssetsSearchAdapter assetsSearchAdapter;
    private List<AssetsInfo> mData = new ArrayList<>();
    private Set<String> scanEpcs = new HashSet<>();
    IEsimUhfService esimUhfService = null;
    private CircleAnimation animation;
    private Boolean canRfid = true;
    @Override
    public AssetsSearchPresenter initPresenter() {
        return new AssetsSearchPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        initRfidAndEvent();
        assetsSearchAdapter = new AssetsSearchAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(assetsSearchAdapter);
        empty_page.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tips.setVisibility(View.VISIBLE);
        rotateAnim();
        scanNmuber.setText("查找到资产数量0个");
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if(esimUhfService != null ){
            esimUhfService.setEnable(true);
        }
    }

    private void rotateAnim() {
        int radii = (int)(20  * getResources().getDisplayMetrics().density);
        animation = new CircleAnimation(radii);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);//设置动画重复次数 无限循环
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatMode(Animation.RESTART);

    }

    private void initRfidAndEvent() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assets_search;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.titleLeft, R.id.search_ast, R.id.edit_search})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft:
                finish();
                break;
            case R.id.search_ast:
                if (esimUhfService != null) {
                    esimUhfService.startStopScanning();
                } else {
                    ToastUtils.showShort(R.string.not_connect_prompt);
                }
                break;
            case R.id.edit_search:
                assetsSearch();
                break;
        }
    }

    private void assetsSearch() {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    String assetsId = editText.getText().toString();
                    editText.setSelection(assetsId.length());
                    mPresenter.getSearchAssetsById(assetsId);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void handleScanAssets(List<AssetsInfo> assetsSearchInfos) {
        mData.clear();
        mData.addAll(assetsSearchInfos);
        assetsSearchAdapter.notifyDataSetChanged();
        handleResultList(mData);
        String formatNum = String.format(stringNum,mData.size());
        scanNmuber.setText(formatNum);
        if(mData.size()>0){
            scanNmuber.setVisibility(View.VISIBLE);
        }else
            scanNmuber.setVisibility(View.GONE);

    }

    @Override
    public void handleSearchAssets(List<AssetsInfo> searchInfos) {
        mData.clear();
        mData.addAll(searchInfos);
        assetsSearchAdapter.notifyDataSetChanged();
        handleResultList(mData);
        String formatNum = String.format(stringNum,mData.size());
        scanNmuber.setText(formatNum);
        if(mData.size()>0){
            scanNmuber.setVisibility(View.VISIBLE);
        }else
            scanNmuber.setVisibility(View.GONE);
    }

    private void handleResultList(List<AssetsInfo> mData) {
        if (mData.size() > 0) {
            empty_page.setVisibility(View.GONE);
            tips.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            empty_page.setVisibility(View.VISIBLE);
            tips.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        Log.d(TAG, "handleEPC: " + uhfMsgEvent.toString());
        String epc = null;
        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag = (UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getEpc();
                Log.e(TAG, "epc=====: " + epc);
                handleEpc(epc);
                break;
            case UhfMsgType.UHF_CONNECT:
                break;
            case UhfMsgType.UHF_DISCONNECT:
                break;
            case UhfMsgType.UHF_START:
                image_scan.startAnimation(animation);
                search.setImageResource(R.drawable.stop_search);
                scanEpcs.clear();
                break;
            case UhfMsgType.UHF_STOP:
                image_scan.clearAnimation();
                search.setImageResource(R.drawable.search_nearby_assets);
                if (scanEpcs.size() != 0) {
                    mPresenter.getScanAssetsByEpc(scanEpcs);
                } else {
                    mData.clear();
                    assetsSearchAdapter.notifyDataSetChanged();
                    handleResultList(mData);
                    String formatNum = String.format(stringNum,0);
                    scanNmuber.setText(formatNum);
                    scanNmuber.setVisibility(View.GONE);
                    ToastUtils.showShort(R.string.not_get_epc);
                }
                break;
        }
    }

    private void handleEpc(String epc) {
        scanEpcs.add(epc);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        if(esimUhfService != null && esimUhfService.isStart()){
            esimUhfService.stopScanning();
            image_scan.clearAnimation();
            search.setImageResource(R.drawable.search_nearby_assets);
        }
        if(esimUhfService != null ){
            esimUhfService.setEnable(false);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(canRfid){
            if (esimUhfService != null) {
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
}
