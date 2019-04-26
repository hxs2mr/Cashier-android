package com.gykj.cashier.module.storage.ui.dialog;


import android.app.Dialog;
import android.content.Context;

import android.support.annotation.NonNull;


import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gykj.cashier.R;
import com.gykj.cashier.module.storage.iview.ICloseListener;
import com.gykj.cashier.module.storage.iview.OnSaveListener;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 下午5:25
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class StorageDialog extends Dialog implements RadioGroup.OnCheckedChangeListener{

    @BindView(R.id.storage_code_tv) TextView mStorageCodeTv;
    @BindView(R.id.storage_name_tv) TextView mStorageNameTv;
    @BindView(R.id.storage_amount_tv) TextView mStorageAmountTv;
    @BindView(R.id.storage_rg) RadioGroup mStorageRadioGroup;
    @BindView(R.id.storage_add_rb)
    RadioButton mmStorageRadioButton;




    private long mGoodsId;
    private int mStatus = 0;
    private BigDecimal mTotal;
    private double limit;
    private String mBarCode;
    private String mName;

    OnSaveListener mOnSaveListener;
    ICloseListener mICloseListener;
    private Context mContext;

    public StorageDialog(@NonNull Context context) {
        this(context, R.style.alert_dialog);
        this.mContext = context;
    }

    public StorageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setParams();
        initData();
    }


    private void setParams() {
        setContentView(R.layout.dialog_storage_layout);
        ButterKnife.bind(this);
        Window window = getWindow();
        setCanceledOnTouchOutside(false);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    private void initData() {
        mStorageRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.storage_add_rb:
                mStatus = 0;
                break;
            case R.id.storage_reduce_rb:
                mStatus = 1;
                break;
        }
    }


    @OnClick({R.id.storage_pluse_tv,R.id.storage_reduce_tv,R.id.storage_amount_tv,R.id.storage_save_tv,R.id.dialog_close_iv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.storage_pluse_tv:
                pluseAmount();
                break;
            case R.id.storage_reduce_tv:
                reduceAmount();
                break;
            case R.id.storage_amount_tv:
                showInputAmontDialog();
                break;
            case R.id.storage_save_tv:
                save();
                break;
            case R.id.dialog_close_iv:
                mICloseListener.hideKeyboard();
                break;
        }
    }


    private void showInputAmontDialog() {
        new MaterialDialog.Builder(mContext)
                .backgroundColor(mContext.getResources().getColor(R.color.color_ffffff))
                .title(mContext.getResources().getString(R.string.please_input_amount))
                .titleColor(mContext.getResources().getColor(R.color.color_09c491))
                .contentColor(mContext.getResources().getColor(R.color.color_333333))
                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
                .input(mContext.getResources().getString(R.string.please_input_amount), mStorageAmountTv.getText().toString(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                    }
                })
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(mStatus == 1){
                            if(limit < Float.valueOf(dialog.getInputEditText().getText().toString().trim())){
                                mStorageAmountTv.setText(String.valueOf(limit));
                                ToastUtils.showToast(ToastType.WARNING,mContext.getResources().getString(R.string.less_than_storge)+limit);
                                return;
                            }
                        }
                        if(null == dialog.getInputEditText().getText().toString().trim() || TextUtils.isEmpty(dialog.getInputEditText().getText().toString().trim())){
                            ToastUtils.showToast(ToastType.WARNING,mContext.getResources().getString(R.string.input_good_amount));
                            return;
                        }
                        if(Double.valueOf(dialog.getInputEditText().getText().toString().trim()) == 0){
                            ToastUtils.showToast(ToastType.WARNING,mContext.getResources().getString(R.string.storage_more_than_zero));
                            return;
                        }
                        mStorageAmountTv.setText(dialog.getInputEditText().getText().toString().trim());
                    }
                })
                .negativeText("取消")
                .build()
                .show();
    }



    /**
     * 减少操作
     */
    private void reduceAmount() {
        BigDecimal amount = new BigDecimal(mStorageAmountTv.getText().toString());
        amount = amount.subtract(new BigDecimal(1));
        amount.setScale(3, RoundingMode.HALF_UP);
        if(amount.floatValue()<=0){
            ToastUtils.showToast(ToastType.WARNING,mContext.getResources().getString(R.string.less_than_zero));
            return;
        }
        mTotal = amount;
        mStorageAmountTv.setText(mTotal.toString());
    }

    /**
     * 增加操作
     */
    private void pluseAmount() {
        BigDecimal amount = new BigDecimal(mStorageAmountTv.getText().toString());
        amount = amount.add(new BigDecimal(1));
        amount.setScale(3,RoundingMode.HALF_UP);
        if(mStatus == 1){
            if(limit <  amount.floatValue()){
                mTotal = new BigDecimal(limit);
                mTotal.setScale(3,RoundingMode.HALF_UP);
                mStorageAmountTv.setText(mTotal.toString());
                ToastUtils.showToast(ToastType.WARNING,mContext.getResources().getString(R.string.less_than_storge)+limit);
                return;
            }
        }
        mTotal = amount;
        mStorageAmountTv.setText(mTotal.toString());
    }


    /**
     * 显示数据
     * @param goodsId
     * @param barCode
     * @param name
     */
    public void showDialogData(long goodsId, String barCode, String name,double limit,OnSaveListener listener) {
        this.mOnSaveListener = listener;
        this.mGoodsId = goodsId;
        this.limit = limit;
        this.mBarCode = barCode;
        this.mName = name;
        mStorageCodeTv.setText(mBarCode);
        mStorageNameTv.setText(mName);
        mStorageAmountTv.setText(String.valueOf(1));
        mmStorageRadioButton.setChecked(true);


    }


    /**
     * 保存操作
     */
    private void save(){
        mTotal = new BigDecimal(mStorageAmountTv.getText().toString().trim());
        mTotal.setScale(3,RoundingMode.HALF_UP);
        mOnSaveListener.save(mGoodsId,mStatus,mTotal);
    }

    public void setICloseListener(ICloseListener listener){
        this.mICloseListener = listener;
    }


}
