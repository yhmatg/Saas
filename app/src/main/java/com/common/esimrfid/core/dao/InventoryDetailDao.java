package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;

import java.lang.reflect.Array;
import java.util.List;

@Dao
public interface InventoryDetailDao extends BaseDao<InventoryDetail> {
    @Query("SELECT * FROM InventoryDetail where inv_id = :invId")
    public List<InventoryDetail> findLocalInvDetailByInvid(String invId);

    @Query("DELETE FROM InventoryDetail where inv_id in (:invIds)")
    public void deleteLocalInvDetailByInvids(List<String> invIds);

    //根据盘点单id和位置查询对应的盘点条目
    @Query("SELECT * FROM InventoryDetail where inv_id = :invId AND ((loc_id = :locId and code != 2) or invdt_plus_loc_id =:locId)")
    public List<InventoryDetail> findInvDetailByInvidAndLocid(String invId, String locId);

    @Query("SELECT * FROM InventoryDetail where inv_id = :invId AND loc_id = :locId AND ast_id = :astId")
    public List<InventoryDetail> findLocalInvDetailByAstId(String invId, String locId, String astId);

    //获取盘点单下某一区域中盘盈的资产
    @Query("SELECT * FROM InventoryDetail where inv_id = :invId AND invdt_plus_loc_id =:locId AND code =2")
    public List<InventoryDetail> findMoreInvDetailByInvidAndLocid(String invId, String locId);

    //获取盘点中已经盘点的资产（包括已盘点和盘亏）
    @Query("SELECT * FROM InventoryDetail where inv_id = :invId AND (code =1 or code =10)")
    public List<InventoryDetail> findLocalFinishAssets(String invId);

    //获取盘点中没有提交的资产（包括已盘点，盘亏，盘盈）
    @Query("SELECT * FROM InventoryDetail where inv_id = :invId AND needUpload = :needSubmit")
    public List<InventoryDetail> findNeedSubmitAssets(String invId, Boolean needSubmit);

    //获取盘点中待盘点的资产
    @Query("SELECT * FROM InventoryDetail where inv_id = :invId AND code = 0 ")
    public List<InventoryDetail> findLocalNotInvhAssets(String invId);
}
