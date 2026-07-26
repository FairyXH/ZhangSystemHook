package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class AppTimeTracker {
    private final android.util.ArrayMap<java.lang.String, android.util.MutableLong> mPackageTimes = new android.util.ArrayMap<>();
    private final android.app.PendingIntent mReceiver;
    private java.lang.String mStartedPackage;
    private android.util.MutableLong mStartedPackageTime;
    private long mStartedTime;
    private long mTotalTime;

    public AppTimeTracker(android.app.PendingIntent receiver) {
        this.mReceiver = receiver;
    }

    public void start(java.lang.String packageName) {
        long now = android.os.SystemClock.elapsedRealtime();
        if (this.mStartedTime == 0) {
            this.mStartedTime = now;
        }
        if (!packageName.equals(this.mStartedPackage)) {
            if (this.mStartedPackageTime != null) {
                long elapsedTime = now - this.mStartedTime;
                this.mStartedPackageTime.value += elapsedTime;
                this.mTotalTime += elapsedTime;
            }
            this.mStartedPackage = packageName;
            this.mStartedPackageTime = this.mPackageTimes.get(packageName);
            if (this.mStartedPackageTime == null) {
                this.mStartedPackageTime = new android.util.MutableLong(0L);
                this.mPackageTimes.put(packageName, this.mStartedPackageTime);
            }
        }
    }

    public void stop() {
        if (this.mStartedTime != 0) {
            long elapsedTime = android.os.SystemClock.elapsedRealtime() - this.mStartedTime;
            this.mTotalTime += elapsedTime;
            if (this.mStartedPackageTime != null) {
                this.mStartedPackageTime.value += elapsedTime;
            }
            this.mStartedPackage = null;
            this.mStartedPackageTime = null;
        }
    }

    public void deliverResult(android.content.Context context) {
        stop();
        android.os.Bundle extras = new android.os.Bundle();
        extras.putLong("android.activity.usage_time", this.mTotalTime);
        android.os.Bundle pkgs = new android.os.Bundle();
        for (int i = this.mPackageTimes.size() - 1; i >= 0; i--) {
            pkgs.putLong(this.mPackageTimes.keyAt(i), this.mPackageTimes.valueAt(i).value);
        }
        extras.putBundle("android.usage_time_packages", pkgs);
        android.content.Intent fillinIntent = new android.content.Intent();
        fillinIntent.putExtras(extras);
        try {
            this.mReceiver.send(context, 0, fillinIntent);
        } catch (android.app.PendingIntent.CanceledException e) {
        }
    }

    public void dumpWithHeader(java.io.PrintWriter pw, java.lang.String prefix, boolean details) {
        pw.print(prefix);
        pw.print("AppTimeTracker #");
        pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        pw.println(":");
        dump(pw, prefix + "  ", details);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean details) {
        pw.print(prefix);
        pw.print("mReceiver=");
        pw.println(this.mReceiver);
        pw.print(prefix);
        pw.print("mTotalTime=");
        android.util.TimeUtils.formatDuration(this.mTotalTime, pw);
        pw.println();
        for (int i = 0; i < this.mPackageTimes.size(); i++) {
            pw.print(prefix);
            pw.print("mPackageTime:");
            pw.print(this.mPackageTimes.keyAt(i));
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.mPackageTimes.valueAt(i).value, pw);
            pw.println();
        }
        if (details && this.mStartedTime != 0) {
            pw.print(prefix);
            pw.print("mStartedTime=");
            android.util.TimeUtils.formatDuration(android.os.SystemClock.elapsedRealtime(), this.mStartedTime, pw);
            pw.println();
            pw.print(prefix);
            pw.print("mStartedPackage=");
            pw.println(this.mStartedPackage);
        }
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, boolean details) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, this.mReceiver.toString());
        proto.write(1112396529666L, this.mTotalTime);
        for (int i = 0; i < this.mPackageTimes.size(); i++) {
            long ptoken = proto.start(2246267895811L);
            proto.write(1138166333441L, this.mPackageTimes.keyAt(i));
            proto.write(1112396529666L, this.mPackageTimes.valueAt(i).value);
            proto.end(ptoken);
        }
        if (details && this.mStartedTime != 0) {
            android.util.proto.ProtoUtils.toDuration(proto, 1146756268036L, this.mStartedTime, android.os.SystemClock.elapsedRealtime());
            proto.write(1138166333445L, this.mStartedPackage);
        }
        proto.end(token);
    }
}
