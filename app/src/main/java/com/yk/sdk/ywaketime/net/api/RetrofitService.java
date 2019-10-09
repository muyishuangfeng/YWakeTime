package com.yk.sdk.ywaketime.net.api;

import com.yk.sdk.ywaketime.model.BaseEntry;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {


    /**
     * 初始化验证
     *
     */
    @GET("api/me/init-lock")
    Observable<BaseEntry<String>> initVerify(@Query("api_token") String api_token);





}
