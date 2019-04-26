package com.gykj.cashier.module.edit.data;


import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.gykj.cashier.module.edit.entity.BarcodeEntity;
import com.gykj.cashier.module.edit.entity.ClassfyListEntity;
import com.gykj.cashier.module.edit.entity.ClassifyEntity;
import com.gykj.cashier.module.edit.entity.PhotoEntity;
import com.gykj.cashier.module.edit.entity.SearchType;
import com.gykj.cashier.module.edit.model.EditService;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.gykj.cashier.module.storage.model.StorageService;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.NullEntity;
import com.wrs.gykjewm.baselibrary.utils.HttpMethod;
import com.wrs.gykjewm.baselibrary.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Path;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午12:51
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class EditApi {

    private String BASE_URL = Constant.TEXT_SERVER_ADDRESS;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");


    private EditService mService;
    private Retrofit retrofit;
    private OkHttpClient client;


    public static EditApi getEditApi(){
        return EditApi.EditHolder.instance;
    }



    private static class EditHolder{
        private static EditApi instance = new EditApi();
    }

    public EditApi(){
        initRetrofit();
    }

    public void initRetrofit(){
        retrofit = new Retrofit.Builder()
                .client(HttpMethod.instance.getClient(0xF0))
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(EditService.class);
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
     * 通过商品名字或者条形码查询列表
     * @param nameOrCode
     * @param type
     * @return
     */
    public Observable<BaseResult<List<GoodsNameEntity>>> goodsList(String nameOrCode, SearchType type,int offset){
        Map<String,Object> map = new ArrayMap<>();
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


    /**
     * 上传商品图片
     * @param files
     * @return
     */
    public Observable<PhotoEntity> uplodeGoodsPic(File files){
        MediaType type=MediaType.parse("multipart/form-data");//"text/xml;charset=utf-8"
        RequestBody fileBody=RequestBody.create(type,files);
        RequestBody requestBody = new MultipartBody.Builder().setType(type).addFormDataPart("file", files.getName(), fileBody).build();
        return mService.uplodeGoodsPic(requestBody);
    }


    /**
     * 获取分类列表
     * @param classifyId
     * @return
     */
    public Observable<BaseResult<List<ClassifyEntity>>> classifyList(String classifyId){
        return mService.classifyList(classifyId);
    }

    /**
     * 新增商品
     * @param commodityBarcode
     * @param commodityName
     * @param pic
     * @param classifyId
     * @param status
     * @param buyPrice
     * @param salesPrice
     * @param effectiveDay
     * @param produceTime
     * @return
     */
    public Observable<BaseResult<NullEntity>> goodsAdd(String commodityBarcode,String commodityName,String pic,long classifyId,int status,
                                                       BigDecimal buyPrice,BigDecimal salesPrice,String effectiveDay ,String produceTime,
                                                       String shelfTime,String downTime){
        Map<String,Object> map = new ArrayMap<>();
        map.put("commodityBarcode",commodityBarcode);
        map.put("commodityName",commodityName);
        map.put("pic",pic);
        map.put("classifyId",classifyId);
        map.put("status",status);
        map.put("buyPrice",buyPrice);
        map.put("salesPrice",salesPrice);
        map.put("effectiveDay",effectiveDay);
        map.put("produceTime",produceTime);
        map.put("shelfTime",shelfTime);
        map.put("downTime",downTime);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.goodsAdd(body);
    }


    /**
     * 修改商品
     * @param id
     * @param commodityBarcode
     * @param commodityName
     * @param pic
     * @param classifyId
     * @param status
     * @param buyPrice
     * @param salesPrice
     * @param total
     * @param effectiveDay
     * @param produceTime
     * @return
     */
    public Observable<BaseResult<NullEntity>> goodsUpdate(long id ,String commodityBarcode,String commodityName,String pic,long classifyId,int status,
                                                       BigDecimal buyPrice,BigDecimal salesPrice,BigDecimal total,String effectiveDay ,String produceTime,
                                                          String shelfTime,String downTime){
        Map<String,Object> map = new ArrayMap<>();
        map.put("id",id);
        map.put("commodityBarcode",commodityBarcode);
        map.put("commodityName",commodityName);
        map.put("pic",pic);
        map.put("classifyId",classifyId);
        map.put("status",status);
        map.put("buyPrice",buyPrice);
        map.put("salesPrice",salesPrice);
        map.put("total",total);
        map.put("effectiveDay",effectiveDay);
        map.put("produceTime",produceTime);
        map.put("shelfTime",shelfTime);
        map.put("downTime",downTime);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, JsonUtils.map2json(map));
        return mService.goodsUpdate(body);
    }


    /**
     *  通过ID 查询商品信息
     * @param goodsId
     * @return
     */
    public Observable<BaseResult<BarCodeEntity>> selectOne( String goodsId){
        return mService.selectOne(goodsId);
    }


    /**
     * 获取所有商品分类
     * @return
     */
    public Observable<BaseResult<List<ClassfyListEntity>>> classifyAllList(){
        return mService.classifyAllList();
    }

    /**
     * 检查商品码是否存在
     * @param barcode
     * @return
     */
    public Observable<BaseResult<BarcodeEntity>> checkBarcode(String barcode){
        return mService.checkBarcode(barcode);
    }




}
