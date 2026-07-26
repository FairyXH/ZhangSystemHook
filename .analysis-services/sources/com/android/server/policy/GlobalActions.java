package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
class GlobalActions implements com.android.server.policy.GlobalActionsProvider.GlobalActionsListener {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "GlobalActions";
    private final android.content.Context mContext;
    private boolean mDeviceProvisioned;
    private boolean mGlobalActionsAvailable;
    private boolean mKeyguardShowing;
    private com.android.server.policy.LegacyGlobalActions mLegacyGlobalActions;
    private boolean mShowing;
    private final com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs mWindowManagerFuncs;
    private final java.lang.Runnable mShowTimeout = new java.lang.Runnable() { // from class: com.android.server.policy.GlobalActions.1
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.policy.GlobalActions.this.ensureLegacyCreated();
            com.android.server.policy.GlobalActions.this.mLegacyGlobalActions.showDialog(com.android.server.policy.GlobalActions.this.mKeyguardShowing, com.android.server.policy.GlobalActions.this.mDeviceProvisioned);
        }
    };
    private final android.os.Handler mHandler = new android.os.Handler();
    private final com.android.server.policy.GlobalActionsProvider mGlobalActionsProvider = (com.android.server.policy.GlobalActionsProvider) com.android.server.LocalServices.getService(com.android.server.policy.GlobalActionsProvider.class);

    public GlobalActions(android.content.Context context, com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
        this.mContext = context;
        this.mWindowManagerFuncs = windowManagerFuncs;
        if (this.mGlobalActionsProvider != null) {
            this.mGlobalActionsProvider.setGlobalActionsListener(this);
        } else {
            android.util.Slog.i(TAG, "No GlobalActionsProvider found, defaulting to LegacyGlobalActions");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureLegacyCreated() {
        if (this.mLegacyGlobalActions != null) {
            return;
        }
        this.mLegacyGlobalActions = new com.android.server.policy.LegacyGlobalActions(this.mContext, this.mWindowManagerFuncs, new java.lang.Runnable() { // from class: com.android.server.policy.GlobalActions$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.onGlobalActionsDismissed();
            }
        });
    }

    public void showDialog(boolean keyguardShowing, boolean deviceProvisioned) {
        if (this.mGlobalActionsProvider != null && this.mGlobalActionsProvider.isGlobalActionsDisabled()) {
            return;
        }
        this.mKeyguardShowing = keyguardShowing;
        this.mDeviceProvisioned = deviceProvisioned;
        this.mShowing = true;
        if (this.mGlobalActionsAvailable) {
            this.mHandler.postDelayed(this.mShowTimeout, 5000L);
            this.mGlobalActionsProvider.showGlobalActions();
        } else {
            ensureLegacyCreated();
            this.mLegacyGlobalActions.showDialog(this.mKeyguardShowing, this.mDeviceProvisioned);
        }
    }

    @Override // com.android.server.policy.GlobalActionsProvider.GlobalActionsListener
    public void onGlobalActionsShown() {
        this.mHandler.removeCallbacks(this.mShowTimeout);
    }

    @Override // com.android.server.policy.GlobalActionsProvider.GlobalActionsListener
    public void onGlobalActionsDismissed() {
        this.mShowing = false;
    }

    @Override // com.android.server.policy.GlobalActionsProvider.GlobalActionsListener
    public void onGlobalActionsAvailableChanged(boolean available) {
        this.mGlobalActionsAvailable = available;
        if (this.mShowing && !this.mGlobalActionsAvailable) {
            this.mHandler.removeCallbacks(this.mShowTimeout);
            ensureLegacyCreated();
            this.mLegacyGlobalActions.showDialog(this.mKeyguardShowing, this.mDeviceProvisioned);
        }
    }
}
