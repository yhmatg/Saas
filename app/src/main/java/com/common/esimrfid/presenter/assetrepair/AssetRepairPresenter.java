package com.common.esimrfid.presenter.assetrepair;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetrepair.AssetRepairContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.assetdetail.NewAssetRepairPara;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.core.bean.assetdetail.AssetRepairParameter;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;

public class AssetRepairPresenter extends BasePresenter<AssetRepairContract.View> implements AssetRepairContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "NewInventoryPre";

    public AssetRepairPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void getAllEmpUsers() {
        addSubscribe(mDataManager.getAllEmpUsers()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<MangerUser>>(mView, false) {
                    @Override
                    public void onNext(List<MangerUser> mangerUsers) {
                        mView.handleAllEmpUsers(mangerUsers);
                    }
                }));
    }

    @Override
    public void createNewRepairOrder(AssetRepairParameter repairParameter) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.createNewRepairOrder(repairParameter)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleBaseResponse())
                .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
                    @Override
                    public void onNext(BaseResponse createInvResult) {
                        mView.dismissDialog();
                        mView.handleCreateNewRepairOrder(createInvResult);
                    }
                }));
    }

    @Override
    public void createNewRepairOrder(NewAssetRepairPara repariPara) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.createNewRepairOrder(repariPara)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleBaseResponse())
                .subscribeWith(new BaseObserver<BaseResponse>(mView, false) {
                    @Override
                    public void onNext(BaseResponse createInvResult) {
                        mView.dismissDialog();
                        mView.handleCreateNewRepairOrder(createInvResult);
                    }
                }));
    }
}
