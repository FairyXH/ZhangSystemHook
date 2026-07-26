package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class PendingRemoteAnimationRegistry {
    static final long TIMEOUT_MS = 3000;
    private final android.util.ArrayMap<java.lang.String, com.android.server.wm.PendingRemoteAnimationRegistry.Entry> mEntries = new android.util.ArrayMap<>();
    private final android.os.Handler mHandler;
    private final com.android.server.wm.WindowManagerGlobalLock mLock;

    PendingRemoteAnimationRegistry(com.android.server.wm.WindowManagerGlobalLock lock, android.os.Handler handler) {
        this.mLock = lock;
        this.mHandler = handler;
    }

    void addPendingAnimation(java.lang.String packageName, android.view.RemoteAnimationAdapter adapter, android.os.IBinder launchCookie) {
        this.mEntries.put(packageName, new com.android.server.wm.PendingRemoteAnimationRegistry.Entry(packageName, adapter, launchCookie));
    }

    android.app.ActivityOptions overrideOptionsIfNeeded(java.lang.String callingPackage, android.app.ActivityOptions options) {
        com.android.server.wm.PendingRemoteAnimationRegistry.Entry entry = this.mEntries.get(callingPackage);
        if (entry == null) {
            return options;
        }
        if (options == null) {
            options = android.app.ActivityOptions.makeRemoteAnimation(entry.adapter);
        } else {
            options.setRemoteAnimationAdapter(entry.adapter);
        }
        android.os.IBinder launchCookie = entry.launchCookie;
        if (launchCookie != null) {
            options.setLaunchCookie(launchCookie);
        }
        this.mEntries.remove(callingPackage);
        return options;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Entry {
        final android.view.RemoteAnimationAdapter adapter;
        final android.os.IBinder launchCookie;
        final java.lang.String packageName;

        Entry(final java.lang.String packageName, android.view.RemoteAnimationAdapter adapter, android.os.IBinder launchCookie) {
            this.packageName = packageName;
            this.adapter = adapter;
            this.launchCookie = launchCookie;
            com.android.server.wm.PendingRemoteAnimationRegistry.this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.wm.PendingRemoteAnimationRegistry$Entry$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0(packageName);
                }
            }, 3000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(java.lang.String packageName) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.PendingRemoteAnimationRegistry.this.mLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.PendingRemoteAnimationRegistry.Entry entry = (com.android.server.wm.PendingRemoteAnimationRegistry.Entry) com.android.server.wm.PendingRemoteAnimationRegistry.this.mEntries.get(packageName);
                    if (entry == this) {
                        com.android.server.wm.PendingRemoteAnimationRegistry.this.mEntries.remove(packageName);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }
}
