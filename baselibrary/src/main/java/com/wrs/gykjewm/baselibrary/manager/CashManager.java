package com.wrs.gykjewm.baselibrary.manager;

import com.wrs.gykjewm.baselibrary.base.BaseApplication;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.Account;
import com.wrs.gykjewm.baselibrary.utils.ACache;

/**
 * description: 缓存管理
 * <p>
 * author: josh.lu
 * created: 20/8/18 下午1:07
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CashManager {

    private ACache mACache;

    private CashManager(){
        this.mACache = ACache.get(BaseApplication.getInstance());
    }

    public static CashManager getCashApi(){
        return CashManager.CashManagerHolder.instance;
    }


    private static class CashManagerHolder{
        private static CashManager instance = new CashManager();
    }

    public void saveToken(String token){
        mACache.put(Constant.TOKEN,token);
    }

    public String getToken(){
        return mACache.getAsString(Constant.TOKEN);
    }


    public void saveAccount(Account account){
        mACache.put(Account.class.getSimpleName(),account);
    }

    public Account getAccount(){
        return (Account) mACache.getAsObject(Account.class.getSimpleName());
    }


    public void saveDeviceId(String deviceId){
        mACache.put(Constant.DEVICEID,deviceId);
    }

    public String getDeviceId(){
        return mACache.getAsString(Constant.DEVICEID);
    }


    public void saveDeviceName(String deviceName){
        mACache.put(Constant.DEVICENAME,deviceName);
    }

    public String getDeviceName(){
        return mACache.getAsString(Constant.DEVICENAME);
    }


    public void clearAllData(){
        mACache.remove(Constant.TOKEN);
        mACache.remove(Account.class.getSimpleName());
        mACache.remove(Constant.DEVICEID);
        mACache.remove(Constant.DEVICENAME);
    }


    public void clearDeviceId(){
        mACache.remove(Constant.DEVICEID);
    }
    public void clearDeviceName(){
        mACache.remove(Constant.DEVICENAME);
    }

}
