package com.common.esimrfid.presenter.distribute;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.distribute.DistOrdDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.assetdetail.AssetFilterParameter;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DistributeOrderDetail;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;

public class DistOrderDetailPresenter extends BasePresenter<DistOrdDetailContract.View> implements DistOrdDetailContract.Presenter {
    @Override
    public void getDistOrdDetail(String id) {
        addSubscribe(DataManager.getInstance().getDistributeOrderDetail(id)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<DistributeOrderDetail>(mView, false) {
                    @Override
                    public void onNext(DistributeOrderDetail distributeOrderDetail) {
                        mView.handleGetDistOrdDetail(distributeOrderDetail);
                    }
                }));
    }

    @Override
    public void fetchPageAssetsInfos(Integer size, Integer page, String patternName, String userRealName, int currentSize, AssetFilterParameter conditions) {
        addSubscribe(DataManager.getInstance().fetchPageAssetsList(size, page, patternName, userRealName, conditions.toString())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<AssetsListPage>(mView, false) {
                    @Override
                    public void onNext(AssetsListPage assetsInfoPage) {
                        mView.dismissDialog();
                        if (assetsInfoPage.isLocal()) {
                            mView.handleFetchPageAssetsInfos(assetsInfoPage.getList());
                        } else {
                            if (page <= assetsInfoPage.getPages()) {
                                mView.handleFetchPageAssetsInfos(assetsInfoPage.getList());
                            } else {
                                mView.handleFetchPageAssetsInfos(new ArrayList<>());
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

    @Override
    public void confirmDistributeAsset(DistributeOrderDetail distributeOrderDetail) {
        addSubscribe(DataManager.getInstance().confirmDistributeAsset(distributeOrderDetail)
        .compose(RxUtils.rxSchedulerHelper())
        .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
            @Override
            public void onNext(BaseResponse baseResponse) {
                mView.handleConfirmDistributeAsset(baseResponse);
            }
        }));
    }
}
