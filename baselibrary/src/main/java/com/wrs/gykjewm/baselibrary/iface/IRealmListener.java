package com.wrs.gykjewm.baselibrary.iface;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/9/415:03
 * version: 1.0
 */
public interface IRealmListener {

    void OnSuccess();

    void onError(Throwable error);
}
