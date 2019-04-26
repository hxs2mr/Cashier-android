package com.gykj.cashier.module.finance.presenter;

import com.gykj.cashier.R;
import com.gykj.cashier.module.finance.data.FinanceApi;
import com.gykj.cashier.module.finance.entity.FinanceEntity;
import com.gykj.cashier.module.finance.iview.IFinanceView;
import com.gykj.cashier.module.finance.ui.adapter.FinanceAdapter;
import com.gykj.cashier.module.inventory.entity.InventoryEntity;
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
 * created: 28/8/18 下午1:20
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FinancePresenter extends BasePresenter<IFinanceView> {

    FinanceApi api;

    FinanceAdapter adapter;
    private List<FinanceEntity> financeList = new ArrayList<>();


    @Override
    public void attachView(IFinanceView view) {
        super.attachView(view);
        api = FinanceApi.getFinanceApi();
        initAdapter();
    }

    private void initAdapter() {
        if(null == adapter){
            adapter = new FinanceAdapter(financeList);
        }
    }


    public void settlementList(final int limit, final int offset){
        mBaseView.showProgressDialog(mBaseView.getContext().getString(R.string.loading));
        Disposable subscribe = api.settlementList(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<List<FinanceEntity>>>() {
                    @Override
                    public void accept(BaseResult<List<FinanceEntity>> listBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        mBaseView.finishRefreshing();
                        if(listBaseResult.isSuccessed() && listBaseResult.getData().size()>0){
                            if(listBaseResult.getData().size()<limit){
                                mBaseView.finishLoading();
                            }
                            adapter.setNewData(repetiteList(financeList,listBaseResult.getData()));
                            ToastUtils.showToast(ToastType.SUCCESS,listBaseResult.getMsg());
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

    /**
     * 排除重复的数据集合
     * @param list
     * @return
     */
    private List<FinanceEntity> repetiteList(List<FinanceEntity> showList, List<FinanceEntity> list){
        for(FinanceEntity entity:list){
            if(showList.contains(entity)){
                continue;
            }else {
                showList.add(entity);
            }
        }
        return showList;
    }


    public FinanceAdapter getAdapter() {
        return adapter;
    }

    public List<FinanceEntity> getFinanceList() {
        return financeList;
    }

}
