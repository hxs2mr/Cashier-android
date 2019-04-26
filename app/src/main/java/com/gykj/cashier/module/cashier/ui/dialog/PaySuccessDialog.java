package com.gykj.cashier.module.cashier.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.iview.OnPrintListener;
import com.gykj.cashier.module.order.entity.UserDetailEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * desc   : 支付成功弹窗
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/11/811:11
 * version: 1.0
 */
public class PaySuccessDialog extends Dialog {

    @BindView(R.id.pay_avater_iv) CircleImageView mPayAvaterIv;
    @BindView(R.id.pay_username_tv) TextView mPayUserNameTv;
    @BindView(R.id.pay_gender_iv) ImageView mPayGenderIv;
    @BindView(R.id.pay_gender_tv) TextView mPayGenderTv;
    @BindView(R.id.pay_school_tv) TextView mPaySchoolTv;
    @BindView(R.id.pay_grade_tv) TextView mPayGradeTv;
    @BindView(R.id.pay_class_tv) TextView mPayClassTv;

    private Context mContext;

    OnPrintListener mOnPrintListener;

    public PaySuccessDialog(@NonNull Context context) {
        super(context,R.style.alert_dialog);
        this.mContext = context;
        setParams();
    }



    private void setParams() {
        setContentView(R.layout.dialog_pay_success_layout);
        Window window = getWindow();
        ButterKnife.bind(this);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.pay_cancle_tv,R.id.pay_certain_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.pay_cancle_tv:
                mOnPrintListener.printTicket(false);
                break;
            case R.id.pay_certain_tv:
                mOnPrintListener.printTicket(true);
                break;
        }
        dismiss();
    }


    public void setOnPrintListener(OnPrintListener onPrintListener){
        this.mOnPrintListener = onPrintListener;
    }

    public void showUserInfo(UserDetailEntity entity){
        RequestOptions options = new RequestOptions();
        options.centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(mContext.getResources().getDrawable(R.mipmap.icon_default_user))
                .error(mContext.getResources().getDrawable(R.mipmap.icon_default_user));
        Glide.with(mContext)
                .load(entity.getPic())
                .apply(options)
                .into(mPayAvaterIv);
        mPayUserNameTv.setText(entity.getUserName());
        if(entity.getSex().equals("男")){
            mPayGenderIv.setImageResource(R.mipmap.icon_male);
        }else {
            mPayGenderIv.setImageResource(R.mipmap.icon_female);
        }
        mPayGenderTv.setText(entity.getSex());
        mPaySchoolTv.setText(mContext.getResources().getString(R.string.school)+entity.getSchoolName());
        mPayGradeTv.setText(mContext.getResources().getString(R.string.grade)+entity.getGradeName());
        mPayClassTv.setText(mContext.getResources().getString(R.string.classfy)+entity.getClassName());
    }

}
