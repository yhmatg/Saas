package com.common.esimrfid.uhf;

import android.util.Log;
import android.view.KeyEvent;

import com.module.interaction.ModuleConnector;
import com.nativec.tools.ModuleManager;
import com.rfid.RFIDReaderHelper;
import com.rfid.ReaderConnector;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.ReaderSetting;
import com.rfid.rxobserver.bean.RXInventoryTag;

import org.greenrobot.eventbus.EventBus;


/**
 * @author rylai
 * created at 2019/5/31 10:54
 */
public class RodinbellUhfServiceImpl extends EsimUhfAbstractService{
    private static final String TAG = "RodinbellUhfServiceImpl";
    private ModuleConnector connector = new ReaderConnector();
    private RFIDReaderHelper mReaderHelper;

    /**
     * 初始化设备
     *
     * @author rylai
     * created at 2019/5/31 10:58
     */
    @Override
    public boolean initRFID() {
        if (connector.connectCom("dev/ttyS4", 115200)) {
            //Power on the UHF,must set the UHF can work.
            ModuleManager.newInstance().setUHFStatus(true);
            try {
                mReaderHelper = RFIDReaderHelper.getDefaultHelper();
                mReaderHelper.registerObserver(rxObserver);
                mEnable=true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean closeRFID() {
        try {
            mEnable=false;
            if (mReaderHelper != null) {
                mReaderHelper.unRegisterObserver(rxObserver);
            }
            if (connector != null) {
                connector.disConnect();
            }
            ModuleManager.newInstance().setUHFStatus(false);
            ModuleManager.newInstance().release();
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
    public int getDownKey() {
        return KeyEvent.KEYCODE_F4;
    }

    @Override
    public boolean startScanning() {
        try {
            if (!isStart) {
                isStart = true;
                mReaderHelper.realTimeInventory(ReaderSetting.newInstance().btReadId, (byte) 0x01);
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
        UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_STOP);
        EventBus.getDefault().post(uhfMsgEvent);
        return true;
    }
    private RXObserver rxObserver = new RXObserver() {
        @Override
        protected void onInventoryTag(RXInventoryTag tag) {
            try {
                if (isStart) {
                    //modify yhm 20190709 start
                    //UhfTag utag=new UhfTag(tag.strEPC);
                    UhfTag utag=new UhfTag(tag.strEPC.replaceAll(" ", ""));
                    //modify yhm 20190709 end
                    UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.INV_TAG,utag);
                    Log.e(TAG,"epc=====" + uhfMsgEvent.getData().getEpc());
                    EventBus.getDefault().post(uhfMsgEvent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onInventoryTagEnd(RXInventoryTag.RXInventoryTagEnd tagEnd) {
            super.onInventoryTagEnd(tagEnd);
            Log.e("EPC", "----EPC==");

            try {
                if (isStart) {
                    mReaderHelper.realTimeInventory(ReaderSetting.newInstance().btReadId, (byte) 0x01);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
