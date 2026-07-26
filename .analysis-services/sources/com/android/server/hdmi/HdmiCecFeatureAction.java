package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
abstract class HdmiCecFeatureAction {
    protected static final int MSG_TIMEOUT = 100;
    protected static final int STATE_NONE = 0;
    private static final java.lang.String TAG = "HdmiCecFeatureAction";
    protected com.android.server.hdmi.HdmiCecFeatureAction.ActionTimer mActionTimer;
    final java.util.List<android.hardware.hdmi.IHdmiControlCallback> mCallbacks;
    private java.util.ArrayList<android.util.Pair<com.android.server.hdmi.HdmiCecFeatureAction, java.lang.Runnable>> mOnFinishedCallbacks;
    private final com.android.server.hdmi.HdmiControlService mService;
    private final com.android.server.hdmi.HdmiCecLocalDevice mSource;
    protected int mState;

    interface ActionTimer {
        void clearTimerMessage();

        void sendTimerMessage(int i, long j);
    }

    abstract void handleTimerEvent(int i);

    abstract boolean processCommand(com.android.server.hdmi.HdmiCecMessage hdmiCecMessage);

    abstract boolean start();

    HdmiCecFeatureAction(com.android.server.hdmi.HdmiCecLocalDevice source) {
        this(source, new java.util.ArrayList());
    }

    HdmiCecFeatureAction(com.android.server.hdmi.HdmiCecLocalDevice source, android.hardware.hdmi.IHdmiControlCallback callback) {
        this(source, (java.util.List<android.hardware.hdmi.IHdmiControlCallback>) java.util.Arrays.asList(callback));
    }

    HdmiCecFeatureAction(com.android.server.hdmi.HdmiCecLocalDevice source, java.util.List<android.hardware.hdmi.IHdmiControlCallback> callbacks) {
        this.mState = 0;
        this.mCallbacks = new java.util.ArrayList();
        for (android.hardware.hdmi.IHdmiControlCallback callback : callbacks) {
            addCallback(callback);
        }
        this.mSource = source;
        this.mService = this.mSource.getService();
        this.mActionTimer = createActionTimer(this.mService.getServiceLooper());
    }

    void setActionTimer(com.android.server.hdmi.HdmiCecFeatureAction.ActionTimer actionTimer) {
        this.mActionTimer = actionTimer;
    }

    private class ActionTimerHandler extends android.os.Handler implements com.android.server.hdmi.HdmiCecFeatureAction.ActionTimer {
        public ActionTimerHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // com.android.server.hdmi.HdmiCecFeatureAction.ActionTimer
        public void sendTimerMessage(int state, long delayMillis) {
            sendMessageDelayed(obtainMessage(100, state, 0), delayMillis);
        }

