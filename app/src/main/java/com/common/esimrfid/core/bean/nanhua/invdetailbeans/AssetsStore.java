package com.common.esimrfid.core.bean.nanhua.invdetailbeans;

import java.io.Serializable;
import java.util.List;


public class AssetsStore implements Serializable {

    private String code;
    private String message;
    private List<AssetsStoreResult> result;
    private boolean success;
    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setResult(List<AssetsStoreResult> result) {
        this.result = result;
    }
    public List<AssetsStoreResult> getResult() {
        return result;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }

}