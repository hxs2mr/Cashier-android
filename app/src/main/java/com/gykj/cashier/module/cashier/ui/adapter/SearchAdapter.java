package com.gykj.cashier.module.cashier.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gykj.cashier.R;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * desc   : 搜索适配器
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/12/510:44
 * version: 1.0
 */
public class SearchAdapter extends BaseQuickAdapter<GoodsNameEntity,BaseViewHolder> {

    private Context mContext;

    public SearchAdapter(Context context, @Nullable List<GoodsNameEntity> data) {
        super(R.layout.layout_search_item, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsNameEntity item) {
        helper.setText(R.id.search_name_tv,item.getCommodityName());
        helper.setText(R.id.search_code_tv,item.getCommodityBarcode());
        ImageView imageView = helper.getView(R.id.search_select_iv);
        if(item.isCheck()){
            imageView.setImageResource(R.mipmap.icon_circle_selected);
        }else {
            imageView.setImageResource(R.mipmap.icon_circle_normal);
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.icon_goods_default)
                .placeholder(R.mipmap.icon_goods_default);
        Glide.with(mContext)
                .load(item.getPic())
                .apply(options)
                .into((CircleImageView) helper.getView(R.id.search_good_iv));
        helper.addOnClickListener(R.id.search_layout);
    }
}
