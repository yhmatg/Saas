package com.ddcommon.esimrfid.presenter.home;

import android.util.Log;

import com.ddcommon.esimrfid.R;
import com.ddcommon.esimrfid.base.presenter.BasePresenter;
import com.ddcommon.esimrfid.contract.home.WriteTagContract;
import com.ddcommon.esimrfid.core.DataManager;
import com.ddcommon.esimrfid.core.bean.nanhua.BaseResponse;
import com.ddcommon.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.ddcommon.esimrfid.core.room.DbBank;
import com.ddcommon.esimrfid.utils.CommonUtils;
import com.ddcommon.esimrfid.utils.RxUtils;
import com.ddcommon.esimrfid.utils.StringUtils;
import com.ddcommon.esimrfid.utils.ToastUtils;
import com.ddcommon.esimrfid.widget.BaseObserver;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class WriteTagPresenter extends BasePresenter<WriteTagContract.View> implements WriteTagContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "WriteTagPresenter";

    public WriteTagPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }


    @Override
    public void getAssetsInfoById(String assetsId) {
        mView.showDialog("loading...");
        //addSubscribe(mDataManager.fetchWriteAssetsInfo(assetsId)
        addSubscribe(Observable.concat(getLocalAssetsObservable(assetsId),mDataManager.fetchWriteAssetsInfo(assetsId))
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleResult())
        .subscribeWith(new BaseObserver<List<AssetsInfo>>(mView, false) {
            @Override
            public void onNext(List<AssetsInfo> assetsInfos) {
                if(StringUtils.isEmpty(assetsId) && CommonUtils.isNetworkConnected()){
                    DbBank.getInstance().getAssetsinfoDao().deleteAllData();
                    DbBank.getInstance().getAssetsinfoDao().insertItems(assetsInfos);
                }
                mView.dismissDialog();
                mView.handleAssetsById(assetsInfos);
            }
            @Override
            public void onError(Throwable e){
                mView.dismissDialog();
                ToastUtils.showShort(R.string.not_find_asset);
            }
        }));
    }

    public Observable<BaseResponse<List<AssetsInfo>>> getLocalAssetsObservable(String para) {
        Observable<BaseResponse<List<AssetsInfo>>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<List<AssetsInfo>>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<List<AssetsInfo>>> emitter) throws Exception {
                List<AssetsInfo> newestOrders = DbBank.getInstance().getAssetsinfoDao().findLocalAssetsByPara(para);
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                    Log.e(TAG, "network get data");
                } else {
                    Log.e(TAG, "newestOrders======" + newestOrders);
                    BaseResponse<List<AssetsInfo>> invOrderResponse = new BaseResponse<>();
                    invOrderResponse.setResult(newestOrders);
                    invOrderResponse.setCode("200000");
                    invOrderResponse.setMessage("成功");
                    invOrderResponse.setSuccess(true);
                    emitter.onNext(invOrderResponse);
                }
            }
        });
        return invOrderObservable;
    }
}
