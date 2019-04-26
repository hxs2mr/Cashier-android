package com.gykj.cashier.module.login.entity;

import java.io.Serializable;
import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 上午10:39
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class LoginEntity implements Serializable {


    private static final long serialVersionUID = -461250480433479872L;
    /**
     * msg : success
     * code : 0
     * data : {"expire":120,"userId":0,"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwIiwiaWF0IjoxNTM0NzMyNDgyLCJleHAiOjE1MzQ3MzI2MDJ9.r5sOkRK9XEnL7j81pyPXptE91TPMx1HeWKvnWdhzCoOAOTSK5-5knf-05ksAdRYG0pOGh0lyLgLR0tnCsE3daQ"}
     * deviceList : [{"addressName":"第一中学小卖部","deviceId":1,"deviceName":"小卖部收银机一"},{"addressName":"第一中学小卖部","deviceId":2,"deviceName":"小卖部收银机二"},{"addressName":"第一中学小卖部","deviceId":3,"deviceName":"小卖部收银机三"},{"addressName":"第一中学不好吃食堂","deviceId":4,"deviceName":"食堂一号窗口收银机"}]
     */

    private String msg;
    private int code;
    private DataBean data;
    private List<DeviceListBean> deviceList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public List<DeviceListBean> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceListBean> deviceList) {
        this.deviceList = deviceList;
    }

    public class DataBean implements Serializable {
        /**
         * expire : 120
         * userId : 0
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwIiwiaWF0IjoxNTM0NzMyNDgyLCJleHAiOjE1MzQ3MzI2MDJ9.r5sOkRK9XEnL7j81pyPXptE91TPMx1HeWKvnWdhzCoOAOTSK5-5knf-05ksAdRYG0pOGh0lyLgLR0tnCsE3daQ
         */

        private int expire;
        private int userId;
        private String token;
        private long schoolId;

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(long schoolId) {
            this.schoolId = schoolId;
        }
    }

    public class DeviceListBean implements Serializable{

        /**
         * addressName : 学校超市2
         * equipmentList : [{"deviceId":21,"deviceName":"超市设备"}]
         * addressId : 20
         */

        private String addressName;
        private int addressId;
        private List<EquipmentListBean> equipmentList;

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public int getAddressId() {
            return addressId;
        }

        public void setAddressId(int addressId) {
            this.addressId = addressId;
        }

        public List<EquipmentListBean> getEquipmentList() {
            return equipmentList;
        }

        public void setEquipmentList(List<EquipmentListBean> equipmentList) {
            this.equipmentList = equipmentList;
        }

        public class EquipmentListBean {
            /**
             * deviceId : 21
             * deviceName : 超市设备
             */

            private int deviceId;
            private String deviceName;

            public int getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(int deviceId) {
                this.deviceId = deviceId;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }
        }
    }
}
