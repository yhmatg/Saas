package com.common.esimrfid.ui.inventorytask;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.InventoryStatus;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.presenter.home.InvDetailPresenter;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InvdetialActivity extends BaseActivity<InvDetailPresenter> implements InvDetailContract.View {
    public static final String INV_ID = "inv_id";
    public static final String INV_NAME = "inv_name";
    private static final String INTENT_FROM = "intent_from";
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.invtask_title)
    TextView mInvTitle;
    @BindView(R.id.create_date)
    TextView mCreateDate;
    @BindView(R.id.expect_finish_date)
    TextView mExpFinishDate;
    @BindView(R.id.inv_all_num)
    TextView mAllNum;
    @BindView(R.id.unfinished_num)
    TextView mUnfinishedNum;
    //已经盘点或者未提交
    @BindView(R.id.inv_finished_num)
    TextView mNotSubmitNum;
    @BindView(R.id.tv_name)
    TextView mUnfinishOrNotsubmit;
    @BindView(R.id.bt_start_inv)
    Button mStartInv;
    @BindView(R.id.rv_inventory_detail)
    RecyclerView mInvDetailRecyclerView;
    private String mInvId;
    private String mInvName;
    private String userId;
    private String mFrom;
    //所有条目数据
    List<InventoryDetail> mInventoryDetails = new ArrayList<>();
    //已经盘点提交过的条目
    private ArrayList<InventoryDetail> checkedEpcList = new ArrayList<>();
    //盘点未提交过的条目
    private ArrayList<InventoryDetail> notSubmitInvDetails = new ArrayList<>();
    private ArrayList<String> notSubmitEpcList = new ArrayList<>();
    InvDetailAdapter mAdapter;
    private Boolean isFirstOnResume = true;
    //资产总数
    private int mTotalInventoried;
    //已经盘点资产
    private int mHasInventoried;
    //没有盘点的资产数目
    private int mNotInventoried;
    //盘点未提交资产
    private int mNotSubmit;

    @Override
    public InvDetailPresenter initPresenter() {
        return new InvDetailPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.inv_detail);
        userId = getUserLoginResponse().getUserinfo().getId();
        if (getIntent() != null) {
            mInvId = getIntent().getStringExtra(INV_ID);
            mInvName = getIntent().getStringExtra(INV_NAME);
            mFrom = getIntent().getStringExtra(INTENT_FROM);
            mInvTitle.setText(mInvName);
        }
        if("InvTaskAdapter".equals(mFrom)){
            mUnfinishOrNotsubmit.setText(R.string.asset_inventoried);
        }
        mAdapter = new InvDetailAdapter(mInventoryDetails, this);
        mInvDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mInvDetailRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstOnResume) {
            mPresenter.fetchAllInvDetails(mInvId, true);
        } else {
            mPresenter.fetchAllInvDetails(mInvId, false);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inventory_detail;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public void handleInvDetails(ResultInventoryDetail mInventoryDetail) {
        mHasInventoried = 0;
        mNotSubmit = 0;
        if (isFirstOnResume) {
            mCreateDate.setText(DateUtils.date2String(mInventoryDetail.getCreate_date()));
            mExpFinishDate.setText(DateUtils.date2String(mInventoryDetail.getInv_exptfinish_date()));
            mAllNum.setText(String.valueOf(mInventoryDetail.getInv_total_count()));
            mTotalInventoried = mInventoryDetail.getInv_total_count();
            isFirstOnResume = false;
        }
        mInventoryDetails.clear();
        mInventoryDetails.addAll(mInventoryDetail.getDetailResults());
        sortListByInvStatus(mInventoryDetails);
        mAdapter.notifyDataSetChanged();
        for (InventoryDetail inventoryDetail : mInventoryDetails) {
            if (inventoryDetail.getInvdt_status().getCode() == InventoryStatus.FINISH.getIndex()) {
                checkedEpcList.add(inventoryDetail);
                mHasInventoried++;
            } else if (inventoryDetail.getInvdt_status().getCode() == InventoryStatus.FINISH_NOT_SUBMIT.getIndex()) {
                notSubmitInvDetails.add(inventoryDetail);
                notSubmitEpcList.add(inventoryDetail.getAst_id());
                mNotSubmit++;
            }
        }
        mNotInventoried = mTotalInventoried - mHasInventoried - mNotSubmit;
        mUnfinishedNum.setText(String.valueOf(mNotInventoried));
        //1223 start
        if("InvTaskAdapter".equals(mFrom)){
            mNotSubmitNum.setText(String.valueOf(mHasInventoried + mNotSubmit));
        }else {
            mNotSubmitNum.setText(String.valueOf(mNotSubmit));
        }
        //1223 end
        //如果有网络直接提交本地盘点过的数据
        if (CommonUtils.isNetworkConnected() && notSubmitInvDetails.size() > 0) {
            mPresenter.upLoadInvDetails(mInvId, notSubmitEpcList, notSubmitInvDetails, userId);
        }

    }

    //资产列表排序，未盘点在前
    public void sortListByInvStatus(List<InventoryDetail> inventoryDetails) {
        Collections.sort(inventoryDetails, new Comparator<InventoryDetail>() {
            @Override
            public int compare(InventoryDetail t1, InventoryDetail t2) {
                return t1.getInvdt_status().getCode().compareTo(t2.getInvdt_status().getCode());
            }
        });
    }

    //上传盘点资产回调
    @Override
    public void handelUploadResult(BaseResponse baseResponse) {
        if (baseResponse.isSuccess()) {
            List<InventoryDetail> tempDataList = new ArrayList<>();
            tempDataList.addAll(mInventoryDetails);
            tempDataList.retainAll(notSubmitInvDetails);
            mHasInventoried += notSubmitInvDetails.size();
            mNotSubmit -= notSubmitInvDetails.size();
            mNotInventoried = mTotalInventoried - mHasInventoried - mNotSubmit;
            mUnfinishedNum.setText(String.valueOf(mNotInventoried));
            //1223 start
            if("InvTaskAdapter".equals(mFrom)){
                mNotSubmitNum.setText(String.valueOf(mHasInventoried + mNotSubmit));
            }else {
                mNotSubmitNum.setText(String.valueOf(mNotSubmit));
            }

            //1223 end
            notSubmitInvDetails.clear();
            for (InventoryDetail inventoryDetail : tempDataList) {
                inventoryDetail.getInvdt_status().setCode(InventoryStatus.FINISH.getIndex());
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void handelFinishInvorder(BaseResponse baseResponse) {

    }

    @Override
    public void uploadInvDetails(List<InventoryDetail> inventoryDetails) {

    }

    @OnClick({R.id.title_back, R.id.bt_start_inv})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.bt_start_inv:
                startScanActivity();
                break;
        }
    }

    private void startScanActivity() {
        Intent intent = new Intent();
        intent.putExtra(INV_ID, mInvId);
        intent.setClass(this, InventoryScanActivity.class);
        startActivity(intent);
    }
}
