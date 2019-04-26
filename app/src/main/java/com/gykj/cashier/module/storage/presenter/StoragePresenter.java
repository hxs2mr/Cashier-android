package com.gykj.cashier.module.storage.presenter;

import com.gykj.cashier.R;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.storage.data.StorageApi;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.gykj.cashier.module.storage.iview.IStorageView;
import com.gykj.cashier.module.storage.ui.adapter.StorageAdapter;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.math.BigDecimal;
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
 * created: 20/8/18 上午9:23
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class StoragePresenter extends BasePresenter<IStorageView> {

    StorageApi api;

    StorageAdapter adapter;


    private List<GoodsNameEntity> goodsList = new ArrayList<>();

    @Override
    public void attachView(IStorageView view) {
        super.attachView(view);
        api = StorageApi.getStorageApi();
        initAdapter();
    }

    public void electByBarcode(String barcode){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.selectByBarcode(barcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<BarCodeEntity>>() {
                    @Override
                    public void accept(BaseResult<BarCodeEntity> barCodeEntityBaseResult) throws Exception {
                        if(null != barCodeEntityBaseResult && barCodeEntityBaseResult.isSuccessed()){
                            ToastUtils.showToast(ToastType.SUCCESS,barCodeEntityBaseResult.getMsg());
                            BarCodeEntity data = barCodeEntityBaseResult.getData();
                            if(null != data){
                                mBaseView.showDialogData(data.getId(),data.getCommodityBarcode(),data.getCommodityName(),data.getTotal());
                            }
                        }else if(barCodeEntityBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,barCodeEntityBaseResult.getMsg());
                        } else {
                            ToastUtils.showToast(ToastType.WARNING,barCodeEntityBaseResult.getMsg());
                        }
                        mBaseView.closeProgressDialog();
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }


    public void goodsList(String commodityName){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.goodsList(commodityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<List<GoodsNameEntity>>>() {
                    @Override
                    public void accept(BaseResult<List<GoodsNameEntity>> listBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(null != listBaseResult && listBaseResult.isSuccessed()){
                            ToastUtils.showToast(ToastType.SUCCESS,listBaseResult.getMsg());
                            goodsList = listBaseResult.getData();
                            adapter.setNewData(goodsList);
                        }else if(listBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,listBaseResult.getMsg());
                        } else {
                            ToastUtils.showToast(ToastType.WARNING,listBaseResult.getMsg());
                        }
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }


    public void addStock(long goodsId, int status, BigDecimal total){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.addStock(goodsId, status, total)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<NullEntity>>() {
                    @Override
                    public void accept(BaseResult<NullEntity> nullEntityBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(nullEntityBaseResult.isSuccessed()){
                            mBaseView.searchLast();
                            ToastUtils.showToast(ToastType.SUCCESS,nullEntityBaseResult.getMsg());
                        }else if(nullEntityBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,nullEntityBaseResult.getMsg());
                        } else {
                            ToastUtils.showToast(ToastType.WARNING,nullEntityBaseResult.getMsg());
                        }

                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
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

}
