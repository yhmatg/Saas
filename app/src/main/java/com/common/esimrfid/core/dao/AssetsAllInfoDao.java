package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.inventorytask.EpcBean;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;

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
    @Query("SELECT * FROM AssetsAllInfo where id = :astId")
    public List<AssetsAllInfo> findLocalAssetsByAstId(String astId);

    //根据资产id或者epc获取资产详情
    @Query("SELECT * FROM AssetsAllInfo where id = :astId or ast_epc_code = :epcCode")
    public List<AssetsAllInfo> findLocalAssetsByAstIdOrEpc(String astId, String epcCode);

    //获取所有的资产（资产搜索查找使用）
    @Query("SELECT ast_brand,ast_barcode,ast_epc_code,ast_model,ast_name,id,loc_info_loc_name FROM AssetsAllInfo")
    public List<SearchAssetsInfo> getAllAssetForSearch();

    //根据资产名称，资产编号模糊查询(资产搜索查找使用)
    @Query("SELECT ast_brand,ast_barcode,ast_epc_code,ast_model,ast_name,id,loc_info_loc_name FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%'")
    public List<SearchAssetsInfo> searchLocalAssetsByPara(String para);

    //获取所有资产的epc
    @Query("SELECT ast_epc_code FROM AssetsAllInfo")
    public List<EpcBean> getAllAssetEpcs();

    //根据资产名称，资产编号模糊查询分页
    @Query("SELECT * FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%' LIMIT :size OFFSET :currentSize")
    public List<AssetsInfo> findPageLocalAssetsByPara(Integer size, String para, int currentSize);

    //根据资产名称，资产编号模糊查询(精简)分页
    @Query("SELECT ast_brand,ast_barcode,ast_epc_code,ast_model,ast_name,id,loc_info_loc_name FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%' LIMIT :size OFFSET :currentSize")
    public List<SearchAssetsInfo> searchPageLocalAssetsByPara(Integer size, String para, int currentSize);
}
