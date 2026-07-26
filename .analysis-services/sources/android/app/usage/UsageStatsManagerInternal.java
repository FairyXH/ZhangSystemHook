package android.app.usage;

/* JADX INFO: loaded from: classes.dex */
public abstract class UsageStatsManagerInternal {

    public interface EstimatedLaunchTimeChangedListener {
        void onEstimatedLaunchTimeChanged(int i, java.lang.String str, long j);
    }

    public interface UsageEventListener {
        void onUsageEvent(int i, android.app.usage.UsageEvents.Event event);
    }

    public abstract void applyRestoredPayload(int i, java.lang.String str, byte[] bArr);

    public abstract int getAppStandbyBucket(java.lang.String str, int i, long j);

    public abstract android.app.usage.UsageStatsManagerInternal.AppUsageLimitData getAppUsageLimit(java.lang.String str, android.os.UserHandle userHandle);

    public abstract byte[] getBackupPayload(int i, java.lang.String str);

    public abstract long getEstimatedPackageLaunchTime(java.lang.String str, int i);

    public abstract int[] getIdleUidsForUser(int i);

    public abstract long getTimeSinceLastJobRun(java.lang.String str, int i);

    public abstract boolean isAppIdle(java.lang.String str, int i, int i2);

    public abstract void onActiveAdminAdded(java.lang.String str, int i);

    public abstract void onAdminDataAvailable();

    public abstract void prepareForPossibleShutdown();

    public abstract void prepareShutdown();

    public abstract boolean pruneUninstalledPackagesData(int i);

    public abstract android.app.usage.UsageEvents queryEventsForUser(int i, long j, long j2, int i2);

    public abstract java.util.List<android.app.usage.UsageStats> queryUsageStatsForUser(int i, int i2, long j, long j2, boolean z);

    public abstract void registerLaunchTimeChangedListener(android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener estimatedLaunchTimeChangedListener);

    public abstract void registerListener(android.app.usage.UsageStatsManagerInternal.UsageEventListener usageEventListener);

    public abstract void reportAppJobState(java.lang.String str, int i, int i2, long j);

    public abstract void reportBroadcastDispatched(int i, java.lang.String str, android.os.UserHandle userHandle, long j, long j2, int i2);

    public abstract void reportConfigurationChange(android.content.res.Configuration configuration, int i);

    public abstract void reportContentProviderUsage(java.lang.String str, java.lang.String str2, int i);

    public abstract void reportEvent(android.content.ComponentName componentName, int i, int i2, int i3, android.content.ComponentName componentName2);

    public abstract void reportEvent(java.lang.String str, int i, int i2);

    public abstract void reportExemptedSyncStart(java.lang.String str, int i);

    public abstract void reportInterruptiveNotification(java.lang.String str, java.lang.String str2, int i);

    public abstract void reportLocusUpdate(android.content.ComponentName componentName, int i, android.content.LocusId locusId, android.os.IBinder iBinder);

    public abstract void reportNotificationPosted(java.lang.String str, android.os.UserHandle userHandle, long j);

    public abstract void reportNotificationRemoved(java.lang.String str, android.os.UserHandle userHandle, long j);

    public abstract void reportNotificationUpdated(java.lang.String str, android.os.UserHandle userHandle, long j);

    public abstract void reportShortcutUsage(java.lang.String str, java.lang.String str2, int i);

    public abstract void reportSyncScheduled(java.lang.String str, int i, boolean z);

    public abstract void reportUserInteractionEvent(java.lang.String str, int i, android.os.PersistableBundle persistableBundle);

    public abstract void setActiveAdminApps(java.util.Set<java.lang.String> set, int i);

    public abstract void setAdminProtectedPackages(java.util.Set<java.lang.String> set, int i);

    public abstract void setLastJobRunTime(java.lang.String str, int i, long j);

    public abstract void unregisterLaunchTimeChangedListener(android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener estimatedLaunchTimeChangedListener);

    public abstract void unregisterListener(android.app.usage.UsageStatsManagerInternal.UsageEventListener usageEventListener);

    public abstract boolean updatePackageMappingsData(int i);

    public static class AppUsageLimitData {
        private final long mTotalUsageLimit;
        private final long mUsageRemaining;

        public AppUsageLimitData(long totalUsageLimit, long usageRemaining) {
            this.mTotalUsageLimit = totalUsageLimit;
            this.mUsageRemaining = usageRemaining;
        }

        public long getTotalUsageLimit() {
            return this.mTotalUsageLimit;
        }

        public long getUsageRemaining() {
            return this.mUsageRemaining;
        }
    }
}
