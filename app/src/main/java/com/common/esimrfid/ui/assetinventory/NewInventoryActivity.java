package com.common.esimrfid.ui.assetinventory;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetinventory.NewInventoryContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.CreateInvResult;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.InventoryParameter;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.presenter.assetinventory.NewInventoryPressnter;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.DateUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.contrarywind.view.WheelView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewInventoryActivity extends BaseActivity<NewInventoryPressnter> implements NewInventoryContract.View {
    public static final int MANFER_USER = 0;
    public static final int USE_COMPANY = 1;
    public static final int USE_DEPARTMANET = 2;
    public static final int ASSETS_TYPE = 3;
    public static final int ASSETS_LOCATION = 4;
    public static final int OWN_COMPANY = 5;
    @BindView(R.id.title_back)
    ImageView mBackImg;
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.ev_inv_name)
    EditText mInvName;
    @BindView(R.id.tv_inv_person)
    TextView mInvPersion;
    @BindView(R.id.tv_expfin_date)
    TextView mExpFinData;
    @BindView(R.id.tv_inv_usecom)
    TextView mUseCom;
    @BindView(R.id.tv_inv_usedepart)
    TextView mUseDepart;
    @BindView(R.id.tv_inv_asstype)
    TextView mAssType;
    @BindView(R.id.tv_inv_loc)
    TextView mAssLocation;
    @BindView(R.id.tv_inv_owncom)
    TextView mOwnCom;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    int currentOption;
    private TimePickerView pvCustomTime;
    private OptionsPickerView pvCustomOptions;
    private TextView tvTitle;
    List<MangerUser> mMangerUsers = new ArrayList<>();
    List<CompanyBean> mCompanyBeans = new ArrayList<>();
    List<DepartmentBean> mDepartmentBeans = new ArrayList<>();
    List<AssetsType> mAssetsTypes = new ArrayList<>();
    List<AssetsLocation> mAssetsLocations = new ArrayList<>();
    MangerUser mSelectMangerUser;
    CompanyBean mSelectUseCompany;
    DepartmentBean mSelectDepartment;
    AssetsType mSelectAssetsType;
    AssetsLocation mSelectAssetsLocation;
    CompanyBean mSelectOwnCompany;
    Date mSelectDate;
    @Override
    public NewInventoryPressnter initPresenter() {
        return new NewInventoryPressnter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.new_inv_task);
        initCustomTimePicker();
        initCustomOptionPicker();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_inv;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.title_back, R.id.ev_inv_name, R.id.tv_inv_person, R.id.tv_expfin_date, R.id.tv_inv_usecom,
            R.id.tv_inv_usedepart, R.id.tv_inv_asstype, R.id.tv_inv_loc, R.id.tv_inv_owncom, R.id.btn_submit})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.ev_inv_name:
                break;
            case R.id.tv_inv_person:
                tvTitle.setText(R.string.new_inv_persion);
                currentOption = 0;
                mPresenter.getAllManagerUsers();
                break;
            case R.id.tv_expfin_date:
                tvTitle.setText(R.string.exp_finish_date);
                pvCustomTime.show();
                break;
            case R.id.tv_inv_usecom:
                tvTitle.setText(R.string.inv_usecom);
                currentOption = 1;
                mPresenter.getAllCompany();
                break;
            case R.id.tv_inv_usedepart:
                if(mSelectUseCompany!= null && !TextUtils.isEmpty(mSelectUseCompany.getId())){
                    tvTitle.setText(R.string.inv_usedepart);
                    currentOption = 2;
                    mPresenter.getAllDeparts(mSelectUseCompany.getId());
                }else {
                    ToastUtils.showShort("请先选择公司");
                }

                break;
            case R.id.tv_inv_asstype:
                tvTitle.setText(R.string.inv_asstype);
                currentOption = 3;
                mPresenter.getAllAssetsType();
                break;
            case R.id.tv_inv_loc:
                tvTitle.setText(R.string.inv_location);
                currentOption = 4;
                mPresenter.getAllAssetsLocation();
                break;
            case R.id.tv_inv_owncom:
                tvTitle.setText(R.string.inv_owncom);
                currentOption = 5;
                mPresenter.getAllCompany();
                break;
            case R.id.btn_submit:
                creataInvtory();
                break;
        }
    }

    private void creataInvtory() {
        InventoryParameter inventoryParameter = new InventoryParameter();
        if(TextUtils.isEmpty(mInvName.getText().toString())){
            ToastUtils.showShort("请输入盘点单名称");
            return;
        }
        if(mSelectMangerUser == null){
            ToastUtils.showShort("请选择盘点人");
            return;
        }

        if(mSelectDate == null){
            ToastUtils.showShort("请选择预计完成时间");
            return;
        }
        inventoryParameter.setInv_name(mInvName.getText().toString());
        inventoryParameter.setInv_assigner_id(mSelectMangerUser.getId());
        inventoryParameter.setInv_exptfinish_date(mSelectDate);
        if(mSelectUseCompany != null){
            ArrayList<String> userCompany = new ArrayList<>();
            userCompany.add(mSelectUseCompany.getId());
            inventoryParameter.setInv_used_corp_filter(userCompany);
        }
        if(mSelectDepartment != null){
            ArrayList<String> userDepartment = new ArrayList<>();
            userDepartment.add(mSelectDepartment.getId());
            inventoryParameter.setInv_used_dept_filter(userDepartment);
        }
        if(mSelectAssetsType != null){
            ArrayList<String> assetsType = new ArrayList<>();
            assetsType.add(mSelectAssetsType.getId());
            inventoryParameter.setInv_type_filter(assetsType);
        }
        if(mSelectAssetsLocation != null){
            ArrayList<String> assetsLocation = new ArrayList<>();
            assetsLocation.add(mSelectAssetsLocation.getId());
            inventoryParameter.setInv_loc_filter(assetsLocation);
        }
        if(mSelectOwnCompany != null){
            ArrayList<String> ownCompany = new ArrayList<>();
            ownCompany.add(mSelectOwnCompany.getId());
            inventoryParameter.setInv_belong_corp_filter(ownCompany);
        }
        mPresenter.createNewInventory(inventoryParameter);
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
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2050, 2, 28);
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
        mExpFinData.setText(DateUtils.date2String(mSelectDate));

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
                optionSelect(options1);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancle);
                        tvTitle = (TextView) v.findViewById(R.id.tv_title);
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

    private void submitOption() {
        switch (currentOption) {
            case MANFER_USER:
                mInvPersion.setText(mSelectMangerUser.getUser_real_name());
                break;
            case USE_COMPANY:
                mUseCom.setText(mSelectUseCompany.getOrg_name());
                break;
            case USE_DEPARTMANET:
                mUseDepart.setText(mSelectDepartment.getOrg_name());
                break;
            case ASSETS_TYPE:
                mAssType.setText(mSelectAssetsType.getType_name());
                break;
            case ASSETS_LOCATION:
                mAssLocation.setText(mSelectAssetsLocation.getLoc_name());
                break;
            case OWN_COMPANY:
                mOwnCom.setText(mSelectOwnCompany.getOrg_name());
                break;
        }
    }

    private void optionSelect(int options1) {
        switch (currentOption) {
            case MANFER_USER:
                if(mMangerUsers.size() > 0){
                    mSelectMangerUser = mMangerUsers.get(options1);
                    mInvPersion.setText(mSelectMangerUser.getUser_real_name());
                }

                break;
            case USE_COMPANY:
                if(mCompanyBeans.size() > 0){
                    mSelectUseCompany = mCompanyBeans.get(options1);
                    mUseCom.setText(mSelectUseCompany.getOrg_name());
                }
                break;
            case USE_DEPARTMANET:
                if(mDepartmentBeans.size() > 0){
                    mSelectDepartment = mDepartmentBeans.get(options1);
                    mUseDepart.setText(mSelectDepartment.getOrg_name());
                }
                break;
            case ASSETS_TYPE:
                if(mAssetsTypes.size() > 0){
                    mSelectAssetsType = mAssetsTypes.get(options1);
                    mAssType.setText(mSelectAssetsType.getType_name());
                }
                break;
            case ASSETS_LOCATION:
                if(mAssetsLocations.size() > 0){
                    mSelectAssetsLocation = mAssetsLocations.get(options1);
                    mAssLocation.setText(mSelectAssetsLocation.getLoc_name());
                }
                break;
            case OWN_COMPANY:
                if(mCompanyBeans.size() > 0){
                    mSelectOwnCompany = mCompanyBeans.get(options1);
                    mOwnCom.setText(mSelectOwnCompany.getOrg_name());
                }

                break;
        }
    }

    @Override
    public void handleAllManagerUsers(List<MangerUser> mangerUsers) {
        mMangerUsers.clear();
        mMangerUsers.addAll(mangerUsers);
        pvCustomOptions.setPicker(mMangerUsers);
        pvCustomOptions.show();
    }

    @Override
    public void handleAllCompany(List<CompanyBean> companyBeans) {
        mCompanyBeans.clear();
        mCompanyBeans.addAll(companyBeans);
        pvCustomOptions.setPicker(mCompanyBeans);
        pvCustomOptions.show();
    }

    @Override
    public void handleAllDeparts(List<DepartmentBean> departmentBeans) {
        //modify 20191230 bug 280 start
        List<DepartmentBean> tempList= new ArrayList<>();
        for (DepartmentBean departmentBean : departmentBeans) {
            if(departmentBean.getOrg_type() == 0){
                tempList.add(departmentBean);
            }
        }
        //modify 20191230 bug 280 end
        mDepartmentBeans.clear();
        mDepartmentBeans.addAll(tempList);
        pvCustomOptions.setPicker(mDepartmentBeans);
        pvCustomOptions.show();
    }

    @Override
    public void handleAllAssetsType(List<AssetsType> assetsTypes) {
        mAssetsTypes.clear();
        mAssetsTypes.addAll(assetsTypes);
        pvCustomOptions.setPicker(mAssetsTypes);
        pvCustomOptions.show();
    }

    @Override
    public void handleAllAssetsLocation(List<AssetsLocation> assetsLocations) {
        mAssetsLocations.clear();
        mAssetsLocations.addAll(assetsLocations);
        pvCustomOptions.setPicker(mAssetsLocations);
        pvCustomOptions.show();
    }

    @Override
    public void handlecreateNewInventory(CreateInvResult createInvResult) {
        dismissDialog();
        if(createInvResult == null){
            showCreateSuccessDialog(R.string.save_newinv_fail);
        }else {
            showCreateSuccessDialog(R.string.save_newinv_succ);
            finish();
        }
    }

    public void showCreateSuccessDialog(int message){
        View contentView = LayoutInflater.from(this).inflate(R.layout.login_success_dialog, null);
        TextView mContent = contentView.findViewById(R.id.tv_status);
        mContent.setText(message);
        MaterialDialog writeDialog = new MaterialDialog.Builder(this)
                .customView(contentView, false)
                .show();
        Window window = writeDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = CommonUtils.dp2px(118);
        layoutParams.height = CommonUtils.dp2px(98);
        window.setAttributes(layoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pvCustomOptions = null;
        pvCustomTime = null;
    }
}
