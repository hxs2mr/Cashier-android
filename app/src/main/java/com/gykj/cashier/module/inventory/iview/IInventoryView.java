package com.gykj.cashier.module.inventory.iview;

import com.wrs.gykjewm.baselibrary.base.IBaseView;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 22/8/18 下午1:05
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface IInventoryView  extends IBaseView{

    void initRecyclerView();

    void showTotalData();

    void finishLoading(boolean noMoreData);

    void finishRefreshing();

    void showScanAllTv(int visiable);
}
