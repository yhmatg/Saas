package com.common.esimrfid.uhf;

/**
 * Created by tiansen on 18-3-28.
 */

public abstract class EsimUhfAbstractService implements IEsimUhfService {
    public boolean isStart;
    public boolean mEnable=false;
    public boolean isSoundOpen=true;
    public abstract boolean initRFID();

    public abstract boolean startScanning();

    public abstract boolean closeRFID();

    public abstract boolean startStopScanning();

    public abstract int getDownKey();
    public abstract boolean stopScanning();

    public abstract void writeEpcTag(String selectEpc, String epcData);
    @Override
    public boolean isEnable(){
        return mEnable;
    }
    @Override
    public boolean isStart(){
        return isStart;
    }
    @Override
    public void setSoundOpen(boolean isOpen){
        this.isSoundOpen=isOpen;
    }
    @Override
    public boolean isSound(){
        return isSoundOpen;
    }
    /**
     * 处理补位
     *
     * @param epc
     * @return
     */
    public String preProcessEpc(String epc) {
        int epcLen = epc.length();
        if (epcLen != 24 || !epc.startsWith("00")) {
            // 去除末尾所有的0
            if (epc.endsWith("00000000")) {
                epc = epc.substring(0, epc.lastIndexOf("00000000"));
            }

            // 只对4个字节整数倍的epc数据进行处理
            if (epc.length() % 8 != 0) return epc;

            if (epc.endsWith("04040404")) {        // 去除补位数 4 个字节
                epc = epc.substring(0, epc.lastIndexOf("04040404"));

            } else if (epc.endsWith("030303")) {   // 去除补位数 3 个字节
                epc = epc.substring(0, epc.lastIndexOf("030303"));

            } else if (epc.endsWith("0202")) {     // 去除补位数 2 个字节
                epc = epc.substring(0, epc.lastIndexOf("0202"));

            } else if (epc.endsWith("01")) {       // 去除补位数 1 个字节
                epc = epc.substring(0, epc.lastIndexOf("01"));
            }
        }
        return epc;
    }
}
