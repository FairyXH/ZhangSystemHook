package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class LaunchObserverRegistryImpl extends com.android.server.wm.ActivityMetricsLaunchObserver implements com.android.server.wm.ActivityMetricsLaunchObserverRegistry {
    private final android.os.Handler mHandler;
    private final java.util.ArrayList<com.android.server.wm.ActivityMetricsLaunchObserver> mList = new java.util.ArrayList<>();

    public LaunchObserverRegistryImpl(android.os.Looper looper) {
        this.mHandler = new android.os.Handler(looper);
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserverRegistry
    public void registerLaunchObserver(com.android.server.wm.ActivityMetricsLaunchObserver launchObserver) {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.wm.LaunchObserverRegistryImpl) obj).handleRegisterLaunchObserver((com.android.server.wm.ActivityMetricsLaunchObserver) obj2);
            }
        }, this, launchObserver));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserverRegistry
    public void unregisterLaunchObserver(com.android.server.wm.ActivityMetricsLaunchObserver launchObserver) {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.wm.LaunchObserverRegistryImpl) obj).handleUnregisterLaunchObserver((com.android.server.wm.ActivityMetricsLaunchObserver) obj2);
            }
        }, this, launchObserver));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onIntentStarted(android.content.Intent intent, long timestampNs) {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda5
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.wm.LaunchObserverRegistryImpl) obj).handleOnIntentStarted((android.content.Intent) obj2, ((java.lang.Long) obj3).longValue());
            }
        }, this, intent, java.lang.Long.valueOf(timestampNs)));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onIntentFailed(long id) {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda7
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.wm.LaunchObserverRegistryImpl) obj).handleOnIntentFailed(((java.lang.Long) obj2).longValue());
            }
        }, this, java.lang.Long.valueOf(id)));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onActivityLaunched(long id, android.content.ComponentName name, int temperature, int userId) {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda3
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                ((com.android.server.wm.LaunchObserverRegistryImpl) obj).handleOnActivityLaunched(((java.lang.Long) obj2).longValue(), (android.content.ComponentName) obj3, ((java.lang.Integer) obj4).intValue(), ((java.lang.Integer) obj5).intValue());
            }
        }, this, java.lang.Long.valueOf(id), name, java.lang.Integer.valueOf(temperature), java.lang.Integer.valueOf(userId)));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onActivityLaunchCancelled(long id) {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda6
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.wm.LaunchObserverRegistryImpl) obj).handleOnActivityLaunchCancelled(((java.lang.Long) obj2).longValue());
            }
        }, this, java.lang.Long.valueOf(id)));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onActivityLaunchFinished(long id, android.content.ComponentName name, long timestampNs, int launchMode) {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda4
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                ((com.android.server.wm.LaunchObserverRegistryImpl) obj).handleOnActivityLaunchFinished(((java.lang.Long) obj2).longValue(), (android.content.ComponentName) obj3, ((java.lang.Long) obj4).longValue(), ((java.lang.Integer) obj5).intValue());
            }
        }, this, java.lang.Long.valueOf(id), name, java.lang.Long.valueOf(timestampNs), java.lang.Integer.valueOf(launchMode)));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onReportFullyDrawn(long id, long timestampNs) {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda2
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.wm.LaunchObserverRegistryImpl) obj).handleOnReportFullyDrawn(((java.lang.Long) obj2).longValue(), ((java.lang.Long) obj3).longValue());
            }
        }, this, java.lang.Long.valueOf(id), java.lang.Long.valueOf(timestampNs)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRegisterLaunchObserver(com.android.server.wm.ActivityMetricsLaunchObserver observer) {
        this.mList.add(observer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUnregisterLaunchObserver(com.android.server.wm.ActivityMetricsLaunchObserver observer) {
        this.mList.remove(observer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnIntentStarted(android.content.Intent intent, long timestampNs) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onIntentStarted(intent, timestampNs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnIntentFailed(long id) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onIntentFailed(id);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnActivityLaunched(long id, android.content.ComponentName name, int temperature, int userId) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onActivityLaunched(id, name, temperature, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnActivityLaunchCancelled(long id) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onActivityLaunchCancelled(id);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnActivityLaunchFinished(long id, android.content.ComponentName name, long timestampNs, int launchMode) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onActivityLaunchFinished(id, name, timestampNs, launchMode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnReportFullyDrawn(long id, long timestampNs) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onReportFullyDrawn(id, timestampNs);
        }
    }
}
