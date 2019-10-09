package com.yk.sdk.ywaketime.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;


public class ToastUtils {

    private static volatile ToastUtils sInstance;
    private Toast mToast;

    private ToastUtils() {
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static ToastUtils getInstance() {
        if (sInstance == null) {
            synchronized (ToastUtils.class) {
                if (sInstance == null) {
                    sInstance = new ToastUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * 长时间显示
     */
    public final void longToast(Context context, int id) {
        longToast(context, context.getString(id));
    }

    /**
     * 长时间显示
     *
     * @param context
     * @param toast
     */
    public final void longToast(Context context, final String toast) {
        toast(context, toast, Toast.LENGTH_LONG);
    }

    /**
     * 弹出
     */
    private final void toast(final Context context, final String toast,
                             final int length) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            doShowToast(context, toast, length);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    doShowToast(context, toast, length);
                }
            });
        }
    }

    /**
     * 显示
     */
    private final void doShowToast(Context context, String toast, int length) {
        try {
            final Toast t = getToast(context);
            t.setText(toast);
            t.setDuration(length);
            t.show();
        } catch (Exception e) {
            Toast.makeText(context, toast, length).show();
        }
    }

    /**
     * 获取
     *
     * @param context
     * @return
     */
    private Toast getToast(Context context) {
        if (mToast != null) {
            mToast = new Toast(context.getApplicationContext());
        }
        return mToast;
    }

    /**
     * 短时间显示
     */
    public final void shortToast(Context context, int id) {
        shortToast(context, context.getString(id));
    }

    /**
     * 短时间显示
     */
    public final void shortToast(Context context, String toast) {
        toast(context, toast, Toast.LENGTH_SHORT);
    }


    /**
     * 结束
     */
    public void onDestroy() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
