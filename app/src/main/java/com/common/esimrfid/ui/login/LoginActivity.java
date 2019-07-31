package com.common.esimrfid.ui.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.login.LoginContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.UserInfo;
import com.common.esimrfid.presenter.login.LoginPresenter;
import com.common.esimrfid.ui.home.HomeActivity;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author yhm
 * @date 2018/2/26
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.login_account_edit)
    EditText mAccountEdit;
    @BindView(R.id.login_password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.btn_setting)
    FloatingActionButton mFloatBut;
    private  String TAG = "LoginActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolbar() {
    }

    @Override
    protected void initEventAndData() {
        if(StringUtils.isEmpty(mPresenter.getHostUrl())){
            showSettingDialog();
        }
        initAccountState();

    }

    private void initAccountState() {
        mAccountEdit.setText(mPresenter.getLoginAccount());
        mPasswordEdit.setText(mPresenter.getLoginPassword());
    }

    @Override
    public void showLoginSuccess() {

    }

    @OnClick({R.id.login_btn,R.id.btn_setting})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                login();
                break;
            case R.id.btn_setting:
                showSettingDialog();
                break;
            default:
                break;
        }
    }


    private void login() {
        if (TextUtils.isEmpty(mAccountEdit.getText().toString())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(mPasswordEdit.getText().toString())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        final UserInfo userInfo=new UserInfo();
        userInfo.setUser_name(mAccountEdit.getText().toString());
        userInfo.setUser_password(mPasswordEdit.getText().toString());
        mPresenter.login(userInfo);
    }

    private void showSettingDialog() {
        final String hostUrl=mPresenter.getHostUrl();
        final boolean isSoundOpen=mPresenter.getOpenSound();
        //check事件
        new MaterialDialog.Builder(this)
                .title("设置")
                .content("配置服务器URL")
                .inputType(InputType.TYPE_CLASS_TEXT)
                //前2个一个是hint一个是预输入的文字
                .input("http://", hostUrl, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                    }
                })
                .checkBoxPrompt("开启声音", isSoundOpen, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newHostUrl = dialog.getInputEditText().getText().toString();

                        boolean newIsSoundOpen = dialog.isPromptCheckBoxChecked();
                        if (!newHostUrl.equals(hostUrl)) {
                            mPresenter.saveHostUrl(newHostUrl);
                        }
                        if (!newIsSoundOpen != isSoundOpen) {
                            mPresenter.saveOpenSound(newIsSoundOpen);
                        }
                    }
                })
                .show();
    }

    public void startMainActivity(){
        startActivity(new Intent(this, HomeActivity.class));
    }


    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter(DataManager.getInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
