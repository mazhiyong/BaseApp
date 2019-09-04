package com.lr.biyou.db;

import com.stx.xhb.xbanner.entity.SimpleBannerInfo;

public class CustomViewsInfo extends SimpleBannerInfo {
    private String info;

    public CustomViewsInfo(String info) {
        this.info = info;
    }

    @Override
    public String getXBannerUrl() {
        return info;
    }
}