        @Override // com.android.server.hdmi.HdmiCecFeatureAction.ActionTimer
        public void clearTimerMessage() {
            removeMessages(100);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    com.android.server.hdmi.HdmiCecFeatureAction.this.handleTimerEvent(msg.arg1);
                    break;
                default:
                    android.util.Slog.w(com.android.server.hdmi.HdmiCecFeatureAction.TAG, "Unsupported message:" + msg.what);
                    break;
            }
        }
    }

    private com.android.server.hdmi.HdmiCecFeatureAction.ActionTimer createActionTimer(android.os.Looper looper) {
        return new com.android.server.hdmi.HdmiCecFeatureAction.ActionTimerHandler(looper);
    }

    protected void addTimer(int state, int delayMillis) {
        this.mActionTimer.sendTimerMessage(state, delayMillis);
    }

    boolean started() {
        return this.mState != 0;
    }

    protected final void sendCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        this.mService.sendCecCommand(cmd);
    }

    protected final void sendCommand(com.android.server.hdmi.HdmiCecMessage cmd, com.android.server.hdmi.HdmiControlService.SendMessageCallback callback) {
        this.mService.sendCecCommand(cmd, callback);
    }

    protected final void sendCommandWithoutRetries(com.android.server.hdmi.HdmiCecMessage cmd, com.android.server.hdmi.HdmiControlService.SendMessageCallback callback) {
        this.mService.sendCecCommandWithoutRetries(cmd, callback);
    }

    protected final void addAndStartAction(com.android.server.hdmi.HdmiCecFeatureAction action) {
        this.mSource.addAndStartAction(action);
    }

    protected final <T extends com.android.server.hdmi.HdmiCecFeatureAction> java.util.List<T> getActions(java.lang.Class<T> clazz) {
        return this.mSource.getActions(clazz);
    }

    protected final com.android.server.hdmi.HdmiCecMessageCache getCecMessageCache() {
        return this.mSource.getCecMessageCache();
    }

    protected final void removeAction(com.android.server.hdmi.HdmiCecFeatureAction action) {
        this.mSource.removeAction(action);
    }

    protected final <T extends com.android.server.hdmi.HdmiCecFeatureAction> void removeAction(java.lang.Class<T> clazz) {
        this.mSource.removeActionExcept(clazz, null);
    }

    protected final <T extends com.android.server.hdmi.HdmiCecFeatureAction> void removeActionExcept(java.lang.Class<T> clazz, com.android.server.hdmi.HdmiCecFeatureAction exception) {
        this.mSource.removeActionExcept(clazz, exception);
    }

    protected final void pollDevices(com.android.server.hdmi.HdmiControlService.DevicePollingCallback callback, int pickStrategy, int retryCount) {
        pollDevices(callback, pickStrategy, retryCount, 0L);
    }

    protected final void pollDevices(com.android.server.hdmi.HdmiControlService.DevicePollingCallback callback, int pickStrategy, int retryCount, long pollingMessageInterval) {
        this.mService.pollDevices(callback, getSourceAddress(), pickStrategy, retryCount, pollingMessageInterval);
    }

    void clear() {
        this.mState = 0;
        this.mActionTimer.clearTimerMessage();
    }

    protected void finish() {
        finish(true);
    }

    void finish(boolean removeSelf) {
        clear();
        if (removeSelf) {
            removeAction(this);
        }
        if (this.mOnFinishedCallbacks != null) {
            for (android.util.Pair<com.android.server.hdmi.HdmiCecFeatureAction, java.lang.Runnable> actionCallbackPair : this.mOnFinishedCallbacks) {
                if (((com.android.server.hdmi.HdmiCecFeatureAction) actionCallbackPair.first).mState != 0) {
                    ((java.lang.Runnable) actionCallbackPair.second).run();
                }
            }
            this.mOnFinishedCallbacks = null;
        }
    }

    protected final com.android.server.hdmi.HdmiCecLocalDevice localDevice() {
        return this.mSource;
    }

    protected final com.android.server.hdmi.HdmiCecLocalDevicePlayback playback() {
        return (com.android.server.hdmi.HdmiCecLocalDevicePlayback) this.mSource;
    }

    protected final com.android.server.hdmi.HdmiCecLocalDeviceSource source() {
        return (com.android.server.hdmi.HdmiCecLocalDeviceSource) this.mSource;
    }

    protected final com.android.server.hdmi.HdmiCecLocalDeviceTv tv() {
        return (com.android.server.hdmi.HdmiCecLocalDeviceTv) this.mSource;
    }

    protected final com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem audioSystem() {
        return (com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem) this.mSource;
    }

    protected final int getSourceAddress() {
        return this.mSource.getDeviceInfo().getLogicalAddress();
    }

    protected final int getSourcePath() {
        return this.mSource.getDeviceInfo().getPhysicalAddress();
    }

    protected final void sendUserControlPressedAndReleased(int targetAddress, int uiCommand) {
        this.mSource.sendUserControlPressedAndReleased(targetAddress, uiCommand);
    }

    protected final void addOnFinishedCallback(com.android.server.hdmi.HdmiCecFeatureAction action, java.lang.Runnable runnable) {
        if (this.mOnFinishedCallbacks == null) {
            this.mOnFinishedCallbacks = new java.util.ArrayList<>();
        }
        this.mOnFinishedCallbacks.add(android.util.Pair.create(action, runnable));
    }

    protected void finishWithCallback(int returnCode) {
        invokeCallback(returnCode);
        finish();
    }

    public void addCallback(android.hardware.hdmi.IHdmiControlCallback callback) {
        this.mCallbacks.add(callback);
    }

    private void invokeCallback(int result) {
        try {
            for (android.hardware.hdmi.IHdmiControlCallback callback : this.mCallbacks) {
                if (callback != null) {
                    callback.onComplete(result);
                }
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Callback failed:" + e);
        }
    }
}
