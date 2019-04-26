package com.gykj.cashier.module.login.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.tencent.bugly.beta.Beta;

import java.util.Timer;
import java.util.TimerTask;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/11/3011:12
 * version: 1.0
 */
public class UpdateService extends Service {

    private Timer mTimer;
    private TimerTask mTask;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("lanzhu","主动检测更新");
                Beta.checkUpgrade();
            }
        };
        mTimer.schedule(mTask,0,10*60*1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTask.cancel();
        mTimer.cancel();
    }
}
