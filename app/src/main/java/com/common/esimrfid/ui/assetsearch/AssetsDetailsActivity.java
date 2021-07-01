package com.common.esimrfid.ui.assetsearch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.emun.AssetsUseStatus;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;
import com.common.esimrfid.presenter.assetsearch.AssetsDetailsPresenter;
import com.common.esimrfid.ui.assetrepair.RepairAssetEvent;
import com.common.esimrfid.ui.newinventory.AssetTag;
import com.common.esimrfid.utils.CommonUtils;
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
    @BindView(R.id.tv_inv_sure)
    TextView assetInved;
    @BindView(R.id.search_ast)
    ImageView searchAsset;
    @BindView(R.id.rv_asset_details)
    RecyclerView assetDetailRecycle;
    private List<AssetResume> mResumeData = new ArrayList<>();//资产履历
    private List<AssetRepair> mRepairData = new ArrayList<>();//维保信息
    private AssetsResumeAdapter assetsResumeAdapter;
    private AssetsRepairAdapter assetsRepairAdapter;
    private AssetsListItemInfo repairAsset = new AssetsListItemInfo();
    private String activityFrom;
    private int status;
    private String epcCode;
    private OptionsPickerView pvCustomOptions;
    List<AssetTag> assetTags = new ArrayList<>();
    private String astId;
    private String mInvId;
    private String mLocId;
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
        mInvId = intent.getStringExtra(INV_ID);
        mLocId = intent.getStringExtra(LOC_IC);
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
        if ("InventoryTaskActivity".equals(EsimAndroidApp.activityFrom) && "notInvEdAsset".equals(EsimAndroidApp.invStatus)) {
            assetInved.setVisibility(View.VISIBLE);
            searchAsset.setVisibility(View.VISIBLE);
            initCustomOptionPicker();
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
            R.id.btn_submit, R.id.tv_inv_sure, R.id.search_ast})
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
            case R.id.tv_inv_sure:
                pvCustomOptions.setPicker(assetTags);
                pvCustomOptions.show();
                break;
            case R.id.search_ast:
                if(CommonUtils.isNormalClick()){
                    Intent intent = new Intent();
                    intent.putExtra(ASSETS_EPC, epcCode);
                    intent.setClass(this, LocationSearchActivity.class);
                    startActivity(intent);
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
            assetDetailItemList.add(new AssetDetailItem("使用状态", AssetsUseStatus.getName(assetsAllInfo.getAst_used_status())));
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
            epcCode = assetsAllInfo.getAst_epc_code();
            astId = assetsAllInfo.getId();
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        assetTags.clear();
        assetTags.add(new AssetTag("标签损坏"));
        assetTags.add(new AssetTag("标签丢失"));
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                optionSelect(options1);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancle);
                        TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
                        tvTitle.setText("添加标记");
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                //submitOption();
                                pvCustomOptions.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                    }
                })
                .setContentTextSize(14)
                .setLineSpacingMultiplier(2.0f)
                .isDialog(true)
                .setOutSideCancelable(true)
                .build();
        Dialog mDialog = pvCustomOptions.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvCustomOptions.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
            //设置dialog宽度沾满全屏
            Window window = mDialog.getWindow();
            // 把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            // 设置宽度
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }


    }

    private void optionSelect(int options1) {
        AssetTag assetTag = assetTags.get(options1);
      mPresenter.setOneAssetInved(assetTag.getTagName(),mInvId,mLocId,astId);
    }

    @Override
    public void handleSetOneAssetInved(Boolean result) {
        ToastUtils.showShort(R.string.inved_toast);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String assetsId = intent.getStringExtra(ASSETS_ID);
        String assetsCode = intent.getStringExtra(ASSETS_CODE);
        activityFrom = intent.getStringExtra(WHERE_FROM);
        mInvId = intent.getStringExtra(INV_ID);
        mLocId = intent.getStringExtra(LOC_IC);
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
        if ("InventoryTaskActivity".equals(EsimAndroidApp.activityFrom) && "notInvEdAsset".equals(EsimAndroidApp.invStatus)) {
            assetInved.setVisibility(View.VISIBLE);
            searchAsset.setVisibility(View.VISIBLE);
            initCustomOptionPicker();
        }
    }
}
