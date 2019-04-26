package com.gykj.cashier.module.storage.entity;

import java.io.Serializable;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 下午2:41
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class GoodsNameEntity implements Serializable{


    private static final long serialVersionUID = -4476609369528720436L;
    /**
     * buyPrice : 12
     * downTime : 1541692800000
     * salesPrice : 20
     * produceTime : 1537977600000
     * shelfTime : 1541433600000
     * pic : http://static.gykjewm.com/cashierSystem/goods/pic/6631c15e68154582bbeb1a96dd922cd820180927182019.jpg
     * classificationId : 2
     * effectiveDay : 3
     * total : 2947
     * commodityBarcode : 1541495016887
     * createTime : 1541495054000
     * id : 20
     * classifyName : 休闲食品
     * commodityName : Cooling Coffee
     * status : 1
     */

    private double buyPrice;
    private long downTime;
    private double salesPrice;
    private long produceTime;
    private long shelfTime;
    private String pic;
    private int classificationId;
    private String effectiveDay;
    private double total;
    private String commodityBarcode;
    private long createTime;
    private int id;
    private String classifyName;
    private String commodityName;
    private int status;
    private boolean isCheck;


    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public long getDownTime() {
        return downTime;
    }

    public void setDownTime(long downTime) {
        this.downTime = downTime;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public long getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(long produceTime) {
        this.produceTime = produceTime;
    }

    public long getShelfTime() {
        return shelfTime;
    }

    public void setShelfTime(long shelfTime) {
        this.shelfTime = shelfTime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(int classificationId) {
        this.classificationId = classificationId;
    }

    public String getEffectiveDay() {
        return effectiveDay;
    }

    public void setEffectiveDay(String effectiveDay) {
        this.effectiveDay = effectiveDay;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    @Override
    public boolean equals(Object obj) {
        if (null == obj){
            return false;
        }else {
            if(obj instanceof GoodsNameEntity){
                GoodsNameEntity entity = (GoodsNameEntity) obj;
                if(entity.getId() == this.id){
                    return true;
                }
            }
        }
        return false;
    }

}
