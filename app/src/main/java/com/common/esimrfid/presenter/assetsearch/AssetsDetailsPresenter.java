package com.common.esimrfid.presenter.assetsearch;

import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetsearch.AssetsDetailsContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.assetdetail.AssetRepair;
import com.common.esimrfid.core.bean.assetdetail.AssetResume;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class AssetsDetailsPresenter extends BasePresenter<AssetsDetailsContract.View> implements AssetsDetailsContract.Presenter {
   
    public AssetsDetailsPresenter() {
        super();
    }

    @Override
    public void getAssetsDetailsById(String astId,String astCode,String whereFrom) {
        mView.showDialog("loading...");
        addSubscribe(Observable.concat(getLocalAssetAllInfoObservable(astId,astCode,whereFrom),"AssetRepairActivity".equals(whereFrom) ? DataManager.getInstance().fetchAssetsInfoWithAuth(astId,astCode) : DataManager.getInstance().fetchAssetsInfo(astId,astCode))
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
    public void getAssetsResumeById(String astId,String astCode) {
        mView.showDialog("loading...");
       addSubscribe(DataManager.getInstance().fetchAssetResume(astId,astCode)
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
    public void getAssetsRepairById(String astid,String astCode) {
        mView.showDialog("loading...");
        addSubscribe(DataManager.getInstance().fetchAssetRepair(astid,astCode)
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
    public void setOneAssetInved(String sign, String invId, String locId, String astId) {
        addSubscribe(getUpdateOneAssetStatusObservable(sign, invId, locId, astId)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<Boolean>(mView, false) {
                    @Override
                    public void onNext(Boolean result) {
                        mView.handleSetOneAssetInved(result);
                    }
                }));
    }


    public Observable<Boolean> getUpdateOneAssetStatusObservable(String sign, String invId, String locId, String astId) {
        Observable<Boolean> baseResponseObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                List<InventoryDetail> localInvDetailByAstId = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailByAstId(invId,locId,astId);
                if(localInvDetailByAstId.size() > 0){
                    InventoryDetail inventoryDetail = localInvDetailByAstId.get(0);
                    inventoryDetail.getInvdt_status().setCode(10);
                    inventoryDetail.setNeedUpload(true);
                    inventoryDetail.setInvdt_sign(sign);
                    DbBank.getInstance().getInventoryDetailDao().updateItem(inventoryDetail);

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
            }
        });
        return baseResponseObservable;
    }


    public Observable<BaseResponse<AssetsAllInfo>> getLocalAssetAllInfoObservable(String astId, String astCode,String whereFrom) {
        Observable<BaseResponse<AssetsAllInfo>> baseResponseObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<AssetsAllInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<AssetsAllInfo>> emitter) throws Exception {
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                } else {
                    List<AssetsAllInfo> localAssetsByAstIdOrEpc = new ArrayList<>();
                    if("AssetRepairActivity".equals(whereFrom)){
                        localAssetsByAstIdOrEpc = DbBank.getInstance().getAssetsAllInfoDao().findLocalAssetsByAstIdOrEpc(astId, astCode, EsimAndroidApp.getDataAuthority().getAuth_corp_scope(),EsimAndroidApp.getDataAuthority().getAuth_dept_scope(),EsimAndroidApp.getDataAuthority().getAuth_type_scope(),EsimAndroidApp.getDataAuthority().getAuth_loc_scope());
                    }else {
                        localAssetsByAstIdOrEpc = DbBank.getInstance().getAssetsAllInfoDao().findLocalAssetsByAstIdOrEpc(astId, astCode);
                    }
                    if(localAssetsByAstIdOrEpc.size() > 0){
                        BaseResponse<AssetsAllInfo> baseResponse = new BaseResponse<>();
                        baseResponse.setResult(localAssetsByAstIdOrEpc.get(0));
                        baseResponse.setCode("200000");
                        baseResponse.setMessage("成功");
                        baseResponse.setSuccess(true);
                        emitter.onNext(baseResponse);
                    }else {
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
        return baseResponseObservable;
    }

    @Override
    public void getXAssetsDetailsById(String invId,String invItemId) {
        addSubscribe(getLocalXfInvDetails(invId,invItemId)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<XfInventoryDetail>(mView, false) {

                    @Override
                    public void onNext(XfInventoryDetail xfInventoryDetails) {
                        mView.handleInvItemDetail(xfInventoryDetails);
                    }
                }));
    }

    private Observable<XfInventoryDetail> getLocalXfInvDetails(String invId,String invItemId) {
        return Observable.create(new ObservableOnSubscribe<XfInventoryDetail>() {
            @Override
            public void subscribe(ObservableEmitter<XfInventoryDetail> emitter) throws Exception {
                List<XfInventoryDetail> xInventoryDetail = new ArrayList<>();
                if(invId == null){
                    xInventoryDetail = DbBank.getInstance().getXfInventoryDetailDao().findXInventoryItemDetail(invItemId);
                }else {
                    xInventoryDetail = DbBank.getInstance().getXfInventoryDetailDao().findXInventoryItemDetail(invId,invItemId);
                }
                if(xInventoryDetail.size() > 0){
                    emitter.onNext(xInventoryDetail.get(0));
                }else {
                    emitter.onNext(null);
                }

            }
        });
    }
}
