package com.lr.biyou.db;

import com.xuhao.didi.core.iocore.interfaces.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * Socket通信 发送文本数据类
 */
public class TextData implements ISendable {

    private String content;
    protected String extra;

    public TextData() {

    }

    public String getContent() {
        return content;
    }

    public String getExtra() {
        return extra;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public byte[] parse() {
        byte[] body = content.getBytes(Charset.defaultCharset());
        ByteBuffer bb = ByteBuffer.allocate(4 + body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }

}
