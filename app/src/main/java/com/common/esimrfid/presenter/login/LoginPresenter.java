package com.common.esimrfid.presenter.login;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.login.LoginContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.DbUser;
import com.common.esimrfid.core.bean.nanhua.UserInfo;
import com.common.esimrfid.core.bean.nanhua.UserLoginResponse;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.Md5Util;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @author yhm
 * @date 2018/2/26
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private DataManager mDataManager;

    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }


    @Override
    public void login(final UserInfo userInfo) {
        final String passWord = userInfo.getUser_password();
        final String userName = userInfo.getUser_name();
        if(CommonUtils.isNetworkConnected()){
            userInfo.setUser_password(Md5Util.getMD5(passWord));
            addSubscribe(mDataManager.login(userInfo)
                    .compose(RxUtils.rxSchedulerHelper())
                    .compose(RxUtils.handleResult())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<UserLoginResponse>() {
                        @Override
                        public void accept(UserLoginResponse userLoginResponse) throws Exception {
                            UserInfo sysUser = userLoginResponse.getSysUser();
                            //保存用户信息DbUser到数据库
                            savaUserInfo(sysUser);
                            mDataManager.setUserLoginResponse(userLoginResponse);
                            //保存UserLoginResponse到sp
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<UserLoginResponse>(mView,
                            false) {
                        @Override
                        public void onNext(UserLoginResponse userLoginResponse) {
                            //取消提示框
                            mView.dismissDialog();
                            setLoginAccount(userInfo.getUser_name());
                            setLoginPassword(passWord);
                            setToken(userLoginResponse.getToken());
                            mDataManager.setLoginStatus(true);
                            mView.startMainActivity();
                        }

                        @Override
                        protected void onStart() {
                            super.onStart();
                            //显示提示框
                            mView.showDialog("请稍等...");
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            //取消提示框
                            mView.dismissDialog();
                        }
                    }));
        }else {
            if(passWord.equals(mDataManager.getLoginPassword()) && userName.equals(mDataManager.getLoginAccount())){
                mDataManager.setLoginStatus(true);
                mView.startMainActivity();
            }else {
                ToastUtils.showShort("账号或者密码错误");
            }

        }

    }

    private void savaUserInfo(UserInfo sysUser) {
        DbUser dbUser = new DbUser();
        dbUser.setId(sysUser.getId());
        dbUser.setUser_name(sysUser.getUser_name());
        dbUser.setUser_password(sysUser.getUser_password());
        dbUser.setUser_status(sysUser.getUser_status());
        dbUser.setUser_age(sysUser.getUser_age());
        dbUser.setUser_mobile(sysUser.getUser_mobile());
        dbUser.setUser_email(sysUser.getUser_email());
        dbUser.setTenant_id(sysUser.getTenant_id());
        dbUser.setUpdate_date(new Date(sysUser.getUpdate_date()));
        dbUser.setUser_avatar(sysUser.getUser_avatar());
        dbUser.setUser_empcode(sysUser.getUser_empcode());
        DbBank.getInstance().getDbUserDao().insertItem(dbUser);

    }

    public void saveHostUrl(String hostUrl) {
        mDataManager.saveHostUrl(hostUrl);
    }

    public String getHostUrl() {
        return mDataManager.getHostUrl();
    }

    public void saveOpenSound(boolean isOpen) {
        mDataManager.saveOpenSound(isOpen);
    }

    public boolean getOpenSound() {
        return mDataManager.getOpenSound();
    }


}
