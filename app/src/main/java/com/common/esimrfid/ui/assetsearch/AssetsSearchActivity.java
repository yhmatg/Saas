package com.common.esimrfid.ui.assetsearch;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetsearch.AssetsSearchContract;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;
import com.common.esimrfid.presenter.assetsearch.AssetsSearchPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.utils.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
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
    @BindString(R.string.scan_epc_str)
    String scanEpcNum;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private AssetsSearchAdapter assetsSearchAdapter;
    private List<SearchAssetsInfo> mData = new ArrayList<>();
    private Set<String> epcSet = new HashSet<>();
    //private List<SearchAssetsInfo> allAssets = new ArrayList<>();
    //private HashMap<String, SearchAssetsInfo> assetsMap = new HashMap<>();
    IEsimUhfService esimUhfService = null;
    private CircleAnimation animation;
    private Boolean canRfid = true;
    private Boolean showScanAssets = false;
    private Boolean isSearch = true;
    private boolean isNeedClearData;
    private String preFilter = "";
    private int pageSize = 10;
    private int currentPage = 1;

    @Override
    public AssetsSearchPresenter initPresenter() {
        return new AssetsSearchPresenter();
    }

    @Override
    protected void initEventAndData() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initRfidAndEvent();
        assetsSearchAdapter = new AssetsSearchAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(assetsSearchAdapter);
        empty_page.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tips.setVisibility(View.VISIBLE);
        rotateAnim();
        scanNmuber.setText("?????????????????????0???");
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                String assetsId = editText.getText().toString();
                isNeedClearData = !preFilter.equals(assetsId);
                if (isNeedClearData) {
                    currentPage = 1;
                } else {
                    currentPage++;
                }
                //int currentSize = isNeedClearData ? 0 : mData.size();
                preFilter = assetsId;
                //mPresenter.fetchPageAssetsInfos(pageSize, assetsId, currentSize);
                mPresenter.fetchPageFilterAssetsList(pageSize,currentPage,preFilter);
            }
        });
        mRefreshLayout.setEnableRefresh(false);//?????????????????????????????????
        mRefreshLayout.setEnableOverScrollDrag(false);//?????????????????????1.0.4???????????????
        mRefreshLayout.setEnableOverScrollBounce(false);//????????????????????????
        mRefreshLayout.setEnableAutoLoadMore(false);
        //mPresenter.fetchLatestPageAssets(500,1);
        //mPresenter.getAllAssetsForSearch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
            esimUhfService.setEnable(true);
        }
    }

    private void rotateAnim() {
        int radii = (int) (20 * getResources().getDisplayMetrics().density);
        animation = new CircleAnimation(radii);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);//???????????????????????? ????????????
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatMode(Animation.RESTART);

    }

    private void initRfidAndEvent() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
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
                if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
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
                    isNeedClearData = true;
                    currentPage = 1;
                    preFilter = assetsId;
                    //mPresenter.fetchPageAssetsInfos(pageSize, preFilter, 0);
                    mPresenter.fetchPageFilterAssetsList(pageSize,currentPage,preFilter);
                    isSearch = true;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void handleSearchAssets(List<SearchAssetsInfo> searchInfos) {
        mData.clear();
        mData.addAll(searchInfos);
        String formatNum = String.format(stringNum, mData.size());
        scanNmuber.setText(formatNum);
        handleResultList(mData);
        assetsSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void handGetAllAssetsForSearch(List<SearchAssetsInfo> searchInfos) {
        //????????????????????????????????????????????????
       /* allAssets.clear();
        allAssets.addAll(searchInfos);
        for (SearchAssetsInfo allAsset : allAssets) {
            assetsMap.put(allAsset.getAst_epc_code(), allAsset);
        }*/
    }

    @Override
    public void handleFetchPageAssetsInfos(List<SearchAssetsInfo> assetsInfos) {
        mRefreshLayout.finishLoadMore();
        if (isNeedClearData || !isSearch) {
            mData.clear();
            isSearch = true;
        }
        mData.addAll(assetsInfos);
        String formatNum = String.format(stringNum, mData.size());
        scanNmuber.setText(formatNum);
        handleResultList(mData);
        assetsSearchAdapter.notifyDataSetChanged();
    }

    private void handleResultList(List<SearchAssetsInfo> mData) {
        if (mData.size() > 0) {
            empty_page.setVisibility(View.GONE);
            tips.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            scanNmuber.setVisibility(View.VISIBLE);
        } else {
            empty_page.setVisibility(View.VISIBLE);
            tips.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            scanNmuber.setVisibility(View.GONE);
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
                image_scan.clearAnimation();
                search.setImageResource(R.drawable.search_nearby_assets);
                break;
            case UhfMsgType.UHF_START:
                image_scan.startAnimation(animation);
                search.setImageResource(R.drawable.stop_search);
                editText.setText("");
                epcSet.clear();
                if (isSearch) {
                    mData.clear();
                    assetsSearchAdapter.notifyDataSetChanged();
                    String formatNum = String.format(scanEpcNum, 0);
                    scanNmuber.setText(formatNum);
                    scanNmuber.setVisibility(View.VISIBLE);
                    isSearch = false;
                }
                mRefreshLayout.setEnableLoadMore(false);
                break;
            case UhfMsgType.UHF_STOP:
                image_scan.clearAnimation();
                search.setImageResource(R.drawable.search_nearby_assets);
                mRefreshLayout.setEnableLoadMore(true);
                mPresenter.fetchScanAssetsEpc(epcSet);
                break;
        }
    }

    private void handleEpc(String epc) {
       /* SearchAssetsInfo searchAssetsInfo = assetsMap.get(epc);
        if (searchAssetsInfo != null && !mData.contains(searchAssetsInfo)) {
            mData.add(searchAssetsInfo);
            if (!showScanAssets) {
                handleResultList(mData);
                showScanAssets = true;
            }
            String formatNum = String.format(stringNum, mData.size());
            scanNmuber.setText(formatNum);
            assetsSearchAdapter.notifyDataSetChanged();
        }*/
        epcSet.add(epc);
        String scanNum = String.format(scanEpcNum, epcSet.size());
        scanNmuber.setText(scanNum);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null && esimUhfService.isStart()) {
            esimUhfService.stopScanning();
            image_scan.clearAnimation();
            search.setImageResource(R.drawable.search_nearby_assets);
        }
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
            esimUhfService.setEnable(false);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (canRfid) {
            if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                if (keyCode == esimUhfService.getDownKey()) { //???????????????
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
