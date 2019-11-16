package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.nanhua.jsonbeans.ResultInventoryOrder;

import java.util.List;

@Dao
public interface ResultInventoryOrderDao extends BaseDao<ResultInventoryOrder> {
    @Query("SELECT * FROM ResultInventoryOrder order by create_date desc")
    public List<ResultInventoryOrder> findInvOrders();

    @Query("SELECT * FROM ResultInventoryOrder WHERE id =:invId")
    public ResultInventoryOrder findInvOrderByInvId(String invId);

    @Query("SELECT * FROM ResultInventoryOrder where opt_status = 1")
    public List<ResultInventoryOrder> findNotSubmitInvOrders();

}
