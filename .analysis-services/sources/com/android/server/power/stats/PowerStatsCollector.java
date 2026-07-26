package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public abstract class PowerStatsCollector {
    private static final int MILLIVOLTS_PER_VOLT = 1000;
    private static final long POWER_STATS_ENERGY_CONSUMERS_TIMEOUT = 20000;
    private static final java.lang.String TAG = "PowerStatsCollector";
    protected final com.android.internal.os.Clock mClock;
    private boolean mEnabled;
    private final android.os.Handler mHandler;
    private final long mThrottlePeriodMs;
    protected final com.android.server.power.stats.PowerStatsUidResolver mUidResolver;
    private final java.lang.Runnable mCollectAndDeliverStats = new java.lang.Runnable() { // from class: com.android.server.power.stats.PowerStatsCollector$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.collectAndDeliverStats();
        }
    };
    private long mLastScheduledUpdateMs = -1;
    private volatile java.util.List<java.util.function.Consumer<com.android.internal.os.PowerStats>> mConsumerList = java.util.Collections.emptyList();

    protected abstract com.android.internal.os.PowerStats collectStats();

    public PowerStatsCollector(android.os.Handler handler, long throttlePeriodMs, com.android.server.power.stats.PowerStatsUidResolver uidResolver, com.android.internal.os.Clock clock) {
        this.mHandler = handler;
        this.mThrottlePeriodMs = throttlePeriodMs;
        this.mUidResolver = uidResolver;
        this.mUidResolver.addListener(new com.android.server.power.stats.PowerStatsCollector.AnonymousClass1());
        this.mClock = clock;
    }

    /* JADX INFO: renamed from: com.android.server.power.stats.PowerStatsCollector$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.power.stats.PowerStatsUidResolver.Listener {
        AnonymousClass1() {
        }

        @Override // com.android.server.power.stats.PowerStatsUidResolver.Listener
        public void onIsolatedUidAdded(int isolatedUid, int parentUid) {
        }

        @Override // com.android.server.power.stats.PowerStatsUidResolver.Listener
        public void onBeforeIsolatedUidRemoved(int isolatedUid, int parentUid) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAfterIsolatedUidRemoved$0(int isolatedUid) {
            com.android.server.power.stats.PowerStatsCollector.this.onUidRemoved(isolatedUid);
        }

        @Override // com.android.server.power.stats.PowerStatsUidResolver.Listener
        public void onAfterIsolatedUidRemoved(final int isolatedUid, int parentUid) {
            com.android.server.power.stats.PowerStatsCollector.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.stats.PowerStatsCollector$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAfterIsolatedUidRemoved$0(isolatedUid);
                }
            });
        }
    }

    public void addConsumer(java.util.function.Consumer<com.android.internal.os.PowerStats> consumer) {
        synchronized (this) {
            if (this.mConsumerList.contains(consumer)) {
                return;
            }
            java.util.List<java.util.function.Consumer<com.android.internal.os.PowerStats>> newList = new java.util.ArrayList<>(this.mConsumerList);
            newList.add(consumer);
            this.mConsumerList = java.util.Collections.unmodifiableList(newList);
        }
    }

    public void removeConsumer(java.util.function.Consumer<com.android.internal.os.PowerStats> consumer) {
        synchronized (this) {
            java.util.List<java.util.function.Consumer<com.android.internal.os.PowerStats>> newList = new java.util.ArrayList<>(this.mConsumerList);
            newList.remove(consumer);
            this.mConsumerList = java.util.Collections.unmodifiableList(newList);
        }
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void collectAndDeliverStats() {
        com.android.internal.os.PowerStats stats = collectStats();
        if (stats == null) {
            return;
        }
        java.util.List<java.util.function.Consumer<com.android.internal.os.PowerStats>> consumerList = this.mConsumerList;
        for (int i = consumerList.size() - 1; i >= 0; i--) {
            consumerList.get(i).accept(stats);
        }
    }

    public boolean schedule() {
        if (!this.mEnabled) {
            return false;
        }
        long uptimeMillis = this.mClock.uptimeMillis();
        if (uptimeMillis - this.mLastScheduledUpdateMs < this.mThrottlePeriodMs && this.mLastScheduledUpdateMs >= 0) {
            return false;
        }
        this.mLastScheduledUpdateMs = uptimeMillis;
        this.mHandler.post(this.mCollectAndDeliverStats);
        return true;
    }

    public boolean forceSchedule() {
        if (!this.mEnabled) {
            return false;
        }
        this.mHandler.removeCallbacks(this.mCollectAndDeliverStats);
        this.mHandler.postAtFrontOfQueue(this.mCollectAndDeliverStats);
        return true;
    }

    public void collectAndDump(java.io.PrintWriter pw) {
        if (java.lang.Thread.currentThread() == this.mHandler.getLooper().getThread()) {
            throw new java.lang.RuntimeException("Calling this method from the handler thread would cause a deadlock");
        }
        android.util.IndentingPrintWriter out = new android.util.IndentingPrintWriter(pw);
        out.print(getClass().getSimpleName());
        if (!isEnabled()) {
            out.println(": disabled");
            return;
        }
        out.println();
        final java.util.ArrayList<com.android.internal.os.PowerStats> collected = new java.util.ArrayList<>();
        java.util.Objects.requireNonNull(collected);
        java.util.function.Consumer<com.android.internal.os.PowerStats> consumer = new java.util.function.Consumer() { // from class: com.android.server.power.stats.PowerStatsCollector$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                collected.add((com.android.internal.os.PowerStats) obj);
            }
        };
        addConsumer(consumer);
        try {
            if (forceSchedule()) {
                awaitCompletion();
            }
            removeConsumer(consumer);
            out.increaseIndent();
            for (com.android.internal.os.PowerStats stats : collected) {
                stats.dump(out);
            }
            out.decreaseIndent();
        } catch (java.lang.Throwable th) {
            removeConsumer(consumer);
            throw th;
        }
    }

    private void awaitCompletion() {
        android.os.ConditionVariable done = new android.os.ConditionVariable();
        android.os.Handler handler = this.mHandler;
        java.util.Objects.requireNonNull(done);
        handler.post(new com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda10(done));
        done.block();
    }

    protected void onUidRemoved(int uid) {
    }

    protected static long uJtoUc(long deltaEnergyUj, int avgVoltageMv) {
        return ((1000 * deltaEnergyUj) + ((long) (avgVoltageMv / 2))) / ((long) avgVoltageMv);
    }

    interface ConsumedEnergyRetriever {
        long[] getConsumedEnergyUws(int[] iArr);

        int[] getEnergyConsumerIds(int i, java.lang.String str);

        default int[] getEnergyConsumerIds(int energyConsumerType) {
            return getEnergyConsumerIds(energyConsumerType, null);
        }
    }

    static class ConsumedEnergyRetrieverImpl implements com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever {
        private final android.power.PowerStatsInternal mPowerStatsInternal;

        ConsumedEnergyRetrieverImpl(android.power.PowerStatsInternal powerStatsInternal) {
            this.mPowerStatsInternal = powerStatsInternal;
        }

        @Override // com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever
        public int[] getEnergyConsumerIds(int energyConsumerType, java.lang.String name) {
            if (this.mPowerStatsInternal == null) {
                return new int[0];
            }
            android.hardware.power.stats.EnergyConsumer[] energyConsumerInfo = this.mPowerStatsInternal.getEnergyConsumerInfo();
            if (energyConsumerInfo == null) {
                return new int[0];
            }
            java.util.List<android.hardware.power.stats.EnergyConsumer> energyConsumers = new java.util.ArrayList<>();
            for (android.hardware.power.stats.EnergyConsumer energyConsumer : energyConsumerInfo) {
                if (energyConsumer.type == energyConsumerType && (name == null || name.equals(energyConsumer.name))) {
                    energyConsumers.add(energyConsumer);
                }
            }
            if (energyConsumers.isEmpty()) {
                return new int[0];
            }
            energyConsumers.sort(java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.power.stats.PowerStatsCollector$ConsumedEnergyRetrieverImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return java.lang.Integer.valueOf(((android.hardware.power.stats.EnergyConsumer) obj).ordinal);
                }
            }));
            int[] ids = new int[energyConsumers.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = energyConsumers.get(i).id;
            }
            return ids;
        }

        @Override // com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever
        public long[] getConsumedEnergyUws(int[] energyConsumerIds) throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyConsumerResult[]> future = this.mPowerStatsInternal.getEnergyConsumedAsync(energyConsumerIds);
            android.hardware.power.stats.EnergyConsumerResult[] results = null;
            try {
                results = future.get(com.android.server.power.stats.PowerStatsCollector.POWER_STATS_ENERGY_CONSUMERS_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(com.android.server.power.stats.PowerStatsCollector.TAG, "Could not obtain energy consumers from PowerStatsService", e);
            }
            if (results == null) {
                return null;
            }
            long[] energy = new long[energyConsumerIds.length];
            for (int i = 0; i < energyConsumerIds.length; i++) {
                int id = energyConsumerIds[i];
                int length = results.length;
                int i2 = 0;
                while (true) {
                    if (i2 < length) {
                        android.hardware.power.stats.EnergyConsumerResult result = results[i2];
                        if (result.id != id) {
                            i2++;
                        } else {
                            energy[i] = result.energyUWs;
                            break;
                        }
                    }
                }
            }
            return energy;
        }
    }
}
