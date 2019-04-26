package com.gykj.cashier.module.inventory.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.module.inventory.entity.InventoryEntity;

import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 22/8/18 下午1:39
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class InventoryAdapter extends BaseQuickAdapter<InventoryEntity.DataBean,BaseViewHolder> {


    public InventoryAdapter(@Nullable List<InventoryEntity.DataBean> data) {
        super(R.layout.layout_inventory_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InventoryEntity.DataBean item) {
        helper.setText(R.id.invent_code_tv,item.getCommodityBarcode());
        helper.setText(R.id.invent_name_tv,item.getCommodityName());
        helper.setText(R.id.invent_origin_tv,String.valueOf(item.getInventBefore()));
        helper.setText(R.id.invent_difference_tv,String.valueOf(item.getInventDifference()));
        helper.setText(R.id.invent_after_tv,String.valueOf(item.getInventAfter()));
        helper.addOnClickListener(R.id.invent_layout);
    }
}
