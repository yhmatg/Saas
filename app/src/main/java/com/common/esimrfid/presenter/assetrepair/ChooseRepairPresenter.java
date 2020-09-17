package com.common.esimrfid.presenter.assetrepair;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetrepair.ChooseRepairContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfoPage;
import com.common.esimrfid.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class ChooseRepairPresenter extends BasePresenter<ChooseRepairContract.View> implements ChooseRepairContract.Presenter {
    private String TAG = "WriteTagPresenter";

    public ChooseRepairPresenter() {
        super();
    }

    //未分页
    @Override
    public void getAllAssetsByOpt(String optType,String patternName) {
        mView.showDialog("loading...");
        addSubscribe(DataManager.getInstance().getAllAssetsByOpt(optType,patternName)
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

    //分页
    @Override
    public void getAllAssetsByOpt(Integer size, Integer page, String optType, String patternName) {
        mView.showDialog("loading...");
        addSubscribe(DataManager.getInstance().getAllAssetsByOptPage(optType,size,page,patternName)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<AssetsInfoPage>(mView, false) {
                    @Override
                    public void onNext(AssetsInfoPage assetsInfoPage) {
                        mView.dismissDialog();
                        if(page <= assetsInfoPage.getPages()){
                            mView.handlePageAssetsByOpt(assetsInfoPage.getList());
                        }else {
                            mView.handlePageAssetsByOpt(new ArrayList<>());
                        }
                    }
                    @Override
                    public void onError(Throwable e){
                        mView.dismissDialog();
                        ToastUtils.showShort(R.string.not_find_asset);
                    }
                }));
    }

    @Override
    public void fetchXfAllInvDetails(String para) {
        addSubscribe(getLocalXfAllInvDetails(para)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<List<XfInventoryDetail>>(mView, false) {

                    @Override
                    public void onNext(List<XfInventoryDetail> xfInventoryDetails) {
                        mView.handleXfInvDetails(xfInventoryDetails);
                    }
                }));
    }

    private Observable<List<XfInventoryDetail>> getLocalXfAllInvDetails(String orderId) {
        return  Observable.create(new ObservableOnSubscribe<List<XfInventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<List<XfInventoryDetail>> emitter) throws Exception {
                List<XfInventoryDetail> xInventoryDetail = DbBank.getInstance().getXfInventoryDetailDao().findXLocalAssetsByPara(orderId);
                emitter.onNext(xInventoryDetail);
            }
        });
    }

}
