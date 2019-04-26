package com.gykj.cashier.module.cashier.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.entity.CashierInfo;


import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 14/8/18 下午1:44
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CashierAdapter extends BaseQuickAdapter<CashierInfo,BaseViewHolder> {


    public CashierAdapter(@Nullable List<CashierInfo> data) {
        super(R.layout.layout_cashier_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CashierInfo item) {
        helper.setText(R.id.cashier_item_code_tv,item.getCode());
        helper.setText(R.id.cashier_item_name_tv,item.getName());
        helper.setText(R.id.cashier_item_single_price_tv,item.getSingle_price());
        helper.setText(R.id.cashier_item_amount_tv,item.getAmount());
        helper.setText(R.id.cashier_item_total_price_tv,item.getTotal_price());
        helper.addOnClickListener(R.id.cashier_item_reduce_tv);
        helper.addOnClickListener(R.id.cashier_item_pluse_tv);
        helper.addOnClickListener(R.id.cashier_item_amount_tv);
        helper.addOnClickListener(R.id.cashier_item_delete_iv);
    }
}
