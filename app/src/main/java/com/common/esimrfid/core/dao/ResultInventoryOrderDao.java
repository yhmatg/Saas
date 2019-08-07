package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.nanhua.inventorybeans.ResultInventoryOrder;

import java.util.List;

@Dao
public interface ResultInventoryOrderDao extends BaseDao<ResultInventoryOrder> {
    @Query("SELECT * FROM ResultInventoryOrder")
    public List<ResultInventoryOrder> findInvOrders();

    @Query("SELECT * FROM ResultInventoryOrder WHERE id =:invId")
    public ResultInventoryOrder findInvOrderByInvId(String invId);
}
