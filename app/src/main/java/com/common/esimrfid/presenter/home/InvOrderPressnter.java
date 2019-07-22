package com.common.esimrfid.presenter.home;

import android.util.Log;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.BaseResponse;
import com.common.esimrfid.core.bean.InvDetail;
import com.common.esimrfid.core.bean.InvOrder;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class InvOrderPressnter extends BasePresenter<InvOrderContract.View> implements InvOrderContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public InvOrderPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void fetchAllIvnOrders() {
        //addSubscribe(mDataManager.fetchAllInvOrders()
        addSubscribe(Observable.concat(getLocalInOrderObservable(),mDataManager.fetchAllInvOrders())
                    .subscribeOn(Schedulers.io())
                    .map(new Function<BaseResponse<List<InvOrder>>, List<InvOrder>>() {
                        @Override
                        public List<InvOrder> apply(BaseResponse<List<InvOrder>> listBaseResponse) throws Exception {
                            Log.e(TAG,"map====" + Thread.currentThread());
                            return listBaseResponse.getResult();
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<List<InvOrder>, ObservableSource<List<InvOrder>>>() {
                        @Override
                        public ObservableSource<List<InvOrder>> apply(List<InvOrder> invOrders) throws Exception {
                            Log.e(TAG,"flatMap====" + Thread.currentThread());
                            Log.e(TAG,"invOrders====" + invOrders);
                            DbBank.getInstance().getInvOrderDao().deleteAll();
                            DbBank.getInstance().getInvOrderDao().insertItems(invOrders);
                            return Observable.just(invOrders);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<List<InvOrder>>(mView,
                            EsimAndroidApp.getInstance().getString(R.string.load_data_fail)) {
                        @Override
                        public void onNext(final List<InvOrder> invOrders) {
                            Log.e(TAG,"subscribeWith====" + Thread.currentThread());
                            mView.loadInvOrders(invOrders);

                        }

                    }));
    }

    @Override
    public void downloadInvOrders(final String orderId) {
        addSubscribe(mDataManager.fetchAllInvDetails(orderId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Function<BaseResponse<List<InvDetail>>, List<InvDetail>>() {
                        @Override
                        public List<InvDetail> apply(BaseResponse<List<InvDetail>> listBaseResponse) throws Exception {
                            return listBaseResponse.getResult();
                        }
                    })
                    .flatMap(new Function<List<InvDetail>, ObservableSource<List<InvDetail>>>() {
                        @Override
                        public ObservableSource<List<InvDetail>> apply(List<InvDetail> invDetails) throws Exception {
                            DbBank.getInstance().getInvDetailDao().deleteInvDetailByOrderId(orderId);
                            DbBank.getInstance().getInvDetailDao().insertItems(invDetails);
                            return Observable.just(invDetails);
                        }
                    })
                    .subscribeWith(new BaseObserver<List<InvDetail>>(mView,
                        EsimAndroidApp.getInstance().getString(R.string.load_data_fail)) {
                    @Override
                    public void onNext(final List<InvDetail> invDetails) {
                        Log.e(TAG,"invDetails====" + invDetails);
                        for (InvDetail invDetail : invDetails) {
                            invDetail.setInvId(orderId);
                        }
                        ToastUtils.showShort("下载完成");
                    }

                }));
    }

    public Observable<BaseResponse<List<InvOrder>>> getLocalInOrderObservable(){
        Observable<BaseResponse<List<InvOrder>>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<List<InvOrder>>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<List<InvOrder>>> emitter) throws Exception {
                List<InvOrder> newestOrders = DbBank.getInstance().getInvOrderDao().findNewestOrder();
                if (newestOrders.isEmpty()) {
                    emitter.onComplete();
                    Log.e(TAG,"network get data");
                } else {
                    Log.e(TAG,"newestOrders======" + newestOrders);
                    BaseResponse<List<InvOrder>> invOrderResponse = new BaseResponse<>();
                    invOrderResponse.setResult(newestOrders);
                    invOrderResponse.setState("200000");
                    invOrderResponse.setStatemsg("成功");
                    emitter.onNext(invOrderResponse);
                }
            }
        });

        return invOrderObservable;
    }
}
