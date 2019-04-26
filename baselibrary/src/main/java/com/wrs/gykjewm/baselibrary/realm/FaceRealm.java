package com.wrs.gykjewm.baselibrary.realm;

import io.realm.RealmObject;

/**
 * description: realm人脸数据库
 * <p>
 * author: josh.lu
 * created: 30/8/18 上午8:45
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FaceRealm extends RealmObject {

    private long user_id;
    private int featuresType;
    private byte[] features;
    private int userSex;
    private int userType;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getFeaturesType() {
        return featuresType;
    }

    public void setFeaturesType(int featuresType) {
        this.featuresType = featuresType;
    }

    public byte[] getFeatures() {
        return features;
    }

    public void setFeatures(byte[] features) {
        this.features = features;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }




}
