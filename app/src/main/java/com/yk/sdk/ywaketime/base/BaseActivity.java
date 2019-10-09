package com.yk.sdk.ywaketime.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yk.sdk.ywaketime.event.Event;
import com.yk.sdk.ywaketime.event.EventUtils;
import com.yk.sdk.ywaketime.net.state.NetStateInfo;
import com.yk.sdk.ywaketime.net.state.NetWorkLiveData;
import com.yk.sdk.ywaketime.util.ToastUtils;
import com.yk.sdk.ywaketime.widget.MainActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    private Activity activity;
    protected ProgressDialog progress;
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        if (getLayoutID() != 0) {
            setContentView(getLayoutID());
        }
        initView();
        initData();
        initNet();
        if (isRegisterEventBus()) {
            EventUtils.register(this);
        }
    }

    protected int getLayoutID() {
        return 0;
    }

    protected abstract void initView();

    protected abstract void initData();

    /**
     * 绑定控件
     */
    public <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    /**
     * 打开一个Activity ，是否关闭当前activity
     */
    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(this, clz);
        if (ex != null) intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            finish();
        }
    }

    /**
     * 显示对话框
     */
    public void showProgress() {
        if (progress == null) {
            progress = new ProgressDialog(activity);
        }
        progress.show();
    }

    /**
     * 隐藏对话框
     */
    public void hideProgress() {
        if (progress != null && progress.isShowing()) {
            progress.hide();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int indext = 0; indext < fragmentManager.getFragments().size(); indext++) {
            Fragment fragment = fragmentManager.getFragments().get(indext); //找到第一层Fragment
            if (fragment == null)
                Log.w(TAG, "Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            else
                handleResult(fragment, requestCode, resultCode, data);
        }
    }

    /**
     * 递归调用，对所有的子Fragment生效
     */
    private void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        for (Fragment f : childFragment)
            if (f != null) {
                handleResult(f, requestCode, resultCode, data);
            }
    }


    /**
     * 是否注册EventBus
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(Event event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(Event event) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventUtils.unregister(this);
        }
        ToastUtils.getInstance().onDestroy();
    }

    /**
     * 显示Toast
     */
    protected void showToast(Context context, String message) {
        ToastUtils.getInstance().shortToast(context, message);
    }

    /**
     * 初始化网络
     */
    private void initNet() {
        NetWorkLiveData.getInstance(this).observe(this, new Observer<NetStateInfo>() {
            @Override
            public void onChanged(NetStateInfo networkInfo) {
                switch (networkInfo.getCode()) {
                    case NetStateInfo.TYPE_NETWORK_UNCONNECTED: {
                        showToast(BaseActivity.this, "未连接网络");
                        break;
                    }
                    case NetStateInfo.TYPE_NETWORK_WIFI: {
                        showToast(BaseActivity.this, "WIFI");
                        break;
                    }
                    case NetStateInfo.TYPE_NETWORK_MOBILE: {
                        showToast(BaseActivity.this, "移动网络");
                        break;
                    }
                }
            }
        });
    }
}
