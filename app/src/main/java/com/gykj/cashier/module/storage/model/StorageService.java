package com.gykj.cashier.module.storage.model;

import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.gykj.cashier.module.storage.entity.StorageListEntity;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;


import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 20/8/18 下午2:36
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface StorageService {

    @GET("api/goods/selectByBarcode/{barcode}")
    Observable<BaseResult<BarCodeEntity>> selectByBarcode(@Path("barcode") String barcode);

    @POST("api/stock/list")
    Observable<BaseResult<List<StorageListEntity>>> stockList(@Body RequestBody body);

    @POST("api/goods/list")
    Observable<BaseResult<List<GoodsNameEntity>>> goodsList(@Body RequestBody body);

    @POST("api/stock/addStock")
    Observable<BaseResult<NullEntity>> addStock(@Body RequestBody body);
}
