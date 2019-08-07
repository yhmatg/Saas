package com.common.esimrfid.ui.invdetail;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.InventoryStatus;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.UserLoginResponse;
import com.common.esimrfid.core.bean.nanhua.invdetailbeans.InventoryDetail;
import com.common.esimrfid.presenter.home.InvDetailPresenter;
import com.common.esimrfid.uhf.EsimUhfAbstractService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.uhf.ZebraUhfServiceImpl;
import com.common.esimrfid.utils.ToastUtils;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InvdetailActivity extends BaseActivity<InvDetailPresenter> implements InvDetailContract.View {
    public static final String TAG = "InvdetailActivity";
    public static final String INV_ID = "inv_id";
    public static final String INV_STATUS = "inv_status";
    public static final String INV_TOTAL_COUNT = "INV_TOTAL_COUNT";
    public static final String INV_FININSHED_COUNT = "INV_FININSHED_COUNT";
    public static final String STATUS_FINISH = "STATUS_FINISH";
    public static final String STATUS_INIT = "STATUS_INIT";
    private String userId;
    private String mInvId;
    private String mStatus;
    private int mTotalInventoried;
    private int mOrignHasInvenroid;
    private int mHasInventorid;
    private int mNotInventoried;
    private InvDetailAdapter mAdapter;
    //所有条目数据
    private List<InventoryDetail> mDataList = new ArrayList<>();
    //已经盘点过的条目id
    private ArrayList<String> checkedEpcList = new ArrayList<>();
    //已经盘点过的条目
    private List<InventoryDetail> mLastInvDataList = new ArrayList<>();
    //所有扫描盘点到的数据
    private List<InventoryDetail> mUpdateInvDataList = new ArrayList<>();
    //最近一次扫描盘点到的数据
    private List<InventoryDetail> mResnentUpdateInvDataList = new ArrayList<>();
    private  int mUpdateInvDataListSize;
    private boolean mDataChanged;
    private boolean mEnable = true;
    EsimUhfAbstractService esimUhfService=null;

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
    @BindView(R.id.openBtn)
    LinearLayout openBtn;
    @BindView(R.id.clearBtn)
    LinearLayout clearBtn;
    @BindView(R.id.openimg)
    ImageView openimg;
    @BindView(R.id.saveBtn)
    LinearLayout saveBtn;
    @BindView(R.id.btn_submit)
    FloatingActionButton btnSubmit;
    @BindView(R.id.tv_inventory_owner)
    TextView tvInventoryOwner;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_not_inventory_num)
    TextView tvNotInventoryNum;
    @BindView(R.id.tv_inventoried_num)
    TextView tvInventoriedNum;
    @BindView(R.id.ll_bottom_bar)
    LinearLayout llBottomBar;
    @BindView(R.id.ll_init)
    LinearLayout llInit;
    @BindView(R.id.ll_finish)
    LinearLayout llFinish;
    @BindView(R.id.ll_current)
    LinearLayout llCurrent;
    @BindView(R.id.tv_current_ast)
    TextView tvCurrentAst;
    @BindView(R.id.twinklingRefreshLayout)
    TwinklingRefreshLayout mTkrefreshlayout;
    @BindView(R.id.invdetail_recycleview)
    RecyclerView mInvdetailRecycle;

    @Override
    public InvDetailPresenter initPresenter() {
        return new InvDetailPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        initRfidAndEventbus();
        if (getIntent() != null) {
            mInvId = getIntent().getStringExtra(INV_ID);
            mStatus = getIntent().getStringExtra(INV_STATUS);
            mTotalInventoried = getIntent().getIntExtra(INV_TOTAL_COUNT, -1);
            mOrignHasInvenroid =  getIntent().getIntExtra(INV_FININSHED_COUNT, -1);
            mHasInventorid = mOrignHasInvenroid;
            mNotInventoried = mTotalInventoried - mHasInventorid;
        }
        userId = EsimAndroidApp.getInstance().getUserLoginResponse().getSysUser().getId();
        //refresTitle();
        tvTitleCenter.setText("扫描盘点");
        imgTitleLeft.setVisibility(View.VISIBLE);
        mAdapter = new InvDetailAdapter(mDataList, this);
        mInvdetailRecycle.setLayoutManager(new LinearLayoutManager(this));
        mInvdetailRecycle.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mInvdetailRecycle.setAdapter(mAdapter);
        //本地获取数据库
        mPresenter.fetchAllInvDetails(mInvId,false);
    }

    private void initRfidAndEventbus() {
        //esimUhfService = new RodinbellUhfServiceImpl();
        esimUhfService = new ZebraUhfServiceImpl();
        esimUhfService.initRFID();
        EventBus.getDefault().register(this);
    }

    //更新盘点数目状态
    private void refresTitle() {
        // 总数
        tvTotal.setText(String.valueOf(mTotalInventoried));
        // 盘点人
        UserLoginResponse uerLogin = DataManager.getInstance().getUserLoginResponse();
        tvInventoryOwner.setText(uerLogin.getSysUser().getUser_real_name());
        // 已盘点
        tvInventoriedNum.setText(String.valueOf(mHasInventorid));
        // 未盘点
        tvNotInventoryNum.setText(String.valueOf(mNotInventoried));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inv_details;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public void handleInvDetails(List<InventoryDetail> InvDetails) {
        for (InventoryDetail invDetail : InvDetails) {
            if(invDetail.getInvdt_status().getCode() == InventoryStatus.FINISH.getIndex() ){
                mHasInventorid++;
                mNotInventoried--;
            }
        }
        refresTitle();
        upDateUi(InvDetails);
    }

    @Override
    public void handelUploadResult(BaseResponse baseResponse) {
        if(baseResponse.isSuccess()){
            mDataChanged = false;
            ToastUtils.showShort("提交成功！");
        }else {
            ToastUtils.showShort("提交失败!");
        }
    }

    @Override
    public void handelFinishInvorder(BaseResponse baseResponse) {
        if(baseResponse.isSuccess()){
            ToastUtils.showShort("提交成功！");
        }else {
            ToastUtils.showShort("提交失败!");
        }
    }

    @Override
    public void uploadInvDetails(List<InventoryDetail> inventoryDetails) {
        if(inventoryDetails == null || inventoryDetails.size() == 0){
            ToastUtils.showShort("未发现已盘点产品");
            return;
        }
        List<String> finishedOdIds = new ArrayList<>();
        List<InventoryDetail> finishedInvOrders = new ArrayList<>();
        for (InventoryDetail inventoryDetail : inventoryDetails) {
            if(inventoryDetail.getInvdt_status().getCode() == InventoryStatus.FINISH.getIndex()){
                finishedOdIds.add(inventoryDetail.getAst_id());
                finishedInvOrders.add(inventoryDetail);
            }
        }
        if (finishedOdIds.size() == 0) {
            ToastUtils.showShort("未发现已盘点产品");
            return;
        }
        //提交已经盘点的数据到服务器
        mPresenter.upLoadInvDetails(mInvId,finishedOdIds,finishedInvOrders);
    }

    private void upDateUi(List<InventoryDetail> invDetails) {
        if (invDetails == null || invDetails.size() == 0) {
            mTkrefreshlayout.setVisibility(View.GONE);
            emptyPage.setVisibility(View.VISIBLE);

            ((CoordinatorLayout.LayoutParams) btnSubmit.getLayoutParams())
                    .setBehavior(null);
            btnSubmit.hide();
            saveBtn.setClickable(false);
            return;
        }
        mTkrefreshlayout.setVisibility(View.VISIBLE);
        emptyPage.setVisibility(View.GONE);
        if (STATUS_FINISH.equals(mStatus)) {
            /*disableInvKey();手持机是否可以盘点扫描
            ((CoordinatorLayout.LayoutParams) btnSubmit.getLayoutParams())
                    .setBehavior(null);*/
            btnSubmit.hide();
            saveBtn.setEnabled(false);
            openBtn.setEnabled(false);
            clearBtn.setEnabled(false);
            llBottomBar.setBackgroundColor(
                    getResources().getColor(R.color.disabled_color1));
        }else {
           /* enableInvKey();
            ((CoordinatorLayout.LayoutParams) btnSubmit.getLayoutParams())
                    .setBehavior(new ScrollAwareFABBehavior());*/
            btnSubmit.show();
            saveBtn.setEnabled(true);
            openBtn.setEnabled(true);
            clearBtn.setEnabled(true);
            llBottomBar.setBackgroundColor(
                    getResources().getColor(R.color.enabled_color1));
        }
        mDataList.clear();
        mDataList.addAll(invDetails);

        checkedEpcList.clear();
        mLastInvDataList.clear();
        for (InventoryDetail detail : invDetails) {
            if (detail.getInvdt_status().getCode() == 1) {
                checkedEpcList.add(detail.getAssets_info().getAst_epc_code());
                mLastInvDataList.add(detail);
            }
        }
        adapterChangeByStatus(mDataList);
    }

    public void adapterChangeByStatus(List<InventoryDetail> inventoryDetails){
        Collections.sort(inventoryDetails, new Comparator<InventoryDetail>() {
            @Override
            public int compare(InventoryDetail t1, InventoryDetail t2) {
                return t1.getInvdt_status().getCode().compareTo(t2.getInvdt_status().getCode());
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.imgTitleLeft, R.id.openBtn, R.id.clearBtn,
            R.id.saveBtn, R.id.btn_submit, R.id.error_page,
            R.id.ll_init, R.id.ll_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgTitleLeft:
                if(mDataChanged){
                    new MaterialDialog.Builder(this)
                            .title("警告")
                            .content("盘点数据未保存，您确定退出盘点吗？")
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    InvdetailActivity.this.finish();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }else {
                    finish();
                }
                break;
            case R.id.clearBtn:
                break;
            case R.id.openBtn:
                esimUhfService.startStopScanning();
                //test start
               /* mDataList.get(0).getInvdt_status().setCode(InventoryStatus.FINISH.getIndex());
                DbBank.getInstance().getInventoryDetailDao().updateItem(mDataList.get(0));
                ResultInventoryOrderDao resultInventoryOrderDao = DbBank.getInstance().getResultInventoryOrderDao();
                ResultInventoryOrder invOrderByInvId = resultInventoryOrderDao.findInvOrderByInvId(mInvId);
                invOrderByInvId.setOpt_status(InvOperateStatus.MODIFIED_BUT_NOT_SUBMIT.getIndex());
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setCode(OrderStatusEm.PROCESSING.getIndex());
                orderStatus.setIndex(OrderStatusEm.PROCESSING.getIndex());
                orderStatus.setName(OrderStatusEm.PROCESSING.getName());
                invOrderByInvId.setInv_status(orderStatus);
                resultInventoryOrderDao.updateItem(invOrderByInvId);
                mAdapter.notifyDataSetChanged();*/
                //test end
                break;
            case R.id.saveBtn:
                mPresenter.findLocalInvDetailByInvid(mInvId);
                break;
            case R.id.btn_submit:
                finishInventory();
                break;
            case R.id.error_page:
                break;
            case R.id.ll_init:
                break;
            case R.id.ll_finish:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        Log.d(TAG, "handleEPC: "+uhfMsgEvent.toString());
        String epc=null;
        switch(uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag=(UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getEpc();
                Log.d(TAG, "epc=====: "+epc);
                handleEpc(epc);
                break;
            case UhfMsgType.UHF_CONNECT:
                Log.d(TAG, "UHF_CONNECT=====: ");
                break;
            case UhfMsgType.UHF_DISCONNECT:
                Log.d(TAG, "UHF_DISCONNECT=====: ");
                break;
            case UhfMsgType.UHF_START:
                Log.d(TAG, "UHF_START=====: ");
                mResnentUpdateInvDataList.clear();
                break;
            case UhfMsgType.UHF_STOP:
                Log.d(TAG, "UHF_STOP=====: ");
                //跟新盘点状态到数据库
                //盘点到新数据才更新到数据库
                if(mResnentUpdateInvDataList.size() > 0){
                    mAdapter.notifyDataSetChanged();
                }
                mPresenter.updateLocalInvDetailsState(mInvId,mResnentUpdateInvDataList);
                break;
        }
    }

    //处理盘点到的数据
    private void handleEpc(String epc) {
        if(epc != null && !checkedEpcList.contains(epc)){
            for (int i = 0; i < mDataList.size(); i++) {
                InventoryDetail inventoryDetail = mDataList.get(i);
                if(epc.equals(inventoryDetail.getAssets_info().getAst_epc_code())){
                    checkedEpcList.add(epc);
                    inventoryDetail.getInvdt_status().setCode(InventoryStatus.FINISH.getIndex());
                    mUpdateInvDataList.add(inventoryDetail);
                    mResnentUpdateInvDataList.add(inventoryDetail);
                    mHasInventorid++;
                    mNotInventoried--;
                    llCurrent.setVisibility(View.VISIBLE);
                    tvCurrentAst.setText(inventoryDetail.getAssets_info().getAst_name() + "-" + inventoryDetail.getAssets_info().getAst_code());
                    refresTitle();
                    mDataChanged = true;
                }
            }

        }
    }

    //盘点完成
    public void finishInventory(){
        ArrayList<InventoryDetail> nuInvDetails = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            InventoryDetail inventoryDetail = mDataList.get(i);
            if (inventoryDetail.getInvdt_status().getCode() == 0){
                nuInvDetails.add(inventoryDetail);
            }
        }
        if(nuInvDetails.size() == 0){
            new MaterialDialog.Builder(this)
                    .title("提示")
                    .content("您确定结束该盘点任务吗？")
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            mPresenter.finishInvOrder(mInvId, userId,null);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else{
            View remarkView = LayoutInflater.from(this).inflate(R.layout.layout_uninv_remark, null);
            new MaterialDialog.Builder(this)
                    .title("提示")
                    .customView(remarkView,true)
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            MaterialAutoCompleteTextView etRemark = (MaterialAutoCompleteTextView)remarkView.findViewById(R.id.uploade_remark);
                            String remark = etRemark.getText().toString();
                            if(remark == null || "".equals(remark)){
                                ToastUtils.showShort("请填写备注！");
                            }else {
                                dialog.dismiss();
                                mPresenter.finishInvOrder(mInvId, userId,remark);
                            }
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F4) { //扳机建扫描
            if (mEnable)
                esimUhfService.startStopScanning();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(esimUhfService.mEnable){
            esimUhfService.closeRFID();
        }
        EventBus.getDefault().unregister(this);
    }
}
