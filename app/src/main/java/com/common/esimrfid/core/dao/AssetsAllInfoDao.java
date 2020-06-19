package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;

import java.util.List;
import java.util.Set;

@Dao
public interface AssetsAllInfoDao extends BaseDao<AssetsAllInfo> {

    //根据资产名称，资产编号模糊查询(精简)
    @Query("SELECT * FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%'")
    public List<AssetsInfo> findLocalAssetsByPara(String para);

    @Query("DELETE FROM AssetsAllInfo")
    public void deleteAllData();

    @Query("SELECT * FROM AssetsAllInfo where ast_epc_code in (:epcs)")
    public List<AssetsInfo> findLocalAssetsByEpcs(Set<String> epcs);

    //根据资产名称，资产编号模糊查询（全部）
    @Query("SELECT * FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%'")
    public List<AssetsAllInfo> findLocalAssetsAllInfoByPara(String para);

    //根据资产id获取资产详情
    @Query("SELECT * FROM AssetsAllInfo where id == :astId")
    public List<AssetsAllInfo> findLocalAssetsByAstId(String astId);

}
