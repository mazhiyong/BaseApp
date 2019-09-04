package com.lr.biyou.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ListUpBean implements Parcelable {
    /**
     * name : BTC
     * price : 150.00000000
     * open : 150.00000000
     * close : 150.00000000
     * ratio : 0.00
     * range : 0.00000000
     * high : 0.00000000
     * low : 0.00000000
     * volume : 0.00
     * cny_number : 1020
     */

    private String name; // 币种名称
    private String area; // 币种交易区
    private String price; // 价格
    private String open; // 开盘价
    private String close; // 关盘价
    private String ratio; // 涨幅百分比
    private String range; // 浮动数
    private String high; // 最高价
    private String low; // 最低价
    private String volume; // 数量
    @SerializedName("cny_number")
    private String cnyNumber; // 人民币

    protected ListUpBean(Parcel in) {
        name = in.readString();
        area = in.readString();
        price = in.readString();
        open = in.readString();
        close = in.readString();
        ratio = in.readString();
        range = in.readString();
        high = in.readString();
        low = in.readString();
        volume = in.readString();
        cnyNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(area);
        dest.writeString(price);
        dest.writeString(open);
        dest.writeString(close);
        dest.writeString(ratio);
        dest.writeString(range);
        dest.writeString(high);
        dest.writeString(low);
        dest.writeString(volume);
        dest.writeString(cnyNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListUpBean> CREATOR = new Creator<ListUpBean>() {
        @Override
        public ListUpBean createFromParcel(Parcel in) {
            return new ListUpBean(in);
        }

        @Override
        public ListUpBean[] newArray(int size) {
            return new ListUpBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area == null ? "" : area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCnyNumber() {
        return cnyNumber;
    }

    public void setCnyNumber(String cnyNumber) {
        this.cnyNumber = cnyNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListUpBean that = (ListUpBean) o;
        return name.equals(that.name) &&
                area.equals(that.area) &&
                price.equals(that.price) &&
                open.equals(that.open) &&
                close.equals(that.close) &&
                ratio.equals(that.ratio) &&
                range.equals(that.range) &&
                high.equals(that.high) &&
                low.equals(that.low) &&
                volume.equals(that.volume) &&
                cnyNumber.equals(that.cnyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, area, price, open, close, ratio, range, high, low, volume, cnyNumber);
    }
}
