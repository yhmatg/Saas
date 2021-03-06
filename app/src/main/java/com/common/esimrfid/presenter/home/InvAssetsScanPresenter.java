package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvAssetLocContract;
import com.common.esimrfid.contract.home.InvAssetScanContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.inventorytask.AssetUploadParameter;
import com.common.esimrfid.core.bean.inventorytask.EpcBean;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.Md5Util;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.StringUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class InvAssetsScanPresenter extends BasePresenter<InvAssetScanContract.View> implements InvAssetScanContract.Presenter {
    private String TAG = "HomePresenter";

    public InvAssetsScanPresenter() {
        super();
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

    //??????????????????????????????
    @Override
    public void handleOneScanned(List<InventoryDetail> oneInvDetails, HashSet<String> oneMoreInvEpcs, String locId, String locName, String invId, String uid) {
        //???????????????????????????????????????
        mView.showDialog("???????????????.....");
        ArrayList<InventoryDetail> mMoreAndUpdateInvDetails = new ArrayList<>();
        addSubscribe(getUpdateInvStatusObservable(oneInvDetails, oneMoreInvEpcs, locId, locName, invId)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<InventoryDetail>, ObservableSource<BaseResponse>>() {

                    @Override
                    public ObservableSource<BaseResponse> apply(List<InventoryDetail> moreInvDetails) throws Exception {
                        mMoreAndUpdateInvDetails.clear();
                        mMoreAndUpdateInvDetails.addAll(oneInvDetails);
                        ArrayList<AssetUploadParameter> assetUploadParameters = new ArrayList<>();
                        for (InventoryDetail inventoryDetail : oneInvDetails) {
                            AssetUploadParameter assetUploadParameter = new AssetUploadParameter();
                            assetUploadParameter.setInvdt_sign(inventoryDetail.getInvdt_sign());
                            assetUploadParameter.setInvdt_status(inventoryDetail.getInvdt_status().getCode());
                            assetUploadParameter.setInvdt_plus_loc_id(inventoryDetail.getInvdt_plus_loc_id());
                            assetUploadParameter.setAst_id(inventoryDetail.getAst_id());
                            assetUploadParameters.add(assetUploadParameter);
                        }
                        for (InventoryDetail moreInvDetail : moreInvDetails) {
                            AssetUploadParameter assetUploadParameter = new AssetUploadParameter();
                            assetUploadParameter.setInvdt_sign(moreInvDetail.getInvdt_sign());
                            assetUploadParameter.setInvdt_status(moreInvDetail.getInvdt_status().getCode());
                            assetUploadParameter.setInvdt_plus_loc_id(moreInvDetail.getInvdt_plus_loc_id());
                            assetUploadParameter.setAst_id(moreInvDetail.getAst_id());
                            if(!StringUtils.isEmpty(moreInvDetail.getLoc_id())){
                                assetUploadParameters.add(assetUploadParameter);
                                mMoreAndUpdateInvDetails.add(moreInvDetail);
                            }
                        }
                        if (CommonUtils.isNetworkConnected()) {
                            return DataManager.getInstance().uploadInvAssets(invId, uid, assetUploadParameters);
                        } else {
                            return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
                                @Override
                                public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                                    BaseResponse localHandleResponse = new BaseResponse();
                                    localHandleResponse.setCode("200000");
                                    localHandleResponse.setMessage("local");
                                    localHandleResponse.setSuccess(true);
                                    emitter.onNext(localHandleResponse);
                                }
                            });
                        }

                    }
                })
                .doOnNext(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        if (!"local".equals(baseResponse.getMessage()) && baseResponse.isSuccess()) {
                            for (InventoryDetail mMoreAndUpdateInvDetail : mMoreAndUpdateInvDetails) {
                                mMoreAndUpdateInvDetail.setNeedUpload(false);
                            }
                        }
                        DbBank.getInstance().getInventoryDetailDao().insertItems(mMoreAndUpdateInvDetails);
                        //???????????????????????????????????????
                        HashMap<String, Integer> moreAndInvedNumMap = new HashMap<>();
                        int oneLocMoreInvCount  = DbBank.getInstance().getInventoryDetailDao().findOneLocMoreInvCount(invId, locId);
                        int oneLocInvedCount  = DbBank.getInstance().getInventoryDetailDao().findOneLocInvedCount(invId, locId);
                        moreAndInvedNumMap.put("more",oneLocMoreInvCount);
                        moreAndInvedNumMap.put("inved",oneLocInvedCount);
                        baseResponse.setResult(moreAndInvedNumMap);
                      /*  //??????????????????????????????????????????????????????????????????????????????????????????
                        ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(invId);
                        //??????????????????????????????????????????????????????????????????
                        List<InventoryDetail> localFinishAssets = DbBank.getInstance().getInventoryDetailDao().findLocalFinishAssets(invId);
                        invOrderByInvId.setInv_finish_count(localFinishAssets.size());
                        //?????????????????????????????????
                        List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(invId, true);
                        invOrderByInvId.setInv_notsubmit_count(needSubmitAssets.size());
                        DbBank.getInstance().getResultInventoryOrderDao().updateItem(invOrderByInvId);*/

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        HashMap<String, Integer> moreAndInvedNum = (HashMap<String, Integer>) baseResponse.getResult();
                        mView.handleMoreInvAndInvedNum(moreAndInvedNum.get("more"),moreAndInvedNum.get("inved"));
                        mView.dismissDialog();
                    }
                }));
    }

    //????????????????????????
    private Observable<List<InventoryDetail>> getLocalInvDetailsObservable(String orderId, String locId) {
        return Observable.create(new ObservableOnSubscribe<List<InventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<List<InventoryDetail>> emitter) throws Exception {
                List<InventoryDetail> invDetailByInvidAndLocid = DbBank.getInstance().getInventoryDetailDao().findInvDetailByInvidAndLocid(orderId, locId);
                emitter.onNext(invDetailByInvidAndLocid);
            }
        });
    }

    //??????????????????????????????
    private Observable<List<InventoryDetail>> getUpdateInvStatusObservable(List<InventoryDetail> oneInvDetails, HashSet<String> oneMoreInvEpcs, String locId, String locName, String invId) {
        return Observable.create(new ObservableOnSubscribe<List<InventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<List<InventoryDetail>> emitter) throws Exception {
                //DbBank.getInstance().getInventoryDetailDao().updateItems(oneInvDetails);
                List<InventoryDetail> locMoreInvDetail = DbBank.getInstance().getInventoryDetailDao().findMoreInvDetailByInvidAndLocid(invId, locId);
                List<InventoryDetail> localAssetsByEpcs = DbBank.getInstance().getAssetsAllInfoDao().findLocalInvdetailByEpcs(oneMoreInvEpcs);
                ArrayList<InventoryDetail> moreInvDetails = new ArrayList<>();
                for (InventoryDetail localAssetsByEpc : localAssetsByEpcs) {
                    if (!isLocalMoreContains(localAssetsByEpc.getId(), locMoreInvDetail)) {
                        localAssetsByEpc.setInvdt_plus_loc_id(locId);
                        localAssetsByEpc.setInvdt_plus_loc_name(locName);
                        localAssetsByEpc.setInv_id(invId);
                        localAssetsByEpc.setInvdt_plus_loc_id(locId);
                        localAssetsByEpc.setAst_id(localAssetsByEpc.getId());
                        localAssetsByEpc.setId(Md5Util.getMD5(localAssetsByEpc.getId() + locId + invId));
                        localAssetsByEpc.setInvdt_status(new InventoryDetail.InvdtStatus());
                        localAssetsByEpc.getInvdt_status().setCode(2);
                        localAssetsByEpc.getInvdt_status().setName("??????");
                        localAssetsByEpc.setNeedUpload(true);
                        moreInvDetails.add(localAssetsByEpc);
                    }
                }
                /*DbBank.getInstance().getInventoryDetailDao().insertItems(moreInvDetails);
                //???????????????????????????
                ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(invId);
                //??????????????????????????????????????????????????????????????????
                List<InventoryDetail> localFinishAssets = DbBank.getInstance().getInventoryDetailDao().findLocalFinishAssets(invId);
                invOrderByInvId.setInv_finish_count(localFinishAssets.size());
                //?????????????????????????????????
                List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(invId, true);
                invOrderByInvId.setInv_notsubmit_count(needSubmitAssets.size());
                DbBank.getInstance().getResultInventoryOrderDao().updateItem(invOrderByInvId);*/
                emitter.onNext(moreInvDetails);
            }
        });
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

}
