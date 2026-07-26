package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintGestureDispatcher extends android.hardware.fingerprint.IFingerprintClientActiveCallback.Stub implements android.os.Handler.Callback {
    private static final java.lang.String LOG_TAG = "FingerprintGestureDispatcher";
    private static final int MSG_REGISTER = 1;
    private static final int MSG_UNREGISTER = 2;
    private final java.util.List<com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient> mCapturingClients;
    private final android.hardware.fingerprint.IFingerprintService mFingerprintService;
    private final android.os.Handler mHandler;
    private final boolean mHardwareSupportsGestures;
    private final java.lang.Object mLock;
    private boolean mRegisteredReadOnlyExceptInHandler;

    public interface FingerprintGestureClient {
        boolean isCapturingFingerprintGestures();

        void onFingerprintGesture(int i);

        void onFingerprintGestureDetectionActiveChanged(boolean z);
    }

    public FingerprintGestureDispatcher(android.hardware.fingerprint.IFingerprintService fingerprintService, android.content.res.Resources resources, java.lang.Object lock) {
        this.mCapturingClients = new java.util.ArrayList(0);
        this.mFingerprintService = fingerprintService;
        this.mHardwareSupportsGestures = resources.getBoolean(android.R.bool.config_enableWifiDisplay);
        this.mLock = lock;
        this.mHandler = new android.os.Handler(this);
    }

    public FingerprintGestureDispatcher(android.hardware.fingerprint.IFingerprintService fingerprintService, android.content.res.Resources resources, java.lang.Object lock, android.os.Handler handler) {
        this.mCapturingClients = new java.util.ArrayList(0);
        this.mFingerprintService = fingerprintService;
        this.mHardwareSupportsGestures = resources.getBoolean(android.R.bool.config_enableWifiDisplay);
        this.mLock = lock;
        this.mHandler = handler;
    }

    public void updateClientList(java.util.List<? extends com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient> clientList) {
        if (this.mHardwareSupportsGestures) {
            synchronized (this.mLock) {
                this.mCapturingClients.clear();
                for (int i = 0; i < clientList.size(); i++) {
                    com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient client = clientList.get(i);
                    if (client.isCapturingFingerprintGestures()) {
                        this.mCapturingClients.add(client);
                    }
                }
                if (this.mCapturingClients.isEmpty()) {
                    if (this.mRegisteredReadOnlyExceptInHandler) {
                        this.mHandler.obtainMessage(2).sendToTarget();
                    }
                } else if (!this.mRegisteredReadOnlyExceptInHandler) {
                    this.mHandler.obtainMessage(1).sendToTarget();
                }
            }
        }
    }

    public void onClientActiveChanged(boolean nonGestureFingerprintClientActive) {
        if (this.mHardwareSupportsGestures) {
            synchronized (this.mLock) {
                for (int i = 0; i < this.mCapturingClients.size(); i++) {
                    this.mCapturingClients.get(i).onFingerprintGestureDetectionActiveChanged(!nonGestureFingerprintClientActive);
                }
            }
        }
    }

    public boolean isFingerprintGestureDetectionAvailable() {
        if (!this.mHardwareSupportsGestures) {
            return false;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return !this.mFingerprintService.isClientActive();
        } catch (android.os.RemoteException e) {
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean onFingerprintGesture(int fingerprintKeyCode) {
        int idForFingerprintGestureManager;
        synchronized (this.mLock) {
            if (this.mCapturingClients.isEmpty()) {
                return false;
            }
            switch (fingerprintKeyCode) {
                case 280:
                    idForFingerprintGestureManager = 4;
                    break;
                case 281:
                    idForFingerprintGestureManager = 8;
                    break;
                case com.android.internal.util.FrameworkStatsLog.DISPLAY_WAKE_REPORTED /* 282 */:
                    idForFingerprintGestureManager = 2;
                    break;
                case 283:
                    idForFingerprintGestureManager = 1;
                    break;
                default:
                    return false;
            }
            java.util.List<com.android.server.accessibility.FingerprintGestureDispatcher.FingerprintGestureClient> clientList = new java.util.ArrayList<>(this.mCapturingClients);
            for (int i = 0; i < clientList.size(); i++) {
                clientList.get(i).onFingerprintGesture(idForFingerprintGestureManager);
            }
            return true;
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(android.os.Message message) {
        long identity;
        if (message.what == 1) {
            identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    this.mFingerprintService.addClientActiveCallback(this);
                    this.mRegisteredReadOnlyExceptInHandler = true;
                } finally {
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(LOG_TAG, "Failed to register for fingerprint activity callbacks");
            }
            return false;
        }
        if (message.what != 2) {
            android.util.Slog.e(LOG_TAG, "Unknown message: " + message.what);
            return false;
        }
        identity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mFingerprintService.removeClientActiveCallback(this);
            } catch (android.os.RemoteException e2) {
                android.util.Slog.e(LOG_TAG, "Failed to unregister for fingerprint activity callbacks");
            }
            this.mRegisteredReadOnlyExceptInHandler = false;
            return true;
        } finally {
        }
    }
}
