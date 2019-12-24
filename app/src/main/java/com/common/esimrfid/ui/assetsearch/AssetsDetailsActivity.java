package com.common.esimrfid.ui.assetsearch;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.AssetsMaterial;
import com.common.esimrfid.core.bean.emun.AssetsUseStatus;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsDetailsInfo;
import com.common.esimrfid.presenter.assetsearch.AssetsDetailsPresenter;
import com.common.esimrfid.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.assets_content)
    LinearLayout content;
    @BindView(R.id.empty_page)
    LinearLayout empty_page;
    @BindView(R.id.title_back)
    ImageView titleLeft;
    @BindView(R.id.title_content)
    TextView title;
    private List<AssetsDetailsInfo> mData = new ArrayList<>();
    private String assetsId;

    @Override
    public AssetsDetailsPresenter initPresenter() {
        return new AssetsDetailsPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        title.setText(R.string.assets_details);
        Intent intent = getIntent();
        assetsId = intent.getStringExtra(ASSETS_ID);
        mPresenter.getAssetsDetailsById(assetsId);
        empty_page.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assets_detail;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.title_back})
    void perforeClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    @Override
    public void handleAssetsDetails(AssetsDetailsInfo assetsDetailsInfo) {
        if (assetsDetailsInfo != null) {
            empty_page.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
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

            long buy_date = assetsDetailsInfo.getAst_buy_date();
            ast_buy_date.setText(DateUtils.long2String(buy_date, DateUtils.FORMAT_TYPE_1));

            int count = assetsDetailsInfo.getAst_price();
            if (count == 0) {
                ast_count.setVisibility(View.GONE);
            } else {
                ast_count.setText(String.valueOf(count));
            }

            String month = TextUtils.isEmpty(assetsDetailsInfo.getAst_expiration_months()) ? "" : assetsDetailsInfo.getAst_expiration_months();
            use_months.setText(month);

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
            requisition_date.setText(DateUtils.long2String(req_date, DateUtils.FORMAT_TYPE_1));

            String text = TextUtils.isEmpty(assetsDetailsInfo.getAst_remark()) ? "" : assetsDetailsInfo.getAst_remark();
            remark.setText(text);
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

            Long end_date = assetsDetailsInfo.getWarranty_info().getWar_enddate();
            maintenance_expire.setText(DateUtils.long2String(end_date, DateUtils.FORMAT_TYPE_1));

            String message = assetsDetailsInfo.getWarranty_info() == null ? "" : assetsDetailsInfo.getWarranty_info().getWar_message();
            message = TextUtils.isEmpty(message) ? "" : message;
            instructions.setText(message);

        }
    }
}
