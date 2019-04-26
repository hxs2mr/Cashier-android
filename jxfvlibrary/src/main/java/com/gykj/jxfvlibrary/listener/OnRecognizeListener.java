package com.gykj.jxfvlibrary.listener;

/**
 * desc   : 指静脉认证成功接口回调
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2019/1/1013:05
 * version: 1.0
 */
public interface OnRecognizeListener {

    void recognizeSuccess(String viens);

    void recognizeFailed(String error);
}
