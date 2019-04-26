package com.wrs.gykjewm.baselibrary.domain;

import java.io.Serializable;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/9/413:16
 * version: 1.0
 */
public class FeatureEntity implements Serializable {

    private static final long serialVersionUID = -7438988832177271883L;
    /**
     * deviceId : 5
     * devieceType : 0
     * method : UPDATE
     * event : FACE_FEATURES
     * data : {"id":null,"schoolId":62,"userId":1222,"userType":1,"featuresType":3,"features":"6xcgcCItQk42Ig==","createTime":1536036940835}
     * descript : null
     */

    private int deviceId;
    private int devieceType;
    private String method;
    private String event;
    private DataBean data;
    private Object descript;

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

    public Object getDescript() {
        return descript;
    }

    public void setDescript(Object descript) {
        this.descript = descript;
    }

    public class DataBean implements Serializable {
        /**
         * id : null
         * schoolId : 62
         * userId : 1222
         * userType : 1
         * featuresType : 3
         * features : 6xcgcCItQk42Ig==
         * createTime : 1536036940835
         * userSex : 1
         */

        private Object id;
        private int schoolId;
        private int userId;
        private int userType;
        private int featuresType;
        private byte[] features;
        private long createTime;
        private int userSex;

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

        public byte[] getFeatures() {
            return features;
        }

        public void setFeatures(byte[] features) {
            this.features = features;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }


        public int getUserSex() {
            return userSex;
        }

        public void setUserSex(int userSex) {
            this.userSex = userSex;
        }
    }
}
