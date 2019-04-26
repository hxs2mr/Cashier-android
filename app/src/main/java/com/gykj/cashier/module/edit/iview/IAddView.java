package com.gykj.cashier.module.edit.iview;

import com.gykj.cashier.module.edit.entity.BarcodeEntity;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.wrs.gykjewm.baselibrary.base.IBaseView;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 21/8/18 下午3:36
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface IAddView extends IBaseView {

    void takePhoto();

    void showPhoto(String path);

    void save();

    void productCode();

    void showDataPicker();

    void initSpinner();

    void finishActivity();

    void showUpdateGoodsInfo(BarCodeEntity entity);

    void loadGoodsInfo();

    void showClassfySelect();

    void watchProtectEt();

    void showChechGoodInfo(BarcodeEntity.ResultBean result);
}
