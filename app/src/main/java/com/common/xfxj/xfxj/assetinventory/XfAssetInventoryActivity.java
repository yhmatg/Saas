package com.common.xfxj.xfxj.assetinventory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.xfxj.R;
import com.common.xfxj.base.activity.BaseActivity;
import com.common.xfxj.contract.home.InvOrderContract;
import com.common.xfxj.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.xfxj.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.xfxj.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.xfxj.core.bean.nanhua.xfxj.XfResultInventoryOrder;
import com.common.xfxj.core.room.DbBank;
import com.common.xfxj.presenter.home.InvOrderPressnter;
import com.common.xfxj.ui.assetinventory.NewInventoryActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class XfAssetInventoryActivity extends BaseActivity<InvOrderPressnter> implements InvOrderContract.View, XfInvAssetAdapter.OnItemClickListener {
    @BindView(R.id.title_back)
    ImageView mBackImg;
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.finish_start)
    TextView mFinishStart;
    @BindView(R.id.unfinish_start)
    TextView mUnFinishStart;
    @BindView(R.id.tv_overdue)
    TextView mOverdueStart;
    @BindView(R.id.rv_inventory_task)
    RecyclerView mInvTaskRecycleview;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.empty_page)
    LinearLayout mEmptyPage;
    @BindView(R.id.divide_line)
    View lineView;
    XfInvAssetAdapter mAdapter;
    List<XfResultInventoryOrder> mInvTaskorders = new ArrayList<>();
    List<XfResultInventoryOrder> mUnFinishedTaskorders = new ArrayList<>();
    List<XfResultInventoryOrder> mFinishedTaskorders = new ArrayList<>();
    List<XfResultInventoryOrder> mOverdueTaskorders = new ArrayList<>();
    List<XfResultInventoryOrder> mShowTaskorders = new ArrayList<>();
    //private Boolean isFirstOnResume = true;
    private int showWitch = 0;;

    @Override
    public InvOrderPressnter initPresenter() {
        return new InvOrderPressnter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText("巡检任务");
        mAdapter = new XfInvAssetAdapter(mShowTaskorders, this);
        mAdapter.setOnItemClickListener(this);
        mInvTaskRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mInvTaskRecycleview.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
        });
        mRefreshLayout.setEnableAutoLoadMore(false);//使上拉加载具有弹性效果
        mRefreshLayout.setEnableOverScrollDrag(false);//禁止越界拖动（1.0.4以上版本）
        mRefreshLayout.setEnableOverScrollBounce(false);//关闭越界回弹功能
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.fetchXfAllIvnOrders();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.xf_activity_inventory_task;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.ll_unfinished, R.id.ll_finished, R.id.ll_overdue, R.id.title_back, R.id.create_invtask})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.ll_unfinished:
                mShowTaskorders.clear();
                mShowTaskorders.addAll(mUnFinishedTaskorders);
                mAdapter.notifyDataSetChanged();
                mFinishStart.setTextColor(getColor(R.color.home_text_one));
                mOverdueStart.setTextColor(getColor(R.color.home_text_one));
                mUnFinishStart.setTextColor(getColor(R.color.titele_color));
                showWitch = 0;
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
                mUnFinishStart.setTextColor(getColor(R.color.home_text_one));
                mOverdueStart.setTextColor(getColor(R.color.home_text_one));
                showWitch = 1;
                if (mShowTaskorders.size() == 0) {
                    mEmptyPage.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.GONE);
                } else {
                    mEmptyPage.setVisibility(View.GONE);
                    lineView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_overdue:
                mShowTaskorders.clear();
                mShowTaskorders.addAll(mOverdueTaskorders);
                mAdapter.notifyDataSetChanged();
                mOverdueStart.setTextColor(getColor(R.color.titele_color));
                mFinishStart.setTextColor(getColor(R.color.home_text_one));
                mUnFinishStart.setTextColor(getColor(R.color.home_text_one));
                showWitch = 2;
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
                startActivity(new Intent(this, NewInventoryActivity.class));
                break;
        }
    }

    @Override
    public void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders) {

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

    @Override
    public void showXfInvOrders(List<XfResultInventoryOrder> resultInventoryOrders) {
        mInvTaskorders.clear();
        mFinishedTaskorders.clear();
        mUnFinishedTaskorders.clear();
        mOverdueTaskorders.clear();
        mShowTaskorders.clear();
        mInvTaskorders.addAll(resultInventoryOrders);
        for (int i = 0; i < resultInventoryOrders.size(); i++) {
            XfResultInventoryOrder resultInventoryOrder = resultInventoryOrders.get(i);
            if (resultInventoryOrder.getInv_status() == 0) {
                mUnFinishedTaskorders.add(resultInventoryOrder);
            } else if (resultInventoryOrder.getInv_status() == 1) {
                mFinishedTaskorders.add(resultInventoryOrder);
            }else if(resultInventoryOrder.getInv_status() == 2){
                mOverdueTaskorders.add(resultInventoryOrder);
            }
        }
        if (showWitch == 0) {
            mShowTaskorders.addAll(mUnFinishedTaskorders);
        } else if(showWitch == 1){
            mShowTaskorders.addAll(mFinishedTaskorders);
        } else if(showWitch == 2){
            mShowTaskorders.addAll(mOverdueTaskorders);
        }
        mAdapter.notifyDataSetChanged();
        if (mShowTaskorders.size() == 0) {
            mEmptyPage.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.GONE);
        } else {
            mEmptyPage.setVisibility(View.GONE);
            lineView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSyncData(XfResultInventoryOrder invOder, int position) {

    }

    @Override
    public void onFinishInv(XfResultInventoryOrder invOder, int position) {
        invOder.setInv_status(1);
        DbBank.getInstance().getXfResultInventoryOrderDao().updateItem(invOder);
        mFinishedTaskorders.clear();
        mUnFinishedTaskorders.clear();
        mOverdueTaskorders.clear();
        mShowTaskorders.clear();
        for (int i = 0; i < mInvTaskorders.size(); i++) {
            XfResultInventoryOrder resultInventoryOrder = mInvTaskorders.get(i);
            if (resultInventoryOrder.getInv_status() == 0) {
                mUnFinishedTaskorders.add(resultInventoryOrder);
            } else if (resultInventoryOrder.getInv_status() == 1) {
                mFinishedTaskorders.add(resultInventoryOrder);
            }else if(resultInventoryOrder.getInv_status() == 2){
                mOverdueTaskorders.add(resultInventoryOrder);
            }
        }
        if (showWitch == 0) {
            mShowTaskorders.addAll(mUnFinishedTaskorders);
        } else if(showWitch == 1){
            mShowTaskorders.addAll(mFinishedTaskorders);
        } else if(showWitch == 2){
            mShowTaskorders.addAll(mOverdueTaskorders);
        }
        mAdapter.notifyDataSetChanged();
        if (mShowTaskorders.size() == 0) {
            mEmptyPage.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.GONE);
        } else {
            mEmptyPage.setVisibility(View.GONE);
            lineView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetailInv(XfResultInventoryOrder invOder, int position) {

    }
}
