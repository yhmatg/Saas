package com.common.esimrfid.presenter.home;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.WriteTagContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;

import butterknife.OnClick;

public class WriteTagPressnter extends BasePresenter<WriteTagContract.View> implements WriteTagContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public WriteTagPressnter(DataManager dataManager) {
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
}
