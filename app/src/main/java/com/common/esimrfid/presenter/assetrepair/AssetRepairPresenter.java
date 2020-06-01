package com.common.esimrfid.presenter.assetrepair;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetinventory.NewInventoryContract;
import com.common.esimrfid.contract.assetrepair.AssetRepairContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.CreateInvResult;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.InventoryParameter;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.BaseResponse;
import com.common.esimrfid.ui.assetrepair.AssetRepairParameter;
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
    public void getAllManagerUsers() {
        addSubscribe(mDataManager.getAllManagerUsers()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<MangerUser>>(mView, false) {
                    @Override
                    public void onNext(List<MangerUser> mangerUsers) {
                        mView.handleAllManagerUsers(mangerUsers);
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
}
