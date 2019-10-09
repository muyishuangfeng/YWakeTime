package com.yk.sdk.ywaketime.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yk.sdk.ywaketime.event.Event;
import com.yk.sdk.ywaketime.event.EventUtils;
import com.yk.sdk.ywaketime.util.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    //视图是否创建
    protected boolean isViewCreated = false;
    //数据是否加载完成
    private boolean isLoadDataCompleted;
    protected BaseActivity mActivity;
    private View mRootView;
    private ProgressDialog dialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getFragmentId(), container, false);
            initView(mRootView);
            isViewCreated = true;
        }
        if (isRegisterEventBus()) {
            EventUtils.register(this);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            lazyLoadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && !isLoadDataCompleted) {
            lazyLoadData();
        } else {
            isLoadDataCompleted = false;
            isViewCreated = false;
        }
    }


    protected abstract int getFragmentId();

    protected abstract void initView(View view);

    /**
     * 懒加载
     */
    protected void lazyLoadData() {
        isLoadDataCompleted = true;
    }

    /**
     * 显示对话框
     */
    public void showLoadingDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(mActivity);
        }
        dialog.show();
    }

    /**
     * 隐藏对话框
     */
    public void dismissLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(getActivity(), clz);
        if (ex != null)
            intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            mActivity.finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRegisterEventBus()) {
            EventUtils.unregister(this);
        }
        mRootView = null;
        ToastUtils.getInstance().onDestroy();
    }


    /**
     * 返回唯一的Activity实例
     */
    public final BaseActivity getProxyActivity() {
        return mActivity;
    }

    /**
     * 线程
     */
    public void runOnUIThread(Runnable r) {
        final Activity activity = mActivity;
        if (activity != null && r != null)
            activity.runOnUiThread(r);
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

    /**
     * 显示Toast
     */
    protected void showToast(Context context, String message) {
        ToastUtils.getInstance().shortToast(context, message);
    }
}
