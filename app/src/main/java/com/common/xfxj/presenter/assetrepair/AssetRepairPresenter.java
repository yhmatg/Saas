package com.common.xfxj.presenter.assetrepair;

import com.common.xfxj.base.presenter.BasePresenter;
import com.common.xfxj.contract.assetrepair.AssetRepairContract;
import com.common.xfxj.core.DataManager;
import com.common.xfxj.core.bean.assetdetail.NewAssetRepairPara;
import com.common.xfxj.core.bean.inventorytask.MangerUser;
import com.common.xfxj.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.xfxj.core.bean.assetdetail.AssetRepairParameter;
import com.common.xfxj.utils.RxUtils;
import com.common.xfxj.widget.BaseObserver;
import java.util.List;

public class AssetRepairPresenter extends BasePresenter<AssetRepairContract.View> implements AssetRepairContract.Presenter {
    private String TAG = "NewInventoryPre";

    public AssetRepairPresenter() {
        super();
    }

    @Override
    public void getAllEmpUsers() {
        addSubscribe(DataManager.getInstance().getAllEmpUsers()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<MangerUser>>(mView, false) {
                    @Override
                    public void onNext(List<MangerUser> mangerUsers) {
                        mView.handleAllEmpUsers(mangerUsers);
                    }
                }));
    }

    //未加审批流程前使用
    @Override
    public void createNewRepairOrder(AssetRepairParameter repairParameter) {
        mView.showDialog("loading...");
        addSubscribe(DataManager.getInstance().createNewRepairOrder(repairParameter)
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
        addSubscribe(DataManager.getInstance().createNewRepairOrder(repariPara)
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
