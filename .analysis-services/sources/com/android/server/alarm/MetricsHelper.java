package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
class MetricsHelper {
    private final android.content.Context mContext;
    private final java.lang.Object mLock;

    MetricsHelper(android.content.Context context, java.lang.Object lock) {
        this.mContext = context;
        this.mLock = lock;
    }

    void registerPuller(final java.util.function.Supplier<com.android.server.alarm.AlarmStore> alarmStoreSupplier) {
        android.app.StatsManager statsManager = (android.app.StatsManager) this.mContext.getSystemService(android.app.StatsManager.class);
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PENDING_ALARM_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new android.app.StatsManager.StatsPullAtomCallback() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda0
            public final int onPullAtom(int i, java.util.List list) {
                return this.f$0.lambda$registerPuller$12(alarmStoreSupplier, i, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$registerPuller$12(java.util.function.Supplier alarmStoreSupplier, int atomTag, java.util.List data) throws java.lang.Throwable {
        java.lang.Object obj;
        if (atomTag != 10106) {
            throw new java.lang.UnsupportedOperationException("Unknown tag" + atomTag);
        }
        final long now = android.os.SystemClock.elapsedRealtime();
        java.lang.Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
                try {
                    com.android.server.alarm.AlarmStore alarmStore = (com.android.server.alarm.AlarmStore) alarmStoreSupplier.get();
                    obj = obj2;
                    try {
                        data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, alarmStore.size(), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda1
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.MetricsHelper.lambda$registerPuller$0((com.android.server.alarm.Alarm) obj3);
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda4
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return ((com.android.server.alarm.Alarm) obj3).wakeup;
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda5
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.MetricsHelper.lambda$registerPuller$2((com.android.server.alarm.Alarm) obj3);
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda6
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.MetricsHelper.lambda$registerPuller$3((com.android.server.alarm.Alarm) obj3);
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda7
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.MetricsHelper.lambda$registerPuller$4((com.android.server.alarm.Alarm) obj3);
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda8
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.MetricsHelper.lambda$registerPuller$5((com.android.server.alarm.Alarm) obj3);
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda9
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.MetricsHelper.lambda$registerPuller$6((com.android.server.alarm.Alarm) obj3);
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda10
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.MetricsHelper.lambda$registerPuller$7((com.android.server.alarm.Alarm) obj3);
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda11
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.MetricsHelper.lambda$registerPuller$8(now, (com.android.server.alarm.Alarm) obj3);
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda12
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.MetricsHelper.lambda$registerPuller$9((com.android.server.alarm.Alarm) obj3);
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda2
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.MetricsHelper.lambda$registerPuller$10((com.android.server.alarm.Alarm) obj3);
                            }
                        }), alarmStore.getCount(new java.util.function.Predicate() { // from class: com.android.server.alarm.MetricsHelper$$ExternalSyntheticLambda3
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj3) {
                                return com.android.server.alarm.AlarmManagerService.isRtc(((com.android.server.alarm.Alarm) obj3).type);
                            }
                        })));
                        return 0;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    obj = obj2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    static /* synthetic */ boolean lambda$registerPuller$0(com.android.server.alarm.Alarm a) {
        return a.windowLength == 0;
    }

    static /* synthetic */ boolean lambda$registerPuller$2(com.android.server.alarm.Alarm a) {
        return (a.flags & 4) != 0;
    }

    static /* synthetic */ boolean lambda$registerPuller$3(com.android.server.alarm.Alarm a) {
        return (a.flags & 64) != 0;
    }

    static /* synthetic */ boolean lambda$registerPuller$4(com.android.server.alarm.Alarm a) {
        return a.operation != null && a.operation.isForegroundService();
    }

    static /* synthetic */ boolean lambda$registerPuller$5(com.android.server.alarm.Alarm a) {
        return a.operation != null && a.operation.isActivity();
    }

    static /* synthetic */ boolean lambda$registerPuller$6(com.android.server.alarm.Alarm a) {
        return a.operation != null && a.operation.isService();
    }

    static /* synthetic */ boolean lambda$registerPuller$7(com.android.server.alarm.Alarm a) {
        return a.listener != null;
    }

    static /* synthetic */ boolean lambda$registerPuller$8(long now, com.android.server.alarm.Alarm a) {
        return a.getRequestedElapsed() > 31536000000L + now;
    }

    static /* synthetic */ boolean lambda$registerPuller$9(com.android.server.alarm.Alarm a) {
        return a.repeatInterval != 0;
    }

    static /* synthetic */ boolean lambda$registerPuller$10(com.android.server.alarm.Alarm a) {
        return a.alarmClock != null;
    }

    private static int reasonToStatsReason(int reasonCode) {
        switch (reasonCode) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            default:
                return 0;
        }
    }

    static void pushAlarmScheduled(com.android.server.alarm.Alarm a, int callerProcState) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.ALARM_SCHEDULED, a.uid, a.windowLength == 0, a.wakeup, (a.flags & 4) != 0, a.alarmClock != null, a.repeatInterval != 0, reasonToStatsReason(a.exactAllowReason), com.android.server.alarm.AlarmManagerService.isRtc(a.type), android.app.ActivityManager.processStateAmToProto(callerProcState));
    }

    static void pushAlarmBatchDelivered(int numAlarms, int wakeups, int[] uids, int[] alarmsPerUid, int[] wakeupAlarmsPerUid) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.ALARM_BATCH_DELIVERED, numAlarms, wakeups, uids, alarmsPerUid, wakeupAlarmsPerUid);
    }
}
