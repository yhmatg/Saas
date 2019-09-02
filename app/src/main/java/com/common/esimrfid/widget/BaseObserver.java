package com.common.esimrfid.widget;

import android.text.TextUtils;
import android.util.Log;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.http.exception.ServerException;
import com.common.esimrfid.core.http.exception.TokenException;
import com.common.esimrfid.utils.LogHelper;
import com.common.esimrfid.utils.ToastUtils;

import java.net.SocketTimeoutException;

import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * @author yhm
 * @date 2017/11/27
 *
 * @param <T>
 */

public abstract class BaseObserver<T> extends ResourceObserver<T> {

    private AbstractView mView;
    private String mErrorMsg;
    private boolean isShowError = true;

    protected BaseObserver(AbstractView view){
        this.mView = view;
    }

    protected BaseObserver(AbstractView view, String errorMsg){
        this.mView = view;
        this.mErrorMsg = errorMsg;
    }

    protected BaseObserver(AbstractView view, boolean isShowError){
        this.mView = view;
        this.isShowError = isShowError;
    }

    protected BaseObserver(AbstractView view, String errorMsg, boolean isShowError){
        this.mView = view;
        this.mErrorMsg = errorMsg;
        this.isShowError = isShowError;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }
        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            mView.showErrorMsg(mErrorMsg);
        } else if (e instanceof ServerException) {
            mView.showErrorMsg(e.toString());
        } else if (e instanceof HttpException) {
                mView.showErrorMsg(EsimAndroidApp.getInstance().getString(R.string.http_error));
        }else if (e instanceof TokenException) {
                ToastUtils.showShort(R.string.token_error);
                mView.startLoginActivity();
        }else if (e instanceof SocketTimeoutException) {
            ToastUtils.showShort(R.string.socket_time_out_error);
        } else {
            mView.showErrorMsg(EsimAndroidApp.getInstance().getString(R.string.unKnown_error));
            LogHelper.d(e.toString());
        }
        if (isShowError) {
            mView.showError();
        }
        Log.e("BaseObserver","Throwable=====" + e);
    }
}
