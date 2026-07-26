package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public class SideFpsEventHandler implements android.view.View.OnClickListener {
    private static final int DEBOUNCE_DELAY_MILLIS = 500;
    private static final java.lang.String TAG = "SideFpsEventHandler";
    private final android.view.accessibility.AccessibilityManager mAccessibilityManager;
    private int mBiometricState;
    private final android.content.Context mContext;
    private com.android.server.policy.SideFpsToast mDialog;
    private com.android.server.policy.SideFpsEventHandler.DialogProvider mDialogProvider;
    private final int mDismissDialogTimeout;
    private android.hardware.fingerprint.FingerprintManager mFingerprintManager;
    private final android.os.Handler mHandler;
    private long mLastPowerPressTime;
    private final android.os.PowerManager mPowerManager;
    private final java.util.concurrent.atomic.AtomicBoolean mSideFpsEventHandlerReady;
    private final java.lang.Runnable mTurnOffDialog;

    interface DialogProvider {
        com.android.server.policy.SideFpsToast provideDialog(android.content.Context context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        dismissDialog("mTurnOffDialog");
    }

    SideFpsEventHandler(android.content.Context context, android.os.Handler handler, android.os.PowerManager powerManager) {
        this(context, handler, powerManager, new com.android.server.policy.SideFpsEventHandler.DialogProvider() { // from class: com.android.server.policy.SideFpsEventHandler$$ExternalSyntheticLambda1
            @Override // com.android.server.policy.SideFpsEventHandler.DialogProvider
            public final com.android.server.policy.SideFpsToast provideDialog(android.content.Context context2) {
                return com.android.server.policy.SideFpsEventHandler.lambda$new$1(context2);
            }
        });
    }

    static /* synthetic */ com.android.server.policy.SideFpsToast lambda$new$1(android.content.Context ctx) {
        com.android.server.policy.SideFpsToast dialog = new com.android.server.policy.SideFpsToast(ctx);
        dialog.getWindow().setType(2017);
        dialog.requestWindowFeature(1);
        return dialog;
    }

    SideFpsEventHandler(android.content.Context context, android.os.Handler handler, android.os.PowerManager powerManager, com.android.server.policy.SideFpsEventHandler.DialogProvider provider) {
        this.mTurnOffDialog = new java.lang.Runnable() { // from class: com.android.server.policy.SideFpsEventHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        this.mContext = context;
        this.mHandler = handler;
        this.mAccessibilityManager = (android.view.accessibility.AccessibilityManager) this.mContext.getSystemService(android.view.accessibility.AccessibilityManager.class);
        this.mPowerManager = powerManager;
        this.mBiometricState = 0;
        this.mSideFpsEventHandlerReady = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mDialogProvider = provider;
        context.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.policy.SideFpsEventHandler.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (com.android.server.policy.SideFpsEventHandler.this.mDialog != null) {
                    com.android.server.policy.SideFpsEventHandler.this.mDialog.dismiss();
                    com.android.server.policy.SideFpsEventHandler.this.mDialog = null;
                }
            }
        }, new android.content.IntentFilter("android.intent.action.SCREEN_OFF"));
        this.mDismissDialogTimeout = context.getResources().getInteger(android.R.integer.config_satellite_stay_at_listening_from_sending_millis);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(android.view.View v) {
        goToSleep(this.mLastPowerPressTime);
    }

    public void notifyPowerPressed() {
        android.util.Log.i(TAG, "notifyPowerPressed");
        if (this.mFingerprintManager == null && this.mSideFpsEventHandlerReady.get()) {
            this.mFingerprintManager = (android.hardware.fingerprint.FingerprintManager) this.mContext.getSystemService(android.hardware.fingerprint.FingerprintManager.class);
        }
        if (this.mFingerprintManager == null) {
            return;
        }
        this.mFingerprintManager.onPowerPressed();
    }

    public boolean shouldConsumeSinglePress(final long eventTime) {
        if (!this.mSideFpsEventHandlerReady.get()) {
            return false;
        }
        switch (this.mBiometricState) {
            case 1:
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.policy.SideFpsEventHandler$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$shouldConsumeSinglePress$2(eventTime);
                    }
                });
                break;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$shouldConsumeSinglePress$2(long eventTime) {
        if (this.mHandler.hasCallbacks(this.mTurnOffDialog)) {
            android.util.Log.v(TAG, "Detected a tap to turn off dialog, ignoring");
            this.mHandler.removeCallbacks(this.mTurnOffDialog);
        }
        showDialog(eventTime, "Enroll Power Press");
        if (!this.mAccessibilityManager.isEnabled()) {
            this.mHandler.postDelayed(this.mTurnOffDialog, this.mDismissDialogTimeout);
        }
    }

    private void goToSleep(long eventTime) {
        this.mPowerManager.goToSleep(eventTime, 4, 0);
    }

    public void onFingerprintSensorReady() {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (!pm.hasSystemFeature("android.hardware.fingerprint")) {
            return;
        }
        final android.hardware.fingerprint.FingerprintManager fingerprintManager = (android.hardware.fingerprint.FingerprintManager) this.mContext.getSystemService(android.hardware.fingerprint.FingerprintManager.class);
        fingerprintManager.addAuthenticatorsRegisteredCallback(new android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback.Stub() { // from class: com.android.server.policy.SideFpsEventHandler.2
            public void onAllAuthenticatorsRegistered(java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> sensors) {
                if (fingerprintManager.isPowerbuttonFps()) {
                    fingerprintManager.registerBiometricStateListener(new com.android.server.policy.SideFpsEventHandler.AnonymousClass2.AnonymousClass1());
                    com.android.server.policy.SideFpsEventHandler.this.mSideFpsEventHandlerReady.set(true);
                }
            }

            /* JADX INFO: renamed from: com.android.server.policy.SideFpsEventHandler$2$1, reason: invalid class name */
            class AnonymousClass1 extends android.hardware.biometrics.BiometricStateListener {
                private java.lang.Runnable mStateRunnable = null;

                AnonymousClass1() {
                }

                public void onStateChanged(final int newState) {
                    android.util.Log.d(com.android.server.policy.SideFpsEventHandler.TAG, "onStateChanged : " + newState);
                    if (this.mStateRunnable != null) {
                        com.android.server.policy.SideFpsEventHandler.this.mHandler.removeCallbacks(this.mStateRunnable);
                        this.mStateRunnable = null;
                    }
                    if (newState == 0) {
                        this.mStateRunnable = new java.lang.Runnable() { // from class: com.android.server.policy.SideFpsEventHandler$2$1$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onStateChanged$0(newState);
                            }
                        };
                        com.android.server.policy.SideFpsEventHandler.this.mHandler.postDelayed(this.mStateRunnable, 500L);
                        com.android.server.policy.SideFpsEventHandler.this.dismissDialog("STATE_IDLE");
                        return;
                    }
                    com.android.server.policy.SideFpsEventHandler.this.mBiometricState = newState;
                }

                /* JADX INFO: Access modifiers changed from: private */
                public /* synthetic */ void lambda$onStateChanged$0(int newState) {
                    com.android.server.policy.SideFpsEventHandler.this.mBiometricState = newState;
                }

                public void onBiometricAction(int action) {
                    android.util.Log.d(com.android.server.policy.SideFpsEventHandler.TAG, "onBiometricAction " + action);
                    if (com.android.server.policy.SideFpsEventHandler.this.mAccessibilityManager != null && com.android.server.policy.SideFpsEventHandler.this.mAccessibilityManager.isEnabled()) {
                        com.android.server.policy.SideFpsEventHandler.this.dismissDialog("mTurnOffDialog");
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissDialog(java.lang.String reason) {
        android.util.Log.d(TAG, "Dismissing dialog with reason: " + reason);
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }

    private void showDialog(long time, java.lang.String reason) {
        android.util.Log.d(TAG, "Showing dialog with reason: " + reason);
        if (this.mDialog != null && this.mDialog.isShowing()) {
            android.util.Log.d(TAG, "Ignoring show dialog");
            return;
        }
        this.mDialog = this.mDialogProvider.provideDialog(this.mContext);
        this.mLastPowerPressTime = time;
        this.mDialog.show();
        this.mDialog.setOnClickListener(this);
        if (this.mAccessibilityManager.isEnabled()) {
            this.mDialog.addAccessibilityDelegate();
        }
    }
}
