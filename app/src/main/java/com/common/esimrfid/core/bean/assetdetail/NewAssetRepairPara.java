package com.common.esimrfid.core.bean.assetdetail;

import java.util.Date;
import java.util.List;

public class NewAssetRepairPara {
    private String formData;
    private String textForms;
    private String title;

    public NewAssetRepairPara(String formData, String textForms, String title) {
        this.formData = formData;
        this.textForms = textForms;
        this.title = title;
    }

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public String getTextForms() {
        return textForms;
    }

    public void setTextForms(String textForms) {
        this.textForms = textForms;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
