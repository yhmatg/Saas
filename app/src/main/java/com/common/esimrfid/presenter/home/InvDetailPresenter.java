package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.InvDetailContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.inventorytask.AssetUploadParameter;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.esimrfid.core.dao.ResultInventoryOrderDao;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class InvDetailPresenter extends BasePresenter<InvDetailContract.View> implements InvDetailContract.Presenter {
    private String TAG = "InvOrderPressnter";

    public InvDetailPresenter() {
        super();
    }

    //网络获取盘点数据
    @Override
    public void fetchAllInvDetails(String orderId, boolean online) {
        mView.showDialog("loading...");
        if (!CommonUtils.isNetworkConnected()) {
            online = false;
        }
        addSubscribe(Observable.concat(getLocalInvDetailsObservable(orderId, online), DataManager.getInstance().fetchAllInvDetails(orderId))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ResultInventoryDetail>() {
                    @Override
                    public void accept(ResultInventoryDetail resultInventoryDetail) throws Exception {
                        if (resultInventoryDetail.getDetailResults() != null) {
                            //保存盘点单资产
                            if(resultInventoryDetail.getInv_status() == 11){
                                ArrayList<String> invIds = new ArrayList<>();
                                invIds.add(resultInventoryDetail.getId());
                                DbBank.getInstance().getInventoryDetailDao().deleteLocalInvDetailByInvids(invIds);
                            }
                            DbBank.getInstance().getInventoryDetailDao().insertItems(resultInventoryDetail.getDetailResults());
                            //add 2020/02/17 start
                            ResultInventoryOrderDao resultInventoryOrderDao = DbBank.getInstance().getResultInventoryOrderDao();
                            ResultInventoryOrder resultInvOrderByInvId = resultInventoryOrderDao.findInvOrderByInvId(orderId);
                            resultInvOrderByInvId.setInv_finish_count(resultInventoryDetail.getInv_finish_count());
                            resultInventoryOrderDao.updateItem(resultInvOrderByInvId);
                            //add 2020/02/17 end
                            //
                        }
                    }
                })
                //本地远程除盘点状态同步 1116
                /*.flatMap(new Function<ResultInventoryDetail, ObservableSource<ResultInventoryDetail>>() {
                    @Override
                    public ObservableSource<ResultInventoryDetail> apply(ResultInventoryDetail resultInventoryDetail) throws Exception {
                        //本地数据库列表
                        List<InventoryDetail> localInvDetailsByInvid = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailByInvid(orderId);
                        //更新出盘点状态的其他盘点数据
                        List<InventoryDetail> finalData = handleLocalAndRemountData(localInvDetailsByInvid, resultInventoryDetail.getDetailResults());
                        DbBank.getInstance().getInventoryDetailDao().insertItems(finalData);
                        resultInventoryDetail.setDetailResults(finalData);
                        return Observable.just(resultInventoryDetail);
                    }
                })*/.observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResultInventoryDetail>(mView, false) {
                    @Override
                    public void onNext(ResultInventoryDetail resultInventoryDetail) {
                        mView.dismissDialog();
                        mView.handleInvDetails(resultInventoryDetail);
                    }
                }));
    }

    //上传盘点未提交的资产
    @Override
    public void uploadLocalInvDetailState(String orderId,String uid) {
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
                        if(baseResponse.isSuccess()){
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

    //本地获取盘点数据
    public Observable<BaseResponse<ResultInventoryDetail>> getLocalInvDetailsObservable(String orderId, final boolean online) {
        Observable<BaseResponse<ResultInventoryDetail>> localInvDetailObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<ResultInventoryDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<ResultInventoryDetail>> emitter) throws Exception {
                List<InventoryDetail> localInvDetailsByInvid = DbBank.getInstance().getInventoryDetailDao().findLocalInvDetailByInvid(orderId);
                ResultInventoryOrder invOrderByInvId = DbBank.getInstance().getResultInventoryOrderDao().findInvOrderByInvId(orderId);
                if (online && (localInvDetailsByInvid.size() == 0 || invOrderByInvId.getInv_status() == 11)) {
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
                    resultInventoryDetail.setInv_status(invOrderByInvId.getInv_status());
                    //20191219 end
                    localInvDetailResponse.setResult(resultInventoryDetail);
                    localInvDetailResponse.setCode("200000");
                    localInvDetailResponse.setMessage("成功");
                    localInvDetailResponse.setSuccess(true);
                    emitter.onNext(localInvDetailResponse);
                }
            }
        });
        return localInvDetailObservable;
    }
    //除盘点状态外，资产的其他属性使用服务器上数据（用户可能修改过）
    public List<InventoryDetail> handleLocalAndRemountData(List<InventoryDetail> local, List<InventoryDetail> remount) {
        ArrayList<InventoryDetail> tempData = new ArrayList<>();
        if (local.size() == 0) {
            tempData.addAll(remount);
        } else {
            HashMap<String, InventoryDetail> hasMap = new HashMap<>();
            for (int i = 0; i < remount.size(); i++) {
                hasMap.put(remount.get(i).getId(), remount.get(i));
            }
            for (int i = 0; i < local.size(); i++) {
                InventoryDetail localDetail = local.get(i);
                InventoryDetail remountDetail = hasMap.get(localDetail.getId());
                if (remountDetail != null) {
                    remountDetail.getInvdt_status().setCode(localDetail.getInvdt_status().getCode());
                    tempData.add(remountDetail);
                }
            }
        }
        return tempData;
    }

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
}
