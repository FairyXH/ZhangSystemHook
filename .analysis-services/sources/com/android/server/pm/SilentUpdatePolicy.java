package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class SilentUpdatePolicy {
    private static final long SILENT_UPDATE_THROTTLE_TIME_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(30);
    private java.lang.String mAllowUnlimitedSilentUpdatesInstaller;
    private final android.util.ArrayMap<android.util.Pair<java.lang.String, java.lang.String>, java.lang.Long> mSilentUpdateInfos = new android.util.ArrayMap<>();
    private long mSilentUpdateThrottleTimeMs = SILENT_UPDATE_THROTTLE_TIME_MS;

    public boolean isSilentUpdateAllowed(java.lang.String installerPackageName, java.lang.String packageName) {
        long throttleTimeMs;
        if (installerPackageName == null) {
            return true;
        }
        long lastSilentUpdatedMs = getTimestampMs(installerPackageName, packageName);
        synchronized (this.mSilentUpdateInfos) {
            throttleTimeMs = this.mSilentUpdateThrottleTimeMs;
        }
        return android.os.SystemClock.uptimeMillis() - lastSilentUpdatedMs > throttleTimeMs;
    }

    public void track(java.lang.String installerPackageName, java.lang.String packageName) {
        if (installerPackageName == null) {
            return;
        }
        synchronized (this.mSilentUpdateInfos) {
            if (this.mAllowUnlimitedSilentUpdatesInstaller == null || !this.mAllowUnlimitedSilentUpdatesInstaller.equals(installerPackageName)) {
                long uptime = android.os.SystemClock.uptimeMillis();
                pruneLocked(uptime);
                android.util.Pair<java.lang.String, java.lang.String> key = android.util.Pair.create(installerPackageName, packageName);
                this.mSilentUpdateInfos.put(key, java.lang.Long.valueOf(uptime));
            }
        }
    }

    void setAllowUnlimitedSilentUpdates(java.lang.String installerPackageName) {
        synchronized (this.mSilentUpdateInfos) {
            if (installerPackageName == null) {
                this.mSilentUpdateInfos.clear();
                this.mAllowUnlimitedSilentUpdatesInstaller = installerPackageName;
            } else {
                this.mAllowUnlimitedSilentUpdatesInstaller = installerPackageName;
            }
        }
    }

    void setSilentUpdatesThrottleTime(long throttleTimeInSeconds) {
        long millis;
        synchronized (this.mSilentUpdateInfos) {
            if (throttleTimeInSeconds >= 0) {
                millis = java.util.concurrent.TimeUnit.SECONDS.toMillis(throttleTimeInSeconds);
            } else {
                millis = SILENT_UPDATE_THROTTLE_TIME_MS;
            }
            this.mSilentUpdateThrottleTimeMs = millis;
        }
    }

    private void pruneLocked(long uptime) {
        int size = this.mSilentUpdateInfos.size();
        for (int i = size - 1; i >= 0; i--) {
            long lastSilentUpdatedMs = this.mSilentUpdateInfos.valueAt(i).longValue();
            if (uptime - lastSilentUpdatedMs > this.mSilentUpdateThrottleTimeMs) {
                this.mSilentUpdateInfos.removeAt(i);
            }
        }
    }

    private long getTimestampMs(java.lang.String installerPackageName, java.lang.String packageName) {
        java.lang.Long timestampMs;
        android.util.Pair<java.lang.String, java.lang.String> key = android.util.Pair.create(installerPackageName, packageName);
        synchronized (this.mSilentUpdateInfos) {
            timestampMs = this.mSilentUpdateInfos.get(key);
        }
        if (timestampMs != null) {
            return timestampMs.longValue();
        }
        return -1L;
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mSilentUpdateInfos) {
            if (this.mSilentUpdateInfos.isEmpty()) {
                return;
            }
            pw.println("Last silent updated Infos:");
            pw.increaseIndent();
            int size = this.mSilentUpdateInfos.size();
            for (int i = 0; i < size; i++) {
                android.util.Pair<java.lang.String, java.lang.String> key = this.mSilentUpdateInfos.keyAt(i);
                if (key != null) {
                    pw.printPair("installerPackageName", key.first);
                    pw.printPair(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, key.second);
                    pw.printPair("silentUpdatedMillis", this.mSilentUpdateInfos.valueAt(i));
                    pw.println();
                }
            }
            pw.decreaseIndent();
        }
    }
}
