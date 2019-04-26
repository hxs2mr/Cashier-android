package com.gykj.cashier.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gykj.cashier.R;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;


/**
 * description:
 * <p>
 * author: josh.lu
 * created: 21/7/18 下午10:36
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class HeaderLayoutView extends RelativeLayout {

    private BaseActivity mContext;
    private LayoutInflater mInflater;
    private TextView mTitleTv;
    private ImageView mLeftIv;
    private TextView mRightTv;
    private ImageView mRightBtn;
    private View view;

    public HeaderLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HeaderLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = (BaseActivity) context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.common_titlebar, this);
        mRightTv = (TextView) view.findViewById(R.id.right);
        mLeftIv = (ImageView) view.findViewById(R.id.left);
        mLeftIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mContext.onBackPressed();
            }
        });

        mTitleTv = (TextView) view.findViewById(R.id.left_title_tv);
        mRightBtn = (ImageView) view.findViewById(R.id.title_right_iv);

    }

    public HeaderLayoutView(Context context) {
        super(context);
    }


    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        mTitleTv.setVisibility(View.VISIBLE);
        mTitleTv.setText(title);
    }

    /**
     * 设置左边返回键是否可见
     =	 */
    public void setLeftVisible(int visible){
        mLeftIv.setVisibility(visible);
    }

    /**
     * 设置右边返回键是否可见
     =	 */
    public void setRightVisible(int visible){
        mRightBtn.setVisibility(visible);
        mRightTv.setVisibility(visible);
    }

    /**
     *  设置左边图片
     * @param res
     */
    public void setLeftImage(int res){
        mLeftIv.setVisibility(VISIBLE);
        mLeftIv.setImageResource(res);
    }


    /**
     * 设置右边标题
     * @param title
     */
    public void setRightTitle(String title){
        mRightTv.setVisibility(View.VISIBLE);
        mRightTv.setText(title);
    }

    public void setRightImage(int res){
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setImageResource(res);
    }


    public void setRightTvOnClickListener(OnClickListener onClickListener){
        mRightTv.setOnClickListener(onClickListener);
    }


    public void setRightBtnOnClickListener(OnClickListener onClickListener){
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setOnClickListener(onClickListener);
    }

}