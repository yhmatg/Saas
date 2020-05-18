package com.ddcommon.esimrfid.contract.login;


import com.ddcommon.esimrfid.base.presenter.AbstractPresenter;
import com.ddcommon.esimrfid.base.view.AbstractView;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;

/**
 * @author yhm
 * @date 2018/2/26
 */

public interface LoginContract {

    interface View extends AbstractView {

        /**
         * Show login data
         *
         */
        void showLoginSuccess();

        void startMainActivity();

        void showUrlSettingDialog();
    }

    interface Presenter extends AbstractPresenter<View> {


        void login(UserInfo userInfo);
    }
}
