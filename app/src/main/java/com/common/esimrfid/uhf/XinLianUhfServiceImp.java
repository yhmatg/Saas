package com.common.esimrfid.uhf;

public class XinLianUhfServiceImp extends EsimUhfAbstractService  {
   /* private IUHFService xinLianService;
    //普通声音
    private static ToneGenerator toneGenerator;
    private boolean beepON = false;
    private Timer tbeep;
    //定位声音
    private final int BEEP_DELAY_TIME_MIN = 0;
    private final int BEEP_DELAY_TIME_MAX = 300;
    private boolean beepONLocate;
    private Timer locatebeep;
    private boolean locaitonStart = false;
    private String filterData;

    public XinLianUhfServiceImp() {
        xinLianService = UHFManager.getUHFService(EsimAndroidApp.getInstance());
        xinLianService.setOnInventoryListener(this);
    }

    @Override
    public boolean initRFID() {
        int i = xinLianService.openDev();
        if (i == 0) {
            beeperSettings();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean closeRFID() {
        xinLianService.closeDev();
        UHFManager.closeUHFService();
        return true;
    }

    @Override
    public boolean startStopScanning() {
        if (!isStart) {
            startScanning();
        } else {
            stopScanning();
        }
        return true;
    }

    @Override
    public boolean startScanning() {
        if (!isStart) {
            isStart = true;
            xinLianService.inventoryStart();
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_START);
            EventBus.getDefault().post(uhfMsgEvent);
        }
        return true;
    }

    @Override
    public boolean stopScanning() {
        if (isStart) {
            isStart = false;
            xinLianService.inventoryStop();
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_STOP);
            EventBus.getDefault().post(uhfMsgEvent);
        }
        return true;
    }

    @Override
    public int getPower() {
        return xinLianService.getAntennaPower();
    }

    @Override
    public void setPower(int data) {
        int status = xinLianService.setAntennaPower(data);
        if (status == -1) {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_POWER_FAIL);
            EventBus.getDefault().post(uhfMsgEvent);
        } else if(status == 0){
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_POWER_SUCCESS);
            EventBus.getDefault().post(uhfMsgEvent);
        }
    }

    //需要先加入过滤才瑞迪模块功能类似
    @Override
    public void writeEpcTag(String selectEpc, String epcData) {
        //选中区域 epc:1
        int selectArea = 1;
        //addr 要读地址，以word计算
        int addr = 2;
        //写入的块数,word为单位
        int count = 6;
        //访问口令
        String passWard = "00000000";
        //需要写入内容
        byte[] content = DataConversionUtils.hexStringToByteArray(epcData);
        int writeResult = xinLianService.writeArea(selectArea, addr, count, passWard, content);
        if (writeResult == 0) {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_WRITE_SUC);
            EventBus.getDefault().post(uhfMsgEvent);
        } else {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_READ_FAIL);
            EventBus.getDefault().post(uhfMsgEvent);
        }
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
        return xinLianService.selectCard(area, data, isSave);
    }

    //盘点成功，返回盘点数据
    @Override
    public void getInventoryData(SpdInventoryData spdInventoryData) {
        UhfTag utag = new UhfTag(spdInventoryData.getEpc(), spdInventoryData.getTid(), spdInventoryData.getRssi(), null);
        UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.INV_TAG, utag);
        EventBus.getDefault().post(uhfMsgEvent);
        if (SettingBeepUtil.isOpen()) {
            if (locaitonStart) {
                if (filterData.equals(spdInventoryData.getEpc())) {
                    short rssi = Short.parseShort(spdInventoryData.getRssi());
                    Log.e("rssi", "rssi======" + rssi);
                    startLocationBeeping(rssi);
                }
            } else {
                startbeepingTimer();
            }

        }
    }

    //盘点失败，返回错误码
    @Override
    public void onInventoryStatus(int i) {

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

    private void startbeepingTimer() {
        if (!beepON) {
            beepON = true;
            beep();
            if (tbeep == null) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        stopbeepingTimer();
                        beepON = false;
                    }
                };
                tbeep = new Timer();
                tbeep.schedule(task, 80);
            }
        }
    }

    private synchronized void stopbeepingTimer() {
        if (tbeep != null) {
            toneGenerator.stopTone();
            tbeep.cancel();
            tbeep.purge();
        }
        tbeep = null;
    }

    private void startLocationBeeping(short dist) {
        int POLLING_INTERVAL1 = BEEP_DELAY_TIME_MIN + (((BEEP_DELAY_TIME_MAX - BEEP_DELAY_TIME_MIN) * (100 - dist)) / 100);
        if (!beepONLocate) {
            beepONLocate = true;
            beep();
            if (locatebeep == null) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        stoplocatebeepingTimer();
                        beepONLocate = false;
                    }
                };
                locatebeep = new Timer();
                locatebeep.schedule(task, POLLING_INTERVAL1, 10);
            }
        }
    }

    private void stoplocatebeepingTimer() {
        if (locatebeep != null) {
            toneGenerator.stopTone();
            locatebeep.cancel();
            locatebeep.purge();
        }
        locatebeep = null;
    }
*/
}
