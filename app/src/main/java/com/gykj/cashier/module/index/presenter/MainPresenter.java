package com.gykj.cashier.module.index.presenter;

import android.app.Activity;
import android.hardware.usb.UsbDevice;

import com.gykj.cashier.module.index.iview.IMainView;
import com.gykj.cashier.module.index.ui.adapter.MainTabAdapter;
import com.gykj.cashier.utils.MainTabUtils;
import com.hoin.usbsdk.UsbController;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;


/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午4:00
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class MainPresenter extends BasePresenter<IMainView> {


    MainTabAdapter adapter;

    private UsbController usbCtrl;
    private UsbDevice usbDevice;
    private int[][] usbInfo;

    @Override
    public void attachView(IMainView view) {
        super.attachView(view);
        initUsbModel();
        initUsbConnect();
    }

    public void initAdapter(){
        if(null == adapter){
            adapter = new MainTabAdapter(MainTabUtils.getMainTabUtils().getMainTabList(mContext));
        }
    }

    public void initUsbModel(){
        usbInfo = new int[5][2];
        usbInfo[0][0] = 0x0456;
        usbInfo[0][1] = 0x0808;
        usbInfo[1][0] = 0x1CB0;
        usbInfo[1][1] = 0x0003;
        usbInfo[2][0] = 0x0483;
        usbInfo[2][1] = 0x5740;
        usbInfo[3][0] = 0x0493;
        usbInfo[3][1] = 0x8760;
        usbInfo[4][0] = 0x0471;
        usbInfo[4][1] = 0x0055;

        usbCtrl = new UsbController((Activity) mContext, mBaseView.getHandler());
    }

    public MainTabAdapter getMainTabAdapter(){
        return adapter;
    }


    public UsbController getUsbCtrl() {
        return usbCtrl;
    }

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    public void initUsbConnect() {
        usbCtrl.close();
        for (int i = 0; i < 5; i++) {
            usbDevice = usbCtrl.getDev(usbInfo[i][0], usbInfo[i][1]);
            if(null != usbDevice){
                break;
            }
        }
        if (usbDevice != null) {
            if (!(usbCtrl.isHasPermission(usbDevice))) {
                usbCtrl.getPermission(usbDevice);
                ToastUtils.showToast(ToastType.SUCCESS,"USB打印机未连接");
            } else {
                ToastUtils.showToast(ToastType.SUCCESS,"USB打印机已连接");
            }
        }
    }
}
