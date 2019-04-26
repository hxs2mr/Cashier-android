package com.gykj.cashier.module.edit.ui.activity;

import android.content.Intent;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gykj.cashier.R;
import com.gykj.cashier.module.edit.entity.BarcodeEntity;
import com.gykj.cashier.module.edit.entity.ClickType;
import com.gykj.cashier.module.edit.entity.SearchType;
import com.gykj.cashier.module.edit.iview.IAddView;
import com.gykj.cashier.module.edit.presenter.AddPresenter;
import com.gykj.cashier.module.edit.ui.dialog.PopSpinner;
import com.gykj.cashier.module.storage.entity.BarCodeEntity;
import com.gykj.cashier.utils.BarCodeUtils;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.utils.DateUtil;
import com.wrs.gykjewm.baselibrary.utils.OthersUtils;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:新增商品界面
 * <p>
 * author: josh.lu
 * created: 21/8/18 下午1:47
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class AddGoodsActivity extends BaseActivity implements IAddView,DatePickerDialog.OnDateSetListener,TextWatcher {

    @BindView(R.id.header) HeaderLayoutView header;
    @BindView(R.id.goods_photo_iv) ImageView mGoodsPhotoIv;
    @BindView(R.id.goods_code_tv) EditText mGoodsCodeTv;
    @BindView(R.id.goods_name_et) EditText mGoodsNameEt;
    @BindView(R.id.goods_buy_price_et) EditText mGoodsBuyPriceEt;
    @BindView(R.id.goods_sale_price_et) EditText mGoodsSalePriceEt;
    @BindView(R.id.goods_storage_et) EditText mGoodsStorageEt;
    @BindView(R.id.goods_date_tv) TextView mGoodsDateTv;
    @BindView(R.id.goods_kinds_tv) TextView mGoodsKindsTv;
    @BindView(R.id.goods_status_tv) TextView mGoodsStatusTv;
    @BindView(R.id.goods_protect_et) EditText mGoodsProtectEt;
    @BindView(R.id.goods_storage_up_tv) TextView mGoodsSorageUpTv;
    @BindView(R.id.goods_storage_down_tv) TextView mGoodsSorageDownTv;
    @BindView(R.id.goods_product_tv) TextView mGoodsProductTv;



    AddPresenter presenter;

    private Calendar calendar;
    private Calendar maxOrMinCalendar;
    private String mGoodsCode;

    private DatePickerDialog datePickerDialog;
    private PopSpinner mStatusSpinner;

    private long kinds = -1;
    private int status = -1;

    private long  goodsId;

    private boolean first_into = true;

    private ClickType mClickType;

    private String photo_path = "";
    private OptionsPickerView mPvOptions;

    @Override
    public int initContentView() {
        return R.layout.activity_add_goods;
    }

    @Override
    public void initData() {
        goodsId =  getIntent().getExtras().getLong(Constant.GOODS_EDIT);
        presenter = new AddPresenter();
        presenter.attachView(this);
        calendar = Calendar.getInstance();
        presenter.classifyAllList();
        mGoodsCodeTv.addTextChangedListener(this);
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if(sdCardExist){
            photo_path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"gykj/";
        }else {
            photo_path = getFilesDir().getAbsolutePath() + File.separator+"gykj/";
        }
        File f = new File(photo_path);
        if(!f.exists()){
            f.mkdir();
        }
    }

    @Override
    public void initUi() {
        if(goodsId < 0){
            header.setTitle(getString(R.string.title_add_goods));
            mGoodsStorageEt.setInputType(InputType.TYPE_NULL);
        }else {
            header.setTitle(getString(R.string.title_update_goods));
            mGoodsProductTv.setVisibility(View.INVISIBLE);
        }

        header.setLeftImage(R.mipmap.icon_back);
        header.setRightVisible(View.INVISIBLE);
        initSpinner();

        //返回的分别是三个级别的选中位置
        mPvOptions = new OptionsPickerBuilder(AddGoodsActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                kinds = presenter.getNextList().get(options1).get(option2).getId();
                mGoodsKindsTv.setText(presenter.getNextList().get(options1).get(option2).getName());
            }
        }).build();
        watchProtectEt();
    }

    @OnClick({R.id.goods_photo_iv,R.id.goods_date_tv,R.id.goods_kinds_tv,R.id.goods_status_tv,R.id.goods_protect_et,
            R.id.goods_product_tv,R.id.goods_save_tv,R.id.goods_cancle_tv,R.id.goods_storage_up_tv,R.id.goods_storage_down_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.goods_photo_iv:
                takePhoto();
                break;
            case R.id.goods_cancle_tv:
                finish();
                break;
            case R.id.goods_date_tv:
                mClickType = ClickType.PRODUCE;
                showDataPicker();
                break;
            case R.id.goods_save_tv:
                save();
                break;
            case R.id.goods_product_tv:
                productCode();  //生产商品的编码 （当前时间戳）
                break;
            case R.id.goods_kinds_tv:
                presenter.getSpinnerAdapter().setDatas(presenter.getmGoodsClassifyList());
                //TODO 显示商品分类弹窗列表
                mPvOptions.show();
                break;
            case R.id.goods_status_tv:
                presenter.getSpinnerAdapter().setDatas(presenter.getmGoodsStatusList());
                mStatusSpinner.showAsDropDown(mGoodsStatusTv);
                break;
            case R.id.goods_storage_up_tv:
                mClickType = ClickType.SHELF;
                showDataPicker();
                break;
            case R.id.goods_storage_down_tv:
                mClickType = ClickType.DOWN;
                showDataPicker();
                break;
            case R.id.goods_protect_et:
                if(TextUtils.isEmpty(mGoodsDateTv.getText().toString().trim())){
                    ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_produce_date));
                    mGoodsProtectEt.setInputType(InputType.TYPE_NULL);
                }else {
                    mGoodsProtectEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                break;
        }
    }

    @Override
    public void takePhoto() {
        PictureSelector.create(AddGoodsActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(6)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .compress(true)// 是否压缩 true or false
                .compressSavePath(photo_path)//压缩图片保存地址
                .cropCompressQuality(70)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void showPhoto(String path) {
        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .error(R.mipmap.icon_select_photo)
                .placeholder(R.mipmap.icon_select_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(AddGoodsActivity.this)
                .load(path)
                .apply(options)
                .into(mGoodsPhotoIv);

    }

    @Override
    public void save() {
        String code = mGoodsCodeTv.getText().toString().trim();
        String name = mGoodsNameEt.getText().toString().trim();
        String buy_price = mGoodsBuyPriceEt.getText().toString().trim();
        String sale_price = mGoodsSalePriceEt.getText().toString().trim();
        String storage = mGoodsStorageEt.getText().toString().trim();
        String date = mGoodsDateTv.getText().toString().trim();
        String protect = mGoodsProtectEt.getText().toString().trim();
        String shelf_time = mGoodsSorageUpTv.getText().toString().trim();
        String down_time = mGoodsSorageDownTv.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_produce_code));
            return;
        }
        if(TextUtils.isEmpty(name)){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_goods_name));
            return;
        }
