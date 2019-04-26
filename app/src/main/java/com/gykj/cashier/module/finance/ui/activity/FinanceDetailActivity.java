package com.gykj.cashier.module.finance.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gykj.cashier.R;
import com.gykj.cashier.module.finance.entity.FinanceEntity;
import com.gykj.cashier.module.finance.iview.IFinanceDetailView;
import com.gykj.cashier.module.finance.presenter.FinanceDetailPresenter;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.common.Constant;

import butterknife.BindView;

/**
 * description: 清单明细界面
 * <p>
 * author: josh.lu
 * created: 28/8/18 下午2:31
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FinanceDetailActivity extends BaseActivity implements IFinanceDetailView{

    @BindView(R.id.header)
    HeaderLayoutView header;

    @BindView(R.id.finance_billcode_tv)
    TextView mFinanceBillCodeTv;

    @BindView(R.id.finance_recyclerview)
    RecyclerView mFinanceRecyclerView;


    FinanceEntity financeEntity;
    FinanceDetailPresenter presenter;

    @Override
    public int initContentView() {
        return R.layout.activity_finance_detail;
    }

    @Override
    public void initData() {
        financeEntity = (FinanceEntity) getIntent().getExtras().getSerializable(Constant.FINANCE_ITEM);
        presenter = new FinanceDetailPresenter();
        presenter.attachView(this);
        presenter.settlementDetail(financeEntity.getId());
    }

    @Override
    public void initUi() {
        header.setLeftImage(R.mipmap.icon_back);
        header.setTitle(getString(R.string.title_finance_detail));
        header.setRightVisible(View.INVISIBLE);
        mFinanceBillCodeTv.setText(getString(R.string.finance_bill_no)+financeEntity.getSettlementNumber());
        initRecyclerView();
    }


    @Override
    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL,false);
        mFinanceRecyclerView.setLayoutManager(layoutManager);
        mFinanceRecyclerView.setAdapter(presenter.getAdapter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter.isAttached()){
            presenter.cancel();
            presenter.detachView();
        }
    }
}
