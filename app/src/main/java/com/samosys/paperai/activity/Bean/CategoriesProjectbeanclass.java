package com.samosys.paperai.activity.Bean;

/**
 * Created by samosys on 17/5/18.
 */

public class CategoriesProjectbeanclass {
  private   String name;
    private String objective;
    private String createdAt;
    private String updatedAt;
    private String catProjectobjectId;
    private String type;

    public CategoriesProjectbeanclass(String name, String objective, String createdAt, String updatedAt, String catProjectobjectId, String type) {
        this.name = name;
        this.objective = objective;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.catProjectobjectId = catProjectobjectId;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCatProjectobjectId() {
        return catProjectobjectId;
    }

    public void setCatProjectobjectId(String catProjectobjectId) {
        this.catProjectobjectId = catProjectobjectId;
    }
}
