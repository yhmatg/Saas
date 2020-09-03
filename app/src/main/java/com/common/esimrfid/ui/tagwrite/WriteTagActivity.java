package com.common.esimrfid.ui.tagwrite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.WriteTagContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.presenter.home.WriteTagPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WriteTagActivity extends BaseActivity<WriteTagPresenter> implements WriteTagContract.View {
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
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    IEsimUhfService esimUhfService = null;
    public static final String TAG = "WriteTagActivity";
    private List<AssetsInfo> mData = new ArrayList<>();
    private WriteTagAdapter adapter;
    private boolean isNeedClearData;
    private String preFilter = "";
    private int currentPage = 1;
    private int pageSize = 10;

    @Override
    public WriteTagPresenter initPresenter() {
        return new WriteTagPresenter();
    }

    @Override
    protected void initEventAndData() {
        initRfidAndEvent();
        adapter = new WriteTagAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        empty_page.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tips.setVisibility(View.GONE);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isNeedClearData = true;
                mPresenter.fetchPageAssetsInfos(pageSize, currentPage, "", 0);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                String assetsId = search.getText().toString();
                isNeedClearData = !preFilter.equals(assetsId);
                if (isNeedClearData) {
                    currentPage = 1;
                } else {
                    currentPage++;
                }
                int currentSize = currentPage == 1 ? 0 : mData.size();
                preFilter = assetsId;
                mPresenter.fetchPageAssetsInfos(pageSize, currentPage, assetsId, currentSize);
            }
        });
        mRefreshLayout.setEnableRefresh(false);//使上拉加载具有弹性效果
        mRefreshLayout.setEnableOverScrollDrag(false);//禁止越界拖动（1.0.4以上版本）
        mRefreshLayout.setEnableOverScrollBounce(false);//关闭越界回弹功能
        mRefreshLayout.setEnableAutoLoadMore(false);
        mPresenter.fetchPageAssetsInfos(pageSize, 0, "", 0);
    }

    private void initRfidAndEvent() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_write_tag;
    }

    @OnClick({R.id.titleLeft, R.id.edit_search})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft:
                finish();
                break;
            case R.id.edit_search:
                searchAssets();
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
                    //mPresenter.getAssetsInfoById(assetsId);
                    mPresenter.fetchPageAssetsInfos(pageSize, currentPage, assetsId, 0);
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
    public void handleAssetsById(List<AssetsInfo> assetsInfos) {
        mData.clear();
        mData.addAll(assetsInfos);
        adapter.notifyDataSetChanged();
        handleResultList(mData);
    }

    @Override
    public void handlefetchPageAssetsInfos(List<AssetsInfo> assetsInfos) {
        mRefreshLayout.finishLoadMore();
        if (isNeedClearData) {
            mData.clear();
        }
        mData.addAll(assetsInfos);
        adapter.notifyDataSetChanged();
        handleResultList(mData);
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
