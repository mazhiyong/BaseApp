package com.lr.biyou.manage;

import android.os.Looper;

import androidx.annotation.Nullable;

import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.WebListener;
import com.lr.biyou.service.MessageService;
import com.lr.biyou.utils.tool.LogUtilDebug;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 后台消息推送管理
 */
public class WebSocketMsgListner extends WebSocketListener {

    private android.os.Handler mHandler;
    private WebListener mListener;
    private WebSocket mWebSocket;

    private static WebSocketMsgListner infoManager;

    public WebListener getListener() {
        return mListener;
    }

    public void setListener(WebListener listener) {
        mListener = listener;
    }

    public static WebSocketMsgListner getInsance(){
        if(infoManager == null){
            synchronized (WebSocketMsgListner.class){
                if(infoManager == null){
                    infoManager = new WebSocketMsgListner();
                }
            }
        }
        return infoManager;

    }

    public void  clearListener(){
        this.mListener=null;
    }

    /**
     * 连接
     * @param webSocket
     * @param response
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        mWebSocket = webSocket;
        mHandler = new android.os.Handler(Looper.getMainLooper());

        //取消正在进行的连接
        MessageService.cancelReconnect();
        LogUtilDebug.i("show","连接成功");
    }

    /**
     * 获取消息
     * @param webSocket
     * @param text
     */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        LogUtilDebug.i("show","消息:"+text);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mListener!=null){
                    mListener.outputMsg(text);
                }
            }
        });
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);

    }


    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        LogUtilDebug.i("show","连接失败");
        this.mWebSocket=null;
        if(MbsConstans.USER_MAP!=null){
            //连接失败重新连接
            MessageService.reConnect();
        }else {
            MessageService.cancelReconnect();
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        LogUtilDebug.i("show","正在关闭"+"/"+code+"/"+reason);
        mWebSocket=null;
        //用户没有退出登录，则继续重新连接   //连接关闭重新连接
        if(MbsConstans.USER_MAP!=null){
            MessageService.reConnect();
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        LogUtilDebug.i("show","连接已关闭"+"/"+code+"/"+reason);
        if(MbsConstans.USER_MAP!=null){
            //连接关闭重新连接
            MessageService.reConnect();
        }else {
            MessageService.cancelReconnect();
        }
    }

    public boolean  sendMessage(String s){
        if(mWebSocket!=null){
            return mWebSocket.send(s);
        }else {
            return false;
        }
    }


    public boolean closeMessage(){
        if(mWebSocket!=null){
            return mWebSocket.close(1000,"");
        }else {
            return false;
        }
    }
}
