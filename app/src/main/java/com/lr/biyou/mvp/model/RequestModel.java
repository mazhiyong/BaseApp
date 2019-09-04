package com.lr.biyou.mvp.model;


import com.lr.biyou.mvp.base.IBaseRequestCallBack;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 描述：MVP中的M；处理获取网络数据
 * 作者：dc on 2017/2/16 11:03
 * 邮箱：597210600@qq.com
 */
public interface RequestModel<T> {

    //Delete请求(支持RequsetBody) 返回ResponseBody
    void requestDeleteToRes(Map<String, String> mHeaderMap, String mUrl, RequestBody mParaMap, IBaseRequestCallBack<T> iBaseRequestCallBack);
    //Delete请求(支持RequsetBody) 返回Map
    void requestDeleteToMap(Map<String, String> mHeaderMap, String mUrl, RequestBody mParaMap, IBaseRequestCallBack<T> iBaseRequestCallBack);



    //Put请求  返回ResponseBody
    void requestPutToRes(Map<String, String> mHeaderMap, String mUrl, RequestBody mParaMap, IBaseRequestCallBack<T> iBaseRequestCallBack);
    //Put请求  返回Map
    void requestPutToMap(Map<String, String> mHeaderMap, String mUrl, RequestBody mParaMap, IBaseRequestCallBack<T> iBaseRequestCallBack);



    //Get请求  返回ResponseBody
    void requestGetToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, String> param, IBaseRequestCallBack<T> iBaseRequestCallBack);
    //Get请求  返回Map
    void requestGetToMap(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParam, IBaseRequestCallBack<T> iBaseRequestCallBack);


    //Post请求  返回ResponseBody
    void requestPostToRes(Map<String, String> mHeaderMap, String mUrl, RequestBody mParaMap, IBaseRequestCallBack<T> iBaseRequestCallBack);
    //Post请求  返回Map
    void requestPostToMap(Map<String, String> mHeaderMap, String mUrl, RequestBody mParaMap, IBaseRequestCallBack<T> iBaseRequestCallBack);


    //表单提交  返回ResponseBody
    void requestPostFormToRes(Map<String,String> mHeaderMap,String mUrl, Map<String,String> mParaMap,IBaseRequestCallBack<T> iBaseRequestCallBack);
    //表单提交  返回Map
    void requestPostFormToMap(Map<String,String> mHeaderMap,String mUrl, Map<String,String> mParaMap,IBaseRequestCallBack<T> iBaseRequestCallBack);


    //下载文件 支持断点续传
    void downloadFile(String start, Map<String, String> mHeaderMap, String mUrl, Map<String, String> param, IBaseRequestCallBack<T> iBaseRequestCallBack);


    //上传图片、文件   返回Map
    void postFileToMap(Map<String, String> mHeaderMap, String mUrl,Map<String,Object> mSignMap, Map<String, RequestBody> mParaMa, List<MultipartBody.Part> parts, IBaseRequestCallBack<T> iBaseRequestCallBack);
    //上传图片、文件   返回ResponseBody
    void postFileToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, RequestBody> mParaMa, List<MultipartBody.Part> parts, IBaseRequestCallBack<T> iBaseRequestCallBack);



    /**
     * @descriptoin	注销subscribe
     * @author
     * @param
     * @date 2017/2/17 19:02
     * @return
     */
    void onUnsubscribe();
}
