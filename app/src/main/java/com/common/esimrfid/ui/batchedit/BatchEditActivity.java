package com.common.esimrfid.ui.batchedit;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.batchedit.BatchEditContract;
import com.common.esimrfid.core.bean.assetdetail.UpdateAssetsPara;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;
import com.common.esimrfid.presenter.batchedit.BatchEditPresenter;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.UhfMsgEvent;
import com.common.esimrfid.uhf.UhfMsgType;
import com.common.esimrfid.uhf.UhfTag;
import com.common.esimrfid.ui.assetinventory.SingleTreeRecyclerAdapter;
import com.common.esimrfid.ui.assetsearch.CircleAnimation;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.utils.Utils;
import com.multilevel.treelist.Node;
import com.multilevel.treelist.TreeRecyclerAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class BatchEditActivity extends BaseActivity<BatchEditPresenter> implements BatchEditContract.View {

    private static final String TAG = "AssetsSearchActivity";
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.tv_tips)
    TextView mTips;
    @BindView(R.id.search_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.search_ast)
    ImageView search;
    @BindView(R.id.image_scan)
    ImageView image_scan;
    @BindView(R.id.empty_page)
    LinearLayout empty_page;
    @BindView(R.id.scan_num)
    TextView scanNmuber;
    @BindString(R.string.zero_str)
    String stringNum;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private BatchEditAdapter assetsSearchAdapter;
    private List<SearchAssetsInfo> mData = new ArrayList<>();
    private List<SearchAssetsInfo> allAssets = new ArrayList<>();
    private HashMap<String, SearchAssetsInfo> assetsMap = new HashMap<>();
    IEsimUhfService esimUhfService = null;
    private CircleAnimation animation;
    private Boolean canRfid = true;
    private Boolean showScanAssets = false;
    private Boolean isSearch = true;
    private MaterialDialog multipleDialog;
    private TextView mulTipleTvTitle;
    private RecyclerView multiRecycle;
    protected List<Node> multiDatas = new ArrayList<>();
    private TreeRecyclerAdapter multiAdapter;
    private View multiContentView;
    private List<Node> checkedLocations = new ArrayList<>();
    List<AssetsLocation> mAssetsLocations = new ArrayList<>();

    @Override
    public BatchEditPresenter initPresenter() {
        return new BatchEditPresenter();
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.batch_edit);
        mTips.setText(R.string.change_tip);
        initRfidAndEvent();
        assetsSearchAdapter = new BatchEditAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(assetsSearchAdapter);
        empty_page.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        initMultiDialogView();
        rotateAnim();
        scanNmuber.setText("查找到资产数量0个");
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);//使上拉加载具有弹性效果
        mRefreshLayout.setEnableOverScrollDrag(false);//禁止越界拖动（1.0.4以上版本）
        mRefreshLayout.setEnableOverScrollBounce(false);//关闭越界回弹功能
        mRefreshLayout.setEnableAutoLoadMore(false);
        //mPresenter.fetchLatestPageAssets(500, 1);
        mPresenter.getAllAssetsForSearch();
        mPresenter.getAllAssetsLocation();

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
            esimUhfService.setEnable(true);
        }
    }

    private void rotateAnim() {
        int radii = (int) (20 * getResources().getDisplayMetrics().density);
        animation = new CircleAnimation(radii);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);//设置动画重复次数 无限循环
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatMode(Animation.RESTART);

    }

    private void initRfidAndEvent() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_batch_edit;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.title_back, R.id.search_ast, R.id.btn_batch_edit})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.search_ast:
                if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                    esimUhfService.startStopScanning();
                } else {
                    ToastUtils.showShort(R.string.not_connect_prompt);
                }
                break;
            case R.id.btn_batch_edit:
                if (CommonUtils.isNetworkConnected()) {
                    if (mAssetsLocations.size() == 0) {
                        mPresenter.getAllAssetsLocation();
                    } else {
                        showMultipleDialog();
                    }
                } else {
                    ToastUtils.showShort("无法连接网络，修改失败");
                }

                break;
        }
    }


    @Override
    public void handGetAllAssetsForSearch(List<SearchAssetsInfo> searchInfos) {
        allAssets.clear();
        allAssets.addAll(searchInfos);
        for (SearchAssetsInfo allAsset : allAssets) {
            assetsMap.put(allAsset.getAst_epc_code(), allAsset);
        }
    }

    @Override
    public void handleAllAssetsLocation(List<AssetsLocation> assetsLocations) {
        mAssetsLocations.clear();
        mAssetsLocations.addAll(assetsLocations);
    }

    @Override
    public void handelUpdateAssetLoc(BaseResponse resultResponse) {
        if ("200000".equals(resultResponse.getCode())) {
            List<SearchAssetsInfo> searchAssetsInfos = assetsSearchAdapter.getmSelectedData();
            if (searchAssetsInfos.size() > 0 && checkedLocations.size() > 0) {
                String locName = (String)checkedLocations.get(0).getName();
                for (SearchAssetsInfo searchAssetsInfo : searchAssetsInfos) {
                    searchAssetsInfo.setLoc_name(locName);
                }
                assetsSearchAdapter.notifyDataSetChanged();
            }
            ToastUtils.showShort("批量修改存放位置成功");
        } else {
            ToastUtils.showShort("批量修改存放位置失败");
        }
    }


    private void handleResultList(List<SearchAssetsInfo> mData) {
        if (mData.size() > 0) {
            empty_page.setVisibility(View.GONE);
            mTips.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            scanNmuber.setVisibility(View.VISIBLE);
        } else {
            empty_page.setVisibility(View.VISIBLE);
            mTips.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            scanNmuber.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUhfMsg(UhfMsgEvent<UhfTag> uhfMsgEvent) {
        Log.d(TAG, "handleEPC: " + uhfMsgEvent.toString());
        String epc = null;
        switch (uhfMsgEvent.getType()) {
            case UhfMsgType.INV_TAG:
                UhfTag uhfTag = (UhfTag) uhfMsgEvent.getData();
                epc = uhfTag.getEpc();
                Log.e(TAG, "epc=====: " + epc);
                handleEpc(epc);
                break;
            case UhfMsgType.UHF_CONNECT:
                break;
            case UhfMsgType.UHF_DISCONNECT:
                image_scan.clearAnimation();
                search.setImageResource(R.drawable.search_nearby_assets);
                break;
            case UhfMsgType.UHF_START:
                image_scan.startAnimation(animation);
                search.setImageResource(R.drawable.stop_search);
                if (isSearch) {
                    mData.clear();
                    assetsSearchAdapter.notifyDataSetChanged();
                    String formatNum = String.format(stringNum, 0);
                    scanNmuber.setText(formatNum);
                    isSearch = false;
                }
                break;
            case UhfMsgType.UHF_STOP:
                image_scan.clearAnimation();
                search.setImageResource(R.drawable.search_nearby_assets);
                break;
        }
    }

    private void handleEpc(String epc) {
        SearchAssetsInfo searchAssetsInfo = assetsMap.get(epc);
        if (searchAssetsInfo != null && !mData.contains(searchAssetsInfo)) {
            mData.add(searchAssetsInfo);
            if (!showScanAssets) {
                handleResultList(mData);
                showScanAssets = true;
            }
            String formatNum = String.format(stringNum, mData.size());
            scanNmuber.setText(formatNum);
            assetsSearchAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null && esimUhfService.isStart()) {
            esimUhfService.stopScanning();
            image_scan.clearAnimation();
            search.setImageResource(R.drawable.search_nearby_assets);
        }
        if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
            esimUhfService.setEnable(false);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (canRfid) {
            if (esimUhfService != null && EsimAndroidApp.getIEsimUhfService() != null) {
                if (keyCode == esimUhfService.getDownKey()) { //扳机建扫描
                    esimUhfService.startStopScanning();
                }
            } else if (keyCode == Utils.getDiffDownKey()) {
                ToastUtils.showShort(R.string.not_connect_prompt);
            }
            canRfid = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        canRfid = true;
        return super.onKeyUp(keyCode, event);
    }

    public void showMultipleDialog() {
        updateMultiAdapterData();
        if (multipleDialog != null) {
            multipleDialog.show();
        } else {
            multipleDialog = new MaterialDialog.Builder(this)
                    .customView(multiContentView, false)
                    .show();
            Window dialogWindow = multipleDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
            //设置dialog宽度沾满全屏
            Window window = multipleDialog.getWindow();
            // 把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            // 设置宽度
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
    }

    public void updateMultiAdapterData() {
        multiAdapter.removeData(multiDatas);
        multiDatas.clear();
        for (AssetsLocation mAssetsLoc : mAssetsLocations) {
            String pId = StringUtils.isEmpty(mAssetsLoc.getLoc_superid()) ? "-1" : mAssetsLoc.getLoc_superid();
            multiDatas.add(new Node(mAssetsLoc.getId(), pId, mAssetsLoc.getLoc_name()));
        }
        multiAdapter.addData(multiDatas);
    }

    public void initMultiDialogView() {
        multiContentView = LayoutInflater.from(this).inflate(R.layout.multiple_choice_dialog, null);
        TextView tvSubmit = (TextView) multiContentView.findViewById(R.id.tv_finish);
        tvSubmit.setText(R.string.edit_loc);
        TextView tvCancel = (TextView) multiContentView.findViewById(R.id.tv_cancle);
        multiRecycle = (RecyclerView) multiContentView.findViewById(R.id.multi_recycle);
        multiRecycle.setLayoutManager(new LinearLayoutManager(this));
        multiAdapter = new SingleTreeRecyclerAdapter(multiRecycle, BatchEditActivity.this,
                multiDatas, 2, R.drawable.tree_ex, R.drawable.tree_ec);
        multiRecycle.setAdapter(multiAdapter);
        mulTipleTvTitle = (TextView) multiContentView.findViewById(R.id.tv_title);
        mulTipleTvTitle.setText(R.string.inv_location);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Node> allNodes = multiAdapter.getAllNodes();
                checkedLocations.clear();
                for (Node node : allNodes) {
                    if (node.isChecked()) {
                        checkedLocations.add(node);
                    }
                }
                List<SearchAssetsInfo> searchAssetsInfos = assetsSearchAdapter.getmSelectedData();
                if (searchAssetsInfos.size() == 0) {
                    ToastUtils.showShort("请选择需要修改位置的资产");
                    return;
                }
                if (checkedLocations.size() == 0) {
                    ToastUtils.showShort("请选择修改存放位置");
                    return;
                }
                UpdateAssetsPara updateAssetsPara = new UpdateAssetsPara();
                ArrayList<String> astIds = new ArrayList<>();
                for (SearchAssetsInfo searchAssetsInfo : searchAssetsInfos) {
                    astIds.add(searchAssetsInfo.getId());
                }
                updateAssetsPara.setAstIds(astIds);
                updateAssetsPara.setLoc_id((String) checkedLocations.get(0).getId());
                mPresenter.updateAssetProp(updateAssetsPara);
                if (multipleDialog != null) {
                    multipleDialog.dismiss();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (multipleDialog != null) {
                    multipleDialog.dismiss();
                }
            }
        });
    }
}
