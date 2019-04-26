package com.gykj.cashier.module.cashier.data;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.google.gson.JsonArray;
import com.gykj.cashier.module.cashier.entity.CashierEntity;
import com.gykj.cashier.module.cashier.entity.CashierInfo;
import com.gykj.cashier.module.cashier.entity.GoodsEntity;
import com.gykj.cashier.module.cashier.entity.OrderEntity;
import com.gykj.cashier.module.cashier.entity.PayEntity;
import com.gykj.cashier.module.cashier.entity.PayType;
import com.gykj.cashier.module.cashier.model.CashierService;
import com.gykj.cashier.module.edit.entity.SearchType;
import com.gykj.cashier.module.order.entity.UserDetailEntity;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.orhanobut.logger.Logger;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.utils.HttpMethod;
import com.wrs.gykjewm.baselibrary.utils.JsonUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午12:51
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CashierApi {

    private String BASE_URL = Constant.TEXT_SERVER_ADDRESS;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");


    private CashierService mService;
    private Retrofit retrofit;
    private OkHttpClient client;

    public static CashierApi getCashierApi(){
        return CashierHolder.instance;
    }



    private static class CashierHolder{
        private static CashierApi instance = new CashierApi();
    }

    private CashierApi(){
        retrofit = new Retrofit.Builder()
                .client(HttpMethod.instance.getClient(0xF0))
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(CashierService.class);
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
     * 通过条形码查询商品
     * @param barcode
     * @return
     */
    public Observable<BaseResult<BarCodeEntity>> selectByBarcode(String barcode){
        return mService.selectByBarcode(barcode);
    }


    public Observable<BaseResult<NullEntity>> testFace(byte[] bytes,int userId,int count){
        Map<String,Object> map = new HashMap<>();
        map.put("features",bytes);
        map.put("featuresType",count);
        map.put("schoolId",1);
        map.put("userId",userId);
        map.put("userType",1);
        map.put("userSex",1);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.testFace(body);
    }

    public Observable<OrderEntity> createOrder(List<CashierInfo> list){
        List<GoodsEntity> goodsList = new ArrayList<>();
        for(int i =0;i< list.size();i++){
            GoodsEntity entity = new GoodsEntity();
            entity.setGoodsId(list.get(i).getGoodsId());
            entity.setNum(new BigDecimal(list.get(i).getAmount()).setScale(3));
            goodsList.add(entity);
        }
        JsonArray jsonElements = JsonUtils.listToJson(goodsList);

        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, jsonElements.toString());
        return mService.createOrder(body);
    }

    /**
     * 支付订单
     * @param orderId 订单编号
     * @param payType 1 -- 现金   ，2 余额
     * @param userId  现金不传 ，余额传
     * @param userType -- 1-学生，2-老师
     * @return
     */
    public Observable<BaseResult<PayEntity>> payOrder(PayType type, long orderId, int payType, long userId, int userType){
        Map<String,Object> map = new HashMap<>();
        map.put("orderId",orderId);
        map.put("payType",payType);
        switch (type){
            case CASH:
                break;
            case FACE:
                map.put("userId",userId);
                break;
        }
        map.put("userType",userType);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.payOrder(body);
    }


    /**
     * 人脸比对
     * @param features
     * @return
     */
    public Observable<BaseResult<NullEntity>> faceMatching(byte[] features){
        Map<String,Object> map = new HashMap<>();
        map.put("features",features);
        String facetures = JsonUtils.map2json(map);
        System.out.println("=====ArrayMap====="+facetures);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.faceMatching(body);
    }


    /**
     * 通过id获取学生详细信息
     * @param accId
     * @return
     */
    public Observable<BaseResult<UserDetailEntity>> getUserInfoByAccId(long accId){
        return mService.getUserInfoByAccId(accId);
    }


    /**
     * 获取用户信息
     * @param userId
     * @param userType
     * @return
     */
    public Observable<BaseResult<UserDetailEntity>> getUserInfo(long userId,int userType){
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("userType",userType);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.getUserInfo(body);
    }


    /**
     * 通过商品名字或者条形码查询列表
     * @param nameOrCode
     * @param type
     * @return
     */
    public Observable<BaseResult<List<GoodsNameEntity>>> goodsList(String nameOrCode, SearchType type, int offset){
        Map<String,Object> map = new HashMap<>();
        if(TextUtils.isEmpty(nameOrCode)){
            map.put("offset",offset);
        }else {
            switch (type){
                case NAME:
                    map.put("commodityName",nameOrCode);
                    break;
                case BARCODE:
                    map.put("commodityBarcode",nameOrCode);
                    break;
            }
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.goodsList(body);
    }

}
