package com.gykj.cashier.module.cashier.presenter;

import android.app.Activity;
import android.hardware.usb.UsbDevice;

import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.data.CashierApi;
import com.gykj.cashier.module.cashier.entity.CashierInfo;
import com.gykj.cashier.module.cashier.entity.OrderEntity;
import com.gykj.cashier.module.cashier.entity.PayEntity;
import com.gykj.cashier.module.cashier.entity.PayType;
import com.gykj.cashier.module.cashier.iview.ICollectView;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.order.entity.UserDetailEntity;
import com.hoin.usbsdk.UsbController;
import com.lanzhu.baidutts.manager.BaiduTtsManager;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.utils.DateUtil;
import com.wrs.gykjewm.baselibrary.utils.ThreadManager;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 15/8/18 下午1:18
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CollectPresenter extends BasePresenter<ICollectView> {


    private UsbController usbCtrl;
    private UsbDevice usbDev;
    private int[][] u_infor;
    private PayEntity mPayEntity;

    CashierApi api;


    private OrderEntity mOrderEntity;
    private UserDetailEntity mUserDetailEntity;


    @Override
    public void attachView(ICollectView view) {
        super.attachView(view);
        api = CashierApi.getCashierApi();
        initUsbModel();
        connectUsb();
    }


    public void createOrder(List<CashierInfo> list){
        mBaseView.showProgressDialog(mBaseView.getContext().getString(R.string.loading));
        Disposable subscribe = api.createOrder(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderEntity>() {
                    @Override
                    public void accept(OrderEntity orderEntity) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(orderEntity.getCode() == 0){
                            mOrderEntity = orderEntity;
                            ToastUtils.showToast(ToastType.SUCCESS,orderEntity.getMsg());
                        }else if(orderEntity.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,orderEntity.getMsg());
                        }else {
                            ToastUtils.showToast(ToastType.WARNING,orderEntity.getMsg());
                        }
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }

    public void payOrder(final PayType type, long orderId, int payType, final long userId, final int userType, final String total_money){
        mBaseView.showProgressDialog("加载中...");
        Disposable subscribe = api.payOrder(type,orderId, payType, userId, userType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<PayEntity>>() {
                    @Override
                    public void accept(BaseResult<PayEntity> nullEntityBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(nullEntityBaseResult.isSuccessed()){
                            ToastUtils.showToast(ToastType.SUCCESS,nullEntityBaseResult.getMsg());
                            mPayEntity = nullEntityBaseResult.getData();
                            switch (type){  //现金支付 现金支付成功之后打开钱箱     还是余额支付
                                case CASH:
                                    mBaseView.openMoneyBin();
                                    mBaseView.showPrintDialog();
                                    break;
                                case FACE:
                                    getUserInfo(userId,userType);
                                    break;
                            }
                            BaiduTtsManager.getManager().speak("支付成功，金额"+total_money+"元");//语音播报 支付完成
                        }else if(nullEntityBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,nullEntityBaseResult.getMsg());
                        }else {
                            BaiduTtsManager.getManager().speak(nullEntityBaseResult.getMsg());
                            ToastUtils.showToast(ToastType.WARNING,nullEntityBaseResult.getMsg());
                        }
                    }
                },getErrorConsumer());
        addDisposable(subscribe);
    }


    public void getUserInfoByAccId(final int payType, long accId, final String orderCode, final long orderTime){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.getUserInfoByAccId(accId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<UserDetailEntity>>() {
                    @Override
                    public void accept(final BaseResult<UserDetailEntity> userDetailEntityBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(userDetailEntityBaseResult.isSuccessed()){
                            ThreadManager.getInstance().submit(new Runnable() {
                                @Override
                                public void run() {
                                    mBaseView.printTicket(payType,userDetailEntityBaseResult.getData().getSchoolName(),userDetailEntityBaseResult.getData().getUserName(),orderCode,orderTime);
                                }
                            });
                        }else{
                            ToastUtils.showToast(ToastType.WARNING,userDetailEntityBaseResult.getMsg());
                        }
                        mBaseView.finishActivity();
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }


    public void  getUserInfo(long userId,int userType){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.getUserInfo(userId, userType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<UserDetailEntity>>() {
                    @Override
                    public void accept(BaseResult<UserDetailEntity> userDetail) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(userDetail.isSuccessed()){
                            mUserDetailEntity = userDetail.getData();
                            mBaseView.showPaySuccessDialog(mUserDetailEntity);
                        }
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }

    public void initUsbModel(){
        usbCtrl = new UsbController((Activity) mContext, mBaseView.getHandler());
        u_infor = new int[5][2];
        u_infor[0][0] = 0x0456;//
        u_infor[0][1] = 0x0808;//
        u_infor[1][0] = 0x1CB0;
        u_infor[1][1] = 0x0003;
        u_infor[2][0] = 0x0483;
        u_infor[2][1] = 0x5740;
        u_infor[3][0] = 0x0493;
        u_infor[3][1] = 0x8760;
        u_infor[4][0] = 0x0471;
        u_infor[4][1] = 0x0055;
    }

    public void connectUsb(){
        usbCtrl.close();
        int i = 0;
        for (i = 0; i < 5; i++) {
            usbDev = usbCtrl.getDev(u_infor[i][0], u_infor[i][1]);
            if (usbDev != null) {
                break;
            }
        }
        if (usbDev != null) {
            if (!(usbCtrl.isHasPermission(usbDev))) {
                //Log.d("usb调试","请求USB设备权限.");
                usbCtrl.getPermission(usbDev);
            } else {
                ToastUtils.showToast(ToastType.SUCCESS,mContext.getString(R.string.cash_print_usb_connected));
            }
        }
    }


    //检查是否具有访问usb设备的权限
    public boolean CheckUsbPermission() {
        if (usbDev != null) {
            if (usbCtrl.isHasPermission(usbDev)) {
                return true;
            }
        }
        ToastUtils.showToast(ToastType.WARNING,mContext.getString(R.string.cash_print_usb_permission));
        return false;
    }










    public OrderEntity getmOrderEntity() {
        return mOrderEntity;
    }

    public UsbController getUsbCtrl() {
        return usbCtrl;
    }

    public void setUsbCtrl(UsbController usbCtrl) {
        this.usbCtrl = usbCtrl;
    }

    public UsbDevice getUsbDev() {
        return usbDev;
    }

    public void setUsbDev(UsbDevice usbDev) {
        this.usbDev = usbDev;
    }


    public PayEntity getmPayEntity() {
        return mPayEntity;
    }
    public UserDetailEntity getmUserDetailEntity() {
        return mUserDetailEntity;
    }



}
