package com.common.esimrfid.presenter.settings;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.settings.SettingConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.LatestModifyPageAssets;
import com.common.esimrfid.core.dao.AssetsAllInfoDao;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

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
            if(mView != null){
                mView.showDialog("loading...");
            }
        }
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().fetchLatestAssetsPage("0", size, page)
                    .compose(RxUtils.handleResult())
                    .subscribeOn(Schedulers.io())
                    .doOnNext(new Consumer<LatestModifyPageAssets>() {
                        @Override
                        public void accept(LatestModifyPageAssets latestModifyPageAssets) throws Exception {
                            int pageNum = latestModifyPageAssets.getPageNum();
                            int pages = latestModifyPageAssets.getPages();
                            AssetsAllInfoDao assetsAllInfoDao = DbBank.getInstance().getAssetsAllInfoDao();
                            if(pageNum == 1){
                                assetsAllInfoDao.deleteAllData();
                            }
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
                                if(mView != null){
                                    mView.dismissDialog();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            if(mView != null){
                                mView.dismissDialog();
                            }
                        }
                    }));
        }
    }
}
