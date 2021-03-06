package com.common.esimrfid.ui.inventorytask;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.presenter.home.InvOrderPressnter;
import com.common.esimrfid.ui.home.BaseDialog;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.ToastUtils;
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
    private int mPosition;
    private MaterialDialog updateDialog;
    private MaterialDialog finishDialog;
    private boolean isFinish = false;
    //modify 1228 start
    private String finishInvId;
    //modify 1228 end
    private int currentPage = 1;
    private int pageSize = 10;
    private boolean isNeedClearData;
    private int invordersSize = 0;
    private ResultInventoryOrder selectedInvOrder;
    private int selectPostion;

    @Override
    public InvOrderPressnter initPresenter() {
        return new InvOrderPressnter();
    }

    @Override
    protected void initEventAndData() {
        EsimAndroidApp.activityFrom = "InventoryTaskActivity";
        mTitle.setText(R.string.inv_task);
        userId = getUserLoginResponse().getUserinfo().getId();
        mAdapter = new InvAssetAdapter(mUnFinishedTaskorders, this);
        mAdapter.setOnItemClickListener(this);
        mInvTaskRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mInvTaskRecycleview.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isNeedClearData = true;
                currentPage = 1;
                mPresenter.fetchAllIvnOrdersPage(pageSize,1,0,userId,true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isNeedClearData = false;
                currentPage ++;
                mPresenter.fetchAllIvnOrdersPage(pageSize,currentPage,invordersSize,userId,true);
            }
        });
        mRefreshLayout.setEnableAutoLoadMore(false);//?????????????????????????????????
        mRefreshLayout.setEnableOverScrollDrag(false);//?????????????????????1.0.4???????????????
        mRefreshLayout.setEnableOverScrollBounce(false);//????????????????????????
        mPresenter.fetchAllIvnOrdersPage(pageSize,currentPage,0,userId,true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //??????????????????????????????????????????
        //isNeedClearData = true;
        //mPresenter.fetchAllIvnOrdersPage(pageSize,1,0,userId,true);
        if(selectedInvOrder != null){
            ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(selectedInvOrder.getId());
            if(invOrderByInvId != null){
                selectedInvOrder.setInv_finish_count(invOrderByInvId.getInv_finish_count());
                selectedInvOrder.setInv_notsubmit_count(invOrderByInvId.getInv_notsubmit_count());
                mAdapter.notifyItemChanged(selectPostion);
            }
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
        if(isNeedClearData){
            mRefreshLayout.finishRefresh();
            mUnFinishedTaskorders.clear();
            invordersSize = 0;
        }else {
            mRefreshLayout.finishLoadMore();
        }
        invordersSize += resultInventoryOrders.size();
        for (int i = 0; i < resultInventoryOrders.size(); i++) {
            ResultInventoryOrder resultInventoryOrder = resultInventoryOrders.get(i);
            if(resultInventoryOrder.getInv_total_count() == 0){
                continue;
            }
            if (resultInventoryOrder.getInv_status() == 10) {
                mUnFinishedTaskorders.add(resultInventoryOrder);
            }
        }
        Collections.sort(mUnFinishedTaskorders, new Comparator<ResultInventoryOrder>() {
            @Override
            public int compare(ResultInventoryOrder o1, ResultInventoryOrder o2) {
                return o2.getCreate_date().compareTo(o1.getCreate_date());
            }
        });
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
        if (mUnFinishedTaskorders.size() == 0) {
            mEmptyPage.setVisibility(View.VISIBLE);
        } else {
            mEmptyPage.setVisibility(View.GONE);
        }

    }

    //????????????????????????
    @Override
    public void handleInvDetails(ResultInventoryDetail mInventoryDetail) {

    }

    //????????????????????????
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
            showConfirmDialog(false, true);
        } else {
            showConfirmDialog(false, false);
        }

    }

    @Override
    public void handelFinishInvOrder(BaseResponse baseResponse) {
        dismissUpdateDialog();
        if (baseResponse.isSuccess()) {
            mUnFinishedTaskorders.remove(mPosition);
            mAdapter.notifyDataSetChanged();
            if (mUnFinishedTaskorders.size() == 0) {
                mEmptyPage.setVisibility(View.VISIBLE);
            } else {
                mEmptyPage.setVisibility(View.GONE);
            }
            showConfirmDialog(true, true);
        } else {
            showConfirmDialog(true, false);
        }
    }

    @Override
    public void handleNotInvAssetLeftStatus(Boolean isAllInved) {
        if(isAllInved){
            mPresenter.finishLocalInvDetailStat(finishInvId,userId);
        }else {
            showFinishInvDialog();
        }
    }

    @Override
    public void onSyncData(ResultInventoryOrder invOder, int position) {
        //?????????????????????????????????
        isFinish = false;
        if (CommonUtils.isNetworkConnected()) {
            showUpdateDialog();
            mPosition = position;
            //?????????????????????????????????
            mPresenter.fetchAllInvDetails(invOder.getId(), true);
            //???????????????????????????
            mPresenter.uploadLocalInvDetailState(invOder.getId(),userId);
        } else {
            //todo
            ToastUtils.showShort("???????????????????????????");
        }
    }

    @Override
    public void onFinishInv(ResultInventoryOrder invOder, int position) {
        //?????????????????????????????????
        isFinish = true;
        if (CommonUtils.isNetworkConnected()) {
            mPosition = position;
            finishInvId = invOder.getId();
            mPresenter.getNotInvAssetLeftStatus(invOder.getId());
        } else {
            //todo
            ToastUtils.showShort("???????????????????????????");
        }
    }

    @Override
    public void onDetailInv(ResultInventoryOrder invOder, int position) {
        selectedInvOrder = invOder;
        selectPostion = position;
    }


    public void showUpdateDialog() {
        if (updateDialog != null) {
            updateDialog.show();
        } else {
            View contentView = LayoutInflater.from(this).inflate(R.layout.update_loading_dialog, null);
            updateDialog = new MaterialDialog.Builder(this)
                    .customView(contentView, false)
                    .show();
            Window window = updateDialog.getWindow();
            window.setBackgroundDrawableResource(android.R.color.transparent);
          /*  WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = CommonUtils.dp2px(250);
            layoutParams.height = CommonUtils.dp2px(130);
            window.setAttributes(layoutParams);*/
        }

    }

    public void dismissUpdateDialog() {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
    }


    public void showFinishInvDialog() {
        if (finishDialog != null) {
            finishDialog.show();
        } else {
            View contentView = LayoutInflater.from(this).inflate(R.layout.finish_inv_dialog, null);
            View cancleTv = contentView.findViewById(R.id.tv_cancel);
            View sureTv = contentView.findViewById(R.id.tv_sure);
            sureTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUpdateDialog();
                    mPresenter.finishLocalInvDetailStat(finishInvId,userId);
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
           /* WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = CommonUtils.dp2px(268);
            layoutParams.height = CommonUtils.dp2px(164);
            window.setAttributes(layoutParams);*/
        }
    }

    public void dismissFinishDialog() {
        if (finishDialog != null && finishDialog.isShowing()) {
            finishDialog.dismiss();
        }
    }

    public void showConfirmDialog(boolean isFinish, boolean isSuccss) {
        BaseDialog baseDialog = new BaseDialog(this, R.style.BaseDialog, R.layout.finish_confirm_dialog);
        TextView context = baseDialog.findViewById(R.id.alert_context);
        Button btSure = baseDialog.findViewById(R.id.bt_confirm);
        if (isFinish) {
            if (isSuccss) {
                context.setText(R.string.finish_inv_confirm);
            } else {
                context.setText(R.string.finish_inv_fail);
            }
        } else {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EsimAndroidApp.activityFrom = "";
    }
}
