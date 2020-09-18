package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.nanhua.xfxj.XfInventoryDetail;

import java.util.List;

@Dao
public interface XInventoryDetailDao extends BaseDao<XfInventoryDetail> {
    @Query("SELECT * FROM XfInventoryDetail ")
    public List<XfInventoryDetail> findAllXInventoryDetail();

    @Query("SELECT * FROM XfInventoryDetail WHERE inv_id = :invId")
    public List<XfInventoryDetail> findXInventoryDetail(String invId);

    @Query("SELECT * FROM XfInventoryDetail WHERE inv_id = :invId AND loc_name = :locId")
    public List<XfInventoryDetail> findXInventoryDetail(String invId, String locId);

    @Query("SELECT * FROM XfInventoryDetail WHERE inv_id = :invId AND ast_barcode = :invItemId ")
    public List<XfInventoryDetail> findXInventoryItemDetail(String invId,String invItemId);

    @Query("SELECT * FROM XfInventoryDetail WHERE  ast_barcode = :invItemId ")
    public List<XfInventoryDetail> findXInventoryItemDetail(String invItemId);

    @Query("SELECT * FROM XfInventoryDetail where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%'")
    public List<XfInventoryDetail> findXLocalAssetsByPara(String para);

    @Query("DELETE FROM XfInventoryDetail")
    public void deleteAllData();
}
