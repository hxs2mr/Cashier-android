package com.gykj.cashier.entity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


/**
 * description:
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午4:07
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class MainTabEntity {



    private String tab_name;
    private Drawable tab_drawable;


    public String getTab_name() {
        return tab_name;
    }

    public void setTab_name(String tab_name) {
        this.tab_name = tab_name;
    }

    public Drawable getTab_drawable() {
        return tab_drawable;
    }

    public void setTab_drawable(Drawable tab_drawable) {
        this.tab_drawable = tab_drawable;
    }

}
