package com.yk.sdk.ywaketime.base;

public abstract class BasePresenter<T> {

    private T mView;

    public T getView() {
        return mView;
    }

    public void attach(T view) {
        this.mView = view;
    }

    public void detach() {
        this.mView = null;
    }

}
