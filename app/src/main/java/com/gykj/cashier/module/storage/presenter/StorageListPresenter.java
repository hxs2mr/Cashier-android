package com.gykj.cashier.module.storage.presenter;

import android.view.View;

import com.gykj.cashier.R;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.storage.data.StorageApi;
import com.gykj.cashier.module.storage.entity.SearchType;
import com.gykj.cashier.module.storage.entity.StorageListEntity;
import com.gykj.cashier.module.storage.iview.IStorageListView;
import com.gykj.cashier.module.storage.ui.adapter.StorageListAdapter;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/2611:00
 * version: 1.0
 */
public class StorageListPresenter extends BasePresenter<IStorageListView> {

    StorageApi api;

    StorageListAdapter adapter;


    private List<StorageListEntity> allList = new ArrayList<>();
    private List<StorageListEntity> nameList = new ArrayList<>();
    private List<StorageListEntity> codeList = new ArrayList<>();

    @Override
    public void attachView(IStorageListView view) {
        super.attachView(view);
        api = StorageApi.getStorageApi();
        initAdapter();
    }


    public void stockList(String commodityBarcode, String commodityName, final int offset, final SearchType type){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.stockList(commodityBarcode, commodityName, offset, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<List<StorageListEntity>>>() {
                    @Override
                    public void accept(BaseResult<List<StorageListEntity>> listBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if (null != listBaseResult && listBaseResult.isSuccessed()) {
                            ToastUtils.showToast(ToastType.SUCCESS, listBaseResult.getMsg());
                            if(offset == 1){
                                mBaseView.getRefreshLayout().finishRefresh();
                            }
                            switch (type){
                                case NAME:
                                    mBaseView.showScanAllTv(View.VISIBLE);
                                    adapter.setNewData(repetiteList(nameList,listBaseResult.getData()));
                                    break;
                                case CODE:
                                    mBaseView.showScanAllTv(View.VISIBLE);
                                    adapter.setNewData(repetiteList(codeList,listBaseResult.getData()));
                                    break;
                                case ALL:
                                    mBaseView.showScanAllTv(View.INVISIBLE);
                                    adapter.setNewData(repetiteList(allList,listBaseResult.getData()));
                                    break;
                            }
                            if(listBaseResult.getData().size()<10){
                                mBaseView.getRefreshLayout().finishLoadMore(0,true,true);
                            }else {
                                mBaseView.getRefreshLayout().finishLoadMore(0,true,false);
                            }
                        } else if (listBaseResult.getCode() == 100) {
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING, listBaseResult.getMsg());
                        } else {
                            ToastUtils.showToast(ToastType.WARNING, listBaseResult.getMsg());
                        }
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }

    /**
     * 排除重复的数据集合
     * @param list
     * @return
     */
    private List<StorageListEntity> repetiteList(List<StorageListEntity> showList,List<StorageListEntity> list){
        for(StorageListEntity entity:list){
            if(showList.contains(entity)){
               continue;
            }else {
                showList.add(entity);
            }
        }
        return showList;
    }

    private void initAdapter(){
        if(null == adapter){
            adapter = new StorageListAdapter(mContext,allList);
        }
    }

    public StorageListAdapter getAdapter(){
        return adapter;
    }

    public List<StorageListEntity> getAllList() {
        return allList;
    }

    public List<StorageListEntity> getNameList() {
        return nameList;
    }

    public List<StorageListEntity> getCodeList() {
        return codeList;
    }
}
