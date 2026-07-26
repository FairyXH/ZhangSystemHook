package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseAppStatePolicy<T extends com.android.server.am.BaseAppStateTracker> {
    protected final boolean mDefaultTrackerEnabled;
    protected final com.android.server.am.BaseAppStateTracker.Injector<?> mInjector;
    protected final java.lang.String mKeyTrackerEnabled;
    protected final T mTracker;
    volatile boolean mTrackerEnabled;

    public abstract void onTrackerEnabled(boolean z);

    BaseAppStatePolicy(com.android.server.am.BaseAppStateTracker.Injector<?> injector, T tracker, java.lang.String keyTrackerEnabled, boolean defaultTrackerEnabled) {
        this.mInjector = injector;
        this.mTracker = tracker;
        this.mKeyTrackerEnabled = keyTrackerEnabled;
        this.mDefaultTrackerEnabled = defaultTrackerEnabled;
    }

    void updateTrackerEnabled() {
        boolean enabled = android.provider.DeviceConfig.getBoolean("activity_manager", this.mKeyTrackerEnabled, this.mDefaultTrackerEnabled);
        if (enabled != this.mTrackerEnabled) {
            this.mTrackerEnabled = enabled;
            onTrackerEnabled(enabled);
        }
    }

    public void onPropertiesChanged(java.lang.String name) {
        if (this.mKeyTrackerEnabled.equals(name)) {
            updateTrackerEnabled();
        }
    }

    public int getProposedRestrictionLevel(java.lang.String packageName, int uid, int maxLevel) {
        return 0;
    }

    public void onSystemReady() {
        updateTrackerEnabled();
    }

    public boolean isEnabled() {
        return this.mTrackerEnabled;
    }

    public int shouldExemptUid(int uid) {
        return this.mTracker.mAppRestrictionController.getBackgroundRestrictionExemptionReason(uid);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print(this.mKeyTrackerEnabled);
        pw.print('=');
        pw.println(this.mTrackerEnabled);
    }
}
