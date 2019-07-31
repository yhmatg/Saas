package com.common.esimrfid.presenter.home;

import android.util.Log;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.FragCheckWaitingContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FragCheckWaitingPressnter extends BasePresenter<FragCheckWaitingContract.View> implements FragCheckWaitingContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "InvOrderPressnter";

    public FragCheckWaitingPressnter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }


    @Override
    public void fetchAllIvnOrders(String userId) {
        addSubscribe(mDataManager.fetchAllIvnOrders(userId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Function<BaseResponse<List<ResultInventoryOrder>>, List<ResultInventoryOrder>>() {
            @Override
            public List<ResultInventoryOrder> apply(BaseResponse<List<ResultInventoryOrder>> listBaseResponse) throws Exception {
                return listBaseResponse.getResult();
            }
        }).subscribeWith(new BaseObserver<List<ResultInventoryOrder>>(mView,false) {
                    @Override
                    public void onNext(List<ResultInventoryOrder> resultInventoryOrders) {
                        Log.e("yhmaaaaaaa","resultInventoryOrders===" + resultInventoryOrders.size());
                        mView.showInvOrders(resultInventoryOrders);
                    }
                }));
    }
}
