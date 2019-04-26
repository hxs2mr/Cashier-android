package com.gykj.cashier.module.storage.data;


import android.support.v4.util.ArrayMap;

import com.gykj.cashier.module.login.entity.LoginEntity;
import com.gykj.cashier.module.login.entity.TokenEntity;
import com.gykj.cashier.module.login.model.LoginService;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.gykj.cashier.module.storage.entity.SearchType;
import com.gykj.cashier.module.storage.entity.StorageListEntity;
import com.gykj.cashier.module.storage.model.StorageService;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.utils.HttpMethod;
import com.wrs.gykjewm.baselibrary.utils.JsonUtils;

import java.io.IOException;
import java.math.BigDecimal;
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
public class StorageApi {

    private String BASE_URL = Constant.TEXT_SERVER_ADDRESS;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");


    private StorageService mService;
    private Retrofit retrofit;
    private OkHttpClient client;


    public static StorageApi getStorageApi(){
        return StorageApi.StorageHolder.instance;
    }



    private static class StorageHolder{
        private static StorageApi instance = new StorageApi();
    }

    public StorageApi(){
        initRetrofit();
    }

    public void initRetrofit(){
        retrofit = new Retrofit.Builder()
                .client(HttpMethod.instance.getClient(0xF0))
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(StorageService.class);
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
     * 通过名字查询商品列表
     * @param commodityName
     * @return
     */
    public Observable<BaseResult<List<GoodsNameEntity>>> goodsList(String commodityName){
        Map<String,Object> map = new ArrayMap<>();
        map.put("commodityName",commodityName);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.goodsList(body);
    }


    /**
     * 通过条形码查询商品
     * @param barcode
     * @return
     */
    public Observable<BaseResult<BarCodeEntity>> selectByBarcode(String barcode){
        return mService.selectByBarcode(barcode);
    }


    /**
     * 入库商品
     * @param goodsId
     * @param status  0 --增加    1--减少
     * @param total
     * @return
     */
    public Observable<BaseResult<NullEntity>> addStock(long goodsId, int status, BigDecimal total){
        Map<String,Object> map = new ArrayMap<>();
        map.put("goodsId",goodsId);
        map.put("status",status);
        map.put("total",total);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.addStock(body);
    }

    /**
     * 获取入库列表
     * @param commodityBarcode
     * @param commodityName
     * @param offset
     * @param type
     * @return
     */
    public Observable<BaseResult<List<StorageListEntity>>> stockList(String commodityBarcode, String commodityName, int offset, SearchType type){
        Map<String,Object> map = new ArrayMap<>();
        map.put("offset",offset);
        switch (type){
            case ALL:
                break;
            case CODE:
                map.put("commodityBarcode",commodityBarcode);
                break;
            case NAME:
                map.put("commodityName",commodityName);
                break;
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.stockList(body);
    }
}
