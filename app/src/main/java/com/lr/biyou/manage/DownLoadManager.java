package com.lr.biyou.manage;

import android.util.Log;

import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class DownLoadManager {

    private static final String TAG = "DownLoadManager";

    //文件类型
    private static String fileSuffix = "";

    private static String APK_CONTENTTYPE = "application/vnd.android.package-archive";
    private static String PNG_CONTENTTYPE = "image/png";
    private static String JPG_CONTENTTYPE = "image/jpg";
    private static String MP4_CONTENTTYPE = "video/mp4";

    private static File sFutureStudioIconFile ;
    private static long sFileSize;

    public static boolean PAUSE = false;




    public static void setsFutureStudioIconFile(File sFutureStudioIconFile) {
        DownLoadManager.sFutureStudioIconFile = sFutureStudioIconFile;
    }

    public static File getsFutureStudioIconFile() {

        return sFutureStudioIconFile;
    }


    public static boolean writeFile(ResponseBody body, String name)  {



        InputStream inputStream = null;
        OutputStream outputStream = null;
        long fileSizeDownloaded;


        String type = body.contentType().toString();
        if (type.equals(APK_CONTENTTYPE)) {
            fileSuffix = ".apk";
        } else if (type.equals(PNG_CONTENTTYPE)) {
            fileSuffix = ".png";
        } else if (type.equals(JPG_CONTENTTYPE)) {
            fileSuffix = ".jpg";
        } else if (type.equals(MP4_CONTENTTYPE)) {
            fileSuffix = ".mp4";
        }

        String sPath = MbsConstans.BASE_PATH+ File.separator + name + fileSuffix;
        try {
            sFutureStudioIconFile = new File(sPath);
            setsFutureStudioIconFile(sFutureStudioIconFile);

            try {
                byte[] fileReader = new byte[1014*4];
                //需要下载的文件总长度
                if (sFileSize == 0) {
                    sFileSize = body.contentLength();
                }
                //已经下载的文件长度
                fileSizeDownloaded = sFutureStudioIconFile.length();

                inputStream = body.byteStream();

                outputStream = new FileOutputStream(sFutureStudioIconFile, true);

                if (fileSizeDownloaded < sFileSize){
                    while (true) {
                        //是否暂停
                        if(PAUSE){
                            break;
                        }else {
                            int read = inputStream.read(fileReader);
                            if (read == -1) {
                                break;
                            }
                            outputStream.write(fileReader, 0, read);
                            fileSizeDownloaded += read;
                            Log.d("show", "当前大小: "+fileSizeDownloaded+"   /总大小："+sFileSize+"  /" + fileSizeDownloaded*100/sFileSize+"%" );

                            //Eventbus  发送事件
                            MessageEvent event = new MessageEvent();
                            event.setType(MbsConstans.MessageEventType.DOWN_LOAD);
                            Map<Object,Object> map = new HashMap<>();
                            map.put("size",fileSizeDownloaded);
                            map.put("max",sFileSize);
                            map.put("progress",fileSizeDownloaded*100/sFileSize+"%");
                            event.setMessage(map);

                            EventBus.getDefault().post(event);

                            outputStream.flush();

                        }
                    }
                }
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
