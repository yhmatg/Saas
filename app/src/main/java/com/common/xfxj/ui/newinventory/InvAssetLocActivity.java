package com.common.xfxj.ui.newinventory;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.xfxj.R;
import com.common.xfxj.app.EsimAndroidApp;
import com.common.xfxj.base.activity.BaseActivity;
import com.common.xfxj.contract.home.InvAssetLocContract;
import com.common.xfxj.core.bean.inventorytask.EpcBean;
import com.common.xfxj.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.xfxj.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.xfxj.customview.CustomPopWindow;
import com.common.xfxj.presenter.home.InvAssetsLocPresenter;
import com.common.xfxj.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InvAssetLocActivity extends BaseActivity<InvAssetsLocPresenter> implements InvAssetLocContract.View, InvAssetAdapter.OnItemClickListener {

    public static final String INV_ID = "inv_id";
    public static final String LOC_IC = "loc_id";
    public static final String LOC_Name = "loc_name";
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.asset_inv_recycler)
    RecyclerView assetInvRecycler;
    @BindView(R.id.empty_page)
    LinearLayout empty_layout;
    @BindView(R.id.asset_status_tab)
    RadioGroup AssetGroup;
    @BindView(R.id.asset_not_inved)
    RadioButton notInvedRadio;
    @BindView(R.id.asset_inved)
    RadioButton invedRadio;
    @BindView(R.id.asset_inv_more)
    RadioButton invMoreRadio;
    @BindView(R.id.asset_inv_less)
    RadioButton invLessRadio;
    @BindView(R.id.start_inv)
    Button mInvButton;
    //这个区域下所有的资产
    List<InventoryDetail> mInventoryDetails = new ArrayList<>();
    //当前显示的资产
    List<InventoryDetail> mCurrentDetails = new ArrayList<>();
    //未盘点
    List<InventoryDetail> mNotInvDetails = new ArrayList<>();
    //已盘
    List<InventoryDetail> mInvedDetails = new ArrayList<>();
    //盘盈
    List<InventoryDetail> mMoreDetails = new ArrayList<>();
    //盘亏
    List<InventoryDetail> mLessDetails = new ArrayList<>();
    InvAssetAdapter mAssetAdapter;
    private String mInvId;
    private String mLocId;
    private String mLocName;
    int currentShowStatus = 0;
    RadioButton preRadioButton;
    CustomPopWindow mCustomPopWindow;
    InventoryDetail selectNotInvBean;
    TextView mAddTag;
    TextView mPrePopTextview;
    @Override
    public InvAssetsLocPresenter initPresenter() {
        return new InvAssetsLocPresenter();
    }

    @Override
    protected void initEventAndData() {
        if (getIntent() != null) {
            mInvId = getIntent().getStringExtra(INV_ID);
            mLocId = getIntent().getStringExtra(LOC_IC);
            mLocName = getIntent().getStringExtra(LOC_Name);
        }
        mTitle.setText(mLocName);
        preRadioButton = notInvedRadio;
        notInvedRadio.setChecked(true);
        notInvedRadio.setTextColor(getColor(R.color.home_number));
        mAssetAdapter = new InvAssetAdapter(mCurrentDetails, this);
        mAssetAdapter.setOnItemClickListener(this);
        assetInvRecycler.setLayoutManager(new LinearLayoutManager(this));
        assetInvRecycler.setAdapter(mAssetAdapter);
        initPopWindow();
        if(!"InventoryTaskActivity".equals(EsimAndroidApp.activityFrom)){
            mInvButton.setVisibility(View.GONE);
        }
    }

    private void initPopWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.add_tag_layout,null);
        TextView tvLost = contentView.findViewById(R.id.tv_ast_lost);
        TextView tvTransfer = contentView.findViewById(R.id.tv_transfer);
        TextView tvUserOut = contentView.findViewById(R.id.tv_user_out);
        TextView tvAstBorrow = contentView.findViewById(R.id.tv_ast_borrow);
        TextView tvAstRepair = contentView.findViewById(R.id.tv_ast_repair);
        tvLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNotInvBean != null){
                    tvLost.setTextColor(getColor(R.color.titele_color));
                    mPrePopTextview = tvLost;
                    selectNotInvBean.setInvdt_sign("资产已丢失");
                    selectNotInvBean.getInvdt_status().setCode(1);
                    selectNotInvBean.setNeedUpload(true);
                    mPresenter.setOneLessAssetInv(selectNotInvBean);
                    mNotInvDetails.remove(selectNotInvBean);
                    mLessDetails.add(selectNotInvBean);
                    notInvedRadio.setText("未盘点" + mNotInvDetails.size());
                    invLessRadio.setText("盘亏" + mLessDetails.size());
                    if(currentShowStatus == 0){
                        mCurrentDetails.remove(selectNotInvBean);
                    }else if(currentShowStatus == 1){
                        mCurrentDetails.add(selectNotInvBean);
                    }
                    mAssetAdapter.notifyDataSetChanged();
                    mCustomPopWindow.dissmiss();
                    ToastUtils.showShort(R.string.inv_less_toast);
                }
            }
        });
        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNotInvBean != null){
                    tvTransfer.setTextColor(getColor(R.color.titele_color));
                    mPrePopTextview = tvTransfer;
                    selectNotInvBean.setInvdt_sign("资产转移了");
                    selectNotInvBean.getInvdt_status().setCode(1);
                    selectNotInvBean.setNeedUpload(true);
                    mPresenter.setOneLessAssetInv(selectNotInvBean);
                    mNotInvDetails.remove(selectNotInvBean);
                    mLessDetails.add(selectNotInvBean);
                    notInvedRadio.setText("未盘点" + mNotInvDetails.size());
                    invLessRadio.setText("盘亏" + mLessDetails.size());
                    if(currentShowStatus == 0){
                        mCurrentDetails.remove(selectNotInvBean);
                    }else if(currentShowStatus == 1){
                        mCurrentDetails.add(selectNotInvBean);
                    }
                    mAssetAdapter.notifyDataSetChanged();
                    mCustomPopWindow.dissmiss();
                    ToastUtils.showShort(R.string.inv_less_toast);
                }
            }
        });
        tvUserOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNotInvBean != null){
                    tvUserOut.setTextColor(getColor(R.color.titele_color));
                    mPrePopTextview = tvUserOut;
                    selectNotInvBean.setInvdt_sign("人员外出中");
                    selectNotInvBean.getInvdt_status().setCode(1);
                    selectNotInvBean.setNeedUpload(true);
                    mPresenter.setOneLessAssetInv(selectNotInvBean);
                    mNotInvDetails.remove(selectNotInvBean);
                    mLessDetails.add(selectNotInvBean);
                    notInvedRadio.setText("未盘点" + mNotInvDetails.size());
                    invLessRadio.setText("盘亏" + mLessDetails.size());
                    if(currentShowStatus == 0){
                        mCurrentDetails.remove(selectNotInvBean);
                    }else if(currentShowStatus == 1){
                        mCurrentDetails.add(selectNotInvBean);
                    }
                    mAssetAdapter.notifyDataSetChanged();
                    mCustomPopWindow.dissmiss();
                    ToastUtils.showShort(R.string.inv_less_toast);
                }
            }
        });
        tvAstBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNotInvBean != null){
                    tvAstBorrow.setTextColor(getColor(R.color.titele_color));
                    mPrePopTextview = tvAstBorrow;
                    selectNotInvBean.setInvdt_sign("资产外借中");
                    selectNotInvBean.getInvdt_status().setCode(1);
                    selectNotInvBean.setNeedUpload(true);
                    mPresenter.setOneLessAssetInv(selectNotInvBean);
                    mNotInvDetails.remove(selectNotInvBean);
                    mLessDetails.add(selectNotInvBean);
                    notInvedRadio.setText("未盘点" + mNotInvDetails.size());
                    invLessRadio.setText("盘亏" + mLessDetails.size());
                    if(currentShowStatus == 0){
                        mCurrentDetails.remove(selectNotInvBean);
                    }else if(currentShowStatus == 1){
                        mCurrentDetails.add(selectNotInvBean);
                    }
                    mAssetAdapter.notifyDataSetChanged();
                    mCustomPopWindow.dissmiss();
                    ToastUtils.showShort(R.string.inv_less_toast);
                }
            }
        });
        tvAstRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNotInvBean != null){
                    tvAstRepair.setTextColor(getColor(R.color.titele_color));
                    mPrePopTextview = tvAstRepair;
                    selectNotInvBean.setInvdt_sign("资产维修中");
                    selectNotInvBean.getInvdt_status().setCode(1);
                    selectNotInvBean.setNeedUpload(true);
                    mPresenter.setOneLessAssetInv(selectNotInvBean);
                    mNotInvDetails.remove(selectNotInvBean);
                    mLessDetails.add(selectNotInvBean);
                    notInvedRadio.setText("未盘点" + mNotInvDetails.size());
                    invLessRadio.setText("盘亏" + mLessDetails.size());
                    if(currentShowStatus == 0){
                        mCurrentDetails.remove(selectNotInvBean);
                    }else if(currentShowStatus == 1){
                        mCurrentDetails.add(selectNotInvBean);
                    }
                    mAssetAdapter.notifyDataSetChanged();
                    mCustomPopWindow.dissmiss();
                    ToastUtils.showShort(R.string.inv_less_toast);
                }
            }
        });
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(false)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if(mPrePopTextview != null){
                            mPrePopTextview.setTextColor(getColor(R.color.setting_text_one));
                        }
                        mAddTag.setTextColor(getColor(R.color.titele_color));
                        mAddTag.setBackground(getDrawable(R.drawable.add_tag_white_shape));
                    }
                })
                .create();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invloction_layout;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.fetchAllInvDetails(mInvId, mLocId);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick({R.id.title_back, R.id.start_inv, R.id.asset_not_inved, R.id.asset_inved, R.id.asset_inv_more, R.id.asset_inv_less})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.start_inv:
                Intent intent = new Intent();
                intent.putExtra(INV_ID, mInvId);
                intent.putExtra(LOC_IC, mLocId);
                intent.putExtra(LOC_Name, mLocName);
                intent.setClass(this, InvAssetScanActivity.class);
                startActivity(intent);
                break;
            case R.id.asset_not_inved:
                currentShowStatus = 0;
                preRadioButton.setTextColor(getColor(R.color.repair_text));
                notInvedRadio.setTextColor(getColor(R.color.home_number));
                preRadioButton = notInvedRadio;
                notInvedRadio.setChecked(true);
                mCurrentDetails.clear();
                mCurrentDetails.addAll(mNotInvDetails);
                mAssetAdapter.notifyDataSetChanged();
                break;
            case R.id.asset_inved:
                currentShowStatus = 10;
                preRadioButton.setTextColor(getColor(R.color.repair_text));
                invedRadio.setTextColor(getColor(R.color.home_number));
                preRadioButton = invedRadio;
                invedRadio.setChecked(true);
                mCurrentDetails.clear();
                mCurrentDetails.addAll(mInvedDetails);
                mAssetAdapter.notifyDataSetChanged();
                break;
            case R.id.asset_inv_more:
                currentShowStatus = 2;
                preRadioButton.setTextColor(getColor(R.color.repair_text));
                invMoreRadio.setTextColor(getColor(R.color.home_number));
                preRadioButton = invMoreRadio;
                invMoreRadio.setChecked(true);
                mCurrentDetails.clear();
                mCurrentDetails.addAll(mMoreDetails);
                mAssetAdapter.notifyDataSetChanged();
                break;
            case R.id.asset_inv_less:
                currentShowStatus = 1;
                preRadioButton.setTextColor(getColor(R.color.repair_text));
                invLessRadio.setTextColor(getColor(R.color.home_number));
                preRadioButton = invLessRadio;
                invLessRadio.setChecked(true);
                mCurrentDetails.clear();
                mCurrentDetails.addAll(mLessDetails);
                mAssetAdapter.notifyDataSetChanged();
                break;
        }
        if(mCurrentDetails.size() > 0){
            empty_layout.setVisibility(View.GONE);
        }else {
            empty_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void handleInvDetails(List<InventoryDetail> inventoryDetails) {
        mNotInvDetails.clear();
        mInvedDetails.clear();
        mMoreDetails.clear();
        mLessDetails.clear();
        mCurrentDetails.clear();
        for (InventoryDetail inventoryDetail : inventoryDetails) {
            if (inventoryDetail.getInvdt_status().getCode() == 0) {
                mNotInvDetails.add(inventoryDetail);
            } else if (inventoryDetail.getInvdt_status().getCode() == 1) {
                mLessDetails.add(inventoryDetail);
            } else if (inventoryDetail.getInvdt_status().getCode() == 2) {
                mMoreDetails.add(inventoryDetail);
            } else if (inventoryDetail.getInvdt_status().getCode() == 10) {
                mInvedDetails.add(inventoryDetail);
            }
        }
        notInvedRadio.setText("未盘点" + mNotInvDetails.size());
        invedRadio.setText("已盘" + mInvedDetails.size());
        invMoreRadio.setText("盘盈" + mMoreDetails.size());
        invLessRadio.setText("盘亏" + mLessDetails.size());
        if (currentShowStatus == 0) {
            mCurrentDetails.addAll(mNotInvDetails);
        } else if (currentShowStatus == 1) {
            mCurrentDetails.addAll(mLessDetails);
        } else if (currentShowStatus == 2) {
            mCurrentDetails.addAll(mMoreDetails);
        } else if (currentShowStatus == 10) {
            mCurrentDetails.addAll(mInvedDetails);
        }
        mAssetAdapter.notifyDataSetChanged();
        if(mCurrentDetails.size() > 0){
            empty_layout.setVisibility(View.GONE);
        }else {
            empty_layout.setVisibility(View.VISIBLE);
        }
        if(mNotInvDetails.size() == 0){
            mInvButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void handleAllAssetEpcs(List<EpcBean> allEpcs) {

    }

    @Override
    public void handleXfInvDetails(List<XfInventoryDetail> xInventoryDetail) {

    }

    @Override
    public void onItemClick(InventoryDetail invDetailBean, TextView tvAddTag) {
        mCustomPopWindow.showAsDropDown(tvAddTag,-160,20);
        selectNotInvBean = invDetailBean;
        mAddTag = tvAddTag;
    }
}
