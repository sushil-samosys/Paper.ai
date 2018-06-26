package com.samosys.paperai.activity.Bean;

import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by samosys on 31/1/18.
 */

public class WorkspaceBean {

    String objectId;
    String user;
    ParseObject userObj;
    String mission;
    String updatedAt;
    String workspaceID;
    String workspace_name;
    String createdAt;
    ParseFile image;
    String user_name;
    String workspace_url;
    String ws_image;
    String archive;

    public WorkspaceBean(String objectId, ParseObject user, String mission, String updatedAt, String workspace_name, String createdAt, ParseFile image, String ws_image, String user_name, String workspace_url, String archive) {


        this.objectId = objectId;
        this.userObj = user;
        this.mission = mission;
        this.updatedAt = updatedAt;
        this.workspaceID = workspaceID;
        this.workspace_name = workspace_name;
        this.createdAt = createdAt;
        this.image = image;
        this.user_name = user_name;
        this.workspace_url = workspace_url;
        this.ws_image=ws_image;
        this.archive=archive;

    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getWs_image() {
        return ws_image;
    }

    public void setWs_image(String ws_image) {
        this.ws_image = ws_image;
    }

    public ParseObject getUserObj() {
        return userObj;
    }

    public void setUserObj(ParseObject userObj) {
        this.userObj = userObj;
    }

    public String getWorkspace_url() {
        return workspace_url;
    }

    public void setWorkspace_url(String workspace_url) {
        this.workspace_url = workspace_url;
    }

    public ParseFile getImage() {
        return image;
    }

    public void setImage(ParseFile image) {
        this.image = image;
    }

//    public WorkspaceBean(String objectId, String user, String mission, String updatedAt, String workspace_name,
//                         String createdAt, ParseFile image, String user_name, String workspace_url) {
//
//        this.objectId = objectId;
//        this.user = user;
//        this.mission = mission;
//        this.updatedAt = updatedAt;
//        this.workspaceID = workspaceID;
//        this.workspace_name = workspace_name;
//        this.createdAt = createdAt;
//        this.image = image;
//        this.user_name = user_name;
//        this.workspace_url = workspace_url;
//
//    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
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

    public String getWorkspace_name() {
        return workspace_name;
    }

    public void setWorkspace_name(String workspace_name) {
        this.workspace_name = workspace_name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }



    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
