package com.gykj.cashier.module.inventory.data;


import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.gykj.cashier.module.cashier.entity.CashierEntity;
import com.gykj.cashier.module.cashier.model.CashierService;
import com.gykj.cashier.module.edit.entity.SearchType;
import com.gykj.cashier.module.inventory.entity.InventoryEntity;
import com.gykj.cashier.module.inventory.model.InventoryService;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.utils.HttpMethod;
import com.wrs.gykjewm.baselibrary.utils.JsonUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
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
public class InventoryApi {

    private String BASE_URL = Constant.TEXT_SERVER_ADDRESS;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");


    private InventoryService mService;
    private Retrofit retrofit;
    private OkHttpClient client;

    public static InventoryApi getInventoryApi(){
        return InventoryHolder.instance;
    }



    private static class InventoryHolder{
        private static InventoryApi instance = new InventoryApi();
    }

    private InventoryApi(){
        retrofit = new Retrofit.Builder()
                .client(HttpMethod.instance.getClient(0xF0))
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(InventoryService.class);
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
     * 查询商品盘点记录
     * @param codeOrName
     * @param type
     * @return
     */
    public Observable<InventoryEntity> inventoryList(String codeOrName, SearchType type,int offset){
        Map<String,Object> map = new ArrayMap<>();
        if(TextUtils.isEmpty(codeOrName)){
            map.put("offset",offset);
            map.put("limit",8);
        }else {
            switch (type){
                case BARCODE:
                    map.put("commodityBarcode",codeOrName);
                    break;
                case NAME:
                    map.put("commodityName",codeOrName);
                    break;
            }
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.inventoryList(body);
    }

    /**
     * 盘点添加
     * @param goodsId
     * @param inventorStock
     * @param differentStock
     * @return
     */
    public Observable<BaseResult<NullEntity>> inventoryAdd(long goodsId, BigDecimal inventBefore,BigDecimal inventorStock,BigDecimal differentStock){
        Map<String,Object> map = new ArrayMap<>();
        map.put("goodsId",goodsId);
        map.put("inventBefore",inventBefore);
        map.put("inventorStock",inventorStock);
        map.put("differentStock",differentStock);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.inventoryAdd(body);
    }
}
