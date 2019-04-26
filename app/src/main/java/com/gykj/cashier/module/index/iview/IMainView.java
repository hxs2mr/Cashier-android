package com.gykj.cashier.module.index.iview;

import android.os.Handler;

import com.wrs.gykjewm.baselibrary.base.IBaseView;


/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午4:00
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface IMainView extends IBaseView {

    void initRecyclerview();

    void openMoneyBin();

    Handler getHandler();

    void sendRabbitMQMessage();



}
