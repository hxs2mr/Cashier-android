package com.gykj.cashier.module.edit.entity;

import java.io.Serializable;
import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 21/8/18 下午4:10
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class PhotoEntity implements Serializable {


    private static final long serialVersionUID = -7540639899128496479L;
    /**
     * msg : 12
     * code : 0
     * data : ["asd","ads"]
     */

    private String msg;
    private int code;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
