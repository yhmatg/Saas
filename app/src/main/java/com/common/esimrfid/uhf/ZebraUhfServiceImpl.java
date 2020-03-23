package com.common.esimrfid.uhf;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;

import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.utils.StringUtils;
import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.DYNAMIC_POWER_OPTIMIZATION;
import com.zebra.rfid.api3.ENUM_TRANSPORT;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE;
import com.zebra.rfid.api3.INVENTORY_STATE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.MEMORY_BANK;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDReader;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * @author rylai
 * created at 2019/5/31 10:54
 */
public class ZebraUhfServiceImpl extends EsimUhfAbstractService implements Readers.RFIDReaderEventHandler {
    final static String TAG = "RFID_SAMPLE";
    // RFID Reader
    private static Readers readers;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    private static RFIDReader reader;
    private EventHandler eventHandler;
    private int MAX_POWER = 270;
    String readername = "RFD8500";
    Application instance;
    boolean locaitonStart = false;
    String filterData;
    //位置型号强度
    private short dist;

    public ZebraUhfServiceImpl() {
        instance = EsimAndroidApp.getInstance();
        if(Build.MODEL.contains("TC20")){
            readername = "RFD2000";
        }else {
            readername = "RFD8500";
        }
        //initRFID();

    }
    @Override
    public boolean initRFID() {
        try {
            Log.d(TAG, "InitSDK");
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
            if (reader != null) {
                reader.Events.removeEventsListener(eventHandler);
                reader.disconnect();
                UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_DISCONNECT);
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
                    if(!locaitonStart){
                        reader.Actions.Inventory.perform();
                    }else {
                        reader.Actions.TagLocationing.Perform(filterData, null, null);
                    }
                } catch (InvalidUsageException e) {
                    e.printStackTrace();
                    return false;
                } catch (OperationFailureException e) {
                    e.printStackTrace();
                    return false;
                }
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

        if (!isReaderConnected())
            return false;
        try {
            if(!locaitonStart){
                reader.Actions.Inventory.stop();
            }else {
                reader.Actions.TagLocationing.Stop();
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
            return false;
        } catch (OperationFailureException e) {
            e.printStackTrace();
            return false;
        }

        UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_STOP);
        EventBus.getDefault().post(uhfMsgEvent);

        return true;
    }

    @Override
    public int setFilterData(int area, int start, int length, String data, boolean isSave) {
        if(StringUtils.isEmpty(data)){
           locaitonStart = false;
            filterData = "";
        }else {
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
                UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_DISCONNECT);
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
            if(!isReaderConnected()){
                EsimAndroidApp.setIEsimUhfService(null);
            }
        }
    }
    // Enumerates SDK based on host device
    private class CreateInstanceTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "CreateInstanceTask");
            if (readers == null) {
                if(Build.MODEL.contains("TC20")){
                    readers = new Readers(instance, ENUM_TRANSPORT.SERVICE_SERIAL);
                }else {
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
    private synchronized void GetAvailableReader() {
        Log.d(TAG, "GetAvailableReader");
        try {
            if (readers == null) {
                return;
            }
            readers.attach(this);
            ArrayList<ReaderDevice> readerDevices = readers.GetAvailableRFIDReaderList();
            if (readerDevices != null && readerDevices.size()>0) {
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
                    UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_CONNECT);
                    EventBus.getDefault().post(uhfMsgEvent);
                    return "Connected";
                }
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
                Log.d(TAG, "OperationFailureException " + e.getVendorMessage());
                String des = e.getResults().toString();
                return "Connection failed" + e.getVendorMessage() + " " + des;
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
                // set trigger mode as rfid so scanner beam will not come
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
                // set start and stop triggers
                reader.Config.setStartTrigger(triggerInfo.StartTrigger);
                reader.Config.setStopTrigger(triggerInfo.StopTrigger);
                //modify 0220 start
                reader.Config.setDPOState(DYNAMIC_POWER_OPTIMIZATION.DISABLE);
                reader.Config.setUniqueTagReport(false);
                //modify 0220 end
                // power levels are index based so maximum power supported get the last one
                MAX_POWER = reader.ReaderCapabilities.getTransmitPowerLevelValues().length - 1;
                // set antenna configurations
                Antennas.AntennaRfConfig config = reader.Config.Antennas.getAntennaRfConfig(1);
                config.setTransmitPowerIndex(MAX_POWER);
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
                //
            } catch (InvalidUsageException | OperationFailureException e) {
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
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
//                            context.handleTriggerPress(true);
                            if(isEnable()){
                                startScanning();
                            }
                            return null;
                        }
                    }.execute();
                }
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
//                            context.handleTriggerPress(false);
                            if (isEnable()){
                                stopScanning();
                            }
                            return null;
                        }
                    }.execute();
                }
            }
            if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.DISCONNECTION_EVENT) {
                UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_DISCONNECT);
                EventBus.getDefault().post(uhfMsgEvent);
                EsimAndroidApp.setIEsimUhfService(null);
            }
        }
    }
    private class AsyncDataUpdate extends AsyncTask<TagData[], Void, Void> {
        @Override
        protected Void doInBackground(TagData[]... params) {
//            context.handleTagdata(params[0]);
            TagData[] param = params[0];
            //add yhm 20190711 start
            if(param.length > 0){
                UhfTag utag=new UhfTag(param[0].getTagID());
                utag.setRssi(dist + "");
                UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.INV_TAG,utag);
                EventBus.getDefault().post(uhfMsgEvent);
            }
            //add yhm 20190711 end
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
                    UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_READ_FAIL);
                    EventBus.getDefault().post(uhfMsgEvent);
                } else {
                    UhfMsgEvent<UhfTag> uhfMsgEvent=new UhfMsgEvent<>(UhfMsgType.UHF_WRITE_SUC);
                    EventBus.getDefault().post(uhfMsgEvent);
                }
            }
        }.execute();
    }
    private ScanEnableTask scanEnableTask;
    public void setScanEnable(){
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
                    if ( reader!= null && reader.isConnected())
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
}
