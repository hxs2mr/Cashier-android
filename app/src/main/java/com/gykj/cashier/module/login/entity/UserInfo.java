package com.gykj.cashier.module.login.entity;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 11/6/18 下午3:10
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -5561151347391369541L;
    /**
     * session_data : MDc5M2M3ZDgyNTg0ZGRiNThlZGI4MDM5YzA2OTA0ZmRjOTQzYTk1ZTp7InVzZXJfaWQiOiI1YjFlMmRjOTU2NGYxYjE5MTg5Y2MzNGIyMDE4LTA2LTExIDA4OjE2OjU1In0=
     * expire_date : 2018-06-11 08:17:55
     */

    private String session_data;
    private String expire_date;

    public String getSession_data() {
        return session_data;
    }

    public void setSession_data(String session_data) {
        this.session_data = session_data;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }
}
