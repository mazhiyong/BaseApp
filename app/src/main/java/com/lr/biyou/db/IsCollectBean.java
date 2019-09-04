package com.lr.biyou.db;

import com.google.gson.annotations.SerializedName;

public class IsCollectBean {

    /**
     * ifCollect : 0
     */

    @SerializedName("ifCollect")
    private int ifCollect;

    public int getIfCollect() {
        return ifCollect;
    }

    public void setIfCollect(int ifCollect) {
        this.ifCollect = ifCollect;
    }
}
