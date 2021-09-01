package com.common.esimrfid.presenter.home;

import android.util.Log;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvAssetLocContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.inventorytask.AssetUploadParameter;
import com.common.esimrfid.core.bean.inventorytask.EpcBean;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.DateUtils;
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

public class InvAssetsLocPresenter extends BasePresenter<InvAssetLocContract.View> implements InvAssetLocContract.Presenter {
    private String TAG = "HomePresenter";

    public InvAssetsLocPresenter() {
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

    @Override
    public void setOneLessAssetInv(InventoryDetail oneLessAsset, String orderId, String uid) {
        mView.showDialog("数据上传中...");
        ArrayList<AssetUploadParameter> assetUploadParameters = new ArrayList<>();
        AssetUploadParameter assetUploadParameter = new AssetUploadParameter();
        assetUploadParameter.setInvdt_sign(oneLessAsset.getInvdt_sign());
        assetUploadParameter.setInvdt_status(oneLessAsset.getInvdt_status().getCode());
        assetUploadParameter.setInvdt_plus_loc_id(oneLessAsset.getInvdt_plus_loc_id());
        assetUploadParameter.setAst_id(oneLessAsset.getAst_id());
        assetUploadParameters.add(assetUploadParameter);
        addSubscribe(Observable.concat(getLocalHandleOneLessObservable(oneLessAsset), DataManager.getInstance().uploadInvAssets(orderId, uid, assetUploadParameters))
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        //提交到远程的结果处理
                        if (!"local".equals(baseResponse.getMessage())) {
                            if (baseResponse.isSuccess()) {
                                oneLessAsset.setNeedUpload(false);
                            }
                            DbBank.getInstance().getInventoryDetailDao().updateItem(oneLessAsset);
                            ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(oneLessAsset.getInv_id());
                            //获取盘点中已经盘点的资产（包括已盘点和盘亏）
                            List<InventoryDetail> localFinishAssets = DbBank.getInstance().getInventoryDetailDao().findLocalFinishAssets(oneLessAsset.getInv_id());
                            invOrderByInvId.setInv_finish_count(localFinishAssets.size());
                            //获取盘点中待提交的资产
                            List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(oneLessAsset.getInv_id(), true);
                            invOrderByInvId.setInv_notsubmit_count(needSubmitAssets.size());
                            DbBank.getInstance().getResultInventoryOrderDao().updateItem(invOrderByInvId);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        mView.dismissDialog();
                    }
                }));
    }

    //给一个资产添加标记本地处理
    private Observable<BaseResponse> getLocalHandleOneLessObservable(InventoryDetail oneLessAsset) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                } else {
                    DbBank.getInstance().getInventoryDetailDao().updateItem(oneLessAsset);
                    ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(oneLessAsset.getInv_id());
                    //获取盘点中已经盘点的资产（包括已盘点和盘亏）
                    List<InventoryDetail> localFinishAssets = DbBank.getInstance().getInventoryDetailDao().findLocalFinishAssets(oneLessAsset.getInv_id());
                    invOrderByInvId.setInv_finish_count(localFinishAssets.size());
                    //获取盘点中待提交的资产
                    List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(oneLessAsset.getInv_id(), true);
                    invOrderByInvId.setInv_notsubmit_count(needSubmitAssets.size());
                    DbBank.getInstance().getResultInventoryOrderDao().updateItem(invOrderByInvId);
                    BaseResponse localHandleResponse = new BaseResponse();
                    localHandleResponse.setCode("200000");
                    localHandleResponse.setMessage("local");
                    localHandleResponse.setSuccess(true);
                    emitter.onNext(localHandleResponse);
                }
            }
        });
    }

    //获取本地盘点数据
    private Observable<List<InventoryDetail>> getLocalInvDetailsObservable(String orderId, String locId) {
        return Observable.create(new ObservableOnSubscribe<List<InventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<List<InventoryDetail>> emitter) throws Exception {
                List<InventoryDetail> invDetailByInvidAndLocid = DbBank.getInstance().getInventoryDetailDao().findInvDetailByInvidAndLocid(orderId, locId);
                emitter.onNext(invDetailByInvidAndLocid);
            }
        });
    }
}
