package com.gykj.cashier.module.order.model;

import com.gykj.cashier.module.order.entity.OrderDetailEntity;
import com.gykj.cashier.module.order.entity.OrderEntity;
import com.gykj.cashier.module.order.entity.UserDetailEntity;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;

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
 * created: 24/8/18 下午1:41
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface OrderService {

    @POST("api/order/orderList")
    Observable<BaseResult<List<OrderEntity>>> orderList(@Body RequestBody body);

    @GET("api/order/orderDetail/{orderId}")
    Observable<BaseResult<List<OrderDetailEntity>>> orderDetail(@Path("orderId") long orderId);

    @GET("api/order/getUserInfoByAccId/{accId}")
    Observable<BaseResult<UserDetailEntity>> getUserInfoByAccId(@Path("accId") long accId);

}
