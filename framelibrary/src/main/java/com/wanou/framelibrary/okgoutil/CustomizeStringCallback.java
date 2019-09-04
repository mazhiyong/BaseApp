package com.wanou.framelibrary.okgoutil;

import android.util.Log;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.wanou.framelibrary.R;
import com.wanou.framelibrary.bean.GeneralResult;
import com.wanou.framelibrary.bean.SimpleResponse;
import com.wanou.framelibrary.utils.GsonUtils;
import com.wanou.framelibrary.utils.LogUtils;
import com.wanou.framelibrary.utils.UiTools;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Author by wodx521
 * Date on 2018/11/12.
 *
 * @author Administrator
 */
public abstract class CustomizeStringCallback extends StringCallback {

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        onRequestStart(request);
    }

    @Override
    public void onSuccess(Response<String> response) {
        String body = response.body();
        if (body == null) {
            return;
        }
        try {
            SimpleResponse simpleResponse = GsonUtils.fromJson(body, SimpleResponse.class);
            int resultCode = simpleResponse.code;
            GeneralResult generalResult = null;
            if (resultCode == 1) {
                generalResult = GsonUtils.fromJson(body, getResultType());
                onRequestSuccess(generalResult);
            } else {
                response.setException(new IllegalAccessException(simpleResponse.msg));
                onError(response);
            }
        } catch (Exception e) {
            response.setException(e);
            onError(response);
        }
    }

    @Override
    public void onError(Response<String> response) {
        Throwable exception = response.getException();
        exception.printStackTrace();
        SimpleResponse simpleResponse = null;
        if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
//            UiTools.showToast(UiTools.getString(R.string.connect_fail));
           LogUtils.e("stringCallBack",UiTools.getString(R.string.connect_fail));
        } else if (exception instanceof SocketTimeoutException) {
//            UiTools.showToast(UiTools.getString(R.string.connect_out_time));
           LogUtils.e("stringCallBack",UiTools.getString(R.string.connect_out_time));
        } else if (exception instanceof IllegalAccessException) {
            UiTools.showToast(exception.getMessage());
            simpleResponse = GsonUtils.fromJson(response.body(), SimpleResponse.class);
        } else {
            UiTools.showToast(UiTools.getString(R.string.server_error));
        }
        onRequestError(simpleResponse);
    }

    @Override
    public void onFinish() {
        onRequestFinish();
    }

    public abstract Type getResultType();

    public abstract void onRequestSuccess(GeneralResult generalResult);

    public abstract void onRequestError(SimpleResponse simpleResponse);

    public abstract void onRequestStart(Request<String, ? extends Request> request);

    public abstract void onRequestFinish();
}
