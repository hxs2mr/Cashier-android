package com.gykj.cashier.module.inventory.model;



import com.gykj.cashier.module.inventory.entity.InventoryEntity;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;

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
 * created: 22/8/18 下午1:04
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface InventoryService {


    /**
     * 商品盘点列表
     * @param body
     * @return
     */
    @POST("api/inventory/list")
    Observable<InventoryEntity> inventoryList(@Body RequestBody body);


    /**
     * 库存商品详情
     * @param
     * @return
     */
    @GET("api/inventory/{recordId}")
    Observable<BaseResult<NullEntity>>inventoryrecordId(@Path("recordId") int recordId);


    @POST("api/inventory/add")
    Observable<BaseResult<NullEntity>> inventoryAdd(@Body RequestBody body);

}
