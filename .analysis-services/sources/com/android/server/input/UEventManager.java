package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
interface UEventManager {

    public static abstract class UEventListener {
        private final android.os.UEventObserver mObserver = new android.os.UEventObserver() { // from class: com.android.server.input.UEventManager.UEventListener.1
            public void onUEvent(android.os.UEventObserver.UEvent event) {
                com.android.server.input.UEventManager.UEventListener.this.onUEvent(event);
            }
        };

        public abstract void onUEvent(android.os.UEventObserver.UEvent uEvent);
    }

    default void addListener(com.android.server.input.UEventManager.UEventListener listener, java.lang.String match) {
        listener.mObserver.startObserving(match);
    }

    default void removeListener(com.android.server.input.UEventManager.UEventListener listener) {
        listener.mObserver.stopObserving();
    }
}
