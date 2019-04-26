package com.gykj.cashier.module.storage.iview;

import com.wrs.gykjewm.baselibrary.base.IBaseView;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 上午9:23
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface IStorageView extends IBaseView {

    void initRecyclerView();

    void showDialogData(long goodsId, String barCode, String name,double limit);


    void searchLast();
}
