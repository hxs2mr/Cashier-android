package com.gykj.cashier.module.login.entity;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 下午12:55
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class TokenEntity implements Serializable {


    private static final long serialVersionUID = 3469441437944354963L;
    /**
     * expire : 7200
     * token :
     */

    private int expire;
    private String token;

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
