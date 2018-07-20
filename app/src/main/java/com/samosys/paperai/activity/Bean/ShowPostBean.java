package com.samosys.paperai.activity.Bean;

import com.parse.ParseFile;

/**
 * Created by samosys on 31/3/18.
 */

public class ShowPostBean {
    String username;
    ParseFile user_imge;
    String text;
    String objectId;
    String updatedAt;
    String createdAt;
    String post_image;

    String post_type;
    String tagUserId;
    String post_file_url;
    String userTag;
    int CommentCount;
    int likesCount;
    String post_userID;
    String post_file;

    public ShowPostBean(String username, ParseFile user_imge, String text, String objectId, String updatedAt, String createdAt, String post_image, String post_type, String tagUserId, String post_file_url, String userTag, int commentCount, int likesCount, String post_userID, String post_file) {

        this.username = username;
        this.user_imge = user_imge;

        this.text = text;
        this.objectId = objectId;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.post_image = post_image;
        this.post_type = post_type;
        this.tagUserId = tagUserId;
        this.post_file_url = post_file_url;
        this.userTag = userTag;
        this.CommentCount = commentCount;
        this.likesCount = likesCount;
        this.post_userID = post_userID;
        this.post_file = post_file;
    }

    public ParseFile getUser_imge() {
        return user_imge;
    }

    public void setUser_imge(ParseFile user_imge) {
        this.user_imge = user_imge;
    }

    public String getPost_file() {
        return post_file;
    }

    public void setPost_file(String post_file) {
        this.post_file = post_file;
    }

//    public ShowPostBean(String username, ParseFile user_imge, String text, String objectId, String updatedAt, String createdAt, String image, String post_type, String tagUserId, String post_file_url, String userTag, int commentCount, int likesCount, String post_userID, String post_file) {
//
//        this.username = username;
//        this.user_imge = user_imge;
//
//        this.text = text;
//        this.objectId = objectId;
//        this.updatedAt = updatedAt;
//        this.createdAt = createdAt;
//        this.post_image = image;
//        this.post_type = post_type;
//        this.tagUserId = tagUserId;
//        this.post_file_url = post_file_url;
//        this.userTag = userTag;
//        this.CommentCount = commentCount;
//        this.likesCount = likesCount;
//        this.post_userID = post_userID;
//this.post_file=post_file;
//
//
//
//    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getPost_userID() {
        return post_userID;
    }

    public void setPost_userID(String post_userID) {
        this.post_userID = post_userID;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTagUserId() {
        return tagUserId;
    }

    public void setTagUserId(String tagUserId) {
        this.tagUserId = tagUserId;
    }

    public String getPost_file_url() {
        return post_file_url;
    }

    public void setPost_file_url(String post_file_url) {
        this.post_file_url = post_file_url;
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int commentCount) {
        CommentCount = commentCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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


    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }
}
