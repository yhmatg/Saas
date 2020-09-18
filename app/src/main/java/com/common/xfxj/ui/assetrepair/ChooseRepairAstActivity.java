package com.common.xfxj.ui.assetrepair;

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

import com.common.xfxj.R;
import com.common.xfxj.app.EsimAndroidApp;
import com.common.xfxj.base.activity.BaseActivity;
import com.common.xfxj.contract.assetrepair.ChooseRepairContract;
import com.common.xfxj.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.xfxj.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.xfxj.presenter.assetrepair.ChooseRepairPresenter;
import com.common.xfxj.uhf.IEsimUhfService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChooseRepairAstActivity extends BaseActivity<ChooseRepairPresenter> implements ChooseRepairContract.View {
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
    private List<AssetsInfo> mData = new ArrayList<>();
    private AssetsRepairAdapter adapter;
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
        adapter = new AssetsRepairAdapter(this, mData,"ChooseRepairAstActivity");
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
        //mPresenter.getAllAssetsByOpt("REPAIR","");
        mPresenter.getAllAssetsByOpt(pageSize, 1, "REPAIR", "");

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
                List<AssetsInfo> assetsInfos = adapter.getmSelectedData();
                RepairAssetEvent repairAssetEvent = new RepairAssetEvent(assetsInfos);
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
                    //mPresenter.getAllAssetsByOpt("REPAIR",assetsId);
                    mPresenter.getAllAssetsByOpt(pageSize, currentPage, "REPAIR", assetsId);
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

    @Override
    public void handlePageAssetsByOpt(List<AssetsInfo> assetsInfos) {
        mRefreshLayout.finishLoadMore();
        if (isNeedClearData) {
            mData.clear();
        }
        mData.addAll(assetsInfos);
        adapter.notifyDataSetChanged();
        handleResultList(mData);
    }

    @Override
    public void handleAllAssetsByOpt(List<AssetsInfo> assetsInfos) {
        mData.clear();
        mData.addAll(assetsInfos);
        adapter.notifyDataSetChanged();
        handleResultList(mData);
    }

    @Override
    public void handleXfInvDetails(List<XfInventoryDetail> inventoryDetails) {

    }

    private void handleResultList(List<AssetsInfo> mData) {
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
}
