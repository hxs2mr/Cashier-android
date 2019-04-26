package com.wrs.gykjewm.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;

import com.lanzhu.autolayout.AutoLayoutActivity;
import com.wrs.gykjewm.baselibrary.R;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.utils.OthersUtils;


import butterknife.ButterKnife;

/**
 * description: activity基类
 * <p>
 * author: josh.lu
 * created: 11/6/18 下午12:46
 * email:  1113799552@qq.com
 * version: v1.0
 */
public abstract class BaseActivity extends AutoLayoutActivity implements IBaseView{

    protected Context mContext;

    private MaterialDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                    WindowManager.LayoutParams. FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(initContentView());
        mContext = this.getContext();
        initInjector();
        initData();
        initUi();
        ActivityStackManager.getManager().push(this);
        OthersUtils.hideKeyboard(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getManager().remove(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 设置View
     *
     * @return
     */
    public abstract int initContentView();

    /**
     * 注入Injector
     */
    public void initInjector() {
        ButterKnife.bind(this);
    }

    ;

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化UI
     */
    public abstract void initUi();



    @Override
    public void showProgressDialog(String msg) {
//        if (hasWindowFocus()) {
//
//        }
        dialog = new MaterialDialog.Builder(this)
                .backgroundColor(getResources().getColor(R.color.color_ffffff))
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .canceledOnTouchOutside(false)
                .contentColor(getResources().getColor(R.color.color_09c491))
                .content(msg)
                .show();
    }

    @Override
    public void closeProgressDialog() {
        if(null != dialog && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void showLoading(int visibility) {

    }

    @Override
    public void showLoadingError(int errorType) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onRefresh(boolean bRefresh) {

    }

    @Override
    public void showActivity(Activity activity, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(activity, cls);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }

    @Override
    public void showActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void showActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void skipActivity(Activity activity, Class<?> cls, Bundle bundle) {
        showActivity(activity, cls, bundle);
        activity.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void skipActivity(Activity activity, Class<?> cls) {
        showActivity(activity, cls);
        activity.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void skipActivity(Activity activity, Intent intent) {
        showActivity(activity, intent);
        activity.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void showActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

}
