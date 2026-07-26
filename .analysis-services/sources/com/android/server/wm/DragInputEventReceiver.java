package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DragInputEventReceiver extends android.view.InputEventReceiver {
    private static final int OS_14_1_0 = 33;
    private final com.android.server.wm.DragDropController mDragDropController;
    private boolean mIsStartEvent;
    private boolean mMuteInput;
    private boolean mStylusButtonDownAtStart;

    DragInputEventReceiver(android.view.InputChannel inputChannel, android.os.Looper looper, com.android.server.wm.DragDropController controller) {
        super(inputChannel, looper);
        this.mIsStartEvent = true;
        this.mMuteInput = false;
        this.mDragDropController = controller;
    }

    public void onInputEvent(android.view.InputEvent event) {
        boolean handled = false;
        try {
            try {
            } catch (java.lang.Exception e) {
                android.util.Slog.e("WindowManager", "Exception caught by drag handleMotion", e);
            }
            if ((event instanceof android.view.MotionEvent) && (event.getSource() & 2) != 0 && !this.mMuteInput) {
                android.view.MotionEvent motionEvent = (android.view.MotionEvent) event;
                float newX = motionEvent.getRawX();
                float newY = motionEvent.getRawY();
                boolean isStylusButtonDown = (motionEvent.getButtonState() & 32) != 0;
                if (this.mIsStartEvent) {
                    this.mStylusButtonDownAtStart = isStylusButtonDown;
                    this.mIsStartEvent = false;
                }
                switch (motionEvent.getAction()) {
                    case 0:
                        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                            android.util.Slog.w("WindowManager", "Unexpected ACTION_DOWN in drag layer");
                        }
                        return;
                    case 1:
                        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                            android.util.Slog.d("WindowManager", "Got UP on move channel; dropping at " + newX + "," + newY);
                        }
                        this.mMuteInput = true;
                        break;
                    case 2:
                        if (this.mStylusButtonDownAtStart && !isStylusButtonDown) {
                            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                                android.util.Slog.d("WindowManager", "Button no longer pressed; dropping at " + newX + "," + newY);
                            }
                            this.mMuteInput = true;
                        }
                        break;
                    case 3:
                        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                            android.util.Slog.d("WindowManager", "Drag cancelled!");
                        }
                        this.mMuteInput = true;
                        if (this.mDragDropController.mDragDropControllerExt.isForwardCompatibleVersion(33)) {
                            this.mDragDropController.sendHandlerMessage(2, null);
                            this.mDragDropController.mDragDropControllerExt.postCancelDragAndDrop();
                        }
                        break;
                    default:
                        return;
                }
                this.mDragDropController.handleMotionEvent(this.mMuteInput ? false : true, newX, newY);
                handled = true;
            }
        } finally {
            finishInputEvent(event, false);
        }
    }
}
