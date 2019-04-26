package com.gykj.cashier.module.inventory.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.cashier.R;
import com.gykj.cashier.module.edit.entity.SearchType;
import com.gykj.cashier.module.inventory.iview.IInventoryView;
import com.gykj.cashier.module.inventory.presenter.InventoryPresenter;
import com.gykj.cashier.module.setting.ui.activity.SettingActivity;
import com.gykj.cashier.utils.BarCodeUtils;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.utils.OthersUtils;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 盘点界面
 * <p>
 * author: josh.lu
 * created: 22/8/18 下午1:02
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class InventoryActivity extends BaseActivity implements IInventoryView,TextWatcher,
        BaseQuickAdapter.OnItemChildClickListener,OnLoadMoreListener,OnRefreshListener,View.OnClickListener{

    @BindView(R.id.header) HeaderLayoutView header;
    @BindView(R.id.cashier_search_et) EditText mInventorySearchEt;
    @BindView(R.id.invent_recyclerview) RecyclerView mInvenstoryRecyclerView;

    @BindView(R.id.inventory_total_tv) TextView mInventoryTotalTv;
    @BindView(R.id.inventory_already_tv) TextView mInventoryAlreadyTv;
    @BindView(R.id.inventory_difference_tv) TextView mInventoryDifferenceTv;

    @BindView(R.id.inventory_total_layout)
    LinearLayout mInventoryTotalLayout;

    @BindView(R.id.invent_refresh_layout)
    SmartRefreshLayout mInventRefreshLayout;

    @BindView(R.id.edit_scan_all_tv) TextView mScanAllTv;

    InventoryPresenter presenter;

    private String last_search ;
    private int offset = 1;
    private int code_offset = 1;
    private int name_offset = 1;
    private SearchType mSearchType;

    @Override
    public int initContentView() {
        return R.layout.activity_inventory;
    }

    @Override
    public void initData() {
        presenter = new InventoryPresenter();
        presenter.attachView(this);
        initRecyclerView();
        presenter.getAdapter().setOnItemChildClickListener(this);
        mInventRefreshLayout.setOnLoadMoreListener(this);
        mInventRefreshLayout.setOnRefreshListener(this);
        mSearchType = SearchType.ALL;
        presenter.inventoryList("",mSearchType,offset);
    }

    @Override
    public void initUi() {
        header.setLeftImage(R.mipmap.icon_menu);
        header.setTitle(getString(R.string.tab_inventory));
        showScanAllTv(View.INVISIBLE);
        mInventorySearchEt.addTextChangedListener(this);
        header.setRightTvOnClickListener(this);
        header.setRightBtnOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInventorySearchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                OthersUtils.hideKeyboard(InventoryActivity.this);
                            }
                        }, 100);
                }
            }
        });
    }

    @OnClick({R.id.edit_scan_all_tv,R.id.search_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.edit_scan_all_tv:
                mSearchType = SearchType.ALL;
                showScanAllTv(View.INVISIBLE);
                presenter.getmCodeList().clear();
                presenter.getmNameList().clear();
                presenter.getAdapter().setNewData(presenter.getmInventoryList());
                mInventRefreshLayout.autoRefresh();
                break;
            case R.id.search_tv:
                String content = mInventorySearchEt.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    ToastUtils.showToast(ToastType.WARNING,getString(R.string.cashier_hint_search));
                    return;
                }
                last_search = content;
                mInventorySearchEt.setText("");
                if(BarCodeUtils.isBarCodeLegal(content)){
                    mSearchType = SearchType.BARCODE;
                    presenter.inventoryList(content, mSearchType,1);
                }else {
                    mSearchType = SearchType.NAME;
                    presenter.inventoryList(content,mSearchType,1);
                }
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        switch (mSearchType){
            case ALL:
                bundle.putSerializable(Constant.FORM_ENTITY,presenter.getmInventoryList().get(position));
                showActivity(InventoryActivity.this,FormActivity.class,bundle);
                break;
            case BARCODE:
                bundle.putSerializable(Constant.FORM_ENTITY,presenter.getmCodeList().get(position));
                showActivity(InventoryActivity.this,FormActivity.class,bundle);
                break;
            case NAME:
                bundle.putSerializable(Constant.FORM_ENTITY,presenter.getmNameList().get(position));
                showActivity(InventoryActivity.this,FormActivity.class,bundle);
                break;

        }
    }

    @Override
    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL,false);
        mInvenstoryRecyclerView.setLayoutManager(layoutManager);
        mInvenstoryRecyclerView.setAdapter(presenter.getAdapter());
    }

    @Override
    public void showTotalData() {
        mInventoryTotalLayout.setVisibility(View.VISIBLE);
        mInventoryTotalTv.setText(String.valueOf(presenter.getmInventoryEntity().getAllStock()));
        mInventoryAlreadyTv.setText(String.valueOf(presenter.getmInventoryEntity().getInventorStock()));
        mInventoryDifferenceTv.setText(String.valueOf(presenter.getmInventoryEntity().getDifferentStock()));
    }

    @Override
    public void finishLoading(boolean noMoreData) {
        mInventRefreshLayout.finishLoadMore(0,true,noMoreData);
    }

    @Override
    public void finishRefreshing() {
        mInventRefreshLayout.finishRefresh();
    }

    @Override
    public void showScanAllTv(int visiable) {
        mScanAllTv.setVisibility(visiable);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mInventRefreshLayout.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != presenter){
            presenter.cancel();
            presenter.detachView();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String s1 = s.toString();
        if(s1.contains("\n")){
            last_search = s1.trim();
            mInventorySearchEt.setText("");
            if(BarCodeUtils.isBarCodeLegal(s1.trim())){
                mSearchType = SearchType.BARCODE;
                presenter.inventoryList(s1.trim(), mSearchType,1);
            }else {
                mSearchType = SearchType.NAME;
                presenter.inventoryList(s1.trim(),mSearchType,1);
            }
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if(null == last_search || TextUtils.isEmpty(last_search)){
            offset += 1;
            mSearchType = SearchType.ALL;
            presenter.inventoryList("",mSearchType,offset);
        }else {
            if(BarCodeUtils.isBarCodeLegal(last_search.trim())){
                code_offset += 1;
                mSearchType = SearchType.BARCODE;
                presenter.inventoryList(last_search.trim(), mSearchType,code_offset);
            }else {
                name_offset += 1;
                mSearchType = SearchType.NAME;
                presenter.inventoryList(last_search.trim(),mSearchType,name_offset);
            }
        }
    }

    @Override
    public void onClick(View v) {
        showActivity(InventoryActivity.this, SettingActivity.class);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        switch (mSearchType){
            case ALL:
                offset = 1;
                mSearchType = SearchType.ALL;
                presenter.inventoryList("",mSearchType,offset);
                break;
            case NAME:
                name_offset = 1;
                mSearchType = SearchType.NAME;
                presenter.inventoryList(last_search.trim(),mSearchType,name_offset);
                break;
            case BARCODE:
                code_offset = 1;
                mSearchType = SearchType.BARCODE;
                presenter.inventoryList(last_search.trim(), mSearchType,code_offset);
                break;
        }
    }
}
