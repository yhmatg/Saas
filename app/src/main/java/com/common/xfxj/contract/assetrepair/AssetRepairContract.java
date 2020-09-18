package com.common.xfxj.contract.assetrepair;

import com.common.xfxj.base.presenter.AbstractPresenter;
import com.common.xfxj.base.view.AbstractView;
import com.common.xfxj.core.bean.assetdetail.NewAssetRepairPara;
import com.common.xfxj.core.bean.inventorytask.MangerUser;
import com.common.xfxj.core.bean.nanhua.jsonbeans.BaseResponse;
import com.common.xfxj.core.bean.assetdetail.AssetRepairParameter;
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
