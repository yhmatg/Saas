package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.nanhua.invdetailbeans.InventoryDetail;

import java.util.List;

@Dao
public interface InventoryDetailDao extends BaseDao<InventoryDetail> {
    @Query("SELECT * FROM InventoryDetail where inv_id = :invId" )
    public List<InventoryDetail> findLocalInvDetailByInvid(String invId);

    @Query("SELECT * FROM InventoryDetail where inv_id = :invId AND code = 2" )
    public List<InventoryDetail> findLocalInvedDetailByInvid(String invId);
}
