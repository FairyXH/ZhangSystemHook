package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class PulledStats {
    static final java.lang.String TAG = "PulledStats";
    private long mTimePeriodEndMs;
    private final long mTimePeriodStartMs;
    private java.util.List<java.lang.String> mUndecoratedPackageNames = new java.util.ArrayList();

    public PulledStats(long startMs) {
        this.mTimePeriodStartMs = startMs;
        this.mTimePeriodEndMs = startMs;
    }

    android.os.ParcelFileDescriptor toParcelFileDescriptor(final int report) throws java.io.IOException {
        final android.os.ParcelFileDescriptor[] fds = android.os.ParcelFileDescriptor.createPipe();
        switch (report) {
            case 1:
                java.lang.Thread thr = new java.lang.Thread("NotificationManager pulled metric output") { // from class: com.android.server.notification.PulledStats.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        try {
                            java.io.FileOutputStream fout = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(fds[1]);
                            android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fout);
                            com.android.server.notification.PulledStats.this.writeToProto(report, proto);
                            proto.flush();
                            fout.close();
                        } catch (java.io.IOException e) {
                            android.util.Slog.w(com.android.server.notification.PulledStats.TAG, "Failure writing pipe", e);
                        }
                    }
                };
                thr.start();
                break;
            default:
                android.util.Slog.w(TAG, "Unknown pulled stats request: " + report);
                break;
        }
        return fds[0];
    }

    public long endTimeMs() {
        return this.mTimePeriodEndMs;
    }

    public void dump(int report, java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        switch (report) {
            case 1:
                pw.print("  Packages with undecordated notifications (");
                pw.print(this.mTimePeriodStartMs);
                pw.print(" - ");
                pw.print(this.mTimePeriodEndMs);
                pw.println("):");
                if (this.mUndecoratedPackageNames.size() == 0) {
                    pw.println("    none");
                } else {
                    for (java.lang.String pkg : this.mUndecoratedPackageNames) {
                        if (!filter.filtered || pkg.equals(filter.pkgFilter)) {
                            pw.println("    " + pkg);
                        }
                    }
                }
                break;
            default:
                pw.println("Unknown pulled stats request: " + report);
                break;
        }
    }

    void writeToProto(int report, android.util.proto.ProtoOutputStream proto) {
        switch (report) {
            case 1:
                for (java.lang.String pkg : this.mUndecoratedPackageNames) {
                    long token = proto.start(2246267895809L);
                    proto.write(1138166333441L, pkg);
                    proto.end(token);
                }
                break;
            default:
                android.util.Slog.w(TAG, "Unknown pulled stats request: " + report);
                break;
        }
    }

    public void addUndecoratedPackage(java.lang.String packageName, long timestampMs) {
        this.mUndecoratedPackageNames.add(packageName);
        this.mTimePeriodEndMs = java.lang.Math.max(this.mTimePeriodEndMs, timestampMs);
    }
}
