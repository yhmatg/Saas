package com.common.esimrfid.contract.login;


import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.UserInfo;

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
    }

    interface Presenter extends AbstractPresenter<View> {


        void login(UserInfo userInfo);
    }
}
