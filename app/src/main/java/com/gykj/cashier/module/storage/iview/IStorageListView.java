package com.gykj.cashier.module.storage.iview;

import com.gykj.cashier.module.storage.entity.SearchType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.wrs.gykjewm.baselibrary.base.IBaseView;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/2610:59
 * version: 1.0
 */
public interface IStorageListView extends IBaseView{

    void initRecyclerViewUI();

    void initListener();

    SearchType getSearchType();

    SmartRefreshLayout getRefreshLayout();

    void showScanAllTv(int visiable);
}
