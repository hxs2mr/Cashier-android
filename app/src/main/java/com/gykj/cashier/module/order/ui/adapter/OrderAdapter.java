package com.gykj.cashier.module.order.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.module.order.entity.OrderEntity;
import com.wrs.gykjewm.baselibrary.utils.DateUtil;

import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 24/8/18 下午1:58
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class OrderAdapter extends BaseQuickAdapter<OrderEntity,BaseViewHolder> {


    private Context mContext;

    public OrderAdapter(Context context,@Nullable List<OrderEntity> data) {
        super(R.layout.layout_order_item,data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderEntity item) {
        helper.setText(R.id.item_order_code_tv,item.getOrderNumber());
        helper.setText(R.id.item_order_price_tv,"¥"+String.valueOf(item.getPaymentNumber()));
        helper.setText(R.id.item_order_date_tv, DateUtil.stampToDate(item.getCreateTime()));
        if(item.isChoosed()){
            helper.setTextColor(R.id.item_order_code_tv,mContext.getResources().getColor(R.color.color_ffffff));
            helper.setTextColor(R.id.item_order_price_tv,mContext.getResources().getColor(R.color.color_ffffff));
            helper.setTextColor(R.id.item_order_date_tv,mContext.getResources().getColor(R.color.color_ffffff));
            helper.getView(R.id.item_order_layout).setBackgroundColor(mContext.getResources().getColor(R.color.color_09c491));
        }else {
            helper.setTextColor(R.id.item_order_code_tv,mContext.getResources().getColor(R.color.color_1f1f1f));
            helper.setTextColor(R.id.item_order_price_tv,mContext.getResources().getColor(R.color.color_1f1f1f));
            helper.setTextColor(R.id.item_order_date_tv,mContext.getResources().getColor(R.color.color_1f1f1f));
            helper.getView(R.id.item_order_layout).setBackgroundColor(mContext.getResources().getColor(R.color.color_f4f4f4));
        }
        helper.addOnClickListener(R.id.item_order_layout);
    }
}
