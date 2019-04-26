package com.gykj.cashier.module.setting.presenter;

import com.gykj.cashier.R;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.setting.data.ModifyApi;
import com.gykj.cashier.module.setting.iview.ISettingView;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.manager.CashManager;
import com.wrs.gykjewm.baselibrary.manager.RabbiMqEngine;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/11/614:43
 * version: 1.0
 */
public class SettingPresenter extends BasePresenter<ISettingView> {


    ModifyApi api;

    @Override
    public void attachView(ISettingView view) {
        super.attachView(view);
        api = ModifyApi.getLoginApi();
    }


    public void logout(){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.logout()     
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<NullEntity>>() {
                    @Override
                    public void accept(BaseResult<NullEntity> nullEntityBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(nullEntityBaseResult.isSuccessed()){
                            ToastUtils.showToast(ToastType.SUCCESS,nullEntityBaseResult.getMsg());
                            CashManager.getCashApi().clearAllData();
                            RabbiMqEngine.getRabbiMqEngine().destoryConnect();
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                        }else if(nullEntityBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,nullEntityBaseResult.getMsg());
                        }else {
                            ToastUtils.showToast(ToastType.WARNING,nullEntityBaseResult.getMsg());
                        }

                    }
                },getErrorConsumer());
        addDisposable(subscribe);
    }
}
