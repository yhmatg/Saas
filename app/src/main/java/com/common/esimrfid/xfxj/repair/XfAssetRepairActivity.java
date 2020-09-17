package com.common.esimrfid.xfxj.repair;

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

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetrepair.AssetRepairContract;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.esimrfid.presenter.assetrepair.AssetRepairPresenter;
import com.common.esimrfid.ui.home.BaseDialog;
import com.common.esimrfid.utils.DateUtils;
import com.common.esimrfid.xfxj.identity.XfIdentityActivity;
import com.contrarywind.view.WheelView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class XfAssetRepairActivity extends BaseActivity<AssetRepairPresenter> implements AssetRepairContract.View, XfAssetsRepairAdapter.OnDeleteClickListener {
    private static final String WHERE_FROM = "where_from";
    @BindView(R.id.title_back)
    ImageView mBackImg;
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.rl_repair_person)
    RelativeLayout rlRepairItem;
    @BindView(R.id.tv_repair_person)
    EditText mRepairPerson;
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
    Date mSelectDate = new Date();
    ArrayList<XfInventoryDetail> selectedAssets = new ArrayList<>();
    private XfAssetsRepairAdapter repairAdapter;

    @Override
    public AssetRepairPresenter initPresenter() {
        return new AssetRepairPresenter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText("设备报修");
        mTvRepairDate.setText(DateUtils.date2String(mSelectDate));
        EventBus.getDefault().register(this);
        repairAdapter = new XfAssetsRepairAdapter(this, selectedAssets, "AssetRepairActivity");
        repairAdapter.setOnDeleteClickListener(this);
        mSelectedRecy.setLayoutManager(new LinearLayoutManager(this));
        mSelectedRecy.setAdapter(repairAdapter);
        initCustomTimePicker();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.xf_activity_ast_repair;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public void handleAllEmpUsers(List<MangerUser> mangerUsers) {

    }

    @Override
    public void handleCreateNewRepairOrder(BaseResponse baseResponse) {

    }

    @OnClick({R.id.rl_repair_person, R.id.rl_repair_date, R.id.title_back, R.id.btn_submit, R.id.tv_scan_add, R.id.tv_choose_add})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.rl_repair_person:

                break;
            case R.id.rl_repair_date:
                pvCustomTime.show();
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.tv_scan_add:
                mScanAdd.setTextColor(getColor(R.color.repair_way));
                mChooseAdd.setTextColor(getColor(R.color.repair_text));
                Intent intent = new Intent();
                intent.putExtra(WHERE_FROM, "AssetRepairActivity");
                intent.setClass(this, XfIdentityActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_choose_add:
                mScanAdd.setTextColor(getColor(R.color.repair_text));
                mChooseAdd.setTextColor(getColor(R.color.repair_way));
                startActivity(new Intent(this, XfChooseRepairAstActivity.class));
                break;
            case R.id.btn_submit:

                break;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSelectedAst(XfRepairAssetEvent repairAssetEvent) {
        ArrayList<XfInventoryDetail> tempAssets = new ArrayList<>();
        tempAssets.addAll(selectedAssets);
        List<XfInventoryDetail> assetsInfos = repairAssetEvent.getmSelectedData();
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
    public void onDeleteClick(XfInventoryDetail assetsInfo) {
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
}
