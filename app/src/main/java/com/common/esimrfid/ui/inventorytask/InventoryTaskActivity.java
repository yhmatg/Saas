package com.common.esimrfid.ui.inventorytask;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
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
import com.common.esimrfid.ui.home.BaseDialog;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InventoryTaskActivity extends BaseActivity<InvOrderPressnter> implements InvOrderContract.View, InvAssetAdapter.OnItemClickListener {
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.rv_asset_inventory)
    RecyclerView mInvTaskRecycleview;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.empty_page)
    LinearLayout mEmptyPage;
    InvAssetAdapter mAdapter;
    List<ResultInventoryOrder> mUnFinishedTaskorders = new ArrayList<>();
    private Boolean isFirstOnResume = true;
    private String userId;
    //盘点未提交过的条目
    private ArrayList<InventoryDetail> notSubmitInvDetails = new ArrayList<>();
    private ArrayList<String> notSubmitEpcList = new ArrayList<>();
    //已经盘点过的条目
    private int finishCount;
    private int totalCount;
    private int mPosition;
    private MaterialDialog updateDialog;
    private MaterialDialog finishDialog;
    private boolean isFinish = false;

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
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.fetchAllIvnOrders(userId, true);
            }
        });
        mRefreshLayout.setEnableAutoLoadMore(false);//使上拉加载具有弹性效果
        mRefreshLayout.setEnableOverScrollDrag(false);//禁止越界拖动（1.0.4以上版本）
        mRefreshLayout.setEnableOverScrollBounce(false);//关闭越界回弹功能
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
        mRefreshLayout.finishRefresh();
        if(mUnFinishedTaskorders.size() == 0){
            mEmptyPage.setVisibility(View.VISIBLE);
        }else {
            mEmptyPage.setVisibility(View.GONE);
        }

    }

    //盘点单中的资产
    @Override
    public void handleInvDetails(ResultInventoryDetail mInventoryDetail) {
        notSubmitInvDetails.clear();
        notSubmitEpcList.clear();
        finishCount = 0;
        totalCount = mInventoryDetail.getInv_total_count();
        List<InventoryDetail> detailResults = mInventoryDetail.getDetailResults();
        for (InventoryDetail detailResult : detailResults) {
            if (detailResult.getInvdt_status().getCode() == InventoryStatus.FINISH_NOT_SUBMIT.getIndex()) {
                notSubmitInvDetails.add(detailResult);
                notSubmitEpcList.add(detailResult.getAst_id());
            } else if (detailResult.getInvdt_status().getCode() == InventoryStatus.FINISH.getIndex()) {
                finishCount++;
            }
        }
        dismissUpdateDialog();
        //盘点数据上传服务器
        if (!isFinish && CommonUtils.isNetworkConnected() && notSubmitInvDetails.size() > 0) {
            mPresenter.upLoadInvDetails(mInventoryDetail.getId(), notSubmitEpcList, notSubmitInvDetails, userId);
        }
        //盘点数据上传服务器并且完成盘点单
        if (isFinish && CommonUtils.isNetworkConnected()) {
            int totalFinishCount = notSubmitEpcList.size() + finishCount;
            if (totalFinishCount < totalCount) {
                showFinishInvDialog(mInventoryDetail.getId(), notSubmitEpcList, notSubmitInvDetails, userId);
            }else {
                showUpdateDialog();
                mPresenter.finishInvOrderWithAsset(mInventoryDetail.getId(), notSubmitEpcList, notSubmitInvDetails, userId);
            }

        }
    }

    //资产盘点上传结果
    @Override
    public void handelUploadResult(BaseResponse baseResponse) {
        dismissUpdateDialog();
        if (baseResponse.isSuccess()) {
            ResultInventoryOrder resultInventoryOrder = mUnFinishedTaskorders.get(mPosition);
            resultInventoryOrder.setInv_notsubmit_count(0);
            //1223 start
          /*  Integer oldFinishCount = resultInventoryOrder.getInv_finish_count();
            resultInventoryOrder.setInv_finish_count(oldFinishCount + notSubmitEpcList.size() );*/
            //1223 end
            mAdapter.notifyItemChanged(mPosition);
            showConfirmDialog(false,true);
        }else {
            showConfirmDialog(false,false);
        }

    }

    @Override
    public void handelFinishInvOrder(BaseResponse baseResponse) {
        dismissUpdateDialog();
        if (baseResponse.isSuccess()) {
            mUnFinishedTaskorders.remove(mPosition);
            showConfirmDialog(true,true);
        }else {
            showConfirmDialog(true,false);
        }
    }

    @Override
    public void onSyncData(ResultInventoryOrder invOder, int position) {
        //网络可用情况下同步数据
        isFinish = false;
        if (CommonUtils.isNetworkConnected()) {
            showUpdateDialog();
            mPosition = position;
            mPresenter.fetchAllInvDetails(invOder.getId(), true);
        } else {
            //todo
            ToastUtils.showShort("请检查网络是否可用");
        }


    }

    @Override
    public void onFinishInv(ResultInventoryOrder invOder, int position) {
        //网络可用情况下完成盘点
        isFinish = true;
        if (CommonUtils.isNetworkConnected()) {
            mPosition = position;
            mPresenter.fetchAllInvDetails(invOder.getId(), true);
        } else {
            //todo
            ToastUtils.showShort("请检查网络是否可用");
        }
    }

    public void showUpdateDialog() {
        if(updateDialog != null){
            updateDialog.show();
        }else {
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

    }

    public void dismissUpdateDialog() {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
    }


    public void showFinishInvDialog(String orderId, List<String> invDetails, List<InventoryDetail> inventoryDetails, String uid) {
        if(finishDialog != null){
            finishDialog.show();
        }else {
            View contentView = LayoutInflater.from(this).inflate(R.layout.finish_inv_dialog, null);
            View cancleTv = contentView.findViewById(R.id.tv_cancel);
            View sureTv = contentView.findViewById(R.id.tv_sure);
            sureTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUpdateDialog();
                    mPresenter.finishInvOrderWithAsset(orderId, invDetails, inventoryDetails, uid);
                    finishDialog.dismiss();
                }
            });
            cancleTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishDialog.dismiss();
                }
            });
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .customView(contentView, false);
            finishDialog = builder.show();
            Window window = finishDialog.getWindow();
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = CommonUtils.dp2px(268);
            layoutParams.height = CommonUtils.dp2px(164);
            window.setAttributes(layoutParams);
        }
    }

    public void dismissFinishDialog() {
        if (finishDialog != null && finishDialog.isShowing()) {
            finishDialog.dismiss();
        }
    }

    public void showConfirmDialog(boolean isFinish,boolean isSuccss){
        BaseDialog baseDialog = new BaseDialog(this, R.style.BaseDialog, R.layout.finish_confirm_dialog);
        TextView context = baseDialog.findViewById(R.id.alert_context);
        Button btSure = baseDialog.findViewById(R.id.bt_confirm);
        if(isFinish){
            if(isSuccss){
                context.setText(R.string.finish_inv_confirm);
            }else {
                context.setText(R.string.finish_inv_fail);
            }
        }else {
            if (isSuccss) {
                context.setText(R.string.sync_data_confirm);
            } else {
                context.setText(R.string.sync_data_fail);
            }
        }
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseDialog.dismiss();
            }
        });
        baseDialog.show();
    }
}
