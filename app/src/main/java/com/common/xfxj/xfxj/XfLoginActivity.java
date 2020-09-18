package com.common.xfxj.xfxj;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.common.xfxj.R;
import com.common.xfxj.base.activity.BaseActivity;
import com.common.xfxj.contract.login.LoginContract;
import com.common.xfxj.presenter.login.LoginPresenter;
import com.common.xfxj.ui.home.HomeActivity;
import com.common.xfxj.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author yhm
 * @date 2018/2/26
 */

public class XfLoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.edit_account)
    EditText mAccountEdit;
    @BindView(R.id.edit_password)
    EditText mPasswordEdit;
    @BindView(R.id.btn_login)
    Button mLoginBtn;
    @BindView(R.id.password_invisible)
    ImageView ivEye;
    private String TAG = "LoginActivity";
    private boolean isOpenEye = false;
    Toast toast;
    private String hostUrl;
    private final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    @Override
    protected int getLayoutId() {
        return R.layout.xf_activity_login;
    }

    @Override
    protected void initToolbar() {
    }

    @Override
    protected void initEventAndData() {
        initAccountState();
    }

    private void initAccountState() {
        mAccountEdit.setText(mPresenter.getLoginAccount());
        mAccountEdit.setSelection(mPresenter.getLoginAccount().length());
        mPasswordEdit.setText(mPresenter.getLoginPassword());
        mPasswordEdit.setSelection(mPresenter.getLoginPassword().length());
    }

    @Override
    public void showLoginWrongLayout() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View toast_view = inflater.inflate(R.layout.login_fail_dialog, null);
        toast = new Toast(getApplication());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 20);
        toast.setView(toast_view);
        toast.show();
    }

    @OnClick({R.id.btn_login, R.id.password_invisible})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if(isNormalClick()){
                    login();
                }
                break;
            case R.id.password_invisible:
                settingVisible();
            default:
                break;
        }
    }

    //密码显示
    private void settingVisible() {
        ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenEye) {
                    ivEye.setSelected(false);
                    isOpenEye = false;
                    ivEye.setImageResource(R.drawable.psd_invisible);
                    mPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    ivEye.setSelected(true);
                    isOpenEye = true;
                    ivEye.setImageResource(R.drawable.psd_visible);
                    mPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
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

        if("admin".equals(mAccountEdit.getText().toString()) && "123456".equals(mPasswordEdit.getText().toString())){
            mPresenter.setLoginAccount("admin");
            mPresenter.setLoginPassword("123456");
            startActivity(new Intent(this, XfHomeActivity.class));
            finish();
        }else {
            ToastUtils.showShort("账户或者密码错误！");
        }

    }

    public void startMainActivity() {
        //设置登录成功弹出框
        LayoutInflater inflater = LayoutInflater.from(this);
        View toast_view = inflater.inflate(R.layout.login_success_dialog, null);
        toast = new Toast(getApplication());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 350);
        toast.setView(toast_view);
        toast.show();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void showUrlSettingDialog() {

    }


    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startLoginActivity(){

    }

    public boolean isNormalClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
