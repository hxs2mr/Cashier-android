package com.gykj.cashier.module.cashier.entity;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 23/8/18 下午1:42
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class OrderEntity {

    /**
     * msg : success
     * code : 0
     * orderId : 6
     * allPay : 1280
     */

    private String msg;
    private int code;
    private int orderId;
    private double allPay;

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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getAllPay() {
        return allPay;
    }

    public void setAllPay(double allPay) {
        this.allPay = allPay;
    }
}
