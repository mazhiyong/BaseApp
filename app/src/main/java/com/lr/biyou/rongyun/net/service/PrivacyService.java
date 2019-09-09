package com.lr.biyou.rongyun.net.service;

import androidx.lifecycle.LiveData;

import com.lr.biyou.rongyun.model.PrivacyResult;
import com.lr.biyou.rongyun.model.Result;
import com.lr.biyou.rongyun.model.ScreenCaptureResult;

import com.lr.biyou.rongyun.net.SealTalkUrl;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PrivacyService {
    @POST(SealTalkUrl.SET_PRIVACY)
    LiveData<Result> setPrivacy(@Body RequestBody body);

    @GET(SealTalkUrl.GET_PRIVACY)
    LiveData<Result<PrivacyResult>> getPrivacy();

    @POST(SealTalkUrl.GET_SCREEN_CAPTURE)
    LiveData<Result<ScreenCaptureResult>> getScreenCapture(@Body RequestBody body);

    @POST(SealTalkUrl.SET_SCREEN_CAPTURE)
    LiveData<Result<Void>> setScreenCapture(@Body RequestBody body);

    @POST(SealTalkUrl.SEND_SC_MSG)
    LiveData<Result<Void>> sendScreenShotMsg(@Body RequestBody body);
}
