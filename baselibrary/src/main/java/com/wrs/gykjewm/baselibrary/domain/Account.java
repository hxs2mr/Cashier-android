package com.wrs.gykjewm.baselibrary.domain;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 下午1:10
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class Account implements Serializable {

    private static final long serialVersionUID = 8521928817528697523L;
    private String account;
    private String pass;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
