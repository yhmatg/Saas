package com.common.esimrfid.ui.assetsearch;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.emun.AssetsUseStatus;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;
import com.common.esimrfid.presenter.assetsearch.AssetsDetailsPresenter;
import com.common.esimrfid.ui.assetrepair.RepairAssetEvent;
import com.common.esimrfid.utils.DateUtils;
import com.common.esimrfid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AssetsDetailsActivity extends BaseActivity<AssetsDetailsPresenter> implements AssetsDetailsContract.View {

    private static final String TAG = "AssetsDetailsActivity";
    private static final String ASSETS_ID = "assets_id";
    private static final String ASSETS_CODE = "assets_code";
    private static final String WHERE_FROM = "where_from";
    private static final String ASSETS_EPC = "assets_epc";
    public static final String INV_ID = "inv_id";
    public static final String LOC_IC = "loc_id";
    @BindView(R.id.supplier)
    TextView supplier;
    @BindView(R.id.contacts)
    TextView contacts;
    @BindView(R.id.contact_information)
    TextView contact_information;
    @BindView(R.id.maintenance_expiration)
    TextView maintenance_expire;
    @BindView(R.id.maintenance_instructions)
    TextView instructions;
    @BindView(R.id.empty_page)
    LinearLayout empty_page;
    @BindView(R.id.title_back)
    ImageView titleLeft;
    @BindView(R.id.title_content)
    TextView title;
    @BindView(R.id.asset_tab)
    RadioGroup title_tab;
    @BindView(R.id.asset_detail)
    RadioButton asset_detail;
    @BindView(R.id.mainten_info)
    RadioButton mainten_info;
    @BindView(R.id.repair_record)
    RadioButton record;
    @BindView(R.id.asset_resume)
    RadioButton resume;
    @BindView(R.id.assets_detail)
    RelativeLayout rv_assetDetail;
    @BindView(R.id.maintenance_info)
    LinearLayout li_maintenance;
    @BindView(R.id.li_repair_record)
    LinearLayout li_repair;
    @BindView(R.id.li_asset_resume)
    LinearLayout li_resume;
    @BindView(R.id.resume_recycler)
    RecyclerView resume_recycler;
    @BindView(R.id.repair_recycler)
    RecyclerView repair_recycler;
    @BindView(R.id.btn_submit)
    Button addButton;
    @BindView(R.id.rv_asset_details)
    RecyclerView assetDetailRecycle;
    private List<AssetResume> mResumeData = new ArrayList<>();//资产履历
    private List<AssetRepair> mRepairData = new ArrayList<>();//维保信息
    private AssetsResumeAdapter assetsResumeAdapter;
    private AssetsRepairAdapter assetsRepairAdapter;
    private AssetsListItemInfo repairAsset = new AssetsListItemInfo();
    private String activityFrom;
    private int status;
    private List<AssetDetailItem> assetDetailItemList = new ArrayList<>();
    private AssetDetailItemAdapter assetDetailItemAdapter;

    @Override
    public AssetsDetailsPresenter initPresenter() {
        return new AssetsDetailsPresenter();
    }

    @Override
    protected void initEventAndData() {
        title.setText(R.string.assets_details);
        Intent intent = getIntent();
        String assetsId = intent.getStringExtra(ASSETS_ID);
        String assetsCode = intent.getStringExtra(ASSETS_CODE);
        activityFrom = intent.getStringExtra(WHERE_FROM);
        mPresenter.getAssetsDetailsById(assetsId, assetsCode, activityFrom);
        mPresenter.getAssetsResumeById(assetsId, assetsCode);
        mPresenter.getAssetsRepairById(assetsId, assetsCode);
        empty_page.setVisibility(View.VISIBLE);
        rv_assetDetail.setVisibility(View.GONE);
        li_maintenance.setVisibility(View.GONE);
        li_repair.setVisibility(View.GONE);
        li_resume.setVisibility(View.GONE);
        assetsResumeAdapter = new AssetsResumeAdapter(this, mResumeData);
        resume_recycler.setLayoutManager(new LinearLayoutManager(this));
        assetsRepairAdapter = new AssetsRepairAdapter(this, mRepairData);
        repair_recycler.setLayoutManager(new LinearLayoutManager(this));
        //资产明细
        assetDetailItemAdapter = new AssetDetailItemAdapter(this,assetDetailItemList);
        assetDetailRecycle.setLayoutManager(new LinearLayoutManager(this));
        assetDetailRecycle.setAdapter(assetDetailItemAdapter);
        if (("AssetRepairActivity".equals(activityFrom))) {
            addButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assets_detail;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.title_back, R.id.asset_detail, R.id.mainten_info, R.id.repair_record, R.id.asset_resume, R.id.asset_tab,
            R.id.btn_submit})
    void perforeClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.asset_detail:
                title_tab.clearCheck();
                title_tab.check(R.id.asset_detail);
                empty_page.setVisibility(View.GONE);
                rv_assetDetail.setVisibility(View.VISIBLE);
                li_maintenance.setVisibility(View.GONE);
                li_repair.setVisibility(View.GONE);
                li_resume.setVisibility(View.GONE);
                break;
            case R.id.mainten_info:
                title_tab.clearCheck();
                title_tab.check(R.id.mainten_info);
                empty_page.setVisibility(View.GONE);
                rv_assetDetail.setVisibility(View.GONE);
                li_maintenance.setVisibility(View.VISIBLE);
                li_repair.setVisibility(View.GONE);
                li_resume.setVisibility(View.GONE);
                break;
            case R.id.repair_record:
                title_tab.clearCheck();
                title_tab.check(R.id.repair_record);
                if (mRepairData.isEmpty()) {
                    empty_page.setVisibility(View.VISIBLE);
                    rv_assetDetail.setVisibility(View.GONE);
                    li_maintenance.setVisibility(View.GONE);
                    li_repair.setVisibility(View.GONE);
                    li_resume.setVisibility(View.GONE);
                } else {
                    empty_page.setVisibility(View.GONE);
                    rv_assetDetail.setVisibility(View.GONE);
                    li_maintenance.setVisibility(View.GONE);
                    li_repair.setVisibility(View.VISIBLE);
                    li_resume.setVisibility(View.GONE);
                    repair_recycler.setAdapter(assetsRepairAdapter);
                }

                break;
            case R.id.asset_resume:
                title_tab.clearCheck();
                title_tab.check(R.id.asset_resume);
                if (mResumeData.isEmpty()) {
                    empty_page.setVisibility(View.VISIBLE);
                    rv_assetDetail.setVisibility(View.GONE);
                    li_maintenance.setVisibility(View.GONE);
                    li_repair.setVisibility(View.GONE);
                    li_resume.setVisibility(View.GONE);
                } else {
                    empty_page.setVisibility(View.GONE);
                    rv_assetDetail.setVisibility(View.GONE);
                    li_maintenance.setVisibility(View.GONE);
                    li_repair.setVisibility(View.GONE);
                    li_resume.setVisibility(View.VISIBLE);
                    resume_recycler.setAdapter(assetsResumeAdapter);
                }

                break;
            case R.id.btn_submit:
                if (!(status == 0 || status == 1)) {
                    ToastUtils.showShort(R.string.repair_toast_str);
                } else {
                    List<AssetsListItemInfo> assetsInfos = new ArrayList<>();
                    assetsInfos.add(repairAsset);
                    RepairAssetEvent repairAssetEvent = new RepairAssetEvent(assetsInfos);
                    EventBus.getDefault().post(repairAssetEvent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void handleAssetsDetails(AssetsAllInfo assetsAllInfo) {
        if (assetsAllInfo != null) {
            title_tab.check(R.id.asset_detail);
            empty_page.setVisibility(View.GONE);
            rv_assetDetail.setVisibility(View.VISIBLE);
            HashMap<String, String> ast_append_info = assetsAllInfo.getAst_append_info();
            assetDetailItemList.clear();
            status = assetsAllInfo.getAst_used_status();
            assetDetailItemList.add(new AssetDetailItem("使用状态", AssetsUseStatus.getName(status)));
            assetDetailItemList.add(new AssetDetailItem("付款状态",ast_append_info.get("付款状态")));
            assetDetailItemList.add(new AssetDetailItem("设备编号",assetsAllInfo.getAst_barcode()));
            assetDetailItemList.add(new AssetDetailItem("DAM资产大类",ast_append_info.get("DAM资产大类")));
            assetDetailItemList.add(new AssetDetailItem("DAM资产小类",ast_append_info.get("DAM资产小类")));
            assetDetailItemList.add(new AssetDetailItem("财务资产卡片号",assetsAllInfo.getAst_code()));
            assetDetailItemList.add(new AssetDetailItem("SAP资产分类",ast_append_info.get("SAP资产分类")));
            assetDetailItemList.add(new AssetDetailItem("资产描述",assetsAllInfo.getAst_name()));
            assetDetailItemList.add(new AssetDetailItem("资产主号说明",ast_append_info.get("资产主号说明")));
            assetDetailItemList.add(new AssetDetailItem("附加资产描述(规格型号)",assetsAllInfo.getAst_model()));
            assetDetailItemList.add(new AssetDetailItem("基本计量单位",assetsAllInfo.getAst_measuring_unit()));
            assetDetailItemList.add(new AssetDetailItem("公司代码",assetsAllInfo.getOrg_usedcorp_name()));
            assetDetailItemList.add(new AssetDetailItem("部门",assetsAllInfo.getOrg_useddept_name()));
            assetDetailItemList.add(new AssetDetailItem("资产保管人",assetsAllInfo.getUser_name()));
            assetDetailItemList.add(new AssetDetailItem("资产管理员",assetsAllInfo.getManager_name()));
            assetDetailItemList.add(new AssetDetailItem("存放位置",assetsAllInfo.getLoc_name()));
            assetDetailItemList.add(new AssetDetailItem("存放描述",ast_append_info.get("存放描述")));
            assetDetailItemList.add(new AssetDetailItem("成本中心",ast_append_info.get("责任成本中心")));
            assetDetailItemList.add(new AssetDetailItem("功能范围",ast_append_info.get("功能范围")));
            assetDetailItemList.add(new AssetDetailItem("业务范围",ast_append_info.get("业务范围")));
            assetDetailItemList.add(new AssetDetailItem("内部订单",ast_append_info.get("内部订单")));
            assetDetailItemList.add(new AssetDetailItem("资产来源",assetsAllInfo.getAst_source()));
            assetDetailItemList.add(new AssetDetailItem("合同号",ast_append_info.get("合同号")));
            assetDetailItemList.add(new AssetDetailItem("供应商编码",ast_append_info.get("供应商编码")));
            assetDetailItemList.add(new AssetDetailItem("事业部",ast_append_info.get("事业部")));
            assetDetailItemList.add(new AssetDetailItem("工厂代码",ast_append_info.get("工厂代码")));
            assetDetailItemList.add(new AssetDetailItem("是否标准设备",ast_append_info.get("是否标准设备")));
            assetDetailItemList.add(new AssetDetailItem("机房",ast_append_info.get("机房")));
            assetDetailItemList.add(new AssetDetailItem("机柜",ast_append_info.get("机柜")));
            assetDetailItemList.add(new AssetDetailItem("机位",ast_append_info.get("机位")));
            assetDetailItemList.add(new AssetDetailItem("SN号",assetsAllInfo.getAst_sn()));
            assetDetailItemList.add(new AssetDetailItem("IP地址",ast_append_info.get("IP地址")));
            assetDetailItemList.add(new AssetDetailItem("资产产权证号",ast_append_info.get("资产产权证号")));
            assetDetailItemList.add(new AssetDetailItem("房产面积",ast_append_info.get("房产面积")));
            assetDetailItemList.add(new AssetDetailItem("是否需缴房产税",ast_append_info.get("是否需缴房产税")));
            assetDetailItemList.add(new AssetDetailItem("MRO订单行ID",ast_append_info.get("MRO订单行ID")));
            assetDetailItemList.add(new AssetDetailItem("原FAM设备编码",ast_append_info.get("原FAM设备编码")));
            assetDetailItemList.add(new AssetDetailItem("创建人",assetsAllInfo.getCreator_name()));
            assetDetailItemList.add(new AssetDetailItem("创建时间",DateUtils.long2String(assetsAllInfo.getCreate_date(),"yyyy-MM-dd")));
            assetDetailItemAdapter.notifyDataSetChanged();
            //维保信息
            String Supplier = assetsAllInfo.getSupplier_name() == null ? "" : assetsAllInfo.getSupplier_name();
            Supplier = TextUtils.isEmpty(Supplier) ? "" : Supplier;
            supplier.setText(Supplier);

            String s_name = assetsAllInfo.getSupplier_person() == null ? "" : assetsAllInfo.getSupplier_person();
            s_name = TextUtils.isEmpty(s_name) ? "" : s_name;
            contacts.setText(s_name);

            String figure = assetsAllInfo.getSupplier_mobile() == null ? "" : assetsAllInfo.getSupplier_mobile();
            figure = TextUtils.isEmpty(figure) ? "" : figure;
            contact_information.setText(figure);

            //维保日期
            long end_date = assetsAllInfo.getWar_enddate();
            if (end_date == 0) {
                maintenance_expire.setText("");
            } else {
                maintenance_expire.setText(DateUtils.long2String(end_date, DateUtils.FORMAT_TYPE_1));
            }
            String message = assetsAllInfo.getWar_message() == null ? "" : assetsAllInfo.getWar_message();
            message = TextUtils.isEmpty(message) ? "" : message;
            instructions.setText(message);
            //扫码添加报修资产
            String type = assetsAllInfo.getType_name() == null ? "" : assetsAllInfo.getType_name();
            type = TextUtils.isEmpty(type) ? "" : type;
            repairAsset.setId(assetsAllInfo.getId());
            repairAsset.setAst_used_status(assetsAllInfo.getAst_used_status());
            repairAsset.setAst_name(assetsAllInfo.getAst_name());
            repairAsset.setAst_barcode(assetsAllInfo.getAst_barcode());
            repairAsset.setType_name(type);
            repairAsset.setAst_brand(assetsAllInfo.getAst_brand());
            repairAsset.setAst_model(assetsAllInfo.getAst_model());
        }
    }

    @Override
    public void handleAssetsResume(List<AssetResume> data) {
        mResumeData.clear();
        mResumeData.addAll(data);
        assetsResumeAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleAssetsRepair(List<AssetRepair> assetRepairs) {
        mRepairData.clear();
        Collections.reverse(assetRepairs);
        mRepairData.addAll(assetRepairs);
        assetsRepairAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleAssetsNoDetail() {
        addButton.setVisibility(View.GONE);
        finish();
    }

    @Override
    public void handleSetOneAssetInved(Boolean result) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String assetsId = intent.getStringExtra(ASSETS_ID);
        String assetsCode = intent.getStringExtra(ASSETS_CODE);
        activityFrom = intent.getStringExtra(WHERE_FROM);
        mPresenter.getAssetsDetailsById(assetsId, assetsCode, activityFrom);
        mPresenter.getAssetsResumeById(assetsId, assetsCode);
        mPresenter.getAssetsRepairById(assetsId, assetsCode);
        empty_page.setVisibility(View.VISIBLE);
        rv_assetDetail.setVisibility(View.GONE);
        li_maintenance.setVisibility(View.GONE);
        li_repair.setVisibility(View.GONE);
        li_resume.setVisibility(View.GONE);
        assetsResumeAdapter = new AssetsResumeAdapter(this, mResumeData);
        resume_recycler.setLayoutManager(new LinearLayoutManager(this));
        assetsRepairAdapter = new AssetsRepairAdapter(this, mRepairData);
        repair_recycler.setLayoutManager(new LinearLayoutManager(this));
        if (("AssetRepairActivity".equals(activityFrom))) {
            addButton.setVisibility(View.VISIBLE);
        }
    }
}
