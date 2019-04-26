package com.gykj.cashier.module.index.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.entity.MainTabEntity;


import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午4:33
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class MainTabAdapter extends BaseQuickAdapter<MainTabEntity,BaseViewHolder> {

    public MainTabAdapter(@Nullable List<MainTabEntity> data) {
        super(R.layout.layout_main_tab,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainTabEntity item) {
        helper.setImageDrawable(R.id.tab_bg_iv,item.getTab_drawable());
        helper.setText(R.id.tab_title_tv,item.getTab_name());
        helper.addOnClickListener(R.id.tab_bg_iv);
    }
}
