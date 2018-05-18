package com.samosys.paperai.activity.Bean;

import java.util.ArrayList;

/**
 * Created by samosys on 12/5/18.
 */

public class ParentBean {

    private String categoryId;
    private String categoryName;
    private String parentType;
    private String strdefault;

    public ParentBean(String objectId, String name, String type, String strdefault, ArrayList<ChildBean> childBeans) {
        this.categoryId = objectId;
        this.categoryName = name;
        this.childBeans = childBeans;
        this.parentType = type;
        this.strdefault = strdefault;
    }

    public String getStrdefault() {
        return strdefault;
    }

    public void setStrdefault(String strdefault) {
        this.strdefault = strdefault;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    private ArrayList<ChildBean> childBeans;

//    public ParentBean(String categoryId, String categoryName, String type, String strdefault, ArrayList<ChildBean> childBeans) {
//        this.categoryId = categoryId;
//        this.categoryName = categoryName;
//        this.childBeans = childBeans;
//        this.parentType = type;
//        this.strdefault = strdefault;
//    }

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

    public ArrayList<ChildBean> getChildBeans() {
        return childBeans;
    }

    public void setChildBeans(ArrayList<ChildBean> childBeans) {
        this.childBeans = childBeans;
    }
}
