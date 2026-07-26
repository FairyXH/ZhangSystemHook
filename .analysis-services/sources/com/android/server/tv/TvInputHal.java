package com.android.server.tv;

/* JADX INFO: loaded from: classes3.dex */
final class TvInputHal implements android.os.Handler.Callback {
    private static final boolean DEBUG = false;
    public static final int ERROR_NO_INIT = -1;
    public static final int ERROR_STALE_CONFIG = -2;
    public static final int ERROR_UNKNOWN = -3;
    public static final int EVENT_DEVICE_AVAILABLE = 1;
    public static final int EVENT_DEVICE_UNAVAILABLE = 2;
    public static final int EVENT_FIRST_FRAME_CAPTURED = 4;
    public static final int EVENT_STREAM_CONFIGURATION_CHANGED = 3;
    public static final int EVENT_TV_MESSAGE = 5;
    public static final int SUCCESS = 0;
    private static final java.lang.String TAG = com.android.server.tv.TvInputHal.class.getSimpleName();
    private final com.android.server.tv.TvInputHal.Callback mCallback;
    private final java.lang.Object mLock = new java.lang.Object();
    private long mPtr = 0;
    private final android.util.SparseIntArray mStreamConfigGenerations = new android.util.SparseIntArray();
    private final android.util.SparseArray<android.media.tv.TvStreamConfig[]> mStreamConfigs = new android.util.SparseArray<>();
    private final android.os.Handler mHandler = new android.os.Handler(this);

    public interface Callback {
        void onDeviceAvailable(android.media.tv.TvInputHardwareInfo tvInputHardwareInfo, android.media.tv.TvStreamConfig[] tvStreamConfigArr);

        void onDeviceUnavailable(int i);

        void onFirstFrameCaptured(int i, int i2);

        void onStreamConfigurationChanged(int i, android.media.tv.TvStreamConfig[] tvStreamConfigArr, int i2);

        void onTvMessage(int i, int i2, android.os.Bundle bundle);
    }

    private static native int nativeAddOrUpdateStream(long j, int i, int i2, android.view.Surface surface);

    private static native void nativeClose(long j);

    private static native android.media.tv.TvStreamConfig[] nativeGetStreamConfigs(long j, int i, int i2);

    private native long nativeOpen(android.os.MessageQueue messageQueue);

    private static native int nativeRemoveStream(long j, int i, int i2);

    private static native int nativeSetTvMessageEnabled(long j, int i, int i2, int i3, boolean z);

    public TvInputHal(com.android.server.tv.TvInputHal.Callback callback) {
        this.mCallback = callback;
    }

    public void init() {
        synchronized (this.mLock) {
            this.mPtr = nativeOpen(this.mHandler.getLooper().getQueue());
        }
    }

    public int addOrUpdateStream(int deviceId, android.view.Surface surface, android.media.tv.TvStreamConfig streamConfig) {
        synchronized (this.mLock) {
            if (this.mPtr == 0) {
                return -1;
            }
            int generation = this.mStreamConfigGenerations.get(deviceId, 0);
            if (generation != streamConfig.getGeneration()) {
                return -2;
            }
            return nativeAddOrUpdateStream(this.mPtr, deviceId, streamConfig.getStreamId(), surface) == 0 ? 0 : -3;
        }
    }

    public int setTvMessageEnabled(int deviceId, android.media.tv.TvStreamConfig streamConfig, int type, boolean enabled) {
        synchronized (this.mLock) {
            if (this.mPtr == 0) {
                return -1;
            }
            int generation = this.mStreamConfigGenerations.get(deviceId, 0);
            if (generation != streamConfig.getGeneration()) {
                return -2;
            }
            return nativeSetTvMessageEnabled(this.mPtr, deviceId, streamConfig.getStreamId(), type, enabled) == 0 ? 0 : -3;
        }
    }

    public int removeStream(int deviceId, android.media.tv.TvStreamConfig streamConfig) {
        synchronized (this.mLock) {
            if (this.mPtr == 0) {
                return -1;
            }
            int generation = this.mStreamConfigGenerations.get(deviceId, 0);
            if (generation != streamConfig.getGeneration()) {
                return -2;
            }
            return nativeRemoveStream(this.mPtr, deviceId, streamConfig.getStreamId()) == 0 ? 0 : -3;
        }
    }

    public void close() {
        synchronized (this.mLock) {
            if (this.mPtr != 0) {
                nativeClose(this.mPtr);
            }
        }
    }

    private void retrieveStreamConfigsLocked(int deviceId) {
        int generation = this.mStreamConfigGenerations.get(deviceId, 0) + 1;
        this.mStreamConfigs.put(deviceId, nativeGetStreamConfigs(this.mPtr, deviceId, generation));
        this.mStreamConfigGenerations.put(deviceId, generation);
    }

    private void deviceAvailableFromNative(android.media.tv.TvInputHardwareInfo info) {
        this.mHandler.obtainMessage(1, info).sendToTarget();
    }

    private void deviceUnavailableFromNative(int deviceId) {
        this.mHandler.obtainMessage(2, deviceId, 0).sendToTarget();
    }

    private void streamConfigsChangedFromNative(int deviceId, int cableConnectionStatus) {
        this.mHandler.obtainMessage(3, deviceId, cableConnectionStatus).sendToTarget();
    }

    private void firstFrameCapturedFromNative(int deviceId, int streamId) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3, deviceId, streamId));
    }

    private void tvMessageReceivedFromNative(int deviceId, int type, android.os.Bundle data) {
        this.mHandler.obtainMessage(5, deviceId, type, data).sendToTarget();
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(android.os.Message msg) {
        android.media.tv.TvStreamConfig[] configs;
        android.media.tv.TvStreamConfig[] configs2;
        switch (msg.what) {
            case 1:
                android.media.tv.TvInputHardwareInfo info = (android.media.tv.TvInputHardwareInfo) msg.obj;
                synchronized (this.mLock) {
                    retrieveStreamConfigsLocked(info.getDeviceId());
                    configs = this.mStreamConfigs.get(info.getDeviceId());
                    break;
                }
                this.mCallback.onDeviceAvailable(info, configs);
                return true;
            case 2:
                this.mCallback.onDeviceUnavailable(msg.arg1);
                return true;
            case 3:
                int deviceId = msg.arg1;
                int cableConnectionStatus = msg.arg2;
                synchronized (this.mLock) {
                    retrieveStreamConfigsLocked(deviceId);
                    configs2 = this.mStreamConfigs.get(deviceId);
                    break;
                }
                this.mCallback.onStreamConfigurationChanged(deviceId, configs2, cableConnectionStatus);
                return true;
            case 4:
                int deviceId2 = msg.arg1;
                int streamId = msg.arg2;
                this.mCallback.onFirstFrameCaptured(deviceId2, streamId);
                return true;
            case 5:
                int deviceId3 = msg.arg1;
                int type = msg.arg2;
                android.os.Bundle data = (android.os.Bundle) msg.obj;
                this.mCallback.onTvMessage(deviceId3, type, data);
                return true;
            default:
                android.util.Slog.e(TAG, "Unknown event: " + msg);
                return false;
        }
    }
}
