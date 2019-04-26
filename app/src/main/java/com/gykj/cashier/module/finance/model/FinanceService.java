package com.gykj.cashier.module.finance.model;

import com.gykj.cashier.module.finance.entity.FinanceDetailEntity;
import com.gykj.cashier.module.finance.entity.FinanceEntity;
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
 * created: 28/8/18 下午1:23
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface FinanceService {


    @POST("api/settlement/list")
    Observable<BaseResult<List<FinanceEntity>>> settlementList(@Body RequestBody body);


    @GET("api/settlement/detail/{settlementId}")
    Observable<BaseResult<List<FinanceDetailEntity>>> settlementDetail(@Path("settlementId") int settlementId);
}
