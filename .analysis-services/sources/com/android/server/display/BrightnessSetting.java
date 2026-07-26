package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class BrightnessSetting {
    private static final int MSG_BRIGHTNESS_CHANGED = 1;
    private static final java.lang.String TAG = "BrightnessSetting";
    private float mBrightness;
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper()) { // from class: com.android.server.display.BrightnessSetting.1
        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                float brightnessVal = java.lang.Float.intBitsToFloat(msg.arg1);
                com.android.server.display.BrightnessSetting.this.notifyListeners(brightnessVal);
            }
        }
    };
    private final java.util.concurrent.CopyOnWriteArraySet<com.android.server.display.BrightnessSetting.BrightnessSettingListener> mListeners = new java.util.concurrent.CopyOnWriteArraySet<>();
    private final com.android.server.display.LogicalDisplay mLogicalDisplay;
    private final com.android.server.display.PersistentDataStore mPersistentDataStore;
    private final com.android.server.display.DisplayManagerService.SyncRoot mSyncRoot;
    private int mUserSerial;

    public interface BrightnessSettingListener {
        void onBrightnessChanged(float f);
    }

    BrightnessSetting(int userSerial, com.android.server.display.PersistentDataStore persistentDataStore, com.android.server.display.LogicalDisplay logicalDisplay, com.android.server.display.DisplayManagerService.SyncRoot syncRoot) {
        this.mPersistentDataStore = persistentDataStore;
        this.mLogicalDisplay = logicalDisplay;
        this.mUserSerial = userSerial;
        this.mBrightness = this.mPersistentDataStore.getBrightness(this.mLogicalDisplay.getPrimaryDisplayDeviceLocked(), userSerial);
        this.mSyncRoot = syncRoot;
    }

    public float getBrightness() {
        float f;
        synchronized (this.mSyncRoot) {
            f = this.mBrightness;
        }
        return f;
    }

    public void registerListener(com.android.server.display.BrightnessSetting.BrightnessSettingListener l) {
        if (this.mListeners.contains(l)) {
            android.util.Slog.wtf(TAG, "Duplicate Listener added");
        }
        this.mListeners.add(l);
    }

    public void unregisterListener(com.android.server.display.BrightnessSetting.BrightnessSettingListener l) {
        this.mListeners.remove(l);
    }

    public void setUserSerial(int userSerial) {
        this.mUserSerial = userSerial;
    }

    public void setBrightness(float brightness) {
        if (java.lang.Float.isNaN(brightness)) {
            android.util.Slog.w(TAG, "Attempting to set invalid brightness");
            return;
        }
        synchronized (this.mSyncRoot) {
            if (brightness != this.mBrightness) {
                this.mPersistentDataStore.setBrightness(this.mLogicalDisplay.getPrimaryDisplayDeviceLocked(), brightness, this.mUserSerial);
            }
            this.mBrightness = brightness;
            int toSend = java.lang.Float.floatToIntBits(this.mBrightness);
            android.os.Message msg = this.mHandler.obtainMessage(1, toSend, 0);
            this.mHandler.sendMessage(msg);
        }
    }

    public void setBrightnessNoNotify(float brightness) {
        if (java.lang.Float.isNaN(brightness)) {
            android.util.Slog.w(TAG, "Attempting to init invalid brightness");
            return;
        }
        synchronized (this.mSyncRoot) {
            if (brightness != this.mBrightness) {
                this.mPersistentDataStore.setBrightness(this.mLogicalDisplay.getPrimaryDisplayDeviceLocked(), brightness, this.mUserSerial);
            }
            this.mBrightness = brightness;
        }
    }

    public float getBrightnessNitsForDefaultDisplay() {
        return this.mPersistentDataStore.getBrightnessNitsForDefaultDisplay();
    }

    public void setBrightnessNitsForDefaultDisplay(float nits) {
        this.mPersistentDataStore.setBrightnessNitsForDefaultDisplay(nits);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListeners(float brightness) {
        for (com.android.server.display.BrightnessSetting.BrightnessSettingListener l : this.mListeners) {
            l.onBrightnessChanged(brightness);
        }
    }
}
