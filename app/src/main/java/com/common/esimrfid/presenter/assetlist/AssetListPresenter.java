package com.common.esimrfid.presenter.assetlist;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.assetlist.AssetListContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.assetdetail.AssetFilterParameter;
import com.common.esimrfid.core.bean.inventorytask.AssetsLocation;
import com.common.esimrfid.core.bean.inventorytask.AssetsType;
import com.common.esimrfid.core.bean.inventorytask.CompanyBean;
import com.common.esimrfid.core.bean.inventorytask.DepartmentBean;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListPage;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.room.DbBank;
import com.common.esimrfid.utils.CommonUtils;
import com.common.esimrfid.utils.RxUtils;
import com.common.esimrfid.utils.ToastUtils;
import com.common.esimrfid.widget.BaseObserver;
import com.multilevel.treelist.Node;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class AssetListPresenter extends BasePresenter<AssetListContract.View> implements AssetListContract.Presenter {
    private String TAG = "AssetListPresenter";

    public AssetListPresenter() {
        super();
    }

    //分页
    @Override
    public void fetchPageAssetsInfos(Integer size, Integer page, String patternName, String userRealName, int currentSize, AssetFilterParameter conditions) {
        mView.showDialog("loading...");
        addSubscribe(Observable.concat(getLocalAssetsObservable(size, patternName, currentSize, conditions), DataManager.getInstance().fetchPageAssetsList(size, page, patternName, userRealName, conditions.toString()))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<AssetsListPage>(mView, false) {
                    @Override
                    public void onNext(AssetsListPage assetsInfoPage) {
                        mView.dismissDialog();
                        if (assetsInfoPage.isLocal()) {
                            mView.handlefetchPageAssetsInfos(assetsInfoPage.getList(), assetsInfoPage.getAstCount(), assetsInfoPage.getTotalMoney());
                        } else {
                            if (page <= assetsInfoPage.getPages()) {
                                mView.handlefetchPageAssetsInfos(assetsInfoPage.getList(), assetsInfoPage.getAstCount(), assetsInfoPage.getTotalMoney());
                            } else {
                                mView.handlefetchPageAssetsInfos(new ArrayList<>(), 0, 0);
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissDialog();
                        ToastUtils.showShort(R.string.not_find_asset);
                    }
                }));
    }

    public Observable<BaseResponse<AssetsListPage>> getLocalAssetsObservable(Integer size, String patternName, int currentSize, AssetFilterParameter conditions) {
        Observable<BaseResponse<AssetsListPage>> invOrderObservable = Observable.create(new ObservableOnSubscribe<BaseResponse<AssetsListPage>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<AssetsListPage>> emitter) throws Exception {
                List<Node> nodes = conditions.getmSelectAssetsStatus();
                ArrayList<Integer> astStatus = new ArrayList<>();
                if (nodes != null && nodes.size() > 0) {
                    for (Node node : nodes) {
                        astStatus.add(Integer.parseInt((String) node.getId()));
                    }
                } else {
                    astStatus.add(-1);
                }
                List<AssetsListItemInfo> assetList = DbBank.getInstance().getAssetsAllInfoDao().searchPageLocalAssetListByPara(size, patternName, currentSize, astStatus, EsimAndroidApp.getDataAuthority().getAuth_corp_scope(), EsimAndroidApp.getDataAuthority().getAuth_dept_scope(), EsimAndroidApp.getDataAuthority().getAuth_type_scope().getGeneral(), EsimAndroidApp.getDataAuthority().getAuth_loc_scope().getGeneral());
                double money = DbBank.getInstance().getAssetsAllInfoDao().searchLocalAssetMoneyByPara(patternName, astStatus, EsimAndroidApp.getDataAuthority().getAuth_corp_scope(), EsimAndroidApp.getDataAuthority().getAuth_dept_scope(), EsimAndroidApp.getDataAuthority().getAuth_type_scope().getGeneral(), EsimAndroidApp.getDataAuthority().getAuth_loc_scope().getGeneral());
                int count = DbBank.getInstance().getAssetsAllInfoDao().searchLocalAssetCountByPara(patternName, astStatus, EsimAndroidApp.getDataAuthority().getAuth_corp_scope(), EsimAndroidApp.getDataAuthority().getAuth_dept_scope(), EsimAndroidApp.getDataAuthority().getAuth_type_scope().getGeneral(), EsimAndroidApp.getDataAuthority().getAuth_loc_scope().getGeneral());
                if (CommonUtils.isNetworkConnected()) {
                    emitter.onComplete();
                } else {
                    BaseResponse<AssetsListPage> invOrderResponse = new BaseResponse<>();
                    AssetsListPage assetsInfoPage = new AssetsListPage();
                    assetsInfoPage.setList(assetList);
                    invOrderResponse.setResult(assetsInfoPage);
                    assetsInfoPage.setLocal(true);
                    assetsInfoPage.setAstCount(count);
                    assetsInfoPage.setTotalMoney(money);
                    invOrderResponse.setCode("200000");
                    invOrderResponse.setMessage("成功");
                    invOrderResponse.setSuccess(true);
                    emitter.onNext(invOrderResponse);
                }
            }
        });
        return invOrderObservable;
    }

    @Override
    public void getAllManagerUsers() {
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().getAllManagerUsers()
                    .compose(RxUtils.rxSchedulerHelper())
                    .compose(RxUtils.handleResult())
                    .subscribeWith(new BaseObserver<List<MangerUser>>(mView, false) {
                        @Override
                        public void onNext(List<MangerUser> mangerUsers) {
                            mView.handleAllManagerUsers(mangerUsers);
                        }
                    }));
        }
    }

    @Override
    public void getAllCompany() {
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().getAllAuthCompany(1)
                    .compose(RxUtils.rxSchedulerHelper())
                    .compose(RxUtils.handleResult())
                    .subscribeWith(new BaseObserver<List<CompanyBean>>(mView, false) {
                        @Override
                        public void onNext(List<CompanyBean> companyBeans) {
                            mView.handleAllCompany(companyBeans);

                        }
                    }));
        }
    }

    @Override
    public void getAllDeparts(String comId) {
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().getAllDeparts(comId)
                    .compose(RxUtils.rxSchedulerHelper())
                    .compose(RxUtils.handleResult())
                    .subscribeWith(new BaseObserver<List<DepartmentBean>>(mView, false) {
                        @Override
                        public void onNext(List<DepartmentBean> departmentBeans) {
                            mView.handleAllDeparts(departmentBeans);
                        }
                    }));
        }
    }

    @Override
    public void getAllAssetsType() {
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().getAllAssetsType()
                    .compose(RxUtils.rxSchedulerHelper())
                    .compose(RxUtils.handleResult())
                    .subscribeWith(new BaseObserver<List<AssetsType>>(mView, false) {
                        @Override
                        public void onNext(List<AssetsType> assetsTypes) {
                            mView.handleAllAssetsType(assetsTypes);
                        }
                    }));
        }
    }

    @Override
    public void getAllAssetsLocation() {
        if (CommonUtils.isNetworkConnected()) {
            addSubscribe(DataManager.getInstance().getAllAssetsLocation()
                    .compose(RxUtils.rxSchedulerHelper())
                    .compose(RxUtils.handleResult())
                    .subscribeWith(new BaseObserver<List<AssetsLocation>>(mView, false) {
                        @Override
                        public void onNext(List<AssetsLocation> assetsLocations) {
                            mView.handleAllAssetsLocation(assetsLocations);
                        }
                    }));
        }
    }

}
