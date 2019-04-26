package com.gykj.cashier.module.setting.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gykj.cashier.R;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.gykj.cashier.module.setting.iview.ISettingView;
import com.gykj.cashier.module.setting.presenter.SettingPresenter;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.manager.ActivityStackManager;
import com.wrs.gykjewm.baselibrary.manager.CashManager;
import com.wrs.gykjewm.baselibrary.utils.AppUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:设置界面
 * <p>
 * author: josh.lu
 * created: 28/8/18 上午9:45
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class SettingActivity extends BaseActivity implements ISettingView{

    @BindView(R.id.header) HeaderLayoutView header;
    @BindView(R.id.setting_account_tv) TextView mSettingAccountTv;
    @BindView(R.id.setting_about_tv) TextView mSettingAboutTv;
    @BindView(R.id.setting_account_layout) LinearLayout mSettingAccountLayout;
    @BindView(R.id.setting_about_layout) LinearLayout mSettingAboutLayout;

    @BindView(R.id.setting_version_tv) TextView mSettingVersionTv;
    @BindView(R.id.setting_account_name_tv) TextView mSettingAccountNameTv;



    SettingPresenter presenter;

    @Override
    public int initContentView() {
        return R.layout.activity_setting;
    }

    @Override
    public void initData() {
        presenter = new SettingPresenter();
        presenter.attachView(this);
    }

    @Override
    public void initUi() {
        header.setLeftImage(R.mipmap.icon_back);
        header.setTitle(getString(R.string.title_setting));
        header.setRightVisible(View.INVISIBLE);
        mSettingVersionTv.setText(getString(R.string.app_version)+ AppUtils.getPackageInfo(this).versionName);
        mSettingAccountNameTv.setText(getString(R.string.account_name)+ CashManager.getCashApi().getAccount().getAccount());
        showAccountInfo();
    }

    @OnClick({R.id.setting_account_tv,R.id.setting_about_tv,R.id.setting_logout_tv,R.id.setting_modify_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.setting_account_tv:
                showAccountInfo();
                break;
            case R.id.setting_about_tv:
                showAboutInfo();
                break;
            case R.id.setting_logout_tv:
                logout();
                break;
            case R.id.setting_modify_tv:
                showActivity(SettingActivity.this,ModifyActivity.class);
                break;
        }
    }

    @Override
    public void showAccountInfo() {
        mSettingAccountTv.setTextColor(getResources().getColor(R.color.color_ffffff));
        mSettingAccountTv.setBackgroundColor(getResources().getColor(R.color.color_09c491));
        mSettingAboutTv.setTextColor(getResources().getColor(R.color.color_0d0d0d));
        mSettingAboutTv.setBackgroundColor(getResources().getColor(R.color.color_f4f4f4));
        mSettingAccountLayout.setVisibility(View.VISIBLE);
        mSettingAboutLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAboutInfo() {
        mSettingAccountTv.setTextColor(getResources().getColor(R.color.color_0d0d0d));
        mSettingAccountTv.setBackgroundColor(getResources().getColor(R.color.color_f4f4f4));
        mSettingAboutTv.setTextColor(getResources().getColor(R.color.color_ffffff));
        mSettingAboutTv.setBackgroundColor(getResources().getColor(R.color.color_09c491));
        mSettingAccountLayout.setVisibility(View.INVISIBLE);
        mSettingAboutLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void logout() {
        presenter.logout();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter.isAttached()){
            presenter.cancel();
            presenter.detachView();
        }
    }
}
