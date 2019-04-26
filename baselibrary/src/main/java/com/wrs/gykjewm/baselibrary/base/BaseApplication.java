package com.wrs.gykjewm.baselibrary.base;

import android.app.Application;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.arcsoft.face.FaceEngine;
import com.gykj.acface.common.Constants;

import com.gykj.zkfveins.manager.ZKFVManager;
import com.lanzhu.autolayout.config.AutoLayoutConifg;
import com.lanzhu.baidutts.manager.BaiduTtsManager;
import com.squareup.leakcanary.LeakCanary;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.wrs.gykjewm.baselibrary.manager.RabbiMqEngine;
import com.wrs.gykjewm.baselibrary.utils.ThreadManager;


import java.io.File;
import io.realm.Realm;
import io.realm.RealmConfiguration;



/**
 * description:
 * <p>
 * author: josh.lu
 * created: 11/6/18 上午11:00
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class BaseApplication extends Application {

    public static BaseApplication instance = null;


    private final String FACE_DB_PATH = Environment.getExternalStorageDirectory() + File.separator+"faceDb/";



    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MultiDex.install(instance);
        AutoLayoutConifg.getInstance().init(instance);
        //CrashReport.initCrashReport(getApplicationContext(), "e4e24f1bcc", false);
        Beta.autoCheckUpgrade = false;
        Bugly.init(getApplicationContext(), "e4e24f1bcc", false);
        if (LeakCanary.isInAnalyzerProcess(instance)) {
            return;
        }
        LeakCanary.install(instance);

        //初始化人脸识别引擎   激活
        initFaceEngine();

        //realm初始化
        File file = new File(FACE_DB_PATH);
        if(!file.exists()){
            file.mkdir();
        }
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .directory(file)
                .build();
        Realm.setDefaultConfiguration(config);
        //初始化指静脉
        //ZKFVManager.getZKFVManager().setContext(instance);
        //ZKFVManager.getZKFVManager().startFingerVeinSensor();

        //初始化RabbitMq  连接设置
        RabbiMqEngine.getRabbiMqEngine().setUpConnectionFactory();
        //初始化语音
        BaiduTtsManager.getManager().initBaiduTts(this);

    }

    private void initFaceEngine() {

        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                FaceEngine faceEngine = new FaceEngine();
                final int activeCode = faceEngine.active(instance, Constants.APP_ID, Constants.SDK_KEY);
                Log.d("lanzhu","activeCode="+activeCode);
            }
        });
    }

    /**
     * 连接RabbitMq
     */
    public void connectRabbitMq(long deviceId){
        RabbiMqEngine.getRabbiMqEngine().connect(deviceId);
    }

    public String getFACE_DB_PATH() {
        return FACE_DB_PATH;
    }

}
