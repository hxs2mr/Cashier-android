package com.wrs.gykjewm.baselibrary.manager;

import android.app.Activity;
import android.util.Base64;

import com.wrs.gykjewm.baselibrary.base.BaseApplication;
import com.wrs.gykjewm.baselibrary.iface.IRealmListener;
import com.wrs.gykjewm.baselibrary.realm.FaceRealm;
import com.wrs.gykjewm.baselibrary.realm.FvRealm;
import com.wrs.gykjewm.baselibrary.utils.ThreadManager;
import com.wrs.gykjewm.baselibrary.utils.WriteLogUtils;
import com.zkteco.android.biometric.module.fingervein.FingerVeinService;

import java.lang.reflect.Array;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * desc   : Realm指静脉数据库管理器
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/9/1216:59
 * version: 1.0
 */
@Deprecated
public class FvRealmManager {

    private FvRealmManager() {
    }

    private static class FvRealmManagerHolder {
        private static FvRealmManager instance = new FvRealmManager();
    }

    public static FvRealmManager getManager() {
        return FvRealmManager.FvRealmManagerHolder.instance;
    }


    /**
     * 添加指静脉
     *
     * @param user_id
     * @param fp_template
     * @param listener
     */
    public void addFvRealm(final long user_id, final int userType, final int featuresType, final byte[] fp_template, final String[] fv_templates, final IRealmListener listener) {
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FvRealm fvRealm = new FvRealm();
                fvRealm.setFp_template(fp_template);
                fvRealm.setUser_id(user_id);
                fvRealm.setFeaturesType(featuresType);
                fvRealm.setUserType(userType);
                fvRealm.setFv_template1(fv_templates[0]);
                fvRealm.setFv_template2(fv_templates[1]);
                fvRealm.setFv_template3(fv_templates[2]);
                realm.copyToRealm(fvRealm);
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
     * 更新指静脉
     *
     * @param user_id
     * @param fv_template
     * @param listener
     */
    public void updateFvToRealm(final long user_id, final int userType,final int featuresType,final byte[] fv_template, final IRealmListener listener) {
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final FvRealm fvRealm = realm.where(FvRealm.class).equalTo("user_id", user_id).equalTo("userType", userType).equalTo("featuresType", featuresType).findFirst();
                fvRealm.setFp_template(fv_template);
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
     * 删除指静脉
     *
     * @param user_id
     * @param listener
     */
    public void deleteFvToRealm(final long user_id,final int userType,final int featuresType, final IRealmListener listener) {
        final Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final FvRealm fvRealm = realm.where(FvRealm.class).equalTo("user_id", user_id).equalTo("userType", userType).findFirst();
                fvRealm.deleteFromRealm();
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
     * 指静脉加载数据到内存中
     */
    public void loadFvData2Memery(){
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                String[] fv_String = new String[3];
                FingerVeinService.clearRegTemplates();
                RealmResults<FvRealm> all = Realm.getDefaultInstance().where(FvRealm.class).findAll();
                for(int i = 0;i<all.size();i++){
                    fv_String[0] = all.get(i).getFv_template1();
                    fv_String[1] = all.get(i).getFv_template2();
                    fv_String[2] = all.get(i).getFv_template3();
//                    WriteLogUtils.getManager().writeTxtToFile(fv_String[0],BaseApplication.getInstance().getFACE_DB_PATH(),"FV0");
//                    WriteLogUtils.getManager().writeTxtToFile(fv_String[1],BaseApplication.getInstance().getFACE_DB_PATH(),"FV1");
//                    WriteLogUtils.getManager().writeTxtToFile(fv_String[2],BaseApplication.getInstance().getFACE_DB_PATH(),"FV2");

                    String user_type = String.valueOf(all.get(i).getUserType());
                    String user_id = String.valueOf(all.get(i).getUser_id());
                    String featureType = String.valueOf(all.get(i).getFeaturesType());

                    //模板ID = UserType + userid
                    FingerVeinService.addRegTemplate(user_type+"_"+user_id+"_"+featureType,all.get(i).getFp_template(),fv_String);
                }

            }
        });

    }
}