package com.common.esimrfid.base.presenter;


import com.common.esimrfid.base.view.AbstractView;

import io.reactivex.disposables.Disposable;


/**
 * Presenter 基类
 *
 * @author yhm
 * @date 2017/11/27
 */

public interface AbstractPresenter<T extends AbstractView> {

    /**
     * 注入View
     *
     * @param view view
     */
    void attachView(T view);

    /**
     * 回收View
     */
    void detachView();

    /**
     * Add rxBing subscribe manager
     *
     * @param disposable Disposable
     */
    void addRxBindingSubscribe(Disposable disposable);


    /**
     * Set login status
     *
     * @param loginStatus login status
     */
    void setLoginStatus(boolean loginStatus);

    /**
     * Get login status
     *
     * @return if is login status
     */
    boolean getLoginStatus();

    /**
     * Get login account
     *
     * @return login account
     */
    String getLoginAccount();

    /**
     * Set login status
     *
     * @param account account
     */
    void setLoginAccount(String account);

    /**
     * Set login password
     *
     * @param password password
     */
    void setLoginPassword(String password);


    //add yhm 20190710 start

    String getLoginPassword();

    String getToken();

    void setToken(String token);


    //add yhm 20190710 end

}
