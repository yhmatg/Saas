package com.common.esimrfid.ui.requisition;

import android.graphics.Color;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.RequisitionContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionItemInfo;
import com.common.esimrfid.presenter.home.RequisitionPressnter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RequisitionActivity extends BaseActivity<RequisitionPressnter> implements RequisitionContract.View {

    @BindView(R.id.imgTitleLeft)
    ImageView imgTitleLeft;
    @BindView(R.id.error_page)
    LinearLayout errorPage;
    @BindView(R.id.empty_page)
    LinearLayout emptyPage;
    @BindView(R.id.tvTitleCenter)
    TextView tvTitleCenter;
    @BindView(R.id.tvTitleRight)
    TextView tvTitleRight;
    @BindView(R.id.twinklingRefreshLayout)
    TwinklingRefreshLayout mTkrefreshlayout;
    @BindView(R.id.requisition_recycleview)
    RecyclerView mReqRecycle;
    @BindView(R.id.tvMyOrderWaiting)
    TextView tvMyOrderWaiting;
    @BindView(R.id.lineMyOrderWaiting)
    View lineMyOrderWaiting;
    @BindView(R.id.tvMyOrderFinish)
    TextView tvMyOrderFinish;
    @BindView(R.id.lineMyOrderFinish)
    View lineMyOrderFinish;
    private RequisitionListAdapter resAdapter;
    private List<RequisitionItemInfo> currentRequisition = new ArrayList<>();
    private List<RequisitionItemInfo> finishedRequisitions = new ArrayList<>();
    private List<RequisitionItemInfo> originalRequisitions = new ArrayList<>();
    private boolean mShowNotFinishd = false;


    @Override
    public RequisitionPressnter initPresenter() {
        return new RequisitionPressnter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText("更新");
        tvTitleCenter.setText("资产领用");
        imgTitleLeft.setVisibility(View.VISIBLE);
        resAdapter = new RequisitionListAdapter(this,currentRequisition);
        mReqRecycle.setLayoutManager(new LinearLayoutManager(this));
        mReqRecycle.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mReqRecycle.setAdapter(resAdapter );
        mPresenter.fetchAllRequisitions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mPresenter.fetchAllRequisitions();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_requisition;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.imgTitleLeft, R.id.tvTitleRight, R.id.tvMyOrderWaiting,  R.id.tvMyOrderFinish})
    public void performClick(View view) {
        switch (view.getId()) {
            case R.id.imgTitleLeft:
                finish();
                break;
            case R.id.tvTitleRight:
                mPresenter.fetchAllRequisitions();
                break;
            case R.id.tvMyOrderWaiting:
                mShowNotFinishd = true;
                updateUI(tvMyOrderWaiting, tvMyOrderFinish, lineMyOrderWaiting, lineMyOrderFinish);
                resAdapter.setRefreshDataList(originalRequisitions);
                if(originalRequisitions.size() == 0){
                    emptyPage.setVisibility(View.VISIBLE);
                    mReqRecycle.setVisibility(View.GONE);
                }else {
                    emptyPage.setVisibility(View.GONE);
                    mReqRecycle.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tvMyOrderFinish:
                mShowNotFinishd = false;
                updateUI(tvMyOrderFinish, tvMyOrderWaiting, lineMyOrderFinish, lineMyOrderWaiting);
                resAdapter.setRefreshDataList(finishedRequisitions);
                if(finishedRequisitions.size() == 0){
                    emptyPage.setVisibility(View.VISIBLE);
                    mReqRecycle.setVisibility(View.GONE);
                }else {
                    emptyPage.setVisibility(View.GONE);
                    mReqRecycle.setVisibility(View.VISIBLE);
                }
                break;

        }

    }

    private void updateUI(TextView tv1, TextView tv2, View line1, View line2) {

        tv1.setTextColor(Color.parseColor("#30b4f8"));
        line1.setVisibility(View.VISIBLE);

        tv2.setTextColor(Color.parseColor("#88d1fc"));
        line2.setVisibility(View.GONE);

    }

    @Override
    public void handleRequisitionsItems(List<RequisitionItemInfo> requisitionItemInfos) {
        finishedRequisitions.clear();
        originalRequisitions.clear();
        currentRequisition.clear();
        for (int i = 0; i < requisitionItemInfos.size(); i++) {
            RequisitionItemInfo item = requisitionItemInfos.get(i);
            if("已完成".equals(item.getOdr_status())) {
                finishedRequisitions.add(item);
            }/*else if("处理中".equals(item.getOdr_status())){
                originalRequisitions.add(item);
            }*/
        }
        if(mShowNotFinishd){
            currentRequisition.addAll(originalRequisitions);
        }else {
            currentRequisition.addAll(finishedRequisitions);
        }
        resAdapter.notifyDataSetChanged();
        if(currentRequisition.size() == 0){
            emptyPage.setVisibility(View.VISIBLE);
            mReqRecycle.setVisibility(View.GONE);
        }else {
            emptyPage.setVisibility(View.GONE);
            mReqRecycle.setVisibility(View.VISIBLE);
        }
    }
}
