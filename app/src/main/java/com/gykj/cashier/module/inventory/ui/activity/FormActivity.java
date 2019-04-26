package com.gykj.cashier.module.inventory.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gykj.cashier.R;
import com.gykj.cashier.module.inventory.entity.InventoryEntity;
import com.gykj.cashier.module.inventory.iview.IFormView;
import com.gykj.cashier.module.inventory.presenter.FormPresenter;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.common.Constant;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:表单盘点界面
 * <p>
 * author: josh.lu
 * created: 22/8/18 下午2:58
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FormActivity extends BaseActivity implements IFormView ,TextWatcher{

    @BindView(R.id.header) HeaderLayoutView header;
    @BindView(R.id.form_code_tv) TextView mFormCodeTv;
    @BindView(R.id.form_name_tv) TextView mFormNameTv;
    @BindView(R.id.form_inventory_et)
    EditText mFormInventoryEt;
    @BindView(R.id.form_origin_tv) TextView mFormOriginTv;
    @BindView(R.id.form_difference_tv) TextView mFormDifferenceTv;


    FormPresenter presenter;


    InventoryEntity.DataBean mFormData;
    BigDecimal after = null;
    BigDecimal already = null;
    BigDecimal difference = null;
    BigDecimal before = null;



    @Override
    public int initContentView() {
        return R.layout.activity_form;
    }

    @Override
    public void initData() {
        mFormData = (InventoryEntity.DataBean) getIntent().getExtras().getSerializable(Constant.FORM_ENTITY);
        presenter = new FormPresenter();
        presenter.attachView(this);
        already = new BigDecimal(mFormData.getTotal());
        already.setScale(3);
        difference = new BigDecimal(mFormData.getInventDifference());
        difference.setScale(3);
        showFormData();
    }

    @Override
    public void initUi() {
        header.setLeftImage(R.mipmap.icon_back);
        header.setTitle(getString(R.string.title_form));
        mFormInventoryEt.addTextChangedListener(this);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showFormData() {
        mFormCodeTv.setText(mFormData.getCommodityBarcode());
        mFormNameTv.setText(mFormData.getCommodityName());
        mFormOriginTv.setText(String.valueOf(mFormData.getTotal()));
        mFormDifferenceTv.setText(String.valueOf(mFormData.getInventDifference()));

    }

    @Override
    public void save() {
        String inventory = mFormInventoryEt.getText().toString().trim();
        if(TextUtils.isEmpty(inventory)){
            inventory = "0";
        }
        before = new BigDecimal(mFormOriginTv.getText().toString());
        before.setScale(3);
        after = new BigDecimal(inventory);
        after.setScale(3);
        difference = new BigDecimal(mFormDifferenceTv.getText().toString().trim());
        difference.setScale(3);
        presenter.inventoryAdd(mFormData.getId(),before,after,difference);
    }

    @OnClick({R.id.form_save_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.form_save_tv:
                save();
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
        if(s.length()<=0){
            after = new BigDecimal(0);
        }else {
            after = new BigDecimal(s1);
        }
        mFormDifferenceTv.setText(String.valueOf(Math.abs(after.subtract(already).floatValue())));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != presenter){
            presenter.cancel();
            presenter.detachView();
        }
    }
}
