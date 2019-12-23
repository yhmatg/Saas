package com.common.esimrfid.ui.inventorytask;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.InventoryStatus;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.presenter.home.InvOrderPressnter;
import com.common.esimrfid.ui.assetinventory.InvTaskAdapter;
import com.common.esimrfid.ui.assetinventory.NewInventoryActivity;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InventoryTaskActivity extends BaseActivity<InvOrderPressnter> implements InvOrderContract.View, InvAssetAdapter.OnItemClickListener {
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.rv_asset_inventory)
    RecyclerView mInvTaskRecycleview;
    InvAssetAdapter mAdapter;
    List<ResultInventoryOrder> mUnFinishedTaskorders = new ArrayList<>();
    private Boolean isFirstOnResume = true;
    private String userId;
    //盘点未提交过的条目
    private ArrayList<InventoryDetail> notSubmitInvDetails = new ArrayList<>();
    private ArrayList<String> notSubmitEpcList = new ArrayList<>();
    private  int mPosition;
    private MaterialDialog updateDialog;

    @Override
    public InvOrderPressnter initPresenter() {
        return new InvOrderPressnter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.inv_task);
        userId = getUserLoginResponse().getUserinfo().getId();
        mAdapter = new InvAssetAdapter(mUnFinishedTaskorders, this);
        mAdapter.setOnItemClickListener(this);
        mInvTaskRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mInvTaskRecycleview.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化或者更新本地盘点的状态
        if (isFirstOnResume) {
            mPresenter.fetchAllIvnOrders(userId, true);
            isFirstOnResume = false;
        } else {
            mPresenter.fetchAllIvnOrders(userId, false);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_asset_inventory;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.title_back})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    @Override
    public void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders) {
        mUnFinishedTaskorders.clear();
        for (int i = 0; i < resultInventoryOrders.size(); i++) {
            ResultInventoryOrder resultInventoryOrder = resultInventoryOrders.get(i);
            if (resultInventoryOrder.getInv_status() == 10) {
                mUnFinishedTaskorders.add(resultInventoryOrder);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    //盘点单中的资产
    @Override
    public void handleInvDetails(ResultInventoryDetail mInventoryDetail) {
        notSubmitInvDetails.clear();
        notSubmitEpcList.clear();
        List<InventoryDetail> detailResults = mInventoryDetail.getDetailResults();
        for (InventoryDetail detailResult : detailResults) {
            if (detailResult.getInvdt_status().getCode() == InventoryStatus.FINISH_NOT_SUBMIT.getIndex()) {
                notSubmitInvDetails.add(detailResult);
                notSubmitEpcList.add(detailResult.getAst_id());
            }
        }
        dismissUpdateDialog();
        if (CommonUtils.isNetworkConnected() && notSubmitInvDetails.size() > 0) {
            mPresenter.upLoadInvDetails(mInventoryDetail.getId(), notSubmitEpcList, notSubmitInvDetails, userId);
        }
    }
    //资产盘点上传结果
    @Override
    public void handelUploadResult(BaseResponse baseResponse) {
        if (baseResponse.isSuccess()) {
            ResultInventoryOrder resultInventoryOrder = mUnFinishedTaskorders.get(mPosition);
            resultInventoryOrder.setInv_notsubmit_count(0);
            //1223 start
          /*  Integer oldFinishCount = resultInventoryOrder.getInv_finish_count();
            resultInventoryOrder.setInv_finish_count(oldFinishCount + notSubmitEpcList.size() );*/
          //1223 end
            mAdapter.notifyItemChanged(mPosition);
            dismissUpdateDialog();
        }

    }

    @Override
    public void onSyncData(ResultInventoryOrder invOder, int position) {
        //网络可用情况下同步数据
        if (CommonUtils.isNetworkConnected()) {
            showUpdateDialog();
            mPosition = position;
            mPresenter.fetchAllInvDetails(invOder.getId(), true);
        }else {
            //todo
            ToastUtils.showShort("请检查网络是否可用");
        }


    }

    @Override
    public void onFinishInv(ResultInventoryOrder invOder) {

    }

    public void showUpdateDialog(){
        View contentView = LayoutInflater.from(this).inflate(R.layout.update_loading_dialog, null);
        updateDialog = new MaterialDialog.Builder(this)
                .customView(contentView, false)
                .show();
        Window window = updateDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = CommonUtils.dp2px(250);
        layoutParams.height = CommonUtils.dp2px(130);
        window.setAttributes(layoutParams);
    }

    public void dismissUpdateDialog(){
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
    }

}
