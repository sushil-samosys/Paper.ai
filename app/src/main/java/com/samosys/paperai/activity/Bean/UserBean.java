package com.samosys.paperai.activity.Bean;

import com.parse.ParseFile;

/**
 * Created by samosys on 19/2/18.
 */

public class UserBean {
    private String objectId;
    private  String username;
    private  String email;
    private  String updatedAt;
    private String createdAt;
    private String passion;
    private ParseFile profileimage;
    private  String title;
    private  String fullname;

    public UserBean(String objectId, String username, String email, String updatedAt, String createdAt, String passion, ParseFile profileimage, String title, String fullname) {
       this.objectId=objectId;
        this.username=username;
        this.email=email;
        this.updatedAt=updatedAt;
        this.createdAt=createdAt;
        this.passion=passion;
        this.profileimage=profileimage;
        this.title=title;
        this.fullname=fullname;

    }

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

    public String getPassion() {
        return passion;
    }

    public void setPassion(String passion) {
        this.passion = passion;
    }

    public ParseFile getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(ParseFile profileimage) {
        this.profileimage = profileimage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String user_name) {
        this.title = user_name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
