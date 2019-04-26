package com.gykj.jxfvlibrary.manager;

import android.util.Log;

import com.gykj.jxfvlibrary.domain.JxFvType;
import com.gykj.jxfvlibrary.listener.OnJxFvListener;
import com.gykj.jxfvlibrary.listener.OnRecognizeListener;
import com.jaredrummler.android.shell.Shell;

import jx.vein.javajar.JXFVJavaInterface;

/**
 * desc   : 静芯指静脉Manager
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2019/1/813:41
 * version: 1.0
 */
public class JXFvManager {

    //数据库句柄
    private long dbCode;
    //指静脉设备句柄
    private long devCode;
    //接口返回状态值
    private int result;
    //错误信息
    private String errorMsg;
    //接口正确错误标准位
    private boolean flag = false;
    //手指是否检测到
    private boolean isTouched;
    //是否采集完成
    private boolean isCollected = true;
    public boolean iswhile = true;

    OnJxFvListener  mOnJxFvListener;

    private JxFvType mJxFvType;

    byte[] group1_byte = new byte[20];

    byte[] fv_bytes = new byte[166520];
    byte[] sample_bytes = new byte[166572];
    byte[] feat_bytes = new byte[1344];

    private JXFVJavaInterface mJxInterface;


    private JXFvManager(){
        mJxInterface = new JXFVJavaInterface();
        initJXFVData();
    }


    /**
     * 静态内部类单例实现
     * @return
     */
    public static    JXFvManager getInstance(){
        return JXFVHolder.instance;
    }

    private static class JXFVHolder {
        private static JXFvManager instance = new JXFvManager();
    }

    /**
     * 设置监听回调
     * @param listener
     */
    public void setOnJxFvListener(OnJxFvListener listener){
        this.mOnJxFvListener = listener;
    }

    /**
     * 得到指静脉阶段类型
     * @return
     */
    public JxFvType getJxType(){
        return mJxFvType;
    }


    /**
     * 初始化数据
     */
    public void initJXFVData(){
        //授权USB 不然usb 会报没有权限
        Shell.SU.run("su","cd /dev/bus","chmod -R 777 usb");
    }
    /**
     * 设置分组1的ID
     * @param groupID
     */
    public void setGroup1Byte(byte[] groupID){
        group1_byte = groupID;
    }

    public void jxInitUSBDriver() {
        devCode = mJxInterface.jxInitUSBDriver();
        if(devCode == 0){
            errorMsg = "获取设备失败";
            mOnJxFvListener.failed(errorMsg);
        }else {
            result = mJxInterface.jxIsFVDConnected(devCode); //链接设备
            if(result == 1){
                jxConnFVD();
            }else {
                errorMsg = "未检测到指静脉设备";
                mOnJxFvListener.failed(errorMsg);
            }
        }

    }

    private void jxConnFVD() {
        //链接指静脉设备
        result = mJxInterface.jxConnFVD(devCode);
        switch (this.result){
            case 0:
                jxIsFingerTouched();
                break;
            case -1:
                errorMsg = "未检测到指静脉设备";
                mOnJxFvListener.failed(errorMsg);
                break;
            case -2:
                errorMsg = "USB权限错误";
                mOnJxFvListener.failed(errorMsg);
                break;
            case -3:
                errorMsg = "设备未授权";
                mOnJxFvListener.failed(errorMsg);
                break;
        }
    }

