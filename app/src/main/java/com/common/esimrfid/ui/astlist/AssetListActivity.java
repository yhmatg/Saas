package com.common.esimrfid.ui.astlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetlist.AssetListContract;
import com.common.esimrfid.contract.home.WriteTagContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.customview.CustomPopWindow;
import com.common.esimrfid.presenter.assetlist.AssetListPresenter;
import com.common.esimrfid.presenter.home.WriteTagPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.ui.inventorytask.FilterBean;
import com.common.esimrfid.ui.inventorytask.FiltterAdapter;
import com.common.esimrfid.ui.inventorytask.InvLocationAdapter;
import com.common.esimrfid.ui.tagwrite.WriteTagAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AssetListActivity extends BaseActivity<AssetListPresenter> implements AssetListContract.View, FiltterAdapter.OnItemClickListener {
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
    @BindView(R.id.view_mask)
    View maskView;
    @BindView(R.id.tv_sort)
    TextView mTvSort;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.im_arrow_one)
    ImageView mSortArrow;
    @BindView(R.id.im_arrow_two)
    ImageView mStatusArrow;
    @BindView(R.id.head_layout)
    RelativeLayout filterLayout;
    IEsimUhfService esimUhfService = null;
    public static final String TAG = "WriteTagActivity";
    private List<AssetsInfo> mData = new ArrayList<>();
    private WriteTagAdapter adapter;
    private boolean isNeedClearData;
    private String preFilter = "";
    private int currentPage = 1;
    private int pageSize = 10;
    List<FilterBean> mSortBeans = new ArrayList<>();
    List<FilterBean> mStatusBeans = new ArrayList<>();
    List<FilterBean> currentFilterBeans = new ArrayList<>();
    private FiltterAdapter filtterAdapter;
    private RecyclerView filerRecycler;
    CustomPopWindow mCustomPopWindow;

    @Override
    public AssetListPresenter initPresenter() {
        return new AssetListPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        initRfidAndEvent();
        inFilterList();
        initView();
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

    public void initView() {
        //初始化popupwindow
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_filter_layout, null);
        filtterAdapter = new FiltterAdapter(currentFilterBeans, this);
        filtterAdapter.setOnItemClickListener(this);
        filerRecycler = contentView.findViewById(R.id.rv_filter);
        filerRecycler.setLayoutManager(new LinearLayoutManager(this));
        filerRecycler.setAdapter(filtterAdapter);
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(false)
                .setAnimationStyle(R.style.popwin_anim)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        maskView.setVisibility(View.GONE);
                        mTvSort.setTextColor(getColor(R.color.repair_text));
                        mSortArrow.setImageResource(R.drawable.down_arrow_black);
                        mTvStatus.setTextColor(getColor(R.color.repair_text));
                        mStatusArrow.setImageResource(R.drawable.down_arrow_black);
                    }
                })
                .create();
    }

    private void inFilterList() {
        mSortBeans.add(new FilterBean("10", "默认排序", false));
        mSortBeans.add(new FilterBean("11", "资产编号正序", false));
        mSortBeans.add(new FilterBean("12", "资产编号倒序", false));
        mSortBeans.add(new FilterBean("13", "购入日期正序", false));
        mSortBeans.add(new FilterBean("14", "购入日期倒序", false));

        mStatusBeans.add(new FilterBean("20", "闲置", false));
        mStatusBeans.add(new FilterBean("21", "在用", false));
        mStatusBeans.add(new FilterBean("22", "借用", false));
        mStatusBeans.add(new FilterBean("23", "报废", false));
        mStatusBeans.add(new FilterBean("24", "审批中", false));
    }

    private void initRfidAndEvent() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_asset_list;
    }

    @OnClick({R.id.titleLeft, R.id.edit_search, R.id.sort_layout, R.id.tv_status})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft:
                finish();
                break;
            case R.id.edit_search:
                searchAssets();
                break;
            case R.id.sort_layout:
                currentFilterBeans.clear();
                currentFilterBeans.addAll(mSortBeans);
                filtterAdapter.notifyDataSetChanged();
                mCustomPopWindow.showAsDropDown(filterLayout);
                maskView.setVisibility(View.VISIBLE);
                mTvSort.setTextColor(getColor(R.color.titele_color));
                mSortArrow.setImageResource(R.drawable.down_arrow_blue);
                break;
            case R.id.status_layout:
                currentFilterBeans.clear();
                currentFilterBeans.addAll(mStatusBeans);
                filtterAdapter.notifyDataSetChanged();
                mCustomPopWindow.showAsDropDown(filterLayout);
                maskView.setVisibility(View.VISIBLE);
                mTvStatus.setTextColor(getColor(R.color.titele_color));
                mStatusArrow.setImageResource(R.drawable.down_arrow_blue);
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

    @Override
    public void onItemSelected(FilterBean filterBean) {

    }
}
