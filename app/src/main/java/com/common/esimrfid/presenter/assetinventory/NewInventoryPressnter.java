package com.common.esimrfid.presenter.assetinventory;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetinventory.NewInventoryContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.CreateInvResult;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.InventoryParameter;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;

public class NewInventoryPressnter extends BasePresenter<NewInventoryContract.View> implements NewInventoryContract.Presenter {
    private DataManager mDataManager;
    private String TAG = "NewInventoryPre";

    public NewInventoryPressnter(DataManager dataManager) {
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
    public void getAllCompany() {
        addSubscribe(mDataManager.getAllCompany()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<CompanyBean>>(mView, false) {
                    @Override
                    public void onNext(List<CompanyBean> companyBeans) {
                        mView.handleAllCompany(companyBeans);

                    }
                }));
    }

    @Override
    public void getAllDeparts(String comId) {
        addSubscribe(mDataManager.getAllDeparts(comId)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<DepartmentBean>>(mView, false) {
                    @Override
                    public void onNext(List<DepartmentBean> departmentBeans) {
                        mView.handleAllDeparts(departmentBeans);
                    }
                }));
    }

    @Override
    public void getAllAssetsType() {
        addSubscribe(mDataManager.getAllAssetsType()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<AssetsType>>(mView, false) {
                    @Override
                    public void onNext(List<AssetsType> assetsTypes) {
                        mView.handleAllAssetsType(assetsTypes);
                    }
                }));
    }

    @Override
    public void getAllAssetsLocation() {
        addSubscribe(mDataManager.getAllAssetsLocation()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<AssetsLocation>>(mView, false) {
                    @Override
                    public void onNext(List<AssetsLocation> assetsLocations) {
                        mView.handleAllAssetsLocation(assetsLocations);
                    }
                }));
    }

    @Override
    public void createNewInventory(InventoryParameter invpara) {
        mView.showDialog("loading...");
        addSubscribe(mDataManager.createNewInventory(invpara)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<CreateInvResult>(mView, false) {
                    @Override
                    public void onNext(CreateInvResult createInvResult) {
                        mView.handlecreateNewInventory(createInvResult);
                    }
                }));
    }
}
