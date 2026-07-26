package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class BroadcastStats {
    static final java.util.Comparator<com.android.server.am.BroadcastStats.ActionEntry> ACTIONS_COMPARATOR = new java.util.Comparator<com.android.server.am.BroadcastStats.ActionEntry>() { // from class: com.android.server.am.BroadcastStats.1
        @Override // java.util.Comparator
        public int compare(com.android.server.am.BroadcastStats.ActionEntry o1, com.android.server.am.BroadcastStats.ActionEntry o2) {
            if (o1.mTotalDispatchTime < o2.mTotalDispatchTime) {
                return -1;
            }
            if (o1.mTotalDispatchTime > o2.mTotalDispatchTime) {
                return 1;
            }
            return 0;
        }
    };
    long mEndRealtime;
    long mEndUptime;
    final android.util.ArrayMap<java.lang.String, com.android.server.am.BroadcastStats.ActionEntry> mActions = new android.util.ArrayMap<>();
    final long mStartRealtime = android.os.SystemClock.elapsedRealtime();
    final long mStartUptime = android.os.SystemClock.uptimeMillis();

    static final class ActionEntry {
        final java.lang.String mAction;
        long mMaxDispatchTime;
        int mReceiveCount;
        int mSkipCount;
        long mTotalDispatchTime;
        final android.util.ArrayMap<java.lang.String, com.android.server.am.BroadcastStats.PackageEntry> mPackages = new android.util.ArrayMap<>();
        final android.util.ArrayMap<java.lang.String, com.android.server.am.BroadcastStats.ViolationEntry> mBackgroundCheckViolations = new android.util.ArrayMap<>();

        ActionEntry(java.lang.String action) {
            this.mAction = action;
        }
    }

    static final class PackageEntry {
        int mSendCount;

        PackageEntry() {
        }
    }

    static final class ViolationEntry {
        int mCount;

        ViolationEntry() {
        }
    }

    public void addBroadcast(java.lang.String action, java.lang.String srcPackage, int receiveCount, int skipCount, long dispatchTime) {
        com.android.server.am.BroadcastStats.ActionEntry ae = this.mActions.get(action);
        if (ae == null) {
            ae = new com.android.server.am.BroadcastStats.ActionEntry(action);
            this.mActions.put(action, ae);
        }
        ae.mReceiveCount += receiveCount;
        ae.mSkipCount += skipCount;
        ae.mTotalDispatchTime += dispatchTime;
        if (ae.mMaxDispatchTime < dispatchTime) {
            ae.mMaxDispatchTime = dispatchTime;
        }
        com.android.server.am.BroadcastStats.PackageEntry pe = ae.mPackages.get(srcPackage);
        if (pe == null) {
            pe = new com.android.server.am.BroadcastStats.PackageEntry();
            ae.mPackages.put(srcPackage, pe);
        }
        pe.mSendCount++;
    }

    public void addBackgroundCheckViolation(java.lang.String action, java.lang.String targetPackage) {
        com.android.server.am.BroadcastStats.ActionEntry ae = this.mActions.get(action);
        if (ae == null) {
            ae = new com.android.server.am.BroadcastStats.ActionEntry(action);
            this.mActions.put(action, ae);
        }
        com.android.server.am.BroadcastStats.ViolationEntry ve = ae.mBackgroundCheckViolations.get(targetPackage);
        if (ve == null) {
            ve = new com.android.server.am.BroadcastStats.ViolationEntry();
            ae.mBackgroundCheckViolations.put(targetPackage, ve);
        }
        ve.mCount++;
    }

    @dalvik.annotation.optimization.NeverCompile
    public boolean dumpStats(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String dumpPackage) {
        boolean printedSomething = false;
        java.util.ArrayList<com.android.server.am.BroadcastStats.ActionEntry> actions = new java.util.ArrayList<>(this.mActions.size());
        for (int i = this.mActions.size() - 1; i >= 0; i--) {
            actions.add(this.mActions.valueAt(i));
        }
        java.util.Collections.sort(actions, ACTIONS_COMPARATOR);
        for (int i2 = actions.size() - 1; i2 >= 0; i2--) {
            com.android.server.am.BroadcastStats.ActionEntry ae = actions.get(i2);
            if (dumpPackage == null || ae.mPackages.containsKey(dumpPackage)) {
                printedSomething = true;
                pw.print(prefix);
                pw.print(ae.mAction);
                pw.println(":");
                pw.print(prefix);
                pw.print("  Number received: ");
                pw.print(ae.mReceiveCount);
                pw.print(", skipped: ");
                pw.println(ae.mSkipCount);
                pw.print(prefix);
                pw.print("  Total dispatch time: ");
                android.util.TimeUtils.formatDuration(ae.mTotalDispatchTime, pw);
                pw.print(", max: ");
                android.util.TimeUtils.formatDuration(ae.mMaxDispatchTime, pw);
                pw.println();
                int j = ae.mPackages.size();
                while (true) {
                    j--;
                    if (j < 0) {
                        break;
                    }
                    pw.print(prefix);
                    pw.print("  Package ");
                    pw.print(ae.mPackages.keyAt(j));
                    pw.print(": ");
                    com.android.server.am.BroadcastStats.PackageEntry pe = ae.mPackages.valueAt(j);
                    pw.print(pe.mSendCount);
                    pw.println(" times");
                }
                for (int j2 = ae.mBackgroundCheckViolations.size() - 1; j2 >= 0; j2--) {
                    pw.print(prefix);
                    pw.print("  Bg Check Violation ");
                    pw.print(ae.mBackgroundCheckViolations.keyAt(j2));
                    pw.print(": ");
                    com.android.server.am.BroadcastStats.ViolationEntry ve = ae.mBackgroundCheckViolations.valueAt(j2);
                    pw.print(ve.mCount);
                    pw.println(" times");
                }
            }
        }
        return printedSomething;
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dumpCheckinStats(java.io.PrintWriter pw, java.lang.String dumpPackage) {
        pw.print("broadcast-stats,1,");
        pw.print(this.mStartRealtime);
        pw.print(",");
        pw.print(this.mEndRealtime == 0 ? android.os.SystemClock.elapsedRealtime() : this.mEndRealtime);
        pw.print(",");
        pw.println((this.mEndUptime == 0 ? android.os.SystemClock.uptimeMillis() : this.mEndUptime) - this.mStartUptime);
        for (int i = this.mActions.size() - 1; i >= 0; i--) {
            com.android.server.am.BroadcastStats.ActionEntry ae = this.mActions.valueAt(i);
            if (dumpPackage == null || ae.mPackages.containsKey(dumpPackage)) {
                pw.print("a,");
                pw.print(this.mActions.keyAt(i));
                pw.print(",");
                pw.print(ae.mReceiveCount);
                pw.print(",");
                pw.print(ae.mSkipCount);
                pw.print(",");
                pw.print(ae.mTotalDispatchTime);
                pw.print(",");
                pw.print(ae.mMaxDispatchTime);
                pw.println();
                for (int j = ae.mPackages.size() - 1; j >= 0; j--) {
                    pw.print("p,");
                    pw.print(ae.mPackages.keyAt(j));
                    com.android.server.am.BroadcastStats.PackageEntry pe = ae.mPackages.valueAt(j);
                    pw.print(",");
                    pw.print(pe.mSendCount);
                    pw.println();
                }
                for (int j2 = ae.mBackgroundCheckViolations.size() - 1; j2 >= 0; j2--) {
                    pw.print("v,");
                    pw.print(ae.mBackgroundCheckViolations.keyAt(j2));
                    com.android.server.am.BroadcastStats.ViolationEntry ve = ae.mBackgroundCheckViolations.valueAt(j2);
                    pw.print(",");
                    pw.print(ve.mCount);
                    pw.println();
                }
            }
        }
    }
}
