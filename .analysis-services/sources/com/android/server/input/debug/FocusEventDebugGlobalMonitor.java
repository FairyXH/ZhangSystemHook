package com.android.server.input.debug;

/* JADX INFO: loaded from: classes2.dex */
class FocusEventDebugGlobalMonitor extends android.view.InputEventReceiver {
    private final com.android.server.input.debug.FocusEventDebugView mDebugView;

    FocusEventDebugGlobalMonitor(com.android.server.input.debug.FocusEventDebugView debugView, com.android.server.input.InputManagerService service) {
        super(service.monitorInput("FocusEventDebugGlobalMonitor", 0), com.android.server.UiThread.getHandler().getLooper());
        this.mDebugView = debugView;
    }

    public void onInputEvent(android.view.InputEvent event) {
        try {
            if (event instanceof android.view.MotionEvent) {
                this.mDebugView.reportMotionEvent((android.view.MotionEvent) event);
            }
        } finally {
            finishInputEvent(event, false);
        }
    }
}
