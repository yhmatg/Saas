package com.common.esimrfid.core.bean.inventorytask;

import com.contrarywind.interfaces.IPickerViewData;

public class AssetsType implements IPickerViewData {

    /**
     * create_date : 1574674207000
     * id : 2b0ab8bf0f6611eaabcf00163e0a6695
     * type_code : 0001
     * type_feilds : []
     * type_img_url :
     * type_isleaf : 1
     * type_name : 类型1
     * update_date : 1574708690000
     */

    private String id;
    private String type_code;
    private String type_name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_code() {
        return type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }


    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    @Override
    public String getPickerViewText() {
        return type_name;
    }
}
