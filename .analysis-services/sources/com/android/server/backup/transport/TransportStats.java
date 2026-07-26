package com.android.server.backup.transport;

/* JADX INFO: loaded from: classes.dex */
public class TransportStats {
    private final java.lang.Object mStatsLock = new java.lang.Object();
    private final java.util.Map<android.content.ComponentName, com.android.server.backup.transport.TransportStats.Stats> mTransportStats = new java.util.HashMap();

    void registerConnectionTime(android.content.ComponentName transportComponent, long timeMs) {
        synchronized (this.mStatsLock) {
            com.android.server.backup.transport.TransportStats.Stats stats = this.mTransportStats.get(transportComponent);
            if (stats == null) {
                stats = new com.android.server.backup.transport.TransportStats.Stats();
                this.mTransportStats.put(transportComponent, stats);
            }
            stats.register(timeMs);
        }
    }

    public com.android.server.backup.transport.TransportStats.Stats getStatsForTransport(android.content.ComponentName transportComponent) {
        synchronized (this.mStatsLock) {
            com.android.server.backup.transport.TransportStats.Stats stats = this.mTransportStats.get(transportComponent);
            if (stats == null) {
                return null;
            }
            return new com.android.server.backup.transport.TransportStats.Stats(stats);
        }
    }

    public void dump(java.io.PrintWriter pw) {
        synchronized (this.mStatsLock) {
            java.util.Optional<com.android.server.backup.transport.TransportStats.Stats> aggregatedStats = this.mTransportStats.values().stream().reduce(new java.util.function.BinaryOperator() { // from class: com.android.server.backup.transport.TransportStats$$ExternalSyntheticLambda0
                @Override // java.util.function.BiFunction
                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.backup.transport.TransportStats.Stats.merge((com.android.server.backup.transport.TransportStats.Stats) obj, (com.android.server.backup.transport.TransportStats.Stats) obj2);
                }
            });
            if (aggregatedStats.isPresent()) {
                dumpStats(pw, "", aggregatedStats.get());
            }
            if (!this.mTransportStats.isEmpty()) {
                pw.println("Per transport:");
                for (android.content.ComponentName transportComponent : this.mTransportStats.keySet()) {
                    com.android.server.backup.transport.TransportStats.Stats stats = this.mTransportStats.get(transportComponent);
                    pw.println("    " + transportComponent.flattenToShortString());
                    dumpStats(pw, "        ", stats);
                }
            }
        }
    }

    private static void dumpStats(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.backup.transport.TransportStats.Stats stats) {
        pw.println(java.lang.String.format(java.util.Locale.US, "%sAverage connection time: %.2f ms", prefix, java.lang.Double.valueOf(stats.average)));
        pw.println(java.lang.String.format(java.util.Locale.US, "%sMax connection time: %d ms", prefix, java.lang.Long.valueOf(stats.max)));
        pw.println(java.lang.String.format(java.util.Locale.US, "%sMin connection time: %d ms", prefix, java.lang.Long.valueOf(stats.min)));
        pw.println(java.lang.String.format(java.util.Locale.US, "%sNumber of connections: %d ", prefix, java.lang.Integer.valueOf(stats.n)));
    }

    public static final class Stats {
        public double average;
        public long max;
        public long min;
        public int n;

        public static com.android.server.backup.transport.TransportStats.Stats merge(com.android.server.backup.transport.TransportStats.Stats a, com.android.server.backup.transport.TransportStats.Stats b) {
            return new com.android.server.backup.transport.TransportStats.Stats(b.n + a.n, ((a.average * ((double) a.n)) + (b.average * ((double) b.n))) / ((double) (a.n + b.n)), java.lang.Math.max(a.max, b.max), java.lang.Math.min(a.min, b.min));
        }

        public Stats() {
            this.n = 0;
            this.average = 0.0d;
            this.max = 0L;
            this.min = Long.MAX_VALUE;
        }

        private Stats(int n, double average, long max, long min) {
            this.n = n;
            this.average = average;
            this.max = max;
            this.min = min;
        }

        private Stats(com.android.server.backup.transport.TransportStats.Stats original) {
            this(original.n, original.average, original.max, original.min);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void register(long sample) {
            this.average = ((this.average * ((double) this.n)) + sample) / ((double) (this.n + 1));
            this.n++;
            this.max = java.lang.Math.max(this.max, sample);
            this.min = java.lang.Math.min(this.min, sample);
        }
    }
}
