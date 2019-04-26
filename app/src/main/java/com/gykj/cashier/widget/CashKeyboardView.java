package com.gykj.cashier.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.ui.adapter.CashKeyAdapter;
import com.lanzhu.autolayout.AutoLayoutInfo;
import com.lanzhu.autolayout.AutoRelativeLayout;
import com.lanzhu.autolayout.utils.AutoUtils;

import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 23/8/18 上午10:02
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class CashKeyboardView extends AutoRelativeLayout {

    private Context mContext;
    private RecyclerView recyclerView;
    private TextView mKey20Tv;
    private TextView mKey50Tv;
    private TextView mKey100Tv;
    private TextView mKey200Tv;

    private RelativeLayout mKeyClearLayout;
    private TextView mKeySureTv;


    private LayoutInflater mInflater;
    private String[] arrays = new String[]{"1","2","3","4","5","6","7","8","9","00","0","."};

    CashKeyAdapter adapter;

    BaseQuickAdapter.OnItemChildClickListener mOnItemChildClickListener;
    OnClickListener mOnClickListener;


    public CashKeyboardView(Context context) {
        this(context,null);
    }

    public CashKeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CashKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.layout_cash_keyboard, this);
        AutoUtils.auto(view);
        initView(view);
        initAdapter();
    }

    private void initListener() {
        mKey20Tv.setOnClickListener(mOnClickListener);
        mKey50Tv.setOnClickListener(mOnClickListener);
        mKey100Tv.setOnClickListener(mOnClickListener);
        mKey200Tv.setOnClickListener(mOnClickListener);
        mKeyClearLayout.setOnClickListener(mOnClickListener);
        mKeySureTv.setOnClickListener(mOnClickListener);
    }

    private void initAdapter() {
        if(null == adapter){
            adapter = new CashKeyAdapter(Arrays.asList(arrays));
        }
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.cash_recyclerview);
        mKey20Tv = view.findViewById(R.id.key_20_tv);
        mKey50Tv = view.findViewById(R.id.key_50_tv);
        mKey100Tv = view.findViewById(R.id.key_100_tv);
        mKey200Tv = view.findViewById(R.id.key_200_tv);
        mKeyClearLayout = view.findViewById(R.id.key_clear_tv);
        mKeySureTv = view.findViewById(R.id.key_sure_tv);
    }


    public void setmOnClickListener(OnClickListener listener){
        this.mOnClickListener = listener;
        initListener();
    }

    public void setmOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener listener){
        this.mOnItemChildClickListener = listener;
        adapter.setOnItemChildClickListener(mOnItemChildClickListener);
    }

    public List<String> getKeyCodeList(){
        return Arrays.asList(arrays);
    }

}
