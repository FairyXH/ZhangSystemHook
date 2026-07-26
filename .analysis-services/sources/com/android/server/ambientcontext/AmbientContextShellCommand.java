package com.android.server.ambientcontext;

/* JADX INFO: loaded from: classes.dex */
final class AmbientContextShellCommand extends android.os.ShellCommand {
    private static final int WEARABLE_AMBIENT_CONTEXT_EVENT_FOR_TESTING = 100001;
    private final com.android.server.ambientcontext.AmbientContextManagerService mService;
    private static final java.lang.String TAG = com.android.server.ambientcontext.AmbientContextShellCommand.class.getSimpleName();
    private static final android.app.ambientcontext.AmbientContextEventRequest REQUEST = new android.app.ambientcontext.AmbientContextEventRequest.Builder().addEventType(1).addEventType(2).addEventType(3).build();
    private static final android.app.ambientcontext.AmbientContextEventRequest WEARABLE_REQUEST = new android.app.ambientcontext.AmbientContextEventRequest.Builder().addEventType(100001).build();
    private static final android.app.ambientcontext.AmbientContextEventRequest MIXED_REQUEST = new android.app.ambientcontext.AmbientContextEventRequest.Builder().addEventType(1).addEventType(100001).build();
    static final com.android.server.ambientcontext.AmbientContextShellCommand.TestableCallbackInternal sTestableCallbackInternal = new com.android.server.ambientcontext.AmbientContextShellCommand.TestableCallbackInternal();

    AmbientContextShellCommand(com.android.server.ambientcontext.AmbientContextManagerService service) {
        this.mService = service;
    }

    static class TestableCallbackInternal {
        private java.util.List<android.app.ambientcontext.AmbientContextEvent> mLastEvents;
        private int mLastStatus;

        TestableCallbackInternal() {
        }

        public java.util.List<android.app.ambientcontext.AmbientContextEvent> getLastEvents() {
            return this.mLastEvents;
        }

