package com.common.esimrfid.xfxj.repair;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetrepair.ChooseRepairContract;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.esimrfid.presenter.assetrepair.ChooseRepairPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class XfChooseRepairAstActivity extends BaseActivity<ChooseRepairPresenter> implements ChooseRepairContract.View {
    @BindView(R.id.edit_search)
    EditText search;
    @BindView(R.id.titleLeft)
    ImageView titleLeft;
    @BindView(R.id.write_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.empty_page)
    LinearLayout empty_page;
    @BindView(R.id.tv_tips)
    TextView tips;
    @BindView(R.id.btn_sure)
    Button btSure;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    IEsimUhfService esimUhfService = null;
    public static final String TAG = "ChooseRepairAstActivity";
    private List<XfInventoryDetail> mData = new ArrayList<>();
    private XfAssetsRepairAdapter adapter;
    private boolean isNeedClearData;
    private String preFilter = "";
    private int currentPage = 1;
    private int pageSize = 10;

    @Override
    public ChooseRepairPresenter initPresenter() {
        return new ChooseRepairPresenter();
    }

    @Override
    protected void initEventAndData() {
        initRfidAndEvent();
        adapter = new XfAssetsRepairAdapter(this, mData,"ChooseRepairAstActivity");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        empty_page.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tips.setVisibility(View.GONE);
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                String param = search.getText().toString();
                isNeedClearData = !preFilter.equals(param);
                if (isNeedClearData) {
                    currentPage = 1;
                } else {
                    currentPage++;
                }
                preFilter = param;
                mPresenter.getAllAssetsByOpt(pageSize, currentPage, "REPAIR", param);
            }
        });
        mRefreshLayout.setEnableRefresh(false);//使上拉加载具有弹性效果
        mRefreshLayout.setEnableOverScrollDrag(false);//禁止越界拖动（1.0.4以上版本）
        mRefreshLayout.setEnableOverScrollBounce(false);//关闭越界回弹功能
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableLoadMore(false);
        mPresenter.fetchXfAllInvDetails( "");

    }

    private void initRfidAndEvent() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_repair;
    }

    @OnClick({R.id.titleLeft, R.id.edit_search ,R.id.btn_sure})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft:
                finish();
                break;
            case R.id.edit_search:
                searchAssets();
                break;
            case R.id.btn_sure:
                List<XfInventoryDetail> assetsInfos = adapter.getmSelectedData();
                XfRepairAssetEvent repairAssetEvent = new XfRepairAssetEvent(assetsInfos);
                EventBus.getDefault().post(repairAssetEvent);
                finish();
                break;
        }
    }

    private void searchAssets() {
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    String assetsId = search.getText().toString();
                    search.setSelection(assetsId.length());
                    isNeedClearData = true;
                    currentPage = 1;
                    preFilter = assetsId;
                    mPresenter.fetchXfAllInvDetails( assetsId);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void handleResultList(List<XfInventoryDetail> mData) {
        if (mData.size() == 0) {
            empty_page.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tips.setVisibility(View.GONE);
        } else {
            empty_page.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            tips.setVisibility(View.GONE);
        }
    }

    @Override
    public void handlePageAssetsByOpt(List<AssetsInfo> assetsInfos) {

    }

    @Override
    public void handleAllAssetsByOpt(List<AssetsInfo> assetsInfos) {

    }

    @Override
    public void handleXfInvDetails(List<XfInventoryDetail> inventoryDetails) {
        mData.clear();
        mData.addAll(inventoryDetails);
        adapter.notifyDataSetChanged();
        handleResultList(mData);
    }
}