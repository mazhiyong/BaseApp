package com.lr.biyou.api;

import android.net.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.lr.biyou.utils.tool.LogUtilDebug;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.HttpException;

public class ErrorHandler {

    public static Map<String,Object> handle(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException error = (HttpException) throwable;
            try {
                return new Gson().fromJson(error.response().errorBody().string(), Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throwable.printStackTrace();
        }
        return null;
    }


    public static final int REFRESH_TOKEN_DATE_CODE = 1007;
    public static final int ACCESS_TOKEN_DATE_CODE = 1006;
    public static final int PHONE_NO_ACTIVE = 1075;


    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static Map<String,Object> handleException(Throwable e) {
        Map<String,Object> errorMap = new HashMap<>();

        if(e instanceof HttpException){
            HttpException httpException = (HttpException) e;
            try {
//                String  s = httpException.response().errorBody().string();
//                boolean b =   JSONUtil.validate(s);
//                if (b){
                errorMap = new Gson().fromJson(httpException.response().errorBody().string(), Map.class);
//                }else {
//                    errorMap = new HashMap<>();
//                }
            } catch (Exception e1) {
                LogUtilDebug.i("打印log日志","解析结果错误"+httpException.code());
                errorMap = new HashMap<>();
                e1.printStackTrace();
            }

            String errorMsg = "";
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    errorMsg = "未授权";
                    break;
                case FORBIDDEN:
                    errorMsg = "服务器禁止访问";
                    break;
                case NOT_FOUND:
                    errorMsg = "服务器未响应，请稍后重试";
                    break;
                case REQUEST_TIMEOUT:
                    errorMsg = "请求超时，请稍后重试";
                    break;
                case GATEWAY_TIMEOUT:
                    errorMsg = "链接服务器失败，请稍后重试";
                    break;
                case INTERNAL_SERVER_ERROR:
                    errorMsg = "服务器内部错误，请稍后重试";
                    break;
                case BAD_GATEWAY:
                    errorMsg = "错误网关，请稍后重试";
                    break;
                case SERVICE_UNAVAILABLE:
                    errorMsg = "服务器不可用，请稍后重试";
                    break;
                default:
                    errorMsg = "请求服务器失败，请稍后重试";
                    break;
            }

            if (errorMap == null || errorMap.isEmpty()){
                errorMap = new HashMap<>();
                errorMap.put("errcode",httpException.code());
                errorMap.put("errmsg",errorMsg);
            }

            return errorMap;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            errorMap.put("errcode",resultException.code);
            errorMap.put("errmsg",resultException.message);
            return errorMap;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            errorMap.put("errcode",ERROR.PARSE_ERROR);
            errorMap.put("errmsg","解析错误");
            return errorMap;
        } else if (e instanceof ConnectException) {
            errorMap.put("errcode",ERROR.NETWORD_ERROR);
            errorMap.put("errmsg","连接失败,请检查网络");
            return errorMap;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            errorMap.put("errcode",ERROR.SSL_ERROR);
            errorMap.put("errmsg","证书验证失败");
            return errorMap;
        } else if (e instanceof ConnectTimeoutException){
            errorMap.put("errcode",ERROR.TIMEOUT_ERROR);
            errorMap.put("errmsg","连接超时");
            return errorMap;
        } else if (e instanceof java.net.SocketTimeoutException) {
            errorMap.put("errcode",ERROR.TIMEOUT_ERROR);
            errorMap.put("errmsg","连接超时");
            return errorMap;
        } else if (e instanceof java.net.UnknownHostException) {
            errorMap.put("errcode",ERROR.NETWORD_ERROR);
            errorMap.put("errmsg","连接失败,请检查网络");
            return errorMap;
        }else {
            errorMap.put("errcode",ERROR.UNKNOWN);
            errorMap.put("errmsg","未知错误");
            e.printStackTrace();
            return errorMap;
        }
    }
    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 10000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 10001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 10002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 10003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 10005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 10006;
    }

    public static class ResponeThrowable extends Exception {
        public int code;
        public String message;

        public ResponeThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;

        }
    }

    public class ServerException extends RuntimeException {
        public int code;
        public String message;
    }
}
