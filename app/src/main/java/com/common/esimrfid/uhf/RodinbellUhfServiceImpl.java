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
import com.util.StringTool;

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
                //mEnable=true;
                UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_CONNECT);
                EventBus.getDefault().post(uhfMsgEvent);
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
            UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_DISCONNECT);
            EventBus.getDefault().post(uhfMsgEvent);
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

    @Override
    public int setFilterData(int area, int start, int length, String data, boolean isSave) {
        return 0;
    }

    @Override
    public void setPower(int data) {

    }

    @Override
    public int getPower() {
        return 0;
    }

    @Override
    public int getBatteryLevel() {
        return 0;
    }

    @Override
    public void setBeeper(boolean hostBeeper, boolean sledBeeperEnable) {

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

    public void setAccessEpcMatch(String selectEpc){
        String[] result = StringTool.stringToStringArray(selectEpc.toUpperCase(), 2);
        byte[] btAryEpc = StringTool.stringArrayToByteArray(result, result.length);
        mReaderHelper.setAccessEpcMatch(ReaderSetting.newInstance().btReadId, (byte)(btAryEpc.length & 0xFF), btAryEpc);
    }

    public void writeEpcTag( String epcData){
        String[] epcResult = StringTool.stringToStringArray(epcData.toUpperCase(), 2);
        //读写区 （epc）固定 0x01
        byte btMemBank = 0x01;
        //写入的起始地址 固定 02
        byte btWordAdd = 0x02;
        //访问密码 固定 00000000
        String[] PassWardresult = StringTool.stringToStringArray("00000000".toUpperCase(), 2);
        byte []btAryPassWord = StringTool.stringArrayToByteArray(PassWardresult, 4);
        //新epc数据
        byte[] btAryData = StringTool.stringArrayToByteArray(epcResult, epcResult.length);
        //长度
        byte btWordCnt = (byte)((epcResult.length / 2 + epcResult.length % 2) & 0xFF);
        mReaderHelper.writeTag(ReaderSetting.newInstance().btReadId, btAryPassWord, btMemBank, btWordAdd, btWordCnt, btAryData);
    }

    @Override
    public void writeEpcTag(String selectEpc, String epcData) {
        String[] result = StringTool.stringToStringArray(selectEpc.toUpperCase(), 2);
        byte[] btAryEpc = StringTool.stringArrayToByteArray(result, result.length);
        mReaderHelper.setAccessEpcMatch(ReaderSetting.newInstance().btReadId, (byte)(btAryEpc.length & 0xFF), btAryEpc);

        String[] epcResult = StringTool.stringToStringArray(epcData.toUpperCase(), 2);
        //读写区 （epc）固定 0x01
        byte btMemBank = 0x01;
        //写入的起始地址 固定 02
        byte btWordAdd = 0x02;
        //访问密码 固定 00000000
        String[] PassWardresult = StringTool.stringToStringArray("00000000".toUpperCase(), 2);
        byte []btAryPassWord = StringTool.stringArrayToByteArray(PassWardresult, 4);
        //新epc数据
        byte[] btAryData = StringTool.stringArrayToByteArray(epcResult, epcResult.length);
        //长度
        byte btWordCnt = (byte)((epcResult.length / 2 + epcResult.length % 2) & 0xFF);
        int writeResult = mReaderHelper.writeTag(ReaderSetting.newInstance().btReadId, btAryPassWord, btMemBank, btWordAdd, btWordCnt, btAryData);
        if(writeResult == 0){
            UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_WRITE_SUC);
            EventBus.getDefault().post(uhfMsgEvent);
        }else {
            UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_READ_FAIL);
            EventBus.getDefault().post(uhfMsgEvent);
        }

    }
}
