package com.common.xfxj.contract.login;


import com.common.xfxj.base.presenter.AbstractPresenter;
import com.common.xfxj.base.view.AbstractView;
import com.common.xfxj.core.bean.nanhua.jsonbeans.UserInfo;

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
        void showLoginWrongLayout();

        void startMainActivity();

        void showUrlSettingDialog();
    }

    interface Presenter extends AbstractPresenter<View> {


        void login(UserInfo userInfo);
    }
}
