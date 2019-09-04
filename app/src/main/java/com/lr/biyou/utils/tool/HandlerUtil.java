package com.lr.biyou.utils.tool;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Handler 封装类(不重建线程)
 * 1、创建Handler实例
 * 2、封装Handler的分发消息的两种方式（消息、线程）
 * 3、封装消息、线程的的处理操作
 */
public class HandlerUtil  {
    public static final String Runable = "myrunable";
    static MessageCallBack mCallBack;
    static HandlerUtil mHandlerUtil;
    //staic 修饰Handler
    static MyHandler mMyHandler;
    private static MyRunable myRunable;
    public static MessageCallBack getmCallBack() {
        return mCallBack;
    }

    public static void setmCallBack(MessageCallBack mCallBack) {
        HandlerUtil.mCallBack = mCallBack;
    }

    public static HandlerUtil init(Context context){
        if(mMyHandler == null){
            mMyHandler = new MyHandler(context);
        }
        if(mHandlerUtil == null){
            mHandlerUtil = new HandlerUtil();
        }

        if(myRunable == null){
            myRunable = new MyRunable();
        }

        return mHandlerUtil;
    }

    /**
     * Post 方法
     * @param message Message
     * @param runnable  Runnable
     * @param time  延迟时间
     * @param what  EmptyMessage时的 int值
     */
    public  void  postMessage(Message message ,String runnable,long time,int what){

        if(message == null && runnable == null){
            mMyHandler.sendEmptyMessageDelayed(what,time);
        }
        //当我们构造Handler传参为CallBack的时候，我们使用handler.handleMessage()；来触发的时候，handler是没有处理的。
        // 只能使用sendMessage();的方式来发送。
        if(message!= null && runnable == null){
            mMyHandler.sendMessageDelayed(message,time);
        }

        if(message == null && runnable!= null && runnable.equals(Runable)){
            mMyHandler.postDelayed(myRunable,time);
        }

    }

    /**
     *  结果处理的回调
     * @param messageCallBack
     */
    public  static void doMessage(MessageCallBack messageCallBack){
        setmCallBack(messageCallBack);
    }

    /**
     * 释放资源
     */
    public static void release(){
        if(myRunable != null){
            mMyHandler.removeCallbacks(myRunable);;
            mMyHandler.removeCallbacksAndMessages(null);
        }
        myRunable = null;
        mMyHandler = null;
        mCallBack = null;

    }

    public static class MyRunable implements Runnable{
        @Override
        public void run() {
            //处理Message消息 更新UI
            if(mCallBack != null){
                Message message = new Message();
                message.what = -1;
                mCallBack.runHandleMessage(message);
            }
        }
    }


    //若引用  防止Handler 内存泄露
    public static class MyHandler extends Handler{
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
           reference = new WeakReference<>(context);
        }

        //处理Message消息 更新UI
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           if(mCallBack != null){
               mCallBack.runHandleMessage(msg);
           }
       }

    }

   public interface MessageCallBack{
        void runHandleMessage(Message message);
   }
}
