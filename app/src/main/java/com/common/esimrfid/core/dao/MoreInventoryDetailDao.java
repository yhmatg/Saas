package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.MoreInventotyDetail;

import java.util.List;

@Dao
public interface MoreInventoryDetailDao extends BaseDao<MoreInventotyDetail> {
    @Query("SELECT * FROM MoreInventotyDetail where inv_id = :invId")
    public List<InventoryDetail> findLocalInvDetailByInvid(String invId);

    @Query("DELETE FROM MoreInventotyDetail where inv_id in (:invIds)")
    public void deleteLocalInvDetailByInvids(List<String> invIds);

    //根据盘点单id和位置查询对应的盘点条目
    @Query("SELECT * FROM MoreInventotyDetail where inv_id = :invId AND (assetsInfos_loc_id = :locId or invdt_plus_loc_id =:locId)")
    public List<InventoryDetail> findInvDetailByInvidAndLocid(String invId, String locId);

    @Query("SELECT * FROM MoreInventotyDetail where assetsInfos_id = :astId")
    public List<InventoryDetail> findLocalInvDetailByAstId(String astId);

}
