package com.gykj.cashier.module.storage.entity;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/2610:30
 * version: 1.0
 */
public class StorageListEntity {


    /**
     * total : 4
     * commodityBarcode : 6903244680042
     * createTime : 1538033865000
     * stockId : 27
     * classifyName : 美妆个户清洁
     * status : 0
     * commodityName : 心相印湿纸巾
     */

    private double total;
    private String commodityBarcode;
    private long createTime;
    private int stockId;
    private String classifyName;
    private int status;
    private String commodityName;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCommodityBarcode() {
        return commodityBarcode;
    }

    public void setCommodityBarcode(String commodityBarcode) {
        this.commodityBarcode = commodityBarcode;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }


    @Override
    public boolean equals(Object obj) {
        if (null == obj){
            return false;
        }else {
            if(obj instanceof StorageListEntity){
                StorageListEntity entity = (StorageListEntity) obj;
                if(entity.getStockId() == this.stockId){
                    return true;
                }
            }
        }
        return false;
    }
}
