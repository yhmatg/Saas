package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvAssetLocContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.MoreInventotyDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.dao.ResultInventoryOrderDao;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.Md5Util;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;
import java.util.HashSet;
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

    //处理一次扫描后的结果
    @Override
    public void handleOneScanned(List<InventoryDetail> oneInvDetails, HashSet<String> oneMoreInvEpcs, String locId, String locName, String invId) {
        addSubscribe(getUpdateInvStatusObservable(oneInvDetails, oneMoreInvEpcs, locId, locName, invId)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<Boolean>(mView, false) {
                    @Override
                    public void onNext(Boolean result) {

                    }
                }));
    }

    @Override
    public void setOneLessAssetInv(InventoryDetail oneLessAsset) {
        addSubscribe(getUpdateOneAssetStatusObservable(oneLessAsset)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<Boolean>(mView, false) {
                    @Override
                    public void onNext(Boolean result) {

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
        return baseResponseObservable;
    }

    //获取本地盘点数据
    public Observable<Boolean> getUpdateInvStatusObservable(List<InventoryDetail> oneInvDetails, HashSet<String> oneMoreInvEpcs, String locId, String locName, String invId) {
        Observable<Boolean> baseResponseObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                DbBank.getInstance().getInventoryDetailDao().updateItems(oneInvDetails);
                List<InventoryDetail> locMoreInvDetail = DbBank.getInstance().getInventoryDetailDao().findMoreInvDetailByInvidAndLocid(invId, locId);
                List<AssetsInfo> localAssetsByEpcs = DbBank.getInstance().getAssetsAllInfoDao().findLocalAssetsByEpcs(oneMoreInvEpcs);
                ArrayList<InventoryDetail> moreInvDetails = new ArrayList<>();
                for (AssetsInfo localAssetsByEpc : localAssetsByEpcs) {
                    if (!isLocalMoreContains(localAssetsByEpc.getId(), locMoreInvDetail)) {
                        localAssetsByEpc.setInvdt_plus_loc_id(locId);
                        localAssetsByEpc.setInvdt_plus_loc_info(new AssetsInfo.InvdtPlusLocInfo());
                        localAssetsByEpc.getInvdt_plus_loc_info().setId(locId);
                        localAssetsByEpc.getInvdt_plus_loc_info().setLoc_name(locName);
                        InventoryDetail inventoryDetail = new InventoryDetail();
                        inventoryDetail.setAssetsInfos(localAssetsByEpc);
                        inventoryDetail.setInv_id(invId);
                        inventoryDetail.setId(Md5Util.getMD5(localAssetsByEpc.getId() + locId + invId));
                        inventoryDetail.setAst_id(localAssetsByEpc.getId());
                        inventoryDetail.setInvdt_status(new InventoryDetail.InvdtStatus());
                        inventoryDetail.getInvdt_status().setCode(2);
                        inventoryDetail.getInvdt_status().setName("盘盈");
                        inventoryDetail.setNeedUpload(true);
                        moreInvDetails.add(inventoryDetail);
                    }
                }
                DbBank.getInstance().getInventoryDetailDao().insertItems(moreInvDetails);

                //更新对应盘点单状态
                ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(invId);
                //获取盘点中已经盘点的资产（包括已盘点和盘亏）
                List<InventoryDetail> localFinishAssets = DbBank.getInstance().getInventoryDetailDao().findLocalFinishAssets(invId);
                invOrderByInvId.setInv_finish_count(localFinishAssets.size());
                //获取盘点中待提交的资产
                List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(invId, true);
                invOrderByInvId.setInv_notsubmit_count(needSubmitAssets.size());
                DbBank.getInstance().getResultInventoryOrderDao().updateItem(invOrderByInvId);
                emitter.onNext(true);
            }
        });
        return baseResponseObservable;
    }

    private boolean isLocalMoreContains(String astId, List<InventoryDetail> locMoreInvDetail) {
        boolean isConstains = false;
        for (InventoryDetail inventoryDetail : locMoreInvDetail) {
            if (astId.equals(inventoryDetail.getAst_id())) {
                isConstains = true;
                break;
            }
        }
        return isConstains;
    }

    public Observable<Boolean> getUpdateOneAssetStatusObservable(InventoryDetail oneLessAsset) {
        Observable<Boolean> baseResponseObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                DbBank.getInstance().getInventoryDetailDao().updateItem(oneLessAsset);
                ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(oneLessAsset.getInv_id());
                //获取盘点中已经盘点的资产（包括已盘点和盘亏）
                List<InventoryDetail> localFinishAssets = DbBank.getInstance().getInventoryDetailDao().findLocalFinishAssets(oneLessAsset.getInv_id());
                invOrderByInvId.setInv_finish_count(localFinishAssets.size());
                //获取盘点中待提交的资产
                List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(oneLessAsset.getInv_id(), true);
                invOrderByInvId.setInv_notsubmit_count(needSubmitAssets.size());
                DbBank.getInstance().getResultInventoryOrderDao().updateItem(invOrderByInvId);
                emitter.onNext(true);
            }
        });
        return baseResponseObservable;
    }
}
