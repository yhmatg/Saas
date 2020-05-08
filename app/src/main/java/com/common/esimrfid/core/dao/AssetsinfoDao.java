package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;

import java.util.List;
import java.util.Set;

@Dao
public interface AssetsinfoDao extends BaseDao<AssetsInfo> {

    //根据资产名称，资产编号模糊查询
    @Query("SELECT * FROM AssetsInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%'")
    public List<AssetsInfo> findLocalAssetsByPara(String para);

    @Query("DELETE FROM AssetsInfo")
    public void deleteAllData();

    @Query("SELECT * FROM AssetsInfo where id LIKE :astId")
    public List<AssetsInfo> findLocalAssetsByAstId(String astId);

    @Query("SELECT * FROM AssetsInfo where ast_epc_code in (:epcs)")
    public List<AssetsInfo> findLocalAssetsByEpcs(Set<String> epcs);

}
