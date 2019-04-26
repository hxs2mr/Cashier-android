package com.gykj.cashier.module.edit.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.cashier.R;
import com.gykj.cashier.module.edit.entity.SearchType;
import com.gykj.cashier.module.edit.iview.IEditView;
import com.gykj.cashier.module.edit.presenter.EditPresenter;
import com.gykj.cashier.module.setting.ui.activity.SettingActivity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.gykj.cashier.module.storage.iview.OnSaveListener;
import com.gykj.cashier.module.storage.presenter.StoragePresenter;
import com.gykj.cashier.module.storage.ui.activity.StorageListActivity;
import com.gykj.cashier.module.storage.ui.dialog.StorageDialog;
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

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:商品编辑界面
 * <p>
 * author: josh.lu
 * created: 21/8/18 下午1:10
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class EditGoodsActivity extends BaseActivity implements IEditView,TextWatcher,
        BaseQuickAdapter.OnItemChildClickListener,OnLoadMoreListener,OnRefreshListener,View.OnClickListener{

    @BindView(R.id.header)
    HeaderLayoutView header;

    @BindView(R.id.storage_search_et)
    EditText mStorageSearchEt;

    @BindView(R.id.storage_recyclerview)
    RecyclerView mStorageRecyclerView;

    @BindView(R.id.edit_refresh_layout)
    SmartRefreshLayout mEditRefreshLayout;

    @BindView(R.id.edit_scan_all_tv)
    TextView mScanAllTv;

    EditPresenter presenter;

    private String last_search ;

    private int offset  = 1;
    private int code_offset = 1;
    private int name_offset = 1;

    private SearchType mSearchType;
    private SearchType mSearchType_last;
    private String TAG="HXS";

    @Override
    public int initContentView() {
        return R.layout.activity_edit_goods;
    }

    @Override
    public void initData() {
        presenter = new EditPresenter();
        presenter.attachView(this);
        initRecyclerView();
        mSearchType= mSearchType_last= SearchType.ALL;
        presenter.goodsList("",mSearchType,offset);
    }

    @Override
    public void initUi() {
        header.setLeftImage(R.mipmap.icon_menu);
        header.setTitle(getString(R.string.title_goods_edit));
        showScanAllTv(View.INVISIBLE);
        mStorageSearchEt.addTextChangedListener(this);
        presenter.getAdapter().setOnItemChildClickListener(this);
        mEditRefreshLayout.setOnLoadMoreListener(this);
        mEditRefreshLayout.setOnRefreshListener(this);
        header.setRightTvOnClickListener(this);
        header.setRightBtnOnClickListener(this);
    }

    @OnClick({R.id.edit_add_tv,R.id.edit_scan_all_tv,R.id.search_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.edit_add_tv:
                Bundle bundle = new Bundle();
                bundle.putLong(Constant.GOODS_EDIT,-1);
                showActivity(EditGoodsActivity.this,AddGoodsActivity.class,bundle);
                break;
            case R.id.edit_scan_all_tv:
                showScanAllTv(View.INVISIBLE);
                mSearchType = SearchType.ALL;
                presenter.getCodeList().clear();
                presenter.getNameList().clear();
                presenter.getAdapter().setNewData(presenter.getGoodsList());
                mEditRefreshLayout.autoRefresh();
                break;
            case R.id.search_tv:
                String content = mStorageSearchEt.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    ToastUtils.showToast(ToastType.WARNING,getString(R.string.cashier_hint_search));
                    return;
                }
                last_search = content;
                mStorageSearchEt.setText("");
                mSearchType_last = mSearchType;
                if(BarCodeUtils.isBarCodeLegal(content)){
                    code_offset = 1;
                    mSearchType = SearchType.BARCODE;  //表示条形码 搜索
                    presenter.goodsList(content,mSearchType,code_offset);
                }else {
                    name_offset = 1;
                    mSearchType = SearchType.NAME;   //表示名称搜索
                    presenter.goodsList(content,mSearchType,name_offset);
                }
                break;
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
            Log.d(TAG,"搜索反应1"+s.toString());

        if(s1.contains("\n")){
            last_search = s1.trim();
            mStorageSearchEt.setText("");
            if(BarCodeUtils.isBarCodeLegal(s1.trim())){
                Log.d(TAG,"搜索反应2"+s.toString());
                code_offset = 1;
                mSearchType = SearchType.BARCODE;
                presenter.goodsList(s1.trim(),mSearchType,code_offset);
            }else {
                Log.d(TAG,"搜索反应3"+s.toString());
                name_offset = 1;
                mSearchType = SearchType.NAME;
                presenter.goodsList(s1.trim(),mSearchType,name_offset);
            }
        }

    }

    @Override
    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL,false);
        mStorageRecyclerView.setLayoutManager(layoutManager);
        mStorageRecyclerView.setAdapter(presenter.getAdapter());
    }


    @Override
    public void finishLoadMore(boolean noMoreData) {
        mEditRefreshLayout.finishLoadMore(0,true,noMoreData);
    }

    @Override
    public SmartRefreshLayout getRefreshLayout() {
        return mEditRefreshLayout;
    }

    @Override
    public void showScanAllTv(int visiable) {
        mScanAllTv.setVisibility(visiable);
    }

    @Override
    public void showSearchEv(int flage) {
        //用来显示  搜索条件的值  1:表示搜索有值  0:没有值
        if(flage ==1)
        {
            mStorageSearchEt.setText(last_search);
        }else {
            mStorageSearchEt.setText("");
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.storage_layout:
                //TODO 进行商品编辑
                if(mSearchType == SearchType.ALL){
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constant.GOODS_EDIT,presenter.getGoodsList().get(position).getId());
                    showActivity(EditGoodsActivity.this,AddGoodsActivity.class,bundle);
                }else if(mSearchType == SearchType.BARCODE) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constant.GOODS_EDIT,presenter.getCodeList().get(position).getId());
                    showActivity(EditGoodsActivity.this,AddGoodsActivity.class,bundle);
                }else {
                    Bundle bundle = new Bundle();
                    if(presenter.getNameList().size()>0) //表示搜索中有值
                    {
                        bundle.putLong(Constant.GOODS_EDIT,presenter.getNameList().get(position).getId());
                    }else {
                        if(mSearchType_last == SearchType.ALL)
                        {
                            bundle.putLong(Constant.GOODS_EDIT,presenter.getGoodsList().get(position).getId());
                        }else if(mSearchType_last == SearchType.BARCODE){
                            bundle.putLong(Constant.GOODS_EDIT,presenter.getCodeList().get(position).getId());
                        }
                    }
                    showActivity(EditGoodsActivity.this,AddGoodsActivity.class,bundle);
                }
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mEditRefreshLayout.autoRefresh();
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
                            OthersUtils.hideKeyboard(EditGoodsActivity.this);
                        }
                    }, 100);
                }
            }
        });
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
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        switch (mSearchType){
            case ALL:
                offset += 1;
                presenter.goodsList("",mSearchType,offset);
                break;
            case NAME:
                name_offset += 1;
                presenter.goodsList(last_search,mSearchType,name_offset);
                break;
            case BARCODE:
                code_offset += 1;
                presenter.goodsList(last_search, mSearchType,code_offset);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        showActivity(EditGoodsActivity.this, SettingActivity.class);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        switch (mSearchType){
            case ALL:
                offset = 1;
                code_offset = 1;
                name_offset  = 1;
                presenter.goodsList("",mSearchType,offset);
                break;
            case NAME:
                name_offset  = 1;
                presenter.goodsList(last_search, mSearchType,code_offset);
                break;
            case BARCODE:
                code_offset = 1;
                presenter.goodsList(last_search,mSearchType,name_offset);
                break;
        }
    }
}
