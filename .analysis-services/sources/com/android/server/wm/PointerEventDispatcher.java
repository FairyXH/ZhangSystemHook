package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class PointerEventDispatcher extends android.view.InputEventReceiver {
    private final java.util.ArrayList<android.view.WindowManagerPolicyConstants.PointerEventListener> mListeners;
    private android.view.WindowManagerPolicyConstants.PointerEventListener[] mListenersArray;
    private com.android.server.wm.IPointerEventDispatcherExt mPointerEventDispatcherExt;

    public PointerEventDispatcher(android.view.InputChannel inputChannel) {
        super(inputChannel, ((com.android.server.wm.IPointerEventDispatcherExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IPointerEventDispatcherExt.class).create()).getOptLooper(com.android.server.UiThread.getHandler().getLooper()));
        this.mListeners = new java.util.ArrayList<>();
        this.mListenersArray = new android.view.WindowManagerPolicyConstants.PointerEventListener[0];
        this.mPointerEventDispatcherExt = (com.android.server.wm.IPointerEventDispatcherExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IPointerEventDispatcherExt.class).base(this).create();
    }

    public void onInputEvent(android.view.InputEvent event) {
        android.view.WindowManagerPolicyConstants.PointerEventListener[] listeners;
        try {
            if ((event instanceof android.view.MotionEvent) && (event.getSource() & 2) != 0) {
                android.view.MotionEvent motionEvent = (android.view.MotionEvent) event;
                synchronized (this.mListeners) {
                    if (this.mListenersArray == null) {
                        this.mListenersArray = new android.view.WindowManagerPolicyConstants.PointerEventListener[this.mListeners.size()];
                        this.mListeners.toArray(this.mListenersArray);
                    }
                    listeners = this.mListenersArray;
                }
                for (int i = 0; i < listeners.length; i++) {
                    long startTime = java.lang.System.currentTimeMillis();
                    listeners[i].onPointerEvent(motionEvent);
                    this.mPointerEventDispatcherExt.debugInputEventDuration(motionEvent, listeners[i], startTime);
                }
            }
        } finally {
            finishInputEvent(event, false);
        }
    }

    public void registerInputEventListener(android.view.WindowManagerPolicyConstants.PointerEventListener listener) {
        synchronized (this.mListeners) {
            if (this.mListeners.contains(listener)) {
                throw new java.lang.IllegalStateException("registerInputEventListener: trying to register" + listener + " twice.");
            }
            this.mListeners.add(listener);
            this.mListenersArray = null;
        }
    }

    public void unregisterInputEventListener(android.view.WindowManagerPolicyConstants.PointerEventListener listener) {
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(listener)) {
                throw new java.lang.IllegalStateException("registerInputEventListener: " + listener + " not registered.");
            }
            this.mListeners.remove(listener);
            this.mListenersArray = null;
        }
    }

    public void dispose() {
        super.dispose();
        synchronized (this.mListeners) {
            this.mListeners.clear();
            this.mListenersArray = null;
        }
    }
}
