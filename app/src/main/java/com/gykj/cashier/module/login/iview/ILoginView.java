package com.gykj.cashier.module.login.iview;


import com.gykj.cashier.module.login.entity.LoginEntity;
import com.wrs.gykjewm.baselibrary.base.IBaseView;

import java.util.List;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午12:49
 * email:  1113799552@qq.com
 * version: v1.0
 */
public interface ILoginView extends IBaseView {

    void login();

    void showAppVersion();

    void intoMainActivity();

    void showDevcieDialog();

    void showAccount();

    boolean initRealmData(long school_id);

    boolean deleteFaceAndFvRealm(long school_id);
}
