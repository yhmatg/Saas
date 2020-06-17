package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvAssetLocContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.dao.ResultInventoryOrderDao;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class InvAssetsLocPresenter extends BasePresenter<InvAssetLocContract.View> implements InvAssetLocContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "HomePresenter";

    public InvAssetsLocPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void fetchAllInvDetails(String orderId, String locId) {
        addSubscribe(getLocalInvDetailsObservable(orderId, locId)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<List<InventoryDetail>>(mView, false) {
                    @Override
                    public void onNext(List<InventoryDetail> resultInventoryDetail) {
                        mView.handleInvDetails(resultInventoryDetail);
                    }
                }));
    }

    //获取本地盘点数据
    public Observable<List<InventoryDetail>> getLocalInvDetailsObservable(String orderId, String locId) {
        Observable<List<InventoryDetail>> baseResponseObservable = Observable.create(new ObservableOnSubscribe<List<InventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<List<InventoryDetail>> emitter) throws Exception {
                List<InventoryDetail> invDetailByInvidAndLocid = DbBank.getInstance().getInventoryDetailDao().findInvDetailByInvidAndLocid(orderId, locId);
                emitter.onNext(invDetailByInvidAndLocid);
            }
        });
        return  baseResponseObservable;
    }
}
