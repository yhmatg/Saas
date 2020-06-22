package com.common.esimrfid.ui.inventorytask;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.InvDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.emun.InventoryStatus;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.customview.CustomPopWindow;
import com.common.esimrfid.customview.MaxHeightRecyclerView;
import com.common.esimrfid.presenter.home.InvDetailPresenter;
import com.common.esimrfid.ui.home.AssetLocationNum;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.DateUtils;
import com.common.esimrfid.utils.StringUtils;

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
    public static final String INV_ID = "inv_id";
    public static final String INV_NAME = "inv_name";
    private static final String INV_STATUS = "inv_status";
    private static final String INTENT_FROM = "intent_from";
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
    private FilterBean currentFilterBean = new FilterBean("10000","全部",false);

    @Override
    public InvDetailPresenter initPresenter() {
        return new InvDetailPresenter(DataManager.getInstance());
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
        if (CommonUtils.isNetworkConnected() ) {
            mPresenter.uploadLocalInvDetailState(mInvId, userId);
        }
    }

    private void inStatusList() {
        mStatusBeans.add(new FilterBean("10000","全部",false));
        mStatusBeans.add(new FilterBean("10001","已完成",false));
        mStatusBeans.add(new FilterBean("10002","未完成",false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstOnResume) {
            mPresenter.fetchAllInvDetails(mInvId, true);
            isFirstOnResume =false;
        } else {
            mPresenter.fetchAllInvDetails(mInvId, false);
        }

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
        mInventoryDetails.clear();
        mLoctionBeans.clear();
        mCurrentLoctionBeans.clear();
        mAreaBeans.clear();
        locationMap.clear();
        mAreaBeans.add(new FilterBean("10000","全部",false));
        mInventoryDetails.addAll(detailResults);
        for (InventoryDetail inventoryDetail : mInventoryDetails) {
            //资产未知
            String locName = inventoryDetail.getAssetsInfos().getLoc_info() == null ? "未分配" : inventoryDetail.getAssetsInfos().getLoc_info().getLoc_name();
            //资产盘盈位置
            String pluLocName = inventoryDetail.getAssetsInfos().getInvdt_plus_loc_info() == null ? "未分配" : inventoryDetail.getAssetsInfos().getInvdt_plus_loc_info().getLoc_name();
            //资产盘点状态
            Integer code = inventoryDetail.getInvdt_status().getCode();
            //资产按地点分类  未完成还要考虑盘盈的资产
            if(code != 2){
                if ( !locationMap.containsKey(locName)) {
                    ArrayList<InventoryDetail> details = new ArrayList<>();
                    details.add(inventoryDetail);
                    locationMap.put(locName, details);
                } else {
                    ArrayList<InventoryDetail> inventoryDetails = locationMap.get(locName);
                    inventoryDetails.add(inventoryDetail);
                }
            }else if(!"未分配".equals(pluLocName)){
                if ( !locationMap.containsKey(pluLocName)) {
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
                    if(StringUtils.isEmpty(invLocationBean.getLocId())){
                        invLocationBean.setLocId(invdetail.getAssetsInfos().getLoc_id());
                    }
                }else if (invdetail.getInvdt_status().getCode() == InventoryStatus.FINISH.getIndex()) {
                    invNum++;
                    if(StringUtils.isEmpty(invLocationBean.getLocId())){
                        invLocationBean.setLocId(invdetail.getAssetsInfos().getLoc_id());
                    }
                }else if (invdetail.getInvdt_status().getCode() == InventoryStatus.MORE.getIndex()) {
                    moreInvNum++;
                }else if (invdetail.getInvdt_status().getCode() == InventoryStatus.LESS.getIndex()) {
                    lessInvNum++;
                    if(StringUtils.isEmpty(invLocationBean.getLocId())){
                        invLocationBean.setLocId(invdetail.getAssetsInfos().getLoc_id());
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
        showFilterData(currentFilterBean);
        if(mInventoryDetails.size()>0){
            mInvDetailRecyclerView.setVisibility(View.VISIBLE);
            empty_layout.setVisibility(View.GONE);
        }else {
            mInvDetailRecyclerView.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
        }
    }

    //资产列表排序，未盘点在前
    public void sortListByInvStatus(List<InventoryDetail> inventoryDetails) {
        Collections.sort(inventoryDetails, new Comparator<InventoryDetail>() {
            @Override
            public int compare(InventoryDetail t1, InventoryDetail t2) {
                return t1.getInvdt_status().getCode().compareTo(t2.getInvdt_status().getCode());
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

    public void initView(){
        mLoctionAdapter = new InvLocationAdapter(mCurrentLoctionBeans, this);
        mInvDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mInvDetailRecyclerView.setAdapter(mLoctionAdapter);
        //初始化popupwindow
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_filter_layout,null);
        filtterAdapter = new FiltterAdapter(currentFilterBeans, this);
        filtterAdapter.setOnItemClickListener(this);
        filerRecycler = contentView.findViewById(R.id.rv_filter);
        filerRecycler.setLayoutManager(new LinearLayoutManager(this));
        filerRecycler.setAdapter(filtterAdapter);
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(false)
                .setAnimationStyle(R.style.popwin_anim)
                .size(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
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
    }

    @Override
    public void onItemSelected(FilterBean filterBean) {
        currentFilterBean = filterBean;
        showFilterData(currentFilterBean);
        mCustomPopWindow.dissmiss();
    }

    public void showFilterData(FilterBean filterBean){
        mCurrentLoctionBeans.clear();
        if("10000".equals(filterBean.getId())){
            mCurrentLoctionBeans.addAll(mLoctionBeans);
        }else if("10001".equals(filterBean.getId())){
            for (InvLocationBean mLoctionBean : mLoctionBeans) {
                if(mLoctionBean.getNotInvNum() == 0){
                    mCurrentLoctionBeans.add(mLoctionBean);
                }
            }
        }else if("10002".equals(filterBean.getId())){
            for (InvLocationBean mLoctionBean : mLoctionBeans) {
                if(mLoctionBean.getNotInvNum() != 0){
                    mCurrentLoctionBeans.add(mLoctionBean);
                }
            }
        }else {
            for (InvLocationBean mLoctionBean : mLoctionBeans) {
                if(filterBean.getId().equals(mLoctionBean.getLocId())){
                    mCurrentLoctionBeans.add(mLoctionBean);
                }
            }
        }
        mLoctionAdapter.notifyDataSetChanged();
    }
}
