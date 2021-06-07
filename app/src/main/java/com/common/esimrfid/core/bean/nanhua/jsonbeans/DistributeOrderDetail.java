package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import java.util.List;
import java.util.Objects;

public class DistributeOrderDetail {
    private String id;
    private String odr_code;
    private List<DistTypeDetail> detail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOdr_code() {
        return odr_code;
    }

    public void setOdr_code(String odr_code) {
        this.odr_code = odr_code;
    }

    public List<DistTypeDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<DistTypeDetail> detail) {
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DistributeOrderDetail)) return false;
        DistributeOrderDetail that = (DistributeOrderDetail) o;
        return getId().equals(that.getId()) &&
                getOdr_code().equals(that.getOdr_code());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOdr_code());
    }
}
