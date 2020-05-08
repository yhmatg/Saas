package com.common.esimrfid.ui.tagwrite;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.utils.ScreenSizeUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class SearchTagActivity extends BaseActivity {
    @BindView(R.id.title_back)
    ImageView titleLeft;
    @BindView(R.id.title_content)
    TextView title;
    @BindView(R.id.scan_outer)
    ImageView scan_outer;
    @BindView(R.id.sacn_inner)
    ImageView scan_inner;
    @BindView(R.id.discern_success)
    ImageView discern_success;
    @BindView(R.id.btn_confirm_write)
    Button confirm;
    @BindView(R.id.identify_tips)
    TextView identify;
    IEsimUhfService esimUhfService = null;
    private static final String TAG_EPC = "tag_epc";
    List<String> scanEpcs = new ArrayList<>();
    private String getTagEpc;//要写入的Epc
    private String scanTagEpc = null;//扫描到的Epc
    private boolean isClick;
    private Animation anim1, anim2;
    private Boolean canRfid = true;
    @Override
    protected void initEventAndData() {
        title.setText("确认写入");
        Intent intent = getIntent();
        getTagEpc = intent.getStringExtra(TAG_EPC);
        rotateAnim1();
        rotateAnim2();
        initRfidAndEvent();
    }

    @Override
    public AbstractPresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_tag;
    }

    @Override
    protected void initToolbar() {

    }

    private void initRfidAndEvent() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        EventBus.getDefault().register(this);
    }

    //扫描时图片进行旋转(外)
    private void rotateAnim1() {
        anim1 = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setFillAfter(true); // 设置保持动画最后的状态
        anim1.setDuration(2000); // 设置动画时间
        anim1.setRepeatCount(Animation.INFINITE);//设置动画重复次数 无限循环
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setRepeatMode(Animation.RESTART);
    }

    //图片旋转（内）
    private void rotateAnim2() {
        anim2 = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setFillAfter(true); // 设置保持动画最后的状态
        anim2.setDuration(500); // 设置动画时间
        anim2.setRepeatCount(Animation.INFINITE);//设置动画重复次数 无限循环
        anim2.setInterpolator(new LinearInterpolator());
        anim2.setRepeatMode(Animation.RESTART);
    }

    private void showfailDialog() {
        final Dialog dialog = new Dialog(this, R.style.SettingDialog);
        View view = View.inflate(this, R.layout.write_success_dialog, null);
        Button confirm2 = (Button) view.findViewById(R.id.btn_confirm);
        TextView content = (TextView) view.findViewById(R.id.tv_content);
        dialog.setContentView(view);
        content.setText(R.string.write_epc_fail);
        dialog.setCanceledOnTouchOutside(true);
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(getApplication()).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(getApplication()).getScreenWidth() * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        confirm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showSuccessDialog() {
        final Dialog dialog = new Dialog(this, R.style.SettingDialog);
        View view = View.inflate(this, R.layout.write_success_dialog, null);
        Button confirm1 = (Button) view.findViewById(R.id.btn_confirm);
        TextView content = (TextView) view.findViewById(R.id.tv_content);
        dialog.setContentView(view);
        content.setText(R.string.write_epc_sucess);
        dialog.setCanceledOnTouchOutside(true);
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(getApplication()).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(getApplication()).getScreenWidth() * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        confirm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @OnClick({R.id.title_back})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag = (UhfTag) uhfMsgEvent.getData();
                scanTagEpc = uhfTag.getEpc();
                //if (!scanEpcs.contains(scanTagEpc)) {
                if (scanEpcs.size() == 0) {
                    scanEpcs.add(scanTagEpc);
                }
                Log.e("hanldeEpc",scanTagEpc);
                handleEpc();
                break;
            case UhfMsgType.UHF_START:
                scan_outer.startAnimation(anim1);
                scan_inner.startAnimation(anim2);
                discern_success.setVisibility(View.GONE);
                scan_inner.setVisibility(View.VISIBLE);
                scan_outer.setVisibility(View.VISIBLE);
                identify.setText(R.string.identify_tips);
                isClick = false;
                scanEpcs.clear();
                handleClick();
                break;
            case UhfMsgType.UHF_STOP:
                scan_outer.clearAnimation();
                scan_inner.clearAnimation();
                break;
            case UhfMsgType.UHF_WRITE_SUC:
                showSuccessDialog();
                break;
            case UhfMsgType.UHF_READ_FAIL:
                showfailDialog();
                break;
        }
    }

    private void handleEpc() {
        if (scanEpcs.size() == 1) {
            scan_outer.clearAnimation();
            scan_inner.clearAnimation();
            discern_success.setVisibility(View.VISIBLE);
            scan_inner.setVisibility(View.GONE);
            scan_outer.setVisibility(View.GONE);
            identify.setText(R.string.identify_success);
            esimUhfService.stopScanning();
            isClick = true;
        } else if (scanEpcs.size() > 1) {
            esimUhfService.startScanning();
            discern_success.setVisibility(View.GONE);
            scan_inner.setVisibility(View.VISIBLE);
            scan_outer.setVisibility(View.VISIBLE);
            identify.setText(R.string.identify_tags);
            isClick = false;
        } else {
            scanEpcs.size();
            discern_success.setVisibility(View.GONE);
            scan_inner.setVisibility(View.VISIBLE);
            scan_outer.setVisibility(View.VISIBLE);
            identify.setText(R.string.identify_tips);
            isClick = false;
        }
        handleClick();
    }

    //处理按钮点击事件
    private void handleClick() {
        if (isClick) {
            confirm.setEnabled(true);
            confirm.setBackgroundResource(R.drawable.btn_confirm_write);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                        esimUhfService.writeEpcTag(scanTagEpc, getTagEpc);
                    } else {
                        ToastUtils.showShort(R.string.not_connect_prompt);
                    }
                }
            });
        } else {
            confirm.setEnabled(false);
            confirm.setBackgroundResource(R.drawable.btn_unconfirm_write);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(canRfid){
            if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                if (keyCode == esimUhfService.getDownKey()) { //扳机建扫描
                    esimUhfService.startStopScanning();
                }
            } else if (keyCode == Utils.getDiffDownKey()) {
                ToastUtils.showShort(R.string.not_connect_prompt);
            }
            canRfid = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        canRfid = true;
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null ){
            esimUhfService.setEnable(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null ){
            esimUhfService.setEnable(false);
        }
    }
}
