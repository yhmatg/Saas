package com.common.esimrfid.uhf;

import android.util.Log;
import android.view.KeyEvent;

import com.common.esimrfid.utils.StringUtils;
import com.uhf.scanlable.UHfData;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import cn.pda.serialport.Tools;

public class HuiGangUhfServiceImpl extends EsimUhfAbstractService {
    private String devport = "/dev/ttyMT1";
    boolean isStart;
    private Timer timer;
    private boolean locaitonStart = false;
    private String filterData;

    public HuiGangUhfServiceImpl() {
        initRFID();
        timer = new Timer();
    }

    @Override
    public boolean initRFID() {
        try {
            int result = UHfData.UHfGetData.OpenUHf(devport, 57600);
            if (result == 0) {
                Thread.sleep(200);
                int i = UHfData.UHfGetData.GetUhfInfo();
                Log.i("Huang, MainActivity", "GetUhfInfo: " + i);
                if (i == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean startScanning() {
        isStart = true;
        UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_START);
        EventBus.getDefault().post(uhfMsgEvent);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UHfData.lsTagList.clear();
                UHfData.dtIndexMap.clear();
                if(locaitonStart){
                    UHfData.Inventory_6c_Mask((byte)0,96,0, UHfData.UHfGetData.hexStringToBytes(filterData));
                }else {
                    UHfData.Inventory_6c(0, 0);
                }
                for (UHfData.InventoryTagMap inventoryTagMap : UHfData.lsTagList) {
                    UhfTag utag = new UhfTag(inventoryTagMap.strEPC, inventoryTagMap.strTID, inventoryTagMap.strRSSI, null);
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.INV_TAG, utag);
                    EventBus.getDefault().post(uhfMsgEvent);
                }
            }
        }, 0, 5);
        return true;
    }

    @Override
    public boolean stopScanning() {
        timer.cancel();
        isStart = false;
        UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_STOP);
        EventBus.getDefault().post(uhfMsgEvent);
        return true;
    }

    @Override
    public boolean closeRFID() {
        UHfData.UHfGetData.CloseUHf();
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
    public int getDownKey() {
        return KeyEvent.KEYCODE_DPAD_UP;
    }

    @Override
    public void writeEpcTag(String selectEpc, String epcData) {
        int writeResult = UHfData.UHfGetData.Write6c((byte) 6, (byte) 6,
                UHfData.UHfGetData.hexStringToBytes(selectEpc),
                (byte) 1, Tools.intToByte(2), UHfData.UHfGetData.hexStringToBytes(epcData),
                UHfData.UHfGetData.hexStringToBytes("00000000"));
        if (writeResult == 0) {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_WRITE_SUC);
            EventBus.getDefault().post(uhfMsgEvent);
        } else {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_READ_FAIL);
            EventBus.getDefault().post(uhfMsgEvent);
        }
    }

    @Override
    public void setPower(int data) {
        int setResult = UHfData.UHfGetData.SetRfPower((byte) data);
        if (setResult == 0) {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_POWER_SUCCESS);
            EventBus.getDefault().post(uhfMsgEvent);
        } else {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_POWER_FAIL);
            EventBus.getDefault().post(uhfMsgEvent);
        }
    }

    @Override
    public int getPower() {
        int getResult = UHfData.UHfGetData.GetUhfInfo();
        if (getResult == 0) {
            return UHfData.UHfGetData.getUhfdBm()[0];
        }
        return super.getPower();
    }

    @Override
    public int setFilterData(int area, int start, int length, String data, boolean isSave) {
        if (StringUtils.isEmpty(data)) {
            locaitonStart = false;
            filterData = "";
        } else {
            locaitonStart = true;
            filterData = data;
        }
        return 0;
    }
}