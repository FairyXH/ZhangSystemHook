package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public class StatLogger {
    private static final java.lang.String TAG = "StatLogger";
    private final int SIZE;
    private final int[] mCallsPerSecond;
    private final int[] mCountStats;
    private final long[] mDurationPerSecond;
    private final long[] mDurationStats;
    private final java.lang.String[] mLabels;
    private final java.lang.Object mLock;
    private final int[] mMaxCallsPerSecond;
    private final long[] mMaxDurationPerSecond;
    private final long[] mMaxDurationStats;
    private long mNextTickTime;
    private final java.lang.String mStatsTag;

    public StatLogger(java.lang.String[] eventLabels) {
        this(null, eventLabels);
    }

    public StatLogger(java.lang.String statsTag, java.lang.String[] eventLabels) {
        this.mLock = new java.lang.Object();
        this.mNextTickTime = android.os.SystemClock.elapsedRealtime() + 1000;
        this.mStatsTag = statsTag;
        this.SIZE = eventLabels.length;
        this.mCountStats = new int[this.SIZE];
        this.mDurationStats = new long[this.SIZE];
        this.mCallsPerSecond = new int[this.SIZE];
        this.mMaxCallsPerSecond = new int[this.SIZE];
        this.mDurationPerSecond = new long[this.SIZE];
        this.mMaxDurationPerSecond = new long[this.SIZE];
        this.mMaxDurationStats = new long[this.SIZE];
        this.mLabels = eventLabels;
    }

    public long getTime() {
        return android.os.SystemClock.uptimeNanos() / 1000;
    }

    public long logDurationStat(int eventId, long start) {
        synchronized (this.mLock) {
            long duration = getTime() - start;
            if (eventId >= 0 && eventId < this.SIZE) {
                int[] iArr = this.mCountStats;
                iArr[eventId] = iArr[eventId] + 1;
                long[] jArr = this.mDurationStats;
                jArr[eventId] = jArr[eventId] + duration;
                if (this.mMaxDurationStats[eventId] < duration) {
                    this.mMaxDurationStats[eventId] = duration;
                }
                long nowRealtime = android.os.SystemClock.elapsedRealtime();
                if (nowRealtime > this.mNextTickTime) {
                    if (this.mMaxCallsPerSecond[eventId] < this.mCallsPerSecond[eventId]) {
                        this.mMaxCallsPerSecond[eventId] = this.mCallsPerSecond[eventId];
                    }
                    if (this.mMaxDurationPerSecond[eventId] < this.mDurationPerSecond[eventId]) {
                        this.mMaxDurationPerSecond[eventId] = this.mDurationPerSecond[eventId];
                    }
                    this.mCallsPerSecond[eventId] = 0;
                    this.mDurationPerSecond[eventId] = 0;
                    this.mNextTickTime = 1000 + nowRealtime;
                }
                int[] iArr2 = this.mCallsPerSecond;
                iArr2[eventId] = iArr2[eventId] + 1;
                long[] jArr2 = this.mDurationPerSecond;
                jArr2[eventId] = jArr2[eventId] + duration;
                return duration;
            }
            android.util.Slog.wtf(TAG, "Invalid event ID: " + eventId);
            return duration;
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        dump(new android.util.IndentingPrintWriter(pw, "  ").setIndent(prefix));
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            if (!android.text.TextUtils.isEmpty(this.mStatsTag)) {
                pw.println(this.mStatsTag + ":");
            } else {
                pw.println("Stats:");
            }
            pw.increaseIndent();
            for (int i = 0; i < this.SIZE; i++) {
                int count = this.mCountStats[i];
                double durationMs = this.mDurationStats[i] / 1000.0d;
                pw.println(java.lang.String.format("%s: count=%d, total=%.1fms, avg=%.3fms, max calls/s=%d max dur/s=%.1fms max time=%.1fms", this.mLabels[i], java.lang.Integer.valueOf(count), java.lang.Double.valueOf(durationMs), java.lang.Double.valueOf(count == 0 ? 0.0d : durationMs / ((double) count)), java.lang.Integer.valueOf(this.mMaxCallsPerSecond[i]), java.lang.Double.valueOf(this.mMaxDurationPerSecond[i] / 1000.0d), java.lang.Double.valueOf(this.mMaxDurationStats[i] / 1000.0d)));
            }
            pw.decreaseIndent();
        }
    }

    public void dumpProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        synchronized (this.mLock) {
            long outer = proto.start(fieldId);
            for (int i = 0; i < this.mLabels.length; i++) {
                long inner = proto.start(2246267895809L);
                proto.write(1120986464257L, i);
                proto.write(1138166333442L, this.mLabels[i]);
                proto.write(1120986464259L, this.mCountStats[i]);
                proto.write(1112396529668L, this.mDurationStats[i]);
                proto.write(1120986464261L, this.mMaxCallsPerSecond[i]);
                proto.write(1112396529670L, this.mMaxDurationPerSecond[i]);
                proto.write(1112396529671L, this.mMaxDurationStats[i]);
                proto.end(inner);
            }
            proto.end(outer);
        }
    }
}
