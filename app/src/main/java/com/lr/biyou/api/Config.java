package com.lr.biyou.api;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by heavyrain lee on 2017/11/24.
 */

public interface Config {

    // 仅仅是host，没有http开头
    // 不可配置为127.0.0.1 或者 192.168.0.1
    //String IM_SERVER_HOST = "wildfirechat.cn";
    //String IM_SERVER_HOST = "8y2cdj.natappfree.cc";
    //String IM_SERVER_HOST = "39.97.238.99";
    String IM_SERVER_HOST = "47.75.185.156";
    //int IM_SERVER_PORT = 80;
    int IM_SERVER_PORT = 88;

    //正式商用时，建议用https，确保token安全
    //String APP_SERVER_ADDRESS = "http://wildfirechat.cn:8888";
    //String APP_SERVER_ADDRESS = "http://39.97.238.99:8888";
    String APP_SERVER_ADDRESS = "http://47.75.185.156:8888";

    String ICE_ADDRESS = "turn:turn.wildfirechat.cn:3478";
    String ICE_USERNAME = "wfchat";
    String ICE_PASSWORD = "wfchat";

    int DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND = 120;

    String VIDEO_SAVE_DIR = Environment.getExternalStorageDirectory().getPath() + "/wfc/video";
    String AUDIO_SAVE_DIR = Environment.getExternalStorageDirectory().getPath() + "/wfc/audio";
    String PHOTO_SAVE_DIR = Environment.getExternalStorageDirectory().getPath() + "/wfc/photo";
    String FILE_SAVE_DIR = Environment.getExternalStorageDirectory().getPath() + "/wfc/file";

    static void validateConfig() {
        if (TextUtils.isEmpty(IM_SERVER_HOST)
                || IM_SERVER_HOST.startsWith("http")
                || TextUtils.isEmpty(APP_SERVER_ADDRESS)
                || !APP_SERVER_ADDRESS.startsWith("http")
                || IM_SERVER_HOST.equals("192.168.0.1")
                || IM_SERVER_HOST.equals("127.0.0.1")
                || APP_SERVER_ADDRESS.contains("192.168.0.1")
                || APP_SERVER_ADDRESS.contains("127.0.0.1")
        ) {
            throw new IllegalStateException("im server host config error");
        }

        if (IM_SERVER_PORT != 80) {
            Log.w("wfc config", "如果IM_SERVER_PORT配置为非80端口，无法使用第三方文件存储");
        }
    }
}
