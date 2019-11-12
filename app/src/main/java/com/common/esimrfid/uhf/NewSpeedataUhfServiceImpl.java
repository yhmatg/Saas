package com.common.esimrfid.uhf;

import android.serialport.DeviceControlSpd;
import android.view.KeyEvent;
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
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean closeRFID() {
        /*int i = driver.Close_Com();
        if(i == 0){
            UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_DISCONNECT);
            EventBus.getDefault().post(uhfMsgEvent);
            return true;
        }else {
            return false;
        }*/
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
                    UhfTag utag = new UhfTag(finalEpc);
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.INV_TAG, utag);
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
}
