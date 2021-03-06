package com.common.esimrfid.presenter.settings;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.settings.SettingConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyPageAssets;
import com.common.esimrfid.core.dao.AssetsAllInfoDao;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SettingPresenter extends BasePresenter<SettingConstract.View> implements SettingConstract.Presenter {

    public SettingPresenter() {
        super();
    }

    @Override
    public void fetchLatestPageAssets(Integer size, Integer page) {
        if (page == 1) {
            if (mView != null) {
                mView.showUpdateProgressDialog();
            }
        }
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().fetchLatestAssetsPage(DataManager.getInstance().getLatestSyncTime(), size, page)
                    .compose(RxUtils.handleResult())
                    .subscribeOn(Schedulers.io())
                    .doOnNext(new Consumer<LatestModifyPageAssets>() {
                        @Override
                        public void accept(LatestModifyPageAssets latestModifyPageAssets) throws Exception {
                            int pageNum = latestModifyPageAssets.getPageNum();
                            int pages = latestModifyPageAssets.getPages();
                            AssetsAllInfoDao assetsAllInfoDao = DbBank.getInstance().getAssetsAllInfoDao();
                            if (latestModifyPageAssets.getModified() != null && latestModifyPageAssets.getModified().size() > 0) {
                                assetsAllInfoDao.insertItems(latestModifyPageAssets.getModified());
                            }
                            if (latestModifyPageAssets.getRemoved() != null && latestModifyPageAssets.getRemoved().size() > 0) {
                                assetsAllInfoDao.deleteItems(latestModifyPageAssets.getRemoved());
                            }
                            if (pageNum + 1 <= pages) {
                                fetchLatestPageAssets(size, pageNum + 1);
                            } else {
                                DataManager.getInstance().setLatestSyncTime(String.valueOf(System.currentTimeMillis() - 60000));
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<LatestModifyPageAssets>(mView, false) {
                        @Override
                        public void onNext(LatestModifyPageAssets latestModifyPageAssets) {
                            int pageNum = latestModifyPageAssets.getPageNum();
                            int pages = latestModifyPageAssets.getPages();
                            if (pageNum + 1 > pages) {
                                if (mView != null) {
                                    mView.dismissProgressDialog();
                                }
                            } else {
                                mView.updateProgress(pageNum + 1, pages);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            if (mView != null) {
                                mView.dismissProgressDialog();
                            }
                        }
                    }));
        }
    }

    @Override
    public void clearAllData() {
        Observable<BaseResponse> clearDataObservable = Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                DbBank.getInstance().getAssetsAllInfoDao().deleteAllData();
                DbBank.getInstance().getInventoryDetailDao().deleteAllData();
                DbBank.getInstance().getResultInventoryOrderDao().deleteAllData();
                BaseResponse baseResponse = new BaseResponse<>();
                baseResponse.setCode("200000");
                baseResponse.setMessage("??????");
                baseResponse.setSuccess(true);
                emitter.onNext(baseResponse);
            }
        });
        mView.showDialog("???????????????...");
        addSubscribe(clearDataObservable
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        DataManager.getInstance().setLatestSyncTime("0");
                        mView.dismissDialog();
                    }
                }));
    }
}
