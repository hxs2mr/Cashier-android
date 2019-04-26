package com.gykj.cashier.module.finance.presenter;

import com.gykj.cashier.R;
import com.gykj.cashier.module.finance.data.FinanceApi;
import com.gykj.cashier.module.finance.entity.FinanceDetailEntity;
import com.gykj.cashier.module.finance.iview.IFinanceDetailView;
import com.gykj.cashier.module.finance.ui.adapter.FinanceDetailAdapter;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
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
 * created: 28/8/18 下午2:59
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FinanceDetailPresenter extends BasePresenter<IFinanceDetailView> {

    FinanceApi api;

    FinanceDetailAdapter adapter;
    private List<FinanceDetailEntity> financeDetailList = new ArrayList<>();

    @Override
    public void attachView(IFinanceDetailView view) {
        super.attachView(view);
        api  = FinanceApi.getFinanceApi();
        initAdapter();
    }

    private void initAdapter() {
        if(null == adapter){
            adapter = new FinanceDetailAdapter(financeDetailList);
        }
    }

    public void settlementDetail(int settlementId){
        mBaseView.showProgressDialog(mBaseView.getContext().getString(R.string.loading));
        Disposable subscribe = api.settlementDetail(settlementId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<List<FinanceDetailEntity>>>() {
                    @Override
                    public void accept(BaseResult<List<FinanceDetailEntity>> listBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(listBaseResult.isSuccessed() && listBaseResult.getData().size()>0){
                            financeDetailList = listBaseResult.getData();
                            adapter.setNewData(financeDetailList);
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

    public FinanceDetailAdapter getAdapter() {
        return adapter;
    }
}
