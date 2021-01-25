package com.common.esimrfid.presenter.assetrepair;

import com.common.esimrfid.R;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetrepair.ChooseRepairContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListPage;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.ArrayList;

public class ChooseRepairPresenter extends BasePresenter<ChooseRepairContract.View> implements ChooseRepairContract.Presenter {
    private String TAG = "WriteTagPresenter";

    public ChooseRepairPresenter() {
        super();
    }

    //分页
    @Override
    public void fetchPageAssetsInfos(Integer size, Integer page, String patternName, String userRealName, String conditions) {
        mView.showDialog("loading...");
        addSubscribe(DataManager.getInstance().fetchPageAssetsList(size,page,patternName,userRealName,conditions)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<AssetsListPage>(mView, false) {
                    @Override
                    public void onNext(AssetsListPage assetsListPage) {
                        mView.dismissDialog();
                        if(page <= assetsListPage.getPages()){
                            mView.handlePageAssetsByOpt(assetsListPage.getList());
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


}
