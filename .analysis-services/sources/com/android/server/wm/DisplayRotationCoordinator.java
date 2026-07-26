package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DisplayRotationCoordinator {
    private static final java.lang.String TAG = "DisplayRotationCoordinator";
    private int mDefaultDisplayCurrentRotation;
    private int mDefaultDisplayDefaultRotation;
    java.lang.Runnable mDefaultDisplayRotationChangedCallback;

    DisplayRotationCoordinator() {
    }

    void onDefaultDisplayRotationChanged(int rotation) {
        this.mDefaultDisplayCurrentRotation = rotation;
        if (this.mDefaultDisplayRotationChangedCallback != null) {
            this.mDefaultDisplayRotationChangedCallback.run();
        }
    }

    void setDefaultDisplayDefaultRotation(int rotation) {
        this.mDefaultDisplayDefaultRotation = rotation;
    }

    int getDefaultDisplayCurrentRotation() {
        return this.mDefaultDisplayCurrentRotation;
    }

    void setDefaultDisplayRotationChangedCallback(java.lang.Runnable callback) {
        if (this.mDefaultDisplayRotationChangedCallback != null) {
            throw new java.lang.UnsupportedOperationException("Multiple clients unsupported");
        }
        this.mDefaultDisplayRotationChangedCallback = callback;
        if (this.mDefaultDisplayCurrentRotation != this.mDefaultDisplayDefaultRotation) {
            callback.run();
        }
    }

    void removeDefaultDisplayRotationChangedCallback() {
        this.mDefaultDisplayRotationChangedCallback = null;
    }

    static boolean isSecondaryInternalDisplay(com.android.server.wm.DisplayContent displayContent) {
        return (displayContent.isDefaultDisplay || displayContent.mDisplay == null || displayContent.mDisplay.getType() != 1) ? false : true;
    }
}
