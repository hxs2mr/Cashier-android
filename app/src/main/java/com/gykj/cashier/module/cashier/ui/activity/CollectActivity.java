package com.gykj.cashier.module.cashier.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.entity.CashierInfo;
import com.gykj.cashier.module.cashier.entity.PayType;
import com.gykj.cashier.module.cashier.iview.ICollectView;
import com.gykj.cashier.module.cashier.iview.IPayListener;
import com.gykj.cashier.module.cashier.iview.IRecognizeListener;
import com.gykj.cashier.module.cashier.iview.OnPrintListener;
import com.gykj.cashier.module.cashier.presenter.CollectPresenter;
import com.gykj.cashier.module.cashier.ui.dialog.FaceRecognizeDialog;

import com.gykj.cashier.module.cashier.ui.dialog.PaySuccessDialog;
import com.gykj.cashier.module.cashier.ui.dialog.PayTypeDialog;
import com.gykj.cashier.module.order.entity.UserDetailEntity;
import com.gykj.cashier.widget.CashKeyboardView;
import com.hoin.usbsdk.UsbController;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.CashManager;
import com.wrs.gykjewm.baselibrary.utils.DateUtil;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;
import com.gykj.cashier.widget.HeaderLayoutView;


import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * description: 收银 界面
 * <p>
 * author: josh.lu
 * created: 15/8/18 上午10:50
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CollectActivity  extends BaseActivity implements ICollectView ,View.OnClickListener,
        BaseQuickAdapter.OnItemChildClickListener,IRecognizeListener,OnPrintListener {

    @BindView(R.id.header) HeaderLayoutView header;
    @BindView(R.id.collect_total_money_tv) TextView mCollectTotalMoneyTv;
    @BindView(R.id.collect_cash_tv) TextView mCollectCashTv;
    @BindView(R.id.collect_change_tv) TextView mCollectChangeTv;

    @BindView(R.id.collect_key_view) CashKeyboardView mCollectKeyView;

    @BindView(R.id.collect_pay_mode_layout) LinearLayout mCollectPayModeLayout;
    @BindView(R.id.collect_change_layout) LinearLayout mCollectChangeLayout;
    @BindView(R.id.collect_sperate_iv) ImageView mCollectSperateIv;
    @BindView(R.id.collect_balance_tv) TextView mCollectBalanceTv;
    @BindView(R.id.collect_cashier_tv) TextView mCollectCashierTv;

    private String total_money;
    private List<CashierInfo> cashierInfoList;

    private StringBuilder builder = new StringBuilder();
    private BigDecimal cashBigDecimal;
    private BigDecimal total;
    private int PaymentType = 1;

    private long mUserId;
    private int mUserType;

    CollectPresenter presenter;

    FaceRecognizeDialog faceDialog;


    PaySuccessDialog mPaySuccessDialog;



    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    ToastUtils.showToast(ToastType.SUCCESS,getString(R.string.cash_print_usb_connected));
                    break;
            }
        }
    };

    @Override
    public int initContentView() {
        return R.layout.activity_collect;
    }

    @Override
    public void initData() {
        total_money = (String) getIntent().getExtras().get(Constant.CAISHIER_TOTAL_PRICE);
        cashierInfoList = (List<CashierInfo>) getIntent().getExtras().getSerializable(Constant.CAISHIER_GOODS);
        presenter = new CollectPresenter();
        presenter.attachView(this);
        faceDialog = new FaceRecognizeDialog();
        presenter.createOrder(cashierInfoList);
    }

    @Override
    public void initUi() {
        mPaySuccessDialog = new PaySuccessDialog(this);
        mPaySuccessDialog.setOnPrintListener(this);
        header.setLeftImage(R.mipmap.icon_back);
        header.setRightVisible(View.VISIBLE);
        header.setTitle(getString(R.string.title_collect));
        header.setRightVisible(View.INVISIBLE);
        mCollectKeyView .setmOnClickListener(this);
        mCollectKeyView.setmOnItemChildClickListener(this);
        mCollectTotalMoneyTv.setText(getString(R.string.collect_total_money)+total_money);
        mCollectCashTv.setText(total_money);

        total = new BigDecimal(total_money);
        total.setScale(2);

        BigDecimal cash = new BigDecimal(mCollectCashTv.getText().toString());
        cash.setScale(2);

        BigDecimal change = cash.subtract(total);
        mCollectChangeTv.setText(change.toString());
    }


    @OnClick({R.id.collect_balance_tv,R.id.collect_cashier_tv,R.id.collect_vein_layout,R.id.collect_face_layout,R.id.collect_password_layout})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.collect_cashier_tv://现金支付
                mCollectPayModeLayout.setVisibility(View.INVISIBLE);
                mCollectChangeLayout.setVisibility(View.VISIBLE);
                mCollectSperateIv.setVisibility(View.VISIBLE);
                mCollectKeyView.setVisibility(View.VISIBLE);
                mCollectBalanceTv.setBackgroundResource(R.drawable.ic_cash_white_background);
                mCollectCashierTv.setBackgroundResource(R.drawable.ic_cash_background);
                mCollectCashierTv.setTextColor(getResources().getColor(R.color.color_ffffff));
                mCollectBalanceTv.setTextColor(getResources().getColor(R.color.color_09c491));
                break;
            case R.id.collect_balance_tv://余额支付
                mCollectPayModeLayout.setVisibility(View.VISIBLE);
                mCollectChangeLayout.setVisibility(View.INVISIBLE);
                mCollectSperateIv.setVisibility(View.INVISIBLE);
                mCollectKeyView.setVisibility(View.INVISIBLE);
                mCollectBalanceTv.setBackgroundResource(R.drawable.ic_cash_background);
                mCollectCashierTv.setBackgroundResource(R.drawable.ic_cash_white_background);
                mCollectBalanceTv.setTextColor(getResources().getColor(R.color.color_ffffff));
                mCollectCashierTv.setTextColor(getResources().getColor(R.color.color_09c491));
                break;
            case R.id.collect_vein_layout://指静脉
                showActivityForResult(new Intent(this,JxFvActivity.class),100);
                break;
            case R.id.collect_face_layout://刷脸
                if(null == faceDialog){
                    faceDialog = new FaceRecognizeDialog();
                    faceDialog.show(getSupportFragmentManager(),"face");
                    faceDialog.setIRecognizeListener(this);
                }else {
                    faceDialog.show(getSupportFragmentManager(),"face");
                    faceDialog.setIRecognizeListener(this);
                }
                break;
            case R.id.collect_password_layout://密码
                ToastUtils.showToast(ToastType.SUCCESS,"密码支付");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data && resultCode == 101 && requestCode == 100){
            String fv_templete_id = data.getStringExtra(Constant.DATA);
            String[] split = fv_templete_id.split("_");
            //TODO 指纹需要UserType ，待定
            recognize(Integer.valueOf(split[1]),Integer.valueOf(split[0]));
        }
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public String changePrintTitle(String title) {
        String change_title = "";
        switch (title.length()){
            case 3:
            case 4:
                change_title = "            "+title;
                break;
            case 5:
            case 6:
                change_title = "          "+title;
                break;
            case 7:
            case 8:
                change_title = "        "+title;
                break;
            case 9:
            case 10:
                change_title = "      "+title;
                break;
            case 11:
            case 12:
                change_title = "    "+title;
                break;
            case 13:
            case 14:
                change_title = "  "+title;
                break;
            case 15:
            case 16:
                change_title = title;
                break;

        }
        return change_title;
    }

    @Override
    public void printTicket(int payType,String schoolName,String userName,String orderCode,long orderTime) {
        if (null == presenter.getUsbCtrl() || null == presenter.getUsbDev()) {
            presenter.initUsbModel();
            presenter.connectUsb();
            if (null == presenter.getUsbCtrl() || null == presenter.getUsbDev()) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(ToastType.WARNING, getString(R.string.collect_cash_print_prompt));
                    }
                });
                return;
            }
        }
        if(presenter==null)
        {
            return;
        }
        if (presenter.getUsbCtrl().revByte(presenter.getUsbDev()) == 0x38) {
            return;
        }
        if (presenter.CheckUsbPermission() == true) {

            StringBuilder builder = new StringBuilder();

            builder.append("\n");
            builder.append(changePrintTitle(schoolName));
            builder.append("\n");
            builder.append(changePrintTitle(CashManager.getCashApi().getDeviceName().split("--")[0]));
            builder.append("\n");
            builder.append(changePrintTitle(CashManager.getCashApi().getDeviceName().split("--")[1]));
            builder.append("\n");
            builder.append("————————————————");
            builder.append("  "+getString(R.string.trade_code)+orderCode);
            builder.append("\n");
            builder.append(getString(R.string.trade_date)+DateUtil.stampToTime(orderTime));
            builder.append("\n");
            builder.append("————————————————");

            builder.append(" 商品名称"+"      "+"单价  "+"数量  "+"金额");
            builder.append("\n");
            builder.append("————————————————");
            builder.append("\n");
            for(int i=0;i<cashierInfoList.size();i++){
                builder.append(cashierInfoList.get(i).getName());
                builder.append("\n");
                builder.append("               ");
                builder.append(cashierInfoList.get(i).getSingle_price());
                builder.append("   ");
                builder.append(cashierInfoList.get(i).getAmount());
                builder.append("  ");
                builder.append(cashierInfoList.get(i).getTotal_price());
                builder.append("\n");
            }

            builder.append("————————————————");
            builder.append("\n");
            builder.append(getString(R.string.trade_total_money)+total_money);
            builder.append("\n");
            builder.append("————————————————");
            if(payType == 2){
                builder.append(getString(R.string.order_buy_people)+userName);
                builder.append("\n");
            }
            builder.append(getString(R.string.trade_should_total_money)+total_money);
            builder.append("\n");
            builder.append(getString(R.string.trade_actual_total_money)+mCollectCashTv.getText().toString());
            builder.append("\n");
            builder.append(getString(R.string.trade_change_total_money)+mCollectChangeTv.getText().toString().trim());
            builder.append("\n");
            builder.append("————————————————");
            builder.append("\n");
            builder.append(getString(R.string.trade_footer_prompt_one));
            builder.append("\n");
            builder.append(getString(R.string.trade_footer_prompt_two));
            builder.append("\n");
            builder.append("\n");
            builder.append("\n");
            builder.append("\n");
            builder.append("\n");

            presenter.getUsbCtrl().sendMsg(builder.toString(), "GBK", presenter.getUsbDev());
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showPrintDialog() {
        new MaterialDialog.Builder(this)
                .content(getString(R.string.print_ticket))
                .contentColor(getResources().getColor(R.color.color_333333))
                .titleColor(getResources().getColor(R.color.color_0183f1))
                .title(getString(R.string.title_prompt))
                .backgroundColorRes(com.wrs.gykjewm.baselibrary.R.color.color_ffffff)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        finishActivity();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        if(PaymentType == 1){
                            presenter.getUserInfoByAccId(PaymentType,0,presenter.getmPayEntity().getOrderNum(),presenter.getmPayEntity().getCreateTime());
                        }
                    }
                })
                .negativeColor(getResources().getColor(R.color.color_04b685))
                .positiveColor(getResources().getColor(R.color.color_0183f1))
                .positiveText(getString(R.string.sure))
                .negativeText(getString(R.string.cancle))
                .canceledOnTouchOutside(false)
                .show();    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showPaySuccessDialog(UserDetailEntity entity) {
        mPaySuccessDialog.showUserInfo(entity);
        mPaySuccessDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != presenter){
            if(null != presenter.getUsbCtrl()){
                presenter.getUsbCtrl().close();
                presenter.setUsbCtrl(null);
            }
            presenter.cancel();
            presenter.detachView();
        }
        faceDialog = null;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.key_20_tv:
                builder.append("20");
                mCollectCashTv.setText(builder.toString());
                break;
            case R.id.key_50_tv:
                builder.append("50");
                mCollectCashTv.setText(builder.toString());
                break;
            case R.id.key_100_tv:
                builder.append("100");
                mCollectCashTv.setText(builder.toString());
                break;
            case R.id.key_200_tv:
                builder.append("200");
                mCollectCashTv.setText(builder.toString());
                break;
            case R.id.key_clear_tv:
                if(builder.length()>0){
                    builder.deleteCharAt(builder.length()-1);
                }
                mCollectCashTv.setText(builder.toString());
                break;
            case R.id.key_sure_tv:
                if(null == presenter.getmOrderEntity()){
                    ToastUtils.showToast(ToastType.WARNING,"订单创建失败");
                    finish();
                    return;
                }
                PaymentType = 1;
                presenter.payOrder(PayType.CASH,presenter.getmOrderEntity().getOrderId(),PaymentType,0,1,total_money);
                break;
        }

        try {
            if(null == builder || TextUtils.isEmpty(builder.toString())){
                cashBigDecimal = new BigDecimal(mCollectCashTv.getText().toString().trim());
            }else {
                cashBigDecimal = new BigDecimal(builder.toString());
                cashBigDecimal.setScale(2);
            }
        }catch (Exception e){
            cashBigDecimal = new BigDecimal(0);
            cashBigDecimal.setScale(2);
        }

        BigDecimal changeBigDecimal = cashBigDecimal.subtract(total);
        changeBigDecimal.setScale(2);
        mCollectChangeTv.setText(changeBigDecimal.toString());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        builder.append(mCollectKeyView.getKeyCodeList().get(position));
        mCollectCashTv.setText(builder.toString());
        try {
            cashBigDecimal = new BigDecimal(builder.toString());
            cashBigDecimal.setScale(2);
        }catch (Exception e){
            cashBigDecimal = new BigDecimal(0);
            cashBigDecimal.setScale(2);
        }

        BigDecimal changeBigDecimal = cashBigDecimal.subtract(total);
        changeBigDecimal.setScale(2);
        mCollectChangeTv.setText(changeBigDecimal.toString());
    }

    @Override
    public void recognize(long user_id,int userType) {
        PaymentType = 2;
        mUserId = user_id;
        mUserType = userType;
        presenter.payOrder(PayType.FACE,presenter.getmOrderEntity().getOrderId(),PaymentType,mUserId,mUserType,total_money);
    }

    /**
     *
     * 打印回调
     * @param print
     */
    @Override
    public void printTicket(boolean print) {
        if(print){
                printTicket(2,presenter.getmUserDetailEntity().getSchoolName(),presenter.getmUserDetailEntity().getUserName(),
                        presenter.getmPayEntity().getOrderNum(),presenter.getmPayEntity().getCreateTime());
        }
        finishActivity();
    }

    @Override
    public void openMoneyBin() {
        final byte[] cmd = new byte[3];
        presenter.initUsbModel();
        presenter.connectUsb();
        if(null == presenter.getUsbCtrl() || null == presenter.getUsbDev()){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.cashier_money_bin_prompt));
            return;
        }
        final TimerTask task = new TimerTask() {
            public void run() {
                cmd[0] = 0x1B;
                cmd[1] = 0x70;
                cmd[2] = 0x01;
                presenter.getUsbCtrl().sendByte(cmd, presenter.getUsbDev());//左对齐
                presenter.getUsbCtrl().sendByte(cmd, presenter.getUsbDev());//左对齐
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 300);
    }
}
