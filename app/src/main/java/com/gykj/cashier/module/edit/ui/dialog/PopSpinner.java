package com.gykj.cashier.module.edit.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.gykj.cashier.R;
import com.gykj.cashier.module.edit.ui.adapter.SpinnerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 22/8/18 上午9:36
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class PopSpinner extends PopupWindow implements AdapterView.OnItemClickListener {

    @BindView(R.id.listview)
    ListView mListview;

    private Context mContext;
    private SpinnerAdapter mAdapter;

    private PopSpinner.OnItemClickListener mOnItemClickListener;

    public PopSpinner(Context context) {
        super(context);
        this.mContext = context;
        init();
        mListview.setOnItemClickListener(this);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setAdapter(SpinnerAdapter adapter) {
        this.mAdapter = adapter;
        mListview.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        dismiss();
        if (null != mOnItemClickListener)
            mOnItemClickListener.onClick((int) l);
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_spinner, null);
        setContentView(view);

        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);

        ButterKnife.bind(this, view);
    }

    public interface OnItemClickListener {
        void onClick(int i);
    }
}
