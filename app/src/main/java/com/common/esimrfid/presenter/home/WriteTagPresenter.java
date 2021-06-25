package com.common.esimrfid.presenter.home;

import android.util.Log;
import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.WriteTagContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfoPage;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class WriteTagPresenter extends BasePresenter<WriteTagContract.View> implements WriteTagContract.Presenter {
    private String TAG = "WriteTagPresenter";

    public WriteTagPresenter() {
        super();
    }


    public Observable<BaseResponse<List<AssetsInfo>>> getLocalAssetsObservable(String para) {
        Observable<BaseResponse<List<AssetsInfo>>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<List<AssetsInfo>>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<List<AssetsInfo>>> emitter) throws Exception {
                List<AssetsInfo> newestOrders = DbBank.getInstance().getAssetsAllInfoDao().findLocalAssetsByPara(para,EsimAndroidApp.getDataAuthority().getAuth_corp_scope(),EsimAndroidApp.getDataAuthority().getAuth_dept_scope(),EsimAndroidApp.getDataAuthority().getAuth_type_scope().getGeneral(),EsimAndroidApp.getDataAuthority().getAuth_loc_scope().getGeneral());
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

    //分页
    @Override
    public void fetchPageAssetsInfos(Integer size, Integer page, String patternName, int currentSize) {
        mView.showDialog("loading...");
        addSubscribe(Observable.concat(getLocalAssetsObservable(size, patternName, currentSize), DataManager.getInstance().fetchPageAssetsInfos(size, page, patternName))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<AssetsInfoPage>(mView, false) {
                    @Override
                    public void onNext(AssetsInfoPage assetsInfoPage) {
                        mView.dismissDialog();
                        if(assetsInfoPage.isLocal()){
                            mView.handlefetchPageAssetsInfos(assetsInfoPage.getList());
                        }else {
                            if(page <= assetsInfoPage.getPages()){
                                mView.handlefetchPageAssetsInfos(assetsInfoPage.getList());
                            }else {
                                mView.handlefetchPageAssetsInfos(new ArrayList<>());
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissDialog();
                        ToastUtils.showShort(R.string.not_find_asset);
                    }
                }));
    }

    public Observable<BaseResponse<AssetsInfoPage>> getLocalAssetsObservable(Integer size, String patternName, int currentSize) {
        Observable<BaseResponse<AssetsInfoPage>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<AssetsInfoPage>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<AssetsInfoPage>> emitter) throws Exception {
                List<AssetsInfo> newestOrders = DbBank.getInstance().getAssetsAllInfoDao().findPageLocalAssetsByPara(size, patternName, currentSize, EsimAndroidApp.getDataAuthority().getAuth_corp_scope(),EsimAndroidApp.getDataAuthority().getAuth_dept_scope(),EsimAndroidApp.getDataAuthority().getAuth_type_scope().getGeneral(),EsimAndroidApp.getDataAuthority().getAuth_loc_scope().getGeneral());
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                    Log.e(TAG, "network get data");
                } else {
                    Log.e(TAG, "newestOrders======" + newestOrders);
                    BaseResponse<AssetsInfoPage> invOrderResponse = new BaseResponse<>();
                    AssetsInfoPage assetsInfoPage = new AssetsInfoPage();
                    assetsInfoPage.setList(newestOrders);
                    invOrderResponse.setResult(assetsInfoPage);
                    assetsInfoPage.setLocal(true);
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
