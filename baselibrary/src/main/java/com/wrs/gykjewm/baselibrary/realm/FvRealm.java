package com.wrs.gykjewm.baselibrary.realm;

import io.realm.RealmObject;

/**
 * desc   :指静脉数据库表
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/9/1216:53
 * version: 1.0
 */
public class FvRealm extends RealmObject {


    private long user_id;
    private int userType;

    private int featuresType;
    private byte[] fp_template;
    private String fv_template1;
    private String fv_template2;
    private String fv_template3;


    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public byte[] getFp_template() {
        return fp_template;
    }

    public void setFp_template(byte[] fp_template) {
        this.fp_template = fp_template;
    }

    public String getFv_template1() {
        return fv_template1;
    }

    public void setFv_template1(String fv_template1) {
        this.fv_template1 = fv_template1;
    }

    public String getFv_template2() {
        return fv_template2;
    }

    public void setFv_template2(String fv_template2) {
        this.fv_template2 = fv_template2;
    }

    public String getFv_template3() {
        return fv_template3;
    }

    public void setFv_template3(String fv_template3) {
        this.fv_template3 = fv_template3;
    }

    public int getFeaturesType() {
        return featuresType;
    }

    public void setFeaturesType(int featuresType) {
        this.featuresType = featuresType;
    }
}