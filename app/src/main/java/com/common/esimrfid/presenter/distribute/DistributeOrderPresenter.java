package com.common.esimrfid.presenter.distribute;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.distribute.DistributeOrderContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.DistributeOrderPage;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;

public class DistributeOrderPresenter extends BasePresenter<DistributeOrderContract.View> implements DistributeOrderContract.Presenter {
    @Override
    public void getDistributeOrderPage(String patternName, String conditions, Integer page, Integer size) {
        addSubscribe(DataManager.getInstance().getDistributeOrderPage(patternName, conditions, page, size)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<DistributeOrderPage>(mView, false) {
                    @Override
                    public void onNext(DistributeOrderPage distributeOrderPage) {
                        if (page <= distributeOrderPage.getPages()) {
                            mView.handleDistributeOrderPage(distributeOrderPage.getList());
                        } else {
                            mView.handleDistributeOrderPage(new ArrayList<>());
                        }
                    }
                }));
    }

    @Override
    public void rejectDistributeAsset(String id) {
        addSubscribe(DataManager.getInstance().rejectDistributeAsset(id)
        .compose(RxUtils.rxSchedulerHelper())
        .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
            @Override
            public void onNext(BaseResponse baseResponse) {
                mView.handelRejectDistributeAsset(baseResponse);
            }
        }));
    }
}
