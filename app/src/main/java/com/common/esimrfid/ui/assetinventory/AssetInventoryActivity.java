package com.common.esimrfid.ui.assetinventory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.presenter.home.InvOrderPressnter;
import com.common.esimrfid.utils.CommonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AssetInventoryActivity extends BaseActivity<InvOrderPressnter> implements InvOrderContract.View {
    @BindView(R.id.title_back)
    ImageView mBackImg;
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.tv_unfinished)
    TextView mUnFinishedNum;
    @BindView(R.id.tv_finished)
    TextView mFinishedNum;
    @BindView(R.id.finish_start)
    TextView mFinishStart;
    @BindView(R.id.finish_end)
    TextView mFinishedEnd;
    @BindView(R.id.unfinish_start)
    TextView mUnFinishStart;
    @BindView(R.id.unfinish_end)
    TextView mUnFinishedEnd;
    @BindView(R.id.rv_inventory_task)
    RecyclerView mInvTaskRecycleview;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.empty_page)
    LinearLayout mEmptyPage;
    @BindView(R.id.divide_line)
    View lineView;
    InvTaskAdapter mAdapter;
    //List<ResultInventoryOrder> mInvTaskorders = new ArrayList<>();
    List<ResultInventoryOrder> mUnFinishedTaskorders = new ArrayList<>();
    List<ResultInventoryOrder> mFinishedTaskorders = new ArrayList<>();
    List<ResultInventoryOrder> mShowTaskorders = new ArrayList<>();
    //private Boolean isFirstOnResume = true;
    private String userId;
    private Boolean isShowUnfinish = true;
    private int currentPage = 1;
    private int pageSize = 10;
    private boolean isNeedClearData;
    private int invordersSize = 0;

    @Override
    public InvOrderPressnter initPresenter() {
        return new InvOrderPressnter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.ast_inv);
        userId = getUserLoginResponse().getUserinfo().getId();
        mAdapter = new InvTaskAdapter(mShowTaskorders, this);
        mInvTaskRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mInvTaskRecycleview.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isNeedClearData = true;
                currentPage = 1;
                //mPresenter.fetchAllIvnOrders(userId, true);
                mPresenter.fetchAllIvnOrdersPage(pageSize, 1, 0, userId, true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isNeedClearData = false;
                currentPage++;
                mPresenter.fetchAllIvnOrdersPage(pageSize, currentPage, invordersSize, userId, true);
            }
        });
        mRefreshLayout.setEnableAutoLoadMore(false);//使上拉加载具有弹性效果
        mRefreshLayout.setEnableOverScrollDrag(false);//禁止越界拖动（1.0.4以上版本）
        mRefreshLayout.setEnableOverScrollBounce(false);//关闭越界回弹功能
        //mPresenter.fetchAllIvnOrdersPage(pageSize, 1, 0, userId, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化或者更新本地盘点的状态
        /*if (isFirstOnResume) {
            mPresenter.fetchAllIvnOrders(userId, true);
            isFirstOnResume = false;
        } else {
            mPresenter.fetchAllIvnOrders(userId, true);
        }*/
       /* isNeedClearData = true;
        currentPage = 1;
        mPresenter.fetchAllIvnOrdersPage(pageSize,1,0,userId,true);*/
        mPresenter.fetchAllIvnOrdersPage(pageSize, 1, 0, userId, true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inventory_task;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.ll_unfinished, R.id.ll_finished, R.id.title_back, R.id.create_invtask})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.ll_unfinished:
                mShowTaskorders.clear();
                mShowTaskorders.addAll(mUnFinishedTaskorders);
                mAdapter.notifyDataSetChanged();
                mFinishStart.setTextColor(getColor(R.color.home_text_one));
                mFinishedEnd.setTextColor(getColor(R.color.home_text_one));
                mFinishedNum.setTextColor(getColor(R.color.home_text_one));
                mUnFinishStart.setTextColor(getColor(R.color.titele_color));
                mUnFinishedEnd.setTextColor(getColor(R.color.titele_color));
                mUnFinishedNum.setTextColor(getColor(R.color.titele_color));
                isShowUnfinish = true;
                if (mShowTaskorders.size() == 0) {
                    mEmptyPage.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.GONE);
                } else {
                    mEmptyPage.setVisibility(View.GONE);
                    lineView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_finished:
                mShowTaskorders.clear();
                mShowTaskorders.addAll(mFinishedTaskorders);
                mAdapter.notifyDataSetChanged();
                mFinishStart.setTextColor(getColor(R.color.titele_color));
                mFinishedEnd.setTextColor(getColor(R.color.titele_color));
                mFinishedNum.setTextColor(getColor(R.color.titele_color));
                mUnFinishStart.setTextColor(getColor(R.color.home_text_one));
                mUnFinishedEnd.setTextColor(getColor(R.color.home_text_one));
                mUnFinishedNum.setTextColor(getColor(R.color.home_text_one));
                isShowUnfinish = false;
                if (mShowTaskorders.size() == 0) {
                    mEmptyPage.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.GONE);
                } else {
                    mEmptyPage.setVisibility(View.GONE);
                    lineView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.create_invtask:
                if (CommonUtils.isNormalClick()) {
                    if(CommonUtils.isNetworkConnected()){
                        startActivity(new Intent(this, NewInventoryActivity.class));
                    }else {
                        showNoInternetDialog();
                    }
                }
                break;
        }
    }

    @Override
    public void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders) {
        Collections.sort(resultInventoryOrders, new Comparator<ResultInventoryOrder>() {
            @Override
            public int compare(ResultInventoryOrder o1, ResultInventoryOrder o2) {
                return o2.getCreate_date().compareTo(o1.getCreate_date());
            }
        });
        if (isNeedClearData) {
            mRefreshLayout.finishRefresh();
            mFinishedTaskorders.clear();
            mUnFinishedTaskorders.clear();
            invordersSize = 0;
        } else {
            mRefreshLayout.finishLoadMore();
        }
        mShowTaskorders.clear();
        invordersSize += resultInventoryOrders.size();
        for (int i = 0; i < resultInventoryOrders.size(); i++) {
            ResultInventoryOrder resultInventoryOrder = resultInventoryOrders.get(i);
            if (resultInventoryOrder.getInv_status() == 10) {
                mUnFinishedTaskorders.add(resultInventoryOrder);
            } else if (resultInventoryOrder.getInv_status() == 11) {
                mFinishedTaskorders.add(resultInventoryOrder);
            }
        }
        if (isShowUnfinish) {
            mShowTaskorders.addAll(mUnFinishedTaskorders);
        } else {
            mShowTaskorders.addAll(mFinishedTaskorders);
        }
        mUnFinishedNum.setText(String.valueOf(mUnFinishedTaskorders.size()));
        mFinishedNum.setText(String.valueOf(mFinishedTaskorders.size()));
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
        if (mShowTaskorders.size() == 0) {
            mEmptyPage.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.GONE);
        } else {
            mEmptyPage.setVisibility(View.GONE);
            lineView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void handleInvDetails(ResultInventoryDetail mInventoryDetail) {

    }

    @Override
    public void handelUploadResult(BaseResponse baseResponse) {

    }

    @Override
    public void handelFinishInvOrder(BaseResponse baseResponse) {

    }

    @Override
    public void handleNotInvAssetLeftStatus(Boolean isAllInved) {

    }

}
