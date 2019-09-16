package com.lr.biyou.rongyun.common;


import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;

/**
 * 批量发送消息工具类，将消息排序延迟 300ms 发送
 * 图片消息发送
 */
public class BatchForwardHelper {
    private static final String TAG = "BatchForwardHelper";
    private static BatchForwardHelper instance;
    private Queue<MessageWrapper> messagelist = new LinkedBlockingDeque<>();
    private Object object = new Object();

    static {
        instance = new BatchForwardHelper();
    }

    public static BatchForwardHelper getInstance() {
        return instance;
    }

    private BatchForwardHelper() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {

                        if (messagelist.isEmpty()) {
                            synchronized (object) {
                                object.wait();
                            }
                        }
                        MessageWrapper wrapper = messagelist.poll();
                        if(wrapper == null) continue;

                        Message message = wrapper.getMessage();
                        MessageContent messageContent = message.getContent();
                        if(messageContent instanceof ImageMessage &&((ImageMessage)messageContent).getRemoteUri() == null){
                            RongIM.getInstance().sendImageMessage(message, null, null, new SendImageMessageWrapper(wrapper.getCallback()));
                        } else {
                            RongIM.getInstance().sendMessage(message, null, null, wrapper.getCallback());
                        }
                        Thread.sleep(300);//这里需要延迟 300ms 来发送，防止消息阻塞
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void batchSendMessage(Message message, IRongCallback.ISendMediaMessageCallback callback) {
        messagelist.offer(new MessageWrapper(message, callback));
        synchronized (object) {
            object.notify();
        }
    }

    private class MessageWrapper {
        private Message message;
        private IRongCallback.ISendMediaMessageCallback callback;

        public MessageWrapper(Message message, IRongCallback.ISendMediaMessageCallback callback) {
            this.message = message;
            this.callback = callback;
        }

        public Message getMessage() {
            return message;
        }

        public IRongCallback.ISendMediaMessageCallback getCallback() {
            return callback;
        }
    }

    private class SendImageMessageWrapper extends RongIMClient.SendImageMessageCallback{
        private IRongCallback.ISendMediaMessageCallback callback;

        public SendImageMessageWrapper(IRongCallback.ISendMediaMessageCallback callback){
         this.callback = callback;
        }

        @Override
        public void onAttached(Message message) {
            callback.onAttached(message);
        }

        @Override
        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            callback.onError(message, errorCode);
        }

        @Override
        public void onSuccess(Message message) {
            callback.onSuccess(message);
        }

        @Override
        public void onProgress(Message message, int i) {
            callback.onProgress(message, i);
        }
    }

}
