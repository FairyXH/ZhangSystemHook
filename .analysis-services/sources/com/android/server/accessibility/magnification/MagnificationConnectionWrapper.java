package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
class MagnificationConnectionWrapper {
    private static final boolean DBG = false;
    private static final java.lang.String TAG = "MagnificationConnectionWrapper";
    private final android.view.accessibility.IMagnificationConnection mConnection;
    private final com.android.server.accessibility.AccessibilityTraceManager mTrace;

    MagnificationConnectionWrapper(android.view.accessibility.IMagnificationConnection connection, com.android.server.accessibility.AccessibilityTraceManager trace) {
        this.mConnection = connection;
        this.mTrace = trace;
    }

    void unlinkToDeath(android.os.IBinder.DeathRecipient deathRecipient) {
        this.mConnection.asBinder().unlinkToDeath(deathRecipient, 0);
    }

    void linkToDeath(android.os.IBinder.DeathRecipient deathRecipient) throws android.os.RemoteException {
        this.mConnection.asBinder().linkToDeath(deathRecipient, 0);
    }

    boolean onFullscreenMagnificationActivationChanged(int displayId, boolean activated) {
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.onFullscreenMagnificationActivationChanged", 128L);
        }
        try {
            this.mConnection.onFullscreenMagnificationActivationChanged(displayId, activated);
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    boolean enableWindowMagnification(int displayId, float scale, float centerX, float centerY, float magnificationFrameOffsetRatioX, float magnificationFrameOffsetRatioY, android.view.accessibility.MagnificationAnimationCallback callback) {
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.enableWindowMagnification", 128L, "displayId=" + displayId + ";scale=" + scale + ";centerX=" + centerX + ";centerY=" + centerY + ";magnificationFrameOffsetRatioX=" + magnificationFrameOffsetRatioX + ";magnificationFrameOffsetRatioY=" + magnificationFrameOffsetRatioY + ";callback=" + callback);
        }
        try {
            this.mConnection.enableWindowMagnification(displayId, scale, centerX, centerY, magnificationFrameOffsetRatioX, magnificationFrameOffsetRatioY, transformToRemoteCallback(callback, this.mTrace));
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    boolean setScaleForWindowMagnification(int displayId, float scale) {
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.setScale", 128L, "displayId=" + displayId + ";scale=" + scale);
        }
        try {
            this.mConnection.setScaleForWindowMagnification(displayId, scale);
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    boolean disableWindowMagnification(int displayId, android.view.accessibility.MagnificationAnimationCallback callback) {
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.disableWindowMagnification", 128L, "displayId=" + displayId + ";callback=" + callback);
        }
        try {
            this.mConnection.disableWindowMagnification(displayId, transformToRemoteCallback(callback, this.mTrace));
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    boolean moveWindowMagnifier(int displayId, float offsetX, float offsetY) {
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.moveWindowMagnifier", 128L, "displayId=" + displayId + ";offsetX=" + offsetX + ";offsetY=" + offsetY);
        }
        try {
            this.mConnection.moveWindowMagnifier(displayId, offsetX, offsetY);
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    boolean moveWindowMagnifierToPosition(int displayId, float positionX, float positionY, android.view.accessibility.MagnificationAnimationCallback callback) {
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.moveWindowMagnifierToPosition", 128L, "displayId=" + displayId + ";positionX=" + positionX + ";positionY=" + positionY);
        }
        try {
            this.mConnection.moveWindowMagnifierToPosition(displayId, positionX, positionY, transformToRemoteCallback(callback, this.mTrace));
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    boolean showMagnificationButton(int displayId, int magnificationMode) {
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.showMagnificationButton", 128L, "displayId=" + displayId + ";mode=" + magnificationMode);
        }
        try {
            this.mConnection.showMagnificationButton(displayId, magnificationMode);
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    boolean removeMagnificationButton(int displayId) {
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.removeMagnificationButton", 128L, "displayId=" + displayId);
        }
        try {
            this.mConnection.removeMagnificationButton(displayId);
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    boolean removeMagnificationSettingsPanel(int displayId) {
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.removeMagnificationSettingsPanel", 128L, "displayId=" + displayId);
        }
        try {
            this.mConnection.removeMagnificationSettingsPanel(displayId);
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    boolean onUserMagnificationScaleChanged(int userId, int displayId, float scale) {
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.onMagnificationScaleUpdated", 128L, "displayId=" + displayId);
        }
        try {
            this.mConnection.onUserMagnificationScaleChanged(userId, displayId, scale);
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    boolean setConnectionCallback(android.view.accessibility.IMagnificationConnectionCallback connectionCallback) {
        if (this.mTrace.isA11yTracingEnabledForTypes(384L)) {
            this.mTrace.logTrace("MagnificationConnectionWrapper.setConnectionCallback", 384L, "callback=" + connectionCallback);
        }
        try {
            this.mConnection.setConnectionCallback(connectionCallback);
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    private static android.view.accessibility.IRemoteMagnificationAnimationCallback transformToRemoteCallback(android.view.accessibility.MagnificationAnimationCallback callback, com.android.server.accessibility.AccessibilityTraceManager trace) {
        if (callback == null) {
            return null;
        }
        return new com.android.server.accessibility.magnification.MagnificationConnectionWrapper.RemoteAnimationCallback(callback, trace);
    }

    private static class RemoteAnimationCallback extends android.view.accessibility.IRemoteMagnificationAnimationCallback.Stub {
        private final android.view.accessibility.MagnificationAnimationCallback mCallback;
        private final com.android.server.accessibility.AccessibilityTraceManager mTrace;

        RemoteAnimationCallback(android.view.accessibility.MagnificationAnimationCallback callback, com.android.server.accessibility.AccessibilityTraceManager trace) {
            this.mCallback = callback;
            this.mTrace = trace;
            if (this.mTrace.isA11yTracingEnabledForTypes(64L)) {
                this.mTrace.logTrace("RemoteAnimationCallback.constructor", 64L, "callback=" + callback);
            }
        }

        public void onResult(boolean success) throws android.os.RemoteException {
            this.mCallback.onResult(success);
            if (this.mTrace.isA11yTracingEnabledForTypes(64L)) {
                this.mTrace.logTrace("RemoteAnimationCallback.onResult", 64L, "success=" + success);
            }
        }
    }
}
