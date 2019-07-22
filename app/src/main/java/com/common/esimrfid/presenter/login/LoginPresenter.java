package com.common.esimrfid.presenter.login;

import android.util.Log;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.login.LoginContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.BaseResponse;
import com.common.esimrfid.core.bean.UserInfo;
import com.common.esimrfid.core.bean.UserLoginResponse;
import com.common.esimrfid.utils.Md5Util;
import com.common.esimrfid.widget.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
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
        final String passWord = userInfo.getUserPassword();
        userInfo.setUserPassword(Md5Util.getMD5(passWord));
        addSubscribe(mDataManager.login(userInfo)
                //.compose(RxUtils.rxSchedulerHelper())
                //.compose(RxUtils.handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BaseResponse<UserLoginResponse>, UserLoginResponse>() {

                    @Override
                    public UserLoginResponse apply(BaseResponse<UserLoginResponse> userLoginResponseBaseResponse) throws Exception {
                        Log.e("login error","userLoginResponseBaseResponse=====" + userLoginResponseBaseResponse);
                        return userLoginResponseBaseResponse.getResult();
                    }
                })
                .subscribeWith(new BaseObserver<UserLoginResponse>(mView,
                        EsimAndroidApp.getInstance().getString(R.string.login_fail)) {
                    @Override
                    public void onNext(UserLoginResponse userLoginResponse) {
                        //取消提示框
                        mView.dismissDialog();
                        setLoginAccount(userInfo.getUserName());
                        setLoginPassword(passWord);
                        setToken(userLoginResponse.getToken());
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
    }

    //add yhm 20190708 start
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
    //add yhm 20190708 start


}
