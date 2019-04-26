package com.gykj.cashier.module.inventory.entity;

import com.gykj.cashier.module.storage.entity.GoodsNameEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 22/8/18 下午1:11
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class InventoryEntity implements Serializable {


    private static final long serialVersionUID = 2608330994488391740L;
    /**
     * msg : success
     * code : 0
     * allStock : 373.41
     * inventorStock : 30
     * data : [{"total":23,"inventAfter":30,"inventDifference":11,"commodityBarcode":"9878765787687658765","createTime":"2018-08-20 05:52:41","id":2,"commodityName":"小面包","status":1},{"total":30.11,"commodityBarcode":"98787657876223434","id":4,"commodityName":"小面包2","status":1}]
     * differentStock : 11
     */

    private String msg;
    private int code;
    private double allStock;
    private int inventorStock;
    private int differentStock;
    private List<DataBean> data;

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

    public double getAllStock() {
        return allStock;
    }

    public void setAllStock(double allStock) {
        this.allStock = allStock;
    }

    public int getInventorStock() {
        return inventorStock;
    }

    public void setInventorStock(int inventorStock) {
        this.inventorStock = inventorStock;
    }

    public int getDifferentStock() {
        return differentStock;
    }

    public void setDifferentStock(int differentStock) {
        this.differentStock = differentStock;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public class DataBean implements Serializable{
        /**
         * total : 23
         * inventAfter : 30
         * inventDifference : 11
         * commodityBarcode : 9878765787687658765
         * createTime : 2018-08-20 05:52:41
         * id : 2
         * commodityName : 小面包
         * status : 1
         */

        private double total;
        private double inventAfter;
        private double inventDifference;
        private double inventBefore;
        private String commodityBarcode;
        private long createTime;
        private int id;
        private String commodityName;
        private int status;

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public double getInventAfter() {
            return inventAfter;
        }

        public void setInventAfter(double inventAfter) {
            this.inventAfter = inventAfter;
        }

        public double getInventDifference() {
            return inventDifference;
        }

        public void setInventDifference(double inventDifference) {
            this.inventDifference = inventDifference;
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

        public double getInventBefore() {
            return inventBefore;
        }

        public void setInventBefore(double inventBefore) {
            this.inventBefore = inventBefore;
        }


        @Override
        public boolean equals(Object obj) {
            if (null == obj){
                return false;
            }else {
                if(obj instanceof DataBean){
                    DataBean entity = (DataBean) obj;
                    if(entity.getId() == this.id){
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
