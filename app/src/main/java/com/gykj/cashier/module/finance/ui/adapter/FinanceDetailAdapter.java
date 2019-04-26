package com.gykj.cashier.module.finance.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.module.finance.entity.FinanceDetailEntity;
import com.wrs.gykjewm.baselibrary.utils.DateUtil;

import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 28/8/18 下午2:48
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FinanceDetailAdapter extends BaseQuickAdapter<FinanceDetailEntity,BaseViewHolder> {


    public FinanceDetailAdapter(@Nullable List<FinanceDetailEntity> data) {
        super(R.layout.layout_finance_detail_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FinanceDetailEntity item) {
        helper.setText(R.id.finance_order_code_tv,item.getOrderNumber());
        helper.setText(R.id.finance_goods_num_tv,String.valueOf(item.getOrderCount()));
        helper.setText(R.id.finance_goods_money_tv,String.valueOf(item.getOrderPay()));
        helper.setText(R.id.finance_order_time_tv, DateUtil.stampToDate(item.getOrderCreateTime()));
        String status = "";
        switch (item.getSettlementStatus()){
                case 0:
                status = "未转账";
                break;
            case 1:
                status = "转账申请";
                break;
            case 2:
                status = "已转账";
                break;
        }
        helper.setText(R.id.finance_status_tv,status);

    }
}
