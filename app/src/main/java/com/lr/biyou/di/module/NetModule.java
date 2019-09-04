package com.lr.biyou.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.lr.biyou.api.ApiManagerService;
import com.lr.biyou.api.TrustAllCerts;
import com.lr.biyou.basic.BasicApplication;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.manage.ActivityManager;
import com.lr.biyou.utils.imageload.MyGlideAppModule;
import com.lr.biyou.utils.tool.NetUtil;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.cookie.store.SPCookieStore;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求类Okhttp和Retrofit的对象的实例化
 */
@Module
public class NetModule {
    //地址
    // public static final String BASE_PHONENUMINFO_URL = "https://cms.51hxe.com/hy_cms/";
    // public static final String BASE_PHONENUMINFO_URL = "http://172.16.1.107:8088/api/v1/";


    //短缓存有效期为1分钟
    public static final int CACHE_STALE_SHORT = 60;
    //长缓存有效期为7天
    public static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;

    public static final String CACHE_CONTROL_AGE = "Cache-Control: public, max-age=";

    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_LONG;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    public static final String CACHE_CONTROL_NETWORK = "max-age=0";


    //实例化Okhttp
    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient mOkHttpClient = null;
        SSLSocketFactory socketFactory = null;
        try {
            //获取证书  getResources().openRawResource(R.raw.new_dd)
//            InputStream stream = TestActivity.mInstance.getResources().openRawResource(R.raw.skxy);
         /*   InputStream stream = BasicApplication.getContext().getResources(). getAssets().open("cc.cer");
            Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(stream);
            //使用默认证书
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            //去掉系统默认证书
            keystore.load(null);
            //设置自己的证书
            keystore.setCertificateEntry("phb", certificate);
            //通过信任管理器获取一个默认的算法
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            //算法工厂创建
            TrustManagerFactory instance = TrustManagerFactory.getInstance(algorithm);
            instance.init(keystore);

            TrustManager[] wrappedTrustManagers =getWrappedTrustManagers(instance.getTrustManagers());

            SSLContext tls = SSLContext.getInstance("TLS");//TLS
            tls.init(null, wrappedTrustManagers, null);
            socketFactory = tls.getSocketFactory();
            socketFactory = new Tls12SocketFactory(tls.getSocketFactory());*/
        } catch (Exception e) {

            e.printStackTrace();
        }

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (NetModule.class) {
                if (mOkHttpClient == null) {
                   /* SSLSocketFactory sslSocketFactory = null;
                    try {
                        sslSocketFactory = getSSLSocketFactory_Certificate(BaseApplication.getmContext().getApplicationContext(),null, R.raw.skxy);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    } catch (KeyStoreException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    }*/
                    // 指定缓存路径,缓存大小100Mb
//                    Cache cache = new Cache(new File(BaseApplication.getmContext().getCacheDir(), "HttpCache"),
//                            1024 * 1024 * 100);


                    mOkHttpClient = new OkHttpClient.Builder()
                            //.sslSocketFactory(socketFactory,new HttpsUtil.UnSafeTrustManager())
                            .sslSocketFactory(TrustAllCerts.createSSLSocketFactory())
                            .hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String s, SSLSession sslSession) {
                                    return true;
                                }
                            })
                            // .cache(cache)
                            // .addInterceptor(mRewriteCacheControlInterceptor)
                            //.addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .addInterceptor(interceptor)//okhttp  输出日志信息
                            .retryOnConnectionFailure(true)
                            .cookieJar(new CookieJarImpl(new SPCookieStore(ActivityManager.getInstance().currentActivity())))
                            /*
                             //.addNetworkInterceptor(new StethoInterceptor())
                             .retryOnConnectionFailure(false)
                             */
                            .build();
                }
            }
        }

        return mOkHttpClient;
    }

    //实例化 Gson
    @Provides
    @Singleton
    public Gson provideGson() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                //gson  解决 默认把  int  转double
                .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
                }.getType(), new MapTypeAdapter())
                .setLenient()
                .create();

        return gson;
    }


    //实例化Retrofit
    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MbsConstans.SERVER_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    //初始化化ApiManagerService
    @Provides
    @Singleton
    public ApiManagerService provideApiManagerService(Retrofit retrofit) {
        return retrofit.create(ApiManagerService.class);
    }


    // 缓存拦截器，用来配置缓存策略和网络拦截器配合使用
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            //无网络 请求缓存
            if (!NetUtil.isNetworkConnected()) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }

            Response originalResponse = chain.proceed(request);
            if (NetUtil.isNetworkConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        //清除响应体对Cache有影响的信息
                        .removeHeader("Pragma").build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
                        //清除响应体对Cache有影响的信息
                        .removeHeader("Pragma").build();
            }
        }
    };


    private static TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {

        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];

        return new TrustManager[]{

                new X509TrustManager() {

                    public X509Certificate[] getAcceptedIssuers() {

                        return originalTrustManager.getAcceptedIssuers();

                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {

                        try {

                            originalTrustManager.checkClientTrusted(certs, authType);

                        } catch (CertificateException e) {

                            e.printStackTrace();

                        }

                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);

                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                    }
                }
        };
    }


    public class MapTypeAdapter extends TypeAdapter<Object> {

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();

                case NUMBER:
                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    double dbNum = in.nextDouble();

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }

                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        return lngNum;
                    } else {
                        return dbNum;
                    }

                    /*//将其作为一个字符串读取出来
                    String numberStr = in.nextString();


                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+numberStr);
                    //返回的numberStr不会为null
                    if (numberStr.contains(".") || numberStr.contains("e")
                            || numberStr.contains("E")) {
                        return Double.parseDouble(numberStr);
                    }
                    return Long.parseLong(numberStr);*/

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            // 序列化无需实现
        }

    }
}
