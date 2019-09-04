package com.lr.biyou.api;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 描述：retrofit的接口service定义
 */
public interface ApiManagerService {

    //Delete请求(支持RequsetBody) 返回ResponseBody
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @HTTP(method = "DELETE", hasBody = true)
    Observable<Response<ResponseBody>> requestDeleteToRes(@HeaderMap Map<String, String> header, @Url String url,@Body RequestBody params);

    //Delete请求(支持RequsetBody) 返回Map
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @HTTP(method = "DELETE", hasBody = true)
    Observable<Map<String,Object>> requestDeleteToMap(@HeaderMap Map<String, String> header, @Url String url,@Body RequestBody params);







    //Put请求 返回ResonseBody
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PUT
    Observable<Response<ResponseBody>> requestPutToRes(@HeaderMap Map<String, String> headers, @Url String url, @Body RequestBody params);

    //Put请求 返回Map
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PUT
    Observable<Map<String,Object>> requestPutToMap(@HeaderMap Map<String, String> headers, @Url String url, @Body RequestBody params);






    //Get请求 返回ResonseBody
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET
    Observable<ResponseBody>  requestGetToRes(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String, String> param);

    //Get请求 返回Map
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET
    Observable<Map<String,Object>> requestGetToMap(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String,String> param);


    //下载大文件(支持断点续传)
    @Streaming //大文件时要加不然会OOM
    @GET
    Observable<Response<ResponseBody>> downloadFile(@Header("RANGE") String start, @HeaderMap Map<String,String> headers,@Url String fileUrl,@QueryMap Map<String,String> param);







    //POST请求  返回ResonseBody
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST
    Observable<Response<ResponseBody>> requestPostToRes(@HeaderMap Map<String, String> headers, @Url String url, @Body RequestBody params);

    //POST请求  返回Map
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST
    Observable<Map<String,Object>> requestPostToMap(@HeaderMap Map<String, String> headers, @Url String url, @Body RequestBody params);


    //表单提交 返回ResonseBody
    @Headers({"Content-type:application/x-www-form-urlencoded;charset=UTF-8","Accept: application/json"})
    @FormUrlEncoded
    @POST
    Observable<Response<ResponseBody>> requestPostFormToRes(@HeaderMap Map<String, String> header, @Url String url, @FieldMap Map<String, String> params);

    //表单提交 返回Map
    @Headers({"Content-type:application/x-www-form-urlencoded;charset=UTF-8","Accept: application/json"})
    @FormUrlEncoded
    @POST
    Observable<Map<String,Object>> requestPostFormToMap(@HeaderMap Map<String, String> header, @Url String url, @FieldMap Map<String, String> params);


    //上传文件/图片  返回ResponseBody
    @Multipart
    @POST
    Observable<Response<ResponseBody>> postFileToRes(@HeaderMap Map<String, String> header, @Url String url, @PartMap Map<String, RequestBody> usermaps, @Part() List<MultipartBody.Part> parts);

    //上传文件/图片  返回Map
    @Multipart
    @POST
    Observable<Map<String,Object>> postFileToMap(@HeaderMap Map<String, String> header, @Url String url, @PartMap Map<String , RequestBody> usermaps , @Part() List<MultipartBody.Part> parts);


//    //上传文件/图片  返回Map
//    @Multipart
//    @POST
//    Observable<Map<String,Object>> postOneFileToMap(@HeaderMap Map<String, String> header, @Url String url,@PartMap Map<String , RequestBody> usermaps, @Part() List<MultipartBody.Part> parts);

































}
