package com.common.esimrfid.contract.assetrepair;

import com.common.esimrfid.base.presenter.AbstractPresenter;
import com.common.esimrfid.base.view.AbstractView;
import com.common.esimrfid.core.bean.assetdetail.NewAssetRepairPara;
import com.common.esimrfid.core.bean.inventorytask.MangerUser;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.esimrfid.core.bean.assetdetail.AssetRepairParameter;
import java.util.List;

public interface AssetRepairContract {
    interface View extends AbstractView {
        void handleAllEmpUsers(List<MangerUser> mangerUsers);

        void handleCreateNewRepairOrder(BaseResponse baseResponse);
    }

    interface Presenter extends AbstractPresenter<View> {
        void getAllEmpUsers();

        void createNewRepairOrder(AssetRepairParameter repairParameter);

        void createNewRepairOrder(NewAssetRepairPara repariPara);
    }
}
