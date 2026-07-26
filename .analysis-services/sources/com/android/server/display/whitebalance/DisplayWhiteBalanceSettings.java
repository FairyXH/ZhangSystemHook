package com.android.server.display.whitebalance;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayWhiteBalanceSettings implements com.android.server.display.color.ColorDisplayService.DisplayWhiteBalanceListener {
    private static final int MSG_SET_ACTIVE = 1;
    protected static final java.lang.String TAG = "DisplayWhiteBalanceSettings";
    private boolean mActive;
    private com.android.server.display.whitebalance.DisplayWhiteBalanceController.Callbacks mCallbacks;
    private final com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal mCdsi;
    private final android.content.Context mContext;
    private boolean mEnabled;
    private final android.os.Handler mHandler;
    protected boolean mLoggingEnabled;

    public DisplayWhiteBalanceSettings(android.content.Context context, android.os.Handler handler) {
        validateArguments(context, handler);
        this.mLoggingEnabled = false;
        this.mContext = context;
        this.mHandler = new com.android.server.display.whitebalance.DisplayWhiteBalanceSettings.DisplayWhiteBalanceSettingsHandler(handler.getLooper());
        this.mCallbacks = null;
        this.mCdsi = (com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal) com.android.server.LocalServices.getService(com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal.class);
        setEnabled(this.mCdsi.isDisplayWhiteBalanceEnabled());
        boolean isActive = this.mCdsi.setDisplayWhiteBalanceListener(this);
        setActive(isActive);
    }

    public boolean setCallbacks(com.android.server.display.whitebalance.DisplayWhiteBalanceController.Callbacks callbacks) {
        if (this.mCallbacks == callbacks) {
            return false;
        }
        this.mCallbacks = callbacks;
        return true;
    }

    public boolean setLoggingEnabled(boolean loggingEnabled) {
        if (this.mLoggingEnabled == loggingEnabled) {
            return false;
        }
        this.mLoggingEnabled = loggingEnabled;
        return true;
    }

    public boolean isEnabled() {
        return this.mEnabled && this.mActive;
    }

    public void dump(java.io.PrintWriter writer) {
        writer.println(TAG);
        writer.println("  mLoggingEnabled=" + this.mLoggingEnabled);
        writer.println("  mContext=" + this.mContext);
        writer.println("  mHandler=" + this.mHandler);
        writer.println("  mEnabled=" + this.mEnabled);
        writer.println("  mActive=" + this.mActive);
        writer.println("  mCallbacks=" + this.mCallbacks);
    }

    @Override // com.android.server.display.color.ColorDisplayService.DisplayWhiteBalanceListener
    public void onDisplayWhiteBalanceStatusChanged(boolean z) {
        this.mHandler.obtainMessage(1, z ? 1 : 0, 0).sendToTarget();
    }

    private void validateArguments(android.content.Context context, android.os.Handler handler) {
        java.util.Objects.requireNonNull(context, "context must not be null");
        java.util.Objects.requireNonNull(handler, "handler must not be null");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnabled(boolean enabled) {
        if (this.mEnabled == enabled) {
            return;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Setting: " + enabled);
        }
        this.mEnabled = enabled;
        if (this.mCallbacks != null) {
            this.mCallbacks.updateWhiteBalance();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setActive(boolean active) {
        if (this.mActive == active) {
            return;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Active: " + active);
        }
        this.mActive = active;
        if (this.mCallbacks != null) {
            this.mCallbacks.updateWhiteBalance();
        }
    }

    private final class DisplayWhiteBalanceSettingsHandler extends android.os.Handler {
        DisplayWhiteBalanceSettingsHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.display.whitebalance.DisplayWhiteBalanceSettings.this.setActive(msg.arg1 != 0);
                    com.android.server.display.whitebalance.DisplayWhiteBalanceSettings.this.setEnabled(com.android.server.display.whitebalance.DisplayWhiteBalanceSettings.this.mCdsi.isDisplayWhiteBalanceEnabled());
                    break;
            }
        }
    }
}
