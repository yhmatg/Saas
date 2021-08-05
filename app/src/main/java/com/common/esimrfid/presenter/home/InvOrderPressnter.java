package com.common.esimrfid.presenter.home;

import android.util.Log;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvOrderContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.inventorytask.AssetUploadParameter;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryOrderPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
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

public class InvOrderPressnter extends BasePresenter<InvOrderContract.View> implements InvOrderContract.Presenter {
    private String TAG = "InvOrderPressnter";

    public InvOrderPressnter() {
        super();
    }

    //分页获取盘点单列表
    @Override
    public void fetchAllIvnOrdersPage(Integer size, Integer page, int currentSize,String userId, boolean online) {
        mView.showDialog("loading...");
        if (!CommonUtils.isNetworkConnected()) {
            online = false;
        }
        addSubscribe(Observable.concat(getLocalInOrderPageObservable(size, currentSize, online), DataManager.getInstance().fetchAllIvnOrdersPage(size,page,userId))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<InventoryOrderPage>() {
                    @Override
                    public void accept(InventoryOrderPage inventoryOrderPage) throws Exception {
                        List<ResultInventoryOrder> resultInventoryOrders = inventoryOrderPage.getList();
                        DbBank.getInstance().getResultInventoryOrderDao().insertItems(resultInventoryOrders);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<InventoryOrderPage>(mView, false) {
                    @Override
                    public void onNext(InventoryOrderPage inventoryOrderPage) {
                        mView.dismissDialog();
                        if (inventoryOrderPage.isLocal()) {
                            mView.showInvOrders(inventoryOrderPage.getList());
                        } else {
                            if (page <= inventoryOrderPage.getPages()) {
                                mView.showInvOrders(inventoryOrderPage.getList());
                            } else {
                                mView.showInvOrders(new ArrayList<>());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
    }

    //分页
    public Observable<BaseResponse<InventoryOrderPage>> getLocalInOrderPageObservable(Integer size, Integer currentSize, final boolean online) {
        Observable<BaseResponse<InventoryOrderPage>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<InventoryOrderPage>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<InventoryOrderPage>> emitter) throws Exception {
                List<ResultInventoryOrder> newestOrders = DbBank.getInstance().getResultInventoryOrderDao().findInvOrdersPage(size, currentSize);
                if (online) {
                    emitter.onComplete();
                    Log.e(TAG, "network get data");
                } else {
                    Log.e(TAG, "newestOrders======" + newestOrders);
                    BaseResponse<InventoryOrderPage> invOrderResponse = new BaseResponse<>();
                    InventoryOrderPage inventoryOrderPage = new InventoryOrderPage();
                    inventoryOrderPage.setList(newestOrders);
                    inventoryOrderPage.setLocal(true);
                    invOrderResponse.setResult(inventoryOrderPage);
                    invOrderResponse.setCode("200000");
                    invOrderResponse.setMessage("成功");
                    invOrderResponse.setSuccess(true);
                    emitter.onNext(invOrderResponse);
                }
            }
        });
        return invOrderObservable;
    }

    //网络获取盘点数据
    @Override
    public void fetchAllInvDetails(String orderId, boolean online) {
        //mView.showDialog("loading...");
        if (!CommonUtils.isNetworkConnected()) {
            online = false;
        }
        addSubscribe(Observable.concat(getLocalInvDetailsObservable(orderId, online), DataManager.getInstance().fetchAllInvDetails(orderId))
                .compose(RxUtils.handleResult())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<ResultInventoryDetail>() {
                    @Override
                    public void accept(ResultInventoryDetail resultInventoryDetail) throws Exception {
                        if (resultInventoryDetail.getDetailResults() != null) {
                            //功能修改保存服务端已盘，盘盈，盘亏的资产
                            if(CommonUtils.isNetworkConnected()){
                                List<InventoryDetail> detailResults = resultInventoryDetail.getDetailResults();
                                int localInvDetailCount = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailCount(orderId);
                                if(localInvDetailCount > 0){
                                    ArrayList<InventoryDetail> needUpdateDetails = new ArrayList<>();
                                    for (InventoryDetail detailResult : detailResults) {
                                        if(detailResult.getInvdt_status().getCode() != 0){
                                            needUpdateDetails.add(detailResult);
                                        }
                                    }
                                    DbBank.getInstance().getInventoryDetailDao().insertItems(needUpdateDetails);
                                }else {
                                    DbBank.getInstance().getInventoryDetailDao().insertItems(detailResults);
                                }
                            }
                        }
                    }
                })
                //本地远程除盘点状态同步 1116
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResultInventoryDetail>(mView, false) {
                    @Override
                    public void onNext(ResultInventoryDetail resultInventoryDetail) {
                        //mView.dismissDialog();
                        mView.handleInvDetails(resultInventoryDetail);
                    }
                }));
    }

    //本地获取盘点数据
    private Observable<BaseResponse<ResultInventoryDetail>> getLocalInvDetailsObservable(String orderId, final boolean online) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<ResultInventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<ResultInventoryDetail>> emitter) throws Exception {
                List<InventoryDetail> localInvDetailsByInvid = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailByInvid(orderId);
                ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(orderId);
                if (online) {
                    emitter.onComplete();
                } else {
                    BaseResponse<ResultInventoryDetail> localInvDetailResponse = new BaseResponse<>();
                    ResultInventoryDetail resultInventoryDetail = new ResultInventoryDetail();
                    resultInventoryDetail.setDetailResults(localInvDetailsByInvid);
                    //20191219 start 从盘点单条目中获取相关属性信息
                    resultInventoryDetail.setInv_total_count(invOrderByInvId.getInv_total_count());
                    resultInventoryDetail.setInv_finish_count(invOrderByInvId.getInv_finish_count());
                    resultInventoryDetail.setCreate_date(invOrderByInvId.getCreate_date());
                    resultInventoryDetail.setInv_exptfinish_date(invOrderByInvId.getInv_exptfinish_date());
                    resultInventoryDetail.setId(invOrderByInvId.getId());
                    //20191219 end
                    localInvDetailResponse.setResult(resultInventoryDetail);
                    localInvDetailResponse.setCode("200000");
                    localInvDetailResponse.setMessage("local");
                    localInvDetailResponse.setSuccess(true);
                    emitter.onNext(localInvDetailResponse);
                }
            }
        });
    }

    //上传盘点未提交的资产
    @Override
    public void uploadLocalInvDetailState(String orderId, String uid) {
        addSubscribe(getNeedSbumitOneAssetObservable(orderId)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<InventoryDetail>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(List<InventoryDetail> inventoryDetails) throws Exception {
                        ArrayList<AssetUploadParameter> assetUploadParameters = new ArrayList<>();
                        for (InventoryDetail inventoryDetail : inventoryDetails) {
                            AssetUploadParameter assetUploadParameter = new AssetUploadParameter();
                            assetUploadParameter.setInvdt_sign(inventoryDetail.getInvdt_sign());
                            assetUploadParameter.setInvdt_status(inventoryDetail.getInvdt_status().getCode());
                            assetUploadParameter.setInvdt_plus_loc_id(inventoryDetail.getInvdt_plus_loc_id());
                            assetUploadParameter.setAst_id(inventoryDetail.getAst_id());
                            assetUploadParameters.add(assetUploadParameter);
                        }
                        return DataManager.getInstance().uploadInvAssets(orderId,uid,assetUploadParameters);
                    }
                })
                .doOnNext(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                            List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(orderId, true);
                            for (InventoryDetail needSubmitAsset : needSubmitAssets) {
                                needSubmitAsset.setNeedUpload(false);
                            }
                            DbBank.getInstance().getInventoryDetailDao().updateItems(needSubmitAssets);
                            ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(orderId);
                            invOrderByInvId.setInv_notsubmit_count(0);
                            DbBank.getInstance().getResultInventoryOrderDao().updateItem(invOrderByInvId);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        mView.handelUploadResult(baseResponse);
                    }
                }));
    }

    @Override
    public void finishLocalInvDetailStat(String orderId, String uid) {
        addSubscribe(getNeedSbumitOneAssetObservable(orderId)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<InventoryDetail>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(List<InventoryDetail> inventoryDetails) throws Exception {
                        ArrayList<AssetUploadParameter> assetUploadParameters = new ArrayList<>();
                        for (InventoryDetail inventoryDetail : inventoryDetails) {
                            AssetUploadParameter assetUploadParameter = new AssetUploadParameter();
                            assetUploadParameter.setInvdt_sign(inventoryDetail.getInvdt_sign());
                            assetUploadParameter.setInvdt_status(inventoryDetail.getInvdt_status().getCode());
                            assetUploadParameter.setInvdt_plus_loc_id(inventoryDetail.getInvdt_plus_loc_id());
                            assetUploadParameter.setAst_id(inventoryDetail.getAst_id());
                            assetUploadParameters.add(assetUploadParameter);
                        }
                        return DataManager.getInstance().finishInvAssets(orderId,uid,assetUploadParameters);
                    }
                })
                .doOnNext(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                            List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(orderId, true);
                            for (InventoryDetail needSubmitAsset : needSubmitAssets) {
                                needSubmitAsset.setNeedUpload(false);
                            }
                            DbBank.getInstance().getInventoryDetailDao().updateItems(needSubmitAssets);
                            ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(orderId);
                            invOrderByInvId.setInv_notsubmit_count(0);
                            invOrderByInvId.setInv_status(11);
                            DbBank.getInstance().getResultInventoryOrderDao().updateItem(invOrderByInvId);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        mView.handelFinishInvOrder(baseResponse);
                    }
                }));
    }

    @Override
    public void getNotInvAssetLeftStatus(String orderId) {
        addSubscribe(getNotInvAssetObservable(orderId)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<Boolean>(mView, false) {
                    @Override
                    public void onNext(Boolean isAllInved) {
                        mView.handleNotInvAssetLeftStatus(isAllInved);
                    }
                }));
    }

    //获取盘点单中需要提交的资产
    public Observable<List<InventoryDetail>> getNeedSbumitOneAssetObservable(String orderId) {
        Observable<List<InventoryDetail>> baseResponseObservable = Observable.create(new ObservableOnSubscribe<List<InventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<List<InventoryDetail>> emitter) throws Exception {
                List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findNeedSubmitAssets(orderId, true);
                emitter.onNext(needSubmitAssets);
            }
        });
        return baseResponseObservable;
    }


    //获取盘点单中待盘点的资产
    public Observable<Boolean> getNotInvAssetObservable(String orderId) {
        Observable<Boolean> baseResponseObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                boolean isAllInved = false;
                List<InventoryDetail> needSubmitAssets = DbBank.getInstance().getInventoryDetailDao().findLocalNotInvhAssets(orderId);
                List<InventoryDetail> allInvDetails = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailByInvid(orderId);
                if (allInvDetails.size() > 0 && needSubmitAssets.size() == 0) {
                    isAllInved = true;
                }
                emitter.onNext(isAllInved);
            }
        });
        return baseResponseObservable;
    }
}
