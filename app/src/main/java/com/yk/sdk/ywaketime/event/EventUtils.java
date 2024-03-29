package com.yk.sdk.ywaketime.event;


import org.greenrobot.eventbus.EventBus;

public class EventUtils {

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }


    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }

}
