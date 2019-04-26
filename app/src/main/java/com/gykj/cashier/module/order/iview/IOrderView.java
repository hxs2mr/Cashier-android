package com.gykj.cashier.module.order.iview;

import android.os.Handler;

import com.gykj.cashier.module.order.entity.OrderEntity;
import com.gykj.cashier.module.order.entity.UserDetailEntity;
import com.wrs.gykjewm.baselibrary.base.IBaseView;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 24/8/18 下午1:41
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface IOrderView extends IBaseView {

    void initRecyclerView();

    void showOrderCode(OrderEntity entity);


    void showTotalData(OrderEntity entity);

    Handler getHandler();

    void printTicket(int payType,String schoolName,String userName);

    String changePrintTitle(String title);

    void finishLoadMore(boolean noMoreData);

    void finishRefreshing();

    void showPayUserInfo(UserDetailEntity data);

}
