package com.samosys.paperai.activity.Bean;


/**
 * Created by samosys on 22/6/18.
 */

public class WorkSpaceMemberBean {


    private String objectId;
    private String username;
    private String email;
    private String updatedAt;
    private String createdAt;
    private String image;
    private String passion;
    private String title;
    private String fullname;
    private String campanyrole;
    private String communityrole;
    private String address;

    public WorkSpaceMemberBean(String objectId, String username, String email, String updatedAt, String createdAt, String passion, String img, String title, String fullname, String campanyrole, String communityrole, String address) {
        this.objectId = objectId;
        this.username = username;
        this.email = email;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.passion = passion;
        this.image = img;

        this.title = title;
        this.fullname = fullname;
        this.campanyrole = campanyrole;
        this.communityrole = communityrole;
        this.address = address;

    }


//    public WorkSpaceMemberBean(String objectId, String username, String email, String updatedAt, String createdAt, String image, String passion, String title, String fullname, String campanyrole, String communityrole, String address) {
//        this.objectId = objectId;
//        this.username = username;
//        this.email = email;
//        this.updatedAt = updatedAt;
//        this.createdAt = createdAt;
//        this.image = image;
//        this.passion = passion;
//        this.title = title;
//        this.fullname = fullname;
//        this.campanyrole = campanyrole;
//        this.communityrole = communityrole;
//        this.address = address;
//    }


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getPassion() {
        return passion;
    }

    public void setPassion(String passion) {
        this.passion = passion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCampanyrole() {
        return campanyrole;
    }

    public void setCampanyrole(String campanyrole) {
        this.campanyrole = campanyrole;
    }

    public String getCommunityrole() {
        return communityrole;
    }

    public void setCommunityrole(String communityrole) {
        this.communityrole = communityrole;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
