package com.wrs.gykjewm.baselibrary.manager;

import com.wrs.gykjewm.baselibrary.iface.IRealmListener;
import com.wrs.gykjewm.baselibrary.realm.SchoolRealm;

import io.realm.Realm;

/**
 * desc   :学校Realm数据库管理类
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/11/710:05
 * version: 1.0
 */
public class SchoolRealmManager{

    private SchoolRealmManager(){
    }

    private static class SchoolRealmManagerHolder{
        private static SchoolRealmManager instance = new SchoolRealmManager();
    }

    public static SchoolRealmManager getManager(){
    return SchoolRealmManager.SchoolRealmManagerHolder.instance;
    }


    /**
     * 添加学校id
      * @param school_id
     * @param listener
     */
    public void addSchoolRealm(final long school_id, final IRealmListener listener) {
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SchoolRealm schoolRealm = new SchoolRealm();
                schoolRealm.setSchool_id(school_id);
                realm.copyToRealm(schoolRealm);
            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                listener.OnSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                listener.onError(error);
            }
        });
    }


    /**
     * 修改学校id
     * @param school_id
     * @param listener
     */
    public void updateSchoolToRealm(final long school_id,final IRealmListener listener){
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final SchoolRealm schoolRealm = realm.where(SchoolRealm.class).findFirst();
                schoolRealm.setSchool_id(school_id);
            }
        },  new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                listener.OnSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                listener.onError(error);
            }
        });
    }

}
