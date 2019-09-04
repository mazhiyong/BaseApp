package com.lr.biyou.utils.websocketparams;

public class CoinCoinTradeParams {

//    {
//        "area": "USDT", 交易区
//        "symbol":"ADA", 交易币
//        "type":1, 深度类型 1三方 0本地
//        "method": "coin_gather", 方法名
//        "size": "5", 聊天条数
//        "page": "1", 页数
//        "token": "26bc07dae616a825e581ded5dc197d4aeb6712a7" 用户token
//    }

    public String type = "1";
    public String method = "coin_gather";
    public String area;
    public String symbol;
}
