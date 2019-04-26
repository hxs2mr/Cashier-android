package com.gykj.cashier.module.storage.ui.activity;

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
import android.widget.TextView;

import com.gykj.cashier.R;
import com.gykj.cashier.module.storage.entity.SearchType;
import com.gykj.cashier.module.storage.iview.IStorageListView;
import com.gykj.cashier.module.storage.presenter.StorageListPresenter;
import com.gykj.cashier.utils.BarCodeUtils;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.utils.OthersUtils;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * desc   : 商品入库记录列表界面
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/2610:29
 * version: 1.0
 */
public class StorageListActivity extends BaseActivity implements IStorageListView,TextWatcher,
        OnLoadMoreListener,OnRefreshListener {

    @BindView(R.id.header)
    HeaderLayoutView header;

    @BindView(R.id.storage_search_et)
    EditText mStorageSearchEt;

    @BindView(R.id.storage_recyclerview)
    RecyclerView mStorageRecyclerView;

    @BindView(R.id.storage_refresh_layout)
    SmartRefreshLayout mStorageRefreshLayout;

    @BindView(R.id.edit_scan_all_tv)
    TextView mScanAllTv;


    StorageListPresenter presenter;
    SearchType mSearchType;

    private int mAllOffset = 1;
    private int mNameOrCodeOffset = 1;
    private String last_search;



    @Override
    public int initContentView() {
        return R.layout.activity_storage_list;
    }

    @Override
    public void initData() {
        presenter = new StorageListPresenter();
        presenter.attachView(this);
        initListener();
        initRecyclerViewUI();
        mSearchType = SearchType.ALL;
        presenter.stockList("","",mAllOffset, mSearchType);
    }

    @Override
    public void initUi() {
        header.setLeftImage(R.mipmap.icon_back);
        header.setRightVisible(View.INVISIBLE);
        header.setTitle(getString(R.string.title_storage_list));
    }


    @OnClick({R.id.edit_scan_all_tv,R.id.search_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.edit_scan_all_tv:
                showScanAllTv(View.INVISIBLE);
                mNameOrCodeOffset = 1;
                mSearchType = SearchType.ALL;
                presenter.getAdapter().setNewData(presenter.getAllList());
                presenter.getCodeList().clear();
                presenter.getNameList().clear();
                break;
            case R.id.search_tv:
                String content = mStorageSearchEt.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    ToastUtils.showToast(ToastType.WARNING,getString(R.string.cashier_hint_search));
                    return;
                }
                last_search = content;
                mNameOrCodeOffset = 1;
                mStorageSearchEt.setText("");
                if(BarCodeUtils.isBarCodeLegal(content)){
                    mSearchType = SearchType.CODE;
                    presenter.stockList(content,"",mNameOrCodeOffset, mSearchType);
                }else {
                    mSearchType = SearchType.NAME;
                    presenter.stockList("",content,mNameOrCodeOffset, mSearchType);
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mStorageSearchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            OthersUtils.hideKeyboard(StorageListActivity.this);
                        }
                    }, 100);
                }
            }
        });
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String s1 = s.toString();
        if(s1.contains("\n")){
            mStorageSearchEt.setText("");
            last_search = s1.trim();
            if(BarCodeUtils.isBarCodeLegal(s1.trim())){
                mSearchType = SearchType.CODE;
                presenter.stockList(s1.trim(),"",mNameOrCodeOffset, mSearchType);
            }else {
                mSearchType = SearchType.NAME;
                presenter.stockList("",s1.trim(),mNameOrCodeOffset, mSearchType);
            }
        }
    }

    @Override
    public void initRecyclerViewUI() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL,false);
        mStorageRecyclerView.setLayoutManager(layoutManager);
        mStorageRecyclerView.setAdapter(presenter.getAdapter());    }

    @Override
    public void initListener() {
        mStorageSearchEt.addTextChangedListener(this);
        mStorageRefreshLayout.setOnLoadMoreListener(this);
        mStorageRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public SearchType getSearchType() {
        return mSearchType;
    }

    @Override
    public SmartRefreshLayout getRefreshLayout() {
        return mStorageRefreshLayout;
    }

    @Override
    public void showScanAllTv(int visiable) {
        mScanAllTv.setVisibility(visiable);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        switch (mSearchType){
            case ALL:
                mAllOffset++;
                presenter.stockList("","",mAllOffset, mSearchType);
                break;
            case CODE:
                mNameOrCodeOffset ++;
                presenter.stockList(mStorageSearchEt.getText().toString().trim(),"",mNameOrCodeOffset, mSearchType);
                break;
            case NAME:
                mNameOrCodeOffset ++;
                presenter.stockList("",mStorageSearchEt.getText().toString().trim(),mNameOrCodeOffset, mSearchType);
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        switch (mSearchType){
            case ALL:
                presenter.stockList("","",1, mSearchType);
                break;
            case CODE:
                mNameOrCodeOffset = 1;
                presenter.stockList(last_search,"",mNameOrCodeOffset, mSearchType);
                break;
            case NAME:
                mNameOrCodeOffset = 1;
                presenter.stockList("",last_search,mNameOrCodeOffset, mSearchType);
                break;
        }
    }
}
