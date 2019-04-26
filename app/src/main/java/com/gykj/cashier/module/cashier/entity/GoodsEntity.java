package com.gykj.cashier.module.cashier.entity;

import java.math.BigDecimal;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 23/8/18 下午1:56
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class GoodsEntity {

    private long goodsId;
    private BigDecimal num;


    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

}