        public int getLastStatus() {
            return this.mLastStatus;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.app.ambientcontext.IAmbientContextObserver createAmbientContextObserver() {
            return new android.app.ambientcontext.IAmbientContextObserver.Stub() { // from class: com.android.server.ambientcontext.AmbientContextShellCommand.TestableCallbackInternal.1
                public void onEvents(java.util.List<android.app.ambientcontext.AmbientContextEvent> events) throws android.os.RemoteException {
                    com.android.server.ambientcontext.AmbientContextShellCommand.TestableCallbackInternal.this.mLastEvents = events;
                    java.lang.System.out.println("Detection events available: " + events);
                }

                public void onRegistrationComplete(int statusCode) throws android.os.RemoteException {
                    com.android.server.ambientcontext.AmbientContextShellCommand.TestableCallbackInternal.this.mLastStatus = statusCode;
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.os.RemoteCallback createRemoteStatusCallback() {
            return new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.ambientcontext.AmbientContextShellCommand$TestableCallbackInternal$$ExternalSyntheticLambda0
                public final void onResult(android.os.Bundle bundle) {
                    this.f$0.lambda$createRemoteStatusCallback$0(bundle);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createRemoteStatusCallback$0(android.os.Bundle result) {
            int status = result.getInt("android.app.ambientcontext.AmbientContextStatusBundleKey");
            long token = android.os.Binder.clearCallingIdentity();
            try {
                this.mLastStatus = status;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:41:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r2) {
        /*
            Method dump skipped, instruction units count: 278
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.ambientcontext.AmbientContextShellCommand.onCommand(java.lang.String):int");
    }

    private int runStartDetection() {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String packageName = getNextArgRequired();
        this.mService.startDetection(userId, REQUEST, packageName, sTestableCallbackInternal.createAmbientContextObserver());
        this.mService.newClientAdded(userId, REQUEST, packageName, sTestableCallbackInternal.createAmbientContextObserver());
        return 0;
    }

    private int runWearableStartDetection() {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String packageName = getNextArgRequired();
        this.mService.startDetection(userId, WEARABLE_REQUEST, packageName, sTestableCallbackInternal.createAmbientContextObserver());
        this.mService.newClientAdded(userId, WEARABLE_REQUEST, packageName, sTestableCallbackInternal.createAmbientContextObserver());
        return 0;
    }

    private int runMixedStartDetection() {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String packageName = getNextArgRequired();
        this.mService.startDetection(userId, MIXED_REQUEST, packageName, sTestableCallbackInternal.createAmbientContextObserver());
        this.mService.newClientAdded(userId, MIXED_REQUEST, packageName, sTestableCallbackInternal.createAmbientContextObserver());
        return 0;
    }

    private int runStopDetection() {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String packageName = getNextArgRequired();
        this.mService.stopAmbientContextEvent(userId, packageName);
        return 0;
    }

    private int runQueryServiceStatus() {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String packageName = getNextArgRequired();
        int[] types = {1, 2};
        this.mService.queryServiceStatus(userId, packageName, types, sTestableCallbackInternal.createRemoteStatusCallback());
        return 0;
    }

    private int runQueryWearableServiceStatus() {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String packageName = getNextArgRequired();
        int[] types = {100001};
        this.mService.queryServiceStatus(userId, packageName, types, sTestableCallbackInternal.createRemoteStatusCallback());
        return 0;
    }

    private int runQueryMixedServiceStatus() {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String packageName = getNextArgRequired();
        int[] types = {1, 100001};
        this.mService.queryServiceStatus(userId, packageName, types, sTestableCallbackInternal.createRemoteStatusCallback());
        return 0;
    }

    private int getLastStatusCode() {
        java.io.PrintWriter resultPrinter = getOutPrintWriter();
        int lastStatus = sTestableCallbackInternal.getLastStatus();
        resultPrinter.println(lastStatus);
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("AmbientContextEvent commands: ");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println();
        pw.println("  start-detection USER_ID PACKAGE_NAME: Starts AmbientContextEvent detection.");
        pw.println("  start-detection-wearable USER_ID PACKAGE_NAME: Starts AmbientContextEvent detection for wearable.");
        pw.println("  start-detection-mixed USER_ID PACKAGE_NAME:  Starts AmbientContextEvent detection for mixed events.");
        pw.println("  stop-detection USER_ID PACKAGE_NAME: Stops AmbientContextEvent detection.");
        pw.println("  get-last-status-code: Prints the latest request status code.");
        pw.println("  query-service-status USER_ID PACKAGE_NAME: Prints the service status code.");
        pw.println("  query-wearable-service-status USER_ID PACKAGE_NAME: Prints the service status code for wearable.");
        pw.println("  query-mixed-service-status USER_ID PACKAGE_NAME: Prints the service status code for mixed events.");
        pw.println("  get-bound-package USER_ID:     Print the bound package that implements the service.");
        pw.println("  set-temporary-service USER_ID [PACKAGE_NAME] [COMPONENT_NAME DURATION]");
        pw.println("    Temporarily (for DURATION ms) changes the service implementation.");
        pw.println("    To reset, call with just the USER_ID argument.");
        pw.println("  set-temporary-services USER_ID [FIRST_PACKAGE_NAME] [SECOND_PACKAGE_NAME] [COMPONENT_NAME DURATION]");
        pw.println("    Temporarily (for DURATION ms) changes the service implementation.");
        pw.println("    To reset, call with just the USER_ID argument.");
    }

    private int getBoundPackageName() {
        java.io.PrintWriter resultPrinter = getOutPrintWriter();
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        android.content.ComponentName componentName = this.mService.getComponentName(userId, com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.DEFAULT);
        resultPrinter.println(componentName == null ? "" : componentName.getPackageName());
        return 0;
    }

    private int setTemporaryService() {
        java.io.PrintWriter out = getOutPrintWriter();
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String serviceName = getNextArg();
        if (serviceName == null) {
            this.mService.resetTemporaryService(userId);
            out.println("AmbientContextDetectionService temporary reset. ");
            this.mService.setDefaultServiceEnabled(userId, true);
            return 0;
        }
        int duration = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setTemporaryService(userId, serviceName, duration);
        out.println("AmbientContextDetectionService temporarily set to " + serviceName + " for " + duration + "ms");
        return 0;
    }

    private int setTemporaryServices() {
        java.lang.String[] serviceNames = new java.lang.String[2];
        java.io.PrintWriter out = getOutPrintWriter();
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setDefaultServiceEnabled(userId, false);
        java.lang.String firstServiceName = getNextArg();
        java.lang.String secondServiceName = getNextArg();
        if (firstServiceName == null || secondServiceName == null) {
            this.mService.resetTemporaryService(userId);
            this.mService.setDefaultServiceEnabled(userId, true);
            out.println("AmbientContextDetectionService temporary reset.");
            return 0;
        }
        serviceNames[0] = firstServiceName;
        serviceNames[1] = secondServiceName;
        int duration = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setTemporaryServices(userId, serviceNames, duration);
        android.util.Slog.w(TAG, "AmbientContextDetectionService temporarily set to " + serviceNames[0] + " and " + serviceNames[1] + " for " + duration + "ms");
        out.println("AmbientContextDetectionService temporarily set to " + serviceNames[0] + " and " + serviceNames[1] + " for " + duration + "ms");
        return 0;
    }
}
