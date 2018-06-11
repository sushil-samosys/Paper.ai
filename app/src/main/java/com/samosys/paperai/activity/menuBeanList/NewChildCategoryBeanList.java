package com.samosys.paperai.activity.menuBeanList;

/**
 * Created by samosys on 30/5/18.
 */

public class NewChildCategoryBeanList {

    String childObjectId;
    String childName;
    String childUpdatedAt;
    String childWorkspaceID;
    String childObjective;
    String childCreatedAt;
    String strdefault;
    String childType;

    public NewChildCategoryBeanList(String childObjectId, String childName, String childUpdatedAt,
                                    String childWorkspaceID, String childObjective, String childCreatedAt, String strdefault, String childType) {
        this.childObjectId = childObjectId;
        this.childName = childName;
        this.childUpdatedAt = childUpdatedAt;
        this.childWorkspaceID = childWorkspaceID;
        this.childObjective = childObjective;
        this.childCreatedAt = childCreatedAt;
        this.strdefault = strdefault;
        this.childType = childType;
    }

    public String getChildObjectId() {
        return childObjectId;
    }

    public void setChildObjectId(String childObjectId) {
        this.childObjectId = childObjectId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildUpdatedAt() {
        return childUpdatedAt;
    }

    public void setChildUpdatedAt(String childUpdatedAt) {
        this.childUpdatedAt = childUpdatedAt;
    }

    public String getChildWorkspaceID() {
        return childWorkspaceID;
    }

    public void setChildWorkspaceID(String childWorkspaceID) {
        this.childWorkspaceID = childWorkspaceID;
    }

    public String getChildObjective() {
        return childObjective;
    }

    public void setChildObjective(String childObjective) {
        this.childObjective = childObjective;
    }

    public String getChildCreatedAt() {
        return childCreatedAt;
    }

    public void setChildCreatedAt(String childCreatedAt) {
        this.childCreatedAt = childCreatedAt;
    }

    public String getStrdefault() {
        return strdefault;
    }

    public void setStrdefault(String strdefault) {
        this.strdefault = strdefault;
    }

    public String getChildType() {
        return childType;
    }

    public void setChildType(String childType) {
        this.childType = childType;
    }
}
