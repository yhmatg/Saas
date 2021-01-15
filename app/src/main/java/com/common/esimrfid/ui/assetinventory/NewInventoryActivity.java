package com.common.esimrfid.ui.assetinventory;

import android.app.Dialog;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.RelativeLayout;
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
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.contrarywind.view.WheelView;
import com.multilevel.treelist.Node;
import com.multilevel.treelist.TreeRecyclerAdapter;

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
    @BindView(R.id.rl_assign_name)
    RelativeLayout rInvPersion;
    @BindView(R.id.tv_start_date)
    TextView mStartData;
    @BindView(R.id.rl_start_date)
    RelativeLayout rStartData;
    @BindView(R.id.tv_expfin_date)
    TextView mExpFinData;
    @BindView(R.id.rl_expect_date)
    RelativeLayout rExpFinData;
    @BindView(R.id.tv_inv_usecom)
    TextView mUseCom;
    @BindView(R.id.rl_use_company)
    RelativeLayout rUseCom;
    @BindView(R.id.tv_inv_usedepart)
    TextView mUseDepart;
    @BindView(R.id.rl_use_depart)
    RelativeLayout rUseDepart;
    @BindView(R.id.tv_inv_asstype)
    TextView mAssType;
    @BindView(R.id.rl_ast_type)
    RelativeLayout rAssType;
    @BindView(R.id.tv_inv_loc)
    TextView mAssLocation;
    @BindView(R.id.rl_store_location)
    RelativeLayout rAssLocation;
    @BindView(R.id.tv_inv_owncom)
    TextView mOwnCom;
    @BindView(R.id.rl_own_company)
    RelativeLayout rOwnCom;
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
    Date mSelectFinishDate;
    boolean usersClickShow;
    boolean companysClickShow;
    boolean typesClickShow;
    boolean locationsClickShow;
    Date currentDate;

    List<AssetsLocation> selectLocations = new ArrayList<>();
    List<AssetsType> selectTypes = new ArrayList<>();
    List<DepartmentBean> selectDeparts = new ArrayList<>();

    private MaterialDialog multipleDialog;
    private TextView mulTipleTvTitle;
    private RecyclerView multiRecycle;
    protected List<Node> multiDatas = new ArrayList<>();
    private TreeRecyclerAdapter multiAdapter;
    private View multiContentView;
    private List<Node> checkedDeparts = new ArrayList<>();
    private List<Node> checkedTypes = new ArrayList<>();
    private List<Node> checkedLocations = new ArrayList<>();
    private int preOption = -1;
    private boolean isStartDate;
    private Date mSelectStartDate;

    @Override
    public NewInventoryPressnter initPresenter() {
        return new NewInventoryPressnter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.new_inv_task);
        initCustomTimePicker();
        initCustomOptionPicker();
        initOptions();
        initDate();
        initMultiDialogView();
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        currentDate = calendar.getTime();
    }

    private void initOptions() {
        mPresenter.getAllManagerUsers();
        mPresenter.getAllCompany();
        mPresenter.getAllAssetsType();
        mPresenter.getAllAssetsLocation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_inv;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.title_back, R.id.ev_inv_name, R.id.rl_assign_name, R.id.rl_start_date, R.id.rl_expect_date, R.id.rl_use_company,
            R.id.rl_use_depart, R.id.rl_ast_type, R.id.rl_store_location, R.id.rl_own_company, R.id.btn_submit})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.ev_inv_name:
                break;
            case R.id.rl_assign_name:
                tvTitle.setText(R.string.new_inv_persion);
                currentOption = 0;
                if (mMangerUsers.size() == 0) {
                    usersClickShow = true;
                    mPresenter.getAllManagerUsers();
                } else {
                    pvCustomOptions.setPicker(mMangerUsers);
                    pvCustomOptions.show();
                }

                break;
            case R.id.rl_start_date:
                tvTitle.setText(R.string.exp_start_date);
                isStartDate = true;
                pvCustomTime.show();
                break;
            case R.id.rl_expect_date:
                tvTitle.setText(R.string.exp_finish_date);
                isStartDate = false;
                pvCustomTime.show();
                break;
            case R.id.rl_use_company:
                tvTitle.setText(R.string.inv_usecom);
                currentOption = 1;
                preOption = 1;
                if (mCompanyBeans.size() == 0) {
                    companysClickShow = true;
                    mPresenter.getAllCompany();
                } else {
                    pvCustomOptions.setPicker(mCompanyBeans);
                    pvCustomOptions.show();
                }
                break;
            case R.id.rl_use_depart:
                if (mSelectUseCompany != null && !TextUtils.isEmpty(mSelectUseCompany.getId()) && !"-1".equals(mSelectUseCompany.getId())) {
                    mulTipleTvTitle.setText(R.string.inv_usedepart);
                    currentOption = 2;
                    mPresenter.getAllDeparts(mSelectUseCompany.getId());
                } else {
                    ToastUtils.showShort("请先选择公司");
                }

                break;
            case R.id.rl_ast_type:
                mulTipleTvTitle.setText(R.string.inv_asstype);
                currentOption = 3;
                if (mAssetsTypes.size() == 0) {
                    typesClickShow = true;
                    mPresenter.getAllAssetsType();
                } else {
                   /* pvCustomOptions.setPicker(mAssetsTypes);
                    pvCustomOptions.show();*/
                    showMultipleDialog();
                }
                break;
            case R.id.rl_store_location:
                mulTipleTvTitle.setText(R.string.inv_location);
                currentOption = 4;
                if (mAssetsLocations.size() == 0) {
                    locationsClickShow = true;
                    mPresenter.getAllAssetsLocation();
                } else {
                    /*pvCustomOptions.setPicker(mAssetsLocations);
                    pvCustomOptions.show();*/
                    showMultipleDialog();
                }
                break;
            case R.id.rl_own_company:
                tvTitle.setText(R.string.inv_owncom);
                currentOption = 5;
                preOption = 5;
                if (mCompanyBeans.size() == 0) {
                    companysClickShow = true;
                    mPresenter.getAllCompany();
                } else {
                    pvCustomOptions.setPicker(mCompanyBeans);
                    pvCustomOptions.show();
                }
                break;
            case R.id.btn_submit:
                creataInvtory();
                break;
        }
    }

    private void creataInvtory() {
        InventoryParameter inventoryParameter = new InventoryParameter();
        if (TextUtils.isEmpty(mInvName.getText().toString())) {
            ToastUtils.showShort(R.string.inv_name_hint);
            return;
        }
        if (mSelectMangerUser == null) {
            ToastUtils.showShort(R.string.inv_persion_hint);
            return;
        }
        if (mSelectStartDate == null) {
            ToastUtils.showShort(R.string.inv_start_hint);
            return;
        }
        if (mSelectFinishDate == null) {
            ToastUtils.showShort(R.string.inv_finish_hint);
            return;
        }
        if (mSelectStartDate.getTime() > mSelectFinishDate.getTime()) {
            ToastUtils.showShort(R.string.start_end_time_error);
            return;
        }
        inventoryParameter.setInv_name(mInvName.getText().toString());
        inventoryParameter.setInv_assigner_id(mSelectMangerUser.getId());
        inventoryParameter.setInv_assigner_name(mSelectMangerUser.getUser_real_name());
        inventoryParameter.setInv_exptbegin_date(mSelectStartDate);
        inventoryParameter.setInv_exptfinish_date(mSelectFinishDate);
        if (mSelectUseCompany != null && !"-1".equals(mSelectUseCompany.getId())) {
            ArrayList<String> userCompany = new ArrayList<>();
            userCompany.add(mSelectUseCompany.getId());
            inventoryParameter.setInv_used_corp_filter(userCompany);
        }
        /*if (mSelectDepartment != null && !"-1".equals(mSelectDepartment.getId())) {
            ArrayList<String> userDepartment = new ArrayList<>();
            //userDepartment.add(mSelectDepartment.getId());
            for (DepartmentBean selectDepart : selectDeparts) {
                userDepartment.add(selectDepart.getId());
            }
            inventoryParameter.setInv_used_dept_filter(userDepartment);
        }*/
       /* if (mSelectAssetsType != null && !"-1".equals(mSelectAssetsType.getId())) {
            ArrayList<String> assetsType = new ArrayList<>();
            //assetsType.add(mSelectAssetsType.getId());
            for (AssetsType selectType : selectTypes) {
                assetsType.add(selectType.getId());
            }
            inventoryParameter.setInv_type_filter(checkedTypes);
        }*/
        /*if (mSelectAssetsLocation != null && !"-1".equals(mSelectAssetsLocation.getId())) {
            ArrayList<String> assetsLocation = new ArrayList<>();
            //assetsLocation.add(mSelectAssetsLocation.getId());
            for (AssetsLocation selectLocation : selectLocations) {
                assetsLocation.add(selectLocation.getId());
            }
            inventoryParameter.setInv_loc_filter(assetsLocation);
        }*/
        if (checkedDeparts.size() > 0) {
            ArrayList<String> userDepartment = new ArrayList<>();
            for (Node checkedDepart : checkedDeparts) {
                userDepartment.add((String) checkedDepart.getId());
            }
            inventoryParameter.setInv_used_dept_filter(userDepartment);
        }
        if (checkedTypes.size() > 0) {
            ArrayList<String> assetsType = new ArrayList<>();
            for (Node checkedType : checkedTypes) {
                assetsType.add((String) checkedType.getId());
            }
            inventoryParameter.setInv_type_filter(assetsType);
        }
        if (checkedLocations.size() > 0) {
            ArrayList<String> assetsLocation = new ArrayList<>();
            for (Node checkedLocation : checkedLocations) {
                assetsLocation.add((String) checkedLocation.getId());
            }
            inventoryParameter.setInv_loc_filter(assetsLocation);
        }
        if (mSelectOwnCompany != null && !"-1".equals(mSelectOwnCompany.getId())) {
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
                .setDividerColor(0x50B2B2B2)
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
        if (date.getTime() < currentDate.getTime()) {
            ToastUtils.showShort(R.string.time_too_earl);
        } else {
            if (isStartDate) {
                if(mSelectFinishDate != null && date.getTime() > mSelectFinishDate.getTime()){
                    ToastUtils.showShort(R.string.start_end_time_error);
                    return;
                }
                mSelectStartDate = date;
                mStartData.setText(DateUtils.date2String(mSelectStartDate));
            } else {
                if(mSelectStartDate != null && date.getTime() < mSelectStartDate.getTime()){
                    ToastUtils.showShort(R.string.start_end_time_error);
                    return;
                }
                mSelectFinishDate = date;
                mExpFinData.setText(DateUtils.date2String(mSelectFinishDate));
            }

        }


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
                .setLineSpacingMultiplier(2.8f)
                .isDialog(true)
                .setDividerColor(0x60B2B2B2)
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
                if (mMangerUsers.size() > 0) {
                    mSelectMangerUser = mMangerUsers.get(options1);
                    mInvPersion.setText(mSelectMangerUser.getUser_real_name());
                }

                break;
            case USE_COMPANY:
                if (mCompanyBeans.size() > 0) {
                    mSelectUseCompany = mCompanyBeans.get(options1);
                    mUseCom.setText(mSelectUseCompany.getOrg_name());
                    mSelectDepartment = null;
                    mUseDepart.setText("");
                }
                break;
            case USE_DEPARTMANET:
                if (mDepartmentBeans.size() > 0) {
                    mSelectDepartment = mDepartmentBeans.get(options1);
                    //modify 0103 start
                    selectDeparts.clear();
                    selectDeparts = getSelectDeparts(mDepartmentBeans, mSelectDepartment.getId(), selectDeparts);
                    //modify 0103 start
                    mUseDepart.setText(mSelectDepartment.getOrg_name());
                }
                break;
            case ASSETS_TYPE:
                if (mAssetsTypes.size() > 0) {
                    mSelectAssetsType = mAssetsTypes.get(options1);
                    //modify 0103 start
                    selectTypes.clear();
                    selectTypes = getSelectTypes(mAssetsTypes, mSelectAssetsType.getId(), selectTypes);
                    //modify 0103 end
                    mAssType.setText(mSelectAssetsType.getType_name());
                }
                break;
            case ASSETS_LOCATION:
                if (mAssetsLocations.size() > 0) {
                    mSelectAssetsLocation = mAssetsLocations.get(options1);
                    //modify 0103 start
                    selectLocations.clear();
                    selectLocations = getSelectLocations(mAssetsLocations, mSelectAssetsLocation.getId(), selectLocations);
                    //modify 0103 end
                    mAssLocation.setText(mSelectAssetsLocation.getLoc_name());
                }
                break;
            case OWN_COMPANY:
                if (mCompanyBeans.size() > 0) {
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
        if (usersClickShow) {
            pvCustomOptions.setPicker(mMangerUsers);
            pvCustomOptions.show();
            usersClickShow = false;
        }

    }

    @Override
    public void handleAllCompany(List<CompanyBean> companyBeans) {
        mCompanyBeans.clear();
        //20200103 start
        CompanyBean unKnowCompanyBean = new CompanyBean();
        unKnowCompanyBean.setId("-1");
        unKnowCompanyBean.setOrg_name("不限");
        mCompanyBeans.add(unKnowCompanyBean);
        //20200103 end
        mCompanyBeans.addAll(companyBeans);
        if (companysClickShow) {
            pvCustomOptions.setPicker(mCompanyBeans);
            pvCustomOptions.show();
            companysClickShow = false;
        }

    }

    @Override
    public void handleAllDeparts(List<DepartmentBean> departmentBeans) {
        //modify 20191230 bug 280 start
        //公司所有部门，不包含子公司的部门
        List<DepartmentBean> tempList = new ArrayList<>();
        for (DepartmentBean departmentBean : departmentBeans) {
            if (departmentBean.getOrg_type() == 0 && departmentBean.getOrg_superid().equals(mSelectUseCompany.getId())) {
                //公司一个部门下的所有部门
                List<DepartmentBean> oneDeparts = new ArrayList<>();
                oneDeparts = getSelectDeparts(departmentBeans, departmentBean.getId(), oneDeparts);
                tempList.addAll(oneDeparts);
            }
        }
        //modify 20191230 bug 280 end
        mDepartmentBeans.clear();
        //20200103 start
        DepartmentBean unKnowDepartmentBean = new DepartmentBean();
        unKnowDepartmentBean.setId(mSelectUseCompany.getId());
        unKnowDepartmentBean.setOrg_superid("-2");
        unKnowDepartmentBean.setOrg_name("全部");
        mDepartmentBeans.add(unKnowDepartmentBean);
        //20200103 end
        mDepartmentBeans.addAll(tempList);
        /*pvCustomOptions.setPicker(mDepartmentBeans);
        pvCustomOptions.show();*/
        showMultipleDialog();
    }

    @Override
    public void handleAllAssetsType(List<AssetsType> assetsTypes) {
        mAssetsTypes.clear();
        //20200103 start
        AssetsType unKnowAssetsType = new AssetsType();
        unKnowAssetsType.setId("-1");
        unKnowAssetsType.setType_superid("-2");
        unKnowAssetsType.setType_name("全部");
        mAssetsTypes.add(unKnowAssetsType);
        //20200103 end
        mAssetsTypes.addAll(assetsTypes);
        if (typesClickShow) {
           /* pvCustomOptions.setPicker(mAssetsTypes);
            pvCustomOptions.show();*/
            showMultipleDialog();
            typesClickShow = false;
        }
    }

    @Override
    public void handleAllAssetsLocation(List<AssetsLocation> assetsLocations) {
        mAssetsLocations.clear();
        //20200103 start
        AssetsLocation unKnowAssetsLocation = new AssetsLocation();
        unKnowAssetsLocation.setId("-1");
        unKnowAssetsLocation.setLoc_superid("-2");
        unKnowAssetsLocation.setLoc_name("全部");
        mAssetsLocations.add(unKnowAssetsLocation);
        //20200103 end
        mAssetsLocations.addAll(assetsLocations);
        if (locationsClickShow) {
            /*pvCustomOptions.setPicker(mAssetsLocations);
            pvCustomOptions.show();*/
            showMultipleDialog();
            locationsClickShow = false;
        }
    }

    @Override
    public void handlecreateNewInventory(CreateInvResult createInvResult) {
        dismissDialog();
        if (createInvResult == null) {
            showCreateSuccessDialog(R.string.save_fail);
        } else {
            showCreateSuccessDialog(R.string.save_success);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    finish();
                }
            }, 1500);
        }
    }

    public void showCreateSuccessDialog(int message) {
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

    public List<AssetsLocation> getSelectLocations(List<AssetsLocation> allLocaions, String id, List<AssetsLocation> resultLocations) {
        for (AssetsLocation resultLocation : allLocaions) {
            String selId = resultLocation.getId();
            String pId = resultLocation.getLoc_superid();
            if (id.equals(selId) && !resultLocations.contains(resultLocation)) {
                resultLocations.add(resultLocation);
            }
            if (pId != null && pId.equals(id) && !resultLocations.contains(resultLocation)) {
                resultLocations.add(resultLocation);
                getSelectLocations(allLocaions, selId, resultLocations);
            }
        }
        return resultLocations;
    }

    public List<AssetsType> getSelectTypes(List<AssetsType> allTypes, String id, List<AssetsType> resultTypess) {
        for (AssetsType itemType : allTypes) {
            String selId = itemType.getId();
            String pId = itemType.getType_superid();
            if (id.equals(selId) && !resultTypess.contains(itemType)) {
                resultTypess.add(itemType);
            }
            if (pId != null && pId.equals(id) && !resultTypess.contains(itemType)) {
                resultTypess.add(itemType);
                getSelectTypes(allTypes, selId, resultTypess);
            }
        }
        return resultTypess;
    }

    public List<DepartmentBean> getSelectDeparts(List<DepartmentBean> allDeparts, String id, List<DepartmentBean> resultDeparts) {
        for (DepartmentBean allDepart : allDeparts) {
            String selId = allDepart.getId();
            String pId = allDepart.getOrg_superid();
            if (id.equals(selId) && !resultDeparts.contains(allDepart)) {
                resultDeparts.add(allDepart);
            }
            if (pId != null && pId.equals(id) && !resultDeparts.contains(allDepart)) {
                resultDeparts.add(allDepart);
                getSelectDeparts(allDeparts, selId, resultDeparts);
            }
        }
        return resultDeparts;
    }

    public void showMultipleDialog() {
        if (preOption != currentOption) {
            updateMultiAdapterData();
            preOption = currentOption;
        }
        if (multipleDialog != null) {
            multipleDialog.show();
        } else {
            multipleDialog = new MaterialDialog.Builder(this)
                    .customView(multiContentView, false)
                    .show();
            Window dialogWindow = multipleDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
            //设置dialog宽度沾满全屏
            Window window = multipleDialog.getWindow();
            // 把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            // 设置宽度
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
    }

    public void initMultiDialogView() {
        multiContentView = LayoutInflater.from(this).inflate(R.layout.multiple_choice_dialog, null);
        TextView tvSubmit = (TextView) multiContentView.findViewById(R.id.tv_finish);
        TextView tvCancel = (TextView) multiContentView.findViewById(R.id.tv_cancle);
        multiRecycle = (RecyclerView) multiContentView.findViewById(R.id.multi_recycle);
        multiRecycle.setLayoutManager(new LinearLayoutManager(this));
        multiAdapter = new SimpleTreeRecyclerAdapter(multiRecycle, NewInventoryActivity.this,
                multiDatas, 2, R.drawable.tree_ex, R.drawable.tree_ec);
        multiRecycle.setAdapter(multiAdapter);
        mulTipleTvTitle = (TextView) multiContentView.findViewById(R.id.tv_title);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Node> allNodes = multiAdapter.getAllNodes();
                switch (currentOption) {
                    case 2:
                        checkedDeparts.clear();
                        for (Node node : allNodes) {
                            if (node.isChecked()) {
                                checkedDeparts.add(node);
                            }
                        }
                        String currentDep = "不限";
                        if (checkedDeparts.size() == 1) {
                            currentDep = checkedDeparts.get(0).getName();
                        } else if (checkedDeparts.size() > 1) {
                            currentDep = checkedDeparts.get(0).getName() + "+" + (checkedDeparts.size() - 1);
                        }
                        if (!StringUtils.isEmpty(currentDep)) {
                            mUseDepart.setText(currentDep);
                        }
                        break;
                    case 3:
                        checkedTypes.clear();
                        for (Node node : allNodes) {
                            if (node.isChecked()) {
                                checkedTypes.add(node);
                            }
                        }
                        String currentType = "不限";
                        if (checkedTypes.size() == 1) {
                            currentType = checkedTypes.get(0).getName();
                        } else if (checkedTypes.size() > 1) {
                            currentType = checkedTypes.get(0).getName() + "+" + (checkedTypes.size() - 1);
                        }
                        if (!StringUtils.isEmpty(currentType)) {
                            mAssType.setText(currentType);
                        }
                        break;
                    case 4:
                        checkedLocations.clear();
                        for (Node node : allNodes) {
                            if (node.isChecked()) {
                                checkedLocations.add(node);
                            }
                        }
                        String currentLoc = "不限";
                        if (checkedLocations.size() == 1) {
                            currentLoc = checkedLocations.get(0).getName();
                        } else if (checkedLocations.size() > 1) {
                            currentLoc = checkedLocations.get(0).getName() + "+" + (checkedLocations.size() - 1);
                        }
                        if (!StringUtils.isEmpty(currentLoc)) {
                            mAssLocation.setText(currentLoc);
                        }
                        break;
                }
                if (multipleDialog != null) {
                    multipleDialog.dismiss();
                }
                Log.e("部门", checkedDeparts.toString() + checkedDeparts.size());
                Log.e("类型", checkedTypes.toString() + checkedTypes.size());
                Log.e("位置", checkedLocations.toString() + checkedLocations.size());
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (multipleDialog != null) {
                    multipleDialog.dismiss();
                }
            }
        });
    }

    public void updateMultiAdapterData() {
        multiAdapter.removeData(multiDatas);
        multiDatas.clear();
        switch (currentOption) {
            case 2:
                for (DepartmentBean mDepartmentBean : mDepartmentBeans) {
                    String pId = StringUtils.isEmpty(mDepartmentBean.getOrg_superid()) ? "-1" : mDepartmentBean.getOrg_superid();
                    multiDatas.add(new Node(mDepartmentBean.getId(), pId, mDepartmentBean.getOrg_name()));
                }
                break;
            case 3:
                for (AssetsType mAssetsType : mAssetsTypes) {
                    String pId = StringUtils.isEmpty(mAssetsType.getType_superid()) ? "-1" : mAssetsType.getType_superid();
                    multiDatas.add(new Node(mAssetsType.getId(), pId, mAssetsType.getType_name()));
                }
                break;
            case 4:
                for (AssetsLocation mAssetsLoc : mAssetsLocations) {
                    String pId = StringUtils.isEmpty(mAssetsLoc.getLoc_superid()) ? "-1" : mAssetsLoc.getLoc_superid();
                    multiDatas.add(new Node(mAssetsLoc.getId(), pId, mAssetsLoc.getLoc_name()));
                }
                break;
        }

        multiAdapter.addData(multiDatas);
    }

    boolean isAllChildenChecked(Node node) {
        if (!node.isLeaf()) {
            List<Node> children = node.getChildren();
            for (Node child : children) {
                if (!isAllChildenChecked(child)) {
                    return false;
                }
            }
            return true;
        } else {
            return node.isChecked();
        }
    }
}
