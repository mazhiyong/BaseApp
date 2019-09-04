package com.lr.biyou.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.manage.WebSocketMsgListner;
import com.lr.biyou.utils.tool.LogUtilDebug;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * 主线程  后台Service
 */
public class MessageService extends Service {

    private WebSocket mWebSocket;
    private static MessageService msgService;
    private static MyHandler myHandler;
    private static MyRunnable myRunnable;

    private static int mReConnectTime=1000*30;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        msgService=this;
        initWebSocket();

        myHandler=new MyHandler(getBaseContext());
        //创建子线程
        myRunnable=new MyRunnable(getBaseContext());

        LogUtilDebug.i("show","MessageService");
    }

    /**
     *  Websocket 初始化
     */
    private void initWebSocket() {
        if(MbsConstans.USER_MAP!=null){
            OkHttpClient okHttpClient=new OkHttpClient();
            Request request=new Request.Builder()
                    .url(MbsConstans.WEBSOCKET_URL+"xx/xx")
                    .addHeader("clno",MbsConstans.USER_MAP.get("clno")+"")
                    .build();

            mWebSocket=okHttpClient.newWebSocket(request, WebSocketMsgListner.getInsance());

            //关闭okhttp客户端
            okHttpClient.dispatcher().executorService().shutdown();
            LogUtilDebug.i("show","开始连接到websocket服务器");
        }

    }

    /**
     *  重新连接（每间隔30秒进行一次心跳检测）
     */
    public static  void  reConnect(){
        cancelReconnect();
        myHandler.postDelayed(myRunnable,mReConnectTime);
        LogUtilDebug.i("show","重写连接");
    }

    /**
     *  取消 重新连接
     */
    public static  void  cancelReconnect(){
        myHandler.removeCallbacks(myRunnable);
        LogUtilDebug.i("show","取消连接");
    }


    /**
     * static  防止内存泄露
     */
    static class MyRunnable implements Runnable{
        Context mContext;
        public MyRunnable(Context context) {
            mContext = context;
        }

        @Override
        public void run() {
            Message message=Message.obtain();
            myHandler.sendMessage(message);
        }
    }

    static class MyHandler extends Handler{
        //弱引用  解决内存泄露问题
        private WeakReference<Context> mWeakReference;

        public MyHandler(Context context){
            mWeakReference=new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            msgService.initWebSocket();
            myHandler.postDelayed(myRunnable,mReConnectTime);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