    private void jxIsFingerTouched() {
        isTouched = true;
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                while (iswhile){//
                    if(isTouched){
                        //是否检测到手指ThreadManager
                        result = mJxInterface.jxIsFingerTouched(devCode);
                        if(result == 0){
                            errorMsg = "未检测到手指";
                            //listener.failed(errorMsg);
                        }else if(result == 1){
                            isTouched = false;
                            mJxFvType = JxFvType.TOUCHED;
                            mOnJxFvListener.success();
                            Log.d("yxxs","检测到手指");
                        }else {
                            errorMsg = "未知错误";
                            mOnJxFvListener.failed(errorMsg);
                        }
                    }
                }

            }
        });

    }

    public void jxInitCapEnv() {
        isCollected = true;
        //初始化采集设备
        result = mJxInterface.jxInitCapEnv(devCode);
        switch (result){
            case 0:
                while (isCollected){
                    try {
                        result = mJxInterface.jxIsVeinImgOK(devCode,fv_bytes);
                        if(result == 2){
                            isCollected = false;
                            mJxFvType = JxFvType.COLLECTED;
                            mJxInterface.jxDeInitCapEnv(devCode);
                            mOnJxFvListener.success();
                            Log.d("yxxs","采集完成");
                        }else if(result == -100){
                            errorMsg = "未知错误";
                            mOnJxFvListener.failed(errorMsg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mOnJxFvListener.failed(e.getMessage());
                    }
                }
                break;
            case -100:
                errorMsg = "初始化设备失败";
                mOnJxFvListener.failed(errorMsg);
                break;
        }
    }

    /**
     * 提取手指样本信息
     */
    public void jxLoadVeinSample() throws Exception {
        //获取手指样本
        mJxInterface.jxLoadVeinSample(devCode, sample_bytes);
        //检查样本是否质量是否可靠
        result = mJxInterface.jxCheckVeinSampleQuality(sample_bytes);
        switch (result){
            case 0:
                isCollected = false;
                mJxFvType = JxFvType.LOAD;
                mOnJxFvListener.success();
                Log.d("yxxs","提取样本成功");
                break;
            case -1:
                errorMsg = "样本亮度不合理";
                mOnJxFvListener.failed(errorMsg);
                break;
            case -2:
                errorMsg = "手指存在旋转";
                mOnJxFvListener.failed(errorMsg);
                break;
            case -3:
                errorMsg = "样本信息量过少";
                mOnJxFvListener.failed(errorMsg);
                break;
            case -4:
                errorMsg = "样本格式不正确";
                mOnJxFvListener.failed(errorMsg);
                break;
            case -5:
                errorMsg = "手指位置不合理";
                mOnJxFvListener.failed(errorMsg);
                break;
            case -100:
                errorMsg = "未知错误";
                mOnJxFvListener.failed(errorMsg);
                break;
        }

    }

    /**
     * 从样本数据里面提取特征值
     */
    public void jxGrabVeinFeature() throws Exception {
        result = mJxInterface.jxGrabVeinFeature(sample_bytes, feat_bytes);
        switch (result){
            case 0:
                mJxFvType = JxFvType.GRAB;
                mOnJxFvListener.success();
                Log.d("yxxs","提取特征值成功");
                break;
            case -1:
                errorMsg = "样本格式不正确";
                mOnJxFvListener.failed(errorMsg);
                break;
            case -100:
                errorMsg = "未知错误";
                mOnJxFvListener.failed(errorMsg);
                break;
        }
    }

    /**
     * 初始化数据库信息
     * @param dbName
     */
    public void jxInitVeinDatabase(String dbName,OnJxFvListener listener){
        int dbExist = mJxInterface.jxIsVeinDBExist(dbName);
        flag = false;
        switch (dbExist){
            case 0:
                dbExist = mJxInterface.jxCreateVeinDatabase(dbName);
                switch (dbExist){
                    case 0:
                        flag = true;
                        break;
                    case -1:
                        flag = false;
                        errorMsg = "数据库已经存在";
                        break;
                    case -100:
                        flag = false;
                        errorMsg = "未知错误";
                        break;
                }
                break;
            case 1:
                flag = true;
                break;
        }
        if(flag){
            dbCode = mJxInterface.jxInitVeinDatabase(dbName);
            if(dbCode == 0){
                errorMsg = "数据库初始化失败";
                listener.failed(errorMsg);
            }else {
                listener.success();
            }
        }else {
            listener.failed(errorMsg);
        }

    }

    /**
     * 匹配数据库所有分组指静脉
     */
    public void jxRecognizeVeinFeatureInGroup(OnRecognizeListener listener){
        if(isConnctedDb()){
            byte[] veinsId = new byte[250];
            try {
                mJxFvType = JxFvType.RECOGNIZE;
                final long start = System.currentTimeMillis();
                result = mJxInterface.jxRecognizeVeinFeatureInGroup(dbCode,feat_bytes,group1_byte,veinsId);
                if(result > 0 ){
                    listener.recognizeSuccess(new String(veinsId));
                    Log.d("yxxs","匹配到用户id="+new String(veinsId));
                    long end = System.currentTimeMillis();
                    Log.d("yxxs","耗时"+(end-start)+"ms");
                }else if(result == 0){
                    listener.recognizeSuccess(null);
                }
                switch (result){
                    case -2:
                        errorMsg = "样本格式不正确";
                        listener.recognizeFailed(errorMsg);
                        break;
                    case -1:
                        errorMsg = "数据库忙碌";
                        listener.recognizeFailed(errorMsg);
                        break;
                    case -100:
                        errorMsg = "其他错误";
                        listener.recognizeFailed(errorMsg);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("yxxs","其他错误"+e.getMessage());
            }
        }else {
            Log.d("yxxs","数据库初始化失败");
        }

    }

    /**
     * 添加指静脉到数据库
     * @param feat_buf1 指静脉特征1
     * @param feat_buf2 指静脉特征2
     * @param veinID 指静脉ID
     */
    public void jxAddTwoVeinFeature(byte[] feat_buf1, byte[] feat_buf2, byte[] veinID,OnJxFvListener listener){
        try {
            int addResult = mJxInterface.jxAddTwoVeinFeature(dbCode,feat_buf1,feat_buf2,veinID,group1_byte);
            switch (addResult){
                case 0:
                    listener.success();
                    Log.d("yxxs","添加成功");
                    break;
                case -1:
                    errorMsg = "数据库忙碌";
                    listener.failed(errorMsg);
                    break;
                case -2:
                    errorMsg = "样本格式不正确";
                    listener.failed(errorMsg);
                    break;
                case -100:
                    errorMsg = "其他错误";
                    listener.failed(errorMsg);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库移除指静脉
     * @param veinID 指静脉ID
     * @return
     */
    public void jxRemoveVeinFeature(byte[] veinID,OnJxFvListener listener){
        if(isConnctedDb()){
            try {
                result = mJxInterface.jxRemoveVeinFeature(dbCode,veinID,group1_byte);
                switch (result){
                    case 0:
                        listener.success();
                        Log.d("yxxs","删除成功");
                        break;
                    case -1:
                        errorMsg = "数据库忙碌";
                        listener.failed(errorMsg);
                        break;
                    case -100:
                        errorMsg = "未知错误";
                        listener.failed(errorMsg);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Log.d("yxxs","数据库初始化失败");
        }
    }
    /**
     * 更换指静脉分组
     * @param veinID
     */
    public void jxChangeVeinGroup( byte[] veinID,OnJxFvListener listener){
        if(isConnctedDb()){
            try {
                result = mJxInterface.jxChangeVeinGroup(dbCode,veinID,group1_byte);
                switch (result){
                    case 0:
                        listener.success();
                        Log.d("yxxs","修改成功");
                        break;
                    case -1:
                        errorMsg = "数据库忙碌";
                        listener.failed(errorMsg);
                        break;
                    case -100:
                        errorMsg = "未知错误";
                        listener.failed(errorMsg);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Log.d("yxxs","数据库初始化失败");
        }
    }

    /**
     *
     * 删除数据库指静脉
     * @return
     */
    public void jxRemoveGroupVeinFeature(OnJxFvListener listener) {
        try {
            result = mJxInterface.jxRemoveGroupVeinFeature(dbCode, group1_byte);
            switch (result) {
                case 0:
                    listener.success();
                    Log.d("yxxs", "删除成功");
                    break;
                case -1:
                    errorMsg = "数据库忙碌";
                    listener.failed(errorMsg);
                    break;
                case -100:
                    errorMsg = "未知";
                    listener.failed(errorMsg);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 释放USB资源
     */
    public void jxDeInitUSBDriver(){
        isTouched = false;
        isCollected = false;

        if(isConnectedDev()){
           // mJxInterface.jxDeInitCapEnv(devCode);
            Log.d("HXS","线程销毁! 并释放资源");
            iswhile = false;

            mJxInterface.jxDeInitUSBDriver(devCode);

        }
    }


    /**
     * 判断数据库是否链接成功
     * @return
     */
    public boolean isConnctedDb(){
        return dbCode == 0?false:true;
    }


    /**
     * 判断设备是否链接成功
     * @return
     */
    public boolean isConnectedDev(){
        return devCode == 0 ? false:true;
    }


    /**
     * 开启变量控制指静脉是否检测
     * @param isTouched
     */
    public void setIsFingerTouched(boolean isTouched){
        this.isTouched = isTouched;
    }


    /**
     * 返回指静脉数据大小
     * @return
     */
    public int jxCountGroupFeatures(){
        int count1 = 0;
        try {
            count1 = mJxInterface.jxCountGroupFeatures(dbCode,group1_byte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count1;
    }


}
