package com.gykj.cashier.module.cashier.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gykj.cashier.R;
import com.gykj.jxfvlibrary.listener.OnRecognizeListener;
import com.gykj.jxfvlibrary.manager.JXFvManager;
import com.gykj.jxfvlibrary.listener.OnJxFvListener;
import com.lanzhu.baidutts.manager.BaiduTtsManager;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * desc   : 静芯指静脉识别界面
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2019/1/99:02
 * version: 1.0
 */

public class JxFvActivity extends BaseActivity implements OnJxFvListener,OnRecognizeListener{

    @BindView(R.id.fv_close_iv) ImageView mFvCloseIv;
    @BindView(R.id.fv_finger_iv) ImageView mFvFingerIv;
    @BindView(R.id.fv_data_tv) TextView mFvResultTv;
    @BindView(R.id.fv_finger_frame_iv) ImageView mFvFingerFrameIv;


    private static final int RECOGNIZE_SUCCESS = 100;

     private  MyHandler    mHandler = new MyHandler(this);
     static  class MyHandler extends Handler//声明为静态类
    {
        private final WeakReference<JxFvActivity> mActivtys;
        public MyHandler(JxFvActivity activity){
            mActivtys =new WeakReference<JxFvActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RECOGNIZE_SUCCESS:
                    final    JxFvActivity activity=mActivtys.get();
                    if(activity!=null)
                    {
                        Intent intent = new Intent();
                        intent.putExtra(Constant.DATA,(String) msg.obj);
                        activity.setResult(101, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                        activity.finish();
                    }

                    break;
            }
        }
    }


    @Override
    public int initContentView() {
        return R.layout.activity_jxfv;
    }

    @Override
    public void initData() {
        BaiduTtsManager.getManager().speak("请将手指放 至感应区");
        startAnimate1();
    }

    @Override
    public void initUi() {
        initBackground();
        initJxFvData();
    }

    private void startAnimate1(){
        //      imageView中凡是有get，set方法的属性，都可以通过属性动画操作
        //      创建属性动画对象，并设置移动的方向和偏移量
        //      translationX是imageview的平移属性
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mFvFingerIv, "translationY", 0f, 200f);
        //      设置移动时间
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(-1);
        //      开始动画
        objectAnimator.start();
    }

    public void initBackground(){
        mFvResultTv.setText("");
        mFvFingerIv.setVisibility(View.VISIBLE);
        mFvFingerFrameIv.setImageResource(R.mipmap.icon_finger_frame);
    }

    private void initJxFvData() {
        JXFvManager.getInstance().iswhile=true;
      //  JXFvManager.getInstance().initJXFVData();
        JXFvManager.getInstance().setOnJxFvListener(this);
        JXFvManager.getInstance().jxInitUSBDriver();
    }
    @OnClick({R.id.fv_close_iv})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fv_close_iv:
                finish();
                break;
        }
    }

    /**
     * 指纹采集的回调  设备是否存在 初始化设备  链接成功  开始采集  获取特征值 进行数据库匹配
     */
    @Override
    public void success () {
        try {
            switch (JXFvManager.getInstance().getJxType()){
                case TOUCHED:
                    JXFvManager.getInstance().jxInitCapEnv();
                    break;
                case COLLECTED:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JXFvManager.getInstance().jxLoadVeinSample();
                                } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case LOAD:
                    JXFvManager.getInstance().jxGrabVeinFeature();
                    break;
                case GRAB:
                    JXFvManager.getInstance().jxRecognizeVeinFeatureInGroup(this);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(final String error) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                JXFvManager.getInstance().setIsFingerTouched(true);
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        JXFvManager.getInstance().jxDeInitUSBDriver();
    }

    @Override
    public void recognizeSuccess(String viens) {
        if(null == viens || TextUtils.isEmpty(viens)){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    BaiduTtsManager.getManager().speak("未找到用户信息,请更换手指");
                }
            });
            //2秒以后在重新检测
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    JXFvManager.getInstance().setIsFingerTouched(true);
                }
            },2000);
        }else {
            Message msg = Message.obtain();
            msg.what = RECOGNIZE_SUCCESS;
            msg.obj = viens;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void recognizeFailed(final String error) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                JXFvManager.getInstance().setIsFingerTouched(true);
            }
        },500);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (error){
//                    case "样本格式不正确":
//                        ToastUtils.showToast(ToastType.WARNING,error);
//                        break;
                    case "数据库忙碌":
                        ToastUtils.showToast(ToastType.WARNING,error);
                        break;
                    case "USB权限错误":
                        ToastUtils.showToast(ToastType.WARNING,error);
                        break;
                    case "设备未授权":
                        ToastUtils.showToast(ToastType.WARNING,error);
                        break;
                    case "未检测到指静脉设备":
                        ToastUtils.showToast(ToastType.WARNING,error);
                        break;
                }
            }
        });
    }
}
