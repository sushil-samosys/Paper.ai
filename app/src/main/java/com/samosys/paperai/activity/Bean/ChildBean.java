package com.samosys.paperai.activity.Bean;

/**
 * Created by samosys on 12/5/18.
 */

public class ChildBean {

    private String childObjectId;
    private String childName;
    private String childUpdatedAt;
    private String childWorkspaceID;
    private String childObjective;
    private String childCreatedAt;
    private String strdefault;
    private  String childType;

    public String getStrdefault() {
        return strdefault;
    }

    public void setStrdefault(String strdefault) {
        this.strdefault = strdefault;
    }

    public ChildBean(String objectId, String name, String updatedAt, String objective, String createdAt, String type, String strdefault) {
        this.childObjectId = objectId;
        this.childName = name;
        this.childUpdatedAt = updatedAt;

        this.childObjective = objective;
        this.childCreatedAt = createdAt;

        this.childType = type;
this.strdefault=strdefault;
    }

//    public ChildBean(String childObjectId, String childName, String childUpdatedAt, String childWorkspaceID,
//                     String childObjective, String childCreatedAt,String childType) {
//        this.childObjectId = childObjectId;
//        this.childName = childName;
//        this.childUpdatedAt = childUpdatedAt;
//        this.childWorkspaceID = childWorkspaceID;
//        this.childObjective = childObjective;
//        this.childCreatedAt = childCreatedAt;
////        this.childImage = childImage;
//        this.childType = childType;
//    }

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

//    public String getChildImage() {
//        return childImage;
//    }
//
//    public void setChildImage(String childImage) {
//        this.childImage = childImage;
//    }

    public String getChildType() {
        return childType;
    }

    public void setChildType(String childType) {
        this.childType = childType;
    }
}
