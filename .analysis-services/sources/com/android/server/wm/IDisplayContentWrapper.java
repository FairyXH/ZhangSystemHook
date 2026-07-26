package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayContentWrapper {
    default com.android.server.wm.IDisplayContentExt getExtImpl() {
        return new com.android.server.wm.IDisplayContentExt() { // from class: com.android.server.wm.IDisplayContentWrapper.1
        };
    }

    default com.android.server.wm.INonStaticDisplayContentExt getNonStaticExtImpl() {
        return null;
    }

    default com.android.server.wm.ActivityRecord getFixedRotationLaunchingApp() {
        return null;
    }

    default void startAsyncRotationIfNeeded() {
    }

    default java.lang.Runnable getAsyncRotationStartRunnable() {
        return null;
    }
}
