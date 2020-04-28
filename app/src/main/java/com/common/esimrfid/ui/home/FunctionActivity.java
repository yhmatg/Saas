package com.common.esimrfid.ui.home;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.utils.SettingBeepUtil;
import com.common.esimrfid.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class FunctionActivity extends BaseActivity {

    @BindView(R.id.title_back)
    ImageView mBackImg;
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.sound_set)
    ImageView sound;
    @BindView(R.id.img_reduce)
    ImageView reduce;
    @BindView(R.id.img_plus)
    ImageView plus;
    @BindView(R.id.tv_num)
    TextView num;
    @BindView(R.id.seekbar)
    SeekBar seekBar;
    @BindView(R.id.seekbar_zebar)
    SeekBar seekBar_zebra;
    @BindView(R.id.sound_setting1)
    LinearLayout setting1;
    @BindView(R.id.sound_setting2)
    LinearLayout setting2;
    @BindView(R.id.sled_set)
    ImageView sled;
    @BindView(R.id.host_set)
    ImageView host;
    private int total;
    private boolean isOpen = true;
    private boolean sledOpen = true;
    private boolean hostOpen = true;
    IEsimUhfService esimUhfService = null;
    String model = android.os.Build.MODEL;

    @Override
    public AbstractPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        mTitle.setText("功能设置");
        initData();
    }

    private void initData() {
        if ("ESUR-H600".equals(model) || "SD60".equals(model)) {
            setting1.setVisibility(View.VISIBLE);
            setting2.setVisibility(View.GONE);
            seekBar.setVisibility(View.VISIBLE);
            seekBar_zebra.setVisibility(View.GONE);
        } else if ("TC20".equals(model)) {
            seekBar_zebra.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.GONE);
            setting1.setVisibility(View.VISIBLE);
            setting2.setVisibility(View.GONE);
        } else {
            seekBar_zebra.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.GONE);
            setting1.setVisibility(View.GONE);
            setting2.setVisibility(View.VISIBLE);
        }
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
            plus.setClickable(true);
            reduce.setClickable(true);
            seekBar.setEnabled(true);
            seekBar_zebra.setEnabled(true);
            sound.setClickable(true);
            sled.setClickable(true);
            host.setClickable(true);
            if ("ESUR-H600".equals(model) || "SD60".equals(model)) {
                total = esimUhfService.getPower();
                if (total >= 30) {
                    total = 30;
                } else if (total <= 0) {
                    total = 0;
                }
                seekBar.setMax(25);
                seekBar.setProgress(total - 5);
                num.setText(String.valueOf(total));
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        total = progress + 5;
                        num.setText(String.valueOf(total));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            } else {
                seekBar_zebra.setMax(300);
                total = esimUhfService.getPower();
                if (total >= 300) {
                    total = 300;
                } else if (total <= 0) {
                    total = 0;
                }
                seekBar_zebra.setProgress(total);
                num.setText(String.valueOf(total));
                seekBar_zebra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        total = progress;
                        num.setText(String.valueOf(total));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        } else {
            num.setText("0");
            plus.setClickable(false);
            reduce.setClickable(false);
            seekBar.setEnabled(false);
            seekBar_zebra.setEnabled(false);
            sound.setClickable(false);
            host.setClickable(false);
            sled.setClickable(false);
        }

        if (SettingBeepUtil.isOpen()) {
            isOpen = true;
            sound.setImageResource(R.drawable.img_open);
        } else {
            isOpen = false;
            sound.setImageResource(R.drawable.img_close);
        }

        if (SettingBeepUtil.isHostOpen()) {
            hostOpen = true;
            host.setImageResource(R.drawable.img_open);
        } else {
            hostOpen = false;
            host.setImageResource(R.drawable.img_close);
        }

        if ("TC20".equals(model)) {
            sled.setEnabled(false);
            sledOpen = false;
            SettingBeepUtil.setSledOpen(false);
            sled.setImageResource(R.drawable.img_close);
        } else {
            if (SettingBeepUtil.isSledOpen()) {
                sledOpen = true;
                sled.setImageResource(R.drawable.img_open);
            } else {
                sledOpen = false;
                sled.setImageResource(R.drawable.img_close);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_function;
    }

    @OnClick({R.id.img_plus, R.id.img_reduce, R.id.sound_set, R.id.title_back, R.id.sled_set, R.id.host_set})
    void perform(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                    esimUhfService.setPower(total);
                    esimUhfService.setBeeper();
                    ToastUtils.showShort(R.string.save_newinv_succ);
                }
                finish();
                break;
            case R.id.img_plus:
                total = total + 1;
                seekBar.setProgress(total - 5);
                seekBar_zebra.setProgress(total);
                break;
            case R.id.img_reduce:
                total = total - 1;
                seekBar.setProgress(total - 5);
                seekBar_zebra.setProgress(total);
                break;
            case R.id.sound_set:
                setSoundOpen();
                break;
            case R.id.sled_set:
                setSledOpen();
                break;
            case R.id.host_set:
                setHostOpen();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.SETTING_SOUND_FAIL:
                ToastUtils.showShort("手持机声音设置失败，请检查连接退出重试！");
                break;
        }
    }

    private void setHostOpen() {
        if (hostOpen) {
            hostOpen = false;
            host.setSelected(false);
            SettingBeepUtil.setHostOpen(false);
            host.setImageResource(R.drawable.img_close);
        } else {
            hostOpen = true;
            host.setSelected(true);
            SettingBeepUtil.setHostOpen(true);
            host.setImageResource(R.drawable.img_open);
        }
    }

    private void setSledOpen() {
        if (sledOpen) {
            sledOpen = false;
            sled.setSelected(false);
            SettingBeepUtil.setSledOpen(false);
            sled.setImageResource(R.drawable.img_close);
        } else {
            sledOpen = true;
            sled.setSelected(true);
            SettingBeepUtil.setSledOpen(true);
            sled.setImageResource(R.drawable.img_open);
        }
    }

    private void setSoundOpen() {
        if (isOpen) {
            isOpen = false;
            sound.setSelected(false);
            SettingBeepUtil.setOpen(false);
            sound.setImageResource(R.drawable.img_close);
        } else {
            isOpen = true;
            sound.setSelected(true);
            SettingBeepUtil.setOpen(true);
            sound.setImageResource(R.drawable.img_open);
        }
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataManager.getInstance().setOpenBeeper(SettingBeepUtil.isOpen());
        DataManager.getInstance().setSledBeeper(SettingBeepUtil.isSledOpen());
        DataManager.getInstance().setHostBeeper(SettingBeepUtil.isHostOpen());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                esimUhfService.setPower(total);
                ToastUtils.showShort(R.string.save_newinv_succ);
                esimUhfService.setBeeper();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
