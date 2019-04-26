package com.gykj.cashier.module.cashier.entity;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 14/8/18 下午1:19
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CashierEntity {


    /**
     * code : 6903244680042
     * name : 心心相印湿厕纸
     * single_price : 8.9
     */

    private String code;
    private String name;
    private String single_price;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingle_price() {
        return single_price;
    }

    public void setSingle_price(String single_price) {
        this.single_price = single_price;
    }
}
