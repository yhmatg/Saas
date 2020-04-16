package com.ddcommon.esimrfid.core.bean.emun;
//盘点单状态
public enum InvOperateStatus {

    NOT_MODIFIED("未修改", 0),
    MODIFIED_BUT_NOT_SUBMIT("已修改但未提交",1),
    MODIFIED_AND_SUBMIT_BUT_NOT_FINISHED("已修改已提交但未结束任务",2),
    FINISHED("已结束任务",3);

    // 成员变量
    private String name;
    private int index;

    InvOperateStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (InvOperateStatus c : InvOperateStatus.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static int getIndex(String name) {
        for (InvOperateStatus c : InvOperateStatus.values()) {
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
