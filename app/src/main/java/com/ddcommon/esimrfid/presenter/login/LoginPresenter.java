package com.ddcommon.esimrfid.presenter.login;

import com.ddcommon.esimrfid.R;
import com.ddcommon.esimrfid.base.presenter.BasePresenter;
import com.ddcommon.esimrfid.contract.login.LoginContract;
import com.ddcommon.esimrfid.core.DataManager;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.ddcommon.esimrfid.utils.CommonUtils;
import com.ddcommon.esimrfid.utils.Md5Util;
import com.ddcommon.esimrfid.utils.RxUtils;
import com.ddcommon.esimrfid.utils.ToastUtils;
import com.ddcommon.esimrfid.widget.BaseObserver;

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
                            //保存UserLoginResponse到sp
                            mDataManager.setUserLoginResponse(userLoginResponse);
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
                ToastUtils.showShort(R.string.http_error);
            }

        }

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