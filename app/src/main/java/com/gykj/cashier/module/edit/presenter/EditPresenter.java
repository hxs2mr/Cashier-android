package com.gykj.cashier.module.edit.presenter;

import android.text.TextUtils;
import android.view.View;

import com.gykj.cashier.R;
import com.gykj.cashier.module.edit.data.EditApi;
import com.gykj.cashier.module.edit.entity.ClassfyListEntity;
import com.gykj.cashier.module.edit.entity.SearchType;
import com.gykj.cashier.module.edit.iview.IEditView;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.gykj.cashier.module.storage.entity.StorageListEntity;
import com.gykj.cashier.module.storage.ui.adapter.StorageAdapter;
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
 * description:
 * <p>
 * author: josh.lu
 * created: 21/8/18 下午1:11
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class EditPresenter extends BasePresenter<IEditView>{

    EditApi api;

    StorageAdapter adapter;


    private List<GoodsNameEntity> goodsList = new ArrayList<>();
    private List<GoodsNameEntity> nameList = new ArrayList<>();
    private List<GoodsNameEntity> codeList = new ArrayList<>();


    @Override
    public void attachView(IEditView view) {
        super.attachView(view);
        api = EditApi.getEditApi();
        initAdapter();
    }

    public void goodsList(final String nameOrCode, final SearchType type, final int offset){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.goodsList(nameOrCode, type,offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<List<GoodsNameEntity>>>() {
                    @Override
                    public void accept(BaseResult<List<GoodsNameEntity>> listBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        mBaseView.getRefreshLayout().finishRefresh();
                        if(null != listBaseResult && listBaseResult.isSuccessed()){
                            ToastUtils.showToast(ToastType.SUCCESS,listBaseResult.getMsg());
                            mBaseView.finishLoadMore(listBaseResult.getData().size()<10);
                            switch (type){
                                case BARCODE:
                                    mBaseView.showScanAllTv(View.VISIBLE);
                                    adapter.setNewData(repetiteList(offset,codeList,listBaseResult.getData()));
                                    break;
                                case NAME:
                                    mBaseView.showSearchEv(1);
                                    mBaseView.showScanAllTv(View.VISIBLE);
                                    adapter.setNewData(repetiteList(offset,nameList,listBaseResult.getData()));
                                    break;
                                case ALL:
                                    mBaseView.showScanAllTv(View.INVISIBLE);
                                    adapter.setNewData(repetiteList(offset,goodsList,listBaseResult.getData()));
                                    break;
                                }
                        }else if(listBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,listBaseResult.getMsg());
                        } else {
                             mBaseView.finishLoadMore(false);
                             mBaseView.showSearchEv(0);
                             ToastUtils.showToast(ToastType.WARNING,listBaseResult.getMsg());
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
    private List<GoodsNameEntity> repetiteList(int offset,List<GoodsNameEntity> showList, List<GoodsNameEntity> list){
        if(offset == 1){
            showList.clear();
        }
        showList.addAll(list);
        return showList;
    }

    public void initAdapter(){
        if(null == adapter){
            adapter = new StorageAdapter(goodsList);
        }
    }

    public StorageAdapter getAdapter() {
        return adapter;
    }

    public List<GoodsNameEntity> getGoodsList() {
        return goodsList;
    }

    public List<GoodsNameEntity> getNameList() {
        return nameList;
    }

    public List<GoodsNameEntity> getCodeList() {
        return codeList;
    }


}
