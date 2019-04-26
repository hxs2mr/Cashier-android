package com.gykj.cashier.module.cashier.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.iview.OnCheckListener;
import com.gykj.cashier.module.cashier.ui.adapter.SearchAdapter;
import com.gykj.cashier.module.storage.entity.GoodsNameEntity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc   : 商品查询Dialog
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/1610:53
 * version: 1.0
 */
public class  SearchDialog extends Dialog implements BaseQuickAdapter.OnItemChildClickListener,DialogInterface.OnDismissListener {

    @BindView(R.id.dial_recyclerview)
    RecyclerView mDialRecyclerView;

    SearchAdapter mAdapter;
    OnCheckListener mOnCheckListener;

    private Context mContext;

    /**
     * 点击商品选中的位置
     */
    private int mSelectPosition = -1;


    private List<GoodsNameEntity> mGoodsList = new ArrayList<>();


    public SearchDialog(@NonNull Context context) {
        super(context,R.style.alert_dialog);
        this.mContext = context;
        setParams();
        initRecyclerViewUI();
        setOnDismissListener(this);
    }

    private void initRecyclerViewUI() {
        if(null == mAdapter){
            mAdapter = new SearchAdapter(mContext,mGoodsList);
        }
        mAdapter.setOnItemChildClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,OrientationHelper.VERTICAL,false);
        mDialRecyclerView.setLayoutManager(layoutManager);
        mDialRecyclerView.setAdapter(mAdapter);
    }

    private void setParams() {
        setContentView(R.layout.dialog_search_layout);
        Window window = getWindow();
        ButterKnife.bind(this);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);
    }



    @OnClick({R.id.dial_cancle_tv,R.id.dial_certain_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.dial_cancle_tv:
                dismiss();
                break;
            case R.id.dial_certain_tv:
                if(mSelectPosition < 0){
                    ToastUtils.showToast(ToastType.WARNING,mContext.getResources().getString(R.string.please_select_goods));
                    return;
                }
                mOnCheckListener.checkPosition(mSelectPosition);
                dismiss();
                break;
        }
    }

    public void setmGoodsList(List<GoodsNameEntity> mGoodsList) {
        this.mGoodsList = mGoodsList;
        mAdapter.setNewData(mGoodsList);
    }


    public void setOnCheckListener(OnCheckListener listener){
        this.mOnCheckListener = listener;
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        mSelectPosition = position;
        for(int i = 0;i<mGoodsList.size();i++){
            if(i == position){
                mGoodsList.get(i).setCheck(true);
            }else {
                mGoodsList.get(i).setCheck(false);
            }
        }
        mAdapter.setNewData(mGoodsList);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mSelectPosition = -1;
    }
}
