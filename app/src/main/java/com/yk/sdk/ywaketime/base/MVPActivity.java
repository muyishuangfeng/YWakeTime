package com.yk.sdk.ywaketime.base;


import android.os.Bundle;


import androidx.annotation.Nullable;

public abstract class MVPActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity {

    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter == null) {
            mPresenter = initPresenter();
            mPresenter.attach((V) this);
        }

    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }

    protected abstract P initPresenter();

    public P getPresenter() {
        return mPresenter;
    }
}