//        if(TextUtils.isEmpty(presenter.getmGoodsPicPath())){
//            ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_photo));
//            return;
//        }
        if(TextUtils.isEmpty(sale_price)){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_sale_price));
            return;
        }
        if(goodsId > 0){
            if(TextUtils.isEmpty(storage)){
                ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_storage));
                return;
            }
        }
        if(kinds < 0){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_kinds));
            return;
        }
        if(status < 0){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_status));
            return;
        }
        //判断商品保质期是否填写
        if(!TextUtils.isEmpty(protect)){
            if(TextUtils.isEmpty(down_time)){
                ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_storage_down));
                return;
            }else {
                long product_time = DateUtil.getTimeStamp("yyyy-MM-dd", date);
                long effective_time = Math.abs(Long.valueOf(protect)* 1000*60*60*24);
                long down = DateUtil.getTimeStamp("yyyy-MM-dd", down_time);

                if(down > product_time + effective_time){
                    ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_effective_time));
                    return;
                }
            }

        }
        BigDecimal buyDecimal = null;
        if(TextUtils.isEmpty(buy_price)){
            buyDecimal = new BigDecimal(0);
            buyDecimal.setScale(2, RoundingMode.HALF_UP);
        }else {
            buyDecimal = new BigDecimal(buy_price);
            buyDecimal.setScale(2,RoundingMode.HALF_UP);
        }
        BigDecimal saleDecimal = new BigDecimal(sale_price);
        saleDecimal.setScale(2,RoundingMode.HALF_UP);

        BigDecimal storageDecimal = null;
        if(goodsId >0 ){
            storageDecimal = new BigDecimal(storage);
            storageDecimal.setScale(3,RoundingMode.HALF_UP);
        }

        if(goodsId > 0){
            presenter.goodsUpdate(goodsId,code,name,TextUtils.isEmpty(presenter.getmGoodsPicPath())?"":presenter.getmGoodsPicPath(),kinds,status,buyDecimal,saleDecimal,storageDecimal,protect,date,shelf_time,down_time);
        }else {
            presenter.goodsAdd(code,name,TextUtils.isEmpty(presenter.getmGoodsPicPath())?"":presenter.getmGoodsPicPath(),kinds,status,buyDecimal,saleDecimal,protect,date,shelf_time,down_time);

        }

    }

    @Override
    public void productCode() {
        mGoodsCode = String.valueOf(System.currentTimeMillis());
        mGoodsCodeTv.setText(mGoodsCode);
    }

    @Override
    public void showDataPicker() {
        if(status < 0){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_select_goods_state));
            return;
        }
        if(null == datePickerDialog){
            datePickerDialog = DatePickerDialog.newInstance(
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        }
        maxOrMinCalendar = Calendar.getInstance();
        switch (mClickType){
            case PRODUCE: //生产日期
                maxOrMinCalendar.add(Calendar.DATE,-365);
                datePickerDialog.setMaxDate(calendar);
                datePickerDialog.setMinDate(maxOrMinCalendar);
                break;
            case DOWN:  //下架日期
                String down_time = mGoodsProtectEt.getText().toString().trim();
                if(TextUtils.isEmpty(down_time)){
                    maxOrMinCalendar.add(Calendar.DATE,700);
                }else {
                    long effective_time = Math.abs(Long.valueOf(down_time)*1000*60*60*24);
                    long current_time = DateUtil.getCurrentTimeStamp();
                    long produce_time = DateUtil.getTimeStamp("yyyy-MM-dd", mGoodsDateTv.getText().toString().trim());
                    //用当前时间-生产日期
                    long differ_time = current_time - produce_time;
                    //换算保质期时间戳
                    if(differ_time < effective_time){
                        int day = (int) ((effective_time - differ_time)/(1000*60*60*24));
                        maxOrMinCalendar.add(Calendar.DATE,day);
                    }else {
                        //商品过期
                        ToastUtils.showToast(ToastType.WARNING,"下架时间不能超过商品保质期！");
                        return;
                    }
                }
                if(first_into){
                    calendar.add(Calendar.DATE,1);
                    first_into = false;
                }
                datePickerDialog.setMinDate(calendar);
                datePickerDialog.setMaxDate(maxOrMinCalendar);
                break;
            case SHELF:
                datePickerDialog.setMaxDate(maxOrMinCalendar);
                datePickerDialog.setMinDate(maxOrMinCalendar);
                break;
        }
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void initSpinner() {
        mStatusSpinner = new PopSpinner(this);
        mStatusSpinner.setAdapter(presenter.getSpinnerAdapter());
        mStatusSpinner.setOnItemClickListener(new PopSpinner.OnItemClickListener() {
            @Override
            public void onClick(int i) {
                status = presenter.getmGoodsStatusList().get(i).getId();
                mGoodsStatusTv.setText(presenter.getmGoodsStatusList().get(i).getName());
                if(status == 0){
                    // 0-下架
                    mGoodsSorageUpTv.setText("");
                    mGoodsSorageUpTv.setClickable(false); //上架不可点击
                    mGoodsSorageDownTv.setText(DateUtil.getNowDateTime("yyyy-MM-dd"));
                    mGoodsSorageDownTv.setClickable(false);
                }else{
                    //  1-上架
                    mGoodsSorageUpTv.setText(DateUtil.getNowDateTime("yyyy-MM-dd"));
                    mGoodsSorageUpTv.setClickable(false); //下架不可点击
                    mGoodsSorageDownTv.setClickable(true);
                    mGoodsSorageDownTv.setText("");
                }
            }
        });
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showUpdateGoodsInfo(BarCodeEntity entity) {
        mGoodsCodeTv.setText(entity.getCommodityBarcode());
        mGoodsNameEt.setText(entity.getCommodityName());
        mGoodsSalePriceEt.setText(String.valueOf(entity.getSalesPrice()));
        mGoodsBuyPriceEt.setText(String.valueOf(entity.getBuyPrice()));
        mGoodsStorageEt.setText(String.valueOf(entity.getTotal()));
        mGoodsDateTv.setText(0==entity.getProduceTime()?"":DateUtil.stampToDate(entity.getProduceTime()));
        mGoodsProtectEt.setText(entity.getEffectiveDay());
        mGoodsSorageUpTv.setText(0==entity.getShelfTime()?"":String.valueOf(DateUtil.stampToDate(entity.getShelfTime())));
        mGoodsSorageDownTv.setText(0==entity.getDownTime()?"":String.valueOf(DateUtil.stampToDate(entity.getDownTime())));
        showPhoto(entity.getPic());
        presenter.setmGoodsPicPath(entity.getPic());
        kinds = Long.valueOf(entity.getClassificationId());
        for(int i =0;i<presenter.getClassfyList().size();i++){
            for(int j = 0;j<presenter.getClassfyList().get(i).getNext().size();j++){
                if(kinds == presenter.getClassfyList().get(i).getNext().get(j).getId()){
                    mGoodsKindsTv.setText(presenter.getClassfyList().get(i).getNext().get(j).getName());
                    break;
                }
            }
        }
        for(int i =0;i< presenter.getmGoodsStatusList().size();i++){
            if(entity.getStatus() == presenter.getmGoodsStatusList().get(i).getId()){
                status = entity.getStatus();
                mGoodsStatusTv.setText(presenter.getmGoodsStatusList().get(i).getName());
                break;
            }
        }

    }

    @Override
    public void loadGoodsInfo() {
        if(goodsId >= 0){
            presenter.selectOne(String.valueOf(goodsId));
        }
    }

    @Override
    public void showClassfySelect() {
        mPvOptions.setPicker(presenter.getClassfyList(),presenter.getNextList());
    }

    @Override
    public void watchProtectEt() {
        mGoodsProtectEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(TextUtils.isEmpty(mGoodsDateTv.getText().toString().trim())){
                        ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_produce_date));
                        mGoodsProtectEt.setInputType(InputType.TYPE_NULL);
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void showChechGoodInfo(BarcodeEntity.ResultBean result) {
        mGoodsProductTv.setVisibility(View.INVISIBLE);
        showPhoto(null == result.getImg()?"":String.valueOf(result.getImg()));
        mGoodsNameEt.setText(result.getGoodsName());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if(selectList.size()>0){
                        presenter.uplodeGoodsPic(new File(selectList.get(0).getCompressPath()));
                        showPhoto(selectList.get(0).getCompressPath());
                    }
                    break;
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(year)
                .append("-")
                .append(monthOfYear +1)
                .append("-")
                .append(dayOfMonth);
        switch (mClickType){
            case PRODUCE:
                mGoodsDateTv.setText(buffer.toString());
                mGoodsProtectEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case SHELF:
                mGoodsSorageUpTv.setText(buffer.toString());
                break;
            case DOWN:
                mGoodsSorageDownTv.setText(buffer.toString());
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String s1 = s.toString();
        if(s1.contains("\n")){
            if(BarCodeUtils.isBarCodeLegal(s1.trim())){
                mGoodsCodeTv.setText(s1.split("\n")[0]);
                presenter.checkBarcode(s1.trim());
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != presenter){
            presenter.cancel();
            presenter.detachView();
        }
    }
}
