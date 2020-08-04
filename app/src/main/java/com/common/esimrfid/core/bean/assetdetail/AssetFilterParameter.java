package com.common.esimrfid.core.bean.assetdetail;

import com.common.esimrfid.ui.inventorytask.FilterBean;
import com.multilevel.treelist.Node;

import java.util.Date;
import java.util.List;

public class AssetFilterParameter {
    private List<Node> mSelectAssetsStatus;
    private FilterBean mSelectUseCompany;
    private List<Node> mSelectDepartments;
    private List<Node> mSelectAssetsTypes;
    private List<Node> mSelectAssetsLocations;
    private List<Node> mSelectMangerUsers;
    private String userRealName;

    public List<Node> getmSelectAssetsStatus() {
        return mSelectAssetsStatus;
    }

    public void setmSelectAssetsStatus(List<Node> mSelectAssetsStatus) {
        this.mSelectAssetsStatus = mSelectAssetsStatus;
    }

    public FilterBean getmSelectUseCompany() {
        return mSelectUseCompany;
    }

    public void setmSelectUseCompany(FilterBean mSelectUseCompany) {
        this.mSelectUseCompany = mSelectUseCompany;
    }

    public List<Node> getmSelectDepartments() {
        return mSelectDepartments;
    }

    public void setmSelectDepartments(List<Node> mSelectDepartments) {
        this.mSelectDepartments = mSelectDepartments;
    }

    public List<Node> getmSelectAssetsTypes() {
        return mSelectAssetsTypes;
    }

    public void setmSelectAssetsTypes(List<Node> mSelectAssetsTypes) {
        this.mSelectAssetsTypes = mSelectAssetsTypes;
    }

    public List<Node> getmSelectAssetsLocations() {
        return mSelectAssetsLocations;
    }

    public void setmSelectAssetsLocations(List<Node> mSelectAssetsLocations) {
        this.mSelectAssetsLocations = mSelectAssetsLocations;
    }

    public List<Node> getmSelectMangerUsers() {
        return mSelectMangerUsers;
    }

    public void setmSelectMangerUsers(List<Node> mSelectMangerUsers) {
        this.mSelectMangerUsers = mSelectMangerUsers;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    @Override
    public String toString() {
        return "[" +
                getStatusListString() +
                "]";
    }

    private String getStatusListString() {
        String s1 = "{\"name\":\"ast_used_status\",\"condition\":\"In\",\"values\":[\"3\",\"1\"]}";
        String astIdString = "";
        if (mSelectAssetsStatus!= null && mSelectAssetsStatus.size() > 0) {
            astIdString += "{\"name\":\"ast_used_status\",\"condition\":\"In\",\"values\":[";
            for (int i = 0; i < mSelectAssetsStatus.size(); i++) {
                Node node = mSelectAssetsStatus.get(i);
                String status = "\"" + node.getId() + "\"";
                if (i < mSelectAssetsStatus.size() - 1) {
                    status += ',';
                }
                astIdString += status;
            }
            astIdString += "]}";
        }
        return astIdString;
    }
}
