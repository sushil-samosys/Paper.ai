package com.samosys.paperai.activity.Bean;

import com.parse.ParseFile;

/**
 * Created by samosys on 3/4/18.
 */

public class CommentBean {
    private String post_user_id;
    private String post_user_name;
    private String txt;
    private String updatedAt;
    private String createdAt;
    private ParseFile user_imge;

    public ParseFile getUser_imge() {
        return user_imge;
    }

    public void setUser_imge(ParseFile user_imge) {
        this.user_imge = user_imge;
    }

    public CommentBean(String post_user_id, String post_user_name, ParseFile user_imge, String txt, String updatedAt, String createdAt) {
        this.post_user_id=post_user_id;
        this.post_user_name=post_user_name;
        this.user_imge=user_imge;
        this.txt=txt;
        this.updatedAt=updatedAt;
        this.createdAt=createdAt;

    }

    public String getPost_user_id() {
        return post_user_id;
    }

    public void setPost_user_id(String post_user_id) {
        this.post_user_id = post_user_id;
    }

    public String getPost_user_name() {
        return post_user_name;
    }

    public void setPost_user_name(String post_user_name) {
        this.post_user_name = post_user_name;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
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
}
