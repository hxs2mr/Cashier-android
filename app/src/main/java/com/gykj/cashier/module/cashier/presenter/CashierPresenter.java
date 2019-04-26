package com.gykj.cashier.module.cashier.presenter;


import android.view.View;

import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.data.CashierApi;
import com.gykj.cashier.module.cashier.entity.CashierInfo;
import com.gykj.cashier.module.cashier.iview.ICashierView;
import com.gykj.cashier.module.cashier.ui.adapter.CashierAdapter;
import com.gykj.cashier.module.edit.entity.SearchType;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 14/8/18 上午9:12
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CashierPresenter extends BasePresenter<ICashierView> {


    CashierApi api;

    CashierAdapter adapter;

    private List<CashierInfo> cashierInfoList = new ArrayList<>();

    private Map<String,CashierInfo> cashierInfoMap = new HashMap<>();

    private List<GoodsNameEntity> goodList = new ArrayList<>();

    @Override
    public void attachView(ICashierView view) {
        super.attachView(view);
        api = CashierApi.getCashierApi();
        initCashierAdapter();
    }


    public void selectByBarcode(final String barcode){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.selectByBarcode(barcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<BarCodeEntity>>() {
                    @Override
                    public void accept(BaseResult<BarCodeEntity> barCodeEntityBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(null != barCodeEntityBaseResult && barCodeEntityBaseResult.isSuccessed()){
                            if(null == barCodeEntityBaseResult.getData() || barCodeEntityBaseResult.getData().getStatus() == 0){
                                ToastUtils.showToast(ToastType.WARNING,"此商品未上架，无法购买！");
                            }else {
                                if(barCodeEntityBaseResult.getData().getTotal() > 0){
                                    changeCashierInfo(barCodeEntityBaseResult.getData().getCommodityBarcode(),barCodeEntityBaseResult.getData());
                                }else {
                                    mBaseView.showStorageDialog();
                                }
                            }
                        }else if(barCodeEntityBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,barCodeEntityBaseResult.getMsg());
                        } else {
                            ToastUtils.showToast(ToastType.WARNING,barCodeEntityBaseResult.getMsg());
                        }
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
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
                        if(null != listBaseResult && listBaseResult.isSuccessed()){
                            ToastUtils.showToast(ToastType.SUCCESS,listBaseResult.getMsg());
                            goodList = listBaseResult.getData();
                            mBaseView.showSearchListDialog(goodList);
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


    public void initCashierAdapter(){
        if(null == adapter){
            adapter = new CashierAdapter(cashierInfoList);
        }
    }


    public CashierAdapter getAdapter() {
        return adapter;
    }

    public void changeCashierInfo(String code,BarCodeEntity entity){
        //不有数据的时候
        if(!cashierInfoMap.containsKey(code)){
            CashierInfo info = new CashierInfo();
            info.setGoodsId(entity.getId());
            info.setCode(entity.getCommodityBarcode());
            info.setName(entity.getCommodityName());
            info.setSingle_price(String.valueOf(entity.getSalesPrice()));
            info.setAmount(String.valueOf(1));
            info.setTotal_price(String.valueOf(entity.getSalesPrice()));
            info.setTotal(entity.getTotal());
            cashierInfoMap.put(code,info);
            mapTransitionList(cashierInfoMap);
            adapter.setNewData(cashierInfoList);
            mBaseView.changeBottomData();
        }else {
            //有的情况下
            addCashierItem(code);
        }
    }

    public void changeSearchInfo(String code,GoodsNameEntity entity){
        //不有数据的时候
        if(!cashierInfoMap.containsKey(code)){
            CashierInfo info = new CashierInfo();
            info.setGoodsId(entity.getId());
            info.setCode(entity.getCommodityBarcode());
            info.setName(entity.getCommodityName());
            info.setSingle_price(String.valueOf(entity.getSalesPrice()));
            info.setAmount(String.valueOf(1));
            info.setTotal_price(String.valueOf(entity.getSalesPrice()));
            info.setTotal(entity.getTotal());
            cashierInfoMap.put(code,info);
            mapTransitionList(cashierInfoMap);
            adapter.setNewData(cashierInfoList);
            mBaseView.changeBottomData();
        }else {
            //有的情况下
            addCashierItem(code);
        }

    }

    public void mapTransitionList(Map map) {
        cashierInfoList.clear();
        Iterator iter = map.entrySet().iterator(); // 获得map的Iterator
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            cashierInfoList.add((CashierInfo) entry.getValue());
        }
    }


    /**
     * 加法操作
     */
    public void addCashierItem(String code){
        CashierInfo cashierInfo = cashierInfoMap.get(code);
        //判断当前商品是否有库存
        if(Double.valueOf(cashierInfo.getAmount()) >= cashierInfo.getTotal()){
            ToastUtils.showToast(ToastType.WARNING,"当前数量已经大于商品库存");
            return;
        }
        BigDecimal amount = new BigDecimal(cashierInfo.getAmount());
        amount = amount.add(new BigDecimal(1));
        amount.setScale(3);
        cashierInfo.setAmount(amount.toString());

        BigDecimal amount2 = new BigDecimal(cashierInfo.getAmount());
        amount2.setScale(3);

        BigDecimal single_price = new BigDecimal(cashierInfo.getSingle_price());
        single_price.setScale(2);

        BigDecimal total_price = single_price.multiply(amount2);

        cashierInfo.setTotal_price(total_price.toString());
        cashierInfoMap.put(code,cashierInfo);
        mapTransitionList(cashierInfoMap);
        adapter.setNewData(cashierInfoList);
        mBaseView.changeBottomData();
    }

    /**
     * 减法操作
     */
    public void subtractCashierItem(String code){
        CashierInfo cashierInfo = cashierInfoMap.get(code);
        BigDecimal amount = new BigDecimal(cashierInfo.getAmount());
        amount = amount.subtract(new BigDecimal(1));
        //当当小于等于0时，移除当前的item ，清空map,list
        if(amount.floatValue() <= 0){
            cashierInfoMap.remove(code);
            mapTransitionList(cashierInfoMap);
            adapter.setNewData(cashierInfoList);
            mBaseView.changeBottomData();
            return;
        }
        amount.setScale(3);
        cashierInfo.setAmount(amount.toString());

        BigDecimal amount2 = new BigDecimal(cashierInfo.getAmount());
        amount2.setScale(3);

        BigDecimal single_price = new BigDecimal(cashierInfo.getSingle_price());
        single_price.setScale(2);

        BigDecimal total_price = single_price.multiply(amount2);

        cashierInfo.setTotal_price(total_price.toString());
        cashierInfoMap.put(code,cashierInfo);
        adapter.setNewData(cashierInfoList);
        mBaseView.changeBottomData();

    }


    /**
     * 乘法操作
     */
    public void multiplyCashierItem(String code, String amount){
        //TODO 修复商品输入框为0的崩溃bug
        if(Double.valueOf(amount) == 0){
            cashierInfoMap.remove(code);
            mapTransitionList(cashierInfoMap);
            adapter.setNewData(cashierInfoList);
            mBaseView.changeBottomData();
            return;
        }
        CashierInfo cashierInfo = cashierInfoMap.get(code);
        //判断当前商品是否有库存
        if(Double.valueOf(amount) >= cashierInfo.getTotal()){
            ToastUtils.showToast(ToastType.WARNING,"当前数量已经大于商品库存");
            BigDecimal amount_decimal = new BigDecimal(cashierInfo.getTotal());
            amount_decimal.setScale(3, RoundingMode.HALF_UP);
            cashierInfo.setAmount(amount_decimal.toString());
        }else {
            BigDecimal amount_decimal = new BigDecimal(amount);
            amount_decimal.setScale(3,RoundingMode.HALF_UP);
            cashierInfo.setAmount(amount_decimal.toString());
        }
        BigDecimal amount2 = new BigDecimal(cashierInfo.getAmount());
        amount2.setScale(3);

        BigDecimal single_price = new BigDecimal(cashierInfo.getSingle_price());
        single_price.setScale(2);

        BigDecimal total_price = single_price.multiply(amount2);

        cashierInfo.setTotal_price(total_price.toString());

        cashierInfoMap.put(code,cashierInfo);
        mapTransitionList(cashierInfoMap);
        adapter.setNewData(cashierInfoList);
        mBaseView.changeBottomData();
    }


    public List<CashierInfo> getCashierInfoList() {
        return cashierInfoList;
    }

    public Map<String, CashierInfo> getCashierInfoMap() {
        return cashierInfoMap;
    }

    public List<GoodsNameEntity> getGoodList() {
        return goodList;
    }



}
