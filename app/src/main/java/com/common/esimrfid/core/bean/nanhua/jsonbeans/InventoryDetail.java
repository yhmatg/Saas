package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

//一条盘点资产详情
@Entity
public class InventoryDetail {
    /**
     * assetsInfo : {"ast_brand":"","ast_code":"dsdsds","ast_epc_code":"647364736473","ast_material":0,"ast_model":"","ast_name":"笔记本电脑","ast_sn":"","ast_status":0,"ast_used_status":2,"create_date":1573615599000,"creator_id":"admin_id","id":"b8b39374709c4345bcba7781550f07b0","loc_id":"1","loc_info":{"loc_code":"0004","loc_name":"16号楼行政部"},"org_belongcorp_id":"","org_useddept":{"id":"6990f769cefc11e9abc50242ac110002","org_name":"学习用品部门"},"org_useddept_id":"6990f769cefc11e9abc50242ac110002","type_id":"un_assigned","update_date":1573627276000,"user_id":"862514b87d3a42a6b709aa08667cc677","user_info":{"id":"862514b87d3a42a6b709aa08667cc677","user_name":"test","user_real_name":"test"}}
     * ast_id : b8b39374709c4345bcba7781550f07b0
     * creator : {"id":"admin_id","user_name":"admin","user_real_name":"张三"}
     * creator_id : admin_id
     * id : 05a7d5b944884d3fa8d42e1137fe4811
     * inv_id : 67824be605c511ea8f82258b4dde74a9
     * invdt_status : {"code":1,"index":1,"name":"已盘点"}
     * update_date : 1573627276000
     */
    @Embedded(prefix = "assetsInfos_")
    private AssetsInfo assetsInfos;
    private String ast_id;
    @PrimaryKey
    @NonNull
    private String id;
    private String inv_id;
    @Embedded
    private InvdtStatus invdt_status;

    public AssetsInfo getAssetsInfos() {
        return assetsInfos;
    }

    public void setAssetsInfos(AssetsInfo assetsInfos) {
        this.assetsInfos = assetsInfos;
    }

    public String getAst_id() {
        return ast_id;
    }

    public void setAst_id(String ast_id) {
        this.ast_id = ast_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInv_id() {
        return inv_id;
    }

    public void setInv_id(String inv_id) {
        this.inv_id = inv_id;
    }

    public InvdtStatus getInvdt_status() {
        return invdt_status;
    }

    public void setInvdt_status(InvdtStatus invdt_status) {
        this.invdt_status = invdt_status;
    }

    public static class InvdtStatus {
        /**
         * code : 1
         * index : 1
         * name : 已盘点
         */
        @PrimaryKey
        @NonNull
        private Integer code;
        private String name;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryDetail)) return false;
        InventoryDetail that = (InventoryDetail) o;
        return getAst_id().equals(that.getAst_id()) &&
                getId().equals(that.getId()) &&
                getInv_id().equals(that.getInv_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAst_id(), getId(), getInv_id());
    }
}

