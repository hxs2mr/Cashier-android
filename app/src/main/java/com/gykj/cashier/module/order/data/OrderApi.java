package com.gykj.cashier.module.order.data;



import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.gykj.cashier.module.order.entity.OrderDetailEntity;
import com.gykj.cashier.module.order.entity.OrderEntity;
import com.gykj.cashier.module.order.entity.UserDetailEntity;
import com.gykj.cashier.module.order.model.OrderService;

import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;

import com.wrs.gykjewm.baselibrary.utils.HttpMethod;
import com.wrs.gykjewm.baselibrary.utils.JsonUtils;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午12:51
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class OrderApi {

    private String BASE_URL = Constant.TEXT_SERVER_ADDRESS;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");


    private OrderService mService;
    private Retrofit retrofit;
    private OkHttpClient client;

    public static OrderApi getOrderApi(){
        return OrderHolder.instance;
    }



    private static class OrderHolder{
        private static OrderApi instance = new OrderApi();
    }

    private OrderApi(){
        retrofit = new Retrofit.Builder()
                .client(HttpMethod.instance.getClient(0xF0))
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(OrderService.class);
        buildHttpClient();

    }


    private void buildHttpClient(){
        this.client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        return response;
                    }
                })
                .build();
    }

    /**
     * 通过商品编号查询订单列表
     * @param orderNum
     * @return
     */
    public Observable<BaseResult<List<OrderEntity>>> orderList(String orderNum,int offset){
        Map<String,Object> map = new ArrayMap<>();
        if(offset > 0){
            map.put("offset",offset);
        }
        if(!TextUtils.isEmpty(orderNum)){
            map.put("orderNum",orderNum);
        }
        map.put("status",2);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.orderList(body);
    }


    /**
     * 查询订单列表详情数据
     * @param orderId
     * @return
     */
    public Observable<BaseResult<List<OrderDetailEntity>>> orderDetail(long orderId){
        return mService.orderDetail(orderId);
    }

    /**
     * 通过id获取学生详细信息
     * @param accId
     * @return
     */
    public Observable<BaseResult<UserDetailEntity>> getUserInfoByAccId(long accId){
        return mService.getUserInfoByAccId(accId);
    }


}
