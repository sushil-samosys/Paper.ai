package com.samosys.paperai.activity.Bean;

/**
 * Created by samosys on 30/5/18.
 */

public class NewParentProjectBeanList {

    private String objectId;
    private String name;
    private String updatedAt;
    private String workspaceID;
    private String objective;
    private String createdAt;
    private String image;

    public NewParentProjectBeanList(String objectId, String name, String updatedAt,
                                    String workspaceID, String objective, String createdAt, String image) {
        this.objectId = objectId;
        this.name = name;
        this.updatedAt = updatedAt;
        this.workspaceID = workspaceID;
        this.objective = objective;
        this.createdAt = createdAt;
        this.image = image;
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

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
