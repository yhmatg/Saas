package com.common.esimrfid.core.bean.beacon;

public class BeaconLocInfo {

    /**
     * create_date : 1608189710000
     * id : 23282105c4634e1098a1017e7800db87
     * loc_code : 07552001F2210086
     * loc_isleaf : 1
     * loc_latlng : 07552001F2210086
     * loc_name : 弱电井1
     * loc_superid : ff932abdb5bf11ea891c000c29a8812b
     * update_date : 1608189930000
     */

    private long create_date;
    private String id;
    private String loc_code;
    private int loc_isleaf;
    private String loc_latlng;
    private String loc_name;
    private String loc_superid;
    private long update_date;

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoc_code() {
        return loc_code;
    }

    public void setLoc_code(String loc_code) {
        this.loc_code = loc_code;
    }

    public int getLoc_isleaf() {
        return loc_isleaf;
    }

    public void setLoc_isleaf(int loc_isleaf) {
        this.loc_isleaf = loc_isleaf;
    }

    public String getLoc_latlng() {
        return loc_latlng;
    }

    public void setLoc_latlng(String loc_latlng) {
        this.loc_latlng = loc_latlng;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public String getLoc_superid() {
        return loc_superid;
    }

    public void setLoc_superid(String loc_superid) {
        this.loc_superid = loc_superid;
    }

    public long getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(long update_date) {
        this.update_date = update_date;
    }
}
