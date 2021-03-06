package com.common.esimrfid.ui.assetrepair;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetrepair.AssetRepairContract;
import com.common.esimrfid.core.bean.assetdetail.AssetRepairParameter;
import com.common.esimrfid.core.bean.assetdetail.NewAssetRepairPara;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.presenter.assetrepair.AssetRepairPresenter;
import com.common.esimrfid.ui.home.BaseDialog;
import com.common.esimrfid.ui.identity.IdentityActivity;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.DateUtils;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.contrarywind.view.WheelView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class AssetRepairActivity extends BaseActivity<AssetRepairPresenter> implements AssetRepairContract.View, AssetsRepairAdapter.OnDeleteClickListener {
    private static final String WHERE_FROM = "where_from";
    @BindView(R.id.title_back)
    ImageView mBackImg;
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.rl_repair_person)
    RelativeLayout rlRepairItem;
    @BindView(R.id.tv_repair_person)
    TextView mRepairPerson;
    @BindView(R.id.ev_repair_cost)
    EditText mRepairCost;
    @BindView(R.id.rl_repair_date)
    RelativeLayout rRepairData;
    @BindView(R.id.tv_repair_date)
    TextView mTvRepairDate;
    @BindView(R.id.ev_repair_direc)
    EditText mRepairDirection;
    @BindView(R.id.tv_scan_add)
    TextView mScanAdd;
    @BindView(R.id.tv_choose_add)
    TextView mChooseAdd;
    @BindView(R.id.rv_selected_ast)
    RecyclerView mSelectedRecy;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.divider_five)
    View divideView;
    private TimePickerView pvCustomTime;
    private OptionsPickerView pvCustomOptions;
    List<MangerUser> mMangerUsers = new ArrayList<>();
    MangerUser mSelectMangerUser;
    boolean usersClickShow;
    Date mSelectDate = new Date();
    ArrayList<AssetsListItemInfo> selectedAssets = new ArrayList<>();
    private AssetsRepairAdapter repairAdapter;
    private String userName = "";
    private String userId = "";
    private final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    @Override
    public AssetRepairPresenter initPresenter() {
        return new AssetRepairPresenter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.ast_repair);
        mTvRepairDate.setText(DateUtils.date2String(mSelectDate));
        EventBus.getDefault().register(this);
        repairAdapter = new AssetsRepairAdapter(this, selectedAssets, "AssetRepairActivity");
        repairAdapter.setOnDeleteClickListener(this);
        mSelectedRecy.setLayoutManager(new LinearLayoutManager(this));
        mSelectedRecy.setAdapter(repairAdapter);
        initCustomTimePicker();
        initCustomOptionPicker();
        initOptions();
        userName = getUserLoginResponse().getUserinfo().getUser_real_name();
        userId = getUserLoginResponse().getUserinfo().getId();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ast_repair;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public void handleAllEmpUsers(List<MangerUser> mangerUsers) {
        mMangerUsers.clear();
        mMangerUsers.addAll(mangerUsers);
        if (usersClickShow) {
            pvCustomOptions.setPicker(mMangerUsers);
            pvCustomOptions.show();
            usersClickShow = false;
        }
    }

    @Override
    public void handleCreateNewRepairOrder(BaseResponse baseResponse) {
        showConfirmDialog(baseResponse.isSuccess());
    }

    @OnClick({R.id.rl_repair_person, R.id.rl_repair_date, R.id.title_back, R.id.btn_submit, R.id.tv_scan_add, R.id.tv_choose_add})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.rl_repair_person:
                if (mMangerUsers.size() == 0) {
                    usersClickShow = true;
                    mPresenter.getAllEmpUsers();
                } else {
                    pvCustomOptions.setPicker(mMangerUsers);
                    pvCustomOptions.show();
                }
                break;
            case R.id.rl_repair_date:
                pvCustomTime.show();
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.tv_scan_add:
                if (CommonUtils.isNormalClick()) {
                    mScanAdd.setTextColor(getColor(R.color.repair_way));
                    mChooseAdd.setTextColor(getColor(R.color.repair_text));
                    Intent intent = new Intent();
                    intent.putExtra(WHERE_FROM, "AssetRepairActivity");
                    intent.setClass(this, IdentityActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_choose_add:
                if (CommonUtils.isNormalClick()) {
                    mScanAdd.setTextColor(getColor(R.color.repair_text));
                    mChooseAdd.setTextColor(getColor(R.color.repair_way));
                    startActivity(new Intent(this, ChooseRepairAstActivity.class));
                }
                break;
            case R.id.btn_submit:
                if (CommonUtils.isNormalClick()) {
                    createRepairOrder();
                }
                break;
        }
    }

    private void createRepairOrder() {
        AssetRepairParameter assetRepairParameter = new AssetRepairParameter();
        if (mSelectMangerUser == null) {
            ToastUtils.showShort("请选择报修人");
            return;
        }
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        String costString = mRepairCost.getText().toString();
        Matcher matcher = pattern.matcher(costString);
        if (!matcher.matches()) {
            ToastUtils.showShort("请输入正确的金额，可保留小数点后两位");
            return;
        }
        if (StringUtils.isEmpty(mRepairDirection.getText().toString())) {
            ToastUtils.showShort("请输入维修说明");
            return;
        }
        if (selectedAssets.size() < 1) {
            ToastUtils.showShort("请选择报修资产");
            return;
        }
        assetRepairParameter.setRep_user_id(mSelectMangerUser.getId());
        assetRepairParameter.setOdr_transactor_id(userId);
        assetRepairParameter.setRep_user_name(mSelectMangerUser.getUser_real_name());
        assetRepairParameter.setTra_user_name(userName);
        assetRepairParameter.setMaintain_price(Double.parseDouble(costString));
        assetRepairParameter.setOdr_date(mSelectDate);
        assetRepairParameter.setOdr_remark(mRepairDirection.getText().toString());
        ArrayList<String> selectedAstIds = new ArrayList<>();
        for (AssetsListItemInfo selectedAsset : selectedAssets) {
            selectedAstIds.add(selectedAsset.getId());
        }
        assetRepairParameter.setAst_ids(selectedAstIds);
        String formData = assetRepairParameter.toString();
        String textForms = getTextFormsString(mSelectMangerUser.getUser_real_name(), mSelectMangerUser.getDept_name(), mRepairDirection.getText().toString());
        String title = userName + "提交的维修申请";
        mPresenter.createNewRepairOrder(new NewAssetRepairPara(formData, textForms, title));
    }

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                optionPersonSelect(options1);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancle);
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

    private void optionPersonSelect(int options1) {
        if (mMangerUsers.size() > 0) {
            mSelectMangerUser = mMangerUsers.get(options1);
            mRepairPerson.setText(mSelectMangerUser.getUser_real_name());
        }
    }

    private void initCustomTimePicker() {
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2018, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2050, 0, 1);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                expDateSelect(date);
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.custom_time_picker, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.tv_cancle);
                        WheelView year = v.findViewById(R.id.year);
                        year.setLineSpacingMultiplier(4.0f);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(14)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .setItemVisibleCount(7)
                .isDialog(true)
                .setLineSpacingMultiplier(2.5f)
                .build();

        Dialog mDialog = pvCustomTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvCustomTime.getDialogContainerLayout().setLayoutParams(params);

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

    private void expDateSelect(Date date) {
        mSelectDate = date;
        mTvRepairDate.setText(DateUtils.date2String(mSelectDate));

    }

    private void initOptions() {
        mPresenter.getAllEmpUsers();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSelectedAst(RepairAssetEvent repairAssetEvent) {
        ArrayList<AssetsListItemInfo> tempAssets = new ArrayList<>();
        tempAssets.addAll(selectedAssets);
        List<AssetsListItemInfo> assetsInfos = repairAssetEvent.getmSelectedData();
        tempAssets.retainAll(assetsInfos);
        assetsInfos.removeAll(tempAssets);
        selectedAssets.addAll(assetsInfos);
        if (selectedAssets.size() > 0) {
            divideView.setVisibility(View.VISIBLE);
        } else {
            divideView.setVisibility(View.GONE);
        }
        repairAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDeleteClick(AssetsListItemInfo assetsInfo) {
        selectedAssets.remove(assetsInfo);
        if (selectedAssets.size() > 0) {
            divideView.setVisibility(View.VISIBLE);
        } else {
            divideView.setVisibility(View.GONE);
        }
    }

    public void showConfirmDialog(Boolean isSuccess) {
        BaseDialog baseDialog = new BaseDialog(this, R.style.BaseDialog, R.layout.finish_confirm_dialog);
        TextView context = baseDialog.findViewById(R.id.alert_context);
        Button btSure = baseDialog.findViewById(R.id.bt_confirm);
        if (isSuccess) {
            context.setText(R.string.submit_success);
        } else {
            context.setText(R.string.submit_failed);
        }

        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseDialog.dismiss();
                clearData();
            }
        });
        baseDialog.show();
    }

    public void clearData() {
        mRepairPerson.setText("");
        mRepairCost.setText("0.00");
        mTvRepairDate.setText(DateUtils.date2String(new Date()));
        mRepairDirection.setText("");
        selectedAssets.clear();
        divideView.setVisibility(View.GONE);
        repairAdapter.notifyDataSetChanged();
    }

    private String getTextFormsString(String userName, String departName, String repairRemark) {
        return "[{\"name\":\"申请人\",\"value\":\"" + userName + "\"}," +
                "{\"name\":\"所在部门\",\"value\":\"" + departName + "\"}," +
                "{\"name\":\"报修原因\",\"value\":\"" + repairRemark + "\"}]"
                ;
    }
}
