package com.gykj.cashier.module.cashier.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;

import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 23/8/18 上午10:25
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CashKeyAdapter extends BaseQuickAdapter<String,BaseViewHolder> {


    public CashKeyAdapter(@Nullable List<String> data) {
        super(R.layout.layout_keyboard_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_key_code_tv,item);
        helper.addOnClickListener(R.id.item_key_code_tv);
    }
}
