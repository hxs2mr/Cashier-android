package com.gykj.cashier.module.storage.entity;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 下午2:40
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class BarCodeEntity implements Serializable {


    private static final long serialVersionUID = 7288057218264625253L;


    /**
     * id : 20
     * commodityBarcode : 1541495016887
     * commodityName : Cooling Coffee
     * pic : http://static.gykjewm.com/cashierSystem/goods/pic/6631c15e68154582bbeb1a96dd922cd820180927182019.jpg
     * buyPrice : 12
     * salesPrice : 20
     * total : 2947
     * shelfTime : 1541433600000
     * downTime : 1541692800000
     * effectiveDay : 3
     * createTime : 1541495054000
     * produceTime : 1537977600000
     * addressId : 20
     * status : 1
     * classificationId : 2
     */

    private int id;
    private String commodityBarcode;
    private String commodityName;
    private String pic;
    private double buyPrice;
    private double salesPrice;
    private double total;
    private long shelfTime;
    private long downTime;
    private String effectiveDay;
    private long createTime;
    private long produceTime;
    private int addressId;
    private int status;
    private int classificationId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommodityBarcode() {
        return commodityBarcode;
    }

    public void setCommodityBarcode(String commodityBarcode) {
        this.commodityBarcode = commodityBarcode;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public long getShelfTime() {
        return shelfTime;
    }

    public void setShelfTime(long shelfTime) {
        this.shelfTime = shelfTime;
    }

    public long getDownTime() {
        return downTime;
    }

    public void setDownTime(long downTime) {
        this.downTime = downTime;
    }

    public String getEffectiveDay() {
        return effectiveDay;
    }

    public void setEffectiveDay(String effectiveDay) {
        this.effectiveDay = effectiveDay;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(long produceTime) {
        this.produceTime = produceTime;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(int classificationId) {
        this.classificationId = classificationId;
    }

}
