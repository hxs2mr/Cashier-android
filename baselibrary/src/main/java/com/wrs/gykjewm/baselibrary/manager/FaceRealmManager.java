package com.wrs.gykjewm.baselibrary.manager;

import android.content.Context;

import com.rabbitmq.client.ConnectionFactory;
import com.wrs.gykjewm.baselibrary.iface.IRealmListener;
import com.wrs.gykjewm.baselibrary.realm.FaceRealm;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * desc   :Realm人脸数据库管理器
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/9/413:21
 * version: 1.0
 */
public class FaceRealmManager {

    private FaceRealmManager(){
    }

    private static class FaceRealmManagerHolder{
        private static FaceRealmManager instance = new FaceRealmManager();
    }

    public static FaceRealmManager getManager(){
        return FaceRealmManager.FaceRealmManagerHolder.instance;
    }

    /**
     * 添加人脸
     * @param user_id
     * @param featuresType
     * @param features
     */
    public void addFaceToRealm(final long user_id, final int userTpye, final int featuresType, final byte[] features, final IRealmListener listener){
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FaceRealm faceRealm = new FaceRealm();
                faceRealm.setFeatures(features);
                faceRealm.setUserType(userTpye);
                faceRealm.setFeaturesType(featuresType);
                faceRealm.setUser_id( user_id);
                realm.copyToRealm(faceRealm);
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
     * 更新人脸
     * @param user_id
     * @param featuresType
     * @param features
     */
    public void updateFaceToRealm(final long user_id,final int userType, final int featuresType, final byte[] features, final IRealmListener listener){
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final FaceRealm faceRealm = realm.where(FaceRealm.class).equalTo("user_id", user_id).equalTo("featuresType", featuresType).equalTo("userType",userType).findFirst();
                faceRealm.setFeatures(features);
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


    /**
     * 删除人脸
     * @param user_id
     * @param featuresType
     * @param features
     */
    public void deleteFaceToRealm(final long user_id, final int userType,final int featuresType, final byte[] features, final IRealmListener listener){
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final RealmResults<FaceRealm> faceRealm = realm.where(FaceRealm.class).equalTo("user_id", user_id).equalTo("userType",userType).findAll();
                faceRealm.deleteAllFromRealm();
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
}
