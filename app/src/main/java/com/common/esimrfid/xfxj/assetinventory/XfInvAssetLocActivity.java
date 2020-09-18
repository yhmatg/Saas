package com.common.esimrfid.xfxj.assetinventory;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvAssetLocContract;
import com.common.esimrfid.core.bean.inventorytask.EpcBean;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.esimrfid.presenter.home.InvAssetsLocPresenter;
import com.common.esimrfid.xfxj.identity.XfIdentityActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class XfInvAssetLocActivity extends BaseActivity<InvAssetsLocPresenter> implements InvAssetLocContract.View, XfLocAssetAdapter.OnItemClickListener {

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
    @BindView(R.id.start_inv)
    Button mInvButton;
    //这个区域下所有的资产
    List<XfInventoryDetail> mInventoryDetails = new ArrayList<>();
    //当前显示的资产
    List<XfInventoryDetail> mCurrentDetails = new ArrayList<>();
    //未盘点
    List<XfInventoryDetail> mNotInvDetails = new ArrayList<>();
    //已盘
    List<XfInventoryDetail> mInvedDetails = new ArrayList<>();
    XfLocAssetAdapter mAssetAdapter;
    private String mInvId;
    private String mLocId;
    private String mLocName;
    int currentShowStatus = 0;
    RadioButton preRadioButton;
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
        mAssetAdapter = new XfLocAssetAdapter(mCurrentDetails, this);
        mAssetAdapter.setOnItemClickListener(this);
        assetInvRecycler.setLayoutManager(new LinearLayoutManager(this));
        assetInvRecycler.setAdapter(mAssetAdapter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.xf_activity_invloction_layout;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        EsimAndroidApp.activityFrom = "XfInvAssetLocActivity";
        mPresenter.fetchXfInvDetails(mInvId, mLocId);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick({R.id.title_back, R.id.start_inv, R.id.asset_not_inved, R.id.asset_inved})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.start_inv:
                Intent intent = new Intent();
                intent.putExtra(INV_ID,mInvId);
                intent.putExtra(LOC_IC,mLocId);
                intent.setClass(this,XfIdentityActivity.class);
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
        }
        if(mCurrentDetails.size() > 0){
            empty_layout.setVisibility(View.GONE);
        }else {
            empty_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void handleInvDetails(List<InventoryDetail> inventoryDetails) {

    }

    @Override
    public void handleAllAssetEpcs(List<EpcBean> allEpcs) {

    }

    @Override
    public void handleXfInvDetails(List<XfInventoryDetail> inventoryDetails) {
        mNotInvDetails.clear();
        mInvedDetails.clear();
        mCurrentDetails.clear();
        for (XfInventoryDetail inventoryDetail : inventoryDetails) {
            if (inventoryDetail.getInv_status() == 0) {
                mNotInvDetails.add(inventoryDetail);
            }else if (inventoryDetail.getInv_status() == 1) {
                mInvedDetails.add(inventoryDetail);
            }
        }
        if (currentShowStatus == 0) {
            mCurrentDetails.addAll(mNotInvDetails);
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
    public void onItemClick(XfInventoryDetail invDetailBean, TextView tvAddTag) {
    }
}
