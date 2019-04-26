package com.gykj.cashier.module.cashier.ui.dialog;




import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.entity.PayType;
import com.gykj.cashier.module.cashier.iview.IPayListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc   : 选择支付方式Dialog
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/1610:53
 * version: 1.0
 */
public class PayTypeDialog extends Dialog {

    private IPayListener payListener;

    public PayTypeDialog(@NonNull Context context) {
        super(context,R.style.alert_dialog);
        setParams();
    }

    private void setParams() {
        setContentView(R.layout.dialog_pay_type_layout);
        Window window = getWindow();
        ButterKnife.bind(this);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }



    @OnClick({R.id.pay_face_tv,R.id.pay_fv_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.pay_face_tv:
                payListener.showPayType(PayType.FACE);
                break;
            case R.id.pay_fv_tv:
                payListener.showPayType(PayType.FV);
                break;
        }
        dismiss();
    }


    public void setPayListener(IPayListener payListener){
        this.payListener = payListener;
    }
}
