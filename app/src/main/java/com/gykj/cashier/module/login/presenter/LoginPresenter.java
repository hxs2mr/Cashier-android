package com.gykj.cashier.module.login.presenter;


import com.gykj.cashier.R;
import com.gykj.cashier.module.login.data.LoginApi;
import com.gykj.cashier.module.login.entity.DeviceEntity;
import com.gykj.cashier.module.login.entity.LoginEntity;
import com.gykj.cashier.module.login.entity.TokenEntity;
import com.gykj.cashier.module.login.entity.UserInfo;
import com.gykj.cashier.module.login.iview.ILoginView;
import com.wrs.gykjewm.baselibrary.base.BasePresenter;
import com.wrs.gykjewm.baselibrary.domain.Account;
import com.wrs.gykjewm.baselibrary.domain.BaseResult;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.CashManager;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午12:48
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class LoginPresenter extends BasePresenter<ILoginView> {

    LoginApi api;


    private List<DeviceEntity> deviceList = new ArrayList<>();

    @Override
    public void attachView(ILoginView view) {
        super.attachView(view);
        api = LoginApi.getLoginApi();
    }


    public void login(final String mobilephone, final String password){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        Disposable disposable = api.login(mobilephone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginEntity>() {
                    @Override
                    public void accept(LoginEntity loginEntity) throws Exception {
                        if (null != loginEntity && loginEntity.getCode() == 0) {
                            mBaseView.closeProgressDialog();
                            Account account = new Account();
                            account.setAccount(mobilephone);
                            account.setPass(password);
                            CashManager.getCashApi().saveAccount(account);
                            CashManager.getCashApi().saveToken(loginEntity.getData().getToken());
                            if(mBaseView.initRealmData(loginEntity.getData().getSchoolId())){
                                if(null != CashManager.getCashApi().getDeviceId()){
                                    getToken(CashManager.getCashApi().getDeviceId());
                                }else {
                                    changeDeviceList(loginEntity.getDeviceList());
                                    mBaseView.showDevcieDialog();
                                }
                            }else {
                                ToastUtils.showToast(ToastType.WARNING,"正在同步人脸数据，请重新登录");
                            }
                        } else {
                            mBaseView.closeProgressDialog();
                            ToastUtils.showToast(ToastType.WARNING, loginEntity.getMsg());
                        }
                    }

                }, getErrorConsumer());

        addDisposable(disposable);
    }

    public void getToken(String deviceId){
        mBaseView.showProgressDialog(mContext.getString(R.string.loading));
        api.initRetrofit();
        Disposable subscribe = api.getToken(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<TokenEntity>>() {
                    @Override
                    public void accept(BaseResult<TokenEntity> tokenEntityBaseResult) throws Exception {
                        mBaseView.closeProgressDialog();
                        if(null != tokenEntityBaseResult && tokenEntityBaseResult.isSuccessed()){
                            CashManager.getCashApi().saveToken(tokenEntityBaseResult.getData().getToken());
                            mBaseView.intoMainActivity();
                        }else {
                            ToastUtils.showToast(ToastType.WARNING, tokenEntityBaseResult.getMsg());
                        }

                    }
                }, getErrorConsumer());
        addDisposable(subscribe);

    }


    private void changeDeviceList(List<LoginEntity.DeviceListBean> deviceList){
        this.deviceList.clear();
        for(int i = 0;i<deviceList.size();i++){
            for (int j = 0;j<deviceList.get(i).getEquipmentList().size();j++){
                DeviceEntity entity = new DeviceEntity();
                if(j == 0){
                    entity.setCheck(true);
                }
                entity.setDevice_id(deviceList.get(i).getEquipmentList().get(j).getDeviceId());
                entity.setDevice_name(deviceList.get(i).getAddressName()+"--"+deviceList.get(i).getEquipmentList().get(j).getDeviceName());
                this.deviceList.add(entity);
            }
        }
    }

    public List<DeviceEntity> getDeviceList() {
        return deviceList;
    }
}
