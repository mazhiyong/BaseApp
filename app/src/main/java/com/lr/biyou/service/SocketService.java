package com.lr.biyou.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.db.HandShakeBean;
import com.lr.biyou.db.PulseData;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import org.apache.http.client.RedirectException;

import java.nio.charset.Charset;
import java.util.Arrays;

public class SocketService extends Service {
    private ConnectionInfo info;
    private IConnectionManager manager;
    private SocketActionAdapter socketActionAdapter;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      /*PendingIntent pendingintent = PendingIntent.getActivity(this, 0,
                new Intent(this, SocketService.class), 0);
        Notification notification = new Notification(R.drawable.ic_launcher,
                getString(R.string.app_name), System.currentTimeMillis());
        notification.setLatestEventInfo(this, "uploadservice", "请保持程序在后台运行",
                pendingintent);
        startForeground(0x111, notification);*/

        LogUtilDebug.i("show","SocketService  onStart()");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

       //连接参数设置(IP,端口号),这也是一个连接的唯一标识,不同连接,该参数中的两个值至少有其一不一样
        info = new ConnectionInfo(MbsConstans.SOCKET_IP, MbsConstans.SOCKET_PORT);
        //调用OkSocket,开启这次连接的通道,拿到通道Manager
        manager = OkSocket.open(info);
        //注册Socket行为监听器,SocketActionAdapter是回调的Simple类,其他回调方法请参阅类文档
        socketActionAdapter = new SocketActionAdapter() {
            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                super.onSocketConnectionSuccess(info, action);
                LogUtilDebug.i("show","socket连接成功");
                //发送握手
                manager.send(new HandShakeBean());
                //设置心跳包
                manager.getPulseManager().setPulseSendable(new PulseData());
            }

            @Override
            public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
                super.onSocketConnectionFailed(info, action, e);
                LogUtilDebug.i("show","socket连接失败");
            }

            @Override
            public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
                super.onSocketDisconnection(info, action, e);
                LogUtilDebug.i("show","socket断开连接");
                if (e != null) {
                    if (e instanceof RedirectException) {
                        LogUtilDebug.i("show","正在重定向连接(Redirect Connecting)...");
                        manager.switchConnectionInfo(((com.lr.biyou.service.RedirectException)e).redirectInfo);
                        manager.connect();
                    } else {
                        LogUtilDebug.i("show","socket异常断开:"+ e.getMessage());
                    }
                } else {
                    LogUtilDebug.i("show","socket正常断开:"+ e.getMessage());
                }
            }

            @Override
            public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
                super.onPulseSend(info, data);
                LogUtilDebug.i("show","发送心跳包数据:"+data.toString());
            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                super.onSocketReadResponse(info, action, data);
                LogUtilDebug.i("show","socket读取数据:"+data.toString());
                LogUtilDebug.i("show","socket读取数据头:"+ Arrays.toString(data.getHeadBytes()));
                LogUtilDebug.i("show","socket读取数据体:"+ Arrays.toString(data.getHeadBytes()));

                String str = new String(data.getBodyBytes(), Charset.forName("utf-8"));
                JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
                int cmd = jsonObject.get("cmd").getAsInt();
                if (cmd == 54) {//登陆成功
                    String handshake = jsonObject.get("handshake").getAsString();
                    LogUtilDebug.i("show","握手成功! 握手信息(Handshake Success):" + handshake + ". 开始心跳(Start Heartbeat)..");
                } else if (cmd == 57) {//切换,重定向.(暂时无法演示,如有疑问请咨询github)
                    String ip = jsonObject.get("data").getAsString().split(":")[0];
                    int port = Integer.parseInt(jsonObject.get("data").getAsString().split(":")[1]);
                    ConnectionInfo redirectInfo = new ConnectionInfo(ip, port);
                    redirectInfo.setBackupInfo(info.getBackupInfo());
                    manager.getReconnectionManager().addIgnoreException(RedirectException.class);
                    manager.disconnect(new com.lr.biyou.service.RedirectException(redirectInfo));
                } else if (cmd == 14) {//心跳
                    LogUtilDebug.i("show","收到心跳,喂狗成功(Heartbeat Received,Feed the Dog)");
                    manager.getPulseManager().feed();
                } else {
                    //数据接收成功,更新数据,刷新ui

                }



            }

            @Override
            public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
                super.onSocketWriteResponse(info, action, data);
                LogUtilDebug.i("show","socket发送数据:"+data.toString());
                LogUtilDebug.i("show","socket发送数据体:"+ Arrays.toString(data.parse()));

                byte[] bytes = data.parse();
                bytes = Arrays.copyOfRange(bytes, 4, bytes.length);
                String str = new String(bytes, Charset.forName("utf-8"));
                JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
                int cmd = jsonObject.get("cmd").getAsInt();
                switch (cmd) {
                    case 14:

                        break;
                    case 54:
                        String handshake = jsonObject.get("handshake").getAsString();
                        //开启心跳
                        manager.getPulseManager().pulse();
                        LogUtilDebug.i("show","发送握手数据(Handshake Sending):" + handshake);
                        break;
                    default:
                        //数据发送成功,更新数据,刷新ui
                }

            }
        };

        /*//获得当前连接通道的参配对象
        OkSocketOptions options= manager.getOption();
        //基于当前参配对象构建一个参配建造者类
        OkSocketOptions.Builder builder = new OkSocketOptions.Builder(options);
        //修改参配设置(其他参配请参阅类文档)
        builder.setSinglePackageBytes(size);
       //建造一个新的参配对象并且付给通道
        manager.option(builder.build());*/

        manager.registerReceiver(socketActionAdapter);

        //调用通道进行连接
        manager.connect();


    }

    /**
     * 发送数据
     */








    @Override
    public void onDestroy() {
        LogUtilDebug.i("show","SocketService  onDestroy()");
        if (manager!= null) {
            manager.disconnect();
            manager.unRegisterReceiver(socketActionAdapter);
        }
        //重启service
        Intent sevice = new Intent(this, SocketService.class);
        this.startService(sevice);
        super.onDestroy();
    }
}
