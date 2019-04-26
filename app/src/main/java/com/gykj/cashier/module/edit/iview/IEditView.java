package com.gykj.cashier.module.edit.iview;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.wrs.gykjewm.baselibrary.base.IBaseView;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 21/8/18 下午1:11
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface IEditView extends IBaseView {

    void initRecyclerView();


    void finishLoadMore(boolean noMoreData);

    SmartRefreshLayout getRefreshLayout();

    void showScanAllTv(int visiable);

    void showSearchEv(int flage);
}
