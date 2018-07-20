package com.samosys.paperai.activity.Bean;

import java.util.ArrayList;

/**
 * Created by samosys on 30/5/18.
 */

public class NewParentCategoryBeanList {

    private String categoryId;
    private String categoryName;
    private String parentType;
    private String strdefault;

    public ArrayList<NewChildCategoryBeanList> getNewChildCategoryBeanLists() {
        return newChildCategoryBeanLists;
    }

    public void setNewChildCategoryBeanLists(ArrayList<NewChildCategoryBeanList> newChildCategoryBeanLists) {
        this.newChildCategoryBeanLists = newChildCategoryBeanLists;
    }

    private ArrayList<NewChildCategoryBeanList> newChildCategoryBeanLists;

    public NewParentCategoryBeanList(String categoryId, String categoryName, String parentType, String strdefault,
                                     ArrayList<NewChildCategoryBeanList> newChildCategoryBeanLists) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentType = parentType;
        this.strdefault = strdefault;
        this.newChildCategoryBeanLists = newChildCategoryBeanLists;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getStrdefault() {
        return strdefault;
    }

    public void setStrdefault(String strdefault) {
        this.strdefault = strdefault;
    }
}
