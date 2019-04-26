package com.wrs.gykjewm.baselibrary.realm;

import io.realm.RealmObject;

/**
 * desc   : 学校Realm数据库
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/11/710:08
 * version: 1.0
 */
public class SchoolRealm extends RealmObject {

    private long school_id;


    public long getSchool_id() {
        return school_id;
    }

    public void setSchool_id(long school_id) {
        this.school_id = school_id;
    }

}
