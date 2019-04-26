package com.gykj.cashier.module.login.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.module.login.entity.DeviceEntity;

import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 上午11:12
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class DeviceAdapter extends BaseQuickAdapter<DeviceEntity,BaseViewHolder> {



    public DeviceAdapter(@Nullable List<DeviceEntity> data) {
        super(R.layout.layout_device_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceEntity item) {
        helper.setText(R.id.tab_title_tv,item.getDevice_name());
        if(item.isCheck()){
            helper.setImageResource(R.id.tab_check_cb,R.mipmap.icon_circle_selected);
        }else {
            helper.setImageResource(R.id.tab_check_cb,R.mipmap.icon_circle_normal);
        }
        helper.addOnClickListener(R.id.tab_layout);

    }

}
