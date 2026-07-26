package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilityTraceManager implements android.accessibilityservice.AccessibilityTrace {
    private static com.android.server.accessibility.AccessibilityTraceManager sInstance = null;
    private final com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal mA11yController;
    private final java.lang.Object mA11yMSLock;
    private volatile long mEnabledLoggingFlags = 0;
    private final com.android.server.accessibility.AccessibilityManagerService mService;

    static com.android.server.accessibility.AccessibilityTraceManager getInstance(com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal a11yController, com.android.server.accessibility.AccessibilityManagerService service, java.lang.Object lock) {
        if (sInstance == null) {
            sInstance = new com.android.server.accessibility.AccessibilityTraceManager(a11yController, service, lock);
        }
        return sInstance;
    }

    private AccessibilityTraceManager(com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal a11yController, com.android.server.accessibility.AccessibilityManagerService service, java.lang.Object lock) {
        this.mA11yController = a11yController;
        this.mService = service;
        this.mA11yMSLock = lock;
    }

    public boolean isA11yTracingEnabled() {
        return this.mEnabledLoggingFlags != 0;
    }

    public boolean isA11yTracingEnabledForTypes(long typeIdFlags) {
        return (this.mEnabledLoggingFlags & typeIdFlags) != 0;
    }

    public int getTraceStateForAccessibilityManagerClientState() {
        int state = 0;
        if (isA11yTracingEnabledForTypes(16L)) {
            state = 0 | 256;
        }
        if (isA11yTracingEnabledForTypes(32L)) {
            state |= 512;
        }
        if (isA11yTracingEnabledForTypes(262144L)) {
            state |= 1024;
        }
        if (isA11yTracingEnabledForTypes(16384L)) {
            return state | 2048;
        }
        return state;
    }

    public void startTrace(long loggingTypes) {
        if (loggingTypes == 0) {
            return;
        }
        long oldEnabled = this.mEnabledLoggingFlags;
        this.mEnabledLoggingFlags = loggingTypes;
        if (needToNotifyClients(oldEnabled)) {
            synchronized (this.mA11yMSLock) {
                this.mService.scheduleUpdateClientsIfNeededLocked(this.mService.getCurrentUserState());
            }
        }
        this.mA11yController.startTrace(loggingTypes);
    }

    public void stopTrace() {
        boolean stop = isA11yTracingEnabled();
        long oldEnabled = this.mEnabledLoggingFlags;
        this.mEnabledLoggingFlags = 0L;
        if (needToNotifyClients(oldEnabled)) {
            synchronized (this.mA11yMSLock) {
                this.mService.scheduleUpdateClientsIfNeededLocked(this.mService.getCurrentUserState());
            }
        }
        if (stop) {
            this.mA11yController.stopTrace();
        }
    }

    public void logTrace(java.lang.String where, long loggingTypes) {
        logTrace(where, loggingTypes, "");
    }

    public void logTrace(java.lang.String where, long loggingTypes, java.lang.String callingParams) {
        if (isA11yTracingEnabledForTypes(loggingTypes)) {
            this.mA11yController.logTrace(where, loggingTypes, callingParams, "".getBytes(), android.os.Binder.getCallingUid(), java.lang.Thread.currentThread().getStackTrace(), new java.util.HashSet(java.util.Arrays.asList("logTrace")));
        }
    }

    public void logTrace(long timestamp, java.lang.String where, long loggingTypes, java.lang.String callingParams, int processId, long threadId, int callingUid, java.lang.StackTraceElement[] callStack, java.util.Set<java.lang.String> ignoreElementList) {
        if (isA11yTracingEnabledForTypes(loggingTypes)) {
            this.mA11yController.logTrace(where, loggingTypes, callingParams, "".getBytes(), callingUid, callStack, timestamp, processId, threadId, ignoreElementList == null ? new java.util.HashSet() : ignoreElementList);
        }
    }

    private boolean needToNotifyClients(long otherTypesEnabled) {
        return (this.mEnabledLoggingFlags & 278576) != (278576 & otherTypesEnabled);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int onShellCommand(java.lang.String r7, android.os.ShellCommand r8) {
        /*
            r6 = this;
            int r0 = r7.hashCode()
            r1 = 0
            r2 = -1
            switch(r0) {
                case 1340897306: goto L15;
                case 1857979322: goto La;
                default: goto L9;
            }
        L9:
            goto L20
        La:
            java.lang.String r0 = "stop-trace"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L9
            r0 = 1
            goto L21
        L15:
            java.lang.String r0 = "start-trace"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L9
            r0 = r1
            goto L21
        L20:
            r0 = r2
        L21:
            switch(r0) {
                case 0: goto L29;
                case 1: goto L25;
                default: goto L24;
            }
        L24:
            return r2
        L25:
            r6.stopTrace()
            return r1
        L29:
            java.lang.String r0 = r8.getNextOption()
            if (r0 != 0) goto L35
            r2 = -1
            r6.startTrace(r2)
            return r1
        L35:
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
        L3a:
            if (r0 == 0) goto L83
            int r4 = r0.hashCode()
            switch(r4) {
                case 1511: goto L44;
                default: goto L43;
            }
        L43:
            goto L4e
        L44:
            java.lang.String r4 = "-t"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L43
            r4 = r1
            goto L4f
        L4e:
            r4 = r2
        L4f:
            switch(r4) {
                case 0: goto L70;
                default: goto L52;
            }
        L52:
            java.io.PrintWriter r1 = r8.getErrPrintWriter()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Error: option not recognized "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r4 = r4.toString()
            r1.println(r4)
            r6.stopTrace()
            return r2
        L70:
            java.lang.String r4 = r8.getNextArg()
        L74:
            if (r4 == 0) goto L7e
            r3.add(r4)
            java.lang.String r4 = r8.getNextArg()
            goto L74
        L7e:
            java.lang.String r0 = r8.getNextOption()
            goto L3a
        L83:
            long r4 = android.accessibilityservice.AccessibilityTrace.getLoggingFlagsFromNames(r3)
            r6.startTrace(r4)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.accessibility.AccessibilityTraceManager.onShellCommand(java.lang.String, android.os.ShellCommand):int");
    }

    void onHelp(java.io.PrintWriter pw) {
        pw.println("  start-trace [-t LOGGING_TYPE [LOGGING_TYPE...]]");
        pw.println("    Start the debug tracing. If no option is present, full trace will be");
        pw.println("    generated. Options are:");
        pw.println("      -t: Only generate tracing for the logging type(s) specified here.");
        pw.println("          LOGGING_TYPE can be any one of below:");
        pw.println("            IAccessibilityServiceConnection");
        pw.println("            IAccessibilityServiceClient");
        pw.println("            IAccessibilityManager");
        pw.println("            IAccessibilityManagerClient");
        pw.println("            IAccessibilityInteractionConnection");
        pw.println("            IAccessibilityInteractionConnectionCallback");
        pw.println("            IRemoteMagnificationAnimationCallback");
        pw.println("            IMagnificationConnection");
        pw.println("            IMagnificationConnectionCallback");
        pw.println("            WindowManagerInternal");
        pw.println("            WindowsForAccessibilityCallback");
        pw.println("            MagnificationCallbacks");
        pw.println("            InputFilter");
        pw.println("            Gesture");
        pw.println("            AccessibilityService");
        pw.println("            PMBroadcastReceiver");
        pw.println("            UserBroadcastReceiver");
        pw.println("            FingerprintGesture");
        pw.println("  stop-trace");
        pw.println("    Stop the debug tracing.");
    }
}
