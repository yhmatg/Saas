package com.common.esimrfid.core.bean.emun;

public enum AssetsMaterial {
    NOMEATL("非金属",1),
    METAL("金属",0);

    private String name;
    private int index;

    private AssetsMaterial(String name, int index){
        this.name=name;
        this.index=index;
    }

    public static String getName(int index) {
        for (AssetsMaterial c : AssetsMaterial.values()) {
            if (c.getIndex() == index) {
                return c.name;
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
