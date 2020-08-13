package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import com.common.esimrfid.core.bean.inventorytask.EpcBean;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.AssetsListItemInfo;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.esimrfid.core.bean.nanhua.jsonbeans.SearchAssetsInfo;

import java.util.List;
import java.util.Set;

@Dao
public interface AssetsAllInfoDao extends BaseDao<AssetsAllInfo> {

    //根据资产名称，资产编号模糊查询(精简)(标签写入页面使用)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)//AssetsInfo中没有的属性不返回
    @Query("SELECT * FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%'")
    public List<AssetsInfo> findLocalAssetsByPara(String para);

    @Query("DELETE FROM AssetsAllInfo")
    public void deleteAllData();

    //开始盘点页面，数据结构修改后不再使用
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM AssetsAllInfo where ast_epc_code in (:epcs)")
    public List<AssetsInfo> findLocalAssetsByEpcs(Set<String> epcs);

    //更具扫描到的epc获取资产，数据结构修改后使用（具体某个位置盘点使用）
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM AssetsAllInfo where ast_epc_code in (:epcs)")
    public List<InventoryDetail> findLocalInvdetailByEpcs(Set<String> epcs);

    //根据资产名称，资产编号模糊查询（全部）暂未用到
    @Query("SELECT * FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%'")
    public List<AssetsAllInfo> findLocalAssetsAllInfoByPara(String para);

    //根据资产id获取资产详情（暂未用到）
    @Query("SELECT * FROM AssetsAllInfo where id = :astId and ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_id in (:auth_loc_scope))")
    public List<AssetsAllInfo> findLocalAssetsByAstId(String astId, List<String> auth_corp_scope, List<String> auth_dept_scope, List<String> auth_type_scope, List<String> auth_loc_scope);

    //根据资产id或者epc获取资产详情(资产详情页面使用)
    @Query("SELECT * FROM AssetsAllInfo where id = :astId or ast_epc_code = :epcCode")
    public List<AssetsAllInfo> findLocalAssetsByAstIdOrEpc(String astId, String epcCode);

    //根据资产名称，资产编号模糊查询(资产查找页面使用，不分页)
    @Query("SELECT ast_brand,ast_barcode,ast_epc_code,ast_model,ast_name,id,loc_name FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%'")
    public List<SearchAssetsInfo> searchLocalAssetsByPara(String para);

    //获取所有资产的epc(具体某个位置盘点使用)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT ast_epc_code FROM AssetsAllInfo")
    public List<EpcBean> getAllAssetEpcs();

    //根据资产名称，资产编号模糊查询分页分页（标签写入页面使用）
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%' LIMIT :size OFFSET :currentSize")
    public List<AssetsInfo> findPageLocalAssetsByPara(Integer size, String para, int currentSize);

    //根据资产名称，资产编号模糊查询(精简)分页(资产查找页面使用)
    @Query("SELECT ast_brand,ast_barcode,ast_epc_code,ast_model,ast_name,id,loc_name FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%' LIMIT :size OFFSET :currentSize")
    public List<SearchAssetsInfo> searchPageLocalAssetsByPara(Integer size, String para, int currentSize);


    //根据资产名称，资产编号模糊查询(精简)分页(资产列表使用)
    @Query("SELECT ast_barcode,user_name,ast_name,id,loc_name,ast_price,ast_used_status,ast_buy_date FROM AssetsAllInfo where ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%' LIMIT :size OFFSET :currentSize")
    public List<AssetsListItemInfo> searchPageLocalAssetListByPara(Integer size, String para, int currentSize);

    //<----------------------------------------------------------------------------------------------->

    //根据资产名称，资产编号模糊查询(精简)(标签写入页面使用)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM AssetsAllInfo where (ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%') and ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_id in (:auth_loc_scope))")
    public List<AssetsInfo> findLocalAssetsByPara(String para, List<String> auth_corp_scope, List<String> auth_dept_scope, List<String> auth_type_scope, List<String> auth_loc_scope);

