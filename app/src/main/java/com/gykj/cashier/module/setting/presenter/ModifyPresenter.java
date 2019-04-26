package com.gykj.cashier.module.setting.presenter;

import com.gykj.cashier.R;
import com.gykj.cashier.entity.TabType;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.setting.data.ModifyApi;
import com.gykj.cashier.module.setting.iview.IModifyView;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 28/8/18 下午12:12
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class ModifyPresenter extends BasePresenter<IModifyView> {

    ModifyApi api;

    @Override
    public void attachView(IModifyView view) {
        super.attachView(view);
        api = ModifyApi.getLoginApi();
    }


    public void  updatePass(String oldPass, String newPass){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.updatePass(oldPass, newPass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<NullEntity>>() {
                    @Override
                    public void accept(BaseResult<NullEntity> nullEntityBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(nullEntityBaseResult.isSuccessed()){
                            ToastUtils.showToast(ToastType.SUCCESS,nullEntityBaseResult.getMsg());
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                        }else if(nullEntityBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,nullEntityBaseResult.getMsg());
                        }else {
                            ToastUtils.showToast(ToastType.WARNING,nullEntityBaseResult.getMsg());
                        }
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }
}
