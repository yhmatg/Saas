package com.common.esimrfid.ui.newinventory;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.contract.home.InvAssetLocContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.customview.CustomPopWindow;
import com.common.esimrfid.presenter.home.InvAssetsLocPresenter;
import com.common.esimrfid.ui.inventorytask.InvDetailAdapter;

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
    //这个区域下所有的资产
    List<InventoryDetail> mInventoryDetails = new ArrayList<>();
    //当前显示的资产
    List<InventoryDetail> mCurrentDetails = new ArrayList<>();
    //待盘点
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
    @Override
    public InvAssetsLocPresenter initPresenter() {
        return new InvAssetsLocPresenter(DataManager.getInstance());
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
        assetInvRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        assetInvRecycler.setAdapter(mAssetAdapter);
        initPopWindow();

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
                    selectNotInvBean.getAssetsInfos().setInvdt_sign("资产已丢失");
                    selectNotInvBean.getInvdt_status().setCode(1);
                    selectNotInvBean.setNeedUpload(true);
                    DbBank.getInstance().getInventoryDetailDao().updateItem(selectNotInvBean);
                    mNotInvDetails.remove(selectNotInvBean);
                    mLessDetails.add(selectNotInvBean);
                    notInvedRadio.setText("待盘点" + mNotInvDetails.size());
                    invLessRadio.setText("盘亏" + mLessDetails.size());
                    if(currentShowStatus == 0){
                        mCurrentDetails.remove(selectNotInvBean);
                    }else if(currentShowStatus == 1){
                        mCurrentDetails.add(selectNotInvBean);
                    }
                    mAssetAdapter.notifyDataSetChanged();
                    mCustomPopWindow.dissmiss();
                }
            }
        });
        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNotInvBean != null){
                    tvTransfer.setTextColor(getColor(R.color.titele_color));
                    selectNotInvBean.getAssetsInfos().setInvdt_sign("资产转移了");
                    selectNotInvBean.getInvdt_status().setCode(1);
                    DbBank.getInstance().getInventoryDetailDao().updateItem(selectNotInvBean);
                    mNotInvDetails.remove(selectNotInvBean);
                    mLessDetails.add(selectNotInvBean);
                    notInvedRadio.setText("待盘点" + mNotInvDetails.size());
                    invLessRadio.setText("盘亏" + mLessDetails.size());
                    if(currentShowStatus == 0){
                        mCurrentDetails.remove(selectNotInvBean);
                    }else if(currentShowStatus == 1){
                        mCurrentDetails.add(selectNotInvBean);
                    }
                    mAssetAdapter.notifyDataSetChanged();
                    mCustomPopWindow.dissmiss();
                }
            }
        });
        tvUserOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNotInvBean != null){
                    tvUserOut.setTextColor(getColor(R.color.titele_color));
                    selectNotInvBean.getAssetsInfos().setInvdt_sign("人员外出中");
                    selectNotInvBean.getInvdt_status().setCode(1);
                    DbBank.getInstance().getInventoryDetailDao().updateItem(selectNotInvBean);
                    mNotInvDetails.remove(selectNotInvBean);
                    mLessDetails.add(selectNotInvBean);
                    notInvedRadio.setText("待盘点" + mNotInvDetails.size());
                    invLessRadio.setText("盘亏" + mLessDetails.size());
                    if(currentShowStatus == 0){
                        mCurrentDetails.remove(selectNotInvBean);
                    }else if(currentShowStatus == 1){
                        mCurrentDetails.add(selectNotInvBean);
                    }
                    mAssetAdapter.notifyDataSetChanged();
                    mCustomPopWindow.dissmiss();
                }
            }
        });
        tvAstBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNotInvBean != null){
                    tvAstBorrow.setTextColor(getColor(R.color.titele_color));
                    selectNotInvBean.getAssetsInfos().setInvdt_sign("资产外借中");
                    selectNotInvBean.getInvdt_status().setCode(1);
                    DbBank.getInstance().getInventoryDetailDao().updateItem(selectNotInvBean);
                    mNotInvDetails.remove(selectNotInvBean);
                    mLessDetails.add(selectNotInvBean);
                    notInvedRadio.setText("待盘点" + mNotInvDetails.size());
                    invLessRadio.setText("盘亏" + mLessDetails.size());
                    if(currentShowStatus == 0){
                        mCurrentDetails.remove(selectNotInvBean);
                    }else if(currentShowStatus == 1){
                        mCurrentDetails.add(selectNotInvBean);
                    }
                    mAssetAdapter.notifyDataSetChanged();
                    mCustomPopWindow.dissmiss();
                }
            }
        });
        tvAstRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNotInvBean != null){
                    tvAstRepair.setTextColor(getColor(R.color.titele_color));
                    selectNotInvBean.getAssetsInfos().setInvdt_sign("资产维修中");
                    selectNotInvBean.getInvdt_status().setCode(1);
                    DbBank.getInstance().getInventoryDetailDao().updateItem(selectNotInvBean);
                    mNotInvDetails.remove(selectNotInvBean);
                    mLessDetails.add(selectNotInvBean);
                    notInvedRadio.setText("待盘点" + mNotInvDetails.size());
                    invLessRadio.setText("盘亏" + mLessDetails.size());
                    if(currentShowStatus == 0){
                        mCurrentDetails.remove(selectNotInvBean);
                    }else if(currentShowStatus == 1){
                        mCurrentDetails.add(selectNotInvBean);
                    }
                    mAssetAdapter.notifyDataSetChanged();
                    mCustomPopWindow.dissmiss();
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
        notInvedRadio.setText("待盘点" + mNotInvDetails.size());
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
    }

    @Override
    public void onItemClick(InventoryDetail invDetailBean, TextView tvAddTag) {
        mCustomPopWindow.showAsDropDown(tvAddTag,-40,10);
        selectNotInvBean = invDetailBean;
        mAddTag = tvAddTag;
    }
}
