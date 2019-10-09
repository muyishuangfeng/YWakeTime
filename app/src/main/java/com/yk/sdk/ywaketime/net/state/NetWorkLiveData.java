package com.yk.sdk.ywaketime.net.state;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

public class NetWorkLiveData extends LiveData<NetStateInfo> {

    private Context mContext;
    private static NetWorkLiveData mNetworkLiveData;
    private NetworkReceiver mNetworkReceiver;
    private IntentFilter mIntentFilter;

    private NetWorkLiveData(Context context) {
        mContext = context.getApplicationContext();
        mNetworkReceiver = new NetworkReceiver();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public static NetWorkLiveData getInstance(Context context) {
        if (mNetworkLiveData == null) {
            synchronized (NetWorkLiveData.class) {
                if (mNetworkLiveData == null) {
                    mNetworkLiveData = new NetWorkLiveData(context);
                }
            }
        }
        return mNetworkLiveData;
    }

    @Override
    protected void onActive() {
        super.onActive();
        mContext.registerReceiver(mNetworkReceiver, mIntentFilter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mContext.unregisterReceiver(mNetworkReceiver);
    }

    /**
     * 广播
     */
    private static class NetworkReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo.State wifiState = null;
            NetworkInfo.State mobileState = null;
            NetworkInfo mState=null;
            NetStateInfo info = new NetStateInfo();
            ConnectivityManager manager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert manager != null;
            mState=manager.getActiveNetworkInfo();
            if (mState!=null){
                wifiState = Objects.requireNonNull(manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState();
                mobileState = Objects.requireNonNull(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState();
                if (NetworkInfo.State.CONNECTED == mobileState) {// 手机网络连接成功
                    info.setCode(NetStateInfo.TYPE_NETWORK_MOBILE);
                    getInstance(context).setValue(info);
                } else if (NetworkInfo.State.CONNECTED == wifiState) {// 无线网络连接成功
                    info.setCode(NetStateInfo.TYPE_NETWORK_WIFI);
                    getInstance(context).setValue(info);
                } else  {// 手机没有任何的网络
                    info.setCode(NetStateInfo.TYPE_NETWORK_UNCONNECTED);
                    getInstance(context).setValue(info);
                }
            }else {
                info.setCode(NetStateInfo.TYPE_NETWORK_UNCONNECTED);
                getInstance(context).setValue(info);
            }

        }
    }

}
