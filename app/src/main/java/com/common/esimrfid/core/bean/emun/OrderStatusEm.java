package com.common.esimrfid.core.bean.emun;

public enum OrderStatusEm {
    INIT("初始创建", 0),
    PROCESSING("处理中", 10), FINISH("已完成", 11), CANCELLED("作废", 12);

    // 成员变量
    private String name;
    private int index;

    private OrderStatusEm(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (OrderStatusEm c : OrderStatusEm.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
    public static int getIndex(String name) {
        for (OrderStatusEm c : OrderStatusEm.values()) {
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
