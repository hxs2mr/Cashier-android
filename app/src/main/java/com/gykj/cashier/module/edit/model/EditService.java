package com.gykj.cashier.module.edit.model;

import com.gykj.cashier.module.edit.entity.BarcodeEntity;
import com.gykj.cashier.module.edit.entity.ClassfyListEntity;
import com.gykj.cashier.module.edit.entity.ClassifyEntity;
import com.gykj.cashier.module.edit.entity.PhotoEntity;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
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
 * created: 21/8/18 下午1:28
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface EditService {

    @POST("api/goods/list")
    Observable<BaseResult<List<GoodsNameEntity>>> goodsList(@Body RequestBody body);

    @POST("api/file/uplodeGoodsPic")
    Observable<PhotoEntity> uplodeGoodsPic(@Body RequestBody body);

    @GET("api/classify/list/{classifyId}")
    Observable<BaseResult<List<ClassifyEntity>>> classifyList(@Path("classifyId") String classifyId);

    @POST("/api/goods/add")
    Observable<BaseResult<NullEntity>> goodsAdd(@Body RequestBody body);

    @POST("/api/goods/update")
    Observable<BaseResult<NullEntity>> goodsUpdate(@Body RequestBody body);


    @GET("api/goods/selectOne/{goodsId}")
    Observable<BaseResult<BarCodeEntity>> selectOne(@Path("goodsId") String goodsId);

    @GET("api/classify/allList")
    Observable<BaseResult<List<ClassfyListEntity>>> classifyAllList();

    @GET("api/goods/checkBarcode/{barcode}")
    Observable<BaseResult<BarcodeEntity>> checkBarcode(@Path("barcode") String barcode);


}
