package com.common.xfxj.core.bean.emun;
//资产盘点状态
public enum InventoryStatus {

    INIT("未盘点", 0),
    FINISH("已盘点",10),
    FINISH_NOT_SUBMIT("已盘点未提交",101),
    MORE("盘盈", 2),
    LESS("盘亏",1),;

    // 成员变量
    private String name;
    private int index;

    InventoryStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (InventoryStatus c : InventoryStatus.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static int getIndex(String name) {
        for (InventoryStatus c : InventoryStatus.values()) {
            if (c.getName() == name) {
                return c.index;
            }
        }
        return -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
