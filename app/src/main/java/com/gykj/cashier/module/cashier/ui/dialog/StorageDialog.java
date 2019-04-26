package com.gykj.cashier.module.cashier.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.entity.PayType;
import com.gykj.cashier.module.cashier.iview.IPayListener;
import com.gykj.cashier.module.cashier.iview.IStorageListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc   : 是否入库Dialog
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/1610:53
 * version: 1.0
 */
public class StorageDialog extends Dialog {

    private IStorageListener storageListener;

    public StorageDialog(@NonNull Context context) {
        super(context,R.style.alert_dialog);
        setParams();
    }

    private void setParams() {
        setContentView(R.layout.dialog_storage_type_layout);
        Window window = getWindow();
        ButterKnife.bind(this);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }



    @OnClick({R.id.dialog_cancle_tv,R.id.dialog_storage_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.dialog_cancle_tv:
                dismiss();
                break;
            case R.id.dialog_storage_tv:
                storageListener.intoStorage();
                break;
        }
        dismiss();
    }


    public void setIStorageListener(IStorageListener storageListener){
        this.storageListener = storageListener;
    }
}
