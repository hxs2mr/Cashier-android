package com.gykj.cashier.module.finance.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.cashier.R;
import com.gykj.cashier.module.finance.iview.IFinanceView;
import com.gykj.cashier.module.finance.presenter.FinancePresenter;
import com.gykj.cashier.module.setting.ui.activity.SettingActivity;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.common.Constant;

import butterknife.BindView;

/**
 * description:财务结算界面
 * <p>
 * author: josh.lu
 * created: 28/8/18 下午1:16
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FinanceActivity extends BaseActivity implements IFinanceView,View.OnClickListener,
        OnLoadMoreListener,BaseQuickAdapter.OnItemChildClickListener ,OnRefreshListener{

    @BindView(R.id.header)
    HeaderLayoutView header;

    @BindView(R.id.finance_refresh_layout)
    SmartRefreshLayout mFinanceRefreshLayout;

    @BindView(R.id.finance_recyclerview)
    RecyclerView mFinanceRecyclerView;

    FinancePresenter presenter;

    private int offset = 1;

    @Override
    public int initContentView() {
        return R.layout.activity_finance;
    }

    @Override
    public void initData() {
        presenter = new FinancePresenter();
        presenter.attachView(this);
        header.setRightTvOnClickListener(this);
        header.setRightBtnOnClickListener(this);
        presenter.getAdapter().setOnItemChildClickListener(this);
        mFinanceRefreshLayout.setOnLoadMoreListener(this);
        mFinanceRefreshLayout.setOnRefreshListener(this);
        presenter.settlementList(8,offset);
    }

    @Override
    public void initUi() {
        header.setLeftImage(R.mipmap.icon_menu);
        header.setTitle(getString(R.string.tab_settlement));
        header.setRightBtnOnClickListener(this);
        header.setRightTvOnClickListener(this);
        initRecyclerView();
    }

    //设置
    @Override
    public void onClick(View v) {
        showActivity(FinanceActivity.this, SettingActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter.isAttached()){
            presenter.cancel();
            presenter.detachView();
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        offset += 1;
        presenter.settlementList(8,offset);
    }

    @Override
    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL,false);
        mFinanceRecyclerView.setLayoutManager(layoutManager);
        mFinanceRecyclerView.setAdapter(presenter.getAdapter());
    }

    @Override
    public void finishLoading() {
        mFinanceRefreshLayout.finishLoadMore(0,true,true);
    }

    @Override
    public void finishRefreshing() {
        mFinanceRefreshLayout.finishRefresh();
    }

    /**
     * 清单明细
     * @param adapter
     * @param view     The view whihin the ItemView that was clicked
     * @param position The position of the view int the adapter
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.finance_operate_tv:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.FINANCE_ITEM,presenter.getFinanceList().get(position));
                showActivity(FinanceActivity.this,FinanceDetailActivity.class,bundle);
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        offset = 1;
        presenter.settlementList(8,offset);
    }
}
