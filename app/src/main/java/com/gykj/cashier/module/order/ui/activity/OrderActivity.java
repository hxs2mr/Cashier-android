package com.gykj.cashier.module.order.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.cashier.R;

import com.gykj.cashier.module.order.entity.OrderEntity;
import com.gykj.cashier.module.order.entity.OrderType;
import com.gykj.cashier.module.order.entity.PayType;
import com.gykj.cashier.module.order.entity.UserDetailEntity;
import com.gykj.cashier.module.order.iview.IOrderView;
import com.gykj.cashier.module.order.presenter.OrderPresenter;
import com.gykj.cashier.module.setting.ui.activity.SettingActivity;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.hoin.usbsdk.UsbController;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.CashManager;
import com.wrs.gykjewm.baselibrary.utils.DateUtil;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;



import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * description:销售单据界面
 * <p>
 * author: josh.lu
 * created: 24/8/18 下午1:19
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class OrderActivity extends BaseActivity implements IOrderView ,TextView.OnEditorActionListener,
        BaseQuickAdapter.OnItemChildClickListener,OnLoadMoreListener,OnRefreshListener,View.OnClickListener,
        TextWatcher{

    @BindView(R.id.header) HeaderLayoutView header;
    @BindView(R.id.order_search_et) EditText mOrderSearchEt;
    @BindView(R.id.order_recyclerview) RecyclerView mOrderRecyclerView;
    @BindView(R.id.order_detail_recyclerview) RecyclerView mOrderDetailRecyclerView;
    @BindView(R.id.order_type_tv) TextView mOrderTypeTv;
    @BindView(R.id.order_detail_total_number_tv) TextView mOrderNumberTv;
    @BindView(R.id.order_detail_total_money_tv) TextView mOrderMoneyTv;
    @BindView(R.id.order_detail_should_collect_tv) TextView mOrderShouldTv;
    @BindView(R.id.order_detail_actual_collect_tv) TextView mOrderActualTv;
    @BindView(R.id.order_avater_iv) CircleImageView mOrderAvaterIv;
    @BindView(R.id.order_name_tv) TextView mOrderNameTv;

    @BindView(R.id.order_refresh_layout)
    SmartRefreshLayout mOrderRefreshLayout;


    OrderPresenter presenter;

    private OrderEntity orderEntity;
    private OrderType mOrderType;

    private int offset = 1;

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
        return R.layout.activity_order;
    }

    @Override
    public void initData() {
        presenter = new OrderPresenter();
        presenter.attachView(this);
        initRecyclerView();
        mOrderType = OrderType.ALL;

        presenter.orderList("",mOrderType,offset);

        presenter.getOrderAdapter().setEnableLoadMore(true);
    }

    @Override
    public void initUi() {
        header.setLeftImage(R.mipmap.icon_menu);
        header.setTitle(getString(R.string.title_order));
        presenter.getOrderAdapter().setOnItemChildClickListener(this);
        mOrderRefreshLayout.setOnLoadMoreListener(this);
        mOrderRefreshLayout.setOnRefreshListener(this);
        mOrderSearchEt.setOnEditorActionListener(this);
        header.setRightBtnOnClickListener(this);
        header.setRightTvOnClickListener(this);
        mOrderSearchEt.addTextChangedListener(this);
    }



    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (mOrderType){
            case ALL:
                mOrderTypeTv.setText(presenter.getOrderList().get(position).getPaymentType() == 1?"支付类型：现金支付":"支付类型：余额支付");
                presenter.orderDetail(presenter.getOrderList().get(position).getId());
                showTotalData(presenter.getOrderList().get(position));
                orderEntity = presenter.getOrderList().get(position);
                presenter.changeListChooseIndex(presenter.getOrderList(),position);
                break;
            case CODE:
                mOrderTypeTv.setText(presenter.getCodeList().get(position).getPaymentType() == 1?"支付类型：现金支付":"支付类型：余额支付");
                presenter.orderDetail(presenter.getCodeList().get(position).getId());
                showTotalData(presenter.getCodeList().get(position));
                orderEntity = presenter.getCodeList().get(position);
                presenter.changeListChooseIndex(presenter.getCodeList(),position);
                break;
        }
        if(orderEntity.getPaymentType() == 1){
            //现金支付信息显示
            mOrderNameTv.setText(getString(R.string.pay_cash));
        }else {
            //余额支付信息显示
            presenter.getUserInfoByAccId(PayType.CASH,orderEntity.getPaymentType(),orderEntity.getStudentId());
        }
    }


    @Override
    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL,false);
        mOrderRecyclerView.setLayoutManager(layoutManager);
        mOrderRecyclerView.setAdapter(presenter.getOrderAdapter());

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, OrientationHelper.VERTICAL,false);
        mOrderDetailRecyclerView.setLayoutManager(layoutManager1);
        mOrderDetailRecyclerView.setAdapter(presenter.getOrderDetailAdapter());
    }

    @Override
    public void showOrderCode(OrderEntity entity) {
        orderEntity = entity;
        mOrderTypeTv.setText(orderEntity.getPaymentType() == 1?"支付类型：现金支付":"支付类型：余额支付");

    }

    @Override
    public void showTotalData(OrderEntity entity) {
        if(entity.getPaymentType() ==1){
            mOrderAvaterIv.setVisibility(View.INVISIBLE);
            mOrderNameTv.setVisibility(View.INVISIBLE);
        }else {
            mOrderAvaterIv.setVisibility(View.VISIBLE);
            mOrderNameTv.setVisibility(View.VISIBLE);
        }
        mOrderNumberTv.setText(entity.getNumberCount()+"件");
        mOrderMoneyTv.setText("¥"+ entity.getPaymentNumber());
        mOrderShouldTv.setText("¥"+ entity.getPaymentNumber());
        mOrderActualTv.setText("¥"+ entity.getPaymentNumber());
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }


    @OnClick({R.id.order_print_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.order_print_tv: //打印单据
                if(null == orderEntity || presenter.getDetailList().size() <= 0){
                    ToastUtils.showToast(ToastType.WARNING,getString(R.string.order_data_error));
                    return;
                }
                presenter.getUserInfoByAccId(PayType.BALANCE,orderEntity.getPaymentType(),orderEntity.getStudentId());
                break;
        }

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
    public void finishLoadMore(boolean noMoreData) {
        mOrderRefreshLayout.finishLoadMore(0,true,noMoreData);
    }

    @Override
    public void finishRefreshing() {
        mOrderRefreshLayout.finishRefresh();
    }

    @Override
    public void showPayUserInfo(UserDetailEntity data) {
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.icon_default_user)
                .placeholder(R.mipmap.icon_default_user)
                .centerCrop();
        Glide.with(this)
                .load(data.getPic())
                .apply(options)
                .into(mOrderAvaterIv);
        mOrderNameTv.setText(data.getUserName());
    }


    @Override
    public void printTicket(int payType,String schoolName,String userName) {
        if(null == presenter.getUsbCtrl() || null == presenter.getUsbDev()){
            presenter.initUsbModel();
            presenter.connectUsb();
            if(null == presenter.getUsbCtrl() || null == presenter.getUsbDev()){
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(ToastType.WARNING,getString(R.string.collect_cash_print_prompt));
                    }
                });
                return;
            }
        }
        try {
            if (presenter.getUsbCtrl().revByte(presenter.getUsbDev()) == 0x38) { //打印机 链接失败
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
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
            builder.append("  "+getString(R.string.trade_code)+orderEntity.getOrderNumber());
            builder.append("\n");
            builder.append(getString(R.string.trade_date)+ DateUtil.stampToTime(orderEntity.getCreateTime()));
            builder.append("\n");
            builder.append("————————————————");

            builder.append(" 商品名称"+"      "+"单价  "+"数量  "+"金额");
            builder.append("\n");
            builder.append("————————————————");
            builder.append("\n");
            for(int i=0;i<presenter.getDetailList().size();i++){
                builder.append(presenter.getDetailList().get(i).getGoodsName());
                builder.append("\n");
                builder.append("               ");
                builder.append(presenter.getDetailList().get(i).getSalesPrice());
                builder.append("   ");
                builder.append(presenter.getDetailList().get(i).getNum());
                builder.append("  ");
                builder.append(presenter.getDetailList().get(i).getPay());
                builder.append("\n");
            }

            builder.append("————————————————");
            builder.append("\n");
            builder.append(getString(R.string.trade_total_money)+orderEntity.getPaymentNumber());
            builder.append("\n");
            builder.append("————————————————");
            if(payType == 2){
                builder.append(getString(R.string.order_buy_people)+userName);
                builder.append("\n");
            }
            builder.append(getString(R.string.trade_should_total_money)+orderEntity.getPaymentNumber());
            builder.append("\n");
            builder.append(getString(R.string.trade_actual_total_money)+orderEntity.getPaymentNumber());
            builder.append("\n");
            builder.append(getString(R.string.trade_change_total_money)+orderEntity.getPaymentNumber());
            builder.append("\n");
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        /*判断是否是“搜索”键*/
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v
                    .getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }
            mOrderType = OrderType.CODE;
            presenter.orderList(mOrderSearchEt.getText().toString().trim(),mOrderType,0);
            return true;
        }
        return false;
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        switch (mOrderType){
            case ALL:
                offset += 1;
                presenter.orderList("",mOrderType,offset);
                break;
            case CODE:
                presenter.orderList(mOrderSearchEt.getText().toString().trim(),mOrderType,0);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        showActivity(OrderActivity.this, SettingActivity.class);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        switch (mOrderType){
            case ALL:
                offset = 1;
                presenter.orderList("",mOrderType,offset);
                break;
            case CODE:
                presenter.orderList(mOrderSearchEt.getText().toString().trim(),mOrderType,0);
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
        if(s.length() == 0){
            mOrderType = OrderType.ALL;
            presenter.orderList("",mOrderType,1);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != presenter && presenter.isAttached()){
            if(null != presenter.getUsbCtrl()){
                presenter.getUsbCtrl().close();
                presenter.setUsbCtrl(null);
            }
            presenter.cancel();
            presenter.detachView();
        }
    }
}
