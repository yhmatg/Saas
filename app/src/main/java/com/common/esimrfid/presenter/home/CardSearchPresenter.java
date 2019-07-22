package com.common.esimrfid.presenter.home;

import com.common.esimrfid.R;
import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.CardSearchContract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.BaseResponse;
import com.common.esimrfid.core.bean.CorpInfo;
import com.common.esimrfid.widget.BaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CardSearchPresenter extends BasePresenter<CardSearchContract.View> implements CardSearchContract.Presenter {
    private DataManager mDataManager;

    public CardSearchPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void findCorpInfoByAll(String corpName, String corpAccount, String cardCode) {
        addSubscribe(mDataManager.findCorpInfoByAll(corpName,corpAccount,cardCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Function<BaseResponse<CorpInfo>, CorpInfo>() {
                        @Override
                        public CorpInfo apply(BaseResponse<CorpInfo> corpInfoBaseResponse) throws Exception {
                            return corpInfoBaseResponse.getResult();
                        }
                    }).subscribeWith(new BaseObserver<CorpInfo>(mView,
                        EsimAndroidApp.getInstance().getString(R.string.search_data_fail)) {
                    @Override
                    public void onNext(CorpInfo corpInfo) {
                        mView.dismissDialog();
                        mView.handleSerachData(corpInfo);
                    }

                    @Override
                    protected void onStart() {
                        super.onStart();
                        //显示提示框
                        mView.showDialog("请稍等...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //取消提示框
                        mView.dismissDialog();
                    }
                }));
    }
}
