package com.gykj.cashier.utils;

import android.content.Context;


import com.gykj.cashier.R;
import com.gykj.cashier.entity.MainTabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * description:主页界面Tab适配器
 * <p>
 * author: josh.lu
 * created: 13/8/18 下午4:06
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class MainTabUtils {


    private MainTabUtils(){}

    public static MainTabUtils getMainTabUtils(){
        return MainTabHolder.instance;
    }


    private static class MainTabHolder{
        private static MainTabUtils instance = new MainTabUtils();
    }


    public List<MainTabEntity> getMainTabList(Context context){
        List<MainTabEntity> mainTabList = new ArrayList<>();

        MainTabEntity cashier = new MainTabEntity();
        cashier.setTab_drawable(context.getResources().getDrawable(R.mipmap.icon_tab_cashier));
        cashier.setTab_name(context.getString(R.string.tab_cashier));
        mainTabList.add(cashier);

        MainTabEntity storage = new MainTabEntity();
        storage.setTab_drawable(context.getResources().getDrawable(R.mipmap.icon_tab_storage));
        storage.setTab_name(context.getString(R.string.tab_storage));
        mainTabList.add(storage);

        MainTabEntity goods = new MainTabEntity();
        goods.setTab_drawable(context.getResources().getDrawable(R.mipmap.icon_tab_goods));
        goods.setTab_name(context.getString(R.string.tab_goods));
        mainTabList.add(goods);

        MainTabEntity inventory = new MainTabEntity();
        inventory.setTab_drawable(context.getResources().getDrawable(R.mipmap.icon_tab_inventory));
        inventory.setTab_name(context.getString(R.string.tab_inventory));
        mainTabList.add(inventory);

        MainTabEntity bill = new MainTabEntity();
        bill.setTab_drawable(context.getResources().getDrawable(R.mipmap.icon_tab_bill));
        bill.setTab_name(context.getString(R.string.tab_bill));
        mainTabList.add(bill);

        MainTabEntity bin = new MainTabEntity();
        bin.setTab_drawable(context.getResources().getDrawable(R.mipmap.icon_tab_bin));
        bin.setTab_name(context.getString(R.string.tab_bin));
        mainTabList.add(bin);

        MainTabEntity message = new MainTabEntity();
        message.setTab_drawable(context.getResources().getDrawable(R.mipmap.icon_tab_message));
        message.setTab_name(context.getString(R.string.tab_message));
        mainTabList.add(message);

        MainTabEntity settlement = new MainTabEntity();
        settlement.setTab_drawable(context.getResources().getDrawable(R.mipmap.icon_tab_settlement));
        settlement.setTab_name(context.getString(R.string.tab_settlement));
        mainTabList.add(settlement);


        return mainTabList;
    }

}
