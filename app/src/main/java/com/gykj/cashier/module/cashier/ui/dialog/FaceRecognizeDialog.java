package com.gykj.cashier.module.cashier.ui.dialog;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.gykj.acface.customview.FaceRectView;
import com.gykj.acface.model.DrawInfo;
import com.gykj.acface.util.CameraHelper;
import com.gykj.acface.util.CameraListener;
import com.gykj.acface.util.DrawHelper;
import com.gykj.cashier.R;
import com.gykj.cashier.module.cashier.iview.IRecognizeListener;

import com.lanzhu.baidutts.manager.BaiduTtsManager;
import com.luck.picture.lib.tools.ScreenUtils;
import com.wrs.gykjewm.baselibrary.domain.ToastType;

import com.wrs.gykjewm.baselibrary.realm.FaceRealm;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * description:
 * <p>刷脸  fragment
 * author: josh.lu
 * created: 16/8/18 下午12:59
 * email:  1113799552@qq.com
 * version: v1.0
 */
@SuppressLint("ValidFragment")
public class FaceRecognizeDialog extends DialogFragment implements CameraListener {

    private static final String TAG = FaceRecognizeDialog.class.getSimpleName();

    private TextureView textureViewPreview;
    private FaceRectView faceRectView;
    private ImageView mDialogCloseIv;
    private ImageView mDialogCircleIv;

