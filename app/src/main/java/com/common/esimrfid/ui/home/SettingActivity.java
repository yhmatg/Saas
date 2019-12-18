package com.common.esimrfid.ui.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.ui.inventorytask.InventoryTaskActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.title_back)
    ImageView mBackImg;
    @BindView(R.id.title_content)
    TextView mTitle;
    @BindView(R.id.tv_version)
    TextView mVersion;

    @Override
    public AbstractPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData() {
        mTitle.setText(R.string.setting_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initToolbar() {

    }

    @OnClick({R.id.bt_loginout, R.id.title_back})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.bt_loginout:
                DataManager.getInstance().setLoginStatus(false);
                EsimAndroidApp.getInstance().exitActivitys();
                System.exit(0);
                break;
            case R.id.title_back:
                finish();
                break;
        }
    }
}
