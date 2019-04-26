package com.gykj.cashier.module.inventory.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.gykj.cashier.R;
import com.gykj.cashier.module.edit.entity.SearchType;
import com.gykj.cashier.module.inventory.data.InventoryApi;
import com.gykj.cashier.module.inventory.entity.InventoryEntity;
import com.gykj.cashier.module.inventory.iview.IInventoryView;
import com.gykj.cashier.module.inventory.ui.adapter.InventoryAdapter;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.utils.OthersUtils;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 22/8/18 下午1:05
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class InventoryPresenter extends BasePresenter<IInventoryView> {


    InventoryApi api;
    InventoryAdapter adapter;

    private List<InventoryEntity.DataBean> mInventoryList = new ArrayList<>();
    private List<InventoryEntity.DataBean> mCodeList = new ArrayList<>();
    private List<InventoryEntity.DataBean> mNameList = new ArrayList<>();

    private InventoryEntity mInventoryEntity;


    @Override
    public void attachView(IInventoryView view) {
        super.attachView(view);
        api = InventoryApi.getInventoryApi();
        initAdapter();
    }

    public void  inventoryList(final String codeOrName, final SearchType type, final int offset){
        mBaseView.showProgressDialog(mBaseView.getContext().getString(R.string.loading));
        Disposable subscribe = api.inventoryList(codeOrName, type,offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<InventoryEntity>(){
                    @Override
                    public void accept(InventoryEntity inventoryEntity) throws Exception {
                        mBaseView.closeProgressDialog();
                        mBaseView.finishRefreshing();
                        if(null != inventoryEntity && inventoryEntity.getCode() == 0){
                            Log.i("Inventory", "loadData");
                            if(null != inventoryEntity.getData() && inventoryEntity.getData().size()>=0){
                                mBaseView.finishLoading(inventoryEntity.getData().size()<10);
                                switch (type){
                                    case NAME:
                                        mBaseView.showScanAllTv(View.VISIBLE);
                                        adapter.setNewData(repetiteList(offset,mNameList,inventoryEntity.getData()));
                                        break;
                                    case BARCODE:
                                        mBaseView.showScanAllTv(View.VISIBLE);
                                        adapter.setNewData(repetiteList(offset,mCodeList,inventoryEntity.getData()));
                                        break;
                                    case ALL:
                                        mBaseView.showScanAllTv(View.INVISIBLE);
                                        adapter.setNewData(repetiteList(offset,mInventoryList,inventoryEntity.getData()));
                                        break;
                                }
                                mInventoryEntity = inventoryEntity;
                                mBaseView.showTotalData();
                                ToastUtils.showToast(ToastType.SUCCESS,inventoryEntity.getMsg());
                            }else if(inventoryEntity.getCode() == 100){
                                ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                                ToastUtils.showToast(ToastType.WARNING,inventoryEntity.getMsg());
                            } else {
                                ToastUtils.showToast(ToastType.WARNING,inventoryEntity.getMsg());
                            }
                        }
                    }
                },getErrorConsumer());
        addDisposable(subscribe);
    }

    /**
     * 排除重复的数据集合
     * @param list
     * @return
     */
    private List<InventoryEntity.DataBean> repetiteList(int offset,List<InventoryEntity.DataBean> showList, List<InventoryEntity.DataBean> list){
        if(offset == 1){
            showList.clear();
        }
        showList.addAll(list);
        return showList;
    }

    private void initAdapter(){
        if(null == adapter){
            adapter = new InventoryAdapter(mInventoryList);
        }
    }


    public InventoryAdapter getAdapter() {
        return adapter;
    }

    public List<InventoryEntity.DataBean> getmInventoryList() {
        return mInventoryList;
    }

    public InventoryEntity getmInventoryEntity() {
        return mInventoryEntity;
    }


    public List<InventoryEntity.DataBean> getmCodeList() {
        return mCodeList;
    }

    public List<InventoryEntity.DataBean> getmNameList() {
        return mNameList;
    }

}
