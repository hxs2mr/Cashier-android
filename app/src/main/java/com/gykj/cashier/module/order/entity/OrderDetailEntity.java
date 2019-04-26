package com.gykj.cashier.module.order.entity;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 24/8/18 下午1:22
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class OrderDetailEntity implements Serializable {


    private static final long serialVersionUID = 7732464943930831533L;
    /**
     * num : 1
     * salesPrice : 0.01
     * pay : 0.01
     * id : 9
     * goodsName : 心相印湿纸巾
     */

    private int num;
    private double salesPrice;
    private double pay;
    private int id;
    private String goodsName;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
