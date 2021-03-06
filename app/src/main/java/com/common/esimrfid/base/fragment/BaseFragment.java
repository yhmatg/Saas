package com.common.esimrfid.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.ui.login.LoginActivity;
import com.common.esimrfid.utils.CommonUtils;

import java.util.List;


/**
 * MVP模式的Base fragment
 *
 * @author yhm
 * @date 2017/11/28
 */

public abstract class BaseFragment<T extends BasePresenter> extends AbstractSimpleFragment
        implements AbstractView {

    protected T mPresenter;
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
    }

    public abstract T initPresenter();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null) {
            mPresenter = null;
        }
    }

    @Override
    public void useNightMode(boolean isNightMode) {
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        if (isAdded()) {
            CommonUtils.showSnackMessage(_mActivity, errorMsg);
        }
    }

    @Override
    public void showNormal() {
    }

    @Override
    public void showError() {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void reload() {
    }

    @Override
    public void showCollectSuccess() {
    }

    @Override
    public void showCancelCollectSuccess() {
    }

    @Override
    public void showLoginView() {
    }

    @Override
    public void showLogoutView() {
    }

    @Override
    public void showToast(String message) {
        CommonUtils.showMessage(_mActivity, message);
    }

    @Override
    public void showSnackBar(String message) {
        CommonUtils.showSnackMessage(_mActivity, message);
    }

    public abstract void showInvOrders(List<ResultInventoryOrder> resultInventoryOrders);

    @Override
    public void startLoginActivity(){
        startActivity(new Intent(mContext, LoginActivity.class));
    }

}
