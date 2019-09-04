package com.lr.biyou.api;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 自定义一个拦截器
 */
public class CommonParamsInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        //得到原始的请求对象
        Request request = chain.request();

        // 执行本次网络请求操作，返回response信息
        Response response = chain.proceed(request);

        //得到用户所使用的请求方式
        String method = request.method();

        //得到原有的请求参数
        FormBody oldBody = (FormBody) request.body();//1 2 3

        //得到原始的url
        String oldUrl = request.url().toString();

        //重新构建请求体
        request = new Request.Builder()
                .url(oldUrl)
                .build();
        //重新发送请求
        return chain.proceed(request);
    }
}