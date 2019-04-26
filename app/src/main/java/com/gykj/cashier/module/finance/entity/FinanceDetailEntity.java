package com.gykj.cashier.module.finance.entity;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 28/8/18 下午1:35
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FinanceDetailEntity implements Serializable {


    private static final long serialVersionUID = -2432406110194049529L;
    /**
     * orderNumber : 20180823141330010101535004810582
     * bankFlow : 85987853695487526
     * transferTime : 2018-08-27 08:49:51
     * orderId : 7
     * settlementStatus : 3
     * orderCreateTime : 2018-08-23 06:13:31
     * orderCount : 1.01
     * orderPay : 0.01
     * userType : 1
     * userId : 5407
     */

    private String orderNumber;
    private String bankFlow;
    private long transferTime;
    private int orderId;
    private int settlementStatus;
    private long orderCreateTime;
    private double orderCount;
    private double orderPay;
    private int userType;
    private int userId;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getBankFlow() {
        return bankFlow;
    }

    public void setBankFlow(String bankFlow) {
        this.bankFlow = bankFlow;
    }

    public long getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(long transferTime) {
        this.transferTime = transferTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(int settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public long getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(long orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public double getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(double orderCount) {
        this.orderCount = orderCount;
    }

    public double getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(double orderPay) {
        this.orderPay = orderPay;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
