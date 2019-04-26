package com.gykj.zkfveins.manager;

import android.content.Context;
import android.util.Log;

import com.gykj.zkfveins.common.Constant;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.LogHelper;
import com.zkteco.android.biometric.module.fingervein.FingerVeinCaptureListener;
import com.zkteco.android.biometric.module.fingervein.FingerVeinFactory;
import com.zkteco.android.biometric.module.fingervein.FingerVeinSensor;
import com.zkteco.android.biometric.module.fingervein.exception.FingerVeinException;

import java.util.HashMap;
import java.util.Map;

/**
 * desc   : 指静脉指纹管理类
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/9/1216:42
 * version: 1.0
 */
@Deprecated
public class ZKFVManager {

    private FingerVeinSensor fingerVeinSensor;
    private Context mContext;

    public FingerVeinCaptureListener captureListener;

    private ZKFVManager(){}


    public static ZKFVManager getZKFVManager(){
        return ZKFVManagerHolder.instance;
    }

    private static class ZKFVManagerHolder{
        private static ZKFVManager instance = new ZKFVManager();
    }

    public void setContext(Context context){
        this.mContext = context;
    }


    /**
     * 启动指静脉设备
     */
    public void startFingerVeinSensor(){
        if(null == mContext){
            throw new RuntimeException("启动指静脉设备请先设置Context上下文");
        }
        // Define output log level
        LogHelper.setLevel(Log.VERBOSE);
        // Start fingerprint sensor
        Map fingerprintParams = new HashMap();
        //set vid
        fingerprintParams.put(ParameterHelper.PARAM_KEY_VID, Constant.FV_VID);
        //set pid
        fingerprintParams.put(ParameterHelper.PARAM_KEY_PID, Constant.FV_PID);
        fingerVeinSensor = FingerVeinFactory.createFingerprintSensor(mContext, TransportType.USB, fingerprintParams);
    }


    /**
     * 开始指静脉检查
     * @param fingerVeinCaptureListener
     * @throws FingerVeinException
     */
    public void startFingerVeinCheck(FingerVeinCaptureListener fingerVeinCaptureListener) throws FingerVeinException{
        fingerVeinSensor.open(0);
        this.captureListener = fingerVeinCaptureListener;
        fingerVeinSensor.setFingerVeinCaptureListener(0, captureListener);
        fingerVeinSensor.startCapture(0);
    }


    public void stopFingerVeinCheck(){
        try {
            fingerVeinSensor.stopCapture(0);
        } catch (FingerVeinException e) {
            e.printStackTrace();
        }
    }

    public void registFingerVein(){

    }

    public void matchingFingerVein(){

    }


    /**
     * 销毁指静脉连接设备
     */
    public void destroyFingerVeinSensor(){
        if(null != fingerVeinSensor){
            FingerVeinFactory.destroy(fingerVeinSensor);
        }
    }

    public FingerVeinSensor getFingerVeinSensor() {
        return fingerVeinSensor;
    }
}
