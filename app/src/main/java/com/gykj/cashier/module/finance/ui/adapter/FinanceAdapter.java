package com.gykj.cashier.module.finance.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.module.finance.entity.FinanceEntity;
import com.wrs.gykjewm.baselibrary.utils.DateUtil;

import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 28/8/18 下午1:56
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FinanceAdapter extends BaseQuickAdapter<FinanceEntity,BaseViewHolder> {


    public FinanceAdapter( @Nullable List<FinanceEntity> data) {
        super(R.layout.layout_finance_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FinanceEntity item) {
        helper.setText(R.id.finance_code_tv,item.getSettlementNumber());
        helper.setText(R.id.finance_money_tv,String.valueOf(item.getTotalAmount()));
        String state = "";
        if(item.getStatus() == 2){
            state = "已转账";
        }else {
            state = "申请转账";
        }
        helper.setText(R.id.finance_state_tv,state);
        helper.setText(R.id.finance_bank_flow_tv,item.getBankFlow());
        helper.setText(R.id.finance_date_tv, DateUtil.stampToDate(item.getTransferTime()));
        helper.addOnClickListener(R.id.finance_operate_tv);
    }
}
