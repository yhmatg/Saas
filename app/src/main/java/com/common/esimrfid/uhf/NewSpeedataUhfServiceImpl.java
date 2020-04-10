package com.common.esimrfid.uhf;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.serialport.DeviceControlSpd;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.common.esimrfid.R;
import com.common.esimrfid.utils.SettingBeepUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import cn.com.example.rfid.driver.Driver;
import cn.com.example.rfid.driver.RfidDriver;

import static android.serialport.DeviceControlSpd.PowerType.EXPAND;


/**
 * @author rylai
 * created at 2019/5/31 10:54
 */
public class NewSpeedataUhfServiceImpl extends EsimUhfAbstractService {
    private static final String TAG = "SpeedataUhfServiceImpl";
    private Driver driver;
    private DeviceControlSpd newUHFDeviceControl;
    private static ToneGenerator toneGenerator;

    public NewSpeedataUhfServiceImpl() {
        driver = new RfidDriver();
        int[] gpios = {9, 14};
        try {
            newUHFDeviceControl = new DeviceControlSpd(EXPAND, gpios);
            newUHFDeviceControl.PowerOnDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean initRFID() {
        int status = driver.initRFID("/dev/ttyMT0");
        Logger.e("speed==", "status===" + status);
        if (1000 == status) {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_CONNECT);
            EventBus.getDefault().post(uhfMsgEvent);
            beeperSettings();
            return true;
        } else {
            UhfMsgEvent<UhfTag> uhfMsgEvent1=new UhfMsgEvent<>(UhfMsgType.UHF_CONNECT_FAIL);
            EventBus.getDefault().post(uhfMsgEvent1);
            return false;
        }

    }

    @Override
    public boolean closeRFID() {
        try {
            newUHFDeviceControl.PowerOffDevice();
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_DISCONNECT);
            EventBus.getDefault().post(uhfMsgEvent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
                int status = driver.readMore();
                new TagThread().start();
                UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_START);
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
        if (isStart) {
            isStart = false;
            driver.stopRead();
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_STOP);
            EventBus.getDefault().post(uhfMsgEvent);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public int setFilterData(int area, int start, int length, String data, boolean isSave) {
        return driver.Set_Filter_Data(area, start, length, data, isSave);
    }

    //设置功率
    @Override
    public void setPower(int data) {
        int status = driver.setTxPowerOnce(data);
        if (-1000 == status || (-1020) == status || 0 == status) {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_POWER_FAIL);
            EventBus.getDefault().post(uhfMsgEvent);
        } else {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_POWER_SUCCESS);
            EventBus.getDefault().post(uhfMsgEvent);
        }
    }

    //获取当前功率
    @Override
    public int getPower() {
        int power = driver.GetTxPower();
        return power;
    }

    @Override
    public int getBatteryLevel() {
        return 0;
    }

    @Override
    public void setBeeper() {
    }


    @Override
    public int getDownKey() {
        return KeyEvent.KEYCODE_F1;
    }

    @Override
    public void writeEpcTag(String selectEpc, String epcData) {
        //密码
        String passWard = "00000000";
        //选中区域 epc:1
        int selectArea = 1;
        //选中标签起始地址
        int selectStartAdr = 32;
        //选中标签长度
        int selectLength = 96;
        //selectEpc 选中标签epc数据
        //写入区域 epc:1
        int writeArea = 1;
        //写入标签起始地址
        int writeStartAdr = 2;
        //写入标签长度
        int writeLength = 6;
        //epcData 要写入的epc数据
        int writeResult = driver.Write_Data_Tag(passWard, selectArea, selectStartAdr, selectLength, selectEpc, writeArea, writeStartAdr, writeLength, epcData);
        if (writeResult == 0) {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_WRITE_SUC);
            EventBus.getDefault().post(uhfMsgEvent);
        } else {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_READ_FAIL);
            EventBus.getDefault().post(uhfMsgEvent);
        }
    }

    class TagThread extends Thread {

        private int mBetween = 0;

        public TagThread() {

        }

        public void run() {

            while (isStart) {

                String[] strEpc1 = {driver.GetBufData()};
                String strEpc = strEpc1[0];
                if (strEpc != null && strEpc.length() != 0) {
                    String text = strEpc.substring(4);
                    String len = strEpc.substring(0, 2);
                    int epclen = (Integer.parseInt(len, 16) / 8) * 4;
                    String finalEpc = text.substring(0, epclen);
                    UhfTag utag = new UhfTag(finalEpc, null, null, strEpc);
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.INV_TAG, utag);
                    EventBus.getDefault().post(uhfMsgEvent);
                    if (SettingBeepUtil.isOpen()) {
                        beep();
                    }
                } else {
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.INV_TAG_NULL);
                    EventBus.getDefault().post(uhfMsgEvent);
                }
                try {
                    sleep(mBetween);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void beeperSettings() {
        int streamType = AudioManager.STREAM_DTMF;
        int percantageVolume = 100;
        toneGenerator = new ToneGenerator(streamType, percantageVolume);
    }

    public static void beep() {
        int toneType = ToneGenerator.TONE_PROP_BEEP;
        toneGenerator.startTone(toneType);
    }
}
