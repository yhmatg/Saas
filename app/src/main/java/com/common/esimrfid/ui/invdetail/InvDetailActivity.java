package com.common.esimrfid.ui.invdetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.InvDetail;
import com.common.esimrfid.presenter.home.InvDetailPressnter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.uhf.ZebraUhfServiceImpl;
import com.common.esimrfid.utils.MaterialDialogUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InvDetailActivity extends BaseActivity<InvDetailPressnter> implements InvDetailContract.View {
    private static final String TAG = "InvDetailActivity";
    @BindView(R.id.tvTitleLeft)
    TextView mBackBtn;
    @BindView(R.id.tv_total)
    TextView mConState;
    @BindView(R.id.tv_inventory_num)
    TextView mTatalNum;
    @BindView(R.id.tv_inventoried_num)
    TextView mInventroiedNum;
    @BindView(R.id.openBtn)
    LinearLayout mUploadBtn;
    @BindView(R.id.clearBtn)
    LinearLayout mFinishBtn;
    @BindView(R.id.bt_con_frid)
    Button mConButton;
    @BindView(R.id.detail_tk_rflayout)
    TwinklingRefreshLayout mTkRefresh;
    @BindView(R.id.detail_recycle)
    RecyclerView mRecycle;

    IEsimUhfService esimUhfService=null;

    private Boolean isConnected = false;

    private Boolean isChanged = false;
    //全部数据invDetail
    private List<InvDetail> invDetails = new ArrayList<>() ;
    //全部数据中已经被扫描到的invDetail
    private List<InvDetail> updateDetails = new ArrayList<>();
    //扫描到的epc集合
    private List<String> checkList = new ArrayList<>();
    private String orderId;
    private InvDetailAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inv_detail;
    }

    @OnClick({R.id.tvTitleLeft,R.id.bt_con_frid,R.id.openBtn,R.id.clearBtn})
    public void performClick(View view){
        switch (view.getId()){
            case R.id.tvTitleLeft:
                backMethod();
                break;
            case R.id.bt_con_frid:
                connctFird();
                break;
            case R.id.openBtn:
                uploadData();
                break;
            case R.id.clearBtn:
                finishInvOrder();
                break;
        }
    }

    private void backMethod() {
        if(isChanged){
            ToastUtils.showShort("盘点数据未保存");
            MaterialDialogUtils.showBasicDialog(EsimAndroidApp.getInstance(),"警告","盘点数据未保存，是否退出？").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    finish();
                }
            }).show();
        }else{
            finish();
        }
    }

    private void finishInvOrder() {
        if(isChanged||!updateDetails.isEmpty()){
            ToastUtils.showShort("存在未提交的盘点数据，请先提交数据");
        }else{
            mPresenter.finishInvOrder(orderId);
        }
    }

    private void uploadData() {
        if(updateDetails.isEmpty()){
            ToastUtils.showShort("暂无待提交数据");
        }else{
            mPresenter.uploadInvDetails(updateDetails,orderId);
        }
    }

    private void connctFird() {
        if(isConnected){
            ToastUtils.showShort("RFID已连接");
        }else{
            ToastUtils.showShort("正在连接RFID..");
            esimUhfService.initRFID();
        }
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initEventAndData() {
        initRFID();
        EventBus.getDefault().register(this);
        orderId = getIntent().getStringExtra("orderId");
        Log.e(TAG,"orderId=====" + orderId);
        mTkRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mPresenter.fetchAllInvDetails(orderId);
            }

        });
        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        mRecycle.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mAdapter = new InvDetailAdapter(invDetails, this);
        mRecycle.setAdapter(mAdapter);
        mPresenter.fetchAllInvDetails(orderId);

    }

    private void initRFID() {
        esimUhfService = new ZebraUhfServiceImpl();
        esimUhfService.initRFID();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRFID();
        EventBus.getDefault().unregister(this);
    }

    private void closeRFID() {
        if (esimUhfService.isStart()){
            esimUhfService.closeRFID();
        }

    }

    @Override
    public InvDetailPressnter initPresenter() {
        return new InvDetailPressnter(DataManager.getInstance());
    }

    @Override
    public void loadInvDetailsNet(List<InvDetail> invdetails) {
        mTkRefresh.finishRefreshing();
        invDetails.clear();
        invDetails.addAll(invdetails);
        excuteDetails();
    }

    @Override
    public void finishSelf() {
        finish();
    }

    @Override
    public void uploadSuccess() {
        isChanged=false;
        updateDetails.clear();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEPC(UhfMsgEvent uhfMsgEvent) {

        String epc=null;
        switch(uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag=(UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getEpc();
                if(epc!=null&&!checkList.contains(epc)){
                    checkList.add(epc);
                    for (InvDetail invDetail : invDetails) {
                        if(invDetail.getCorpEpcCode().equals(epc)){
                            invDetail.setInvdtStatus(1);
                            updateDetails.add(invDetail);
                            isChanged =true;
                            break;
                        }
                    }
                    updateUi();
                }

                break;
            case UhfMsgType.UHF_START:
                ToastUtils.showShort("开始扫描...");
                break;
            case UhfMsgType.UHF_STOP:
                ToastUtils.showShort("停止扫描...");
                break;
            case UhfMsgType.UHF_CONNECT:
                mConState.setText("已连接");
                isConnected = true;
                break;
            case UhfMsgType.UHF_DISCONNECT:
                ToastUtils.showShort("RFID已断开连接");
                mConState.setText("未连接");
                isConnected = false;
                break;
        }

    }

    private void updateUi() {
        int finishNum = 0;
        for (InvDetail invDetail : invDetails) {
            if(invDetail.getInvdtStatus()==1){
                finishNum++;
            }
        }
        mAdapter.setRefreshDataList(invDetails);
        mInventroiedNum.setText(String.valueOf(finishNum));
    }

    public void excuteDetails(){
        checkList.clear();
        int finishNum=0;
        for(InvDetail invDetail:invDetails){
            invDetail.setInvId(orderId);
            if(invDetail.getInvdtStatus()==1){
                checkList.add(invDetail.getCorpEpcCode());
                finishNum++;
            }
        }
        mAdapter.setRefreshDataList(invDetails);
        mTatalNum.setText(String.valueOf(invDetails.size()));
        mInventroiedNum.setText(String.valueOf(finishNum));
        dismissDialog();

    }
}
