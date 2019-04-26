package com.gykj.cashier.module.setting.model;

import com.gykj.cashier.module.order.entity.OrderDetailEntity;
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
 * created: 28/8/18 下午12:11
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface ModifyService {

    @POST("api/user/updatePass")
    Observable<BaseResult<NullEntity>> updatePass(@Body RequestBody body);

    @GET("api/user/logout")
    Observable<BaseResult<NullEntity>> logout();
}
