package com.gykj.cashier.module.index.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.cashier.R;
import com.gykj.cashier.entity.TabType;
import com.gykj.cashier.module.cashier.ui.activity.CashierActivity;
import com.gykj.cashier.module.edit.ui.activity.EditGoodsActivity;
import com.gykj.cashier.module.finance.ui.activity.FinanceActivity;
import com.gykj.cashier.module.index.iview.IMainView;
import com.gykj.cashier.module.index.presenter.MainPresenter;
import com.gykj.cashier.module.inventory.ui.activity.InventoryActivity;
import com.gykj.cashier.module.order.ui.activity.OrderActivity;
import com.gykj.cashier.module.setting.ui.activity.SettingActivity;
import com.gykj.cashier.module.storage.ui.activity.StorageActivity;
import com.gykj.cashier.widget.HeaderLayoutView;
import com.gykj.jxfvlibrary.manager.JXFvManager;
import com.hoin.usbsdk.UsbController;
import com.wrs.gykjewm.baselibrary.base.BaseActivity;
import com.wrs.gykjewm.baselibrary.base.BaseApplication;
import com.wrs.gykjewm.baselibrary.domain.ToastType;
import com.wrs.gykjewm.baselibrary.manager.CashManager;

import com.wrs.gykjewm.baselibrary.manager.RabbiMqEngine;
import com.wrs.gykjewm.baselibrary.realm.FaceRealm;
import com.wrs.gykjewm.baselibrary.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends BaseActivity implements IMainView,BaseQuickAdapter.OnItemChildClickListener,View.OnClickListener{

    @BindView(R.id.header)
    HeaderLayoutView header;

    @BindView(R.id.main_recyclerview)
    RecyclerView mMainRecyclerView;

    MainPresenter presenter;

    Realm mRealm;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    ToastUtils.showToast(ToastType.SUCCESS,"USB打印机已连接");
                    break;
                case UsbController.USB_DISCONNECTED:
                    ToastUtils.showToast(ToastType.WARNING,"USB打印机断开连接");
                    break;
            }
        }
    };


    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        presenter = new MainPresenter();
        presenter.attachView(this);
        presenter.initAdapter();
        initRecyclerview();
        /**
         * RabbitMQ在该应用的场景
         *
         *  Android（连接port url  创建队列）----(往队列中发送一条消息)---> 服务端监听该队列是否收到消息-》 （如果收到）服务端发送信息到服务端创建的RabiitMQ的这条队列中
         *
         *  --》Android 创建队列来获取服务器返回的信息  在进行相关的人脸 指静脉数据库操作
         */
        if(CashManager.getCashApi().getDeviceId()!=null)
        {
            BaseApplication.getInstance().connectRabbitMq(Long.valueOf(CashManager.getCashApi().getDeviceId()));
        }
         sendRabbitMQMessage(); //发送消息
    }

    @Override
    public void initUi() {
        header.setTitle(getString(R.string.title_main));
        header.setRightBtnOnClickListener(this);
        header.setRightTvOnClickListener(this);
    }


    @Override
    public void initRecyclerview() {
        presenter.getMainTabAdapter().setOnItemChildClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        mMainRecyclerView.setLayoutManager(layoutManager);
        mMainRecyclerView.setAdapter(presenter.getMainTabAdapter());
    }

    //打开钱箱
    @Override
    public void openMoneyBin() {
        final byte[] cmd = new byte[3];
        presenter.initUsbModel();
        presenter.initUsbConnect();
        if(null == presenter.getUsbCtrl() || null == presenter.getUsbDevice()){
            ToastUtils.showToast(ToastType.WARNING,getString(R.string.cashier_money_bin_prompt));
            return;
        }
        final TimerTask task = new TimerTask() {
            public void run() {
                //打开钱箱发送的命令
                cmd[0] = 0x1B;
                cmd[1] = 0x70;
                cmd[2] = 0x01;
                presenter.getUsbCtrl().sendByte(cmd, presenter.getUsbDevice());//左对齐
                presenter.getUsbCtrl().sendByte(cmd, presenter.getUsbDevice());//左对齐
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 300);
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void sendRabbitMQMessage() {
        if(null == mRealm){
            mRealm = Realm.getDefaultInstance();
        }
        RealmResults<FaceRealm> face_all = mRealm.where(FaceRealm.class).findAll(); //人脸识别的数据库

        int countFV = JXFvManager.getInstance().jxCountGroupFeatures();//指筋脉数据库

        Log.d("yxxs","指静脉数据库大小"+countFV);

        if(null == face_all || face_all.size() <= 0 || countFV == 0){  //如果数据库中没有数据    则发送消息到 RabbitMQ中获取人脸以及指静脉的数据
            RabbiMqEngine.getRabbiMqEngine().sendMessage(face_all.size() <= 0,countFV == 0);
        }
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position){
            case TabType.TAB_CASHIER:
                showActivity(   MainActivity.this, CashierActivity.class);
                break;
            case TabType.TAB_STORAGE:
                showActivity(MainActivity.this, StorageActivity.class);
                break;
            case TabType.TAB_GOODS:
                showActivity(MainActivity.this, EditGoodsActivity.class);
                break;
            case TabType.TAB_INVENTORY:
                showActivity(MainActivity.this, InventoryActivity.class);
                break;
            case TabType.TAB_BILL:
                showActivity(MainActivity.this, OrderActivity.class);
                break;
            case TabType.TAB_BIN:
                openMoneyBin();
                break;
            case TabType.TAB_MESSAGE: //消息中心
                break;
            case TabType.TAB_SETTLEMENT:   //财务结算
                showActivity(MainActivity.this, FinanceActivity.class);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        showActivity(MainActivity.this, SettingActivity.class);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sendRabbitMQMessage();
    }


    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != presenter){
            presenter.cancel();
            presenter.detachView();
        }
    }


}