    CameraHelper cameraHelper;
    DrawHelper drawHelper;
    Camera.Size previewSize;
    Integer cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
    FaceEngine faceEngine;
    int afCode = -1;
    int processMask =  FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS;


    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * 所需的所有权限信息
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };

    IRecognizeListener recognizeListener;
    private RealmResults<FaceRealm> mFaceList;
    private boolean isRecognize = false;

    private final static int RECOGNIZED = 100;
    private final static int HAS_MORE_PEOPLE = 101;

    private int people = 0;
    private boolean singlePeople;


    /**
     * 未找到人脸计数器
     */
    private int mCount = 0;
    /**
     * 关闭框计数器
     */
    private int close_count = 0;

    /**
     * 活体检测计数器
     */
    private int alive_count = 0;

    private int close_live_count = 0;


    private boolean isResume = false;

    /**
     * 判断中心点人数
     */
    private int center_people = 0;


    private int mfaceRectViewWidth;
    private int mfaceRectViewHeight;



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RECOGNIZED:
                    close_count++;
                    if(close_count >= 3){
                        ToastUtils.showToast(ToastType.WARNING,"未找到用户信息");
                        BaiduTtsManager.getManager().speak("未找到用户信息");
                        dismiss();
                    }
                    break;
                case HAS_MORE_PEOPLE:
                    people ++;
                    if(people >=20){
                        if(!singlePeople){
                            //此时有两人站在中心点以上，不能进行人脸支付，确保只有一人站在识别框内
                            BaiduTtsManager.getManager().speak("请不要多人在识框内");
                            ToastUtils.showToast(ToastType.WARNING,"请不要多人在识框内");
                        }
                        people = 0;
                    }
                    break;
            }
        }
    };

    private Timer mTimer;
    private TimerTask mTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 设置背景透明
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // 去掉标题 死恶心死恶心的
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set cancel on touch outside
        getDialog().setCanceledOnTouchOutside(false);
        View rootView = inflater.inflate(R.layout.dialog_face_login_layout, container,false);
        initView(rootView);
        initData();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = ScreenUtils.dip2px(getActivity(),854);
        //params.width = ScreenUtils.dip2px(getActivity(),640);
        params.height = ScreenUtils.dip2px(getActivity(),480);
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initData() {
        isRecognize = false;
        isResume = false;
        mFaceList = Realm.getDefaultInstance().where(FaceRealm.class).findAll();
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initEngine();
            initCamera();
        }
        BaiduTtsManager.getManager().speak("请进入识别框内识别");
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
        if(mTask!=null)
        {
            mTask.cancel();
            mTask=null;
        }
        initTimer();
    }

    private void initTimer() {
        if(null == mTimer){
            mTimer = new Timer();
        }
        if(null == mTask){
            mTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mFaceList  = Realm.getDefaultInstance().where(FaceRealm.class).findAll();
                        }
                    });
                }
            };
        }
        mTimer.schedule(mTask,0,3000);
    }

    private void initView(View view) {
        textureViewPreview = view.findViewById(R.id.textureview_preview);
        faceRectView = view.findViewById(R.id.facerect_view);
        mDialogCloseIv = view.findViewById(R.id.dialog_close_iv);
        mDialogCircleIv = view.findViewById(R.id.dialog_circle_iv);
        startRotation();
        mDialogCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        faceRectView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mfaceRectViewWidth = faceRectView.getWidth();
                mfaceRectViewHeight = faceRectView.getHeight();
                Log.d("lanzhu","mfaceRectViewWidth="+mfaceRectViewWidth);
            }
        });
        mDialogCircleIv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });
    }

    private void initEngine() {
        faceEngine = new FaceEngine();
        afCode = faceEngine.init(getActivity(), FaceEngine.ASF_DETECT_MODE_VIDEO, FaceEngine.ASF_OP_0_HIGHER_EXT,
                16, 20, FaceEngine.ASF_AGE | FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS | FaceEngine.ASF_FACE_RECOGNITION);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Log.i(TAG, "initEngine:  init: " + afCode + "  version:" + versionInfo);
    }

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        cameraHelper = new CameraHelper.Builder()
                .metrics(metrics)
                .rotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(cameraID != null ? cameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(textureViewPreview)
                .cameraListener(this)
                .build();
        cameraHelper.init();
    }

    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(getActivity(), neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                initEngine();
                initCamera();
                if (cameraHelper != null) {
                    cameraHelper.start();
                }
            } else {
                ToastUtils.showToast(ToastType.WARNING, "请打开相机权限");
            }
        }
    }

    public void startRotation() {
        //设置旋转的样式
        ObjectAnimator animtorAlpha = ObjectAnimator.ofFloat(mDialogCircleIv, "rotation", 0f, 360f);
        //旋转不停顿
        animtorAlpha.setInterpolator(new AccelerateDecelerateInterpolator());
        //设置动画重复次数
        animtorAlpha.setRepeatCount(-1);
        //旋转时长
        animtorAlpha.setDuration(2000);
        //开始旋转
        animtorAlpha.start();
    }


    public void setIRecognizeListener(IRecognizeListener listener){
        this.recognizeListener = listener;
    }

    @Override
    public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
        Log.i(TAG, "onCameraOpened: " + cameraId + "  " + displayOrientation + " " + isMirror);
        previewSize = camera.getParameters().getPreviewSize();
        drawHelper = new DrawHelper(previewSize.width, previewSize.height, textureViewPreview.getWidth(), textureViewPreview.getHeight(), displayOrientation
                , cameraId, true);
    }

    //视频预览帧
    @Override
    public void onPreview(byte[] data, Camera camera) {
        if(!isResume){
            return;
        }
        if (faceRectView != null) {
            faceRectView.clearFaceInfo();
        }
        List<FaceInfo> faceInfoList = new ArrayList<>();

        //图像数据预览数据处理.detectFaces
        int code = faceEngine.detectFaces(data, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList);

        //进行人脸信息监测.process
        if (code == ErrorInfo.MOK) {
            code = faceEngine.process(data, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList, processMask);
            if (code != ErrorInfo.MOK) {
                return;
            }
        }
        for (int i = 0; i < faceInfoList.size(); i++) {
            Log.i(TAG, "onPreview:  face[" + i + "] = " + faceInfoList.get(i).toString());
        }
        List<AgeInfo> ageInfoList = new ArrayList<>();
        List<GenderInfo> genderInfoList = new ArrayList<>();
        List<Face3DAngle> face3DAngleList = new ArrayList<>();
        List<LivenessInfo> faceLivenessInfoList = new ArrayList<>();

        //接口，获取检测的人脸信息
        int ageCode = faceEngine.getAge(ageInfoList);
        int genderCode = faceEngine.getGender(genderInfoList);
        int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
        int livenessCode = faceEngine.getLiveness(faceLivenessInfoList);

        /**
         * 有其中一个的错误码不为0，return
         */
        if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
            return;
        }
        if (faceRectView != null && drawHelper != null) {
            List<DrawInfo> drawInfoList = new ArrayList<>();
            for (int i = 0; i < faceInfoList.size(); i++) {
                    //添加识别出来脸角度 年龄等
              drawInfoList.add(new DrawInfo(faceInfoList.get(i).getRect(), genderInfoList.get(i).getGender(), ageInfoList.get(i).getAge(), faceLivenessInfoList.get(i).getLiveness()));
            }

            drawHelper.draw(faceRectView, drawInfoList);
            if(isRecognize){
                return;
            }
            center_people = 0;
            for(int i = 0;i<faceInfoList.size();i++){

                if(checkIsCenter(faceInfoList.get(i).getRect())){
                    center_people++;
                }
            }
            if(center_people > 1){
                singlePeople = false;
                mHandler.sendEmptyMessage(HAS_MORE_PEOPLE);
                return;
            }
            singlePeople = true;
            for(int i = 0; i < faceInfoList.size(); i ++){
                if(checkIsCenter(faceInfoList.get(i).getRect())){
                    //检查是否是真人还是图片
//                    if(faceLivenessInfoList.get(i).getLiveness() != LivenessInfo.ALIVE){
//                        alive_count ++;
//                        if(alive_count == 30){
//                            ToastUtils.showToast(ToastType.WARNING,"请用真实人脸进行支付");
//                            alive_count = 0;
//                            close_live_count ++;
//                            if(close_live_count == 3){
//                                dismiss();
//                                close_live_count = 0;
//                            }
//                        }
//                        break;
//                    }
                    isRecognize = true;
                    FaceFeature faceFeature = new FaceFeature();
                    FaceFeature face = new FaceFeature();

                    //  进行人脸特征提取.extractFaceFeature
                    int res = faceEngine.extractFaceFeature(data, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList.get(i), faceFeature);
                    if (res == 0) {
                        FaceSimilar faceSimilar = new FaceSimilar();
                        long start = System.currentTimeMillis();
                        int number = 0;
                        for(int j = 0; j< mFaceList.size(); j++) {
                            face.setFeatureData(mFaceList.get(j).getFeatures());
                            // 进行人脸比对.compareFaceFeature
                            number++;
                            int compareResult = faceEngine.compareFaceFeature(face, faceFeature, faceSimilar);
                            System.out.println("=======相似度====" + faceSimilar.getScore());
                            if (faceSimilar.getScore() >= 0.9f) {
                                System.out.println("=======用户id====" + mFaceList.get(j).getUser_id());
                                //人脸识别成功 返回接口
                                recognizeListener.recognize(mFaceList.get(j).getUser_id(), mFaceList.get(j).getUserType());
                                dismiss();
                                break;
                            }
                            long end = System.currentTimeMillis();
                            System.out.println("=======识别时间====" + (end - start));
                        }
                        System.out.println("=======对比次数====" + number);
                        mCount++;
                        isRecognize = false;
                        if(mCount == 5){
                            mCount = 0;
                            mHandler.sendEmptyMessage(RECOGNIZED);
                        }
                    }
                }
            }

        }
    }

    /**
     * 判断是否中心点
     */
    public boolean checkIsCenter(Rect rect){
        float l = (mfaceRectViewWidth/2-130)*((float)previewSize.width/mfaceRectViewWidth);
        float t = (mfaceRectViewHeight/2-130)*((float)previewSize.height/mfaceRectViewHeight);
        float r = (mfaceRectViewWidth/2+130)*((float)previewSize.width/mfaceRectViewWidth);
        float b = (mfaceRectViewHeight/2+130)*((float)previewSize.height/mfaceRectViewHeight);
        if(rect.left >= l
                && rect.top >= t
                && rect.right <= r
                && rect.bottom <= b){
            return true;
        }
        return false;
    }

    @Override
    public void onCameraClosed() {
        Log.i(TAG, "onCameraClosed: ");
    }

    @Override
    public void onCameraError(Exception e) {
        Log.i(TAG, "onCameraError: " + e.getMessage());
    }

    @Override
    public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
        if (drawHelper != null) {
            drawHelper.setCameraDisplayOrientation(displayOrientation);
        }
        Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
    }


    private void unInitEngine() {
        if (afCode == 0) {
            afCode = faceEngine.unInit();
            Log.i(TAG, "unInitEngine: " + afCode);
        }
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }
        if(null != mTask){
            mTask.cancel();
            mTask = null;
        }
        if(null != mTimer){
            mTimer.cancel();
            mTimer = null;
        }
        unInitEngine();
        super.onDestroy();
    }
}
