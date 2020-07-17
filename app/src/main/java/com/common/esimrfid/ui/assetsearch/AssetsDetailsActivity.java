package com.common.esimrfid.ui.assetsearch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.emun.AssetsMaterial;
import com.common.esimrfid.core.bean.emun.AssetsUseStatus;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.presenter.assetsearch.AssetsDetailsPresenter;
import com.common.esimrfid.ui.assetrepair.RepairAssetEvent;
import com.common.esimrfid.ui.newinventory.AssetTag;
import com.common.esimrfid.utils.DateUtils;
import com.common.esimrfid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AssetsDetailsActivity extends BaseActivity<AssetsDetailsPresenter> implements AssetsDetailsContract.View {

    private static final String TAG = "AssetsDetailsActivity";
    private static final String ASSETS_ID = "assets_id";
    private static final String ASSETS_CODE = "assets_code";
    private static final String WHERE_FROM = "where_from";
    private static final String ASSETS_EPC = "assets_epc";
    public static final String INV_ID = "inv_id";
    public static final String LOC_IC = "loc_id";
    @BindView(R.id.ast_code)
    TextView barcode;
    @BindView(R.id.ast_name)
    TextView astName;
    @BindView(R.id.ast_type)
    TextView astType;
    @BindView(R.id.tv_brand)
    TextView astBrand;
    @BindView(R.id.ast_status)
    TextView ast_status;
    @BindView(R.id.rule_model)
    TextView ast_model;
    @BindView(R.id.measuring_unit)
    TextView ast_unit;
    @BindView(R.id.sn)
    TextView sn;
    @BindView(R.id.ast_material)
    TextView ast_material;
    @BindView(R.id.belong_company)
    TextView belong_crop;
    @BindView(R.id.stroe_loaction)
    TextView stroe_loaction;
    @BindView(R.id.manager)
    TextView manager;
    @BindView(R.id.source)
    TextView source;
    @BindView(R.id.buy_date)
    TextView ast_buy_date;
    @BindView(R.id.count)
    TextView ast_count;
    @BindView(R.id.expiration_use_months)
    TextView use_months;
    @BindView(R.id.use_company)
    TextView use_company;
    @BindView(R.id.use_department)
    TextView use_department;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.requisition_date)
    TextView requisition_date;
    @BindView(R.id.remark)
    TextView remark;
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
    LinearLayout li_assetDetail;
    @BindView(R.id.maintenance_info)
    LinearLayout li_maintenance;
    @BindView(R.id.li_repair_record)
    LinearLayout li_repair;
    @BindView(R.id.li_asset_resume)
    LinearLayout li_resume;
    @BindView(R.id.assets_detail_content)
    LinearLayout detail_content;
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
    private List<AssetResume> mResumeData = new ArrayList<>();//资产履历
    private List<AssetRepair> mRepairData = new ArrayList<>();//维保信息
    private AssetsResumeAdapter assetsResumeAdapter;
    private AssetsRepairAdapter assetsRepairAdapter;
    private AssetsInfo repairAsset = new AssetsInfo();
    private String activityFrom;
    private int status;
    private String epcCode;
    private OptionsPickerView pvCustomOptions;
    List<AssetTag> assetTags = new ArrayList<>();
    private String astId;
    private String mInvId;
    private String mLocId;

    @Override
    public AssetsDetailsPresenter initPresenter() {
        return new AssetsDetailsPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        title.setText(R.string.assets_details);
        Intent intent = getIntent();
        String assetsId = intent.getStringExtra(ASSETS_ID);
        mInvId = intent.getStringExtra(INV_ID);
        mLocId = intent.getStringExtra(LOC_IC);
        if (assetsId != null && !assetsId.isEmpty()) {
            mPresenter.getAssetsDetailsById(assetsId, null);
            mPresenter.getAssetsResumeById(assetsId, null);
            mPresenter.getAssetsRepairById(assetsId, null);
        }
        String assetsCode = intent.getStringExtra(ASSETS_CODE);
        if (assetsCode != null && !assetsCode.isEmpty()) {
            mPresenter.getAssetsDetailsById(null, assetsCode);
            mPresenter.getAssetsResumeById(null, assetsCode);
            mPresenter.getAssetsRepairById(null, assetsCode);
        }
        activityFrom = intent.getStringExtra(WHERE_FROM);
        empty_page.setVisibility(View.VISIBLE);
        li_assetDetail.setVisibility(View.GONE);
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
                li_assetDetail.setVisibility(View.VISIBLE);
                li_maintenance.setVisibility(View.GONE);
                li_repair.setVisibility(View.GONE);
                li_resume.setVisibility(View.GONE);
                break;
            case R.id.mainten_info:
                title_tab.clearCheck();
                title_tab.check(R.id.mainten_info);
                empty_page.setVisibility(View.GONE);
                li_assetDetail.setVisibility(View.GONE);
                li_maintenance.setVisibility(View.VISIBLE);
                li_repair.setVisibility(View.GONE);
                li_resume.setVisibility(View.GONE);
                break;
            case R.id.repair_record:
                title_tab.clearCheck();
                title_tab.check(R.id.repair_record);
                if (mRepairData.isEmpty()) {
                    empty_page.setVisibility(View.VISIBLE);
                    li_assetDetail.setVisibility(View.GONE);
                    li_maintenance.setVisibility(View.GONE);
                    li_repair.setVisibility(View.GONE);
                    li_resume.setVisibility(View.GONE);
                } else {
                    empty_page.setVisibility(View.GONE);
                    li_assetDetail.setVisibility(View.GONE);
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
                    li_assetDetail.setVisibility(View.GONE);
                    li_maintenance.setVisibility(View.GONE);
                    li_repair.setVisibility(View.GONE);
                    li_resume.setVisibility(View.GONE);
                } else {
                    empty_page.setVisibility(View.GONE);
                    li_assetDetail.setVisibility(View.GONE);
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
                    List<AssetsInfo> assetsInfos = new ArrayList<>();
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
                Intent intent = new Intent();
                intent.putExtra(ASSETS_EPC, epcCode);
                intent.setClass(this, LocationSearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void handleAssetsDetails(AssetsAllInfo assetsAllInfo) {
        if (assetsAllInfo != null) {
            title_tab.check(R.id.asset_detail);
            empty_page.setVisibility(View.GONE);
            li_assetDetail.setVisibility(View.VISIBLE);
            String code = TextUtils.isEmpty(assetsAllInfo.getAst_barcode()) ? "" : assetsAllInfo.getAst_barcode();
            barcode.setText(code);
            String name = TextUtils.isEmpty(assetsAllInfo.getAst_name()) ? "" : assetsAllInfo.getAst_name();
            astName.setText(name);
            String type = assetsAllInfo.getType_info() == null ? "" : assetsAllInfo.getType_info().getType_name();
            type = TextUtils.isEmpty(type) ? "" : type;
            astType.setText(type);

            String brand = TextUtils.isEmpty(assetsAllInfo.getAst_brand()) ? "" : assetsAllInfo.getAst_brand();
            astBrand.setText(brand);
            status = assetsAllInfo.getAst_used_status();
            String statusName = TextUtils.isEmpty(AssetsUseStatus.getName(status)) ? "" : AssetsUseStatus.getName(status);
            ast_status.setText(statusName);
            String model = TextUtils.isEmpty(assetsAllInfo.getAst_model()) ? "" : assetsAllInfo.getAst_model();
            ast_model.setText(model);
            String unit = TextUtils.isEmpty(assetsAllInfo.getAst_measuring_unit()) ? "" : assetsAllInfo.getAst_measuring_unit();
            ast_unit.setText(unit);
            String ast_sn = TextUtils.isEmpty(assetsAllInfo.getAst_sn()) ? "" : assetsAllInfo.getAst_sn();
            sn.setText(ast_sn);

            int number = assetsAllInfo.getAst_material();
            String material = TextUtils.isEmpty(AssetsMaterial.getName(number)) ? "" : AssetsMaterial.getName(number);
            ast_material.setText(material);

            String corp = assetsAllInfo.getOrg_belongcorp() == null ? "" : assetsAllInfo.getOrg_belongcorp().getOrg_name();
            corp = TextUtils.isEmpty(code) ? "" : corp;
            belong_crop.setText(corp);

            String location = assetsAllInfo.getLoc_info() == null ? "" : assetsAllInfo.getLoc_info().getLoc_name();
            location = TextUtils.isEmpty(location) ? "" : location;
            stroe_loaction.setText(location);

            String person = assetsAllInfo.getCreator() == null ? "" : assetsAllInfo.getCreator().getUser_real_name();
            person = TextUtils.isEmpty(person) ? "" : person;
            manager.setText(person);

            String from = TextUtils.isEmpty(assetsAllInfo.getAst_source()) ? "" : assetsAllInfo.getAst_source();
            source.setText(from);

            //资产购买日期
            long buy_date = assetsAllInfo.getAst_buy_date();
            if (buy_date == 0) {
                ast_buy_date.setText("");
            } else {
                ast_buy_date.setText(DateUtils.long2String(buy_date, DateUtils.FORMAT_TYPE_1));
            }

            double count = assetsAllInfo.getAst_price();
            NumberFormat nf = new DecimalFormat("¥#,###.##");//设置金额显示格式
            String str = nf.format(count);
            if (count == 0) {
                ast_count.setText("");
            } else {
                ast_count.setText(str + "元");
            }

            String month = assetsAllInfo.getAst_expiration_months();
            if (TextUtils.isEmpty(month)) {
                use_months.setText("");
            } else {
                use_months.setText(month + "个月");
            }
//            String month = TextUtils.isEmpty(assetsAllInfo.getAst_expiration_months()) ? "" : assetsAllInfo.getAst_expiration_months();
//            use_months.setText(month+"个月");

            String company = assetsAllInfo.getOrg_usedcorp() == null ? "" : assetsAllInfo.getOrg_usedcorp().getOrg_name();
            company = TextUtils.isEmpty(company) ? "" : company;
            use_company.setText(company);

            String department = assetsAllInfo.getOrg_useddept() == null ? "" : assetsAllInfo.getOrg_useddept().getOrg_name();
            department = TextUtils.isEmpty(department) ? "" : department;
            use_department.setText(department);

            String Name = assetsAllInfo.getUser_info() == null ? "" : assetsAllInfo.getUser_info().getUser_real_name();
            Name = TextUtils.isEmpty(Name) ? "" : Name;
            user.setText(Name);

            long req_date = assetsAllInfo.getAst_req_date();
            if (req_date == 0) {
                requisition_date.setText("");
            } else {
                requisition_date.setText(DateUtils.long2String(req_date, DateUtils.FORMAT_TYPE_1));
            }
            String text = TextUtils.isEmpty(assetsAllInfo.getAst_remark()) ? "" : assetsAllInfo.getAst_remark();
            remark.setText(text);

            //扩展字段信息
            if(assetsAllInfo.getAst_append_info() != null){
                int appendItems = assetsAllInfo.getAst_append_info().size();
                for (int n = 0; n < appendItems; n++) {
                    List<String> Keys = new ArrayList<>();
                    //取出所有的key值
                    Iterator i = assetsAllInfo.getAst_append_info().keySet().iterator();
                    while (i.hasNext()) {
                        String key = i.next().toString();
                        Keys.add(key);
                    }
                    String type_id = Keys.get(n);
                    String content = assetsAllInfo.getAst_append_info().get(type_id);
                    LinearLayout linearLayout = new LinearLayout(this);
                    TextView textView1 = new TextView(this);
                    TextView textView2 = new TextView(this);
                    View view = new View(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, dip2px(this, 45));
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, MATCH_PARENT, 1);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(MATCH_PARENT, dip2px(this, 1));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    textView1.setTextSize(14);
                    textView1.setText(type_id);
                    textView1.setGravity(Gravity.CENTER_VERTICAL);
                    textView2.setTextSize(14);
                    textView2.setText(content);
                    textView2.setGravity(Gravity.CENTER_VERTICAL);
                    view.setBackgroundColor(getColor(R.color.line));

                    //将控件加入布局
                    linearLayout.addView(textView1, layoutParams1);
                    linearLayout.addView(textView2, layoutParams2);
                    detail_content.addView(view, layoutParams3);
                    detail_content.addView(linearLayout, layoutParams);
                }
            }

            //维保信息
            String Supplier = assetsAllInfo.getWarranty_info() == null ? "" : assetsAllInfo.getWarranty_info().getSupplier_name();
            Supplier = TextUtils.isEmpty(Supplier) ? "" : Supplier;
            supplier.setText(Supplier);

            String s_name = assetsAllInfo.getWarranty_info() == null ? "" : assetsAllInfo.getWarranty_info().getSupplier_person();
            s_name = TextUtils.isEmpty(s_name) ? "" : s_name;
            contacts.setText(s_name);

            String figure = assetsAllInfo.getWarranty_info() == null ? "" : assetsAllInfo.getWarranty_info().getSupplier_mobile();
            figure = TextUtils.isEmpty(figure) ? "" : figure;
            contact_information.setText(figure);

            //维保日期
            long end_date = assetsAllInfo.getWarranty_info() == null ? 0 : assetsAllInfo.getWarranty_info().getWar_enddate();
            if (end_date == 0) {
                maintenance_expire.setText("");
            } else {
                maintenance_expire.setText(DateUtils.long2String(end_date, DateUtils.FORMAT_TYPE_1));
            }
            String message = assetsAllInfo.getWarranty_info() == null ? "" : assetsAllInfo.getWarranty_info().getWar_message();
            message = TextUtils.isEmpty(message) ? "" : message;
            instructions.setText(message);
            repairAsset.setId(assetsAllInfo.getId());
            repairAsset.setAst_used_status(assetsAllInfo.getAst_used_status());
            repairAsset.setAst_name(assetsAllInfo.getAst_name());
            repairAsset.setAst_barcode(assetsAllInfo.getAst_barcode());
            AssetsInfo.TypeInfo typeInfo = new AssetsInfo.TypeInfo();
            typeInfo.setType_name(type);
            repairAsset.setType_info(typeInfo);
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
}
