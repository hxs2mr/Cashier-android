package com.gykj.cashier.module.cashier.iview;


import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.wrs.gykjewm.baselibrary.base.IBaseView;

import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 14/8/18 上午9:12
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface ICashierView extends IBaseView {

    void initRecyclerView();

    void changeBottomData();

    void finishActivity();

    void showStorageDialog();

    void searchByCodeOrName();

    void showSearchListDialog(List<GoodsNameEntity> list);
}
