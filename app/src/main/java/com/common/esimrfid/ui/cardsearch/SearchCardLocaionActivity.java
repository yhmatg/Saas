package com.common.esimrfid.ui.cardsearch;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchCardLocaionActivity extends BaseActivity {
    private static final String TAG = "SearchCardLocaion";
    @BindView(R.id.search_tag)
    TextView mSearchTag;
    @BindView(R.id.pb_Search)
    ProgressBar pSearch;
    @BindView(R.id.tv_SearchTag)
    TextView tvCurrentTag;
    @BindView(R.id.tv_ast_name)
    TextView mAstName;
    @BindView(R.id.tv_ast_code)
    TextView mAstCode;

    IEsimUhfService esimUhfService = null;
    private short maxValue = -30, minValue = -80; //RSSI的最大值和最小值
    private String content;
    private SoundPool soundPool;
    private int soundId;
    private long currentMinute, oldMinute;
    private String astName;
    private String epcData;
    private String astBarcode;

    @Override
    public AbstractPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData() {
        initRfidAndEventbus();
        getIntentData();
        filterSet();
        AudioAttributes abs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build() ;
        soundPool =  new SoundPool.Builder()
                .setMaxStreams(10)   //设置允许同时播放的流的最大值
                .setAudioAttributes(abs)   //完全可以设置为null
                .build() ;

        soundId = soundPool.load(this, R.raw.beep, 1);
        mAstName.setText(astName);
        mAstCode.setText(astBarcode);
    }

    private void getIntentData() {
        astName = getIntent().getStringExtra("astName");
        epcData = getIntent().getStringExtra("epcData");
        astBarcode = getIntent().getStringExtra("astBarcode");
    }

    private void initRfidAndEventbus() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_tag;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({ R.id.search_tag})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.search_tag:
                if (esimUhfService != null) {
                    esimUhfService.startStopScanning();
                } else {
                    ToastUtils.showShort(R.string.not_connect_prompt);
                }
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
                DoWithEpc(epc);
                break;
            case UhfMsgType.INV_TAG_NULL:
                tvCurrentTag.setText(null);
                pSearch.setProgress(0);
                break;
            case UhfMsgType.UHF_START:
                mSearchTag.setText(R.string.stop_search);
                break;
            case UhfMsgType.UHF_STOP:
                mSearchTag.setText(R.string.start_search);
                break;
        }
    }

    private void filterSet() {
        if (TextUtils.isEmpty(epcData)) {
            Toast.makeText(this, R.string.filter_data_null, Toast.LENGTH_SHORT).show();
            return;
        }
        int Status = -1;
        int ads = 32;
        int len = 96;
        int val = 1;
        Status = esimUhfService.setFilterData(val,ads,len,epcData,false);
        Log.e(TAG, "Status===" + Status);
    }

    private void filterClear() {
        int Status = 0;
        int ads = 0;
        int len = 0;
        int val = 1;
        Status = esimUhfService.setFilterData(val,ads,len,"",false);
        Log.e(TAG, "Status===" + Status);
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

    //处理EPC
    public void DoWithEpc(String epcid) {
        Log.e("DoWithEpc", "Enter");
        int Hb = 0;
        int Lb = 0;
        int rssi = 0;
        String[] tmp = new String[3];
        String text = epcid.substring(4);
        String len = epcid.substring(0, 2);
        int epclen = (Integer.parseInt(len, 16) / 8) * 4;
        tmp[0] = text.substring(epclen, text.length() - 6);
        tmp[1] = text.substring(0, epclen);
        tmp[2] = text.substring(text.length() - 6, text.length() - 2);
        content = tmp[1];
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
        rssi -= minValue;
        tvCurrentTag.setText(content + "====" + rssi);
        pSearch.setProgress(rssi);
        playSound(rssi);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterClear();
        EventBus.getDefault().unregister(this);
    }
}
