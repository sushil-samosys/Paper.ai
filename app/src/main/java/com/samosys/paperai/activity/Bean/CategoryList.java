package com.samosys.paperai.activity.Bean;

/**
 * Created by samosys on 7/5/18.
 */

public class CategoryList {
    String objectId;
    String name;
    String updatedAt;
    String workspaceID;
    String createdAt;

    public CategoryList(String objectId, String name, String updatedAt, String workspaceID, String createdAt) {
        this.objectId=objectId;
        this.name=name;
        this.updatedAt=updatedAt;
        this.workspaceID=workspaceID;
        this.createdAt=createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getWorkspaceID() {
        return workspaceID;
    }

    public void setWorkspaceID(String workspaceID) {
        this.workspaceID = workspaceID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
