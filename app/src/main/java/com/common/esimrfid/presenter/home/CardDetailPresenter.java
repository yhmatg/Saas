package com.common.esimrfid.presenter.home;

import com.common.esimrfid.base.presenter.BasePresenter;
import com.common.esimrfid.contract.home.CardDetailConstract;
import com.common.esimrfid.core.DataManager;
import com.common.esimrfid.core.bean.BaseResponse;
import com.common.esimrfid.core.bean.SignatureCard;
import com.common.esimrfid.widget.BaseObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CardDetailPresenter extends BasePresenter<CardDetailConstract.View> implements CardDetailConstract.Presenter {
    private DataManager mDataManager;

    public CardDetailPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void fetchAllSignatureCards(String corpAccount) {
        addSubscribe(mDataManager.fetchAllSignatureCards(corpAccount)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Function<BaseResponse<List<SignatureCard>>, List<SignatureCard>>() {
            @Override
            public List<SignatureCard> apply(BaseResponse<List<SignatureCard>> listBaseResponse) throws Exception {
                return  listBaseResponse.getResult();
            }
        })
        .subscribeWith(new BaseObserver<List<SignatureCard>>(mView,false) {
            @Override
            public void onNext(List<SignatureCard> signatureCards) {
                mView.handleSingatureCards(signatureCards);
            }
        }));
    }
}
