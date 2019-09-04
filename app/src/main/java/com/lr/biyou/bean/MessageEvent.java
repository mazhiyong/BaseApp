package com.lr.biyou.bean;

import java.util.Map;

/**
 * EventBus  消息类
 */
public class MessageEvent {
    private int type =-1;
    private Map<Object,Object> message;

    public MessageEvent() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Map<Object, Object> getMessage() {
        return message;
    }

    public void setMessage(Map<Object, Object> message) {
        this.message = message;
    }
}
