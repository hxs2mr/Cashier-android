package com.gykj.cashier.module.cashier.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.entity.CashierInfo;
import com.gykj.cashier.module.cashier.entity.FaceType;
import com.gykj.cashier.module.cashier.iview.ICashierView;
import com.gykj.cashier.module.cashier.iview.IRecognizeListener;
import com.gykj.cashier.module.cashier.iview.IStorageListener;
import com.gykj.cashier.module.cashier.iview.OnCheckListener;
import com.gykj.cashier.module.cashier.presenter.CashierPresenter;
import com.gykj.cashier.module.cashier.ui.dialog.FaceRecognizeDialog;
import com.gykj.cashier.module.cashier.ui.dialog.SearchDialog;
import com.gykj.cashier.module.cashier.ui.dialog.StorageDialog;
import com.gykj.cashier.module.edit.entity.SearchType;
import com.gykj.cashier.module.inventory.ui.activity.InventoryActivity;
import com.gykj.cashier.module.setting.ui.activity.SettingActivity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.gykj.cashier.module.storage.ui.activity.StorageActivity;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.realm.FaceRealm;
import com.gykj.cashier.utils.BarCodeUtils;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.wrs.gykjewm.baselibrary.utils.OthersUtils;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * description:收银台界面
 * <p>
 * author: josh.lu
 * created: 14/8/18 上午9:13
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CashierActivity extends BaseActivity implements ICashierView, TextWatcher,
        BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener, IStorageListener ,OnCheckListener {

    @BindView(R.id.header)
    HeaderLayoutView header;
    @BindView(R.id.cashier_search_et)
    EditText mCashierSearchEt;
    @BindView(R.id.cashier_recyclerview)
    RecyclerView mCashierRecyclerView;
    @BindView(R.id.cashier_cashier_tv)
    TextView mCashierCashierTv;
    @BindView(R.id.cashier_number_tv)
    TextView mCashierNumberTv;
    @BindView(R.id.cashier_money_tv)
    TextView mCashierMoneyTv;

    CashierPresenter presenter;

    StorageDialog storageDialog;
    SearchDialog mSearchDialog;

    /**
     * 上次搜索
     */
    private String last_search;
    private MaterialDialog dialog;


    @Override
    public int initContentView() {
        return R.layout.activity_cashier;
    }

    @Override
    public void initData() {
        presenter = new CashierPresenter();
        presenter.attachView(this);
        presenter.getAdapter().setOnItemChildClickListener(this);
        initRecyclerView();
    }


    @Override
    public void initUi() {
        header.setLeftVisible(View.VISIBLE);
        header.setLeftImage(R.mipmap.icon_menu);
        header.setTitle(getString(R.string.title_cashier));
        mCashierSearchEt.addTextChangedListener(this);
        header.setRightBtnOnClickListener(this);
        header.setRightTvOnClickListener(this);

        if (null == storageDialog)  {
            storageDialog = new StorageDialog(this);
        }
        storageDialog.setIStorageListener(this);
        mSearchDialog = new SearchDialog(this);
        mSearchDialog.setOnCheckListener(this);
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
            mCashierSearchEt.setText("");
            //TODO  进行商品检索
            if (BarCodeUtils.isBarCodeLegal(s1.trim())) {
                String code = s1.trim().split("\n")[0];
                last_search = code;
                presenter.selectByBarcode(code);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(null != dialog){
            dialog.dismiss();
        }
        presenter.getCashierInfoList().clear();
        presenter.getAdapter().setNewData(presenter.getCashierInfoList());
        changeBottomData();
        presenter.getCashierInfoMap().clear();

    }
    @Override
    protected void onResume() {
        super.onResume();
        mCashierSearchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            OthersUtils.hideKeyboard(CashierActivity.this);
                        }
                    }, 100);
                }
            }
        });
    }

    @OnClick({R.id.cashier_cashier_tv,R.id.search_tv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.cashier_cashier_tv:
                if (presenter.getCashierInfoList().size() <= 0) {
                    ToastUtils.showToast(ToastType.WARNING, getString(R.string.collect_please_select_goods));
                    return;
                }
                BigDecimal money = new BigDecimal(0.0f);
                money.setScale(2);
                for (int i = 0; i < presenter.getCashierInfoList().size(); i++) {
                    BigDecimal item = new BigDecimal(presenter.getCashierInfoList().get(i).getTotal_price());
                    item.setScale(2);
                    money = money.add(item);
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CAISHIER_TOTAL_PRICE, String.valueOf(money));
                bundle.putSerializable(Constant.CAISHIER_GOODS, (Serializable) presenter.getCashierInfoList());
                showActivity(CashierActivity.this, CollectActivity.class, bundle);
                break;
            case R.id.search_tv:
                searchByCodeOrName();
                break;

        }
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        final CashierInfo info = presenter.getCashierInfoMap().get(presenter.getCashierInfoList().get(position).getCode());
        switch (view.getId()) {
            case R.id.cashier_item_reduce_tv://加法操作
                presenter.subtractCashierItem(presenter.getCashierInfoList().get(position).getCode());
                break;
            case R.id.cashier_item_pluse_tv://加法操作
                presenter.addCashierItem(presenter.getCashierInfoList().get(position).getCode());
                break;
            case R.id.cashier_item_delete_iv://删除操作
                presenter.getCashierInfoList().remove(position);
                presenter.getCashierInfoMap().remove(info.getCode());
                presenter.getAdapter().setNewData(presenter.getCashierInfoList());
                changeBottomData();
                break;
            case R.id.cashier_item_amount_tv://输入数量
                dialog = new MaterialDialog.Builder(this)
                        .backgroundColor(getResources().getColor(R.color.color_ffffff))
                        .title(getString(R.string.please_input_amount))
                        .titleColor(getResources().getColor(R.color.color_029c70))
                        .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
                        .contentColor(getResources().getColor(R.color.color_333333))
                        .input(getString(R.string.please_input_amount), presenter.getCashierInfoList().get(position).getAmount(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            }
                        })
                        .positiveText("确定")
                        .positiveColor(getResources().getColor(R.color.color_0183f1))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if(null == dialog.getInputEditText().getText().toString() || TextUtils.isEmpty(dialog.getInputEditText().getText().toString())){
                                    ToastUtils.showToast(ToastType.WARNING,getResources().getString(R.string.input_good_amount));
                                    return;
                                }
                                presenter.multiplyCashierItem(presenter.getCashierInfoList().get(position).getCode(), dialog.getInputEditText().getText().toString());
                            }
                        })
                        .negativeColor(getResources().getColor(R.color.color_999999))
                        .negativeText("取消")
                        .build();
                        dialog.show();
                break;
        }
    }


    @Override
    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mCashierRecyclerView.setLayoutManager(layoutManager);
        mCashierRecyclerView.setAdapter(presenter.getAdapter());
    }

    @Override
    public void changeBottomData() {
        BigDecimal amount = new BigDecimal(0);
        amount.setScale(3, RoundingMode.HALF_UP);
        BigDecimal money = new BigDecimal(0);
        money.setScale(2,RoundingMode.HALF_UP);
        for (int i = 0; i < presenter.getCashierInfoList().size(); i++) {
            amount= amount.add(new BigDecimal(presenter.getCashierInfoList().get(i).getAmount()).setScale(3,RoundingMode.HALF_UP));
            money = money.add(new BigDecimal(presenter.getCashierInfoList().get(i).getTotal_price()).setScale(2,RoundingMode.HALF_UP));
        }
        mCashierNumberTv.setText(String.valueOf(amount));
        mCashierMoneyTv.setText(String.valueOf(money));
        mCashierCashierTv.setText(getString(R.string.cashier_cashier_money) + money);
    }
    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showStorageDialog() {
        storageDialog.show();
    }

    @Override
    public void searchByCodeOrName() {
        String content = mCashierSearchEt.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.cashier_hint_search));
            return;
        }
        mCashierSearchEt.setText("");
        if(BarCodeUtils.isBarCode(content)){
            presenter.goodsList(content,SearchType.BARCODE,1);
        }else {
            presenter.goodsList(content,SearchType.NAME,1);
        }
    }

    @Override
    public void showSearchListDialog(List<GoodsNameEntity> list) {
        mSearchDialog.setmGoodsList(list);
        mSearchDialog.show();
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
    public void onClick(View v) {
        showActivity(CashierActivity.this, SettingActivity.class);
    }

    @Override
    public void intoStorage() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BARCODE, last_search);
        showActivity(this, StorageActivity.class, bundle);
    }

    @Override
    public void checkPosition(int index) {
        if(null == presenter.getGoodList().get(index) || presenter.getGoodList().get(index).getStatus() == 0){
            ToastUtils.showToast(ToastType.WARNING,"此商品未上架，无法购买！");
        }else {
            if(presenter.getGoodList().get(index).getTotal() > 0){
                //输入条形码或名称搜索时点击回调
                presenter.changeSearchInfo(presenter.getGoodList().get(index).getCommodityBarcode(),presenter.getGoodList().get(index));
            }else {
                last_search = presenter.getGoodList().get(index).getCommodityBarcode();
                showStorageDialog();
            }
        }
   }
}
