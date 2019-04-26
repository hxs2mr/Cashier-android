package com.gykj.cashier.module.finance.entity;

import com.gykj.cashier.module.inventory.entity.InventoryEntity;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 28/8/18 下午1:34
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FinanceEntity implements Serializable {


    private static final long serialVersionUID = 5094222305463484100L;
    /**
     * id : 9
     * bankFlow : 85987853695487526
     * settlementNumber : 495515353580544628665
     * schoolId : 1
     * status : 1
     * createTime : 2018-08-27 08:20:54
     * transferTime : 2018-08-27 08:49:51
     * settlementType : 2
     */

    private int id;
    private String bankFlow;
    private String settlementNumber;
    private int schoolId;
    private int status;
    private long createTime;
    private long transferTime;
    private int settlementType;
    private double totalAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankFlow() {
        return bankFlow;
    }

    public void setBankFlow(String bankFlow) {
        this.bankFlow = bankFlow;
    }

    public String getSettlementNumber() {
        return settlementNumber;
    }

    public void setSettlementNumber(String settlementNumber) {
        this.settlementNumber = settlementNumber;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(long transferTime) {
        this.transferTime = transferTime;
    }

    public int getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(int settlementType) {
        this.settlementType = settlementType;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


    @Override
    public boolean equals(Object obj) {
        if (null == obj){
            return false;
        }else {
            if(obj instanceof FinanceEntity){
                FinanceEntity entity = (FinanceEntity) obj;
                if(entity.getId() == this.id){
                    return true;
                }
            }
        }
        return false;
    }

}
