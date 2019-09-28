package com.lr.biyou.db;

import com.github.fujianlian.klinechart.entity.KLineEntity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReqKlineDataBean {

    /**
     * status : ok
     * rep : market.btccny.kline.1min
     * tick : [{"amount":1.6206,"count":3,"id":1494465840,"open":9887,"close":9885,"low":9885,"high":9887,"vol":16021.632026},{"amount":2.2124,"count":6,"id":1494465900,"open":9885,"close":9880,"low":9880,"high":9885,"vol":21859.0235}]
     */


    @SerializedName("status")
    private String status;
    @SerializedName("rep")
    private String rep;
    private List<KLineEntity> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public List<KLineEntity> getTick() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setTick(List<KLineEntity> tick) {
        this.data = tick;
    }
}
