package com.lr.biyou.utils.websocketparams;

public class CurrenctPriceWsParams {
    private String method = "coinCurrent";
    private String area;
    private String symbol;

    public String getMethod() {
        return method == null ? "" : method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getArea() {
        return area == null ? "" : area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSymbol() {
        return symbol == null ? "" : symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
