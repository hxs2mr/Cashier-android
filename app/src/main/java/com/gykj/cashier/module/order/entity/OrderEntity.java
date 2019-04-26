package com.gykj.cashier.module.order.entity;

import com.gykj.cashier.module.storage.entity.StorageListEntity;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 24/8/18 下午1:20
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class OrderEntity implements Serializable{


    private static final long serialVersionUID = 5880564152442824347L;
    /**
     * id : 7
     * orderNumber : 20180823141330010101535004810582
     * numberCount : 1
     * paymentType : 2 //余额支付--2 1--现金支付
     * accountBalance : 0
     * paymentNumber : 0.01
     * studentId : 3
     * addressId : 1
     * equipmentId : 1
     * createTime : 2018-08-23 06:13:31
     * status : 2
     * settlementStatus : 0
     * paymentType;  //支付方式
     *
     *
     */

    private int id;
    private String orderNumber;
    private double numberCount;
    private int paymentType;
    private double accountBalance;
    private double paymentNumber;
    private int studentId;
    private int addressId;
    private int equipmentId;
    private long createTime;
    private int status;
    private int settlementStatus;



    private boolean isChoosed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getNumberCount() {
        return numberCount;
    }

    public void setNumberCount(double numberCount) {
        this.numberCount = numberCount;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(double paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(int settlementStatus) {
        this.settlementStatus = settlementStatus;
    }
    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }


    @Override
    public boolean equals(Object obj) {
        if (null == obj){
            return false;
        }else {
            if(obj instanceof OrderEntity){
                OrderEntity entity = (OrderEntity) obj;
                if(entity.getId() == this.id){
                    return true;
                }
            }
        }
        return false;
    }

}
