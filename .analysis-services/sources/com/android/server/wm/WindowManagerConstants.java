package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class WindowManagerConstants {
    static final java.lang.String KEY_SYSTEM_GESTURE_EXCLUSION_LOG_DEBOUNCE_MILLIS = "system_gesture_exclusion_log_debounce_millis";
    private static final int MIN_GESTURE_EXCLUSION_LIMIT_DP = 200;
    private final android.provider.DeviceConfigInterface mDeviceConfig;
    private final com.android.server.wm.WindowManagerGlobalLock mGlobalLock;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mListenerAndroid;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mListenerWindowManager;
    boolean mSystemGestureExcludedByPreQStickyImmersive;
    int mSystemGestureExclusionLimitDp;
    long mSystemGestureExclusionLogDebounceTimeoutMillis;
    private final java.lang.Runnable mUpdateSystemGestureExclusionCallback;

    WindowManagerConstants(final com.android.server.wm.WindowManagerService service, android.provider.DeviceConfigInterface deviceConfig) {
        this(service.mGlobalLock, new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerConstants$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                service.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerConstants$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wm.DisplayContent) obj).updateSystemGestureExclusionLimit();
                    }
                });
            }
        }, deviceConfig);
    }

    WindowManagerConstants(com.android.server.wm.WindowManagerGlobalLock globalLock, java.lang.Runnable updateSystemGestureExclusionCallback, android.provider.DeviceConfigInterface deviceConfig) {
        this.mGlobalLock = (com.android.server.wm.WindowManagerGlobalLock) java.util.Objects.requireNonNull(globalLock);
        this.mUpdateSystemGestureExclusionCallback = (java.lang.Runnable) java.util.Objects.requireNonNull(updateSystemGestureExclusionCallback);
        this.mDeviceConfig = deviceConfig;
        this.mListenerAndroid = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wm.WindowManagerConstants$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.onAndroidPropertiesChanged(properties);
            }
        };
        this.mListenerWindowManager = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wm.WindowManagerConstants$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.onWindowPropertiesChanged(properties);
            }
        };
    }

    void start(java.util.concurrent.Executor executor) {
        this.mDeviceConfig.addOnPropertiesChangedListener(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, executor, this.mListenerAndroid);
        this.mDeviceConfig.addOnPropertiesChangedListener("window_manager", executor, this.mListenerWindowManager);
        updateSystemGestureExclusionLogDebounceMillis();
        updateSystemGestureExclusionLimitDp();
        updateSystemGestureExcludedByPreQStickyImmersive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onAndroidPropertiesChanged(android.provider.DeviceConfig.Properties r6) {
        /*
            r5 = this;
            com.android.server.wm.WindowManagerGlobalLock r0 = r5.mGlobalLock
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection()
            monitor-enter(r0)
            r1 = 0
            java.util.Set r2 = r6.getKeyset()     // Catch: java.lang.Throwable -> L5c
            java.util.Iterator r2 = r2.iterator()     // Catch: java.lang.Throwable -> L5c
        Lf:
            boolean r3 = r2.hasNext()     // Catch: java.lang.Throwable -> L5c
            if (r3 == 0) goto L50
            java.lang.Object r3 = r2.next()     // Catch: java.lang.Throwable -> L5c
            java.lang.String r3 = (java.lang.String) r3     // Catch: java.lang.Throwable -> L5c
            if (r3 != 0) goto L22
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L5c
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return
        L22:
            int r4 = r3.hashCode()     // Catch: java.lang.Throwable -> L5c
            switch(r4) {
                case -1271675449: goto L35;
                case 316878247: goto L2a;
                default: goto L29;
            }     // Catch: java.lang.Throwable -> L5c
        L29:
            goto L40
        L2a:
            java.lang.String r4 = "system_gesture_exclusion_limit_dp"
            boolean r4 = r3.equals(r4)     // Catch: java.lang.Throwable -> L5c
            if (r4 == 0) goto L29
            r4 = 0
            goto L41
        L35:
            java.lang.String r4 = "system_gestures_excluded_by_pre_q_sticky_immersive"
            boolean r4 = r3.equals(r4)     // Catch: java.lang.Throwable -> L5c
            if (r4 == 0) goto L29
            r4 = 1
            goto L41
        L40:
            r4 = -1
        L41:
            switch(r4) {
                case 0: goto L4a;
                case 1: goto L45;
                default: goto L44;
            }     // Catch: java.lang.Throwable -> L5c
        L44:
            goto L4f
        L45:
            r5.updateSystemGestureExcludedByPreQStickyImmersive()     // Catch: java.lang.Throwable -> L5c
            r1 = 1
            goto L4f
        L4a:
            r5.updateSystemGestureExclusionLimitDp()     // Catch: java.lang.Throwable -> L5c
            r1 = 1
        L4f:
            goto Lf
        L50:
            if (r1 == 0) goto L57
            java.lang.Runnable r2 = r5.mUpdateSystemGestureExclusionCallback     // Catch: java.lang.Throwable -> L5c
            r2.run()     // Catch: java.lang.Throwable -> L5c
        L57:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L5c
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return
        L5c:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L5c
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerConstants.onAndroidPropertiesChanged(android.provider.DeviceConfig$Properties):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onWindowPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
        byte b;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                for (java.lang.String name : properties.getKeyset()) {
                    if (name == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    switch (name.hashCode()) {
                        case -125834358:
                            if (name.equals(KEY_SYSTEM_GESTURE_EXCLUSION_LOG_DEBOUNCE_MILLIS)) {
                                b = 0;
                                break;
                            }
                        default:
                            b = -1;
                            break;
                    }
                    switch (b) {
                        case 0:
                            updateSystemGestureExclusionLogDebounceMillis();
                            break;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void updateSystemGestureExclusionLogDebounceMillis() {
        this.mSystemGestureExclusionLogDebounceTimeoutMillis = this.mDeviceConfig.getLong("window_manager", KEY_SYSTEM_GESTURE_EXCLUSION_LOG_DEBOUNCE_MILLIS, 0L);
    }

    private void updateSystemGestureExclusionLimitDp() {
        this.mSystemGestureExclusionLimitDp = java.lang.Math.max(200, this.mDeviceConfig.getInt(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, "system_gesture_exclusion_limit_dp", 0));
    }

    private void updateSystemGestureExcludedByPreQStickyImmersive() {
        this.mSystemGestureExcludedByPreQStickyImmersive = this.mDeviceConfig.getBoolean(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, "system_gestures_excluded_by_pre_q_sticky_immersive", false);
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("WINDOW MANAGER CONSTANTS (dumpsys window constants):");
        pw.print("  ");
        pw.print(KEY_SYSTEM_GESTURE_EXCLUSION_LOG_DEBOUNCE_MILLIS);
        pw.print("=");
        pw.println(this.mSystemGestureExclusionLogDebounceTimeoutMillis);
        pw.print("  ");
        pw.print("system_gesture_exclusion_limit_dp");
        pw.print("=");
        pw.println(this.mSystemGestureExclusionLimitDp);
        pw.print("  ");
        pw.print("system_gestures_excluded_by_pre_q_sticky_immersive");
        pw.print("=");
        pw.println(this.mSystemGestureExcludedByPreQStickyImmersive);
        pw.println();
    }
}
