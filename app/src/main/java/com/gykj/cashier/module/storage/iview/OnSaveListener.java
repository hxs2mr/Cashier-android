package com.gykj.cashier.module.storage.iview;

import java.math.BigDecimal;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 21/8/18 上午9:55
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface OnSaveListener {

    void save(long goodsId, int status, BigDecimal total);
}
