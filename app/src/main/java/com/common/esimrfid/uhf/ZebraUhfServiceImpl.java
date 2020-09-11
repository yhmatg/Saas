package com.common.esimrfid.uhf;

import android.app.Application;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;

import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.ui.identity.IDcsSdkApiDelegateImp;
import com.common.esimrfid.utils.SettingBeepUtil;
import com.common.esimrfid.utils.StringUtils;
import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.BEEPER_VOLUME;
import com.zebra.rfid.api3.DYNAMIC_POWER_OPTIMIZATION;
import com.zebra.rfid.api3.ENUM_TRANSPORT;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.Events;
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE;
import com.zebra.rfid.api3.INVENTORY_STATE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.MEMORY_BANK;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.RFIDResults;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.RfidEventsListener;
import com.zebra.rfid.api3.RfidReadEvents;
import com.zebra.rfid.api3.RfidStatusEvents;
import com.zebra.rfid.api3.SESSION;
import com.zebra.rfid.api3.SL_FLAG;
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TagAccess;
import com.zebra.rfid.api3.TagData;
import com.zebra.rfid.api3.TriggerInfo;
import com.zebra.scannercontrol.DCSSDKDefs;
import com.zebra.scannercontrol.DCSScannerInfo;
import com.zebra.scannercontrol.SDKHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.zebra.scannercontrol.DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_BT_NORMAL;

/**
 * @author rylai
 * created at 2019/5/31 10:54
 */
public class ZebraUhfServiceImpl extends EsimUhfAbstractService implements Readers.RFIDReaderEventHandler {
    final static String TAG = "RFID_SAMPLE";
    // RFID Reader
    public static BEEPER_VOLUME sledBeeperVolume = BEEPER_VOLUME.HIGH_BEEP;
    private static BEEPER_VOLUME beeperVolume = BEEPER_VOLUME.HIGH_BEEP;
    private static final int BEEP_DELAY_TIME_MIN = 0;
    private static final int BEEP_DELAY_TIME_MAX = 300;
    private static Readers readers;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    private static RFIDReader reader;
    private EventHandler eventHandler;
    private int MAX_POWER = 300;
    private String readername = "RFD8500";
    private Application instance;
    private boolean locaitonStart = false;
    private String filterData;
    //位置型号强度
    private short dist;
    private boolean beepONLocate;
    private boolean beepON = false;
    private Timer locatebeep;
    private Timer tbeep;
    private static ToneGenerator toneGenerator;
    private static Antennas.AntennaRfConfig config;
    private static Events.BatteryData BatteryData = null;
    private int level = 0;
    private int percantageVolume = 100;
    private boolean sledBeeperEnable = true;
    private OperationFailureException operationException;
    //scanner
    private ArrayList<DCSScannerInfo> scannerTreeList = new ArrayList<>();
    private SDKHandler sdkHandler;
    private int scannerId;
    private  ScanAsyncTask cmdExecTask = null;
    private int notifications_mask = 0;
    private boolean keyScanEnable;

    public ZebraUhfServiceImpl() {
        instance = EsimAndroidApp.getInstance();
        if (Build.MODEL.contains("TC20")) {
            readername = "RFD2000";
        } else if(Build.MODEL.contains("MC33")){
            readername = "MC33";
        }else {
            readername = "RFD8500";
        }
        //initRFID();

    }

