package com.wrs.gykjewm.baselibrary.domain;

import java.io.Serializable;
import java.util.List;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/1612:15
 * version: 1.0
 */
public class FvEntity implements Serializable {
    private static final long serialVersionUID = 5878442406209139469L;

    /**
     * deviceId : 1
     * devieceType : 1
     * method : INSERT
     * event : FINGERS_FEATURES
     * data : {"id":null,"schoolId":62,"userId":6527,"userSex":1,"userType":1,"featuresType":0,"features1":[],"features2":"","features3":"","features4":""}
     */

    private int deviceId;
    private int devieceType;
    private String method;
    private String event;
    private DataBean data;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getDevieceType() {
        return devieceType;
    }

    public void setDevieceType(int devieceType) {
        this.devieceType = devieceType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {
        /**
         * id : null
         * schoolId : 62
         * userId : 6527
         * userSex : 1
         * userType : 1
         * featuresType : 0
         * features1 : []
         * features2 :
         * features3 :
         * features4 :
         */

        private Object id;
        private int schoolId;
        private int userId;
        private int userSex;
        private int userType;
        private int featuresType;
        private byte[] features2;
        private byte[] features3;
        private byte[] features4;
        private byte[] features1;


        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUserSex() {
            return userSex;
        }

        public void setUserSex(int userSex) {
            this.userSex = userSex;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public int getFeaturesType() {
            return featuresType;
        }

        public void setFeaturesType(int featuresType) {
            this.featuresType = featuresType;
        }

        public byte[] getFeatures2() {
            return features2;
        }

        public void setFeatures2(byte[] features2) {
            this.features2 = features2;
        }

        public byte[] getFeatures3() {
            return features3;
        }

        public void setFeatures3(byte[] features3) {
            this.features3 = features3;
        }

        public byte[] getFeatures4() {
            return features4;
        }

        public void setFeatures4(byte[] features4) {
            this.features4 = features4;
        }

        public byte[] getFeatures1() {
            return features1;
        }

        public void setFeatures1(byte[] features1) {
            this.features1 = features1;
        }



    }
}
