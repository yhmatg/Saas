package com.common.esimrfid.ui.distribute;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.Constants;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.distribute.DistributeOrderContract;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DistributeOrder;
import com.common.esimrfid.presenter.distribute.DistributeOrderPresenter;
import com.common.esimrfid.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DistribureOrderActivity extends BaseActivity<DistributeOrderPresenter> implements DistributeOrderContract.View, DistributeOrderAdapter.OnItemClickListener {
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.rv_asset_inventory)
    RecyclerView mDistributeOrderRecycleview;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.empty_page)
    LinearLayout mEmptyPage;
    private List<DistributeOrder> mDistributeOrders = new ArrayList<>();
    private DistributeOrderAdapter mAdapter;
    private int currentPage = 1;
    private int pageSize = 10;
    private boolean isNeedClearData;

    @Override
    public DistributeOrderPresenter initPresenter() {
        return new DistributeOrderPresenter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.distribute_task);
        mAdapter = new DistributeOrderAdapter(this, mDistributeOrders);
        mAdapter.setOnItemClickListener(this);
        mDistributeOrderRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mDistributeOrderRecycleview.setAdapter(mAdapter);
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isNeedClearData = false;
                currentPage++;
                mPresenter.getDistributeOrderPage("", "", currentPage, pageSize);
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isNeedClearData = true;
                currentPage = 1;
                mPresenter.getDistributeOrderPage("", "", currentPage, pageSize);
            }
        });
        mRefreshLayout.setEnableAutoLoadMore(false);//使上拉加载具有弹性效果
        mRefreshLayout.setEnableOverScrollDrag(false);//禁止越界拖动（1.0.4以上版本）
        mRefreshLayout.setEnableOverScrollBounce(false);//关闭越界回弹功能
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_asset_inventory;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        isNeedClearData = true;
        mPresenter.getDistributeOrderPage("", "", currentPage, pageSize);
    }

    @Override
    public void handleDistributeOrderPage(List<DistributeOrder> distributeOrders) {
        if (isNeedClearData) {
            mDistributeOrders.clear();
            mRefreshLayout.finishRefresh();
        } else {
            mRefreshLayout.finishLoadMore();
        }
        mDistributeOrders.addAll(distributeOrders);
        if (mDistributeOrders.size() == 0) {
            mEmptyPage.setVisibility(View.VISIBLE);
        } else {
            mEmptyPage.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void handelRejectDistributeAsset(BaseResponse baseResponse) {
        if ("200000".equals(baseResponse.getCode())) {
            ToastUtils.showShort("驳回成功");
        } else {
            ToastUtils.showShort("驳回失败");
        }
    }

    @Override
    public void onRejectDist(DistributeOrder distributeOrder) {
        mPresenter.rejectDistributeAsset(distributeOrder.getId());
    }

    @Override
    public void onStartDist(DistributeOrder distributeOrder) {
        Intent intent = new Intent();
        boolean distStatus = false;
        distStatus = "领用已完成".equals(distributeOrder.getOdr_status()) || "领用已驳回".equals(distributeOrder.getOdr_status());
        intent.setClass(this, DistOrderDetailActivity.class);
        intent.putExtra(Constants.DIST_ORDER_ID, distributeOrder.getId());
        intent.putExtra(Constants.DIST_ORDER_IS_FINISH, distStatus);
        startActivity(intent);
    }

    @Override
    public void onItemClick(DistributeOrder distributeOrder) {
        Intent intent = new Intent();
        boolean distStatus = false;
        distStatus = "领用已完成".equals(distributeOrder.getOdr_status()) || "领用已驳回".equals(distributeOrder.getOdr_status());
        intent.setClass(this, DistOrderDetailActivity.class);
        intent.putExtra(Constants.DIST_ORDER_ID, distributeOrder.getId());
        intent.putExtra(Constants.DIST_ORDER_IS_FINISH, distStatus);
        startActivity(intent);
    }

    @OnClick({R.id.title_back})
    void perform(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }
}
