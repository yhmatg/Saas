package com.common.esimrfid.core.bean.nanhua.inventorybeans;

public class OrderStatus {
//    INIT("初始创建", 0),
//    PROCESSING("处理中", 10), FINISH("已完成", 11), CANCELLED("作废", 12);

    // 成员变量
    private Integer code;
    private Integer index;
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
