package com.gykj.cashier.module.login.ui.activity;


import android.content.Intent;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;



import com.gykj.cashier.module.cashier.iview.OnSelectListener;
import com.gykj.cashier.R;
import com.gykj.cashier.module.index.ui.activity.MainActivity;
import com.gykj.cashier.module.login.entity.DeviceEntity;

import com.gykj.cashier.module.login.iview.ILoginView;
import com.gykj.cashier.module.login.presenter.LoginPresenter;
import com.gykj.cashier.module.login.ui.dialog.DeviceDialog;


import com.gykj.cashier.module.login.ui.service.UpdateService;
import com.gykj.jxfvlibrary.manager.JXFvManager;
import com.gykj.jxfvlibrary.listener.OnJxFvListener;
import com.gykj.jxfvlibrary.manager.ThreadManager;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.base.BaseApplication;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.iface.IRealmListener;
import com.wrs.gykjewm.baselibrary.manager.CashManager;
import com.wrs.gykjewm.baselibrary.manager.SchoolRealmManager;
import com.wrs.gykjewm.baselibrary.realm.FaceRealm;
import com.wrs.gykjewm.baselibrary.realm.SchoolRealm;
import com.wrs.gykjewm.baselibrary.utils.AppUtils;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;


import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * description: 登陆界面
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午12:48
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class LoginActivity extends BaseActivity implements ILoginView ,OnSelectListener{

    @BindView(R.id.login_username_et)
    EditText mLoginUsernameEt;

    @BindView(R.id.login_password_et)
    EditText mLoginPasswordEt;

    @BindView(R.id.login_version_tv)
    TextView mLoginVersionTv;

    LoginPresenter presenter;

    DeviceDialog deviceDialog;

    boolean success = false;
    boolean delete = false;

    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        presenter = new LoginPresenter();
        presenter.attachView(this);
        showAccount();
        startService(new Intent(this,UpdateService.class));
    }
    @Override
    public void initUi() {
        showAppVersion();
        deviceDialog = new DeviceDialog(this);
        deviceDialog.setOnSelectListener(this);
    }

    @OnClick({R.id.login_login_tv,R.id.login_company_logo})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.login_login_tv:
                login();
                break;
            case R.id.login_company_logo:
                break;
        }
    }



    @Override
    public void login() {
        String username = mLoginUsernameEt.getEditableText().toString().trim();
        String password = mLoginPasswordEt.getEditableText().toString().trim();
        if(TextUtils.isEmpty(username)){
            ToastUtils.showToast(ToastType.WARNING,R.string.login_hint_username);
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtils.showToast(ToastType.WARNING,R.string.login_hint_password);
            return;
        }
        presenter.login(username,password);
    }

    @Override
    public void showAppVersion() {
        mLoginVersionTv.setText(getString(R.string.app_version_code)+ AppUtils.getPackageInfo(this).versionName);
    }

    @Override
    public void intoMainActivity() {
        showActivity(this, MainActivity.class);
    }

    @Override
    public void showDevcieDialog() {
        deviceDialog.setDeviceList(presenter.getDeviceList());
        deviceDialog.show();
    }

    @Override
    public void showAccount() {
        if(null != CashManager.getCashApi().getAccount()){
            mLoginUsernameEt.setText(CashManager.getCashApi().getAccount().getAccount());
            mLoginPasswordEt.setText(CashManager.getCashApi().getAccount().getPass());
        }else {
            mLoginUsernameEt.setText("");
            mLoginPasswordEt.setText("");
        }
    }

    @Override
    public boolean initRealmData(final long school_id) {
        //初始化静芯指静脉
        JXFvManager.getInstance().jxInitVeinDatabase(BaseApplication.getInstance().getFACE_DB_PATH() + "fv", new OnJxFvListener() {
            @Override
            public void success() {
                //初始化数据库分组信息
                byte[] group1_byte = new byte[20];
                System.arraycopy(String.valueOf(school_id).getBytes(),0,group1_byte,0,String.valueOf(school_id).getBytes().length);
                JXFvManager.getInstance().setGroup1Byte(group1_byte);
            }
            @Override
            public void failed(String error) {
                Log.d("yxxs","创建指静脉数据库失败"+error);
            }
        });
        success = false;
        RealmResults<SchoolRealm> all = Realm.getDefaultInstance().where(SchoolRealm.class).findAll();
        if(null == all || all.size() == 0){
            //初始化学校数据库
            //删除学校相关的人脸指纹数据库
            if(deleteFaceAndFvRealm(school_id)){
                SchoolRealmManager. getManager().addSchoolRealm(school_id, new IRealmListener() {
                    @Override
                    public void OnSuccess() {
                        success = true;
                    }
                    @Override
                    public void onError(Throwable error) {
                        success = false;
                        ToastUtils.showToast(ToastType.EEEOR,"添加学校失败"+error.getMessage());
                    }
                });
            }
        }else {
            if(all.get(0).getSchool_id() != school_id){
                CashManager.getCashApi().clearDeviceId();
                //删除学校相关的人脸 指纹数据库
                if(deleteFaceAndFvRealm(school_id)){
                    SchoolRealmManager.getManager().updateSchoolToRealm(school_id, new IRealmListener() {
                        @Override
                        public void OnSuccess() {
                            success = true;
                        }

                        @Override
                        public void onError(Throwable error) {
                            success = false;
                            ToastUtils.showToast(ToastType.EEEOR,"更新学校失败"+error.getMessage());
                        }
                    });
                }
            }else {
                success = true;
            }
        }
        return success;
    }

    @Override
    public boolean deleteFaceAndFvRealm(final long school_id) {
        delete = false;
        final RealmResults<FaceRealm> faceAll = Realm.getDefaultInstance().where(FaceRealm.class).findAll();
        if(null != faceAll && faceAll.size()>0){
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    faceAll.deleteAllFromRealm();
                    delete = true;
                }
            });
        }else {
            delete = true;
        }
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                //初始化数据库分组信息
                byte[] group1_byte = new byte[20];
                System.arraycopy(String.valueOf(school_id).getBytes(),0,group1_byte,0,String.valueOf(school_id).getBytes().length);
                JXFvManager.getInstance().jxRemoveGroupVeinFeature(new OnJxFvListener() {
                    @Override
                    public void success() {
                        Log.d("yxxs","清除指静脉数据成功");
                    }

                    @Override
                    public void failed(String error) {
                        Log.d("yxxs",error);
                    }
                });
            }
        });
        return delete;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        showAccount();
    }

    @Override
    public void select(DeviceEntity deviceBean) {
        CashManager.getCashApi().saveDeviceId(String.valueOf(deviceBean.getDevice_id()));
        CashManager.getCashApi().saveDeviceName(deviceBean.getDevice_name());
        presenter.getToken(String.valueOf(deviceBean.getDevice_id()));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != presenter){
            presenter.cancel();
            presenter.detachView();
        }
    }

}
