package com.common.xfxj.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.xfxj.core.bean.nanhua.xfxj.XfResultInventoryOrder;

import java.util.List;

@Dao
public interface XResultInventoryOrderDao extends BaseDao<XfResultInventoryOrder> {
    @Query("SELECT * FROM XfResultInventoryOrder ")
    public List<XfResultInventoryOrder> findXfInvOrders();

    @Query("SELECT * FROM XfResultInventoryOrder WHERE id = :invId")
    public List<XfResultInventoryOrder> findXfInvOrdersById(String invId);

    @Query("DELETE FROM XfResultInventoryOrder")
    public void deleteAllData();

}
