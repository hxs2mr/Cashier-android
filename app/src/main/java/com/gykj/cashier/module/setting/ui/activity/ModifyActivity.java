package com.gykj.cashier.module.setting.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gykj.cashier.R;
import com.gykj.cashier.module.setting.iview.IModifyView;
import com.gykj.cashier.module.setting.presenter.ModifyPresenter;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 修改密码界面
 * <p>
 * author: josh.lu
 * created: 28/8/18 上午11:21
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class ModifyActivity extends BaseActivity implements IModifyView{

    @BindView(R.id.header) HeaderLayoutView header;
    @BindView(R.id.modify_origin_pass_et) EditText mModifyOriginPassEt;
    @BindView(R.id.modify_new_pass_et) EditText mModifyNewPassEt;


    ModifyPresenter presenter;

    @Override
    public int initContentView() {
        return R.layout.activity_modify;
    }

    @Override
    public void initData() {
        presenter = new ModifyPresenter();
        presenter.attachView(this);
    }

    @Override
    public void initUi() {
        header.setLeftImage(R.mipmap.icon_back);
        header.setTitle(getString(R.string.title_modify_password));
        header.setRightVisible(View.INVISIBLE);
    }

    @OnClick({R.id.modify_sure_tv,R.id.modify_cancle_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.modify_sure_tv:
                updatePassword();
                break;
            case R.id.modify_cancle_tv:
                finish();
                break;
        }
    }

    @Override
    public void updatePassword() {
        String oldPass = mModifyOriginPassEt.getText().toString().trim();
        String newPass = mModifyNewPassEt.getText().toString().trim();
        if(TextUtils.isEmpty(oldPass)){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_old_password));
            return;
        }
        if(TextUtils.isEmpty(newPass)){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.please_input_new_password));
            return;
        }
        presenter.updatePass(oldPass,newPass);
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
