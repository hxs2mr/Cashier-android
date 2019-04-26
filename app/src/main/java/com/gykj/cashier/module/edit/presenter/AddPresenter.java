package com.gykj.cashier.module.edit.presenter;

import com.gykj.cashier.R;
import com.gykj.cashier.module.edit.data.EditApi;
import com.gykj.cashier.module.edit.entity.BarcodeEntity;
import com.gykj.cashier.module.edit.entity.ClassfyListEntity;
import com.gykj.cashier.module.edit.entity.ClassifyEntity;
import com.gykj.cashier.module.edit.entity.PhotoEntity;
import com.gykj.cashier.module.edit.iview.IAddView;
import com.gykj.cashier.module.edit.ui.adapter.SpinnerAdapter;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.io.File;
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
 * created: 21/8/18 下午3:36
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class AddPresenter extends BasePresenter<IAddView> {

    EditApi api;

    private List<ClassifyEntity> mGoodsStatusList = new ArrayList<>();
    private List<ClassifyEntity> mGoodsClassifyList = new ArrayList<>();

    private List<ClassfyListEntity> classfyList = new ArrayList<>();
    private List<List<ClassfyListEntity.NextBean>> nextList = new ArrayList<>();

    private SpinnerAdapter spinnerAdapter;


    private String mGoodsPicPath = "";

    @Override
    public void attachView(IAddView view) {
        super.attachView(view);
        api = EditApi.getEditApi();
        initGoodsStatusList();
        initSpinnerAdapter();
    }

    /**
     * 上传图片
     * @param files
     */
    public void uplodeGoodsPic(File files){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.uplodeGoodsPic(files)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PhotoEntity>() {
                    @Override
                    public void accept(PhotoEntity photoEntity) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(null != photoEntity && photoEntity.getCode()==0){
                            mGoodsPicPath = photoEntity.getData().get(0);
                            ToastUtils.showToast(ToastType.SUCCESS,photoEntity.getMsg());
                        }else if(photoEntity.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,photoEntity.getMsg());
                        } else {
                            ToastUtils.showToast(ToastType.WARNING,photoEntity.getMsg());
                        }
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }

    /**
     * 获取下拉分类列表
     * @param classifyId
     */
    public void classifyList(String classifyId){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.classifyList(classifyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<List<ClassifyEntity>>>() {
                    @Override
                    public void accept(BaseResult<List<ClassifyEntity>> listBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(null != listBaseResult && listBaseResult.isSuccessed()){
                            mBaseView.loadGoodsInfo();
                            mGoodsClassifyList = listBaseResult.getData();
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
     * 获取所有商品分类   pickerview中你的数据
     */
    public void classifyAllList(){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.classifyAllList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<List<ClassfyListEntity>>>() {
                    @Override
                    public void accept(BaseResult<List<ClassfyListEntity>> listBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(listBaseResult.isSuccessed()){
                            classfyList = listBaseResult.getData();
                            changeOptions2List();
                            mBaseView.showClassfySelect();
                            mBaseView.loadGoodsInfo();
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


    public void goodsAdd(String commodityBarcode, String commodityName, String pic, long classifyId, int status,
                         BigDecimal buyPrice, BigDecimal salesPrice, String effectiveDay , String produceTime,String shelfTime,String downTime){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.goodsAdd(commodityBarcode, commodityName, pic, classifyId, status, buyPrice, salesPrice, effectiveDay, produceTime,shelfTime,downTime)
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

    public void goodsUpdate(long id,String commodityBarcode, String commodityName, String pic, long classifyId, int status,
                         BigDecimal buyPrice, BigDecimal salesPrice, BigDecimal total, String effectiveDay , String produceTime,String shelfTime,String downTime){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.goodsUpdate(id,commodityBarcode, commodityName, pic, classifyId, status, buyPrice, salesPrice, total, effectiveDay, produceTime,shelfTime,downTime)
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


    public void selectOne( String classifyId){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.selectOne(classifyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<BarCodeEntity>>() {
                    @Override
                    public void accept(BaseResult<BarCodeEntity> barCodeEntityBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(null != barCodeEntityBaseResult && barCodeEntityBaseResult.isSuccessed()){
                            mBaseView.showUpdateGoodsInfo(barCodeEntityBaseResult.getData());
                            ToastUtils.showToast(ToastType.SUCCESS,barCodeEntityBaseResult.getMsg());
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

    public void checkBarcode(final String barcode){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable subscribe = api.checkBarcode(barcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<BarcodeEntity>>() {
                    @Override
                    public void accept(BaseResult<BarcodeEntity> barcodeEntityBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(null != barcodeEntityBaseResult && barcodeEntityBaseResult.isSuccessed()){
                            if(barcodeEntityBaseResult.getData().getFlag() == 1){
                                ToastUtils.showToast(ToastType.WARNING,"该商品存在，请勿重复添加");
                                mBaseView.finishActivity();
                            }else {
                                if(barcodeEntityBaseResult.getData().getResult().isFlag()){
                                    mBaseView.showChechGoodInfo(barcodeEntityBaseResult.getData().getResult());
                                    mGoodsPicPath = null == barcodeEntityBaseResult.getData().getResult().getImg()? "":String.valueOf(barcodeEntityBaseResult.getData().getResult().getImg());
                                    ToastUtils.showToast(ToastType.SUCCESS,barcodeEntityBaseResult.getMsg());
                                }else {
                                    ToastUtils.showToast(ToastType.WARNING,barcodeEntityBaseResult.getData().getResult().getRemark());
                                }
                            }
                        }else if(barcodeEntityBaseResult.getCode() == 100){
                            ActivityStackManager.getManager().clearActivityToLogin(LoginActivity.class);
                            ToastUtils.showToast(ToastType.WARNING,barcodeEntityBaseResult.getMsg());
                        } else {
                            ToastUtils.showToast(ToastType.WARNING,barcodeEntityBaseResult.getMsg());
                        }
                    }
                }, getErrorConsumer());
        addDisposable(subscribe);
    }


    private void initGoodsStatusList(){
        ClassifyEntity entity1 = new ClassifyEntity();
        entity1.setId(0);
        entity1.setName("下架");
        mGoodsStatusList.add(entity1);

        ClassifyEntity entity2 = new ClassifyEntity();
        entity2.setId(1);
        entity2.setName("上架");
        mGoodsStatusList.add(entity2);
    }

    public void initSpinnerAdapter(){
        if(null == spinnerAdapter){
            spinnerAdapter = new SpinnerAdapter(mContext,mGoodsClassifyList);
        }
    }

    private void changeOptions2List(){
        for(int i = 0;i<classfyList.size();i++){
            nextList.add(classfyList.get(i).getNext());
        }
    }


    public List<ClassifyEntity> getmGoodsStatusList() {
        return mGoodsStatusList;
    }

    public List<ClassifyEntity> getmGoodsClassifyList() {
        return mGoodsClassifyList;
    }

    public SpinnerAdapter getSpinnerAdapter() {
        return spinnerAdapter;
    }

    public String getmGoodsPicPath() {
        return mGoodsPicPath;
    }

    public void setmGoodsPicPath(String mGoodsPicPath) {
        this.mGoodsPicPath = mGoodsPicPath;
    }

    public List<ClassfyListEntity> getClassfyList() {
        return classfyList;
    }

    public List<List<ClassfyListEntity.NextBean>> getNextList() {
        return nextList;
    }


}
