package com.common.esimrfid.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.esimrfid.R;
import com.common.esimrfid.base.activity.BaseActivity;
import com.common.esimrfid.contract.login.LoginContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.common.esimrfid.presenter.login.LoginPresenter;
import com.common.esimrfid.ui.home.HomeActivity;
import com.common.esimrfid.utils.ScreenSizeUtils;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author yhm
 * @date 2018/2/26
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.edit_account)
    EditText mAccountEdit;
    @BindView(R.id.edit_password)
    EditText mPasswordEdit;
    @BindView(R.id.btn_login)
    Button mLoginBtn;
    @BindView(R.id.password_invisible)
    ImageView ivEye;
    @BindView(R.id.tv_change_address)
    LinearLayout access_address;
    private String TAG = "LoginActivity";
    private boolean isOpenEye = false;
    Toast toast;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolbar() {
    }

    @Override
    protected void initEventAndData() {
        if (StringUtils.isEmpty(mPresenter.getHostUrl())) {
            showSettingDialog();
        }
        initAccountState();

    }

    private void initAccountState() {
        mAccountEdit.setText(mPresenter.getLoginAccount());
        mAccountEdit.setSelection(mPresenter.getLoginAccount().length());
        mPasswordEdit.setText(mPresenter.getLoginPassword());
        mPasswordEdit.setSelection(mPresenter.getLoginPassword().length());
    }

    @Override
    public void showLoginSuccess() {
    }

    @OnClick({R.id.btn_login, R.id.tv_change_address, R.id.password_invisible})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_change_address:
                showSettingDialog();
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
        if (TextUtils.isEmpty(mPresenter.getHostUrl()) || !(mPresenter.getHostUrl().startsWith("http://") || mPresenter.getHostUrl().startsWith("https://"))) {
            ToastUtils.showShort("请配置正确的服务器URL！");
            return;
        }
        final UserInfo userInfo = new UserInfo();
        userInfo.setUser_name(mAccountEdit.getText().toString());
        userInfo.setUser_password(mPasswordEdit.getText().toString());
        mPresenter.login(userInfo);
    }


    //URL弹出框
    private void showSettingDialog() {
        final String hostUrl = mPresenter.getHostUrl();
        final boolean isSoundOpen = mPresenter.getOpenSound();
        final Dialog dialog = new Dialog(this, R.style.SettingDialog);
        View view = View.inflate(this, R.layout.settingurl_dialog, null);
        TextView cancel = (TextView) view.findViewById(R.id.btn_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.btn_save);
        EditText editText = (EditText) view.findViewById(R.id.edit_url);
//        editText.setHint("http://");
        dialog.setContentView(view);
        editText.setText(hostUrl);
        editText.setSelection(hostUrl.length());
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(getApplication()).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(getApplication()).getScreenWidth() * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newHostUrl = editText.getText().toString();
                if (!(newHostUrl.startsWith("https://") || newHostUrl.startsWith("http://"))) {
                    ToastUtils.showShort(R.string.url_error);
                    return;
                }else if(!newHostUrl.equals(hostUrl)){
                    mPresenter.saveHostUrl(newHostUrl);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
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
    public LoginPresenter initPresenter() {
        return new LoginPresenter(DataManager.getInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startLoginActivity(){

    }
}
