package com.common.esimrfid.xfxj.identity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.presenter.assetsearch.AssetsDetailsPresenter;
import com.common.esimrfid.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class XfAssetsDetailsActivity extends BaseActivity<AssetsDetailsPresenter> implements AssetsDetailsContract.View {

    private static final String TAG = "AssetsDetailsActivity";
    private static final String ASSETS_ID = "assets_id";
    private static final String ASSETS_CODE = "assets_code";
    public static final String INV_ID = "inv_id";
    @BindView(R.id.title_content)
    TextView title;
    @BindView(R.id.tv_asset_num)
    TextView astName;
    @BindView(R.id.tv_ast_loc_name)
    TextView locName;
    @BindView(R.id.tv_depart_name)
    TextView companyName;
    @BindView(R.id.tv_asset_num1)
    TextView astCode1;
    @BindView(R.id.tv_device_name1)
    TextView deviceName1;
    @BindView(R.id.tv_ast_loc_name1)
    TextView locName1;
    @BindView(R.id.tv_depart_name1)
    TextView companyName1;
    @BindView(R.id.tv_person_content)
    TextView perContent;
    @BindView(R.id.tv_date_content)
    TextView dateContent;
    @BindView(R.id.tv_result_content)
    TextView resultContent;
    @BindView(R.id.tv_asset_num2)
    TextView assetNum2;
    @BindView(R.id.tv_device_name2)
    TextView deviceName2;
    @BindView(R.id.tv_ast_loc_name2)
    TextView locName2;
    @BindView(R.id.tv_depart_name2)
    TextView departName2;
    @BindView(R.id.ev_repair_direc)
    EditText remarkText;
    @BindView(R.id.asset_tab)
    RadioGroup title_tab;
    @BindView(R.id.zhijia_group)
    RadioGroup zhijiaGroup;
    @BindView(R.id.yali_group)
    RadioGroup yaliGroup;
    @BindView(R.id.qianfen_group)
    RadioGroup qianfenGroup;
    @BindView(R.id.qinjie_group)
    RadioGroup qinjieGroup;
    @BindView(R.id.time_group)
    RadioGroup timeGroup;
    @BindView(R.id.empty_page)
    LinearLayout empty_page;
    @BindView(R.id.assets_detail)
    RelativeLayout li_assetDetail;
    @BindView(R.id.maintenance_info)
    RelativeLayout li_maintenance;
    @BindView(R.id.li_repair_record)
    RelativeLayout li_repair;
    @BindView(R.id.li_asset_resume)
    RelativeLayout li_resume;
    private String mInvId;
    private String assetsCode;
    XfInventoryDetail mInvItemDetail;

    @Override
    public AssetsDetailsPresenter initPresenter() {
        return new AssetsDetailsPresenter();
    }

    @Override
    protected void initEventAndData() {
        title.setText(R.string.assets_details);
        Intent intent = getIntent();
        mInvId = intent.getStringExtra(INV_ID);
        assetsCode = intent.getStringExtra(ASSETS_CODE);
        zhijiaGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_normal1){
                    mInvItemDetail.setZhiJia(0);
                }else if(checkedId == R.id.rb_unNormal1){
                    mInvItemDetail.setZhiJia(1);
                }
            }
        });
        yaliGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_normal2){
                    mInvItemDetail.setYaLi(0);
                }else if(checkedId == R.id.rb_unNormal2){
                    mInvItemDetail.setYaLi(1);
                }
            }
        });
        qianfenGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_normal3){
                    mInvItemDetail.setQianFen(0);
                }else if(checkedId == R.id.rb_unNormal3){
                    mInvItemDetail.setQianFen(1);
                }
            }
        });
        qinjieGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_normal4){
                    mInvItemDetail.setQingJie(0);
                }else if(checkedId == R.id.rb_unNormal4){
                    mInvItemDetail.setQingJie(1);
                }
            }
        });
        timeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_normal5){
                    mInvItemDetail.setYouXiaoQi(0);
                }else if(checkedId == R.id.rb_unNormal5){
                    mInvItemDetail.setYouXiaoQi(1);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getXAssetsDetailsById(mInvId, assetsCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.xf_activity_assets_detail;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.title_back, R.id.asset_detail, R.id.mainten_info, R.id.repair_record, R.id.asset_resume, R.id.asset_tab,
            R.id.bt_submit, R.id.bt_repair})
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
            case R.id.bt_submit:
                mInvItemDetail.setRemark(remarkText.getText().toString());
                mInvItemDetail.setInv_status(1);
                DbBank.getInstance().getXfInventoryDetailDao().updateItem(mInvItemDetail);
                ToastUtils.showShort("提交成功");
                break;
            case R.id.bt_repair:

                break;
        }
    }

    @Override
    public void handleAssetsDetails(AssetsAllInfo assetsAllInfo) {

    }


    @Override
    public void handleAssetsResume(List<AssetResume> data) {

    }

    @Override
    public void handleAssetsRepair(List<AssetRepair> assetRepairs) {

    }

    @Override
    public void handleAssetsNoDetail() {

    }

    @Override
    public void handleSetOneAssetInved(Boolean result) {

    }
   /* @BindView(R.id.tv_asset_num)
    TextView astName;
    @BindView(R.id.tv_ast_loc_name)
    TextView locName;
    @BindView(R.id.tv_depart_name)
    TextView companyName;
    @BindView(R.id.tv_asset_num1)
    TextView astCode1;
    @BindView(R.id.tv_device_name1)
    TextView deviceName1;
    @BindView(R.id.tv_ast_loc_name1)
    TextView locName1;
    @BindView(R.id.tv_depart_name1)
    TextView companyName1;
    @BindView(R.id.tv_person_content)
    TextView perContent;
    @BindView(R.id.tv_date_content)
    TextView dateContent;
    @BindView(R.id.tv_result_content)
    TextView resultContent;
    @BindView(R.id.tv_asset_num2)
    TextView assetNum2;
    @BindView(R.id.tv_device_name2)
    TextView deviceName2;
    @BindView(R.id.tv_ast_loc_name2)
    TextView locName2;
    @BindView(R.id.tv_depart_name2)
    TextView departName2;*/
    @Override
    public void handleInvItemDetail(XfInventoryDetail invItemDetail) {
        mInvItemDetail = invItemDetail;
        astName.setText(mInvItemDetail.getAst_name());
        locName.setText(mInvItemDetail.getLoc_name());
        companyName.setText(mInvItemDetail.getOrg_belongcorp_name());
        int zhiJia = mInvItemDetail.getZhiJia();
        if(zhiJia == 0){
            zhijiaGroup.check(R.id.rb_normal1);
        }else {
            zhijiaGroup.check(R.id.rb_unNormal1);
        }
        int yaLi = mInvItemDetail.getYaLi();
        if(yaLi == 0){
            yaliGroup.check(R.id.rb_normal2);
        }else {
            yaliGroup.check(R.id.rb_unNormal2);
        }
        int qianFen = mInvItemDetail.getQianFen();
        if(qianFen == 0){
            qianfenGroup.check(R.id.rb_normal3);
        }else {
            qianfenGroup.check(R.id.rb_unNormal3);
        }
        int qingJie = mInvItemDetail.getQingJie();
        if(qingJie == 0){
            qinjieGroup.check(R.id.rb_normal4);
        }else {
            qinjieGroup.check(R.id.rb_unNormal4);
        }
        int youXiaoQi = mInvItemDetail.getYouXiaoQi();
        if(youXiaoQi == 0){
            timeGroup.check(R.id.rb_normal5);
        }else {
            timeGroup.check(R.id.rb_unNormal5);
        }
        astCode1.setText(mInvItemDetail.getAst_barcode());
        deviceName1.setText(mInvItemDetail.getAst_name());
        locName1.setText(mInvItemDetail.getLoc_name());
        companyName1.setText(mInvItemDetail.getOrg_belongcorp_name());

        perContent.setText(mInvItemDetail.getXjPerson());
        dateContent.setText(mInvItemDetail.getXjDate());
        resultContent.setText(mInvItemDetail.getXjResult());

        assetNum2.setText(mInvItemDetail.getWeixiuCode());
        deviceName2.setText(mInvItemDetail.getWeixiuPerson());
        locName2.setText(mInvItemDetail.getWeixiuDate());
        departName2.setText(mInvItemDetail.getWeixiuContent());

        remarkText.setText(mInvItemDetail.getRemark());


    }
}
