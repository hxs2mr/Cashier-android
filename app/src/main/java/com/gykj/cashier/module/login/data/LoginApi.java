package com.gykj.cashier.module.login.data;


import android.support.v4.util.ArrayMap;

import com.gykj.cashier.module.cashier.data.CashierApi;
import com.gykj.cashier.module.login.entity.LoginEntity;
import com.gykj.cashier.module.login.entity.TokenEntity;
import com.gykj.cashier.module.login.entity.UserInfo;
import com.gykj.cashier.module.login.model.LoginService;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.utils.HttpMethod;
import com.wrs.gykjewm.baselibrary.utils.JsonUtils;

import java.io.IOException;
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
import retrofit2.http.Query;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午12:51
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class LoginApi {

    private String BASE_URL = Constant.TEXT_SERVER_ADDRESS;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");


    private LoginService mService;
    private Retrofit retrofit;
    private OkHttpClient client;


    public static LoginApi getLoginApi(){
        return LoginApi.LoginHolder.instance;
    }



    private static class LoginHolder{
        private static LoginApi instance = new LoginApi();
    }

    public LoginApi(){
        initRetrofit();
    }

    public void initRetrofit(){
        retrofit = new Retrofit.Builder()
                .client(HttpMethod.instance.getClient(0xF0))
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(LoginService.class);
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
     * 登陆
     * @param account
     * @param pass
     * @return
     */
    public Observable<LoginEntity> login(String account, String pass){
        Map<String,Object> map = new ArrayMap<>();
        map.put("account",account);
        map.put("pass",pass);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.login(body);
    }


    /**
     * 获取Token
     * @param deviceId
     * @return
     */
    public Observable<BaseResult<TokenEntity>> getToken(String deviceId){
        return mService.getToken(deviceId);
    }

}
