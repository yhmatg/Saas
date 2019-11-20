package com.common.esimrfid.uhf;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.KeyEvent;

import com.common.esimrfid.app.EsimAndroidApp;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.bean.SpdInventoryData;
import com.speedata.libuhf.interfaces.OnSpdInventoryListener;
import com.speedata.libuhf.utils.SharedXmlUtil;
import com.speedata.libuhf.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * @author rylai
 * created at 2019/5/31 10:54
 */
public class SpeedataUhfServiceImpl extends EsimUhfAbstractService{
    private static final String TAG = "SpeedataUhfServiceImpl";
    private IUHFService iuhfService;
    public static final String SCAN = "com.geomobile.se4500barcode";
    public static final String update = "uhf.update";
    private SoundPool soundPool;
    private int soundId;
    public SpeedataUhfServiceImpl() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        if (soundPool == null) {
            Log.e("as3992", "Open sound failed");
        }
        soundId = soundPool.load("/system/media/audio/ui/VideoRecord.ogg", 0);
        //initRFID();
    }
    @Override
    public boolean initRFID() {
        try {
            Context applicationContext = EsimAndroidApp.getInstance();
            iuhfService = UHFManager.getUHFService(applicationContext);
//            iuhfService.setOnReadListener(readTagListener);
            iuhfService.setOnInventoryListener(inventoryListener);
            int open=iuhfService.openDev();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean closeRFID() {
        try {
            mEnable=false;
            iuhfService.closeDev();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean startStopScanning() {
        try {
            if (!isStart) {
                startScanning();
            } else {
                stopScanning();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean startScanning() {
        try {
            if (!isStart) {
                isStart = true;
                iuhfService.inventory_start();
                UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_START);
                EventBus.getDefault().post(uhfMsgEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    public boolean stopScanning() {
        isStart = false;
        iuhfService.inventory_stop();
        UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_STOP);
        EventBus.getDefault().post(uhfMsgEvent);
        return true;
    }

    @Override
    public int setFilterData(int area, int start, int length, String data, boolean isSave) {
        return 0;
    }

    private OnSpdInventoryListener inventoryListener=new OnSpdInventoryListener() {
        @Override
        public void getInventoryData(SpdInventoryData var1) {
            Log.d(TAG,"epc:"+var1.epc);
            UhfTag utag=new UhfTag(var1.epc);
            UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.INV_TAG,utag);
            if(isSoundOpen) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
            EventBus.getDefault().post(uhfMsgEvent);
        }
    };
    @Override
    public int getDownKey() {
        return KeyEvent.KEYCODE_F1;
    }

    public void setAccessEpcMatch(String selectEpc){
        boolean u8 = SharedXmlUtil.getInstance(EsimAndroidApp.getInstance()).read("U8", false);
        if (u8) {
            selectEpc = selectEpc.substring(0, 24);
        }
        int res = iuhfService.selectCard(1, selectEpc, true);
        Log.e("select=======","res=====" + res);
        if (res == 0) {
        } else {

        }

    }

    public void writeEpcTag( String epcData){
        //区域
        int which_choose = 1;
        //起始地址
        int addr = 2;
        int count = 2;
        String str_passwd = "00000000";
        byte[] write = StringUtils.stringToByte(epcData);
        int writeArea = iuhfService.writeArea(which_choose, addr, count, str_passwd, write);
    }

    @Override
    public void writeEpcTag(String selectEpc, String epcData) {
        
    }
}
