package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public interface Watchable {
    void dispatchChange(com.android.server.utils.Watchable watchable);

    boolean isRegisteredObserver(com.android.server.utils.Watcher watcher);

    void registerObserver(com.android.server.utils.Watcher watcher);

    void unregisterObserver(com.android.server.utils.Watcher watcher);

    static void verifyWatchedAttributes(java.lang.Object base, com.android.server.utils.Watcher observer, boolean logOnly) {
        if (!android.os.Build.IS_ENG && !android.os.Build.IS_USERDEBUG) {
            return;
        }
        for (java.lang.reflect.Field f : base.getClass().getDeclaredFields()) {
            com.android.server.utils.Watched annotation = (com.android.server.utils.Watched) f.getAnnotation(com.android.server.utils.Watched.class);
            if (annotation != null) {
                java.lang.String fn = base.getClass().getName() + "." + f.getName();
                try {
                    f.setAccessible(true);
                    java.lang.Object o = f.get(base);
                    if (o instanceof com.android.server.utils.Watchable) {
                        com.android.server.utils.Watchable attr = (com.android.server.utils.Watchable) o;
                        if (attr != null && !attr.isRegisteredObserver(observer)) {
                            handleVerifyError("Watchable " + fn + " missing an observer", logOnly);
                        }
                    } else if (!annotation.manual()) {
                        handleVerifyError("@Watched annotated field " + fn + " is not a watchable type and is not flagged for manual watching.", logOnly);
                    }
                } catch (java.lang.IllegalAccessException e) {
                    handleVerifyError("Watchable " + fn + " not visible", logOnly);
                }
            }
        }
    }

    static void handleVerifyError(java.lang.String errorMessage, boolean logOnly) {
        if (logOnly) {
            android.util.Log.e("Watchable", errorMessage);
            return;
        }
        throw new java.lang.RuntimeException(errorMessage);
    }

    static void verifyWatchedAttributes(java.lang.Object base, com.android.server.utils.Watcher observer) {
        verifyWatchedAttributes(base, observer, false);
    }
}
