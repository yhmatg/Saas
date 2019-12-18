package com.common.esimrfid.ui.inventorytask;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.presenter.home.InvOrderPressnter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InventoryTaskActivity extends BaseActivity<InvOrderPressnter> implements InvOrderContract.View {
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
    InvTaskAdapter mAdapter;
    List<ResultInventoryOrder> mInvTaskorders = new ArrayList<>();
    List<ResultInventoryOrder> mUnFinishedTaskorders = new ArrayList<>();
    List<ResultInventoryOrder> mFinishedTaskorders = new ArrayList<>();
    List<ResultInventoryOrder> mShowTaskorders = new ArrayList<>();
    private Boolean isFirstOnResume = true;
    private String userId;

    @Override
    public InvOrderPressnter initPresenter() {
        return new InvOrderPressnter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.ast_inv);
        userId = getUserLoginResponse().getUserinfo().getId();
        mAdapter = new InvTaskAdapter(mShowTaskorders,this);
        mInvTaskRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mInvTaskRecycleview.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化或者更新本地盘点的状态
        if(isFirstOnResume){
            mPresenter.fetchAllIvnOrders(userId,true);
            isFirstOnResume = false;
        }else{
            mPresenter.fetchAllIvnOrders(userId,false);
        }

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
                mFinishStart.setTextColor(getColor(R.color.gray_text));
                mFinishedEnd.setTextColor(getColor(R.color.gray_text));
                mUnFinishStart.setTextColor(getColor(R.color.theme));
                mUnFinishedEnd.setTextColor(getColor(R.color.theme));
                break;
            case R.id.ll_finished:
                mShowTaskorders.clear();
                mShowTaskorders.addAll(mFinishedTaskorders);
                mAdapter.notifyDataSetChanged();
                mFinishStart.setTextColor(getColor(R.color.theme));
                mFinishedEnd.setTextColor(getColor(R.color.theme));
                mUnFinishStart.setTextColor(getColor(R.color.gray_text));
                mUnFinishedEnd.setTextColor(getColor(R.color.gray_text));
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.create_invtask:
                startActivity(new Intent(this,NewInventoryActivity.class));
                break;
        }
    }

    @Override
    public void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders) {
        mInvTaskorders.clear();
        mFinishedTaskorders.clear();
        mUnFinishedTaskorders.clear();
        mShowTaskorders.clear();
        mInvTaskorders.addAll(resultInventoryOrders);
        for (int i = 0; i < mInvTaskorders.size(); i++) {
            ResultInventoryOrder resultInventoryOrder = mInvTaskorders.get(i);
            if(resultInventoryOrder.getInv_status() == 10){
                mUnFinishedTaskorders.add(resultInventoryOrder);
            }else if(resultInventoryOrder.getInv_status() == 11){
                mFinishedTaskorders.add(resultInventoryOrder);
            }
        }
        mShowTaskorders.addAll(mUnFinishedTaskorders);
        mUnFinishedNum.setText(String.valueOf(mUnFinishedTaskorders.size()));
        mFinishedNum.setText(String.valueOf(mFinishedTaskorders.size()));
        mAdapter.notifyDataSetChanged();
    }

}
