package com.gykj.cashier.module.cashier.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gykj.cashier.R;
import com.gykj.zkfveins.manager.ZKFVManager;
import com.lanzhu.baidutts.manager.BaiduTtsManager;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.manager.FvRealmManager;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.module.fingervein.FingerVeinCaptureListener;
import com.zkteco.android.biometric.module.fingervein.FingerVeinService;
import com.zkteco.android.biometric.module.fingervein.exception.FingerVeinException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * desc   : 指静脉识别界面
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/10/1618:22
 * version: 1.0
 */
public class FvRecognizeActivity extends BaseActivity implements FingerVeinCaptureListener {


    @BindView(R.id.fv_close_iv) ImageView mDFvCloseIv;
    @BindView(R.id.fv_finger_iv) ImageView mFvFingerIv;
    @BindView(R.id.fv_data_tv) TextView mFvResultTv;
    @BindView(R.id.fv_finger_frame_iv) ImageView mFvFingerFrameIv;


    private static final int RECOGNIZE_SUCCESS = 100;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RECOGNIZE_SUCCESS:
                    Intent intent = new Intent();
                    intent.putExtra(Constant.DATA,(String) msg.obj);
                    setResult(101, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                    finish();
                    break;
            }
        }
    };

    @Override
    public int initContentView() {
        return R.layout.activity_fv_recognize_layout;
    }

    @Override
    public void initData() {
        try {
            ZKFVManager.getZKFVManager().startFingerVeinCheck(this);
        } catch (FingerVeinException e) {
            e.printStackTrace();
        }
        FvRealmManager.getManager().loadFvData2Memery();
        BaiduTtsManager.getManager().speak("请将手指放 至感应区");
    }

    @Override
    public void initUi() {
        startAnimate1();
    }

    @OnClick({R.id.fv_close_iv})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fv_close_iv:
                finish();
                break;
        }
    }

    @Override
    public void captureOK(final byte[] bytes, byte[] bytes1) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(null != bytes)
                {
                    Bitmap bitmapFp = ToolUtils.renderCroppedGreyScaleBitmap(bytes, ZKFVManager.getZKFVManager().getFingerVeinSensor().getFpImgWidth(), ZKFVManager.getZKFVManager().getFingerVeinSensor().getFpImgHeight());
                    mFvFingerFrameIv.setImageBitmap(bitmapFp);
                    mFvFingerIv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void captureError(FingerVeinException e) {

    }

    @Override
    public void extractOK(byte[] fpTemplate, String fvTemplate) {
        identifyFp(fpTemplate,fvTemplate);
    }

    @Override
    public void extractError(int i) {

    }


    private void identifyFp(final byte[] fpTemplate, final String fvTemplate){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                byte[] idsfp = new byte[1024];
                String strLog = "";
                int code = FingerVeinService.identifyFinger(fpTemplate, idsfp, 1);
                if (code > 0 ){
                    String strRes[] = new String(idsfp).split("\t");
                    strLog += "识别成功,相似度=" + strRes[1];
                    if(null != strRes[0]){
                        Message msg = Message.obtain();
                        msg.what = RECOGNIZE_SUCCESS;
                        msg.obj = strRes[0];
                        mHandler.sendMessage(msg);
                    }
                    else {
                        BaiduTtsManager.getManager().speak("未找到用户信息");
                        strLog += "未找到用户信息";
                    }
                } else {
                    BaiduTtsManager.getManager().speak("未找到用户信息");
                    strLog += "识别失败";
                }
                mFvResultTv.setText(strLog);
                initBackground();
            }
        });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZKFVManager.getZKFVManager().stopFingerVeinCheck();
    }
}
