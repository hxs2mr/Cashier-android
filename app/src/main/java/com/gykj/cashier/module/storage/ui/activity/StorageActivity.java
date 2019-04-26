package com.gykj.cashier.module.storage.ui.activity;

import android.os.Handler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.cashier.R;

import com.gykj.cashier.module.setting.ui.activity.SettingActivity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;

import com.gykj.cashier.module.storage.iview.ICloseListener;
import com.gykj.cashier.module.storage.iview.IStorageView;
import com.gykj.cashier.module.storage.iview.OnSaveListener;
import com.gykj.cashier.module.storage.presenter.StoragePresenter;
import com.gykj.cashier.module.storage.ui.dialog.StorageDialog;
import com.gykj.cashier.utils.BarCodeUtils;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.utils.OthersUtils;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:商品入库
 * <p>
 * author: josh.lu
 * created: 20/8/18 上午9:20
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class StorageActivity extends BaseActivity implements IStorageView, TextWatcher,
        BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener ,ICloseListener {

    @BindView(R.id.header)
    HeaderLayoutView header;

    @BindView(R.id.storage_search_et)
    EditText mStorageSearchEt;

    @BindView(R.id.storage_recyclerview)
    RecyclerView mStorageRecyclerView;

    StoragePresenter presenter;

    StorageDialog storageDialog;


    private String last_search;


    @Override
    public int initContentView() {
        return R.layout.activity_storage;
    }

    @Override
    public void initData() {
        presenter = new StoragePresenter();
        presenter.attachView(this);
        initRecyclerView();
        storageDialog = new StorageDialog(this);
        storageDialog.setICloseListener(this);
        try {
            String code = getIntent().getExtras().getString(Constant.BARCODE);
            if (null != code && !TextUtils.isEmpty(code)) {
                last_search = code;
                if (BarCodeUtils.isBarCodeLegal(code)) {
                    presenter.electByBarcode(code);
                } else {
                    presenter.goodsList(code);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initUi() {
        header.setLeftImage(R.mipmap.icon_menu);
        header.setTitle(getString(R.string.title_storage));
        mStorageSearchEt.addTextChangedListener(this);
        presenter.getAdapter().setOnItemChildClickListener(this);
        header.setRightTvOnClickListener(this);
        header.setRightBtnOnClickListener(this);
    }

    @OnClick({R.id.storage_list_tv, R.id.search_tv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.storage_list_tv:
                showActivity(this, StorageListActivity.class);
                break;
            case R.id.search_tv:
                String content = mStorageSearchEt.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast(ToastType.WARNING, getString(R.string.cashier_hint_search));
                    return;
                }
                last_search = content;
                mStorageSearchEt.setText("");
                if (BarCodeUtils.isBarCodeLegal(content)) {
                    presenter.electByBarcode(content); //条形码
                } else {
                    presenter.goodsList(content);//商品名称
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
                            OthersUtils.hideKeyboard(StorageActivity.this);
                        }
                    }, 100);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != presenter) {
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
        if (s1.contains("\n")) {
            last_search = s1.trim();
            mStorageSearchEt.setText("");
            if (BarCodeUtils.isBarCodeLegal(s1.trim())) {
                presenter.electByBarcode(s1.trim());
            } else {
                presenter.goodsList(s1.trim());
            }
        }

    }

    @Override
    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mStorageRecyclerView.setLayoutManager(layoutManager);
        mStorageRecyclerView.setAdapter(presenter.getAdapter());
    }

    @Override
    public void showDialogData(long goodsId, String barCode, String name, double limit) {
        storageDialog.show();
        storageDialog.showDialogData(goodsId, barCode, name, limit, onSaveListener);
    }

    @Override
    public void searchLast() {
        if (BarCodeUtils.isBarCodeLegal(last_search)) {
            //presenter.electByBarcode(last_search);
        } else {
            presenter.goodsList(last_search);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.storage_layout:
                GoodsNameEntity entity = presenter.getGoodsList().get(position);
                storageDialog.show();
                storageDialog.showDialogData(entity.getId(), entity.getCommodityBarcode(), entity.getCommodityName(), entity.getTotal(), onSaveListener);
                break;
        }
    }


    private OnSaveListener onSaveListener = new OnSaveListener() {
        @Override
        public void save(long goodId, int status, BigDecimal total) {
            storageDialog.dismiss();
            presenter.addStock(goodId, status, total);
        }
    };

    @Override
    public void onClick(View v) {
        showActivity(StorageActivity.this, SettingActivity.class);
    }

    @Override
    public void hideKeyboard() {
        storageDialog.dismiss();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OthersUtils.hideKeyboard(StorageActivity.this);
            }
        }, 100);
    }
}
