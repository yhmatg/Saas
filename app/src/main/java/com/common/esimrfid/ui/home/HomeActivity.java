package com.common.esimrfid.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.UserLoginResponse;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.RodinbellUhfServiceImpl;
import com.common.esimrfid.uhf.SpeedataUhfServiceImpl;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.ZebraUhfServiceImpl;
import com.common.esimrfid.ui.cardsearch.ScanRfidActivity;
import com.common.esimrfid.ui.invorder.InvOrderActicity;
import com.common.esimrfid.ui.login.LoginActivity;
import com.common.esimrfid.ui.writeepc.WriteEpcActivity;
import com.common.esimrfid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.imgHomeAssetsScan)
    ImageView homeAssectScann;
    @BindView(R.id.imgHomeAssetsSearch)
    ImageView homeAssectSearch;
    @BindView(R.id.tv_crop)
    TextView tvCrop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        initRfid();
        checkUserSatus();
    }

    private void initRfid() {
        ToastUtils.showShort("RFID正在连接...");
        String model=android.os.Build.MODEL;
        IEsimUhfService iEsimUhfService ;
        if(model.equals("ESUR-H600")|| model.equals("SD60")){
            iEsimUhfService=new SpeedataUhfServiceImpl();
        }else if(model.equals("common")){
            iEsimUhfService = new RodinbellUhfServiceImpl();
        }else{
            iEsimUhfService=new ZebraUhfServiceImpl();
        }
        iEsimUhfService.initRFID();
        EsimAndroidApp.setIEsimUhfService(iEsimUhfService);
    }

    private void checkUserSatus (){
        UserLoginResponse uerLogin = DataManager.getInstance().getUserLoginResponse();
        boolean loginStatus = DataManager.getInstance().getLoginStatus();
        if(loginStatus){
            EsimAndroidApp.getInstance().setUserLoginResponse(uerLogin);
            tvCrop.setText(uerLogin.getSysUser().getUser_real_name());
        }else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.imgHomeAssetsScan,R.id.imgHomeAssetsSearch,R.id.txtHomeOut,R.id.img_write_epc})
    void performClick(View view){
        switch (view.getId()){
            case R.id.txtHomeOut:
                new MaterialDialog.Builder(this)
                        .title("提示")
                        .content("您确定退出程序吗？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                DataManager.getInstance().setLoginStatus(false);
                                finish();
                                System.exit(0);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.imgHomeAssetsScan:
                startActivity(new Intent(this, InvOrderActicity.class));
                break;
            case R.id.imgHomeAssetsSearch:
                startActivity(new Intent(this, ScanRfidActivity.class));
                break;
            case R.id.img_write_epc:
                startActivity(new Intent(this, WriteEpcActivity.class));
                break;


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEPC(UhfMsgEvent uhfMsgEvent) {

        switch(uhfMsgEvent.getType()) {
            case UhfMsgType.UHF_CONNECT:
                ToastUtils.showShort("RFID已连接");
                break;
            case UhfMsgType.UHF_DISCONNECT:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EsimAndroidApp.getIEsimUhfService().closeRFID();
        EventBus.getDefault().unregister(this);
    }
}
