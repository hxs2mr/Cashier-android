package com.gykj.cashier.module.cashier.entity;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 14/8/18 下午1:43
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CashierInfo implements Serializable{




    private static final long serialVersionUID = -927787943052127911L;
    /**
     * code : 6903244680042
     * name : 心心相印湿厕纸
     * single_price : 8.9
     */

    private long goodsId;
    private String code;
    private String name;
    private String single_price;
    private String amount;
    private String total_price;
    private double total;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
