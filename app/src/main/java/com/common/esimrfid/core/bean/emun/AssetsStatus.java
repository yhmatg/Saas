package com.common.esimrfid.core.bean.emun;

public enum AssetsStatus {
    NORMAL("正常", 0),
    AST_DESTORY("损坏",1),
    MAINTAIN("维修中",2);


    // 成员变量
    private String name;
    private int index;

    private AssetsStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (AssetsStatus c : AssetsStatus.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
    public static AssetsStatus getByName(String name) {
        for (AssetsStatus c : AssetsStatus.values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
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
