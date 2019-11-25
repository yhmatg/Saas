package com.common.esimrfid.ui.requisition;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.RequisitionDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionAssetInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.RequisitionDetailInfo;
import com.common.esimrfid.presenter.home.RequisitionDetailPressnter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class RequisitionDetailActivity extends BaseActivity<RequisitionDetailPressnter> implements RequisitionDetailContract.View {

    private static final String REQUISITION_ID = "req_id";
    private static final String REQUISITION_USER_NAME = "req_user_name";
    private static final String REQUISITION_ORD_STATUS = "req_ord_status";
    @BindView(R.id.error_page)
    LinearLayout errorPage;
    @BindView(R.id.empty_page)
    LinearLayout emptyPage;
    @BindView(R.id.tvTitleCenter)
    TextView tvTitleCenter;
    @BindView(R.id.imgTitleLeft)
    ImageView imgTitleLeft;
    @BindView(R.id.tvTitleRight)
    TextView tvTitleRight;
    @BindView(R.id.tv_start_or_stop)
    TextView tvScannOrStop;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_res_name)
    TextView mReqName;
    @BindView(R.id.tv_res_assets)
    TextView mReqAssetsInfo;
    @BindView(R.id.used_layout)
    LinearLayout mUsedLayout;
    @BindView(R.id.used_assets)
    TextView mUsedAssets;
    @BindView(R.id.used_details)
    TextView mUesdDetails;
    @BindView(R.id.req_recycleview)
    RecyclerView mReqInfosRv;
    @BindView(R.id.et_asset_id)
    EditText mEtAssetId;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.btn_finish)
    FloatingActionButton mFloatButton;
    @BindView(R.id.search_layout)
    LinearLayout mSearchLayout;
    @BindView(R.id.bottom_layout)
    LinearLayout mBottomLayout;
    @BindView(R.id.asset_list_detail)
    TextView mListTitle;
    @BindString(R.string.sub_req_asset_fail)
    String submitFailed;
    RequAssetAdapter resAdapter;
    List<RequisitionAssetInfo> rqeList = new ArrayList<>();
    List<RequisitionAssetInfo> selectItemList = new ArrayList<>();
    IEsimUhfService esimUhfService = null;
    Set<String> scanEpcs = new HashSet<>();
    private String reqId;
    private String reqUserName;
    private String reqOrdStatus;
    private int currentUpload;

    @Override
    public RequisitionDetailPressnter initPresenter() {
        return new RequisitionDetailPressnter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        tvTitleCenter.setText("领用单详情");
        imgTitleLeft.setVisibility(View.VISIBLE);
        tvTitleRight.setText(R.string.clear_card);
        tvTitleRight.setVisibility(View.VISIBLE);
        initRfidAndEventbus();
        Intent intent = getIntent();
        reqId = intent.getStringExtra(REQUISITION_ID);
        reqUserName = intent.getStringExtra(REQUISITION_USER_NAME);
        reqOrdStatus = intent.getStringExtra(REQUISITION_ORD_STATUS);
        mReqName.setText("领用人：" + reqUserName);
        if ("已完成".equals(reqOrdStatus)) {
            mListTitle.setText(R.string.asset_details);
            mFloatButton.hide();
            mSearchLayout.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.GONE);
            mUsedLayout.setVisibility(View.GONE);
            tvTitleRight.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mReqInfosRv.getLayoutParams();
            layoutParams.bottomMargin = 0;
            mReqInfosRv.setLayoutParams(layoutParams);
        }
        resAdapter = new RequAssetAdapter(this, rqeList, reqOrdStatus);
        mReqInfosRv.setLayoutManager(new LinearLayoutManager(this));
        mReqInfosRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mReqInfosRv.setAdapter(resAdapter);
        mPresenter.getFinishedAssetDetaisl(reqId);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_req_detail;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.imgTitleLeft, R.id.tvTitleRight, R.id.tv_start_or_stop, R.id.tv_submit, R.id.tv_search, R.id.btn_finish, R.id.used_details})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.imgTitleLeft:
                finish();
                break;
            case R.id.tvTitleRight:
                //selectItemList.clear();
                resAdapter.getSeleceItems().clear();
                rqeList.clear();
                resAdapter.getStatusMap().clear();
                resAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_start_or_stop:
                if (esimUhfService != null) {
                    esimUhfService.startStopScanning();
                } else {
                    ToastUtils.showShort(R.string.not_connect_prompt);
                }
                break;
            case R.id.tv_submit:
                List<RequisitionAssetInfo> seleceItems = resAdapter.getSeleceItems();
                if (seleceItems.size() == 0) {
                    ToastUtils.showShort(R.string.not_select_asset);
                } else {
                   showConfrimDialog(seleceItems);
                }
                break;
            case R.id.tv_search:
                String assetId = mEtAssetId.getText().toString();
                if ("".equals(assetId)) {
                    ToastUtils.showShort(R.string.input_write_asset_id);
                } else {
                    mPresenter.getAssetByAssetId(assetId);
                }
                break;
            case R.id.btn_finish:

                break;
            case R.id.used_details:
                mPresenter.getFinishedAssetDetaisl(reqId);
                break;
        }
    }

    private void initRfidAndEventbus() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        String epc = null;
        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag = (UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getEpc();
                handleEpc(epc);
                break;
            case UhfMsgType.UHF_CONNECT:
                break;
            case UhfMsgType.UHF_DISCONNECT:
                break;
            case UhfMsgType.UHF_START:
                scanEpcs.clear();
                tvScannOrStop.setText("停止");
                break;
            case UhfMsgType.UHF_STOP:
                tvScannOrStop.setText("扫描");
                if (scanEpcs.size() != 0) {
                    mPresenter.getAssetsByEpcs(scanEpcs);
                } else {
                    ToastUtils.showShort(R.string.not_get_epc);
                }
                break;
        }
    }

    public void handleEpc(String epc) {
        scanEpcs.add(epc);
    }

    @Override
    public void handleAssetsByEpcs(List<RequisitionAssetInfo> assets) {
        rqeList.clear();
        rqeList.addAll(assets);
        resAdapter.notifyDataSetChanged();
        handleResultList(assets);
    }

    @Override
    public void handleAssetsByAssetId(List<RequisitionAssetInfo> assets) {
        ArrayList<RequisitionAssetInfo> tempData = new ArrayList<>();
        tempData.addAll(rqeList);
        assets.removeAll(rqeList);
        rqeList.clear();
        rqeList.addAll(assets);
        rqeList.addAll(tempData);
        resAdapter.getSeleceItems().clear();
        resAdapter.getStatusMap().clear();
        resAdapter.notifyDataSetChanged();
        handleResultList(rqeList);
    }

    @Override
    public void handleFinishedAssets(RequisitionDetailInfo requisitionDetailInfo) {
        List<RequisitionAssetInfo> assets = requisitionDetailInfo.getAssetsInfos();
        /* List<RequisitionDetailInfo.ReqDetail> reqDetails = requisitionDetailInfo.getReq_detail();
        StringBuilder detailString = new StringBuilder("领用备注：" + "\n");
        if (reqDetails != null && reqDetails.size() != 0) {
            for (int i = 0; i < reqDetails.size(); i++) {
                RequisitionDetailInfo.ReqDetail itemsBean = reqDetails.get(i);
                detailString.append("    " + itemsBean.getName());
                detailString.append("(");
                for (int j = 0; j < itemsBean.getFormFields().size(); j++) {
                    detailString.append(itemsBean.getFormFields().get(j).getName() + ": ");
                    detailString.append(itemsBean.getFormFields().get(j).getValue());
                    if (j < itemsBean.getFormFields().size() - 1) {
                        detailString.append(", ");
                    }
                }
                detailString.append(")");
                if (i < reqDetails.size() - 1) {
                    detailString.append("\n");
                }
            }
            mReqAssetsInfo.setText(detailString.toString().replace("null", "无信息"));
        }*/
        String remark = requisitionDetailInfo.getOdr_remark();
        if(!TextUtils.isEmpty(remark))
            mReqAssetsInfo.setText("领用备注："+ remark);
        if (assets != null) {
            currentUpload = assets.size();
            String usedAssets = "已领用资产: " + currentUpload;
            mUsedAssets.setText(usedAssets);
            rqeList.clear();
            rqeList.addAll(assets);
            resAdapter.notifyDataSetChanged();
            handleResultList(assets);
        }

    }

    @Override
    public void handleUploadResAssets(BaseResponse result) {
        List<RequisitionAssetInfo> seleceItems = resAdapter.getSeleceItems();
        int upLoadNub = seleceItems.size();
        if (result.isSuccess()) {
            currentUpload += upLoadNub;
            String usedAssets = "已领用资产: " + currentUpload;
            mUsedAssets.setText(usedAssets);
            ToastUtils.showShort(R.string.sub_req_asset_suc);
        } else {
            ToastUtils.showShort(submitFailed + result.getMessage() + "，可能已在使用中。");
        }
        seleceItems.clear();
        resAdapter.getStatusMap().clear();
        resAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void handleResultList(List<RequisitionAssetInfo> assets) {
        if (assets.size() == 0) {
            emptyPage.setVisibility(View.VISIBLE);
            mReqInfosRv.setVisibility(View.GONE);
        } else {
            emptyPage.setVisibility(View.GONE);
            mReqInfosRv.setVisibility(View.VISIBLE);
        }

    }

    public void showConfrimDialog(List<RequisitionAssetInfo> seleceItems) {
        ArrayList<String> selectEpcs = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < seleceItems.size(); i++) {
            RequisitionAssetInfo requisitionAssetInfo = seleceItems.get(i);
            selectEpcs.add(requisitionAssetInfo.getAst_epc_code());
            stringBuilder.append(requisitionAssetInfo.getAst_name() + "\n(" + requisitionAssetInfo.getAst_barcode() + ")");
            if(i < seleceItems.size() - 1){
                stringBuilder.append(",\n");
            }
        }
        new MaterialDialog.Builder(this)
                .title("提交信息")
                .content(stringBuilder)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.uploadResAssets(reqId, selectEpcs);
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
