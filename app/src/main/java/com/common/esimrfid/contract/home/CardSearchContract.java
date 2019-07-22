package com.common.esimrfid.contract.home;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.CorpInfo;

public interface CardSearchContract {
    interface View extends AbstractView {
        void handleSerachData(CorpInfo corpInfo);
    }

    interface Presenter extends AbstractPresenter<View> {

        void findCorpInfoByAll(String corpName, String corpAccount, String cardCode);

    }
}
