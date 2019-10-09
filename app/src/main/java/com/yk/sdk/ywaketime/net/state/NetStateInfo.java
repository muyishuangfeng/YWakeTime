package com.yk.sdk.ywaketime.net.state;

public class NetStateInfo {

    private static final int BASE_TYPE = 0X01;
    //WIFI
    public static final int TYPE_NETWORK_WIFI = BASE_TYPE + 1;
    //未知
    public static final int TYPE_NETWORK_UNKNOWN = BASE_TYPE + 2;
    //未连接
    public static final int TYPE_NETWORK_UNCONNECTED = BASE_TYPE + 3;
    //连接
    public static final int TYPE_NETWORK_CONNECTED = BASE_TYPE + 4;
    //移动网络
    public static final int TYPE_NETWORK_MOBILE = BASE_TYPE + 5;
    private int code;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
