package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class AttentionDetector {
    private static final boolean DEBUG = false;
    static final long DEFAULT_POST_DIM_CHECK_DURATION_MILLIS = 7000;
    static final long DEFAULT_PRE_DIM_CHECK_DURATION_MILLIS = 2000;
    static final java.lang.String KEY_MAX_EXTENSION_MILLIS = "max_extension_millis";
    static final java.lang.String KEY_POST_DIM_CHECK_DURATION_MILLIS = "post_dim_check_duration_millis";
    static final java.lang.String KEY_PRE_DIM_CHECK_DURATION_MILLIS = "pre_dim_check_duration_millis";
    private static final java.lang.String TAG = "AttentionDetector";
    protected android.attention.AttentionManagerInternal mAttentionManager;
    com.android.server.power.AttentionDetector.AttentionCallbackInternalImpl mCallback;
    protected android.content.ContentResolver mContentResolver;
    private android.content.Context mContext;
    protected long mDefaultMaximumExtensionMillis;
    private long mEffectivePostDimTimeoutMillis;
    private boolean mIsSettingEnabled;
    private long mLastActedOnNextScreenDimming;
    private long mLastUserActivityTime;
    private final java.lang.Object mLock;
    private long mMaximumExtensionMillis;
    private final java.lang.Runnable mOnUserAttention;
    protected long mPreDimCheckDurationMillis;
    private long mRequestedPostDimTimeoutMillis;
    protected com.android.server.wm.WindowManagerInternal mWindowManager;
    private java.util.concurrent.atomic.AtomicLong mConsecutiveTimeoutExtendedCount = new java.util.concurrent.atomic.AtomicLong(0);
    private final java.util.concurrent.atomic.AtomicBoolean mRequested = new java.util.concurrent.atomic.AtomicBoolean(false);
    protected int mRequestId = 0;
    private int mWakefulness = 1;

    public AttentionDetector(java.lang.Runnable onUserAttention, java.lang.Object lock) {
        this.mOnUserAttention = onUserAttention;
        this.mLock = lock;
    }

    void updateEnabledFromSettings(android.content.Context context) {
        this.mIsSettingEnabled = android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "adaptive_sleep", 0, -2) == 1;
    }

    public void systemReady(final android.content.Context context) {
        this.mContext = context;
        updateEnabledFromSettings(context);
        this.mContentResolver = context.getContentResolver();
        this.mAttentionManager = (android.attention.AttentionManagerInternal) com.android.server.LocalServices.getService(android.attention.AttentionManagerInternal.class);
        this.mWindowManager = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        this.mDefaultMaximumExtensionMillis = context.getResources().getInteger(android.R.integer.config_activityShortDur);
        try {
            com.android.server.power.AttentionDetector.UserSwitchObserver observer = new com.android.server.power.AttentionDetector.UserSwitchObserver();
            android.app.ActivityManager.getService().registerUserSwitchObserver(observer, TAG);
        } catch (android.os.RemoteException e) {
        }
        context.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("adaptive_sleep"), false, new android.database.ContentObserver(new android.os.Handler(context.getMainLooper())) { // from class: com.android.server.power.AttentionDetector.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.power.AttentionDetector.this.updateEnabledFromSettings(context);
            }
        }, -1);
        readValuesFromDeviceConfig();
        android.provider.DeviceConfig.addOnPropertiesChangedListener("attention_manager_service", context.getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.power.AttentionDetector$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$systemReady$0(properties);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$0(android.provider.DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    public long updateUserActivity(long nextScreenDimming, long dimDurationMillis) {
        if (nextScreenDimming == this.mLastActedOnNextScreenDimming || !this.mIsSettingEnabled || !isAttentionServiceSupported() || this.mWindowManager.isKeyguardShowingAndNotOccluded()) {
            return nextScreenDimming;
        }
        long now = android.os.SystemClock.uptimeMillis();
        long whenToCheck = nextScreenDimming - this.mPreDimCheckDurationMillis;
        long whenToStopExtending = this.mLastUserActivityTime + this.mMaximumExtensionMillis;
        if (now < whenToCheck) {
            return whenToCheck;
        }
        if (whenToStopExtending < whenToCheck) {
            return nextScreenDimming;
        }
        if (this.mRequested.get()) {
            return whenToCheck;
        }
        this.mRequested.set(true);
        this.mRequestId++;
        this.mLastActedOnNextScreenDimming = nextScreenDimming;
        this.mCallback = new com.android.server.power.AttentionDetector.AttentionCallbackInternalImpl(this.mRequestId);
        this.mEffectivePostDimTimeoutMillis = java.lang.Math.min(this.mRequestedPostDimTimeoutMillis, dimDurationMillis);
        android.util.Slog.v(TAG, "Checking user attention, ID: " + this.mRequestId);
        boolean sent = this.mAttentionManager.checkAttention(this.mPreDimCheckDurationMillis + this.mEffectivePostDimTimeoutMillis, this.mCallback);
        if (!sent) {
            this.mRequested.set(false);
        }
        return whenToCheck;
    }

    public int onUserActivity(long eventTime, int event) {
        switch (event) {
            case 0:
            case 1:
            case 2:
            case 3:
                cancelCurrentRequestIfAny();
                this.mLastUserActivityTime = eventTime;
                resetConsecutiveExtensionCount();
                return 1;
            case 4:
                this.mConsecutiveTimeoutExtendedCount.incrementAndGet();
                return 0;
            default:
                return -1;
        }
    }

    public void onWakefulnessChangeStarted(int wakefulness) {
        this.mWakefulness = wakefulness;
        if (wakefulness != 1) {
            cancelCurrentRequestIfAny();
            resetConsecutiveExtensionCount();
        }
    }

    private void cancelCurrentRequestIfAny() {
        if (this.mRequested.get()) {
            this.mAttentionManager.cancelAttentionCheck(this.mCallback);
            this.mRequested.set(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetConsecutiveExtensionCount() {
        long previousCount = this.mConsecutiveTimeoutExtendedCount.getAndSet(0L);
        if (previousCount > 0) {
            com.android.internal.util.FrameworkStatsLog.write(168, previousCount);
        }
    }

    boolean isAttentionServiceSupported() {
        return this.mAttentionManager != null && this.mAttentionManager.isAttentionServiceSupported();
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("AttentionDetector:");
        pw.println(" mIsSettingEnabled=" + this.mIsSettingEnabled);
        pw.println(" mMaxExtensionMillis=" + this.mMaximumExtensionMillis);
        pw.println(" mPreDimCheckDurationMillis=" + this.mPreDimCheckDurationMillis);
        pw.println(" mEffectivePostDimTimeout=" + this.mEffectivePostDimTimeoutMillis);
        pw.println(" mLastUserActivityTime(excludingAttention)=" + this.mLastUserActivityTime);
        pw.println(" mAttentionServiceSupported=" + isAttentionServiceSupported());
        pw.println(" mRequested=" + this.mRequested);
    }

    protected long getPreDimCheckDurationMillis() {
        long millis = android.provider.DeviceConfig.getLong("attention_manager_service", KEY_PRE_DIM_CHECK_DURATION_MILLIS, DEFAULT_PRE_DIM_CHECK_DURATION_MILLIS);
        if (millis < 0 || millis > 13000) {
            android.util.Slog.w(TAG, "Bad flag value supplied for: pre_dim_check_duration_millis");
            return DEFAULT_PRE_DIM_CHECK_DURATION_MILLIS;
        }
        return millis;
    }

    protected long getPostDimCheckDurationMillis() {
        long millis = android.provider.DeviceConfig.getLong("attention_manager_service", KEY_POST_DIM_CHECK_DURATION_MILLIS, DEFAULT_POST_DIM_CHECK_DURATION_MILLIS);
        if (millis < 0 || millis > 10000) {
            android.util.Slog.w(TAG, "Bad flag value supplied for: post_dim_check_duration_millis");
            return DEFAULT_POST_DIM_CHECK_DURATION_MILLIS;
        }
        return millis;
    }

    protected long getMaxExtensionMillis() {
        long millis = android.provider.DeviceConfig.getLong("attention_manager_service", KEY_MAX_EXTENSION_MILLIS, this.mDefaultMaximumExtensionMillis);
        if (millis < 0 || millis > 3600000) {
            android.util.Slog.w(TAG, "Bad flag value supplied for: max_extension_millis");
            return this.mDefaultMaximumExtensionMillis;
        }
        return millis;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0039  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void onDeviceConfigChange(java.util.Set<java.lang.String> r5) {
        /*
            r4 = this;
            java.util.Iterator r0 = r5.iterator()
        L4:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L5a
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            int r2 = r1.hashCode()
            switch(r2) {
                case -2018189628: goto L2e;
                case -511526975: goto L23;
                case 417901319: goto L18;
                default: goto L17;
            }
        L17:
            goto L39
        L18:
            java.lang.String r2 = "pre_dim_check_duration_millis"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L17
            r2 = 2
            goto L3a
        L23:
            java.lang.String r2 = "max_extension_millis"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L17
            r2 = 0
            goto L3a
        L2e:
            java.lang.String r2 = "post_dim_check_duration_millis"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L17
            r2 = 1
            goto L3a
        L39:
            r2 = -1
        L3a:
            switch(r2) {
                case 0: goto L56;
                case 1: goto L56;
                case 2: goto L56;
                default: goto L3d;
            }
        L3d:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Ignoring change on "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r1)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "AttentionDetector"
            android.util.Slog.i(r3, r2)
            goto L4
        L56:
            r4.readValuesFromDeviceConfig()
            return
        L5a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.AttentionDetector.onDeviceConfigChange(java.util.Set):void");
    }

    private void readValuesFromDeviceConfig() {
        this.mMaximumExtensionMillis = getMaxExtensionMillis();
        this.mPreDimCheckDurationMillis = getPreDimCheckDurationMillis();
        this.mRequestedPostDimTimeoutMillis = getPostDimCheckDurationMillis();
        android.util.Slog.i(TAG, "readValuesFromDeviceConfig():\nmMaximumExtensionMillis=" + this.mMaximumExtensionMillis + "\nmPreDimCheckDurationMillis=" + this.mPreDimCheckDurationMillis + "\nmRequestedPostDimTimeoutMillis=" + this.mRequestedPostDimTimeoutMillis);
    }

    final class AttentionCallbackInternalImpl extends android.attention.AttentionManagerInternal.AttentionCallbackInternal {
        private final int mId;

        AttentionCallbackInternalImpl(int id) {
            this.mId = id;
        }

        public void onSuccess(int result, long timestamp) {
            android.util.Slog.v(com.android.server.power.AttentionDetector.TAG, "onSuccess: " + result + ", ID: " + this.mId);
            if (this.mId == com.android.server.power.AttentionDetector.this.mRequestId && com.android.server.power.AttentionDetector.this.mRequested.getAndSet(false)) {
                synchronized (com.android.server.power.AttentionDetector.this.mLock) {
                    if (com.android.server.power.AttentionDetector.this.mWakefulness != 1) {
                        return;
                    }
                    if (result == 1) {
                        com.android.server.power.AttentionDetector.this.mOnUserAttention.run();
                    } else {
                        com.android.server.power.AttentionDetector.this.resetConsecutiveExtensionCount();
                    }
                }
            }
        }

        public void onFailure(int error) {
            android.util.Slog.i(com.android.server.power.AttentionDetector.TAG, "Failed to check attention: " + error + ", ID: " + this.mId);
            com.android.server.power.AttentionDetector.this.mRequested.set(false);
        }
    }

    private final class UserSwitchObserver extends android.app.SynchronousUserSwitchObserver {
        private UserSwitchObserver() {
        }

        public void onUserSwitching(int newUserId) throws android.os.RemoteException {
            com.android.server.power.AttentionDetector.this.updateEnabledFromSettings(com.android.server.power.AttentionDetector.this.mContext);
        }
    }
}
