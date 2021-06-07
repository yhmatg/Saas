package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import java.util.Date;
import java.util.Objects;

public class DistributeOrder {
    private String id ;
    private String odr_code ;
    private String odr_type ;
    private String odr_status ;
    private String odr_remark ;
    private String creator_name ;
    private Date create_date;
    //todo 字段名称待确认
    private Integer apply_detail_count = 0;

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

    public String getOdr_type() {
        return odr_type;
    }

    public void setOdr_type(String odr_type) {
        this.odr_type = odr_type;
    }

    public String getOdr_status() {
        return odr_status;
    }

    public void setOdr_status(String odr_status) {
        this.odr_status = odr_status;
    }

    public String getOdr_remark() {
        return odr_remark;
    }

    public void setOdr_remark(String odr_remark) {
        this.odr_remark = odr_remark;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Integer getApply_detail_count() {
        return apply_detail_count;
    }

    public void setApply_detail_count(Integer apply_detail_count) {
        this.apply_detail_count = apply_detail_count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DistributeOrder)) return false;
        DistributeOrder that = (DistributeOrder) o;
        return getId().equals(that.getId()) &&
                getOdr_code().equals(that.getOdr_code());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOdr_code());
    }
}
