package com.common.esimrfid.presenter.assetsearch;

import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.inventorytask.AssetUploadParameter;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AssetsDetailsPresenter extends BasePresenter<AssetsDetailsContract.View> implements AssetsDetailsContract.Presenter {

    public AssetsDetailsPresenter() {
        super();
    }

    @Override
    public void getAssetsDetailsById(String astId, String astCode, String whereFrom) {
        mView.showDialog("loading...");
        addSubscribe(Observable.concat(getLocalAssetAllInfoObservable(astId, astCode, whereFrom), "AssetRepairActivity".equals(whereFrom) ? DataManager.getInstance().fetchAssetsInfoWithAuth(astId, astCode) : DataManager.getInstance().fetchAssetsInfo(astId, astCode))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<AssetsAllInfo>(mView, false) {
                    @Override
                    public void onNext(AssetsAllInfo assetsAllInfo) {
                        mView.dismissDialog();
                        mView.handleAssetsDetails(assetsAllInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.handleAssetsNoDetail();
                    }
                }));
    }

    @Override
    public void getAssetsResumeById(String astId, String astCode) {
        mView.showDialog("loading...");
        addSubscribe(DataManager.getInstance().fetchAssetResume(astId, astCode)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<AssetResume>>(mView, false) {
                    @Override
                    public void onNext(List<AssetResume> assetResumes) {
                        mView.dismissDialog();
                        mView.handleAssetsResume(assetResumes);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    @Override
    public void getAssetsRepairById(String astid, String astCode) {
        mView.showDialog("loading...");
        addSubscribe(DataManager.getInstance().fetchAssetRepair(astid, astCode)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<AssetRepair>>(mView, false) {
                    @Override
                    public void onNext(List<AssetRepair> assetRepairs) {
                        mView.dismissDialog();
                        mView.handleAssetsRepair(assetRepairs);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    @Override
    public void setOneAssetInved(String sign, String invId, String locId, String astId, String uid) {
        ArrayList<InventoryDetail> invedDetails = new ArrayList<>();
        addSubscribe(getUpdateOneAssetStatusObservable(sign, invId, locId, astId)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<InventoryDetail, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(InventoryDetail inventoryDetail) throws Exception {
                        invedDetails.clear();
                        invedDetails.add(inventoryDetail);
                        if (CommonUtils.isNetworkConnected()) {
                            ArrayList<AssetUploadParameter> assetUploadParameters = new ArrayList<>();
                            AssetUploadParameter assetUploadParameter = new AssetUploadParameter();
                            assetUploadParameter.setInvdt_sign(inventoryDetail.getInvdt_sign());
                            assetUploadParameter.setInvdt_status(inventoryDetail.getInvdt_status().getCode());
                            assetUploadParameter.setInvdt_plus_loc_id(inventoryDetail.getInvdt_plus_loc_id());
                            assetUploadParameter.setAst_id(inventoryDetail.getAst_id());
                            assetUploadParameters.add(assetUploadParameter);
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
                        if (invedDetails.size() > 0) {
                            InventoryDetail invedDetail = invedDetails.get(0);
                            if (!"local".equals(baseResponse.getMessage()) && baseResponse.isSuccess()) {
                                invedDetail.setNeedUpload(false);
                            }
                            DbBank.getInstance().getInventoryDetailDao().updateItem(invedDetail);
                            ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(invId);
                            //获取盘点中已经盘点的资产（包括已盘点和盘亏）
                            List<InventoryDetail> localFinishAssets = DbBank.getInstance().getInventoryDetailDao().findLocalFinishAssets(invId);
                            invOrderByInvId.setInv_finish_count(localFinishAssets.size());
                            //获取盘点中待提交的资产
                            List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(invId, true);
                            invOrderByInvId.setInv_notsubmit_count(needSubmitAssets.size());
                            DbBank.getInstance().getResultInventoryOrderDao().updateItem(invOrderByInvId);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
                    @Override
                    public void onNext(BaseResponse result) {
                        mView.handleSetOneAssetInved(result);
                    }
                }));
    }


    private Observable<InventoryDetail> getUpdateOneAssetStatusObservable(String sign, String invId, String locId, String astId) {
        return Observable.create(new ObservableOnSubscribe<InventoryDetail>() {
            @Override
            public void subscribe(ObservableEmitter<InventoryDetail> emitter) throws Exception {
                List<InventoryDetail> localInvDetailByAstId = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailByAstId(invId, locId, astId);
                if (localInvDetailByAstId.size() > 0) {
                    InventoryDetail inventoryDetail = localInvDetailByAstId.get(0);
                    inventoryDetail.getInvdt_status().setCode(10);
                    inventoryDetail.setNeedUpload(true);
                    inventoryDetail.setInvdt_sign(sign);
                  /*  DbBank.getInstance().getInventoryDetailDao().updateItem(inventoryDetail);

                    ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(invId);
                    //获取盘点中已经盘点的资产（包括已盘点和盘亏）
                    List<InventoryDetail> localFinishAssets = DbBank.getInstance().getInventoryDetailDao().findLocalFinishAssets(invId);
                    invOrderByInvId.setInv_finish_count(localFinishAssets.size());
                    //获取盘点中待提交的资产
                    List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(invId, true);
                    invOrderByInvId.setInv_notsubmit_count(needSubmitAssets.size());
                    DbBank.getInstance().getResultInventoryOrderDao().updateItem(invOrderByInvId);*/
                    emitter.onNext(inventoryDetail);
                }
            }
        });
    }


    private Observable<BaseResponse<AssetsAllInfo>> getLocalAssetAllInfoObservable(String astId, String astCode, String whereFrom) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<AssetsAllInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<AssetsAllInfo>> emitter) throws Exception {
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                } else {
                    List<AssetsAllInfo> localAssetsByAstIdOrEpc = new ArrayList<>();
                    if ("AssetRepairActivity".equals(whereFrom)) {
                        localAssetsByAstIdOrEpc = DbBank.getInstance().getAssetsAllInfoDao().findLocalAssetsByAstIdOrEpc(astId, astCode, EsimAndroidApp.getDataAuthority().getAuth_corp_scope(), EsimAndroidApp.getDataAuthority().getAuth_dept_scope(), EsimAndroidApp.getDataAuthority().getAuth_type_scope().getGeneral(), EsimAndroidApp.getDataAuthority().getAuth_loc_scope().getGeneral());
                    } else {
                        localAssetsByAstIdOrEpc = DbBank.getInstance().getAssetsAllInfoDao().findLocalAssetsByAstIdOrEpc(astId, astCode);
                    }
                    if (localAssetsByAstIdOrEpc.size() > 0) {
                        BaseResponse<AssetsAllInfo> baseResponse = new BaseResponse<>();
                        baseResponse.setResult(localAssetsByAstIdOrEpc.get(0));
                        baseResponse.setCode("200000");
                        baseResponse.setMessage("成功");
                        baseResponse.setSuccess(true);
                        emitter.onNext(baseResponse);
                    } else {
                        BaseResponse<AssetsAllInfo> baseResponse = new BaseResponse<>();
                        baseResponse.setResult(null);
                        baseResponse.setCode("200000");
                        baseResponse.setMessage("成功");
                        baseResponse.setSuccess(true);
                        emitter.onNext(baseResponse);
                    }
                }

            }
        });
    }
}
