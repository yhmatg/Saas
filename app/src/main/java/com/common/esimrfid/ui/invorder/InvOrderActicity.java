package com.common.esimrfid.ui.invorder;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.InvOrder;
import com.common.esimrfid.presenter.home.InvOrderPressnter;
import com.common.esimrfid.ui.invdetail.InvDetailActivity;
import com.common.esimrfid.utils.ToastUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class InvOrderActicity extends BaseActivity<InvOrderPressnter> implements InvOrderContract.View {

    @BindView(R.id.twinklingRefreshLayout)
    TwinklingRefreshLayout mTkrefreshlayout;
    @BindView(R.id.innorder_recycle)
    RecyclerView mOrderRecycle;
    @BindView(R.id.tvTitleLeft)
    TextView mBackBut;
    private InvOrderAdapter mAdapter;
    private List<InvOrder> mInvOrders = new ArrayList<>();
    private int loadMode = 1;

    @Override
    public InvOrderPressnter initPresenter() {
        return new InvOrderPressnter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        mOrderRecycle.setLayoutManager(new LinearLayoutManager(this));
        mTkrefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                loadMode = 1;
                mPresenter.fetchAllIvnOrders();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                loadMode = 2;
                mPresenter.fetchAllIvnOrders();
            }
        });
        mAdapter = new InvOrderAdapter(mInvOrders, this);
        mAdapter.setOnItemClickListener(new InvOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent detailIntend = new Intent(InvOrderActicity.this, InvDetailActivity.class);
               // Bundle mBundle = new Bundle();
                //mBundle.putString("orderId", mInvOrders.get(position).getId());
                detailIntend.putExtra("orderId",mInvOrders.get(position).getId());
                startActivity(detailIntend);
            }

            @Override
            public void onItemLongClick(int position) {
                ToastUtils.showShort(mInvOrders.get(position).getInvCode());
            }

            @Override
            public void onRightImgClick(int position) {
                mPresenter.downloadInvOrders(mInvOrders.get(position).getId(),mInvOrders.get(position));
            }
        });
        mOrderRecycle.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mOrderRecycle.setAdapter(mAdapter);
        mPresenter.fetchAllIvnOrders();

    }

    @OnClick({R.id.tvTitleLeft})
    public void performClick(View view){
        switch (view.getId()){
            case R.id.tvTitleLeft:
                finish();
                break;

        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inv_order;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public void loadInvOrders(List<InvOrder> invOrders) {
        if(loadMode == 1){
            mInvOrders.clear();
            mInvOrders.addAll(invOrders);
            mAdapter.setRefreshDataList(mInvOrders);
            mTkrefreshlayout.finishRefreshing();
        }else if(loadMode == 2){
            mInvOrders.addAll(invOrders);
            mAdapter.setRefreshDataList(mInvOrders);
            mTkrefreshlayout.finishLoadmore();
        }

    }

    @Override
    public void upDateDownState() {
        mAdapter.notifyDataSetChanged();
    }
}
