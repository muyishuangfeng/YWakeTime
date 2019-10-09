package com.yk.sdk.ywaketime.net.api;


import com.yk.sdk.ywaketime.common.AppConfig;
import com.yk.sdk.ywaketime.net.retrofit.BaseApiImpl;

public class Api extends BaseApiImpl {

    private static Api api = new Api(AppConfig.BASE_URL);

    public Api(String baseUrl) {
        super(baseUrl);
    }

    public static RetrofitService getInstance() {
        return api.getRetrofit().create(RetrofitService.class);
    }


}
