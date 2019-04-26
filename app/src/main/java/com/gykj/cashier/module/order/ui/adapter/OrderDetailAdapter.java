package com.gykj.cashier.module.order.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.module.order.entity.OrderDetailEntity;

import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 24/8/18 下午3:19
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class OrderDetailAdapter extends BaseQuickAdapter<OrderDetailEntity,BaseViewHolder> {

    public OrderDetailAdapter(@Nullable List<OrderDetailEntity> data) {
        super(R.layout.layout_order_detail_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailEntity item) {
        helper.setText(R.id.item_order_name_tv,item.getGoodsName());
        helper.setText(R.id.item_order_number_tv,String.valueOf(item.getNum()));
        helper.setText(R.id.item_order_price_tv,String.valueOf(item.getPay()));
        helper.setText(R.id.item_order_single_tv,String.valueOf(item.getSalesPrice()));

    }
}
