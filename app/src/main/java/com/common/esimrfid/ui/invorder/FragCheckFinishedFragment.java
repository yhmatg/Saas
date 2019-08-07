package com.common.esimrfid.ui.invorder;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.fragment.BaseFragment;
import com.common.esimrfid.contract.home.FragCheckWaitingContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.OrderStatusEm;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;
import com.common.esimrfid.presenter.home.FragCheckFinishedPressnter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.view.View.GONE;

public class FragCheckFinishedFragment extends BaseFragment<FragCheckFinishedPressnter> implements FragCheckWaitingContract.View {

    @BindView(R.id.twinklingRefreshLayout)
    TwinklingRefreshLayout mTkrefreshlayout;
    @BindView(R.id.frag_recycleview)
    RecyclerView mFragRecycle;
    @BindView(R.id.empty_res)
    TextView emptyRes;

    private static final String TAG = "Frag_check_waiting";
    private InvsAdapter mAdapter;
    private List<ResultInventoryOrder> mData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.frag_listview;
    }

    @Override
    protected void initEventAndData() {
        mAdapter = new InvsAdapter(mData, mContext);
        mFragRecycle.setLayoutManager(new LinearLayoutManager(mContext));
        mFragRecycle.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
        mFragRecycle.setAdapter(mAdapter);
    }

    @Override
    public void showDialog(String title) {

    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public FragCheckFinishedPressnter initPresenter() {
        return new FragCheckFinishedPressnter(DataManager.getInstance());
    }

    @Override
    public void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders) {
        mData.clear();
        for (int i = 0; i < resultInventoryOrders.size(); i++) {
            if (resultInventoryOrders.get(i) != null
                    && resultInventoryOrders.get(i).getInv_status().getIndex() == OrderStatusEm.FINISH.getIndex())
                mData.add(resultInventoryOrders.get(i));
        }
        if (mData.size() == 0) {
            mTkrefreshlayout.setVisibility(GONE);
            emptyRes.setVisibility(View.VISIBLE);
            emptyRes.setText("暂无已完成盘点单");
        } else {
            mTkrefreshlayout.setVisibility(View.VISIBLE);
            emptyRes.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

}
