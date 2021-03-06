package com.common.esimrfid.ui.astlist;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.assetlist.AssetListContract;
import com.common.esimrfid.core.bean.assetdetail.AssetFilterParameter;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;
import com.common.esimrfid.customview.CustomPopWindow;
import com.common.esimrfid.presenter.assetlist.AssetListPresenter;
import com.common.esimrfid.ui.inventorytask.FilterBean;
import com.common.esimrfid.ui.inventorytask.FiltterAdapter;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.multilevel.treelist.Node;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AssetListActivity extends BaseActivity<AssetListPresenter> implements AssetListContract.View, FiltterAdapter.OnItemClickListener {
    @BindView(R.id.edit_search)
    EditText search;
    @BindView(R.id.titleLeft)
    ImageView titleLeft;
    @BindView(R.id.im_filer)
    ImageView imFilter;
    @BindView(R.id.write_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.empty_page)
    LinearLayout empty_page;
    @BindView(R.id.tv_tips)
    TextView tips;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.view_mask)
    View maskView;
    @BindView(R.id.tv_sort)
    TextView mTvSort;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.im_arrow_one)
    ImageView mSortArrow;
    @BindView(R.id.im_arrow_two)
    ImageView mStatusArrow;
    @BindView(R.id.sort_layout)
    LinearLayout sortLayout;
    @BindView(R.id.tv_ast_num)
    TextView mAstNum;
    @BindView(R.id.tv_ast_price)
    TextView mAstPrice;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.bt_reset)
    Button btReset;
    @BindView(R.id.tv_ast_status)
    TextView mAstStatusStr;
    @BindView(R.id.tv_inv_usecom)
    TextView mUseCompStr;
    @BindView(R.id.tv_inv_usedepart)
    TextView mUseDepartStr;
    @BindView(R.id.tv_inv_asstype)
    TextView mAstTypeStr;
    @BindView(R.id.tv_inv_loc)
    TextView mAstLocStr;
    @BindView(R.id.tv_manager)
    TextView mAstManagerStr;
    @BindView(R.id.tv_asset_user)
    EditText mAstUserStr;
    ImageView mBackImg;
    TextView mTitle;
    TextView mConfirm;
    public static final String TAG = "WriteTagActivity";
    private List<AssetsListItemInfo> mData = new ArrayList<>();
    private AssetListAdapter adapter;
    private boolean isNeedClearData;
    private String preFilter = "";
    private int currentPage = 1;
    private int pageSize = 10;
    List<FilterBean> mSortBeans = new ArrayList<>();
    List<FilterBean> mStatusBeans = new ArrayList<>();
    List<FilterBean> currentFilterBeans = new ArrayList<>();
    private FiltterAdapter filtterAdapter;
    private RecyclerView filerRecycler;
    CustomPopWindow mCustomPopWindow;
    private FilterBean currentSortFilter = new FilterBean("11", "??????????????????", false);
    private FilterBean currentStatusFilter = new FilterBean("1", "??????", false);
    private String currentCodition = "[]";
    private Dialog filterDialog;
    private RecyclerView multiRecycle;
    private RecyclerView singleRecycle;
    private View filterView;
    private List<Node> currentMultiDatas = new ArrayList<>();
    List<FilterBean> currentSingleDatas = new ArrayList<>();
    private AssetFilterRecyclerAdapter multiFilterAdapter;
    private FiltterAdapter singleFilterAdapter;
    List<Node> mMangerUsers = new ArrayList<>();
    List<CompanyBean> mCompanyBeans = new ArrayList<>();
    List<DepartmentBean> mDepartmentBeans = new ArrayList<>();
    List<Node> mAssetsTypes = new ArrayList<>();
    List<Node> mAssetsLocations = new ArrayList<>();
    List<Node> mAllStatusBeans = new ArrayList<>();
    List<Node> mSelectMangerUsers = new ArrayList<>();
    FilterBean mSelectUseCompany;
    List<Node> mSelectDepartments = new ArrayList<>();
    List<Node> mSelectAssetsTypes = new ArrayList<>();
    List<Node> mSelectAssetsLocations = new ArrayList<>();
    List<Node> mSelectAssetsStatus = new ArrayList<>();
    int currentFilterId = -1;
    AssetFilterParameter conditions = new AssetFilterParameter();

    @Override
    public AssetListPresenter initPresenter() {
        return new AssetListPresenter();
    }

    @Override
    protected void initEventAndData() {
        inFilterList();
        initView();
        initOptions();
        adapter = new AssetListAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        empty_page.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tips.setVisibility(View.GONE);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isNeedClearData = true;
                mPresenter.fetchPageAssetsInfos(pageSize, currentPage, "", "", 0, conditions);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                String assetsId = search.getText().toString();
                isNeedClearData = !preFilter.equals(assetsId);
                if (isNeedClearData) {
                    currentPage = 1;
                } else {
                    currentPage++;
                }
                int currentSize = currentPage == 1 ? 0 : mData.size();
                preFilter = assetsId;
                String userName = conditions.getUserRealName() == null ? "" : conditions.getUserRealName();
                mPresenter.fetchPageAssetsInfos(pageSize, currentPage, assetsId, userName, currentSize, conditions);
            }
        });
        mRefreshLayout.setEnableRefresh(false);//?????????????????????????????????
        mRefreshLayout.setEnableOverScrollDrag(false);//?????????????????????1.0.4???????????????
        mRefreshLayout.setEnableOverScrollBounce(false);//????????????????????????
        mRefreshLayout.setEnableAutoLoadMore(false);
        mPresenter.fetchPageAssetsInfos(pageSize, 1, "", "", 0, conditions);
    }

    public void initView() {
        //?????????popupwindow
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
                        mTvSort.setTextColor(getColor(R.color.repair_text));
                        mSortArrow.setImageResource(R.drawable.down_arrow_black);
                        mTvStatus.setTextColor(getColor(R.color.repair_text));
                        mStatusArrow.setImageResource(R.drawable.down_arrow_black);
                    }
                })
                .create();

        //?????????????????????
        filterView = LayoutInflater.from(this).inflate(R.layout.filter_item_layout, null);
        multiRecycle = (RecyclerView) filterView.findViewById(R.id.multi_recycle);
        multiRecycle.setLayoutManager(new LinearLayoutManager(this));
        multiFilterAdapter = new AssetFilterRecyclerAdapter(multiRecycle, this,
                currentMultiDatas, 2, R.drawable.tree_reduce, R.drawable.tree_add);
        multiRecycle.setAdapter(multiFilterAdapter);
        singleRecycle = (RecyclerView) filterView.findViewById(R.id.single_recycle);
        singleRecycle.setLayoutManager(new LinearLayoutManager(this));
        singleFilterAdapter = new FiltterAdapter(currentSingleDatas, this);
        singleFilterAdapter.setOnItemClickListener(this);
        singleRecycle.setAdapter(singleFilterAdapter);
        mBackImg = filterView.findViewById(R.id.title_back);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });
        mTitle = filterView.findViewById(R.id.title_content);
        mConfirm = filterView.findViewById(R.id.tv_inv_sure);
        mConfirm.setText(R.string.finish_str);
        mConfirm.setVisibility(View.VISIBLE);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Node> allNodes = multiFilterAdapter.getAllNodes();
                switch (currentFilterId) {
                    case R.id.rl_asset_status:
                        mSelectAssetsStatus.clear();
                        for (Node node : allNodes) {
                            if (node.isChecked()) {
                                mSelectAssetsStatus.add(node);
                            }
                        }
                        String currentStatus = "??????";
                        if (mSelectAssetsStatus.size() == 1) {
                            currentStatus = mSelectAssetsStatus.get(0).getName();
                        } else if (mSelectAssetsStatus.size() > 1) {
                            currentStatus = mSelectAssetsStatus.get(0).getName() + "+" + (mSelectAssetsStatus.size() - 1);
                        }
                        if (!StringUtils.isEmpty(currentStatus)) {
                            mAstStatusStr.setText(currentStatus);
                        }
                        break;
                    case R.id.rl_use_depart:
                        mSelectDepartments.clear();
                        for (Node node : allNodes) {
                            if (node.isChecked()) {
                                mSelectDepartments.add(node);
                            }
                        }
                        String currentdepart = "??????";
                        if (mSelectDepartments.size() == 1) {
                            currentdepart = mSelectDepartments.get(0).getName();
                        } else if (mSelectDepartments.size() > 1) {
                            currentdepart = mSelectDepartments.get(0).getName() + "+" + (mSelectDepartments.size() - 1);
                        }
                        if (!StringUtils.isEmpty(currentdepart)) {
                            mUseDepartStr.setText(currentdepart);
                        }
                        break;
                    case R.id.rl_ast_type:
                        mSelectAssetsTypes.clear();
                        for (Node node : allNodes) {
                            if (node.isChecked()) {
                                mSelectAssetsTypes.add(node);
                            }
                        }
                        String currentType = "??????";
                        if (mSelectAssetsTypes.size() == 1) {
                            currentType = mSelectAssetsTypes.get(0).getName();
                        } else if (mSelectAssetsTypes.size() > 1) {
                            currentType = mSelectAssetsTypes.get(0).getName() + "+" + (mSelectAssetsTypes.size() - 1);
                        }
                        if (!StringUtils.isEmpty(currentType)) {
                            mAstTypeStr.setText(currentType);
                        }
                        break;
                    case R.id.rl_store_location:
                        mSelectAssetsLocations.clear();
                        for (Node node : allNodes) {
                            if (node.isChecked()) {
                                mSelectAssetsLocations.add(node);
                            }
                        }
                        String currentLoc = "??????";
                        if (mSelectAssetsLocations.size() == 1) {
                            currentLoc = mSelectAssetsLocations.get(0).getName();
                        } else if (mSelectAssetsLocations.size() > 1) {
                            currentLoc = mSelectAssetsLocations.get(0).getName() + "+" + (mSelectAssetsLocations.size() - 1);
                        }
                        if (!StringUtils.isEmpty(currentLoc)) {
                            mAstLocStr.setText(currentLoc);
                        }
                        break;
                    case R.id.rl_manager:
                        mSelectMangerUsers.clear();
                        for (Node node : allNodes) {
                            if (node.isChecked()) {
                                mSelectMangerUsers.add(node);
                            }
                        }
                        String currentManager = "??????";
                        if (mSelectMangerUsers.size() == 1) {
                            currentManager = mSelectMangerUsers.get(0).getName();
                        } else if (mSelectMangerUsers.size() > 1) {
                            currentManager = mSelectMangerUsers.get(0).getName() + "+" + (mSelectMangerUsers.size() - 1);
                        }
                        if (!StringUtils.isEmpty(currentManager)) {
                            mAstManagerStr.setText(currentManager);
                        }
                        break;
                }
                filterDialog.dismiss();
            }
        });
        //???????????????????????????
        if (!CommonUtils.isNetworkConnected()) {
            // ??????????????????
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            imFilter.setClickable(false);
        }
    }

    private void initOptions() {
        mPresenter.getAllManagerUsers();
        mPresenter.getAllCompany();
        mPresenter.getAllAssetsType();
        mPresenter.getAllAssetsLocation();
    }

    private void inFilterList() {
        //??????
        mSortBeans.add(new FilterBean("10", "????????????", false));
        mSortBeans.add(new FilterBean("11", "??????????????????", false));
        mSortBeans.add(new FilterBean("12", "??????????????????", false));
        mSortBeans.add(new FilterBean("13", "??????????????????", false));
        mSortBeans.add(new FilterBean("14", "??????????????????", false));
        //????????????
        mStatusBeans.add(new FilterBean("20", "??????", false));
        mStatusBeans.add(new FilterBean("21", "??????", false));
        mStatusBeans.add(new FilterBean("22", "??????", false));
        mStatusBeans.add(new FilterBean("23", "??????", false));
        mStatusBeans.add(new FilterBean("24", "?????????", false));
        //????????????
        mAllStatusBeans.add(new Node("-1", "-2", "??????"));
        mAllStatusBeans.add(new Node("0", "-1", "??????"));
        mAllStatusBeans.add(new Node("1", "-1", "??????"));
        mAllStatusBeans.add(new Node("2", "-1", "?????????"));
        mAllStatusBeans.add(new Node("3", "-1", "?????????"));
        mAllStatusBeans.add(new Node("4", "-1", "?????????"));
        mAllStatusBeans.add(new Node("5", "-1", "?????????"));
        mAllStatusBeans.add(new Node("6", "-1", "??????"));
        mAllStatusBeans.add(new Node("7", "-1", "???????????????"));
        mAllStatusBeans.add(new Node("8", "-1", "???????????????"));
        mAllStatusBeans.add(new Node("9", "-1", "???????????????"));
        mAllStatusBeans.add(new Node("10", "-1", "??????"));
        mAllStatusBeans.add(new Node("11", "-1", "???????????????"));
        mAllStatusBeans.add(new Node("12", "-1", "???????????????"));
        mAllStatusBeans.add(new Node("13", "-1", "???????????????"));
        mAllStatusBeans.add(new Node("14", "-1", "???????????????"));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_asset_list;
    }

    @OnClick({R.id.titleLeft, R.id.edit_search, R.id.sort_layout, R.id.status_layout, R.id.im_filer, R.id.bt_sure, R.id.bt_reset, R.id.rl_asset_status, R.id.rl_use_company,
            R.id.rl_use_depart, R.id.rl_ast_type, R.id.rl_manager, R.id.rl_store_location, R.id.rl_asset_user})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft:
                finish();
                break;
            case R.id.edit_search:
                searchAssets();
                break;
            case R.id.sort_layout:
                currentFilterBeans.clear();
                currentFilterBeans.addAll(mSortBeans);
                filtterAdapter.notifyDataSetChanged();
                mCustomPopWindow.showAsDropDown(sortLayout);
                maskView.setVisibility(View.VISIBLE);
                mTvSort.setTextColor(getColor(R.color.titele_color));
                mSortArrow.setImageResource(R.drawable.down_arrow_blue);
                break;
            case R.id.status_layout:
                currentFilterBeans.clear();
                currentFilterBeans.addAll(mStatusBeans);
                filtterAdapter.notifyDataSetChanged();
                mCustomPopWindow.showAsDropDown(sortLayout);
                maskView.setVisibility(View.VISIBLE);
                mTvStatus.setTextColor(getColor(R.color.titele_color));
                mStatusArrow.setImageResource(R.drawable.down_arrow_blue);
                break;
            case R.id.im_filer:
                mDrawerLayout.openDrawer(Gravity.END);
                break;
            case R.id.rl_asset_status:
                currentFilterId = R.id.rl_asset_status;
                mTitle.setText(R.string.asset_status_hint);
                multiFilterAdapter.removeData(currentMultiDatas);
                currentMultiDatas.clear();
                currentMultiDatas.addAll(mAllStatusBeans);
                multiFilterAdapter.addData(currentMultiDatas);
                multiRecycle.setVisibility(View.VISIBLE);
                singleRecycle.setVisibility(View.GONE);
                showFilterDialog();
                break;
            case R.id.rl_use_company:
                currentFilterId = R.id.rl_use_company;
                mTitle.setText(R.string.inv_usecom_hint);
                singleFilterAdapter.notifyDataSetChanged();
                multiRecycle.setVisibility(View.GONE);
                singleRecycle.setVisibility(View.VISIBLE);
                showFilterDialog();
                break;
            case R.id.rl_use_depart:
                currentFilterId = R.id.rl_use_depart;
                mTitle.setText(R.string.inv_usedepart_hint);
                if (mSelectUseCompany != null && !TextUtils.isEmpty(mSelectUseCompany.getId()) && !"-1".equals(mSelectUseCompany.getId())) {
                    mPresenter.getAllDeparts(mSelectUseCompany.getId());
                } else {
                    ToastUtils.showShort(R.string.choose_com_first);
                }
                break;
            case R.id.rl_ast_type:
                currentFilterId = R.id.rl_ast_type;
                mTitle.setText(R.string.inv_asstype_hint);
                multiFilterAdapter.removeData(currentMultiDatas);
                currentMultiDatas.clear();
                currentMultiDatas.addAll(mAssetsTypes);
                multiFilterAdapter.addData(currentMultiDatas);
                multiRecycle.setVisibility(View.VISIBLE);
                singleRecycle.setVisibility(View.GONE);
                showFilterDialog();
                break;
            case R.id.rl_store_location:
                currentFilterId = R.id.rl_store_location;
                mTitle.setText(R.string.inv_loc_hint);
                multiFilterAdapter.removeData(currentMultiDatas);
                currentMultiDatas.clear();
                currentMultiDatas.addAll(mAssetsLocations);
                multiFilterAdapter.addData(currentMultiDatas);
                multiRecycle.setVisibility(View.VISIBLE);
                singleRecycle.setVisibility(View.GONE);
                showFilterDialog();
                break;
            case R.id.rl_manager:
                currentFilterId = R.id.rl_manager;
                mTitle.setText(R.string.manager_hing);
                multiFilterAdapter.removeData(currentMultiDatas);
                currentMultiDatas.clear();
                currentMultiDatas.addAll(mMangerUsers);
                multiFilterAdapter.addData(currentMultiDatas);
                multiRecycle.setVisibility(View.VISIBLE);
                singleRecycle.setVisibility(View.GONE);
                showFilterDialog();
                break;
            case R.id.rl_asset_user:
                break;
            case R.id.bt_sure:
                isNeedClearData = true;
                currentPage = 1;
                conditions.clearData();
                conditions.setmSelectAssetsStatus(mSelectAssetsStatus);
                conditions.setmSelectUseCompany(mSelectUseCompany);
                conditions.setmSelectDepartments(mSelectDepartments);
                conditions.setmSelectAssetsTypes(mSelectAssetsTypes);
                conditions.setmSelectAssetsLocations(mSelectAssetsLocations);
                conditions.setmSelectMangerUsers(mSelectMangerUsers);
                conditions.setUserRealName(mAstUserStr.getText().toString());
                mPresenter.fetchPageAssetsInfos(pageSize, currentPage, "", mAstUserStr.getText().toString(), 0, conditions);
                currentStatusFilter.setSelected(false);
                filtterAdapter.notifyDataSetChanged();
                mDrawerLayout.closeDrawer(Gravity.END);
                break;
            case R.id.bt_reset:
                isNeedClearData = true;
                currentPage = 1;
                conditions.clearData();
                mSelectAssetsStatus.clear();
                mSelectUseCompany = null;
                mSelectDepartments.clear();
                mSelectAssetsTypes.clear();
                mSelectAssetsLocations.clear();
                mSelectMangerUsers.clear();
                mAstStatusStr.setText("");
                mUseCompStr.setText("");
                mUseDepartStr.setText("");
                mAstTypeStr.setText("");
                mAstLocStr.setText("");
                mAstManagerStr.setText("");
                mAstUserStr.setText("");
                break;
        }
    }

    private void searchAssets() {
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    String assetsId = search.getText().toString();
                    search.setSelection(assetsId.length());
                    isNeedClearData = true;
                    currentPage = 1;
                    preFilter = assetsId;
                    conditions.clearData();
                    currentStatusFilter.setSelected(false);
                    filtterAdapter.notifyDataSetChanged();
                    mPresenter.fetchPageAssetsInfos(pageSize, currentPage, assetsId, "", 0, conditions);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void handlefetchPageAssetsInfos(List<AssetsListItemInfo> assetsInfos, int astCount, double totalMoney) {
        mRefreshLayout.finishLoadMore();
        if (isNeedClearData) {
            mData.clear();
        }
        mData.addAll(assetsInfos);
        sortList(currentSortFilter);
        adapter.notifyDataSetChanged();
        handleResultList(mData);
       /* double totalPrice = 0;
        for (AssetsListItemInfo mDatum : mData) {
            totalPrice += mDatum.getAst_price();
        }
        mAstNum.setText(String.valueOf(mData.size()));
        mAstPrice.setText(String.valueOf(totalPrice));*/
        if (!(0 == astCount && 0 == totalMoney)) {
            mAstNum.setText(String.valueOf(astCount));
            mAstPrice.setText(String.valueOf(totalMoney));
        }
    }

    @Override
    public void handleAllManagerUsers(List<MangerUser> mangerUsers) {
        mMangerUsers.clear();
        mMangerUsers.add(new Node("-1", "-2", "??????"));
        for (MangerUser mMangerUser : mangerUsers) {
            mMangerUsers.add(new Node(mMangerUser.getId(), "-1", mMangerUser.getUser_real_name()));
        }
    }

    @Override
    public void handleAllCompany(List<CompanyBean> companyBeans) {
        mCompanyBeans.clear();
        mCompanyBeans.addAll(companyBeans);
        currentSingleDatas.clear();
        currentSingleDatas.add(new FilterBean("-1", "??????", false));
        for (CompanyBean mCompanyBean : mCompanyBeans) {
            currentSingleDatas.add(new FilterBean(mCompanyBean.getId(), mCompanyBean.getOrg_name(), false));
        }
    }

    @Override
    public void handleAllDeparts(List<DepartmentBean> departmentBeans) {
        //????????????????????????????????????????????????
        List<DepartmentBean> tempList = new ArrayList<>();
        for (DepartmentBean departmentBean : departmentBeans) {
            if (departmentBean.getOrg_type() == 0 && departmentBean.getOrg_superid().equals(mSelectUseCompany.getId())) {
                //????????????????????????????????????
                List<DepartmentBean> oneDeparts = new ArrayList<>();
                oneDeparts = getSelectDeparts(departmentBeans, departmentBean.getId(), oneDeparts);
                tempList.addAll(oneDeparts);
            }
        }
        mDepartmentBeans.clear();
        mDepartmentBeans.addAll(tempList);
        multiFilterAdapter.removeData(currentMultiDatas);
        currentMultiDatas.clear();
        currentMultiDatas.add(new Node(mSelectUseCompany.getId(), "-2", "??????"));
        for (DepartmentBean mDepartmentBean : mDepartmentBeans) {
            String pId = StringUtils.isEmpty(mDepartmentBean.getOrg_superid()) ? "-1" : mDepartmentBean.getOrg_superid();
            currentMultiDatas.add(new Node(mDepartmentBean.getId(), pId, mDepartmentBean.getOrg_name()));
        }
        multiFilterAdapter.addData(currentMultiDatas);
        multiRecycle.setVisibility(View.VISIBLE);
        singleRecycle.setVisibility(View.GONE);
        showFilterDialog();
    }

    @Override
    public void handleAllAssetsType(List<AssetsType> assetsTypes) {
        mAssetsTypes.clear();
        mAssetsTypes.add(new Node("-1", "-2", "??????"));
        for (AssetsType assetsType : assetsTypes) {
            String pId = StringUtils.isEmpty(assetsType.getType_superid()) ? "-1" : assetsType.getType_superid();
            mAssetsTypes.add(new Node(assetsType.getId(), pId, assetsType.getType_name()));
        }
    }

    @Override
    public void handleAllAssetsLocation(List<AssetsLocation> assetsLocations) {
        mAssetsLocations.clear();
        mAssetsLocations.add(new Node("-1", "-2", "??????"));
        for (AssetsLocation mAssetsLocation : assetsLocations) {
            String pId = StringUtils.isEmpty(mAssetsLocation.getLoc_superid()) ? "-1" : mAssetsLocation.getLoc_superid();
            mAssetsLocations.add(new Node(mAssetsLocation.getId(), pId, mAssetsLocation.getLoc_name()));
        }
    }

    private void handleResultList(List<AssetsListItemInfo> mData) {
        if (mData.size() == 0) {
            empty_page.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tips.setVisibility(View.GONE);
        } else {
            empty_page.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            tips.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(FilterBean filterBean) {
        switch (filterBean.getId()) {
            case "10":
            case "11":
                currentSortFilter = filterBean;
                Collections.sort(mData, new Comparator<AssetsListItemInfo>() {
                    @Override
                    public int compare(AssetsListItemInfo o1, AssetsListItemInfo o2) {
                        return o1.getAst_barcode().compareTo(o2.getAst_barcode());
                    }
                });
                break;
            case "12":
                currentSortFilter = filterBean;
                Collections.sort(mData, new Comparator<AssetsListItemInfo>() {
                    @Override
                    public int compare(AssetsListItemInfo o1, AssetsListItemInfo o2) {
                        return o2.getAst_barcode().compareTo(o1.getAst_barcode());
                    }
                });
                break;
            case "13":
                currentSortFilter = filterBean;
                Collections.sort(mData, new Comparator<AssetsListItemInfo>() {
                    @Override
                    public int compare(AssetsListItemInfo o1, AssetsListItemInfo o2) {
                        return Long.compare(o1.getAst_buy_date(), o2.getAst_buy_date());
                    }
                });
                break;
            case "14":
                currentSortFilter = filterBean;
                Collections.sort(mData, new Comparator<AssetsListItemInfo>() {
                    @Override
                    public int compare(AssetsListItemInfo o1, AssetsListItemInfo o2) {
                        return Long.compare(o2.getAst_buy_date(), o1.getAst_buy_date());
                    }
                });
                break;
            case "20":
                currentStatusFilter = filterBean;
                isNeedClearData = true;
                currentPage = 1;
                ArrayList<Node> statusOne = new ArrayList<>();
                statusOne.add(new Node("0", "-1", "??????"));
                conditions.clearData();
                conditions.setmSelectAssetsStatus(statusOne);
                search.setText("");
                mPresenter.fetchPageAssetsInfos(pageSize, currentPage, "", "", 0, conditions);
                break;
            case "21":
                currentStatusFilter = filterBean;
                isNeedClearData = true;
                currentPage = 1;
                ArrayList<Node> statusTwo = new ArrayList<>();
                statusTwo.add(new Node("1", "-1", "??????"));
                conditions.clearData();
                conditions.setmSelectAssetsStatus(statusTwo);
                search.setText("");
                mPresenter.fetchPageAssetsInfos(pageSize, currentPage, "", "", 0, conditions);
                break;
            case "22":
                currentStatusFilter = filterBean;
                isNeedClearData = true;
                currentPage = 1;
                ArrayList<Node> statusThree = new ArrayList<>();
                statusThree.add(new Node("6", "-1", "??????"));
                conditions.clearData();
                conditions.setmSelectAssetsStatus(statusThree);
                search.setText("");
                mPresenter.fetchPageAssetsInfos(pageSize, currentPage, "", "", 0, conditions);
                break;
            case "23":
                currentStatusFilter = filterBean;
                isNeedClearData = true;
                currentPage = 1;
                ArrayList<Node> statusFour = new ArrayList<>();
                statusFour.add(new Node("10", "-1", "??????"));
                conditions.clearData();
                conditions.setmSelectAssetsStatus(statusFour);
                search.setText("");
                mPresenter.fetchPageAssetsInfos(pageSize, currentPage, "", "", 0, conditions);
                break;
            case "24":
                currentStatusFilter = filterBean;
                isNeedClearData = true;
                currentPage = 1;
                ArrayList<Node> statusFive = new ArrayList<>();
                statusFive.add(new Node("7", "-1", "???????????????"));
                statusFive.add(new Node("8", "-1", "???????????????"));
                statusFive.add(new Node("9", "-1", "???????????????"));
                statusFive.add(new Node("11", "-1", "???????????????"));
                statusFive.add(new Node("12", "-1", "???????????????"));
                statusFive.add(new Node("13", "-1", "???????????????"));
                statusFive.add(new Node("14", "-1", "???????????????"));
                conditions.clearData();
                conditions.setmSelectAssetsStatus(statusFive);
                search.setText("");
                mPresenter.fetchPageAssetsInfos(pageSize, currentPage, "", "", 0, conditions);
                break;
            default:
                mSelectUseCompany = filterBean;
                mUseCompStr.setText(mSelectUseCompany.getName());
                filterDialog.dismiss();
                break;
        }
        mCustomPopWindow.dissmiss();
        adapter.notifyDataSetChanged();
    }

    public void sortList(FilterBean filterBean) {
        switch (filterBean.getId()) {
            case "10":
            case "11":
                Collections.sort(mData, new Comparator<AssetsListItemInfo>() {
                    @Override
                    public int compare(AssetsListItemInfo o1, AssetsListItemInfo o2) {
                        return o1.getAst_barcode().compareTo(o2.getAst_barcode());
                    }
                });
                break;
            case "12":
                Collections.sort(mData, new Comparator<AssetsListItemInfo>() {
                    @Override
                    public int compare(AssetsListItemInfo o1, AssetsListItemInfo o2) {
                        return o2.getAst_barcode().compareTo(o1.getAst_barcode());
                    }
                });
                break;
            case "13":
                Collections.sort(mData, new Comparator<AssetsListItemInfo>() {
                    @Override
                    public int compare(AssetsListItemInfo o1, AssetsListItemInfo o2) {
                        return Long.compare(o1.getAst_buy_date(), o2.getAst_buy_date());
                    }
                });
                break;
            case "14":
                Collections.sort(mData, new Comparator<AssetsListItemInfo>() {
                    @Override
                    public int compare(AssetsListItemInfo o1, AssetsListItemInfo o2) {
                        return Long.compare(o2.getAst_buy_date(), o1.getAst_buy_date());
                    }
                });
                break;
        }
        mCustomPopWindow.dissmiss();
        adapter.notifyDataSetChanged();
    }

    public void showFilterDialog() {
        if (filterDialog != null) {
            filterDialog.show();
        } else {
            filterDialog = new Dialog(this);
            filterDialog.setContentView(filterView);
            filterDialog.show();
            Window window = filterDialog.getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.getDecorView().setBackgroundColor(Color.WHITE);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(layoutParams);
            }
        }

    }

    public List<DepartmentBean> getSelectDeparts(List<DepartmentBean> allDeparts, String id, List<DepartmentBean> resultDeparts) {
        for (DepartmentBean allDepart : allDeparts) {
            String selId = allDepart.getId();
            String pId = allDepart.getOrg_superid();
            if (id.equals(selId) && !resultDeparts.contains(allDepart)) {
                resultDeparts.add(allDepart);
            }
            if (pId != null && pId.equals(id) && !resultDeparts.contains(allDepart)) {
                resultDeparts.add(allDepart);
                getSelectDeparts(allDeparts, selId, resultDeparts);
            }
        }
        return resultDeparts;
    }
}
