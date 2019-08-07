package com.common.esimrfid.core.bean.nanhua.invdetailbeans;

import java.io.Serializable;
import java.util.List;


public class AssetsLocation implements Serializable {

    private String code;
    private String message;
    private List<AssetsLocationResult> result;
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

    public void setResult(List<AssetsLocationResult> result) {
        this.result = result;
    }
    public List<AssetsLocationResult> getResult() {
        return result;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }

}