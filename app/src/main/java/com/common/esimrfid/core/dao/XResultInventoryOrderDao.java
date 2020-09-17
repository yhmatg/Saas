package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.nanhua.xfxj.XfResultInventoryOrder;

import java.util.List;

@Dao
public interface XResultInventoryOrderDao extends BaseDao<XfResultInventoryOrder> {
    @Query("SELECT * FROM XfResultInventoryOrder ")
    public List<XfResultInventoryOrder> findXfInvOrders();

    @Query("DELETE FROM XfResultInventoryOrder")
    public void deleteAllData();

}
