package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.inventorytask.EpcBean;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;
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
    @Query("SELECT * FROM AssetsAllInfo where id = :astId and ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_info_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_info_id in (:auth_loc_scope))")
    public List<AssetsAllInfo> findLocalAssetsByAstId(String astId,List<String> auth_corp_scope,List<String> auth_dept_scope,List<String> auth_type_scope,List<String> auth_loc_scope);

    //根据资产id或者epc获取资产详情
    @Query("SELECT * FROM AssetsAllInfo where id = :astId or ast_epc_code = :epcCode")
    public List<AssetsAllInfo> findLocalAssetsByAstIdOrEpc(String astId, String epcCode);

    //获取所有的资产（资产搜索查找使用）
    @Query("SELECT ast_brand,ast_barcode,ast_epc_code,ast_model,ast_name,id,loc_info_loc_name FROM AssetsAllInfo where ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_info_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_info_id in (:auth_loc_scope)) ")
    public List<SearchAssetsInfo> getAllAssetForSearch(List<String> auth_corp_scope,List<String> auth_dept_scope,List<String> auth_type_scope,List<String> auth_loc_scope);

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

    //根据资产名称，资产编号模糊查询(精简)分页(资产列表使用)
    @Query("SELECT ast_barcode,userinfo_user_real_name,ast_name,id,loc_info_loc_name,ast_price,ast_used_status,ast_buy_date FROM AssetsAllInfo where (ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%') and ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_info_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_info_id in (:auth_loc_scope)) LIMIT :size OFFSET :currentSize")
    public List<AssetsListItemInfo> searchPageLocalAssetListByPara(Integer size, String para, int currentSize,List<String> auth_corp_scope,List<String> auth_dept_scope,List<String> auth_type_scope,List<String> auth_loc_scope);

}
