package com.gykj.cashier.module.inventory.presenter;

import com.gykj.cashier.R;
import com.gykj.cashier.module.inventory.data.InventoryApi;
import com.gykj.cashier.module.inventory.iview.IFormView;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 22/8/18 下午3:05
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FormPresenter extends BasePresenter<IFormView> {

    InventoryApi api;

    @Override
    public void attachView(IFormView view) {
        super.attachView(view);
        api = InventoryApi.getInventoryApi();
    }

    public void inventoryAdd(long goodsId,BigDecimal inventBefore, BigDecimal inventorStock, BigDecimal differentStock){
        mBaseView.showProgressDialog(mBaseView.getContext().getString(R.string.loading));
        Disposable subscribe = api.inventoryAdd(goodsId, inventBefore,inventorStock, differentStock)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<NullEntity>>() {
                    @Override
                    public void accept(BaseResult<NullEntity> nullEntityBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(nullEntityBaseResult.isSuccessed()){
                            ToastUtils.showToast(ToastType.SUCCESS,nullEntityBaseResult.getMsg());
                            mBaseView.finishActivity();
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
}
