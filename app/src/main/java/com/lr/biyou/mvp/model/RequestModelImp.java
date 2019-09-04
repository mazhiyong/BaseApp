package com.lr.biyou.mvp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.lr.biyou.api.ErrorHandler;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.api.RxApiManager;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.di.component.DaggerMVPComponent;
import com.lr.biyou.mvp.base.BaseModel;
import com.lr.biyou.mvp.base.IBaseRequestCallBack;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * 描述：MVP中的M实现类，处理业务逻辑数据
 */
public class RequestModelImp extends BaseModel implements RequestModel<Map<String,Object>> {
    private Context context = null;
    CompositeDisposable mCompositeSubscription;

    public RequestModelImp(Context mContext) {
        super();
        context = mContext;
        //mCompositeSubscription = new CompositeDisposable();
        DaggerMVPComponent.create().InjectinTo(this);
        RxApiManager.get().add(context,this);
    }

    /**
     *  Delete请求(支持RequsetBody) 返回ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param iBaseRequestCallBack
     */
    @Override
    public void requestDeleteToRes(Map<String, String> mHeaderMap,final String mUrl, RequestBody mParaMap,final IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.requestDeleteToRes(mHeaderMap,MbsConstans.SERVER_URL+mUrl,mParaMap)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志","################################################"+mCompositeSubscription.isDisposed());
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"   "+map+"------------------获取请求异常信息--------------"+e);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBody) {
                        iBaseRequestCallBack.requestComplete();
                        Map<String, Object> mDataMap = new HashMap<>();
                        //回调接口：请求成功，获取实体类对象
                        //iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        String result = null;
                        try {
                            result = responseBody.body().string().trim();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LogUtilDebug.i("打印log日志", mUrl + "------------------获取get请求结果json字符串-----------------" + result);
                        if (!TextUtils.isEmpty(result)) {
                            mDataMap.put("result", result);
                        } else {
                            mDataMap.put("msg", "");
                        }
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(mDataMap, mUrl);
                        LogUtilDebug.i("打印log日志", mUrl + "------------------获取请求结果-----------------" + mDataMap);

                    }
                }));
    }


    /**
     * Delete请求(支持RequsetBody) 返回Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param iBaseRequestCallBack
     */
    @Override
    public void requestDeleteToMap(Map<String, String> mHeaderMap, String mUrl, RequestBody mParaMap, IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.requestDeleteToMap(mHeaderMap,MbsConstans.SERVER_URL+mUrl,mParaMap)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Map<String,Object>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志","################################################"+mCompositeSubscription.isDisposed());
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"   "+map+"------------------获取请求异常信息--------------"+e);
                    }

                    @Override
                    public void onNext(Map<String,Object> mDataMap) {
                        iBaseRequestCallBack.requestComplete();
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"------------------获取请求结果-----------------"+mDataMap);

                    }
                }));
    }


    /**
     *  Put请求  返回ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param iBaseRequestCallBack
     */
    @Override
    public void requestPutToRes(Map<String, String> mHeaderMap,final String mUrl, RequestBody mParaMap,final IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.requestPutToRes(mHeaderMap,MbsConstans.SERVER_URL+mUrl, mParaMap)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志","################################################"+mCompositeSubscription.isDisposed());
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"   "+map+"------------------获取请求异常信息--------------"+e);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBody) {
                        iBaseRequestCallBack.requestComplete();
                        Map<String,Object> mDataMap = new HashMap<>();
                        //回调接口：请求成功，获取实体类对象
                        // iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        // LogUtilDebug.i("打印log日志",mUrl+"------------------获取请求结果-----------------"+mDataMap);
                        try {
                            String result = responseBody.body().string().trim();
                            LogUtilDebug.i("打印log日志",mUrl+"------------------获取put请求结果json字符串-----------------"+result);
                            if (!TextUtils.isEmpty(result)) {
                                mDataMap.put("msg",result);
                            }else {
                                mDataMap.put("msg","");
                            }
                            iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }


    /**
     * Put请求  返回Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param iBaseRequestCallBack
     */
    @Override
    public void requestPutToMap(Map<String, String> mHeaderMap, String mUrl, RequestBody mParaMap, IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.requestPutToMap(mHeaderMap,MbsConstans.SERVER_URL+mUrl, mParaMap)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Map<String,Object>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志","################################################"+mCompositeSubscription.isDisposed());
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"   "+map+"------------------获取请求异常信息--------------"+e);
                    }

                    @Override
                    public void onNext(Map<String,Object> mDataMap) {
                        iBaseRequestCallBack.requestComplete();
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"------------------获取请求结果-----------------"+mDataMap);

                    }
                }));
    }


    /**
     *  Post请求  返回体 Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param iBaseRequestCallBack
     */
    @Override
    public void requestPostToMap(Map<String,String> mHeaderMap, final String mUrl, RequestBody mParaMap, final IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.requestPostToMap(mHeaderMap,MbsConstans.SERVER_URL+mUrl, mParaMap)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Map<String,Object>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志","################################################"+mCompositeSubscription.isDisposed());
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"   "+map+"------------------获取请求异常信息--------------"+e);

                    }

                    @Override
                    public void onNext(Map<String,Object> mDataMap) {
                        iBaseRequestCallBack.requestComplete();
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        if (mUrl.equals(MethodUrl.REFRESH_TOKEN)){

                            SPUtils.put(context, MbsConstans.SharedInfoConstans.REFRESH_TOKEN, MbsConstans.REFRESH_TOKEN);
                        }
                        LogUtilDebug.i("打印log日志",mUrl+"------------------获取请求结果-----------------"+mDataMap);
                    }
                }));
    }


    /**
     *  Post请求 返回体ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param iBaseRequestCallBack
     */
    @Override
    public void requestPostToRes(Map<String,String> mHeaderMap, final String mUrl, RequestBody mParaMap, final IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {

        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.requestPostToRes(mHeaderMap,MbsConstans.SERVER_URL+mUrl, mParaMap)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志","################################################"+mCompositeSubscription.isDisposed());
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"   "+map+"------------------获取请求异常信息--------------"+e);

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBody) {
                        iBaseRequestCallBack.requestComplete();
                        Map<String,Object> mDataMap = new HashMap<>();
                        //回调接口：请求成功，获取实体类对象
                        //iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        try {
                            String result = responseBody.body().string().trim();
                            LogUtilDebug.i("打印log日志",mUrl+"------------------获取get请求结果json字符串-----------------"+result);

                            if (!TextUtils.isEmpty(result)) {
                                mDataMap.put("result",result);
                            }else {
                                mDataMap.put("msg","");
                            }
                            iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    /**
     * Get 请求 返回体Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParam
     * @param iBaseRequestCallBack
     */
    @Override
    public void requestGetToMap(Map<String, String> mHeaderMap, final String mUrl, Map<String,String> mParam, final IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.requestGetToMap(mHeaderMap,MbsConstans.SERVER_URL+mUrl,mParam)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Map<String,Object>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志","requestGetToMap################################################"+mCompositeSubscription.isDisposed());
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"   "+map+"------------------获取请求异常信息--------------"+e);
                    }

                    @Override
                    public void onNext(Map<String,Object> mDataMap) {
                        iBaseRequestCallBack.requestComplete();
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"------------------获取请求结果-----------------"+mDataMap);
                    }
                }));
    }

    /**
     *  Get 请求 返回体 ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParamMap
     * @param iBaseRequestCallBack
     */
    @Override
    public void requestGetToRes(Map<String, String> mHeaderMap,final String mUrl,Map<String,String> mParamMap,final IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.requestGetToRes(mHeaderMap,MbsConstans.SERVER_URL+mUrl,mParamMap)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<ResponseBody>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志","loadGetStringData################################################"+mCompositeSubscription.isDisposed());
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"   "+map+"------------------获取请求异常信息--------------"+e+"   ");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        iBaseRequestCallBack.requestComplete();
                        Map<String,Object> mDataMap = new HashMap<>();
                        //回调接口：请求成功，获取实体类对象
                        //iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        LogUtilDebug.i(" System.out 打印log日志",mUrl+"------------------获取get请求结果json字符串-----------------"+responseBody);
                        try {
                            if (responseBody == null){
                                LogUtilDebug.i("System.out  responseBody为空",mUrl+"------------------获取get请求结果json字符串-----------------");
                            }else {
                                switch (mUrl){
                                    case MethodUrl.imageCode: //加载图形验证码 返回BitMap
                                        InputStream inputStream = responseBody.byteStream();
                                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                        mDataMap.put("img",bitmap);
                                        break;
                                    default:
                                        String result = responseBody.string().trim(); //返回Map
                                        if (!TextUtils.isEmpty(result)) {
                                            mDataMap.put("result",result);
                                        }else {
                                            mDataMap.put("msg","");
                                        }
                                        break;
                                }

                            }

                            iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }));
    }


    /**
     * 表单提交  返回ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param iBaseRequestCallBack
     */
    @Override
    public void requestPostFormToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap, IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add((Disposable) mApiManagerService.requestPostFormToRes(mHeaderMap,MbsConstans.SERVER_URL+mUrl,mParaMap)
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())//指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Map<String, Object> map = ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map, mUrl);
                        System.out.println(mUrl + map + "------------------获取请求异常信息--------------" + e);
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBody) {
                        Map<String ,Object> mDataMap = new HashMap<>();
                        try {
                            String result = responseBody.body().string().trim();
                            LogUtilDebug.i("show","result:"+result);
                            if (!TextUtils.isEmpty(result)) {
                                mDataMap.put("result",result);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        iBaseRequestCallBack.requestSuccess(mDataMap, mUrl);
                        System.out.println(mUrl + "------------------获取请求结果-----------------" + mDataMap);
                    }
                })
        );
    }

    /**
     * 表单提交  返回Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param iBaseRequestCallBack
     */
    @Override
    public void requestPostFormToMap(Map<String, String> mHeaderMap, String mUrl, Map<String, String> mParaMap, IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add((Disposable) mApiManagerService.requestPostFormToMap(mHeaderMap,MbsConstans.SERVER_URL+mUrl,mParaMap)
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())//指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Map<String,Object>>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Map<String, Object> map = ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map, mUrl);
                        System.out.println(mUrl + map + "------------------获取请求异常信息--------------" + e);
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onNext(Map<String,Object> mDataMap) {
                        iBaseRequestCallBack.requestComplete();
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"------------------获取请求结果-----------------"+mDataMap);
                    }
                })
        );
    }


    /**
     *  下载文件 支持断点续传
     * @param start
     * @param mHeaderMap
     * @param mUrl
     * @param param
     * @param iBaseRequestCallBack
     */
    @Override
    public void downloadFile(String start, Map<String, String> mHeaderMap, String mUrl, Map<String,String> param, IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.downloadFile(start,mHeaderMap,mUrl,param)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        LogUtilDebug.i("打印log日志","################################################"+mCompositeSubscription.isDisposed());
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"   "+map+"------------------获取请求异常信息--------------"+e);

                    }

                    @Override
                    public void onNext(Response<ResponseBody> mDataMap) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("file",mDataMap);
                        iBaseRequestCallBack.requestComplete();
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"------------------获取请求结果-----------------"+mDataMap);
                    }
                }));
    }


    /**
     * 上传图片/文件  返回Map
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param parts
     * @param iBaseRequestCallBack
     */
    @Override
    public void postFileToMap(Map<String, String> mHeaderMap, final String mUrl,Map<String,Object> mSignMap, Map<String, RequestBody> mParaMap , List<MultipartBody.Part> parts, final IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.postFileToMap(mHeaderMap,MbsConstans.SERVER_URL+mUrl, mParaMap,parts)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Map<String,Object>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        if (map == null){
                            map = new HashMap<>();
                        }
                        map.putAll(mSignMap);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+map+"------------------获取请求异常信息--------------"+e);

                    }

                    @Override
                    public void onNext(Map<String,Object> mDataMap) {
                        iBaseRequestCallBack.requestComplete();
                        //回调接口：请求成功，获取实体类对象
                        mDataMap.putAll(mSignMap);
                        iBaseRequestCallBack.requestSuccess(mDataMap,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+"------------------获取请求结果-----------------"+mDataMap);
                    }
                }));
    }

    /**
     * 上传图片/文件  ResponseBody
     * @param mHeaderMap
     * @param mUrl
     * @param mParaMap
     * @param parts
     * @param iBaseRequestCallBack
     */
    @Override
    public void postFileToRes(Map<String, String> mHeaderMap, String mUrl, Map<String, RequestBody> mParaMap, List<MultipartBody.Part> parts, IBaseRequestCallBack<Map<String, Object>> iBaseRequestCallBack) {
        mCompositeSubscription = new CompositeDisposable();
        RxApiManager.get().addCompositeDisposable(context,mCompositeSubscription);
        mCompositeSubscription.add(mApiManagerService.postFileToRes(mHeaderMap,MbsConstans.SERVER_URL+mUrl, mParaMap,parts)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribeWith(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onComplete() {
                        //回调接口：请求已完成，可以隐藏progress
                        //iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBaseRequestCallBack.requestComplete();
                        e.printStackTrace();
                        //回调接口：请求异常
                        Map<String,Object> map =ErrorHandler.handleException(e);
                        iBaseRequestCallBack.requestError(map,mUrl);
                        LogUtilDebug.i("打印log日志",mUrl+map+"------------------获取请求异常信息--------------"+e);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBody) {
                        Map<String ,Object> mDataMap = new HashMap<>();

                        try {
                            String result =  responseBody.body().string();
                            LogUtilDebug.i("show","result:"+result);
                            if (!TextUtils.isEmpty(result)) {
                                mDataMap.put("result",result);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        iBaseRequestCallBack.requestSuccess(mDataMap, mUrl);
                        System.out.println(mUrl + "------------------获取请求结果-----------------" + mDataMap);
                    }
                }));
    }

    @Override
    public void onUnsubscribe() {
        if (mCompositeSubscription != null){
            LogUtilDebug.i("打印log日志","onUnsubscribe  "+mCompositeSubscription.isDisposed());
            //判断状态
            if(!mCompositeSubscription.isDisposed()){
                mCompositeSubscription.clear();  //注销
                mCompositeSubscription.dispose();
            }
        }else {
            LogUtilDebug.i("打印log日志","onUnsubscribe  为空"+context.getPackageName());

        }

    }
}
