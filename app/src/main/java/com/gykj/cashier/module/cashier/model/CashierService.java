package com.gykj.cashier.module.cashier.model;


import com.gykj.cashier.module.cashier.entity.CashierEntity;
import com.gykj.cashier.module.cashier.entity.OrderEntity;
import com.gykj.cashier.module.cashier.entity.PayEntity;
import com.gykj.cashier.module.cashier.ui.activity.CashierActivity;
import com.gykj.cashier.module.cashier.ui.activity.CollectActivity;
import com.gykj.cashier.module.order.entity.UserDetailEntity;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
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
 * created: 14/8/18 下午1:19
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface CashierService {


    @GET("api/goods/selectByBarcode/{barcode}")
    Observable<BaseResult<BarCodeEntity>> selectByBarcode(@Path("barcode") String barcode);

    @POST("api/face/add")
    Observable<BaseResult<NullEntity>> testFace(@Body RequestBody body);

    @POST("api/order/createOrder")
    Observable<OrderEntity> createOrder(@Body RequestBody body);

    @POST("api/order/payOrder")
    Observable<BaseResult<PayEntity>> payOrder(@Body RequestBody body);

    @POST("api/face/faceMatching")
    Observable<BaseResult<NullEntity>> faceMatching(@Body RequestBody body);

    @GET("api/order/getUserInfoByAccId/{accId}")
    Observable<BaseResult<UserDetailEntity>> getUserInfoByAccId(@Path("accId") long accId);

    @POST("api/order/getUserInfo")
    Observable<BaseResult<UserDetailEntity>> getUserInfo(@Body RequestBody body);

    @POST("api/goods/list")
    Observable<BaseResult<List<GoodsNameEntity>>> goodsList(@Body RequestBody body);
}

