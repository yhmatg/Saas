package com.ddcommon.esimrfid.core.bean.emun;

/**
 * @Auther: lijiahang
 * @Date: 2019/2/14 16:36
 * @Description:
 */
public enum AssetsUseStatus {
    IN_IDEL("闲置",0),
    IN_USED("在用", 1),
    IN_REPAIR("维修中", 2),
    IN_Transfer("调拨中",3),
    IN_DISTRIBUTE("待派发",4),
    AL_DISTRIBUTE("已派发",5),
    IN_BORROW("借用",6),
    APPROVAL_BORROW("借用审批中",7),
    APPROVAL_RETURN("归还审批中",8),
    APPROVAL_REPAIR("维修审批中",9),
    SCRAP("报废",10),
    APPROVAL_ALLOT("调拨审批中",11),
    APPROVAL_SCRAP("报废审批中",12),
    APPROVAL_REQUISITION("领用审批中",13),
    APPROVAL_RE_STORE("退库审批中",14);

    // 成员变量
    private String name;
    private int index;

    private AssetsUseStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (AssetsUseStatus c : AssetsUseStatus.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
//    public static AssetsStatus getByName(String name) {
//        for (AssetsStatus c : AssetsStatus.values()) {
//            if (c.getName().equals(name)) {
//                return c;
//            }
//        }
//        return null;
//    }
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
