package com.gykj.cashier.module.storage.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.module.storage.entity.StorageListEntity;
import com.wrs.gykjewm.baselibrary.utils.DateUtil;

import java.util.List;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/2611:14
 * version: 1.0
 */
public class StorageListAdapter extends BaseQuickAdapter<StorageListEntity,BaseViewHolder> {

    private Context mContext;

    public StorageListAdapter(Context context,@Nullable List<StorageListEntity> data) {
        super(R.layout.layout_storage_list_item,data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, StorageListEntity item) {
        helper.setText(R.id.item_storage_code_tv,item.getCommodityBarcode());
        helper.setText(R.id.item_storage_name_tv,item.getCommodityName());
        if(item.getStatus() == 0){
            helper.setText(R.id.item_storage_number_tv,"+ "+String.valueOf(item.getTotal()));
            helper.setTextColor(R.id.item_storage_number_tv,mContext.getResources().getColor(R.color.color_f80000));
        }else {
            helper.setText(R.id.item_storage_number_tv,"- "+ String.valueOf(item.getTotal()));
            helper.setTextColor(R.id.item_storage_number_tv,mContext.getResources().getColor(R.color.color_04b685));
        }

        helper.setText(R.id.item_storage_time_tv, DateUtil.stampToTime(item.getCreateTime()));
    }
}
