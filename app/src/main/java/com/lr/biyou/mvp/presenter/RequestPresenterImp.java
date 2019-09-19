package com.lr.biyou.mvp.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.lr.biyou.R;
import com.lr.biyou.api.ErrorHandler;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.base.BasePresenterImp;
import com.lr.biyou.mvp.model.RequestModelImp;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.NetworkUtils;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * 描述：MVP中的P实现类
 */

public class RequestPresenterImp extends BasePresenterImp<RequestView,Map<String,Object>> implements RequestPresenter { //传入泛型V和T分别为WeatherView、WeatherInfoBean表示建立这两者之间的桥梁
    private Context context = null;
    private RequestModelImp mRequestModelImp ;
    /**
     * @descriptoin 构造方法
     * @param view 具体业务的视图接口对象
     */
    public RequestPresenterImp(RequestView view, Context context) {
        super(view);
        this.context = context;
        this.mRequestModelImp = new RequestModelImp(context);
    }

    private void checkData(){

        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN) || UtilTools.empty( MbsConstans.REFRESH_TOKEN )){
            MbsConstans.ACCESS_TOKEN = SPUtils.get(context, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
            MbsConstans.REFRESH_TOKEN = SPUtils.get(context, MbsConstans.SharedInfoConstans.REFRESH_TOKEN, "").toString();
            String s  = SPUtils.get(context, MbsConstans.SharedInfoConstans.LOGIN_INFO, "").toString();
            MbsConstans.USER_MAP =   JSONUtil.getInstance().jsonMap(s);
        }
    }

    private Map<String,Object> netErrorBack(){
        Map<String,Object> errorMap = new HashMap<>();
        errorMap.put("errcode",ErrorHandler.ERROR.NETWORD_ERROR);
        errorMap.put("errmsg",context.getResources().getString(R.string.net_error));
        return errorMap;
    }

    /**
     * 请求(支持RequsetBody) 返回ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void requestDeleteToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap) {
        //checkData();
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }
        LogUtilDebug.i("requestGetToRes.ACCESS_TOKEN",MbsConstans.ACCESS_TOKEN);
        LogUtilDebug.i("requestGetToRes.REFRESH_TOKEN",MbsConstans.REFRESH_TOKEN);
        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }


        //请求参数为json类型的时候的请求，需要做下面操作
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mParaMap);
        LogUtilDebug.i("打印log日志","请求的参数"+jsonStr);
        RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonStr);
        mRequestModelImp.requestDeleteToRes(mHeaderMap,mUrl, body, this);
    }

    /**
     * Delete请求(支持RequsetBody) 返回Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void requestDeleteToMap(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap) {
        checkData();
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }
        LogUtilDebug.i("requestGetToRes.ACCESS_TOKEN",MbsConstans.ACCESS_TOKEN);
        LogUtilDebug.i("requestGetToRes.REFRESH_TOKEN",MbsConstans.REFRESH_TOKEN);
        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }

        //请求参数为json类型的时候的请求，需要做下面操作
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mParaMap);
        LogUtilDebug.i("打印log日志","请求的参数"+jsonStr);
        RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonStr);
        mRequestModelImp.requestDeleteToMap(mHeaderMap,mUrl, body, this);
    }

    /**
     * Put请求  返回ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void requestPutToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap) {
        checkData();
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }
        LogUtilDebug.i("requestGetToRes.ACCESS_TOKEN",MbsConstans.ACCESS_TOKEN);
        LogUtilDebug.i("requestGetToRes.REFRESH_TOKEN",MbsConstans.REFRESH_TOKEN);
        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }


        //请求参数为json类型的时候的请求，需要做下面操作
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mParaMap);

        LogUtilDebug.i("打印log日志","请求的参数"+jsonStr);
        RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonStr);
        mRequestModelImp.requestPutToRes(mHeaderMap,mUrl, body, this);
    }

    /**
     * Put请求  返回Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void requestPutToMap(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap) {
        checkData();
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }
        LogUtilDebug.i("requestGetToRes.ACCESS_TOKEN",MbsConstans.ACCESS_TOKEN);
        LogUtilDebug.i("requestGetToRes.REFRESH_TOKEN",MbsConstans.REFRESH_TOKEN);
        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }

        //请求参数为json类型的时候的请求，需要做下面操作
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mParaMap);

        LogUtilDebug.i("打印log日志","请求的参数"+jsonStr);
        RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonStr);
        mRequestModelImp.requestPutToMap(mHeaderMap,mUrl, body, this);
    }


    /**
     *Get请求  返回ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void requestGetToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap) {
        checkData();
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }
        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }

        mRequestModelImp.requestGetToRes(mHeaderMap,mUrl,mParaMap, this);
    }


    /**
     * Get请求  返回Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void requestGetToMap(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap) {
        checkData();
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }

       /* LogUtilDebug.i("requestGetToMap.ACCESS_TOKEN",MbsConstans.ACCESS_TOKEN);
        LogUtilDebug.i("requestGetToMap.REFRESH_TOKEN",MbsConstans.REFRESH_TOKEN);
        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }*/

        mRequestModelImp.requestGetToMap(mHeaderMap,mUrl, mParaMap,this);
    }


    /**
     * Post请求  返回ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void requestPostToRes(Map<String,String> mHeaderMap, String mUrl, Map<String,String> mParaMap) {
        checkData();
        // mHeaderMap.put("Connection", "close");
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }

        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }


        //请求参数为json类型的时候的请求，需要做下面操作
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mParaMap);

        LogUtilDebug.i("打印log日志","请求的参数"+jsonStr);
        RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonStr);
        mRequestModelImp.requestPostToRes(mHeaderMap,mUrl, body, this);
    }

    /**
     *  Post请求  返回Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void requestPostToMap(Map<String,String> mHeaderMap, String mUrl, Map<String,Object> mParaMap) {
        //checkData();
        // mHeaderMap.put("Connection", "close");
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }


//        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
//            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
//        }
//        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
//            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
//        }


        //请求参数为json类型的时候的请求，需要做下面操作
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mParaMap);

        LogUtilDebug.i("打印log日志","请求的参数"+jsonStr);
        RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonStr);
        mRequestModelImp.requestPostToMap(mHeaderMap,mUrl, body, this);
    }




    /**
     *  //表单提交  返回ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void requestPostFormToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap) {
        checkData();
        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }


        mRequestModelImp.requestPostFormToRes(mHeaderMap,mUrl,mParaMap, this);
    }

    /**
     *  //表单提交  返回Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void requestPostFormToMap(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap) {
        checkData();
        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }

        mRequestModelImp.requestPostFormToMap(mHeaderMap,mUrl,mParaMap, this);
    }


    /**
     *  下载文件  支持断点续传
     * @param start
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     */
    @Override
    public void downloadFile(String start, Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap) {
        checkData();
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }
        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }

        //请求参数为json类型的时候的请求，需要做下面操作
        mRequestModelImp.downloadFile(start,mHeaderMap,mUrl,mParaMap, this);
    }


    /**
     *  上传文件/图片  返回ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param mFileMap
     */
    @Override
    public void postFileToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, Object> mParaMap, Map<String, Object> mFileMap) {
        checkData();
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }

        if(!UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if(!UtilTools.empty(MbsConstans.REFRESH_TOKEN)){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }


        List<MultipartBody.Part> parts = getRequestBodyFileMap(mFileMap);
        Map<String,RequestBody> requestBodyMap = getRequestBodyMap(mParaMap);

        mRequestModelImp.postFileToRes(mHeaderMap,mUrl,requestBodyMap, parts, this);
    }


    /**
     *  上传文件/图片  返回Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param mFileMap
     */

    @Override
    public void postFileToMap(Map<String, String> mHeaderMap, String mUrl, Map<String,Object> mSignMap,Map<String, Object> mParaMap, Map<String,Object> mFileMap) {
        checkData();
        if (!NetworkUtils.isNetAvailable(context)){
            Map<String,Object> errorMap = netErrorBack();
            this.requestError(errorMap,mUrl);
            return;
        }

        if (MbsConstans.ACCESS_TOKEN != null && !MbsConstans.ACCESS_TOKEN.equals("") && !MbsConstans.ACCESS_TOKEN.equals("null")){
            mHeaderMap.put("access_token",MbsConstans.ACCESS_TOKEN);
        }
        if (MbsConstans.REFRESH_TOKEN != null && !MbsConstans.REFRESH_TOKEN.equals("") && !MbsConstans.REFRESH_TOKEN.equals("null")){
            mHeaderMap.put("refresh_token",MbsConstans.REFRESH_TOKEN);
        }


        //将File 封装成RequestBody
        List<MultipartBody.Part> parts = getRequestBodyFileMap(mFileMap);
        //将String 封装成RequestBody
        Map<String,RequestBody> requestBodyMap = getRequestBodyMap(mParaMap);
        mRequestModelImp.postFileToMap(mHeaderMap,mUrl,mSignMap,requestBodyMap, parts, this);
    }




    /**
     * /将File 封装成RequestBody
     * @param map
     * @return
     */
    private List<MultipartBody.Part> getRequestBodyFileMap(Map<String,Object> map){
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (String in : map.keySet()) {
            //map.keySet()返回的是所有key的值
            String str = map.get(in)+"";//得到每个key多对用value的值
            File file = new File(str);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData(in, file.getName(), requestBody);
            parts.add(body);
        }
        return parts;
    }


    private Map<String,RequestBody> getRequestBodyMap(Map<String,Object> map){
        Map<String,RequestBody> param = new HashMap<>();
        for (String in : map.keySet()) {
            //map.keySet()返回的是所有key的值
            String str = map.get(in)+"";//得到每个key多对用value的值
            LogUtilDebug.i("打印log日志",in + "     " + str);
            param.put(in,RequestBody.create(MediaType.parse("text/plain"), str));
        }

        return param;
    }

    @Override
    public void unSubscribe() {
        mRequestModelImp.onUnsubscribe();
    }


}
