package com.gykj.cashier.module.finance.iview;

import com.wrs.gykjewm.baselibrary.base.IBaseView;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 28/8/18 下午1:20
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface IFinanceView extends IBaseView {

    void initRecyclerView();

    void finishLoading();

    void finishRefreshing();
}
