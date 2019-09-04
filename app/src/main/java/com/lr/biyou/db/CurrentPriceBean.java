package com.lr.biyou.db;

import com.github.fujianlian.klinechart.KLineEntity;
import com.google.gson.annotations.SerializedName;

public class CurrentPriceBean {

    /**
     * ch : market.btccny.kline.1min
     * ts : 1489474082831
     * tick : {"id":1489464480,"amount":0,"count":0,"open":7962.62,"close":7962.62,"low":7962.62,"high":7962.62,"vol":0}
     */

    @SerializedName("ch")
    private String ch;
    @SerializedName("ts")
    private long ts;
    @SerializedName("tick")
    private KLineEntity tick;

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public KLineEntity getTick() {
        return tick;
    }

    public void setTick(KLineEntity tick) {
        this.tick = tick;
    }

}
