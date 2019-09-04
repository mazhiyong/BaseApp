package com.lr.biyou.mvp.presenter;

import java.util.Map;

/**
 * 描述：MVP中的P接口定义
 */
public interface RequestPresenter {

    //Delete请求(支持RequsetBody) 返回ResponseBody
    void requestDeleteToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap);
    //Delete请求(支持RequsetBody) 返回Map
    void requestDeleteToMap(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap);



    //Put请求  返回ResponseBody
    void requestPutToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap);
    //Put请求  返回Map
    void requestPutToMap(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap);



    //Get请求  返回ResponseBody
    void requestGetToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap);
    //Get请求  返回Map
    void requestGetToMap(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap);



    //Post请求  返回ResponseBody
    void requestPostToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap);
    //Post请求  返回Map
    void requestPostToMap(Map<String, String> mHeaderMap, String mUrl, Map<String, Object> mParaMap);



    //表单提交  返回ResponseBody
    void requestPostFormToRes(Map<String,String> mHeaderMap,String mUrl, Map<String,String> mParaMap);
    //表单提交  返回Map
    void requestPostFormToMap(Map<String,String> mHeaderMap,String mUrl, Map<String,String> mParaMap);


    //下载文件  断点续传
    void downloadFile(String start, Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap);


    //文件上传  返回Response
    void postFileToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, Object> mParaMap, Map<String, Object> mFileMap);
    //文件上传  返回Map
    void postFileToMap(Map<String, String> mHeaderMap, String mUrl,Map<String,Object> mSignMap, Map<String, Object> mParaMap, Map<String, Object> mFileMap);




    /**
     * @descriptoin	注销subscribe
     * @author	dc
     * @date 2017/2/17 19:36
     */
    void unSubscribe();

}
