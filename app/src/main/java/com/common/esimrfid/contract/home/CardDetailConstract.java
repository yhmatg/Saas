package com.common.esimrfid.contract.home;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.SignatureCard;

import java.util.List;

public interface CardDetailConstract {
    interface View extends AbstractView {
        void handleSingatureCards(List<SignatureCard> singatureCards);
    }

    interface Presenter extends AbstractPresenter<View> {
        void fetchAllSignatureCards(String corpAccount);
    }
}
