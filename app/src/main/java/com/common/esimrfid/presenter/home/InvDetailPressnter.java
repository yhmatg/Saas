package com.common.esimrfid.presenter.home;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.BaseResponse;
import com.common.esimrfid.core.bean.InvDetail;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class InvDetailPressnter extends BasePresenter<InvDetailContract.View> implements InvDetailContract.Presenter {
    private DataManager mDataManager;

    public InvDetailPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    //先数据库后网络获取数据
    @Override
    public void fetchAllInvDetails(String orderId) {
        //addSubscribe(mDataManager.fetchAllInvDetails(orderId)
        addSubscribe(Observable.concat(createLocalObservable(orderId),mDataManager.fetchAllInvDetails(orderId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Function<BaseResponse<List<InvDetail>>, List<InvDetail>>() {
                        @Override
                        public List<InvDetail> apply(BaseResponse<List<InvDetail>> listBaseResponse) throws Exception {
                            return listBaseResponse.getResult();
                        }
                    }).subscribeWith(new BaseObserver<List<InvDetail>>(mView,
                        EsimAndroidApp.getInstance().getString(R.string.load_data_fail)) {
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

    @Override
    public void uploadInvDetails(List<InvDetail> invDetails, String orderId) {
        addSubscribe(mDataManager.uploadInvDetails(invDetails,orderId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<BaseResponse>(mView,
                            EsimAndroidApp.getInstance().getString(R.string.load_data_fail)) {
                        @Override
                        public void onNext(BaseResponse baseResponse) {
                            if (baseResponse.isOk()) {
                                mView.dismissDialog();
                                ToastUtils.showShort("提交成功");
                                mView.uploadSuccess();
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
    public void finishInvOrder(String orderId) {
        addSubscribe(mDataManager.finishInvOrder(orderId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<BaseResponse>(mView,
                            EsimAndroidApp.getInstance().getString(R.string.load_data_fail)) {
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


    public Observable<BaseResponse<List<InvDetail>>> createLocalObservable(final String orderId){
        Observable<BaseResponse<List<InvDetail>>> listObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<List<InvDetail>>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<List<InvDetail>>> emitter) throws Exception {
                List<InvDetail> invDetails = DbBank.getInstance().getInvDetailDao().findInvDetailByOrderId(orderId);
                if (invDetails.isEmpty()) {
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
}
