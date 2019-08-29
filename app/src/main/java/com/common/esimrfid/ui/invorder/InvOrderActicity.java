package com.common.esimrfid.ui.invorder;

import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.base.fragment.BaseFragment;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;
import com.common.esimrfid.presenter.home.InvOrderPressnter;
import com.common.esimrfid.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class InvOrderActicity extends BaseActivity<InvOrderPressnter> implements InvOrderContract.View {

    @BindView(R.id.imgTitleLeft)
    ImageView imgTitleLeft;
    @BindView(R.id.error_page)
    LinearLayout errorPage;
    @BindView(R.id.empty_page)
    LinearLayout emptyPage;
    @BindView(R.id.tvTitleCenter)
    TextView tvTitleCenter;
    @BindView(R.id.tvTitleRight)
    TextView tvTitleRight;
    @BindView(R.id.tvMyOrderWaiting)
    TextView tvMyOrderWaiting;
    @BindView(R.id.lineMyOrderWaiting)
    View lineMyOrderWaiting;
    @BindView(R.id.tvMyOrderDisposing)
    TextView tvMyOrderDisposing;
    @BindView(R.id.lineMyOrderDisposing)
    View lineMyOrderDisposing;
    @BindView(R.id.tvMyOrderFinish)
    TextView tvMyOrderFinish;
    @BindView(R.id.lineMyOrderFinish)
    View lineMyOrderFinish;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    public List<ResultInventoryOrder> mDataList = new ArrayList<>();
    private BaseFragment mCurrentFragemnt;
    private FragCheckWaitingFragment fragCheckWaitingFragment;
    private FragCheckFinishedFragment fragCheckFinishedFragment;
    private String userId;
    private Boolean isUpdate = false;
    private Boolean isFirstOnResume = true;

    @Override
    public InvOrderPressnter initPresenter() {
        return new InvOrderPressnter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText("更新");
        tvTitleCenter.setText("资产盘点");
        imgTitleLeft.setVisibility(View.VISIBLE);
        fragCheckWaitingFragment = new FragCheckWaitingFragment();
        fragCheckFinishedFragment = new FragCheckFinishedFragment();
        addOrShowFragment(fragCheckFinishedFragment);
        addOrShowFragment(fragCheckWaitingFragment);
        userId = getUserLoginResponse().getSysUser().getId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化或者更新本地盘点的状态
        if(isFirstOnResume){
            mPresenter.fetchAllIvnOrders(userId,true);
            isFirstOnResume = false;
        }else{
            mPresenter.fetchAllIvnOrders(userId,false);
        }

    }

    @OnClick({R.id.imgTitleLeft, R.id.tvTitleRight, R.id.tvMyOrderWaiting, R.id.tvMyOrderDisposing, R.id.tvMyOrderFinish})
    public void performClick(View view){
        switch (view.getId()){
            case R.id.imgTitleLeft:
                finish();
                break;
            case R.id.tvTitleRight:
                mPresenter.fetchAllIvnOrders(userId,true);
                isUpdate = true;
                break;
            case R.id.tvMyOrderWaiting:
                updateUI(tvMyOrderWaiting, tvMyOrderFinish, tvMyOrderDisposing, lineMyOrderWaiting, lineMyOrderDisposing, lineMyOrderFinish);
                addOrShowFragment(fragCheckWaitingFragment);
                break;
            case R.id.tvMyOrderFinish:
                updateUI(tvMyOrderFinish, tvMyOrderWaiting, tvMyOrderDisposing, lineMyOrderFinish, lineMyOrderWaiting, lineMyOrderDisposing);
                addOrShowFragment(fragCheckFinishedFragment);
                break;

        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inv_order;
    }

    @Override
    protected void initToolbar() {

    }

    public void addOrShowFragment(BaseFragment fragment){
        if(fragment != null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(mCurrentFragemnt != null){
                transaction.hide(mCurrentFragemnt);
            }
            if (fragment.isAdded()){
                transaction.show(fragment);
            }else{
                transaction.add(R.id.frameLayout,fragment);
                mCurrentFragemnt = fragment;
            }
            transaction.commit();
        }

    }

    private void updateUI(TextView tv1, TextView tv2, TextView tv3, View line1, View line2, View line3) {

        tv1.setTextColor(Color.parseColor("#30b4f8"));
        line1.setVisibility(View.VISIBLE);

        tv2.setTextColor(Color.parseColor("#88d1fc"));
        line2.setVisibility(View.GONE);

        tv3.setTextColor(Color.parseColor("#88d1fc"));
        line3.setVisibility(View.GONE);
    }

    @Override
    public void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders) {
        mDataList.clear();
        mDataList.addAll(resultInventoryOrders);
        fragCheckWaitingFragment.showInvOrders(mDataList);
        fragCheckFinishedFragment.showInvOrders(mDataList);
        if(isUpdate){
            ToastUtils.showShort(R.string.data_updated);
            isUpdate = false;
        }

    }

    @Override
    public void loadOrUpdateData(boolean locaLeftUpload) {
        /*if (locaLeftUpload && CommonUtils.isNetworkConnected()){
            new MaterialDialog.Builder(this)
                    .title("更新提示")
                    .positiveText("确定")
                    .negativeText("取消")
                    .content("本地数据库中还有未完成的盘点单，更新数据会覆盖这些盘点单，确定更新吗？")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mPresenter.fetchAllIvnOrders(userId,true);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            mPresenter.fetchAllIvnOrders(userId,false);
                            isUpdate = false;
                        }
                    })
                    .show();
        }else {
            mPresenter.fetchAllIvnOrders(userId,true);
        }*/
    }

}
