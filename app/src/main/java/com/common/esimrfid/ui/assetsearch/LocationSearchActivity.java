package com.common.esimrfid.ui.assetsearch;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class LocationSearchActivity extends BaseActivity {
    private static final String TAG = "LocationSearchActivity";
    private static final String ASSETS_EPC = "assets_epc";
    @BindView(R.id.energy)
    EnergyView progressBar;
    @BindView(R.id.title_back)
    ImageView titltLeft;
    @BindView(R.id.title_content)
    TextView title;
    private String AssetsEpc;
    IEsimUhfService esimUhfService = null;
    private short maxValue = -30, minValue = -80; //RSSI的最大值和最小值
    private SoundPool soundPool;
    private int soundId;
    private long currentMinute, oldMinute;

    @Override
    public AbstractPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData() {
        initRfidAndEvent();
        title.setText(R.string.location_search);
        progressBar.setMaxProgress(60);
        progressBar.clearProgress();
//        progressBar.setEmptyVisibility(View.GONE);
        Intent intent = getIntent();
        AssetsEpc = intent.getStringExtra(ASSETS_EPC);
        setSound();
        filterSet();
    }

    private void setSound() {
        AudioAttributes abs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)   //设置允许同时播放的流的最大值
                .setAudioAttributes(abs)   //完全可以设置为null
                .build();

        soundId = soundPool.load(this, R.raw.beep, 1);
    }


    private void initRfidAndEvent() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location_search;
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
        Log.d(TAG, "handleEPC: " + uhfMsgEvent.toString());
        String epc = null;
        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag = (UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getAllData();
                handleEpc(epc);
                break;
            case UhfMsgType.INV_TAG_NULL:
//                progressBar.setProgress(0);
                break;
            case UhfMsgType.UHF_START:
                break;
            case UhfMsgType.UHF_STOP:
                break;
        }
    }

    private void handleEpc(String epc) {
        int Hb = 0;
        int Lb = 0;
        int rssi = 0;
        String[] tmp = new String[3];
        String text = epc.substring(4);
        String len = epc.substring(0, 2);
        int epclen = (Integer.parseInt(len, 16) / 8) * 4;
        tmp[0] = text.substring(epclen, text.length() - 6);
        tmp[1] = text.substring(0, epclen);
        tmp[2] = text.substring(text.length() - 6, text.length() - 2);
        String content = tmp[1];
        if (4 != tmp[2].length()) {
            tmp[2] = "0000";
        } else {
            Hb = Integer.parseInt(tmp[2].substring(0, 2), 16);
            Lb = Integer.parseInt(tmp[2].substring(2, 4), 16);
            rssi = ((Hb - 256 + 1) * 256 + (Lb - 256)) / 10;
        }

        // int length = maxValue - minValue; //取差值
        if (rssi >= maxValue) {
            rssi = maxValue;
        } else if (rssi <= minValue) {
            rssi = minValue;
        }
        rssi -= minValue;//rssi+80转为正数
        String a=String.valueOf(rssi);
        Log.e("wzmmmmmmmmmm",a);
        progressBar.setProgress(rssi);
        playSound(rssi);
    }

    private void filterSet() {
        if (TextUtils.isEmpty(AssetsEpc)) {
            Toast.makeText(this, R.string.filter_data_null, Toast.LENGTH_SHORT).show();
            return;
        }
        int ads = 32;
        int len = 96;
        int val = 1;
        esimUhfService.setFilterData(val, ads, len, AssetsEpc, false);
    }

    //音源播放
    private void playSound(int val) {
        if (val > 20) {
            //playSound();
            soundPool.play(soundId, 1, 1, 0, 1, 1);
            oldMinute = System.currentTimeMillis();
        } else if (val > 10) {
            currentMinute = System.currentTimeMillis();
            if (currentMinute - oldMinute > 1000) {
                //playSound();
                soundPool.play(soundId, 0.7f, 0.7f, 0, 1, 1);
                oldMinute = currentMinute;
            }
        } else if (val > 0) {
            currentMinute = System.currentTimeMillis();
            if (currentMinute - oldMinute > 1500) {
                //playSound();
                soundPool.play(soundId, 0.4f, 0.4f, 0, 1, 1);
                oldMinute = currentMinute;
            }
        }
    }

    @Override
    protected void initToolbar() {

    }

    private void filterClear() {
        int ads = 0;
        int len = 0;
        int val = 1;
        esimUhfService.setFilterData(val, ads, len, "", false);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        EventBus.getDefault().unregister(this);
//        if (esimUhfService != null && esimUhfService.isStart()) {
//            esimUhfService.stopScanning();
//        }
//        filterClear();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (esimUhfService != null && esimUhfService.isStart()) {
            esimUhfService.stopScanning();
        }
        filterClear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (esimUhfService != null) {
            if (keyCode == esimUhfService.getDownKey()) { //扳机建扫描
                esimUhfService.startStopScanning();
            }
        } else if (keyCode == Utils.getDiffDownKey()) {
            ToastUtils.showShort(R.string.not_connect_prompt);
        }
        return super.onKeyDown(keyCode, event);
    }

}
