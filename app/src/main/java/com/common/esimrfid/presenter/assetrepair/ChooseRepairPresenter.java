package com.common.esimrfid.presenter.assetrepair;

import android.util.Log;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetrepair.ChooseRepairContract;
import com.common.esimrfid.contract.home.WriteTagContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListPage;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class ChooseRepairPresenter extends BasePresenter<ChooseRepairContract.View> implements ChooseRepairContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "WriteTagPresenter";

    public ChooseRepairPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }


    @Override
    public void getAssetsInfoById(String assetsId) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.fetchWriteAssetsInfo(assetsId)
        .compose(RxUtils.rxSchedulerHelper())
        .compose(RxUtils.handleResult())
        .subscribeWith(new BaseObserver<List<AssetsInfo>>(mView, false) {
            @Override
            public void onNext(List<AssetsInfo> assetsInfos) {
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

    @Override
    public void getAllAssetsByOpt(String optType,String patternName) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.getAllAssetsByOpt(optType,patternName)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<AssetsInfo>>(mView, false) {
                    @Override
                    public void onNext(List<AssetsInfo> assetsInfos) {
                        mView.dismissDialog();
                        mView.handleAllAssetsByOpt(assetsInfos);
                    }
                    @Override
                    public void onError(Throwable e){
                        mView.dismissDialog();
                        ToastUtils.showShort(R.string.not_find_asset);
                    }
                }));
    }
}
