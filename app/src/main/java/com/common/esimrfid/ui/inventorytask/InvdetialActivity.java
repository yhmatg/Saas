package com.common.esimrfid.ui.inventorytask;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvDetailContract;
import com.common.esimrfid.core.bean.emun.InventoryStatus;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.customview.CustomPopWindow;
import com.common.esimrfid.presenter.home.InvDetailPresenter;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.StringUtils;
import com.ty.ibeacon.Beacon;
import com.ty.ibeacon.BeaconManager;
import com.ty.ibeacon.BeaconRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class InvdetialActivity extends BaseActivity<InvDetailPresenter> implements InvDetailContract.View, FiltterAdapter.OnItemClickListener {
    public static final String TAG = "InvdetialActivity";
    public static final String INV_ID = "inv_id";
    public static final String INV_NAME = "inv_name";
    private static final String INV_STATUS = "inv_status";
    private static final String INTENT_FROM = "intent_from";
    public static final int REQUEST_ENABLE_BT = 1234;
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.tv_area)
    TextView mTvArea;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.im_area_arrow)
    ImageView mAreaArrow;
    @BindView(R.id.im_status_arrow)
    ImageView mStatusArrow;
    @BindView(R.id.rv_inventory_detail)
    RecyclerView mInvDetailRecyclerView;
    @BindView(R.id.empty_page)
    LinearLayout empty_layout;
    @BindView(R.id.filter_layout)
    LinearLayout filterLayout;
    @BindView(R.id.view_mask)
    View maskView;
    @BindView(R.id.wv_loc)
    WebView webView;
    private String userId;
    //所有条目数据
    List<InventoryDetail> mInventoryDetails = new ArrayList<>();
    List<FilterBean> mAreaBeans = new ArrayList<>();
    List<FilterBean> mStatusBeans = new ArrayList<>();
    List<FilterBean> currentFilterBeans = new ArrayList<>();
    CustomPopWindow mCustomPopWindow;
    private FiltterAdapter filtterAdapter;
    private RecyclerView filerRecycler;
    //位置和对应的资产
    HashMap<String, ArrayList<InventoryDetail>> locationMap = new HashMap<>();
    List<InvLocationBean> mLoctionBeans = new ArrayList<>();
    List<InvLocationBean> mCurrentLoctionBeans = new ArrayList<>();
    private InvLocationAdapter mLoctionAdapter;
    private String mInvId;
    private Boolean isFirstOnResume = true;
    private FilterBean currentFilterBean = new FilterBean("10000", "全部", false);
    private List<InventoryDetail> mergeResults = new ArrayList<>();
    private HashMap<String, InventoryDetail> mergeAssets = new HashMap<>();
    private BeaconManager beaconManager;
    private BeaconRegion beaconRegion = new BeaconRegion("markID", "40D6F323-6732-4C7C-A438-C9A740D0BF15", null, null);
    @Override
    public InvDetailPresenter initPresenter() {
        return new InvDetailPresenter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.inv_area);
        userId = getUserLoginResponse().getUserinfo().getId();
        if (getIntent() != null) {
            mInvId = getIntent().getStringExtra(INV_ID);
        }
        inStatusList();
        initView();
        if (CommonUtils.isNetworkConnected()) {
            mPresenter.uploadLocalInvDetailState(mInvId, userId);
        }
        initWebView();
        initBeacon();

    }

    private void initBeacon() {
        // 创建蓝牙管理实例
        beaconManager = new BeaconManager(this);

//        监听蓝牙扫描回调，在此回调中收集数据后调用上传信号接口
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> list) {
                Log.e(TAG,"========" + list.size());
            }
        });

        checkBluetooth();
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

    private void initWebView() {
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webView.loadUrl("http://www.baidu.com");
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
    }

    private void inStatusList() {
        mStatusBeans.add(new FilterBean("10000", "全部", false));
        mStatusBeans.add(new FilterBean("10001", "已完成", false));
        mStatusBeans.add(new FilterBean("10002", "未完成", false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        if (isFirstOnResume) {
            mPresenter.fetchAllInvDetails(mInvId, true);
            isFirstOnResume = false;
        } else {
            mPresenter.fetchAllInvDetails(mInvId, false);
        }
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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inventory_detail;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public void handleInvDetails(ResultInventoryDetail mInventoryDetail) {
        List<InventoryDetail> detailResults = mInventoryDetail.getDetailResults();
        mergeResults.clear();
        mergeAssets.clear();
        mInventoryDetails.clear();
        mLoctionBeans.clear();
        mCurrentLoctionBeans.clear();
        mAreaBeans.clear();
        locationMap.clear();
        mAreaBeans.add(new FilterBean("10000", "全部", false));
        mInventoryDetails.addAll(detailResults);
        for (InventoryDetail inventoryDetail : mInventoryDetails) {
            //资产盘盈位置
            String pluLocName = inventoryDetail.getInvdt_plus_loc_name() == null ? "未分配" : inventoryDetail.getInvdt_plus_loc_name();
            //资产盘点状态
            int code = inventoryDetail.getInvdt_status().getCode();
            if (code == 10 && !StringUtils.isEmpty(inventoryDetail.getInvdt_plus_loc_name())) {
                inventoryDetail.setLoc_id(inventoryDetail.getInvdt_plus_loc_id());
                inventoryDetail.setLoc_name(inventoryDetail.getInvdt_plus_loc_name());
                mergeResults.add(inventoryDetail);
                mergeAssets.put(inventoryDetail.getAst_id(), inventoryDetail);
            }
            //资产位置
            String locName = inventoryDetail.getLoc_name() == null ? "未分配" : inventoryDetail.getLoc_name();
            //资产按地点分类  未完成还要考虑盘盈的资产
            if (code != 2) {
                if (!locationMap.containsKey(locName)) {
                    ArrayList<InventoryDetail> details = new ArrayList<>();
                    details.add(inventoryDetail);
                    locationMap.put(locName, details);
                } else {
                    ArrayList<InventoryDetail> inventoryDetails = locationMap.get(locName);
                    inventoryDetails.add(inventoryDetail);
                }
            } else if (!"未分配".equals(pluLocName)) {
                if (!locationMap.containsKey(pluLocName)) {
                    ArrayList<InventoryDetail> details = new ArrayList<>();
                    details.add(inventoryDetail);
                    locationMap.put(pluLocName, details);
                } else {
                    ArrayList<InventoryDetail> inventoryDetails = locationMap.get(pluLocName);
                    inventoryDetails.add(inventoryDetail);
                }
            }
        }
        Set<Map.Entry<String, ArrayList<InventoryDetail>>> entries = locationMap.entrySet();
        for (Map.Entry<String, ArrayList<InventoryDetail>> entry : entries) {
            InvLocationBean invLocationBean = new InvLocationBean();
            invLocationBean.setInvId(mInvId);
            invLocationBean.setLocNmme(entry.getKey());
            ArrayList<InventoryDetail> invdetails = entry.getValue();
            invLocationBean.setmInventoryDetails(invdetails);
            int notInvNum = 0;
            int invNum = 0;
            int moreInvNum = 0;
            int lessInvNum = 0;
            for (InventoryDetail invdetail : invdetails) {
                if (invdetail.getInvdt_status().getCode() == InventoryStatus.INIT.getIndex()) {
                    notInvNum++;
                    if (StringUtils.isEmpty(invLocationBean.getLocId())) {
                        invLocationBean.setLocId(invdetail.getLoc_id());
                    }
                } else if (invdetail.getInvdt_status().getCode() == InventoryStatus.FINISH.getIndex()) {
                    invNum++;
                    if (StringUtils.isEmpty(invLocationBean.getLocId())) {
                        invLocationBean.setLocId(invdetail.getLoc_id());
                    }
                } else if (invdetail.getInvdt_status().getCode() == InventoryStatus.MORE.getIndex()) {
                    moreInvNum++;
                    if (mergeAssets.containsKey(invdetail.getAst_id())) {
                        InventoryDetail tempInventoryDetail = mergeAssets.get(invdetail.getAst_id());
                        if (tempInventoryDetail != null) {
                            invdetail.setLoc_id(tempInventoryDetail.getInvdt_plus_loc_id());
                            invdetail.setLoc_name(tempInventoryDetail.getInvdt_plus_loc_name());
                            mergeResults.add(invdetail);
                        }
                    }
                } else if (invdetail.getInvdt_status().getCode() == InventoryStatus.LESS.getIndex()) {
                    lessInvNum++;
                    if (StringUtils.isEmpty(invLocationBean.getLocId())) {
                        invLocationBean.setLocId(invdetail.getLoc_id());
                    }
                }
            }
            invLocationBean.setAllNum(invdetails.size() - moreInvNum);
            invLocationBean.setNotInvNum(notInvNum);
            invLocationBean.setInvNum(invNum);
            invLocationBean.setMoreInvNum(moreInvNum);
            invLocationBean.setLessInvNum(lessInvNum);
            mLoctionBeans.add(invLocationBean);
            FilterBean filterBean = new FilterBean();
            filterBean.setSelected(false);
            filterBean.setId(invLocationBean.getLocId());
            filterBean.setName(invLocationBean.getLocNmme());
            mAreaBeans.add(filterBean);
        }
        DbBank.getInstance().getInventoryDetailDao().updateItems(mergeResults);
        showFilterData(currentFilterBean);
        if (mInventoryDetails.size() > 0) {
            mInvDetailRecyclerView.setVisibility(View.VISIBLE);
            empty_layout.setVisibility(View.GONE);
        } else {
            mInvDetailRecyclerView.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
        }
    }

    //资产列表排序，未盘点在前
    public void sortListByInvStatus(List<InvLocationBean> invLocationBean) {
        Collections.sort(invLocationBean, new Comparator<InvLocationBean>() {
            @Override
            public int compare(InvLocationBean t1, InvLocationBean t2) {
                return t2.getNotInvNum() - t1.getNotInvNum();
            }
        });
    }

    //上传盘点资产回调
    @Override
    public void handelUploadResult(BaseResponse baseResponse) {


    }

    @Override
    public void handelFinishInvorder(BaseResponse baseResponse) {

    }

    @Override
    public void uploadInvDetails(List<InventoryDetail> inventoryDetails) {

    }

    @OnClick({R.id.title_back, R.id.area_layout, R.id.status_layout})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.area_layout:
                currentFilterBeans.clear();
                currentFilterBeans.addAll(mAreaBeans);
                filtterAdapter.notifyDataSetChanged();
                mCustomPopWindow.showAsDropDown(filterLayout);
                maskView.setVisibility(View.VISIBLE);
                mTvArea.setTextColor(getColor(R.color.titele_color));
                mAreaArrow.setImageResource(R.drawable.down_arrow_blue);
                break;
            case R.id.status_layout:
                currentFilterBeans.clear();
                currentFilterBeans.addAll(mStatusBeans);
                filtterAdapter.notifyDataSetChanged();
                mCustomPopWindow.showAsDropDown(filterLayout);
                maskView.setVisibility(View.VISIBLE);
                mTvStatus.setTextColor(getColor(R.color.titele_color));
                mStatusArrow.setImageResource(R.drawable.down_arrow_blue);
                break;
        }
    }

    public void initView() {
        mLoctionAdapter = new InvLocationAdapter(mCurrentLoctionBeans, this);
        mInvDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mInvDetailRecyclerView.setAdapter(mLoctionAdapter);
        //初始化popupwindow
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_filter_layout, null);
        filtterAdapter = new FiltterAdapter(currentFilterBeans, this);
        filtterAdapter.setOnItemClickListener(this);
        filerRecycler = contentView.findViewById(R.id.rv_filter);
        filerRecycler.setLayoutManager(new LinearLayoutManager(this));
        filerRecycler.setAdapter(filtterAdapter);
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(false)
                .setAnimationStyle(R.style.popwin_anim)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        maskView.setVisibility(View.GONE);
                        mTvArea.setTextColor(getColor(R.color.repair_text));
                        mAreaArrow.setImageResource(R.drawable.down_arrow_black);
                        mTvStatus.setTextColor(getColor(R.color.repair_text));
                        mStatusArrow.setImageResource(R.drawable.down_arrow_black);
                    }
                })
                .create();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        try {
            beaconManager.stopRanging(beaconRegion);
            beaconManager.disconnect();
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }
    }

    @Override
    public void onItemSelected(FilterBean filterBean) {
        currentFilterBean = filterBean;
        showFilterData(currentFilterBean);
        mCustomPopWindow.dissmiss();
    }

    public void showFilterData(FilterBean filterBean) {
        mCurrentLoctionBeans.clear();
        if ("10000".equals(filterBean.getId())) {
            mCurrentLoctionBeans.addAll(mLoctionBeans);
        } else if ("10001".equals(filterBean.getId())) {
            for (InvLocationBean mLoctionBean : mLoctionBeans) {
                if (mLoctionBean.getNotInvNum() == 0) {
                    mCurrentLoctionBeans.add(mLoctionBean);
                }
            }
        } else if ("10002".equals(filterBean.getId())) {
            for (InvLocationBean mLoctionBean : mLoctionBeans) {
                if (mLoctionBean.getNotInvNum() != 0) {
                    mCurrentLoctionBeans.add(mLoctionBean);
                }
            }
        } else {
            for (InvLocationBean mLoctionBean : mLoctionBeans) {
                if (filterBean.getId().equals(mLoctionBean.getLocId())) {
                    mCurrentLoctionBeans.add(mLoctionBean);
                }
            }
        }
        sortListByInvStatus(mCurrentLoctionBeans);
        mLoctionAdapter.notifyDataSetChanged();
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
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
}
