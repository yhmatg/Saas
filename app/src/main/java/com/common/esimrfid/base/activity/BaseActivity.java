package com.common.esimrfid.base.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.uhf.IEsimUhfService;
import com.common.esimrfid.uhf.ZebraUhfServiceImpl;
import com.common.esimrfid.ui.login.LoginActivity;
import com.common.esimrfid.utils.CommonUtils;


/**
 * MVP模式的Base Activity
 *
 * @author yhm
 * @date 2017/11/28
 */

public abstract class BaseActivity<T extends AbstractPresenter> extends AbstractSimpleActivity implements
        AbstractView {

    protected T mPresenter;

    private MaterialDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        onViewCreated();
        initEventAndData();
        EsimAndroidApp.getInstance().addActivity(this);
    }

    public abstract T initPresenter();

    @Override
    protected void onViewCreated() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
        EsimAndroidApp.getInstance().removeActivity(this);
        super.onDestroy();
    }


    @Override
    public void useNightMode(boolean isNight) {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        CommonUtils.showSnackMessage(this, errorMsg);
    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void showCollectSuccess() {

    }

    @Override
    public void showCancelCollectSuccess() {

    }

    @Override
    public void showLoginView() {

    }

    @Override
    public void showLogoutView() {

    }

    @Override
    public void showToast(String message) {
        CommonUtils.showMessage(this, message);
    }

    @Override
    public void showSnackBar(String message) {
        CommonUtils.showSnackMessage(this, message);
    }

    public void showDialog(String title) {
        if (dialog != null) {
            dialog.show();
        } else {
            //MaterialDialog.Builder builder = MaterialDialogUtils.showIndeterminateProgressDialog(this, title, true);
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .title(title)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .canceledOnTouchOutside(false)
                    .backgroundColorRes(R.color.white)
                    .keyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {//如果是按下，则响应，否则，一次按下会响应两次
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    //activity.onBackPressed();

                                }
                            }
                            return false;//false允许按返回键取消对话框，true除了调用取消，其他情况下不会取消
                        }
                    });
            dialog = builder.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 初始化数据
     */
    protected abstract void initEventAndData();

    @Override
    public void startLoginActivity(){
        DataManager.getInstance().setLoginStatus(false);
        EsimAndroidApp.getInstance().exitActivitys();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public UserLoginResponse getUserLoginResponse(){
        UserLoginResponse loginResponse = EsimAndroidApp.getInstance().getUserLoginResponse();
        if(loginResponse == null){
            loginResponse = DataManager.getInstance().getUserLoginResponse();
        }
        return loginResponse;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IEsimUhfService iEsimUhfService = EsimAndroidApp.getIEsimUhfService();
        if( iEsimUhfService instanceof ZebraUhfServiceImpl){
            ((ZebraUhfServiceImpl)iEsimUhfService).setScanEnable();
        }
    }
}
