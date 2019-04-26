package com.wrs.gykjewm.baselibrary.manager;



import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.gykj.jxfvlibrary.manager.JXFvManager;
import com.gykj.jxfvlibrary.listener.OnJxFvListener;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.wrs.gykjewm.baselibrary.common.Constant;
import com.wrs.gykjewm.baselibrary.domain.FeatureEntity;
import com.wrs.gykjewm.baselibrary.domain.FvEntity;
import com.wrs.gykjewm.baselibrary.iface.IRealmListener;
import com.wrs.gykjewm.baselibrary.realm.FaceRealm;
import com.wrs.gykjewm.baselibrary.utils.ThreadManager;

import java.io.IOException;

import io.realm.Realm;

/**
 * desc   : RabbiMq引擎类
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/9/49:57
 * version: 1.0
 */
public class RabbiMqEngine {

    private ConnectionFactory factory;

    private Handler handler = new Handler();
    private Connection connection;

    byte[] group1_byte = new byte[20];
    byte[] veinsId = new byte[250];

    private RabbiMqEngine(){
        // 声明ConnectionFactory对象
        factory = new ConnectionFactory();
        System.arraycopy(String.valueOf("lanzhu1").getBytes(),0,group1_byte,0,String.valueOf("lanzhu1").getBytes().length);

    }

    private static class RabbiMqEngineHolder{
        private static RabbiMqEngine instance = new RabbiMqEngine();
    }

    public static RabbiMqEngine getRabbiMqEngine(){
        return RabbiMqEngineHolder.instance;
    }


    public void setUpConnectionFactory(){
        factory.setHost(Constant.MQ_HOST);//主机地址：
        factory.setPort(Constant.MQ_PORT);// 端口号
        factory.setUsername(Constant.MQ_USERNAME);// 用户名
        factory.setPassword(Constant.MQ_PASSWORD);// 密码
        factory.setAutomaticRecoveryEnabled(true);// 设置连接恢复
    }

