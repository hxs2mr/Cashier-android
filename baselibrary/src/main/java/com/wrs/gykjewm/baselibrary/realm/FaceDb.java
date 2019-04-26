package com.wrs.gykjewm.baselibrary.realm;

import io.realm.RealmObject;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 30/8/18 下午4:59
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FaceDb extends RealmObject{

    private String user_id;
    private String face_no;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFace_no() {
        return face_no;
    }

    public void setFace_no(String face_no) {
        this.face_no = face_no;
    }
}
