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
        FragCheckWaitingFragment fragCheckWaitingFragment = new FragCheckWaitingFragment();
        addOrShowFragment(fragCheckWaitingFragment);

    }

    @OnClick({R.id.imgTitleLeft, R.id.tvTitleRight, R.id.tvMyOrderWaiting, R.id.tvMyOrderDisposing, R.id.tvMyOrderFinish})
    public void performClick(View view){
        switch (view.getId()){
            case R.id.imgTitleLeft:
                finish();
                break;
            case R.id.tvTitleRight:
                break;
            case R.id.tvMyOrderWaiting:
                updateUI(tvMyOrderWaiting, tvMyOrderFinish, tvMyOrderDisposing, lineMyOrderWaiting, lineMyOrderDisposing, lineMyOrderFinish);

                break;
            case R.id.tvMyOrderFinish:
                updateUI(tvMyOrderFinish, tvMyOrderWaiting, tvMyOrderDisposing, lineMyOrderFinish, lineMyOrderWaiting, lineMyOrderDisposing);

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
            if (fragment.isAdded()){
                transaction.show(fragment);
            }else{
                transaction.add(R.id.frameLayout,fragment);
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

}
