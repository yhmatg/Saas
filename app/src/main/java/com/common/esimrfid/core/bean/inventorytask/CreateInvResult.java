package com.common.esimrfid.core.bean.inventorytask;

public class CreateInvResult {

    /**
     * create_date : 1575515768311
     * creator_id : caddb5d8014311eab97300163e086c26
     * id : 9496c30d170d11eaabcf00163e0a6695
     * inv_assigner_id : 07043df511c511eaabcf00163e0a6695
     * inv_code : PD201912050016
     * inv_creator_id : caddb5d8014311eab97300163e086c26
     * inv_exptfinish_date : 1574825478000
     * inv_finish_count : 0
     * inv_name : admin
     * inv_status : 10
     * inv_total_count : 100
     * update_date : 1575515768311
     * updater_id : caddb5d8014311eab97300163e086c26
     */

    private String creator_id;
    private String id;
    private String inv_assigner_id;
    private String inv_code;


    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInv_assigner_id() {
        return inv_assigner_id;
    }

    public void setInv_assigner_id(String inv_assigner_id) {
        this.inv_assigner_id = inv_assigner_id;
    }

    public String getInv_code() {
        return inv_code;
    }

    public void setInv_code(String inv_code) {
        this.inv_code = inv_code;
    }
}
