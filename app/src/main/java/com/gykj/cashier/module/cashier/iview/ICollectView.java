package com.gykj.cashier.module.cashier.iview;

import android.app.Activity;
import android.os.Handler;

import com.gykj.cashier.module.order.entity.UserDetailEntity;
import com.wrs.gykjewm.baselibrary.base.IBaseView;


/**
 * description:
 * <p>
 * author: josh.lu
 * created: 15/8/18 下午1:18
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface ICollectView extends IBaseView {

    Handler getHandler();

    String changePrintTitle(String title);

    void printTicket(int payType,String schoolName,String userName,String orderCode,long orderTime);

    void finishActivity();

    void showPrintDialog();

    Activity getActivity();

    void showPaySuccessDialog(UserDetailEntity entity);

    void openMoneyBin();

}
