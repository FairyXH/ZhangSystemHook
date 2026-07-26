package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class ScreenUndimDetector {
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_KEEP_SCREEN_ON_ENABLED = false;
    static final long DEFAULT_KEEP_SCREEN_ON_FOR_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(10);
    static final long DEFAULT_MAX_DURATION_BETWEEN_UNDIMS_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(5);
    static final int DEFAULT_UNDIMS_REQUIRED = 2;
    static final java.lang.String KEY_KEEP_SCREEN_ON_ENABLED = "keep_screen_on_enabled";
    static final java.lang.String KEY_KEEP_SCREEN_ON_FOR_MILLIS = "keep_screen_on_for_millis";
    static final java.lang.String KEY_MAX_DURATION_BETWEEN_UNDIMS_MILLIS = "max_duration_between_undims_millis";
    static final java.lang.String KEY_UNDIMS_REQUIRED = "undims_required";
    private static final int OUTCOME_POWER_BUTTON = 1;
    private static final int OUTCOME_TIMEOUT = 2;
    private static final java.lang.String TAG = "ScreenUndimDetector";
    private static final java.lang.String UNDIM_DETECTOR_WAKE_LOCK = "UndimDetectorWakeLock";
    private com.android.server.power.ScreenUndimDetector.InternalClock mClock;
    int mCurrentScreenPolicy;
    private long mInteractionAfterUndimTime;
    private boolean mKeepScreenOnEnabled;
    private long mKeepScreenOnForMillis;
    private long mMaxDurationBetweenUndimsMillis;
    int mUndimCounter;
    long mUndimCounterStartedMillis;
    private long mUndimOccurredTime;
    private int mUndimsRequired;
    android.os.PowerManager.WakeLock mWakeLock;

    public ScreenUndimDetector() {
        this.mUndimCounter = 0;
        this.mUndimOccurredTime = -1L;
        this.mInteractionAfterUndimTime = -1L;
        this.mClock = new com.android.server.power.ScreenUndimDetector.InternalClock();
    }

    ScreenUndimDetector(com.android.server.power.ScreenUndimDetector.InternalClock clock) {
        this.mUndimCounter = 0;
        this.mUndimOccurredTime = -1L;
        this.mInteractionAfterUndimTime = -1L;
        this.mClock = clock;
    }

    static class InternalClock {
        InternalClock() {
        }

        public long getCurrentTime() {
            return android.os.SystemClock.elapsedRealtime();
        }
    }

    public void systemReady(android.content.Context context) {
        readValuesFromDeviceConfig();
        android.provider.DeviceConfig.addOnPropertiesChangedListener("attention_manager_service", context.getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.power.ScreenUndimDetector$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$systemReady$0(properties);
            }
        });
        android.os.PowerManager powerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        this.mWakeLock = powerManager.newWakeLock(536870922, UNDIM_DETECTOR_WAKE_LOCK);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$0(android.provider.DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    public void recordScreenPolicy(int displayGroupId, int newPolicy) {
        if (displayGroupId != 0 || newPolicy == this.mCurrentScreenPolicy) {
            return;
        }
        int currentPolicy = this.mCurrentScreenPolicy;
        this.mCurrentScreenPolicy = newPolicy;
        if (!this.mKeepScreenOnEnabled) {
        }
        switch (currentPolicy) {
            case 2:
                if (newPolicy == 3) {
                    long now = this.mClock.getCurrentTime();
                    long timeElapsedSinceFirstUndim = now - this.mUndimCounterStartedMillis;
                    if (timeElapsedSinceFirstUndim >= this.mMaxDurationBetweenUndimsMillis) {
                        reset();
                    }
                    if (this.mUndimCounter == 0) {
                        this.mUndimCounterStartedMillis = now;
                    }
                    this.mUndimCounter++;
                    if (this.mUndimCounter >= this.mUndimsRequired) {
                        reset();
                        if (this.mWakeLock != null) {
                            this.mUndimOccurredTime = this.mClock.getCurrentTime();
                            this.mWakeLock.acquire(this.mKeepScreenOnForMillis);
                        }
                    }
                } else {
                    if (newPolicy == 0 || newPolicy == 1) {
                        checkAndLogUndim(2);
                    }
                    reset();
                }
                break;
            case 3:
                if (newPolicy == 0 || newPolicy == 1) {
                    checkAndLogUndim(1);
                }
                if (newPolicy != 2) {
                    reset();
                }
                break;
        }
    }

    void reset() {
        this.mUndimCounter = 0;
        this.mUndimCounterStartedMillis = 0L;
        if (this.mWakeLock != null && this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
    }

    private boolean readKeepScreenOnNotificationEnabled() {
        return android.provider.DeviceConfig.getBoolean("attention_manager_service", KEY_KEEP_SCREEN_ON_ENABLED, false);
    }

    private long readKeepScreenOnForMillis() {
        return android.provider.DeviceConfig.getLong("attention_manager_service", KEY_KEEP_SCREEN_ON_FOR_MILLIS, DEFAULT_KEEP_SCREEN_ON_FOR_MILLIS);
    }

    private int readUndimsRequired() {
        int undimsRequired = android.provider.DeviceConfig.getInt("attention_manager_service", KEY_UNDIMS_REQUIRED, 2);
        if (undimsRequired < 1 || undimsRequired > 5) {
            android.util.Slog.e(TAG, "Provided undimsRequired=" + undimsRequired + " is not allowed [1, 5]; using the default=2");
            return 2;
        }
        return undimsRequired;
    }

    private long readMaxDurationBetweenUndimsMillis() {
        return android.provider.DeviceConfig.getLong("attention_manager_service", KEY_MAX_DURATION_BETWEEN_UNDIMS_MILLIS, DEFAULT_MAX_DURATION_BETWEEN_UNDIMS_MILLIS);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void onDeviceConfigChange(java.util.Set<java.lang.String> r6) {
        /*
            r5 = this;
            java.util.Iterator r0 = r6.iterator()
        L4:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L7a
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "onDeviceConfigChange; key="
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r1)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "ScreenUndimDetector"
            android.util.Slog.i(r3, r2)
            int r2 = r1.hashCode()
            switch(r2) {
                case -2114725254: goto L50;
                case -1871288230: goto L46;
                case 352003779: goto L3c;
                case 1709324730: goto L31;
                default: goto L30;
            }
        L30:
            goto L5b
        L31:
            java.lang.String r2 = "max_duration_between_undims_millis"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L30
            r2 = 3
            goto L5c
        L3c:
            java.lang.String r2 = "keep_screen_on_for_millis"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L30
            r2 = 1
            goto L5c
        L46:
            java.lang.String r2 = "keep_screen_on_enabled"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L30
            r2 = 0
            goto L5c
        L50:
            java.lang.String r2 = "undims_required"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L30
            r2 = 2
            goto L5c
        L5b:
            r2 = -1
        L5c:
            switch(r2) {
                case 0: goto L76;
                case 1: goto L76;
                case 2: goto L76;
                case 3: goto L76;
                default: goto L5f;
            }
        L5f:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "Ignoring change on "
            java.lang.StringBuilder r2 = r2.append(r4)
            java.lang.StringBuilder r2 = r2.append(r1)
            java.lang.String r2 = r2.toString()
            android.util.Slog.i(r3, r2)
            goto L4
        L76:
            r5.readValuesFromDeviceConfig()
            return
        L7a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.ScreenUndimDetector.onDeviceConfigChange(java.util.Set):void");
    }

    void readValuesFromDeviceConfig() {
        this.mKeepScreenOnEnabled = readKeepScreenOnNotificationEnabled();
        this.mKeepScreenOnForMillis = readKeepScreenOnForMillis();
        this.mUndimsRequired = readUndimsRequired();
        this.mMaxDurationBetweenUndimsMillis = readMaxDurationBetweenUndimsMillis();
        android.util.Slog.i(TAG, "readValuesFromDeviceConfig():\nmKeepScreenOnForMillis=" + this.mKeepScreenOnForMillis + "\nmKeepScreenOnNotificationEnabled=" + this.mKeepScreenOnEnabled + "\nmUndimsRequired=" + this.mUndimsRequired);
    }

    public void userActivity(int displayGroupId) {
        if (displayGroupId == 0 && this.mUndimOccurredTime != 1 && this.mInteractionAfterUndimTime == -1) {
            this.mInteractionAfterUndimTime = this.mClock.getCurrentTime();
        }
    }

    private void checkAndLogUndim(int outcome) {
        if (this.mUndimOccurredTime != -1) {
            long now = this.mClock.getCurrentTime();
            com.android.internal.util.FrameworkStatsLog.write(365, outcome, now - this.mUndimOccurredTime, this.mInteractionAfterUndimTime != -1 ? now - this.mInteractionAfterUndimTime : -1L);
            this.mUndimOccurredTime = -1L;
            this.mInteractionAfterUndimTime = -1L;
        }
    }
}
