package com.common.esimrfid.ui.home;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.HomeConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.home.AssetStatusNum;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.presenter.home.HomePresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.NewSpeedataUhfServiceImpl;
import com.common.esimrfid.uhf.RodinbellUhfServiceImpl;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.ZebraUhfServiceImpl;
import com.common.esimrfid.ui.assetinventory.AssetInventoryActivity;
import com.common.esimrfid.ui.inventorytask.InventoryTaskActivity;
import com.common.esimrfid.ui.login.LoginActivity;
import com.common.esimrfid.ui.tagwrite.WriteTagActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomeConstract.View {
    @BindView(R.id.tv_username)
    TextView mUserName;
    @BindView(R.id.all_number)
    TextView mAllAssets;
    @BindView(R.id.inuse_number)
    TextView mInuseAssets;
    @BindView(R.id.free_number)
    TextView mFreeAssets;
    @BindView(R.id.rv_assets_location)
    RecyclerView mLocationRecycle;
    @BindString(R.string.welcom)
    String welcom;
    ArrayList<AssetLocationNum> mAstLocaionNum = new ArrayList<>();
    int maxAssetNum = 0;
    private LocationAssetAdapter locationAssetAdapter;

    @Override
    public HomePresenter initPresenter() {
        return new HomePresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        checkUserSatus();
        initRfid();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        locationAssetAdapter = new LocationAssetAdapter(mAstLocaionNum, this, maxAssetNum);
        mLocationRecycle.setLayoutManager(new LinearLayoutManager(this));
        mLocationRecycle.setAdapter(locationAssetAdapter);
        mPresenter.getAssetsNmbDiffLocation();
        mPresenter.getAssetsNmbDiffStatus();
    }

    //检查登录状态，未登录跳转登录界面
    private void checkUserSatus() {
        UserLoginResponse uerLogin = DataManager.getInstance().getUserLoginResponse();
        boolean loginStatus = DataManager.getInstance().getLoginStatus();
        if (loginStatus) {
            EsimAndroidApp.getInstance().setUserLoginResponse(uerLogin);
            mUserName.setText(welcom + uerLogin.getUserinfo().getUser_real_name());
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initToolbar() {

    }

    //初始化连接rfid
    private void initRfid() {
        String model = android.os.Build.MODEL;
        IEsimUhfService iEsimUhfService;
        if ("ESUR-H600".equals(model) || "SD60".equals(model)) {
            iEsimUhfService = new NewSpeedataUhfServiceImpl();
        } else if ("common".equals(model) || "ESUR-H500".equals(model)) {
            iEsimUhfService = new RodinbellUhfServiceImpl();
        } else {
            iEsimUhfService = new ZebraUhfServiceImpl();
        }
        iEsimUhfService.initRFID();
        EsimAndroidApp.setIEsimUhfService(iEsimUhfService);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEPC(UhfMsgEvent uhfMsgEvent) {

        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.UHF_CONNECT:

                break;
            case UhfMsgType.UHF_DISCONNECT:

                break;
        }

    }

    @OnClick({R.id.inv_task, R.id.ast_inv, R.id.ast_search, R.id.write_tag, R.id.home_setting})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.inv_task:
                startActivity(new Intent(this, InventoryTaskActivity.class));
                break;
            case R.id.ast_inv:
                startActivity(new Intent(this, AssetInventoryActivity.class));
                break;
            case R.id.ast_search:

                break;
            case R.id.write_tag:
			 startActivity(new Intent(this, WriteTagActivity.class));
                break;
            case R.id.home_setting:
                startActivity(new Intent(this,SettingActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EsimAndroidApp.getIEsimUhfService() != null) {
            EsimAndroidApp.getIEsimUhfService().closeRFID();
            EsimAndroidApp.setIEsimUhfService(null);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void handleAssetsNmbDiffLocation(HashMap<String, Integer> assetLocations) {
        Set<Map.Entry<String, Integer>> entries = assetLocations.entrySet();
        mAstLocaionNum.clear();
        for (Map.Entry<String, Integer> entry : entries) {
            AssetLocationNum assetLocationNum = new AssetLocationNum();
            assetLocationNum.setLocation(entry.getKey());
            assetLocationNum.setNumber(entry.getValue());
            if (entry.getValue() > maxAssetNum) {
                maxAssetNum = entry.getValue();
            }
            mAstLocaionNum.add(assetLocationNum);
        }
        locationAssetAdapter.setmMaxAssetNum(maxAssetNum);
        locationAssetAdapter.notifyDataSetChanged();

    }

    @Override
    public void handleAssetsNmbDiffStatus(AssetStatusNum assetStatus) {
        mAllAssets.setText(String.valueOf(assetStatus.getTotal()));
        mInuseAssets.setText(String.valueOf(assetStatus.getInused()));
        mFreeAssets.setText(String.valueOf(assetStatus.getInstore()));
    }
}
