package com.gykj.cashier.module.storage.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;

import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 下午4:53
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class StorageAdapter extends BaseQuickAdapter<GoodsNameEntity,BaseViewHolder>{


    public StorageAdapter(@Nullable List<GoodsNameEntity> data) {
        super(R.layout.layout_storage_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsNameEntity item) {
        helper.setText(R.id.storage_bar_code_tv,item.getCommodityBarcode());
        helper.setText(R.id.storage_name_tv,item.getCommodityName());
        helper.setText(R.id.storage_kinds_tv,item.getClassifyName());
        helper.setText(R.id.storage_status_tv,item.getStatus()==0?"未上架":"已上架");
        helper.setText(R.id.storage_buying_price_tv,String.valueOf(item.getBuyPrice()));
        helper.setText(R.id.storage_sale_price_tv,String.valueOf(item.getSalesPrice()));
        helper.setText(R.id.storage_storage_tv,String.valueOf(item.getTotal()));
        helper.addOnClickListener(R.id.storage_layout);
    }
}
