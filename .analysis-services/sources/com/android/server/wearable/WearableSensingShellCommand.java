package com.android.server.wearable;

/* JADX INFO: loaded from: classes3.dex */
final class WearableSensingShellCommand extends android.os.ShellCommand {
    private static android.os.ParcelFileDescriptor[] sPipe;
    private final com.android.server.wearable.WearableSensingManagerService mService;
    private static final java.lang.String TAG = com.android.server.wearable.WearableSensingShellCommand.class.getSimpleName();
    static final com.android.server.wearable.WearableSensingShellCommand.TestableCallbackInternal sTestableCallbackInternal = new com.android.server.wearable.WearableSensingShellCommand.TestableCallbackInternal();

    WearableSensingShellCommand(com.android.server.wearable.WearableSensingManagerService service) {
        this.mService = service;
    }

    static class TestableCallbackInternal {
        private int mLastStatus;

        TestableCallbackInternal() {
        }

        public int getLastStatus() {
            return this.mLastStatus;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.os.RemoteCallback createRemoteStatusCallback() {
            return new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.wearable.WearableSensingShellCommand$TestableCallbackInternal$$ExternalSyntheticLambda0
                public final void onResult(android.os.Bundle bundle) {
                    this.f$0.lambda$createRemoteStatusCallback$0(bundle);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createRemoteStatusCallback$0(android.os.Bundle result) {
            int status = result.getInt("android.app.wearable.WearableSensingStatusBundleKey");
            long token = android.os.Binder.clearCallingIdentity();
            try {
                this.mLastStatus = status;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:35:0x006f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r2) {
        /*
            Method dump skipped, instruction units count: 226
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wearable.WearableSensingShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("WearableSensingCommands commands: ");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println();
        pw.println("  create-data-stream: Creates a data stream to be provided.");
        pw.println("  destroy-data-stream: Destroys a data stream if one was previously created.");
        pw.println("  provide-data-stream USER_ID: Provides data stream to WearableSensingService.");
        pw.println("  write-to-data-stream STRING: writes string to data stream.");
        pw.println("  provide-data USER_ID KEY INTEGER: provide integer as data with key.");
        pw.println("  get-last-status-code: Prints the latest request status code.");
        pw.println("  get-bound-package USER_ID:     Print the bound package that implements the service.");
        pw.println("  set-temporary-service USER_ID [PACKAGE_NAME] [COMPONENT_NAME DURATION]");
        pw.println("    Temporarily (for DURATION ms) changes the service implementation.");
        pw.println("    To reset, call with just the USER_ID argument.");
        pw.println("  set-data-request-rate-limit-window-size WINDOW_SIZE");
        pw.println("    Set the window size used in data request rate limiting to WINDOW_SIZE seconds.");
        pw.println("    positive WINDOW_SIZE smaller than 20 will be automatically set to 20.");
        pw.println("    To reset, call with 0 or a negative WINDOW_SIZE.");
    }

    private int createDataStream() {
        android.util.Slog.d(TAG, "createDataStream");
        try {
            sPipe = android.os.ParcelFileDescriptor.createPipe();
            return 0;
        } catch (java.io.IOException e) {
            android.util.Slog.d(TAG, "Failed to createDataStream.", e);
            return 0;
        }
    }

    private int destroyDataStream() {
        android.util.Slog.d(TAG, "destroyDataStream");
        try {
            if (sPipe != null) {
                sPipe[0].close();
                sPipe[1].close();
            }
        } catch (java.io.IOException e) {
            android.util.Slog.d(TAG, "Failed to destroyDataStream.", e);
        }
        return 0;
    }

    private int provideDataStream() {
        android.util.Slog.d(TAG, "provideDataStream");
        if (sPipe != null) {
            int userId = java.lang.Integer.parseInt(getNextArgRequired());
            this.mService.provideDataStream(userId, sPipe[0], sTestableCallbackInternal.createRemoteStatusCallback());
        }
        return 0;
    }

    private int writeToDataStream() {
        android.util.Slog.d(TAG, "writeToDataStream");
        if (sPipe != null) {
            java.lang.String value = getNextArgRequired();
            try {
                android.os.ParcelFileDescriptor writePipe = sPipe[1].dup();
                java.io.OutputStream os = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(writePipe);
                os.write(value.getBytes());
                return 0;
            } catch (java.io.IOException e) {
                android.util.Slog.d(TAG, "Failed to writeToDataStream.", e);
                return 0;
            }
        }
        return 0;
    }

    private int provideData() {
        android.util.Slog.d(TAG, "provideData");
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String key = getNextArgRequired();
        int value = java.lang.Integer.parseInt(getNextArgRequired());
        android.os.PersistableBundle data = new android.os.PersistableBundle();
        data.putInt(key, value);
        this.mService.provideData(userId, data, null, sTestableCallbackInternal.createRemoteStatusCallback());
        return 0;
    }

    private int getLastStatusCode() {
        android.util.Slog.d(TAG, "getLastStatusCode");
        java.io.PrintWriter resultPrinter = getOutPrintWriter();
        int lastStatus = sTestableCallbackInternal.getLastStatus();
        resultPrinter.println(lastStatus);
        return 0;
    }

    private int setTemporaryService() {
        java.io.PrintWriter out = getOutPrintWriter();
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String serviceName = getNextArg();
        if (serviceName == null) {
            this.mService.resetTemporaryService(userId);
            out.println("WearableSensingManagerService temporary reset. ");
            return 0;
        }
        int duration = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setTemporaryService(userId, serviceName, duration);
        out.println("WearableSensingService temporarily set to " + serviceName + " for " + duration + "ms");
        return 0;
    }

    private int getBoundPackageName() {
        java.io.PrintWriter resultPrinter = getOutPrintWriter();
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        android.content.ComponentName componentName = this.mService.getComponentName(userId);
        resultPrinter.println(componentName == null ? "" : componentName.getPackageName());
        return 0;
    }

    private int setDataRequestRateLimitWindowSize() {
        android.util.Slog.d(TAG, "setDataRequestRateLimitWindowSize");
        int windowSizeSeconds = java.lang.Integer.parseInt(getNextArgRequired());
        if (windowSizeSeconds <= 0) {
            this.mService.resetDataRequestRateLimitWindowSize();
            return 0;
        }
        if (windowSizeSeconds < 20) {
            windowSizeSeconds = 20;
        }
        this.mService.setDataRequestRateLimitWindowSize(java.time.Duration.ofSeconds(windowSizeSeconds));
        return 0;
    }
}
