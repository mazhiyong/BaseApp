package com.wanou.framelibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.wanou.framelibrary.utils.NetworkUtils;

public class NetStatusReceiver extends BroadcastReceiver {
    NetStatusListener netStatusListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            //获取网络状态的类型
            boolean netStatus = NetworkUtils.isConnected();
            if (netStatusListener != null) {
                // 接口传递网络状态的类型到注册广播的页面
                netStatusListener.onNetChange(netStatus);
                if (netStatus) {
                    NetworkUtils.NetworkType networkType = NetworkUtils.getNetworkType();
                    switch (networkType){
                        case NETWORK_WIFI:
                            // wifi 是否连接
                            netStatusListener.onWifi(NetworkUtils.isWifiConnected());
                            break;
                        case NETWORK_MOBILE:
                            // 数据网络是否连接
                            netStatusListener.onMobile(NetworkUtils.isMobileData());
                            break;
                        default:
                    }
                }
            }
        }
    }

    public void setNetStatusListener(NetStatusListener netStatusListener) {
        this.netStatusListener = netStatusListener;
    }

    public interface NetStatusListener {
        void onNetChange(boolean netStatus);

        void onWifi(boolean isWifiConneted);

        void onMobile(boolean isMobileData);
    }
}
