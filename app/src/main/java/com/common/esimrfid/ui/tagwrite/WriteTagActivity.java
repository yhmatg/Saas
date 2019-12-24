package com.common.esimrfid.ui.tagwrite;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.home.WriteTagContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.ui.home.HomeActivity;
import com.common.esimrfid.presenter.home.WriteTagPresenter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WriteTagActivity extends BaseActivity<WriteTagPresenter> implements WriteTagContract.View {
    @BindView(R.id.edit_search)
    EditText search;
    @BindView(R.id.titleLeft)
    ImageView titleLeft;
    @BindView(R.id.write_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.empty_page)
    LinearLayout empty_page;
    @BindView(R.id.tv_tips)
    TextView tips;
    IEsimUhfService esimUhfService = null;
    public static final String TAG = "WriteTagActivity";
    private List<AssetsInfo> mData = new ArrayList<>();
    private WriteTagAdapter adapter;

    @Override
    public WriteTagPresenter initPresenter() {
        return new WriteTagPresenter(DataManager.getInstance());
    }

    @Override
    protected void initEventAndData() {
        initRfidAndEvent();
        adapter = new WriteTagAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        empty_page.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tips.setVisibility(View.GONE);

    }

    private void initRfidAndEvent() {
        esimUhfService = EsimAndroidApp.getIEsimUhfService();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_write_tag;
    }

    @OnClick({R.id.titleLeft, R.id.edit_search})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.titleLeft:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.edit_search:
                searchAssets();
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
                    mPresenter.getAssetsInfoById(assetsId);
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
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void handleAssetsById(List<AssetsInfo> assetsInfos) {
        mData.clear();
        mData.addAll(assetsInfos);
        adapter.notifyDataSetChanged();
        handleResultList(mData);
    }

    private void handleResultList(List<AssetsInfo> mData) {
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
}