    //更具扫描到的epc获取资产，数据结构修改后使用（具体某个位置盘点使用）
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM AssetsAllInfo where ast_epc_code in (:epcs) and ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_id in (:auth_loc_scope))")
    public List<InventoryDetail> findLocalInvdetailByEpcs(Set<String> epcs, List<String> auth_corp_scope, List<String> auth_dept_scope, List<String> auth_type_scope, List<String> auth_loc_scope);

    //获取所有的资产（资产搜索查找使用）
    @Query("SELECT ast_brand,ast_barcode,ast_epc_code,ast_model,ast_name,id,loc_name FROM AssetsAllInfo where ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_id in (:auth_loc_scope)) ")
    public List<SearchAssetsInfo> getAllAssetForSearch(List<String> auth_corp_scope, List<String> auth_dept_scope, List<String> auth_type_scope, List<String> auth_loc_scope);


    //根据资产名称，资产编号模糊查询(资产查找页面使用，不分页)
    @Query("SELECT ast_brand,ast_barcode,ast_epc_code,ast_model,ast_name,id,loc_name FROM AssetsAllInfo where (ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%') and ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_id in (:auth_loc_scope))")
    public List<SearchAssetsInfo> searchLocalAssetsByPara(String para, List<String> auth_corp_scope, List<String> auth_dept_scope, List<String> auth_type_scope, List<String> auth_loc_scope);

    //获取所有资产的epc(具体某个位置盘点使用不分页)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT ast_epc_code FROM AssetsAllInfo where ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_id in (:auth_loc_scope))")
    public List<EpcBean> getAllAssetEpcs(List<String> auth_corp_scope, List<String> auth_dept_scope, List<String> auth_type_scope, List<String> auth_loc_scope);

    //根据资产名称，资产编号模糊查询分页分页（标签写入页面使用分页）
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM AssetsAllInfo where (ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%') and ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_id in (:auth_loc_scope)) LIMIT :size OFFSET :currentSize")
    public List<AssetsInfo> findPageLocalAssetsByPara(Integer size, String para, int currentSize, List<String> auth_corp_scope, List<String> auth_dept_scope, List<String> auth_type_scope, List<String> auth_loc_scope);

    //根据资产名称，资产编号模糊查询(精简)分页(资产查找页面使用分页)
    @Query("SELECT ast_brand,ast_barcode,ast_epc_code,ast_model,ast_name,id,loc_name FROM AssetsAllInfo where (ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%') and ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_id in (:auth_loc_scope)) LIMIT :size OFFSET :currentSize")
    public List<SearchAssetsInfo> searchPageLocalAssetsByPara(Integer size, String para, int currentSize, List<String> auth_corp_scope, List<String> auth_dept_scope, List<String> auth_type_scope, List<String> auth_loc_scope);


    //根据资产名称，资产编号,状态模糊查询(精简)分页(资产列表使用分页) 加权限过滤
    @Query("SELECT ast_barcode,user_name,ast_name,id,loc_name,ast_price,ast_used_status,ast_buy_date FROM AssetsAllInfo where (ast_name LIKE '%' || :para || '%' OR ast_barcode LIKE '%' || :para || '%') and ( -1 in (:status) or ast_used_status in (:status))and ('allData' in (:auth_corp_scope) or org_usedcorp_id in (:auth_corp_scope)) and ('allData' in (:auth_dept_scope) or org_useddept_id in (:auth_dept_scope)) and ('allData' in (:auth_type_scope) or type_id in (:auth_type_scope)) and ('allData' in (:auth_loc_scope) or loc_id in (:auth_loc_scope)) LIMIT :size OFFSET :currentSize")
    public List<AssetsListItemInfo> searchPageLocalAssetListByPara(Integer size, String para, int currentSize, List<Integer> status, List<String> auth_corp_scope, List<String> auth_dept_scope, List<String> auth_type_scope, List<String> auth_loc_scope);

}
