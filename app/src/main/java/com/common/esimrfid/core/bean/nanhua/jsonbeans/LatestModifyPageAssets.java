package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import java.util.List;

public class LatestModifyPageAssets {

    /**
     * modified : [{"ast_barcode":"200911000968","ast_brand":"","ast_buy_date":1599062400000,"ast_count":1,"ast_epc_code":"E22020091127410050090202","ast_material":1,"ast_measuring_unit":"","ast_model":"","ast_name":"资产39","ast_remark":"","ast_source":"购入","ast_status":0,"ast_used_status":2,"create_date":1599793862000,"creator_id":"85ddbc5649cb4131adfa1db222fd9d9b","id":"699db321cab84af19a93d7528ea7844f","loc_id":"59a2c8c2b06111ea891c000c29a8812b","loc_name":"二楼","manager_id":"85ddbc5649cb4131adfa1db222fd9d9b","manager_name":"周","org_belongcorp_id":"","primary_ast":1,"type_id":"496255baa4b811ea891c000c29a8812b","type_name":"笔记本","update_date":1600160349000,"updater_id":"85ddbc5649cb4131adfa1db222fd9d9b"},{"ast_barcode":"200911000962","ast_brand":"","ast_buy_date":1599062400000,"ast_count":1,"ast_epc_code":"E22020091127410050030202","ast_material":1,"ast_measuring_unit":"","ast_model":"","ast_name":"资产33","ast_remark":"","ast_source":"购入","ast_status":0,"ast_used_status":2,"create_date":1599793862000,"creator_id":"85ddbc5649cb4131adfa1db222fd9d9b","id":"44f4dc40371f4c2586fe8898399f8da8","loc_id":"59a2c8c2b06111ea891c000c29a8812b","loc_name":"二楼","manager_id":"85ddbc5649cb4131adfa1db222fd9d9b","manager_name":"周","org_belongcorp_id":"","primary_ast":1,"type_id":"496255baa4b811ea891c000c29a8812b","type_name":"笔记本","update_date":1600160240000,"updater_id":"85ddbc5649cb4131adfa1db222fd9d9b"}]
     * removed : [{"ast_barcode":"200914000971","ast_brand":"","ast_code":"","ast_epc_code":"E22020091451280062090202","ast_expiration_months":"25","ast_img_url":"","ast_measuring_unit":"","ast_model":"","ast_name":"money2","ast_remark":"","ast_sn":"","ast_source":"购入","ast_status":0,"ast_used_status":0,"creator_id":"85ddbc5649cb4131adfa1db222fd9d9b","id":"dcb57d1edaa14680ac66773f2acd4226","loc_id":"56e85ccab06111ea891c000c29a8812b","manager_id":"85ddbc5649cb4131adfa1db222fd9d9b","org_belongcorp_id":"2ab55801259648abbdf46bfbd9215687","org_belongdept_id":"","org_usedcorp_id":"43a0dd5d5efa4553a139642b4796a3ea","org_useddept_id":"","price_currency_unit":"","type_id":"a4bd3fa8a6314a0f905e3dc77b040de6","update_date":1600149214000,"updater_id":"","user_id":"","warranty_id":""},{"ast_barcode":"200818000882","ast_brand":"","ast_code":"","ast_epc_code":"E20159774241806191150202","ast_expiration_months":"","ast_img_url":"","ast_measuring_unit":"","ast_model":"","ast_name":"笔记本电脑","ast_remark":"","ast_sn":"","ast_source":"购入","ast_status":0,"ast_used_status":0,"creator_id":"85ddbc5649cb4131adfa1db222fd9d9b","id":"788586f3436d4983809fdbf23fa7076a","loc_id":"56e85ccab06111ea891c000c29a8812b","manager_id":"85ddbc5649cb4131adfa1db222fd9d9b","org_belongcorp_id":"66fa07447fcb449eb251db1427e9ec1b","org_belongdept_id":"","org_usedcorp_id":"66fa07447fcb449eb251db1427e9ec1b","org_useddept_id":"","price_currency_unit":"","type_id":"04cc7a36b53c11ea891c000c29a8812b","update_date":1597742832000,"updater_id":"","user_id":"","warranty_id":""}]
     * pageNum : 1
     * pageSize : 2
     * pages : 193
     * total : 467
     */

    private int pageNum;
    private int pageSize;
    private int pages;
    private int total;
    private List<AssetsAllInfo> modified;
    private List<AssetsAllInfo> removed;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<AssetsAllInfo> getModified() {
        return modified;
    }

    public void setModified(List<AssetsAllInfo> modified) {
        this.modified = modified;
    }

    public List<AssetsAllInfo> getRemoved() {
        return removed;
    }

    public void setRemoved(List<AssetsAllInfo> removed) {
        this.removed = removed;
    }
}