    public void sendMessage(final boolean face_init, final boolean fv_init){
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                        //创建连接
                        connection = factory.newConnection();

                        //创建通道
                        Channel channel = connection.createChannel();

                        //2.指定一个队列
                        channel.queueDeclare(Constant.QUEUE_NAME+CashManager.getCashApi().getDeviceId(), true, false, false, null);// 声明共享队列
                        //人脸
                        if(face_init){
                            FeatureEntity entity = new FeatureEntity();
                            entity.setEvent("FACE_FEATURES");
                            entity.setMethod("SYN_INIT");
                            entity.setDevieceType(0);
                            entity.setDeviceId(Integer.valueOf(CashManager.getCashApi().getDeviceId()));

                            //3.往队列中发出一条消息
                            channel.basicPublish("cashierTopicExchange", "topic.cashier.server", null, JSON.toJSONString(entity).getBytes());
                        }

                    //指静脉
                        if(fv_init){
                            FeatureEntity entity = new FeatureEntity();
                            entity.setEvent("FINGERS_FEATURES");
                            entity.setMethod("SYN_INIT");
                            entity.setDevieceType(0);
                            entity.setDeviceId(Integer.valueOf(CashManager.getCashApi().getDeviceId()));

                            //往队列中发出一天消息
                            channel.basicPublish("cashierTopicExchange", "topic.cashier.server", null, JSON.toJSONString(entity).getBytes());

                        }
                       //4.关闭频道和连接
                        channel.close();
                        connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 销毁连接
     */
    public void destoryConnect(){
        if(null != connection){
            try {
                connection.abort();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                connection = null;
            }
        }
    }

    public void connect(final long deviceId){
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    connection = factory.newConnection();

                    //创建通道
                    final Channel channel = connection.createChannel();

                    //命名一个队列
                    String queueName = Constant.QUEUE_NAME+deviceId;

                    // 声明队列（持久的、非独占的、连接断开后队列不会自动删除）
                    AMQP.Queue.DeclareOk q = channel.queueDeclare(queueName, true, false, false, null);// 声明共享队列

                    // 根据路由键将队列绑定到交换机上（需要知道交换机名称和路由键名称）
                    channel.queueBind(q.getQueue(), Constant.MQ_EXCHANGE_CAR, Constant.QUEUE_NAME+deviceId);
                    // 创建消费者获取rabbitMQ上的消息。每当获取到一条消息后，就会回调handleDelivery（）方法，
                    // 该方法可以获取到消息数据并进行相应处理
                    Consumer consumer = new DefaultConsumer(channel){
                        @Override
                        public void handleDelivery(String consumerTag, final Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            super.handleDelivery(consumerTag, envelope, properties, body);
                            // 通过geiBody方法获取消息中 的数据
                            // 发消息通知UI更新
                            final FeatureEntity entity = JSON.parseObject(body,FeatureEntity.class);  //获取到的消息进行转换
                            final FvEntity fvEntity = JSON.parseObject(body,FvEntity.class);
                            Log.d("lanzhu",entity.getMethod());
                            String method = "";
                            if(null != entity){
                                method = entity.getMethod();
                            }else {
                                method = fvEntity.getMethod();
                            }
                            Message msg = Message.obtain();
                            //添加  跟新  删除  人脸数据库 和 指静脉数据库
                            switch (method){
                                case "INSERT":
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(null != entity && entity.getEvent().equals("FACE_FEATURES")){
                                                insertFace(entity,channel,envelope);
                                            }else {
                                                insertFv(fvEntity,channel,envelope);
                                            }

                                        }
                                    });
                                    break;
                                case "UPDATE":
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(null != entity && entity.getEvent().equals("FACE_FEATURES")){
                                                updateFace(entity,channel,envelope);
                                            }else {
                                                updateFv(fvEntity,channel,envelope);
                                            }

                                        }
                                    });
                                    break;
                                case "DELETE":
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(null != entity && entity.getEvent().equals("FACE_FEATURES")){
                                                deleteFace(entity,channel,envelope);
                                            }else {
                                                deleteFv(fvEntity,channel,envelope);
                                            }
                                        }
                                    });
                                    break;
                            }
                        }
                    };
                    channel.basicConsume(q.getQueue(), false, consumer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void insertFace(final FeatureEntity entity, final Channel channel, final Envelope envelope){
        final Realm mRealm = Realm.getDefaultInstance();
        FaceRealm faceRealm = mRealm.where(FaceRealm.class).equalTo("user_id", entity.getData().getUserId()) .equalTo("featuresType", entity.getData().getFeaturesType()).equalTo("userType",entity.getData().getUserType()).findFirst();
        if(null == faceRealm){
            FaceRealmManager.getManager().addFaceToRealm(entity.getData().getUserId(),entity.getData().getUserType(), entity.getData().getFeaturesType(), entity.getData().getFeatures(), new IRealmListener() {
                @Override
                public void OnSuccess() {

                    Log.d("lanzhu",entity.getData().getUserId() +"=添加人脸成功");
                    try {
                        channel.basicAck(envelope.getDeliveryTag(),false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable error) {
                    Log.d("lanzhu",entity.getData().getUserId() +"=添加人脸失败"+error.getMessage());
                }
            });
        }else {
            updateFace( entity, channel, envelope);
        }

    }

    private void updateFace(final FeatureEntity entity, final Channel channel, final Envelope envelope){
        FaceRealmManager.getManager().updateFaceToRealm(entity.getData().getUserId(), entity.getData().getUserType(),entity.getData().getFeaturesType(), entity.getData().getFeatures(), new IRealmListener() {
            @Override
            public void OnSuccess() {
                Log.d("lanzhu",entity.getData().getUserId() +"=修改人脸成功");
                try {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable error) {
                Log.d("lanzhu",entity.getData().getUserId() +"=修改人脸失败"+error.getMessage());

            }
        });

    }
    private void deleteFace(final FeatureEntity entity, final Channel channel, final Envelope envelope){
        FaceRealmManager.getManager().deleteFaceToRealm(entity.getData().getUserId(), entity.getData().getUserType(),entity.getData().getFeaturesType(), entity.getData().getFeatures(), new IRealmListener() {
            @Override
            public void OnSuccess() {
                Log.d("lanzhu",entity.getData().getUserId() +"=删除人脸成功");
                try {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable error) {
                Log.d("lanzhu",entity.getData().getUserId() +"=删除人脸失败"+error.getMessage());
            }
        });

    }

    private void insertFv(FvEntity entity, final Channel channel, final Envelope envelope){
        byte[] veinsId = new byte[50];
        StringBuffer buffer = new StringBuffer();
        buffer.append(entity.getData().getUserType())
                .append("_")
                .append(entity.getData().getUserId())
                .append("_")
                .append(entity.getData().getFeaturesType());
        System.arraycopy(buffer.toString().getBytes(), 0,veinsId,0,buffer.toString().getBytes().length);
        JXFvManager.getInstance().jxAddTwoVeinFeature(entity.getData().getFeatures2(), entity.getData().getFeatures3(), veinsId, new OnJxFvListener() {
            @Override
            public void success() {
                Log.d("lanzhu","添加指静脉成功");
                try {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String error) {
                Log.d("lanzhu","添加指静脉失败"+error);
            }
        });
    }
    private void updateFv(final FvEntity entity, final Channel channel, final Envelope envelope){
        byte[] veinsId = new byte[50];
        StringBuffer buffer = new StringBuffer();
        buffer.append(entity.getData().getUserType())
                .append("_")
                .append(entity.getData().getUserId())
                .append("_")
                .append(entity.getData().getFeaturesType());
        System.arraycopy(buffer.toString().getBytes(), 0,veinsId,0,buffer.toString().getBytes().length);
        JXFvManager.getInstance().jxRemoveVeinFeature(veinsId, new OnJxFvListener() {
            @Override
            public void success() {
                Log.d("lanzhu","修改指纹成功");
                insertFv(entity,channel,envelope);
            }

            @Override
            public void failed(String error) {
                Log.d("lanzhu","修改指纹失败"+error);
            }
        });


    }
    private void deleteFv(FvEntity entity, final Channel channel, final Envelope envelope){
        byte[] veinsId = new byte[50];
        StringBuffer buffer = new StringBuffer();
        buffer.append(entity.getData().getUserType())
                .append("_")
                .append(entity.getData().getUserId())
                .append("_")
                .append(entity.getData().getFeaturesType());
        System.arraycopy(buffer.toString().getBytes(), 0,veinsId,0,buffer.toString().getBytes().length);
        JXFvManager.getInstance().jxRemoveVeinFeature(veinsId, new OnJxFvListener() {
            @Override
            public void success() {
                Log.d("lanzhu","删除指纹成功");
                try {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String error) {
                Log.d("lanzhu","删除指纹失败"+error);
            }
        });
    }


}