    @Override
    public boolean initRFID() {
        try {
            Log.d(TAG, "InitSDK");
            beeperSettings();
            if (readers == null) {
                new CreateInstanceTask().execute();
            } else
                new ConnectionTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean closeRFID() {

        Log.d(TAG, "disconnect " + reader);
        try {
            if (readername.equals("RFD8500")) {
                desconnectScanner();
            }
            if (reader != null) {
                reader.Config.saveConfig();
                reader.Events.removeEventsListener(eventHandler);
                reader.disconnect();
                UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_DISCONNECT);
                EventBus.getDefault().post(uhfMsgEvent);
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
                if (!isReaderConnected())
                    return false;
                try {
                    if (!locaitonStart) {
                        reader.Actions.Inventory.perform();
                    } else {
                        reader.Actions.TagLocationing.Perform(filterData, null, null);
                    }
                } catch (InvalidUsageException e) {
                    e.printStackTrace();
                    return false;
                } catch (OperationFailureException e) {
                    e.printStackTrace();
                    return false;
                }
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
        try {
            if(isStart){
                isStart = false;
                if (!isReaderConnected())
                    return false;
                if (!locaitonStart) {
                    reader.Actions.Inventory.stop();
                } else {
                    reader.Actions.TagLocationing.Stop();
                }
                UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_STOP);
                EventBus.getDefault().post(uhfMsgEvent);
                return true;
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
            return false;
        } catch (OperationFailureException e) {
            e.printStackTrace();
            return false;
        }
        return false;
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


    private boolean isReaderConnected() {
        if (reader != null && reader.isConnected())
            return true;
        else {
            Log.d(TAG, "reader is not connected");
            return false;
        }
    }


    @Override
    public int getDownKey() {
        return KeyEvent.KEYCODE_F1;
    }

    // handler for receiving reader appearance events
    @Override
    public void RFIDReaderAppeared(ReaderDevice readerDevice) {
        Log.d(TAG, "RFIDReaderAppeared " + readerDevice.getName());
        new ConnectionTask().execute();
    }

    @Override
    public void RFIDReaderDisappeared(ReaderDevice readerDevice) {
        Log.d(TAG, "RFIDReaderDisappeared " + readerDevice.getName());
        if (readerDevice.getName().equals(reader.getHostName()))
            disconnect();
    }

    private synchronized void disconnect() {
        Log.d(TAG, "disconnect " + reader);
        try {
            if (reader != null) {
                reader.Events.removeEventsListener(eventHandler);
                reader.disconnect();
                UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_DISCONNECT);
                EventBus.getDefault().post(uhfMsgEvent);
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ConnectionTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "ConnectionTask");
            GetAvailableReader();
            if (reader != null)
                return connect();
            return "Failed to find or connect reader";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (operationException != null && operationException.getResults() == RFIDResults.RFID_BATCHMODE_IN_PROGRESS){
                new ReconnectTask().execute();
            }
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_DISMISS_DIALOG);
            EventBus.getDefault().post(uhfMsgEvent);
            if (!isReaderConnected()) {
                EsimAndroidApp.setIEsimUhfService(null);
                UhfMsgEvent<UhfTag> uhfMsgEvent1 = new UhfMsgEvent<>(UhfMsgType.UHF_CONNECT_FAIL);
                EventBus.getDefault().post(uhfMsgEvent1);
            }
        }
    }

    // Enumerates SDK based on host device
    private class CreateInstanceTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "CreateInstanceTask");
            if (readers == null) {
                if (Build.MODEL.contains("TC20") || Build.MODEL.contains("MC33")) {
                    readers = new Readers(instance, ENUM_TRANSPORT.SERVICE_SERIAL);
                } else {
                    readers = new Readers(instance, ENUM_TRANSPORT.BLUETOOTH);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new ConnectionTask().execute();
        }

    }

    private class ReconnectTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                isStart = true;
                stopScanning();
                if (eventHandler == null){
                    eventHandler = new EventHandler();
                }
                reader.PostConnectReaderUpdate();
                reader.Events.addEventsListener(eventHandler);
                // HH event
                reader.Events.setHandheldEvent(true);
                // tag event with tag data
                reader.Events.setTagReadEvent(true);
                reader.Events.setAttachTagDataWithReadEvent(false);
                reader.Events.setReaderDisconnectEvent(true);
                //获取电量配置设置及启动
                reader.Events.setBatteryEvent(true);
                config = reader.Config.Antennas.getAntennaRfConfig(1);
                config.setrfModeTableIndex(0);
                config.setTari(0);
                reader.Config.Antennas.setAntennaRfConfig(1, config);
                if (readername.equals("RFD8500")) {
                    sledBeeperEnable = true;
                    sledBeeperVolume = reader.Config.getBeeperVolume();
                    if (sledBeeperVolume.equals(BEEPER_VOLUME.QUIET_BEEP)) {
                        SettingBeepUtil.setSledOpen(false);
                    } else {
                        SettingBeepUtil.setSledOpen(true);
                    }
                } else {
                    sledBeeperEnable = false;
                }
                setBeeper();
                return true;
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.d(TAG, "null pointer ");
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(!result){
                EsimAndroidApp.setIEsimUhfService(null);
                UhfMsgEvent<UhfTag> uhfMsgEvent1 = new UhfMsgEvent<>(UhfMsgType.UHF_CONNECT_FAIL);
                EventBus.getDefault().post(uhfMsgEvent1);
            }

        }
    }

    private synchronized void GetAvailableReader() {
        Log.d(TAG, "GetAvailableReader");
        try {
            if (readers == null) {
                return;
            }
            readers.attach(this);
            ArrayList<ReaderDevice> readerDevices = readers.GetAvailableRFIDReaderList();
            if (readerDevices != null && readerDevices.size() > 0) {
                for (ReaderDevice device : readerDevices) {
                    if (device.getName().startsWith(readername)) {
                        readerDevice = device;
                        reader = readerDevice.getRFIDReader();
                    }
                }
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        }
    }

    private synchronized String connect() {
        if (reader != null) {
            Log.d(TAG, "connect " + reader.getHostName());
            try {
                if (!reader.isConnected()) {
                    // Establish connection to the RFID Reader
                    reader.connect();
                    ConfigureReader();
                    if (readername.equals("RFD8500")) {
                        initScanner();
                    }
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_CONNECT);
                    EventBus.getDefault().post(uhfMsgEvent);
                    return "Connected";
                }
            } catch (InvalidUsageException e) {
                e.printStackTrace();
                return "InvalidUsageException";
            } catch (OperationFailureException e) {
                e.printStackTrace();
                Log.d(TAG, "OperationFailureException " + e.getVendorMessage());
                operationException = e;
                return "OperationFailureException";
            }
        }
        return "";
    }

    private void ConfigureReader() {
        Log.d(TAG, "ConfigureReader " + reader.getHostName());
        if (reader.isConnected()) {
            TriggerInfo triggerInfo = new TriggerInfo();
            triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
            try {
                // receive events from reader
                if (eventHandler == null)
                    eventHandler = new EventHandler();
                reader.Events.addEventsListener(eventHandler);
                // HH event
                reader.Events.setHandheldEvent(true);
                // tag event with tag data
                reader.Events.setTagReadEvent(true);
                reader.Events.setAttachTagDataWithReadEvent(false);
                reader.Events.setReaderDisconnectEvent(true);
                //获取电量配置设置及启动
                reader.Events.setBatteryEvent(true);
                reader.Config.getDeviceStatus(true, false, false);
                // set trigger mode as rfid so scanner beam will not come
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
                // set start and stop triggers
                reader.Config.setStartTrigger(triggerInfo.StartTrigger);
                reader.Config.setStopTrigger(triggerInfo.StopTrigger);
                reader.Config.setDPOState(DYNAMIC_POWER_OPTIMIZATION.DISABLE);
                reader.Config.setUniqueTagReport(false);
                // power levels are index based so maximum power supported get the last one
                MAX_POWER = reader.ReaderCapabilities.getTransmitPowerLevelValues().length - 1;
                // set antenna configurations
                config = reader.Config.Antennas.getAntennaRfConfig(1);
                config.setrfModeTableIndex(0);
                config.setTari(0);
                reader.Config.Antennas.setAntennaRfConfig(1, config);
                // Set the singulation control
                Antennas.SingulationControl s1_singulationControl = reader.Config.Antennas.getSingulationControl(1);
                s1_singulationControl.setSession(SESSION.SESSION_S0);
                s1_singulationControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);
                s1_singulationControl.Action.setSLFlag(SL_FLAG.SL_ALL);
                reader.Config.Antennas.setSingulationControl(1, s1_singulationControl);
                // delete any prefilters
                reader.Actions.PreFilters.deleteAll();
                beeperSettings();
                if (readername.equals("RFD8500")) {
                    sledBeeperEnable = true;
                    sledBeeperVolume = reader.Config.getBeeperVolume();
                    if (sledBeeperVolume.equals(BEEPER_VOLUME.QUIET_BEEP)) {
                        SettingBeepUtil.setSledOpen(false);
                    } else {
                        SettingBeepUtil.setSledOpen(true);
                    }
                } else {
                    sledBeeperEnable = false;
                }
                setBeeper();
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
            }
        }
    }

    // Read/Status Notify handler
    // Implement the RfidEventsLister class to receive event notifications
    public class EventHandler implements RfidEventsListener {
        // Read Event Notification
        public void eventReadNotify(RfidReadEvents e) {
            // Recommended to use new method getReadTagsEx for better performance in case of large tag population
            TagData[] myTags = reader.Actions.getReadTags(100);
            if (myTags != null) {
                for (int index = 0; index < myTags.length; index++) {
                    Log.d(TAG, "Tag ID " + myTags[index].getTagID());
                    if (myTags[index].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ &&
                            myTags[index].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS) {
                        if (myTags[index].getMemoryBankData().length() > 0) {
                            Log.d(TAG, " Mem Bank Data " + myTags[index].getMemoryBankData());
                        }
                    }
                    if (myTags[index].isContainsLocationInfo()) {
                        dist = myTags[index].LocationInfo.getRelativeDistance();
                        stopbeepingTimer();
                        Log.d(TAG, "Tag relative distance " + dist);
                    }
                }
                // possibly if operation was invoked from async task and still busy
                // handle tag data responses on parallel thread thus THREAD_POOL_EXECUTOR
                new AsyncDataUpdate().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myTags);
            }
        }

        // Status Event Notification
        public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
            Log.d(TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType());
            if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {
                    if(keyScanEnable && readername.equals("RFD8500")){
                        pullTrigger();
                    }else if (isEnable() && !isStart) {
                        startScanning();
                    }
                }
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                    if(keyScanEnable && readername.equals("RFD8500")){
                        releaseTrigger();
                    }else if (isEnable() && isStart) {
                        stopScanning();
                    }
                }
            }
            if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.DISCONNECTION_EVENT) {
                UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_DISCONNECT);
                EventBus.getDefault().post(uhfMsgEvent);
                EsimAndroidApp.setIEsimUhfService(null);
            }
            Log.e("wzmmmm", rfidStatusEvents.StatusEventData.getStatusEventType().toString());
            if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.BATTERY_EVENT) {
                final Events.BatteryData batteryData = rfidStatusEvents.StatusEventData.BatteryData;
                BatteryData = batteryData;
            }
        }
    }

    private void startLocationBeeping(short dist) {
        if (beeperVolume != BEEPER_VOLUME.QUIET_BEEP) {
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
    }

    private void stoplocatebeepingTimer() {
        if (locatebeep != null) {
            toneGenerator.stopTone();
            locatebeep.cancel();
            locatebeep.purge();
        }
        locatebeep = null;
    }

    private void beeperSettings() {
        int streamType = AudioManager.STREAM_DTMF;
        toneGenerator = new ToneGenerator(streamType, percantageVolume);
    }

    private void beep() {
        int toneType = ToneGenerator.TONE_PROP_BEEP;
        toneGenerator.startTone(toneType);
    }

    private class AsyncDataUpdate extends AsyncTask<TagData[], Void, Void> {
        @Override
        protected Void doInBackground(TagData[]... params) {
//            context.handleTagdata(params[0]);
            TagData[] param = params[0];
            //add yhm 20190711 start
            if (param.length > 0) {
                UhfTag utag = new UhfTag(param[0].getTagID());
                utag.setRssi(dist + "");
                UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.INV_TAG, utag);
                EventBus.getDefault().post(uhfMsgEvent);
                if (locaitonStart) {
                    startLocationBeeping(dist);
                } else {
                    startbeepingTimer();
                }
            }
            return null;
        }
    }

    @Override
    public void writeEpcTag(String selectEpc, String epcData) {
        TagAccess tagAccess = new TagAccess();
        final TagAccess.WriteAccessParams writeAccessParams = tagAccess.new WriteAccessParams();
        //设置密码
        writeAccessParams.setAccessPassword(Long.decode("0X" + "00000000"));
        //设置长度
        writeAccessParams.setWriteDataLength(epcData.length() / 4);
        //设置写入区域为ecp
        writeAccessParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
        writeAccessParams.setOffset(2);
        writeAccessParams.setWriteData(epcData);

        new AsyncTask<Void, Void, Boolean>() {
            private InvalidUsageException invalidUsageException;
            private OperationFailureException operationFailureException;
            private Boolean bResult = false;

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    //tagId 选中的epc
                    reader.Actions.TagAccess.writeWait(selectEpc, writeAccessParams, null, null);
                    bResult = true;
                } catch (InvalidUsageException e) {
                    invalidUsageException = e;
                    e.printStackTrace();
                } catch (OperationFailureException e) {
                    operationFailureException = e;
                    e.printStackTrace();
                }
                return bResult;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (!result) {
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_READ_FAIL);
                    EventBus.getDefault().post(uhfMsgEvent);
                } else {
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_WRITE_SUC);
                    EventBus.getDefault().post(uhfMsgEvent);
                }
            }
        }.execute();
    }

    private ScanEnableTask scanEnableTask;

    public void setScanEnable(Boolean enable) {
        scanEnableTask = new ScanEnableTask(false);
        scanEnableTask.execute();
    }

    public class ScanEnableTask extends AsyncTask<Void, Void, Boolean> {

        private Boolean enable;

        ScanEnableTask(Boolean scanenable) {
            enable = scanenable;
        }

        @Override
        protected Boolean doInBackground(Void... a) {
            try {
                if (enable) {
                    //onPause
                    if (reader != null && reader.isConnected())
                        reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.BARCODE_MODE, true);
                } else {
                    //onResume
                    if (reader != null && reader.isConnected())
                        reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
                }
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onCancelled() {
            scanEnableTask = null;
            super.onCancelled();
        }
    }

    //设置功率
    @Override
    public void setPower(int data) {
        new AsyncTask<Void, Void, Boolean>() {
            private OperationFailureException operationFailureException;
            private InvalidUsageException invalidUsageException;

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    config.setTransmitPowerIndex(data);
                    reader.Config.Antennas.setAntennaRfConfig(1, config);//设置功率www

                    return true;
                } catch (InvalidUsageException e) {
                    e.printStackTrace();
                    invalidUsageException = e;
                } catch (OperationFailureException e) {
                    e.printStackTrace();
                    operationFailureException = e;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (!result) {
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_POWER_FAIL);
                    EventBus.getDefault().post(uhfMsgEvent);
                } else {
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_POWER_SUCCESS);
                    EventBus.getDefault().post(uhfMsgEvent);
                }
            }
        }.execute();
    }

    //得到当前设备功率
    @Override
    public int getPower() {
        int[] powerLevels;
        int power = 0;
        if (config != null && reader != null) {
            powerLevels = reader.ReaderCapabilities.getTransmitPowerLevelValues();
            power = powerLevels[config.getTransmitPowerIndex()];
        }
        return power;
    }

    @Override
    public int getBatteryLevel() {
        if (BatteryData != null) {
            level = BatteryData.getLevel();
        }
        return level;
    }

    @Override
    public void setBeeper() {
        boolean hostBeeper = SettingBeepUtil.isHostOpen();
        boolean sledBeeper = SettingBeepUtil.isSledOpen();
        if (readername.equals("RFD2000") || readername.equals("MC33")) {
            hostBeeper = SettingBeepUtil.isOpen();
        }
        boolean finalHostBeeper = hostBeeper;
        new AsyncTask<Void, Void, Boolean>() {
            private OperationFailureException operationFailureException;
            private InvalidUsageException invalidUsageException;

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean result = true;
                if (reader != null) {
                    if (sledBeeperEnable) {
                        if (sledBeeper) {
                            try {
                                reader.Config.setBeeperVolume(BEEPER_VOLUME.HIGH_BEEP);
                                sledBeeperVolume = BEEPER_VOLUME.HIGH_BEEP;
                            } catch (InvalidUsageException e) {
                                e.printStackTrace();
                                result = false;
                                invalidUsageException = e;
                            } catch (OperationFailureException e) {
                                e.printStackTrace();
                                result = false;
                                operationFailureException = e;
                            }
                        } else {
                            try {
                                reader.Config.setBeeperVolume(BEEPER_VOLUME.QUIET_BEEP);
                                sledBeeperVolume = BEEPER_VOLUME.QUIET_BEEP;
                            } catch (InvalidUsageException e) {
                                e.printStackTrace();
                                result = false;
                                invalidUsageException = e;
                            } catch (OperationFailureException e) {
                                e.printStackTrace();
                                result = false;
                                operationFailureException = e;
                            }
                        }
                    }
                    if (finalHostBeeper) {
                        beeperVolume = BEEPER_VOLUME.HIGH_BEEP;
                        percantageVolume = 100;
                    } else {
                        beeperVolume = BEEPER_VOLUME.QUIET_BEEP;
                        percantageVolume = 0;
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (!result) {
                    if (invalidUsageException != null && operationFailureException != null) {
                        UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_SOUND_FAIL);
                        EventBus.getDefault().post(uhfMsgEvent);
                    }
                } else {
                    beeperSettings();
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_SOUND_SUCCESS);
                    EventBus.getDefault().post(uhfMsgEvent);
                }
            }
        }.execute();
    }

    private void startbeepingTimer() {
        if (beeperVolume != BEEPER_VOLUME.QUIET_BEEP) {
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
                    tbeep.schedule(task, 10);
                }
            }
        }
    }

    /**
     * method to stop timer
     */
    private synchronized void stopbeepingTimer() {
        if (tbeep != null) {
            toneGenerator.stopTone();
            tbeep.cancel();
            tbeep.purge();
        }
        tbeep = null;
    }

    private void initScanner() {
        notifications_mask |= DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_BARCODE.value;
        sdkHandler = new SDKHandler(EsimAndroidApp.getInstance());
        DCSSDKDefs.DCSSDK_RESULT result = sdkHandler.dcssdkSetOperationalMode(DCSSDK_OPMODE_BT_NORMAL);
        sdkHandler.dcssdkSubsribeForEvents(notifications_mask);
        sdkHandler.dcssdkGetAvailableScannersList(scannerTreeList);
        sdkHandler.dcssdkSetDelegate(new IDcsSdkApiDelegateImp() {
            @Override
            public void dcssdkEventBarcode(byte[] bytes, int i, int i1) {
                super.dcssdkEventBarcode(bytes, i, i1);
                Log.e("statatat", "data==" + new String(bytes));
                if(bytes != null){
                    UhfTag uhfTag = new UhfTag();
                    uhfTag.setBarcode(new String(bytes));
                    UhfMsgEvent<UhfTag> scanTagUhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SCAN_DATA, uhfTag);
                    EventBus.getDefault().post(scanTagUhfMsgEvent);
                }
            }
        });
        for (DCSScannerInfo dcsScannerInfo : scannerTreeList) {
            if (dcsScannerInfo.getScannerName().startsWith("RFD8500")) {
                scannerId = dcsScannerInfo.getScannerID();
            }
        }
        new ConnectScanner(scannerId).execute();
    }

    private class ConnectScanner extends AsyncTask<Void, Void, Boolean> {
        int scannerId;

        public ConnectScanner(int scannerId) {
            this.scannerId = scannerId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            DCSSDKDefs.DCSSDK_RESULT result = sdkHandler.dcssdkEstablishCommunicationSession(scannerId);
            if(result== DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_SUCCESS){
                return true;
            }
            else if(result== DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_FAILURE){
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.e("Scanner","Scanner Connect result == " + result);
        }
    }

    private class ScanAsyncTask extends AsyncTask<String, Integer, Boolean> {
        int scannerId;
        StringBuilder outXML;
        DCSSDKDefs.DCSSDK_COMMAND_OPCODE opcode;

        public ScanAsyncTask(int scannerId, DCSSDKDefs.DCSSDK_COMMAND_OPCODE opcode, StringBuilder outXML) {
            this.scannerId = scannerId;
            this.opcode = opcode;
            this.outXML = outXML;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return executeCommand(opcode, strings[0], outXML, scannerId);
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
        }

        private boolean executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE opCode, String inXML, StringBuilder outXML, int scannerID) {
            if (sdkHandler != null) {
                if (outXML == null) {
                    outXML = new StringBuilder();
                }
                DCSSDKDefs.DCSSDK_RESULT result = sdkHandler.dcssdkExecuteCommandOpCodeInXMLForScanner(opCode, inXML, outXML, scannerID);
                if (result == DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_SUCCESS)
                    return true;
                else if (result == DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_FAILURE)
                    return false;
            }
            return false;
        }
    }

    private void pullTrigger() {
        String in_xml = "<inArgs><scannerID>" + scannerId + "</scannerID></inArgs>";
        cmdExecTask = new ScanAsyncTask(scannerId, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_PULL_TRIGGER, null);
        cmdExecTask.execute(in_xml);
    }

    private void releaseTrigger() {
        String in_xml = "<inArgs><scannerID>" + scannerId + "</scannerID></inArgs>";
        cmdExecTask = new ScanAsyncTask(scannerId, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_RELEASE_TRIGGER,null);
        cmdExecTask.execute(in_xml);
    }

    private void desconnectScanner() {
        if (sdkHandler != null) {
            sdkHandler.dcssdkTerminateCommunicationSession(scannerId);
        }
    }

    public void setPressScan(Boolean enable){
        keyScanEnable = enable;
    }

    public Boolean isTc20(){
        return readername.equals("RFD2000");
    }
}
