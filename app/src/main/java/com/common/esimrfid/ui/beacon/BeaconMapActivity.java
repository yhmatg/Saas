package com.common.esimrfid.ui.beacon;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.common.esimrfid.R;
import com.common.esimrfid.app.Constants;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.ty.ibeacon.Beacon;
import com.ty.ibeacon.BeaconManager;
import com.ty.ibeacon.BeaconRegion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

public class BeaconMapActivity extends BaseActivity implements SensorEventListener {
    public static final String TAG = "BeaconMapActivity";
    public static final int REQUEST_ENABLE_BT = 1234;
    @BindView(R.id.wv_loc)
    WebView webView;
    private BeaconManager beaconManager;
    private BeaconRegion beaconRegion = new BeaconRegion("markID", "40D6F323-6732-4C7C-A438-C9A740D0BF15", null, null);
    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    private String mPoild;
    @Override
    public AbstractPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData() {
        if (getIntent() != null) {
            mPoild = getIntent().getStringExtra(Constants.POIL_D);
        }
        initRealWebView();
        initBeacon();
        //传感器，获取设备方向
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_beacon_map;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(beaconRegion);
                } catch (RemoteException e) {
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String script = "hideLocation()";
        webView.loadUrl("javascript:" + script);
        webView.onPause();
        try {
            beaconManager.stopRanging(beaconRegion);
            beaconManager.disconnect();
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading,
                    0, accelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading,
                    0, magnetometerReading.length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void initBeacon() {
        // 创建蓝牙管理实例
        beaconManager = new BeaconManager(this);

//        监听蓝牙扫描回调，在此回调中收集数据后调用上传信号接口
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> list) {
                Log.e(TAG,"========" + list.size());
                try {
                    String s = beaconsToJson(list);
                    Log.i(TAG, s);
                    String script = "scan(" + s + ")";
                    webView.loadUrl("javascript:" + script);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        checkBluetooth();
    }

    String beaconsToJson(List<Beacon> beaconList) throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray beaconArray = new JSONArray();
        for (int i = 0; i < beaconList.size(); ++i) {
            Beacon b = beaconList.get(i);
            JSONObject beaconJson = new JSONObject();
            beaconJson.put("uuid", b.getProximityUUID());
            beaconJson.put("major", b.getMajor());
            beaconJson.put("minor", b.getMinor());
            beaconJson.put("minor", b.getMinor());
            beaconJson.put("accuracy", b.getAccuracy());
            beaconArray.put(beaconJson);
        }
        json.put("ble", beaconArray);

        updateOrientationAngles();
        double heading = Math.toDegrees(orientationAngles[0]);
        heading = heading < 0 ? heading + 360 : heading;
        heading = heading > 360 ? heading - 360 : heading;
        Log.i(TAG, heading + "");
        json.put("heading", heading);
        return json.toString();
    }

    public void updateOrientationAngles() {
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);

    }
    private void checkBluetooth() {
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG,"手机不支持蓝牙");
            return;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
//判断是否需要 向用户解释，为什么要申请该权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
            }
        }

        if (!beaconManager.isBluetoothEnabled()) {
            Log.e(TAG,"蓝牙未开启");
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Log.e(TAG,"蓝牙已开启");
        }
    }

    private void initRealWebView() {
        final String url = "https://www.platalk.cn/HG/home/07552001?poiId=" + mPoild;
        WebSettings websettings = webView.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setDomStorageEnabled(true);  // 开启 DOM storage 功能
        webView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i(TAG, "webview.2");
                Log.i(TAG, webView.getWidth() + ", " + webView.getHeight());
                Log.i(TAG, webView.toString());
                if (webView != null) {
                    webView.loadUrl(url);
                    webView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }
}
