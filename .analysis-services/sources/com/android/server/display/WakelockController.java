package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public final class WakelockController {
    static final int WAKE_LOCK_MAX = 5;
    public static final int WAKE_LOCK_PROXIMITY_DEBOUNCE = 3;
    public static final int WAKE_LOCK_PROXIMITY_NEGATIVE = 2;
    public static final int WAKE_LOCK_PROXIMITY_POSITIVE = 1;
    public static final int WAKE_LOCK_STATE_CHANGED = 4;
    public static final int WAKE_LOCK_UNFINISHED_BUSINESS = 5;
    private final int mDisplayId;
    private final android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks mDisplayPowerCallbacks;
    private boolean mHasProximityDebounced;
    private boolean mIsProximityNegativeAcquired;
    private boolean mIsProximityPositiveAcquired;
    private boolean mOnStateChangedPending;
    private final java.lang.String mSuspendBlockerIdOnStateChanged;
    private final java.lang.String mSuspendBlockerIdProxDebounce;
    private final java.lang.String mSuspendBlockerIdProxNegative;
    private final java.lang.String mSuspendBlockerIdProxPositive;
    private final java.lang.String mSuspendBlockerIdUnfinishedBusiness;
    private final java.lang.String mTag;
    private boolean mUnfinishedBusiness;
    private com.android.server.display.IWakelockControllerExt mWLExtImpl = (com.android.server.display.IWakelockControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IWakelockControllerExt.class).base(this).create();
    private static final java.lang.String TAG = "WakelockController";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface WAKE_LOCK_TYPE {
    }

    public WakelockController(int displayId, android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks callbacks) {
        this.mDisplayId = displayId;
        this.mTag = "WakelockController[" + this.mDisplayId + "]";
        this.mDisplayPowerCallbacks = callbacks;
        this.mSuspendBlockerIdUnfinishedBusiness = "[" + displayId + "]unfinished business";
        this.mSuspendBlockerIdOnStateChanged = "[" + displayId + "]on state changed";
        this.mSuspendBlockerIdProxPositive = "[" + displayId + "]prox positive";
        this.mSuspendBlockerIdProxNegative = "[" + displayId + "]prox negative";
        this.mSuspendBlockerIdProxDebounce = "[" + displayId + "]prox debounce";
    }

    public boolean acquireWakelock(int wakelock) {
        return acquireWakelockInternal(wakelock);
    }

    public boolean releaseWakelock(int wakelock) {
        return releaseWakelockInternal(wakelock);
    }

    public void releaseAll() {
        for (int i = 1; i <= 5; i++) {
            releaseWakelockInternal(i);
        }
    }

    private boolean acquireWakelockInternal(int wakelock) {
        switch (wakelock) {
            case 1:
                return acquireProxPositiveSuspendBlocker();
            case 2:
                return acquireProxNegativeSuspendBlocker();
            case 3:
                return acquireProxDebounceSuspendBlocker();
            case 4:
                return acquireStateChangedSuspendBlocker();
            case 5:
                return acquireUnfinishedBusinessSuspendBlocker();
            default:
                if (this.mWLExtImpl.acquireWakelockCustom(wakelock, this.mDisplayPowerCallbacks)) {
                    return true;
                }
                throw new java.lang.RuntimeException("Invalid wakelock attempted to be acquired");
        }
    }

    private boolean releaseWakelockInternal(int wakelock) {
        switch (wakelock) {
            case 1:
                return releaseProxPositiveSuspendBlocker();
            case 2:
                return releaseProxNegativeSuspendBlocker();
            case 3:
                return releaseProxDebounceSuspendBlocker();
            case 4:
                return releaseStateChangedSuspendBlocker();
            case 5:
                return releaseUnfinishedBusinessSuspendBlocker();
            default:
                if (this.mWLExtImpl.releaseWakelockCustom(wakelock, this.mDisplayPowerCallbacks)) {
                    return true;
                }
                throw new java.lang.RuntimeException("Invalid wakelock attempted to be released");
        }
    }

    private boolean acquireProxPositiveSuspendBlocker() {
        if (!this.mIsProximityPositiveAcquired) {
            this.mDisplayPowerCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdProxPositive);
            this.mIsProximityPositiveAcquired = true;
            return true;
        }
        return false;
    }

    private boolean acquireStateChangedSuspendBlocker() {
        if (!this.mOnStateChangedPending) {
            if (DEBUG) {
                android.util.Slog.d(this.mTag, "State Changed...");
            }
            this.mDisplayPowerCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdOnStateChanged);
            this.mOnStateChangedPending = true;
            return true;
        }
        return false;
    }

    private boolean releaseStateChangedSuspendBlocker() {
        if (!this.mOnStateChangedPending) {
            return false;
        }
        this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdOnStateChanged);
        this.mOnStateChangedPending = false;
        return true;
    }

    private boolean acquireUnfinishedBusinessSuspendBlocker() {
        if (!this.mUnfinishedBusiness) {
            if (DEBUG) {
                android.util.Slog.d(this.mTag, "Unfinished business...");
            }
            this.mDisplayPowerCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdUnfinishedBusiness);
            this.mUnfinishedBusiness = true;
            return true;
        }
        return false;
    }

    private boolean releaseUnfinishedBusinessSuspendBlocker() {
        if (!this.mUnfinishedBusiness) {
            return false;
        }
        if (DEBUG) {
            android.util.Slog.d(this.mTag, "Finished business...");
        }
        this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdUnfinishedBusiness);
        this.mUnfinishedBusiness = false;
        return true;
    }

    private boolean releaseProxPositiveSuspendBlocker() {
        if (!this.mIsProximityPositiveAcquired) {
            return false;
        }
        this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxPositive);
        this.mIsProximityPositiveAcquired = false;
        return true;
    }

    private boolean acquireProxNegativeSuspendBlocker() {
        if (!this.mIsProximityNegativeAcquired) {
            this.mDisplayPowerCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdProxNegative);
            this.mIsProximityNegativeAcquired = true;
            return true;
        }
        return false;
    }

    private boolean releaseProxNegativeSuspendBlocker() {
        if (!this.mIsProximityNegativeAcquired) {
            return false;
        }
        this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxNegative);
        this.mIsProximityNegativeAcquired = false;
        return true;
    }

    private boolean acquireProxDebounceSuspendBlocker() {
        if (!this.mHasProximityDebounced) {
            this.mDisplayPowerCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdProxDebounce);
            this.mHasProximityDebounced = true;
            return true;
        }
        return false;
    }

    private boolean releaseProxDebounceSuspendBlocker() {
        if (!this.mHasProximityDebounced) {
            return false;
        }
        this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxDebounce);
        this.mHasProximityDebounced = false;
        return true;
    }

    public java.lang.Runnable getOnProximityPositiveRunnable() {
        return new java.lang.Runnable() { // from class: com.android.server.display.WakelockController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getOnProximityPositiveRunnable$0();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOnProximityPositiveRunnable$0() {
        if (this.mIsProximityPositiveAcquired) {
            this.mIsProximityPositiveAcquired = false;
            this.mDisplayPowerCallbacks.onProximityPositive();
            this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxPositive);
        }
    }

    public java.lang.Runnable getOnStateChangedRunnable() {
        return new java.lang.Runnable() { // from class: com.android.server.display.WakelockController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getOnStateChangedRunnable$1();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOnStateChangedRunnable$1() {
        if (this.mOnStateChangedPending) {
            this.mOnStateChangedPending = false;
            this.mDisplayPowerCallbacks.onStateChanged();
            this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdOnStateChanged);
        }
    }

    public java.lang.Runnable getOnProximityNegativeRunnable() {
        return new java.lang.Runnable() { // from class: com.android.server.display.WakelockController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getOnProximityNegativeRunnable$2();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOnProximityNegativeRunnable$2() {
        if (this.mIsProximityNegativeAcquired) {
            this.mIsProximityNegativeAcquired = false;
            this.mDisplayPowerCallbacks.onProximityNegative();
            this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxNegative);
        }
    }

    public void dumpLocal(java.io.PrintWriter pw) {
        pw.println("WakelockController State:");
        pw.println("  mDisplayId=" + this.mDisplayId);
        pw.println("  mUnfinishedBusiness=" + hasUnfinishedBusiness());
        pw.println("  mOnStateChangePending=" + isOnStateChangedPending());
        pw.println("  mOnProximityPositiveMessages=" + isProximityPositiveAcquired());
        pw.println("  mOnProximityNegativeMessages=" + isProximityNegativeAcquired());
    }

    java.lang.String getSuspendBlockerUnfinishedBusinessId() {
        return this.mSuspendBlockerIdUnfinishedBusiness;
    }

    java.lang.String getSuspendBlockerOnStateChangedId() {
        return this.mSuspendBlockerIdOnStateChanged;
    }

    java.lang.String getSuspendBlockerProxPositiveId() {
        return this.mSuspendBlockerIdProxPositive;
    }

    java.lang.String getSuspendBlockerProxNegativeId() {
        return this.mSuspendBlockerIdProxNegative;
    }

    java.lang.String getSuspendBlockerProxDebounceId() {
        return this.mSuspendBlockerIdProxDebounce;
    }

    boolean hasUnfinishedBusiness() {
        return this.mUnfinishedBusiness;
    }

    boolean isOnStateChangedPending() {
        return this.mOnStateChangedPending;
    }

    boolean isProximityPositiveAcquired() {
        return this.mIsProximityPositiveAcquired;
    }

    boolean isProximityNegativeAcquired() {
        return this.mIsProximityNegativeAcquired;
    }

    boolean hasProximitySensorDebounced() {
        return this.mHasProximityDebounced;
    }
}
