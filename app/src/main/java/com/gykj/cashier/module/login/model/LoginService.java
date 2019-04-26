package com.gykj.cashier.module.login.model;



import com.gykj.cashier.module.login.entity.LoginEntity;
import com.gykj.cashier.module.login.entity.TokenEntity;
import com.gykj.cashier.module.login.entity.UserInfo;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午12:52
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface LoginService {

    @POST("api/user/login")
    Observable<LoginEntity> login(@Body RequestBody body);

    @GET("api/user/getToken/{deviceId}")
    Observable<BaseResult<TokenEntity>> getToken(@Path("deviceId") String deviceId);
}
