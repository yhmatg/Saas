package com.common.esimrfid.ui.assetsearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.emun.AssetsMaterial;
import com.common.esimrfid.core.bean.emun.AssetsUseStatus;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;
import com.common.esimrfid.presenter.assetsearch.AssetsDetailsPresenter;
import com.common.esimrfid.utils.DateUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AssetsDetailsActivity extends BaseActivity<AssetsDetailsPresenter> implements AssetsDetailsContract.View {

    private static final String TAG = "AssetsDetailsActivity";
    private static final String ASSETS_ID = "assets_id";
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
    private List<AssetResume> mResumeData = new ArrayList<>();//资产履历
    private List<AssetRepair> mRepairData=new ArrayList<>();//维保信息
    private AssetsResumeAdapter assetsResumeAdapter;
    private AssetsRepairAdapter assetsRepairAdapter;

    @Override
    public AssetsDetailsPresenter initPresenter() {
        return new AssetsDetailsPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        title.setText(R.string.assets_details);
        Intent intent = getIntent();
        String assetsId = intent.getStringExtra(ASSETS_ID);
        mPresenter.getAssetsDetailsById(assetsId);
        mPresenter.getAssetsResumeById(assetsId);
        mPresenter.getAssetsRepairById(assetsId);
        empty_page.setVisibility(View.VISIBLE);
        li_assetDetail.setVisibility(View.GONE);
        li_maintenance.setVisibility(View.GONE);
        li_repair.setVisibility(View.GONE);
        li_resume.setVisibility(View.GONE);
        assetsResumeAdapter = new AssetsResumeAdapter(this, mResumeData);
        resume_recycler.setLayoutManager(new LinearLayoutManager(this));
        resume_recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        resume_recycler.setAdapter(assetsResumeAdapter);

        assetsRepairAdapter=new AssetsRepairAdapter(this,mRepairData);
        repair_recycler.setLayoutManager(new LinearLayoutManager(this));
        repair_recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        repair_recycler.setAdapter(assetsRepairAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assets_detail;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.title_back, R.id.asset_detail, R.id.mainten_info, R.id.repair_record, R.id.asset_resume, R.id.asset_tab})
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
                empty_page.setVisibility(View.GONE);
                li_assetDetail.setVisibility(View.GONE);
                li_maintenance.setVisibility(View.GONE);
                li_repair.setVisibility(View.VISIBLE);
                li_resume.setVisibility(View.GONE);
                break;
            case R.id.asset_resume:
                title_tab.clearCheck();
                title_tab.check(R.id.asset_resume);
                empty_page.setVisibility(View.GONE);
                li_assetDetail.setVisibility(View.GONE);
                li_maintenance.setVisibility(View.GONE);
                li_repair.setVisibility(View.GONE);
                li_resume.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void handleAssetsDetails(AssetsDetailsInfo assetsDetailsInfo) {
        if (assetsDetailsInfo != null) {
            title_tab.check(R.id.asset_detail);
            empty_page.setVisibility(View.GONE);
            li_assetDetail.setVisibility(View.VISIBLE);
            String code = TextUtils.isEmpty(assetsDetailsInfo.getAst_barcode()) ? "" : assetsDetailsInfo.getAst_barcode();
            barcode.setText(code);
            String name = TextUtils.isEmpty(assetsDetailsInfo.getAst_name()) ? "" : assetsDetailsInfo.getAst_name();
            astName.setText(name);

            String type = assetsDetailsInfo.getType_info() == null ? "" : assetsDetailsInfo.getType_info().getType_name();
            type = TextUtils.isEmpty(type) ? "" : type;
            astType.setText(type);

            String brand = TextUtils.isEmpty(assetsDetailsInfo.getAst_brand()) ? "" : assetsDetailsInfo.getAst_brand();
            astBrand.setText(brand);
            int status = assetsDetailsInfo.getAst_used_status();
            String statusName = TextUtils.isEmpty(AssetsUseStatus.getName(status)) ? "" : AssetsUseStatus.getName(status);
            ast_status.setText(statusName);
            String model = TextUtils.isEmpty(assetsDetailsInfo.getAst_model()) ? "" : assetsDetailsInfo.getAst_model();
            ast_model.setText(model);
            String unit = TextUtils.isEmpty(assetsDetailsInfo.getAst_measuring_unit()) ? "" : assetsDetailsInfo.getAst_measuring_unit();
            ast_unit.setText(unit);
            String ast_sn = TextUtils.isEmpty(assetsDetailsInfo.getAst_sn()) ? "" : assetsDetailsInfo.getAst_sn();
            sn.setText(ast_sn);

            int number = assetsDetailsInfo.getAst_material();
            String material = TextUtils.isEmpty(AssetsMaterial.getName(number)) ? "" : AssetsMaterial.getName(number);
            ast_material.setText(material);

            String corp = assetsDetailsInfo.getOrg_belongcorp() == null ? "" : assetsDetailsInfo.getOrg_belongcorp().getOrg_name();
            corp = TextUtils.isEmpty(code) ? "" : corp;
            belong_crop.setText(corp);

            String location = assetsDetailsInfo.getLoc_info() == null ? "" : assetsDetailsInfo.getLoc_info().getLoc_name();
            location = TextUtils.isEmpty(location) ? "" : location;
            stroe_loaction.setText(location);

            String person = assetsDetailsInfo.getCreator() == null ? "" : assetsDetailsInfo.getCreator().getUser_real_name();
            person = TextUtils.isEmpty(person) ? "" : person;
            manager.setText(person);

            String from = TextUtils.isEmpty(assetsDetailsInfo.getAst_source()) ? "" : assetsDetailsInfo.getAst_source();
            source.setText(from);

            //资产购买日期
            long buy_date = assetsDetailsInfo.getAst_buy_date();
            if (buy_date == 0) {
                ast_buy_date.setText("");
            } else {
                ast_buy_date.setText(DateUtils.long2String(buy_date, DateUtils.FORMAT_TYPE_1));
            }

            double count = assetsDetailsInfo.getAst_price();
            NumberFormat nf = new DecimalFormat("¥#,###.##");//设置金额显示格式
            String str = nf.format(count);
            if (count == 0) {
                ast_count.setVisibility(View.GONE);
            } else {
                ast_count.setText(str + "元");
            }

            String month = assetsDetailsInfo.getAst_expiration_months();
            if (TextUtils.isEmpty(month)) {
                use_months.setText("");
            } else {
                use_months.setText(month + "个月");
            }
//            String month = TextUtils.isEmpty(assetsDetailsInfo.getAst_expiration_months()) ? "" : assetsDetailsInfo.getAst_expiration_months();
//            use_months.setText(month+"个月");

            String company = assetsDetailsInfo.getOrg_usedcorp() == null ? "" : assetsDetailsInfo.getOrg_usedcorp().getOrg_name();
            company = TextUtils.isEmpty(company) ? "" : company;
            use_company.setText(company);

            String department = assetsDetailsInfo.getOrg_useddept() == null ? "" : assetsDetailsInfo.getOrg_useddept().getOrg_name();
            department = TextUtils.isEmpty(department) ? "" : department;
            use_department.setText(department);

            String Name = assetsDetailsInfo.getUser_info() == null ? "" : assetsDetailsInfo.getUser_info().getUser_real_name();
            Name = TextUtils.isEmpty(Name) ? "" : Name;
            user.setText(Name);

            long req_date = assetsDetailsInfo.getAst_req_date();
            if (req_date == 0) {
                requisition_date.setText("");
            } else {
                requisition_date.setText(DateUtils.long2String(req_date, DateUtils.FORMAT_TYPE_1));
            }
            String text = TextUtils.isEmpty(assetsDetailsInfo.getAst_remark()) ? "" : assetsDetailsInfo.getAst_remark();
            remark.setText(text);

            //扩展字段信息
            int appendItems = assetsDetailsInfo.getAst_append_info().size();
            for (int n = 0; n < appendItems; n++) {
                List<String> Keys = new ArrayList<>();
                //取出所有的key值
                Iterator i = assetsDetailsInfo.getAst_append_info().keySet().iterator();
                while (i.hasNext()) {
                    String key = i.next().toString();
                    Keys.add(key);
                }
                String type_id = Keys.get(n);
                String content = assetsDetailsInfo.getAst_append_info().get(type_id);
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

            //维保信息
            String Supplier = assetsDetailsInfo.getWarranty_info() == null ? "" : assetsDetailsInfo.getWarranty_info().getSupplier_name();
            Supplier = TextUtils.isEmpty(Supplier) ? "" : Supplier;
            supplier.setText(Supplier);

            String s_name = assetsDetailsInfo.getWarranty_info() == null ? "" : assetsDetailsInfo.getWarranty_info().getSupplier_person();
            s_name = TextUtils.isEmpty(s_name) ? "" : s_name;
            contacts.setText(s_name);

            String figure = assetsDetailsInfo.getWarranty_info() == null ? "" : assetsDetailsInfo.getWarranty_info().getSupplier_mobile();
            figure = TextUtils.isEmpty(figure) ? "" : figure;
            contact_information.setText(figure);

            //维保日期
            long end_date = assetsDetailsInfo.getWarranty_info().getWar_enddate();
            if (end_date == 0) {
                maintenance_expire.setText("");
            } else {
                maintenance_expire.setText(DateUtils.long2String(end_date, DateUtils.FORMAT_TYPE_1));
            }
            String message = assetsDetailsInfo.getWarranty_info() == null ? "" : assetsDetailsInfo.getWarranty_info().getWar_message();
            message = TextUtils.isEmpty(message) ? "" : message;
            instructions.setText(message);

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
        mRepairData.addAll(assetRepairs);
        assetsRepairAdapter.notifyDataSetChanged();
    }
}
