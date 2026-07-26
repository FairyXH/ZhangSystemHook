package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
class RotationHelper {
    private static final boolean DEBUG_ROTATION = false;
    private static final java.lang.String TAG = "AudioService.RotationHelper";
    private static android.content.Context sContext;
    private static com.android.server.audio.RotationHelper.AudioDisplayListener sDisplayListener;
    private static java.util.function.Consumer<java.lang.Boolean> sFoldStateCallback;
    private static android.hardware.devicestate.DeviceStateManager.FoldStateListener sFoldStateListener;
    private static android.os.Handler sHandler;
    private static java.util.function.Consumer<java.lang.Integer> sRotationCallback;
    private static final java.lang.Object sRotationLock = new java.lang.Object();
    private static final java.lang.Object sFoldStateLock = new java.lang.Object();
    private static java.lang.Integer sRotation = null;
    private static java.lang.Boolean sFoldState = null;

    RotationHelper() {
    }

    static void init(android.content.Context context, android.os.Handler handler, java.util.function.Consumer<java.lang.Integer> rotationCallback, java.util.function.Consumer<java.lang.Boolean> foldStateCallback) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("Invalid null context");
        }
        sContext = context;
        sHandler = handler;
        sDisplayListener = new com.android.server.audio.RotationHelper.AudioDisplayListener();
        sFoldStateListener = new android.hardware.devicestate.DeviceStateManager.FoldStateListener(sContext, new java.util.function.Consumer() { // from class: com.android.server.audio.RotationHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.audio.RotationHelper.updateFoldState(((java.lang.Boolean) obj).booleanValue());
            }
        });
        sRotationCallback = rotationCallback;
        sFoldStateCallback = foldStateCallback;
        enable();
    }

    static void enable() {
        ((android.hardware.display.DisplayManager) sContext.getSystemService("display")).registerDisplayListener(sDisplayListener, sHandler);
        updateOrientation();
        ((android.hardware.devicestate.DeviceStateManager) sContext.getSystemService(android.hardware.devicestate.DeviceStateManager.class)).registerCallback(new android.os.HandlerExecutor(sHandler), sFoldStateListener);
    }

    static void disable() {
        ((android.hardware.display.DisplayManager) sContext.getSystemService("display")).unregisterDisplayListener(sDisplayListener);
        ((android.hardware.devicestate.DeviceStateManager) sContext.getSystemService(android.hardware.devicestate.DeviceStateManager.class)).unregisterCallback(sFoldStateListener);
    }

    static void updateOrientation() {
        int newRotation = android.hardware.display.DisplayManagerGlobal.getInstance().getDisplayInfo(0).rotation;
        synchronized (sRotationLock) {
            if (sRotation == null || sRotation.intValue() != newRotation) {
                sRotation = java.lang.Integer.valueOf(newRotation);
                publishRotation(sRotation.intValue());
            }
        }
    }

    private static void publishRotation(int rotation) {
        int rotationDegrees;
        switch (rotation) {
            case 0:
                rotationDegrees = 0;
                break;
            case 1:
                rotationDegrees = 90;
                break;
            case 2:
                rotationDegrees = 180;
                break;
            case 3:
                rotationDegrees = 270;
                break;
            default:
                android.util.Log.e(TAG, "Unknown device rotation");
                rotationDegrees = -1;
                break;
        }
        if (rotationDegrees != -1) {
            sRotationCallback.accept(java.lang.Integer.valueOf(rotationDegrees));
        }
    }

    static void updateFoldState(boolean foldState) {
        synchronized (sFoldStateLock) {
            if (sFoldState == null || sFoldState.booleanValue() != foldState) {
                sFoldState = java.lang.Boolean.valueOf(foldState);
                sFoldStateCallback.accept(java.lang.Boolean.valueOf(foldState));
            }
        }
    }

    static void forceUpdate() {
        synchronized (sRotationLock) {
            sRotation = null;
        }
        updateOrientation();
        synchronized (sFoldStateLock) {
            if (sFoldState != null) {
                sFoldStateCallback.accept(sFoldState);
            }
        }
    }

    static final class AudioDisplayListener implements android.hardware.display.DisplayManager.DisplayListener {
        AudioDisplayListener() {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
            com.android.server.audio.RotationHelper.updateOrientation();
        }
    }
}
