package com.common.esimrfid.presenter.home;

import android.util.Log;

import com.common.esimrfid.app.Constants;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.BaseResponse;
import com.common.esimrfid.core.bean.InvDetail;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.core.room.LocalDbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class InvDetailPressnter extends BasePresenter<InvDetailContract.View> implements InvDetailContract.Presenter {
    private DataManager mDataManager;
    String TAG = "InvDetailPressnter";

    public InvDetailPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    //先数据库后网络获取要盘点的数据
    @Override
    public void fetchAllInvDetails(String orderId) {
        //addSubscribe(mDataManager.fetchAllInvDetails(orderId)
        addSubscribe(Observable.concat(createLocalObservable(orderId), mDataManager.fetchAllInvDetails(orderId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BaseResponse<List<InvDetail>>, List<InvDetail>>() {
                    @Override
                    public List<InvDetail> apply(BaseResponse<List<InvDetail>> listBaseResponse) throws Exception {
                        return listBaseResponse.getResult();
                    }
                }).subscribeWith(new BaseObserver<List<InvDetail>>(mView,
                        false) {
                    @Override
                    public void onNext(List<InvDetail> invdtails) {
                        mView.dismissDialog();
                        mView.loadInvDetailsNet(invdtails);
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

    //上传已经盘点过的数据
    @Override
    public void uploadInvDetails(List<InvDetail> invDetails, final String orderId) {
        //addSubscribe(mDataManager.uploadInvDetails(invDetails,orderId)  20190723
        addSubscribe(Observable.concat(createLocalUploadObservable(invDetails), mDataManager.uploadInvDetails(invDetails, orderId))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        if (baseResponse.isOk()) {
                            Log.e(TAG, "doOnNext======" + Thread.currentThread());
                            LocalDbBank.getInstance().getInvDetailDao().deleteInvDetailByOrderId(orderId);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseResponse>(mView,
                        false) {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        Log.e(TAG, "onNext======" + Thread.currentThread());
                        if (baseResponse.isOk()) {
                            mView.dismissDialog();
                            ToastUtils.showShort("提交服务器成功");
                            mView.uploadSuccess();
                        } else if (Constants.CODE_LOCAL_SUC.equals(baseResponse.getState())) {
                            mView.dismissDialog();
                            ToastUtils.showShort("提交本地成功");
                            mView.uploadLocalSucess();
                        }
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

    //所有数据都已经盘点完成
    @Override
    public void finishInvOrder(String orderId) {
        addSubscribe(mDataManager.finishInvOrder(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseResponse>(mView,
                        false) {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isOk()) {
                            mView.dismissDialog();
                            ToastUtils.showShort("操作成功");
                            mView.finishSelf();
                        }
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

    @Override
    public void getLocaCommitedDetails(final String orderId) {
        addSubscribe(Observable.create(new ObservableOnSubscribe<List<InvDetail>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<InvDetail>> emitter) throws Exception {
                        List<InvDetail> localUpload = LocalDbBank.getInstance().getInvDetailDao().findInvDetailByOrderId(orderId);
                        emitter.onNext(localUpload);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new BaseObserver<List<InvDetail>>(mView, false) {
                            @Override
                            public void onNext(List<InvDetail> invDetails) {
                                mView.initLocalCommitDetails(invDetails);
                            }
                        })
        );

    }


    public Observable<BaseResponse<List<InvDetail>>> createLocalObservable(final String orderId) {
        Observable<BaseResponse<List<InvDetail>>> listObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<List<InvDetail>>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<List<InvDetail>>> emitter) throws Exception {
                List<InvDetail> invDetails = DbBank.getInstance().getInvDetailDao().findInvDetailByOrderId(orderId);
                if (invDetails.isEmpty() || CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                } else {
                    BaseResponse<List<InvDetail>> listBaseResponse = new BaseResponse<>();
                    listBaseResponse.setResult(invDetails);
                    listBaseResponse.setState("200000");
                    listBaseResponse.setStatemsg("成功");
                    emitter.onNext(listBaseResponse);
                }
            }
        });
        return listObservable;
    }

    //20190723 start
    public Observable<BaseResponse> createLocalUploadObservable(final List<InvDetail> invDetails) {
        Observable<BaseResponse> localUploade = Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                } else {
                    LocalDbBank.getInstance().getInvDetailDao().insertItems(invDetails);
                    BaseResponse loacaBr = new BaseResponse();
                    loacaBr.setStatemsg("提交本地成功");
                    loacaBr.setState("200001");
                    emitter.onNext(loacaBr);
                }
            }
        });

        return localUploade;
    }
    //20190723 end

}
