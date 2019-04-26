package com.gykj.cashier.module.order.presenter;

import android.app.Activity;
import android.hardware.usb.UsbDevice;

import com.gykj.cashier.R;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.order.data.OrderApi;
import com.gykj.cashier.module.order.entity.OrderDetailEntity;
import com.gykj.cashier.module.order.entity.OrderEntity;
import com.gykj.cashier.module.order.entity.OrderType;
import com.gykj.cashier.module.order.entity.PayType;
import com.gykj.cashier.module.order.entity.UserDetailEntity;
import com.gykj.cashier.module.order.iview.IOrderView;
import com.gykj.cashier.module.order.ui.adapter.OrderAdapter;
import com.gykj.cashier.module.order.ui.adapter.OrderDetailAdapter;
import com.gykj.cashier.module.storage.entity.StorageListEntity;
import com.hoin.usbsdk.UsbController;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.utils.ThreadManager;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 24/8/18 下午1:50
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class OrderPresenter extends BasePresenter<IOrderView> {


    private UsbController usbCtrl;
    private UsbDevice usbDev;
    private int[][] u_infor;

    OrderApi api;


    OrderAdapter orderAdapter;

    private List<OrderEntity> orderList = new ArrayList<>();
    private List<OrderEntity> codeList = new ArrayList<>();

    OrderDetailAdapter orderDetailAdapter;
    private List<OrderDetailEntity> detailList = new ArrayList<>();

    @Override
    public void attachView(IOrderView view) {
        super.attachView(view);
        api = OrderApi.getOrderApi();

        initOrderAdapter();


        //初始化打印机   链接
        initUsbModel();

        connectUsb();

    }


    public void orderList(String orderNum , final OrderType type, final int offset){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.orderList(orderNum,offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<List<OrderEntity>>>() {
                    @Override
                    public void accept(BaseResult<List<OrderEntity>> listBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        mBaseView.finishRefreshing();
                        if(listBaseResult.isSuccessed()){
                            mBaseView.finishLoadMore(listBaseResult.getData().size()<10);
                            switch (type){
                                case CODE:
                                    orderAdapter.setNewData(repetiteList(codeList,listBaseResult.getData()));
                                    getUserInfoByAccId(PayType.CASH,codeList.get(0).getPaymentType(),codeList.get(0).getStudentId());
                                    mBaseView.showOrderCode(codeList.get(0));
                                    changeListChooseIndex(codeList,0);
                                    mBaseView.showTotalData(codeList.get(0));
                                    orderDetail(codeList.get(0).getId());
                                    break;
                                case ALL:
                                    orderAdapter.setNewData(repetiteList(orderList,listBaseResult.getData()));
                                    getUserInfoByAccId(PayType.CASH,orderList.get(0).getPaymentType(),orderList.get(0).getStudentId());
                                    mBaseView.showOrderCode(orderList.get(0));
                                    changeListChooseIndex(orderList,0);
                                    mBaseView.showTotalData(orderList.get(0));
                                    orderDetail(orderList.get(0).getId());
                                    break;
                            }
                            //ToastUtils.showToast(ToastType.SUCCESS,listBaseResult.getMsg());
                        }else if(listBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,listBaseResult.getMsg());
                        } else {
                            ToastUtils.showToast(ToastType.WARNING,listBaseResult.getMsg());
                        }
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }

    public void orderDetail(long orderId){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.orderDetail(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<List<OrderDetailEntity>>>() {
                    @Override
                    public void accept(BaseResult<List<OrderDetailEntity>> listBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(listBaseResult.isSuccessed() && listBaseResult.getData().size()>=0){
                            detailList = listBaseResult.getData();
                            orderDetailAdapter.setNewData(detailList);
                            //ToastUtils.showToast(ToastType.SUCCESS,listBaseResult.getMsg());
                        }else if(listBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,listBaseResult.getMsg());
                        } else {
                            ToastUtils.showToast(ToastType.WARNING,listBaseResult.getMsg());
                        }

                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }


    public void getUserInfoByAccId(final PayType type, final int payType, long accId){
        Disposable subscribe = api.getUserInfoByAccId(accId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<UserDetailEntity>>() {
                    @Override
                    public void accept(final BaseResult<UserDetailEntity> userDetailEntityBaseResult) throws Exception {
                        if (userDetailEntityBaseResult.isSuccessed()) {
                            switch (type) {
                                case CASH:
                                    mBaseView.showPayUserInfo(userDetailEntityBaseResult.getData());
                                    break;
                                case BALANCE:
                                    ThreadManager.getInstance().submit(new Runnable() {
                                        @Override
                                        public void run() {
                                            mBaseView.printTicket(payType, userDetailEntityBaseResult.getData().getSchoolName(), userDetailEntityBaseResult.getData().getUserName());
                                        }
                                    });
                                    break;
                                }
                        } else {
                            ToastUtils.showToast(ToastType.WARNING, userDetailEntityBaseResult.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showToast(ToastType.EEEOR, "获取数据异常"+throwable.getMessage());
                    }
                });
        addDisposable(subscribe);
    }

    /**
     * 排除重复的数据集合
     * @param list
     * @return
     */
    private List<OrderEntity> repetiteList(List<OrderEntity> showList, List<OrderEntity> list){
        for(OrderEntity entity:list){
            if(showList.contains(entity)){
                continue;
            }else {
                showList.add(entity);
            }
        }
        return showList;
    }


    public void changeListChooseIndex(List<OrderEntity> list,int position){
        for (int i = 0;i<list.size();i++){
            if(i == position){
                list.get(i).setChoosed(true);
            }else {
                list.get(i).setChoosed(false);
            }
        }
        orderAdapter.setNewData(list);
    }


    private void initOrderAdapter(){
        if(null == orderAdapter){
            orderAdapter = new OrderAdapter(mContext,orderList);
        }
        if(null == orderDetailAdapter){
            orderDetailAdapter = new OrderDetailAdapter(detailList);
        }
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




    public OrderAdapter getOrderAdapter() {
        return orderAdapter;
    }

    public List<OrderEntity> getOrderList() {
        return orderList;
    }

    public OrderDetailAdapter getOrderDetailAdapter() {
        return orderDetailAdapter;
    }

    public List<OrderDetailEntity> getDetailList() {
        return detailList;
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


    public List<OrderEntity> getCodeList() {
        return codeList;
    }

}
