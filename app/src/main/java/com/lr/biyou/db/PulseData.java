package com.lr.biyou.db;

import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * socket 心跳数据包
 */
public class PulseData implements IPulseSendable {
    private String str = "pulse";

    public PulseData() {

    }

    @Override
    public byte[] parse() {
        byte[] body = str.getBytes(Charset.defaultCharset());
        ByteBuffer bb = ByteBuffer.allocate(4 + body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }


}
