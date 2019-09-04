package com.lr.biyou.utils.websocketparams;

public class CoinCoinKlineParams {

    /**
     * symbol : btc
     * area : usdt
     * method : coin_price_kline
     * range : 60
     */

    public String symbol;
    public String area;
    public String method = "k_coinChart";
    public String range;
}
