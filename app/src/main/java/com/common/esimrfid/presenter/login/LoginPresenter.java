package com.common.esimrfid.presenter.login;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.login.LoginContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserLoginResponse;
import com.common.esimrfid.core.http.exception.WrongAccountOrPassException;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.Md5Util;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


/**
 * @author yhm
 * @date 2018/2/26
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    

    public LoginPresenter() {
        super();
    }
    
    @Override
    public void login(final UserInfo userInfo) {
        final String passWord = userInfo.getUser_password();
        final String userName = userInfo.getUser_name();
        if(CommonUtils.isNetworkConnected()){
            userInfo.setUser_password(Md5Util.getMD5(passWord));
            addSubscribe(DataManager.getInstance().login(userInfo)
                    .compose(RxUtils.rxSchedulerHelper())
                    .compose(RxUtils.handleResult())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<UserLoginResponse>() {
                        @Override
                        public void accept(UserLoginResponse userLoginResponse) throws Exception {
                            //保存UserLoginResponse到sp
                            //不同管理员分配的盘点任务不一样，盘点相关的数据需要清除
                            UserLoginResponse localUserLogin = DataManager.getInstance().getUserLoginResponse();
                            if(localUserLogin != null && !userLoginResponse.getUserinfo().getId().equals(localUserLogin.getUserinfo().getId())){
                                DbBank.getInstance().getAssetsAllInfoDao().deleteAllData();
                                DbBank.getInstance().getInventoryDetailDao().deleteAllData();
                                DbBank.getInstance().getResultInventoryOrderDao().deleteAllData();
                                DataManager.getInstance().setLatestSyncTime("0");
                            }
                            DataManager.getInstance().setUserLoginResponse(userLoginResponse);
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
                            DataManager.getInstance().setLoginStatus(true);
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
                            if (e instanceof SocketTimeoutException || e instanceof SSLHandshakeException || e instanceof UnknownHostException
                                    || e instanceof SSLPeerUnverifiedException || e instanceof HttpException) {
                               mView.showUrlSettingDialog();
                            }
                            if (e instanceof WrongAccountOrPassException) {
                                mView.showLoginWrongLayout();
                            }
                        }
                    }));
        }else {
            if(passWord.equals(DataManager.getInstance().getLoginPassword()) && userName.equals(DataManager.getInstance().getLoginAccount())){
                DataManager.getInstance().setLoginStatus(true);
                mView.startMainActivity();
            }else {
                ToastUtils.showShort(R.string.http_error);
            }

        }

    }

    public void saveHostUrl(String hostUrl) {
        DataManager.getInstance().saveHostUrl(hostUrl);
    }

    public String getHostUrl() {
        return DataManager.getInstance().getHostUrl();
    }

}
