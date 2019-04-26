package com.gykj.cashier.module.finance.data;



import android.support.v4.util.ArrayMap;

import com.gykj.cashier.module.finance.entity.FinanceDetailEntity;
import com.gykj.cashier.module.finance.entity.FinanceEntity;
import com.gykj.cashier.module.finance.model.FinanceService;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
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

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午12:51
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class FinanceApi {

    private String BASE_URL = Constant.TEXT_SERVER_ADDRESS;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");


    private FinanceService mService;
    private Retrofit retrofit;
    private OkHttpClient client;

    public static FinanceApi getFinanceApi(){
        return FinanceHolder.instance;
    }



    private static class FinanceHolder{
        private static FinanceApi instance = new FinanceApi();
    }

    private FinanceApi(){
        retrofit = new Retrofit.Builder()
                .client(HttpMethod.instance.getClient(0xF0))
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(FinanceService.class);
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
     * 获取财务清单详情
     * @param settlementId
     * @return
     */
    public Observable<BaseResult<List<FinanceDetailEntity>>> settlementDetail(int settlementId){
        return mService.settlementDetail(settlementId);
    }


    /**
     * 获取财经结算列表数据
     * @param limit
     * @param offset
     * @return
     */
    public Observable<BaseResult<List<FinanceEntity>>> settlementList(int limit, int offset){
        Map<String,Object> map = new ArrayMap<>();
        map.put("limit",limit);
        map.put("offset",offset);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.settlementList(body);
    }


}
