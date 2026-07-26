package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public final class AutofillManagerServiceShellCommand extends android.os.ShellCommand {
    private final com.android.server.autofill.AutofillManagerService mService;

    public AutofillManagerServiceShellCommand(com.android.server.autofill.AutofillManagerService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0053  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r3) {
        /*
            r2 = this;
            if (r3 != 0) goto L7
            int r0 = r2.handleDefaultCommands(r3)
            return r0
        L7:
            java.io.PrintWriter r0 = r2.getOutPrintWriter()
            int r1 = r3.hashCode()
            switch(r1) {
                case 102230: goto L48;
                case 113762: goto L3d;
                case 3322014: goto L32;
                case 97513095: goto L28;
                case 108404047: goto L1d;
                case 1557372922: goto L13;
                default: goto L12;
            }
        L12:
            goto L53
        L13:
            java.lang.String r1 = "destroy"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 1
            goto L54
        L1d:
            java.lang.String r1 = "reset"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 2
            goto L54
        L28:
            java.lang.String r1 = "flags"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 5
            goto L54
        L32:
            java.lang.String r1 = "list"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 0
            goto L54
        L3d:
            java.lang.String r1 = "set"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 4
            goto L54
        L48:
            java.lang.String r1 = "get"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 3
            goto L54
        L53:
            r1 = -1
        L54:
            switch(r1) {
                case 0: goto L75;
                case 1: goto L70;
                case 2: goto L6b;
                case 3: goto L66;
                case 4: goto L61;
                case 5: goto L5c;
                default: goto L57;
            }
        L57:
            int r1 = r2.handleDefaultCommands(r3)
            return r1
        L5c:
            int r1 = r2.requestFlags(r0)
            return r1
        L61:
            int r1 = r2.requestSet(r0)
            return r1
        L66:
            int r1 = r2.requestGet(r0)
            return r1
        L6b:
            int r1 = r2.requestReset()
            return r1
        L70:
            int r1 = r2.requestDestroy(r0)
            return r1
        L75:
            int r1 = r2.requestList(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.autofill.AutofillManagerServiceShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            pw.println("AutoFill Service (autofill) commands:");
            pw.println("  help");
            pw.println("    Prints this help text.");
            pw.println("");
            pw.println("  get log_level ");
            pw.println("    Gets the Autofill log level (off | debug | verbose).");
            pw.println("");
            pw.println("  get max_partitions");
            pw.println("    Gets the maximum number of partitions per session.");
            pw.println("");
            pw.println("  get max_visible_datasets");
            pw.println("    Gets the maximum number of visible datasets in the UI.");
            pw.println("");
            pw.println("  get full_screen_mode");
            pw.println("    Gets the Fill UI full screen mode");
            pw.println("");
            pw.println("  get fc_score [--algorithm ALGORITHM] value1 value2");
            pw.println("    Gets the field classification score for 2 fields.");
            pw.println("");
            pw.println("  get bind-instant-service-allowed");
            pw.println("    Gets whether binding to services provided by instant apps is allowed");
            pw.println("");
            pw.println("  get saved-password-count");
            pw.println("    Gets the number of saved passwords in the current service.");
            pw.println("");
            pw.println("  set log_level [off | debug | verbose]");
            pw.println("    Sets the Autofill log level.");
            pw.println("");
            pw.println("  set max_partitions number");
            pw.println("    Sets the maximum number of partitions per session.");
            pw.println("");
            pw.println("  set max_visible_datasets number");
            pw.println("    Sets the maximum number of visible datasets in the UI.");
            pw.println("");
            pw.println("  set full_screen_mode [true | false | default]");
            pw.println("    Sets the Fill UI full screen mode");
            pw.println("");
            pw.println("  set bind-instant-service-allowed [true | false]");
            pw.println("    Sets whether binding to services provided by instant apps is allowed");
            pw.println("");
            pw.println("  set temporary-augmented-service USER_ID [COMPONENT_NAME DURATION]");
            pw.println("    Temporarily (for DURATION ms) changes the augmented autofill service implementation.");
            pw.println("    To reset, call with just the USER_ID argument.");
            pw.println("");
            pw.println("  set default-augmented-service-enabled USER_ID [true|false]");
            pw.println("    Enable / disable the default augmented autofill service for the user.");
            pw.println("");
            pw.println("  set temporary-detection-service USER_ID [COMPONENT_NAME DURATION]");
            pw.println("    Temporarily (for DURATION ms) changes the autofill detection service implementation.");
            pw.println("    To reset, call with [COMPONENT_NAME 0].");
            pw.println("");
            pw.println("  get default-augmented-service-enabled USER_ID");
            pw.println("    Checks whether the default augmented autofill service is enabled for the user.");
            pw.println("");
            pw.println("  list sessions [--user USER_ID]");
            pw.println("    Lists all pending sessions.");
            pw.println("");
            pw.println("  destroy sessions [--user USER_ID]");
            pw.println("    Destroys all pending sessions.");
            pw.println("");
            pw.println("  reset");
            pw.println("    Resets all pending sessions and cached service connections.");
            pw.println("");
            pw.println("  flags");
            pw.println("    Prints out all autofill related flags.");
            pw.println("");
            if (pw != null) {
                pw.close();
            }
        } catch (java.lang.Throwable th) {
            if (pw != null) {
                try {
                    pw.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private int requestFlags(java.io.PrintWriter pw) {
        if (android.service.autofill.Flags.test()) {
            pw.println("Hello Flag World!");
        }
        java.lang.reflect.Method[] methodArr = new java.lang.reflect.Method[0];
        try {
            java.lang.reflect.Method[] flagMethods = android.service.autofill.Flags.class.getDeclaredMethods();
            for (java.lang.reflect.Method method : flagMethods) {
                if (java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
                    try {
                        try {
                            pw.print(method.getName() + ": ");
                            pw.print(method.invoke(null, new java.lang.Object[0]));
                        } finally {
                            pw.println("");
                        }
                    } catch (java.lang.Exception ex) {
                        ex.printStackTrace(pw);
                    }
                }
            }
            return 0;
        } catch (java.lang.SecurityException ex2) {
            ex2.printStackTrace(pw);
            return -1;
        }
    }

    private int requestGet(java.io.PrintWriter pw) {
        byte b;
        java.lang.String what = getNextArgRequired();
        switch (what.hashCode()) {
            case -2124387184:
                b = !what.equals("fc_score") ? (byte) -1 : (byte) 3;
                break;
            case -2006901047:
                b = !what.equals("log_level") ? (byte) -1 : (byte) 0;
                break;
            case -1298810906:
                b = !what.equals("full_screen_mode") ? (byte) -1 : (byte) 4;
                break;
            case -633247282:
                b = !what.equals("field-detection-service-enabled") ? (byte) -1 : (byte) 7;
                break;
            case -255918237:
                b = !what.equals("saved-password-count") ? (byte) -1 : (byte) 8;
                break;
            case 809633044:
                b = !what.equals("bind-instant-service-allowed") ? (byte) -1 : (byte) 5;
                break;
            case 852405952:
                b = !what.equals("default-augmented-service-enabled") ? (byte) -1 : (byte) 6;
                break;
            case 1393110435:
                b = !what.equals("max_visible_datasets") ? (byte) -1 : (byte) 2;
                break;
            case 1772188804:
                b = !what.equals("max_partitions") ? (byte) -1 : (byte) 1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return getLogLevel(pw);
            case 1:
                return getMaxPartitions(pw);
            case 2:
                return getMaxVisibileDatasets(pw);
            case 3:
                return getFieldClassificationScore(pw);
            case 4:
                return getFullScreenMode(pw);
            case 5:
                return getBindInstantService(pw);
            case 6:
                return getDefaultAugmentedServiceEnabled(pw);
            case 7:
                return isFieldDetectionServiceEnabled(pw);
            case 8:
                return getSavedPasswordCount(pw);
            default:
                pw.println("Invalid set: " + what);
                return -1;
        }
    }

    private int requestSet(java.io.PrintWriter pw) {
        byte b;
        java.lang.String what = getNextArgRequired();
        switch (what.hashCode()) {
            case -2006901047:
                b = !what.equals("log_level") ? (byte) -1 : (byte) 0;
                break;
            case -1298810906:
                b = !what.equals("full_screen_mode") ? (byte) -1 : (byte) 3;
                break;
            case -571600804:
                b = !what.equals("temporary-augmented-service") ? (byte) -1 : (byte) 5;
                break;
            case 809633044:
                b = !what.equals("bind-instant-service-allowed") ? (byte) -1 : (byte) 4;
                break;
            case 852405952:
                b = !what.equals("default-augmented-service-enabled") ? (byte) -1 : (byte) 6;
                break;
            case 1393110435:
                b = !what.equals("max_visible_datasets") ? (byte) -1 : (byte) 2;
                break;
            case 1772188804:
                b = !what.equals("max_partitions") ? (byte) -1 : (byte) 1;
                break;
            case 2027866865:
                b = !what.equals("temporary-detection-service") ? (byte) -1 : (byte) 7;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return setLogLevel(pw);
            case 1:
                return setMaxPartitions();
            case 2:
                return setMaxVisibileDatasets();
            case 3:
                return setFullScreenMode(pw);
            case 4:
                return setBindInstantService(pw);
            case 5:
                return setTemporaryAugmentedService(pw);
            case 6:
                return setDefaultAugmentedServiceEnabled(pw);
            case 7:
                return setTemporaryDetectionService(pw);
            default:
                pw.println("Invalid set: " + what);
                return -1;
        }
    }

    private int getLogLevel(java.io.PrintWriter pw) {
        int logLevel = this.mService.getLogLevel();
        switch (logLevel) {
            case 0:
                pw.println(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF);
                break;
            case 1:
            case 3:
            default:
                pw.println("unknow (" + logLevel + ")");
                break;
            case 2:
                pw.println("debug");
                break;
            case 4:
                pw.println("verbose");
                break;
        }
        return 0;
    }

    private int setLogLevel(java.io.PrintWriter pw) {
        byte b;
        java.lang.String logLevel = getNextArgRequired();
        java.lang.String lowerCase = logLevel.toLowerCase();
        switch (lowerCase.hashCode()) {
            case 109935:
                b = !lowerCase.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF) ? (byte) -1 : (byte) 2;
                break;
            case 95458899:
                b = !lowerCase.equals("debug") ? (byte) -1 : (byte) 1;
                break;
            case 351107458:
                b = !lowerCase.equals("verbose") ? (byte) -1 : (byte) 0;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                this.mService.setLogLevel(4);
                return 0;
            case 1:
                this.mService.setLogLevel(2);
                return 0;
            case 2:
                this.mService.setLogLevel(0);
                return 0;
            default:
                pw.println("Invalid level: " + logLevel);
                return -1;
        }
    }

    private int getMaxPartitions(java.io.PrintWriter pw) {
        pw.println(this.mService.getMaxPartitions());
        return 0;
    }

    private int setMaxPartitions() {
        this.mService.setMaxPartitions(java.lang.Integer.parseInt(getNextArgRequired()));
        return 0;
    }

    private int getMaxVisibileDatasets(java.io.PrintWriter pw) {
        pw.println(this.mService.getMaxVisibleDatasets());
        return 0;
    }

    private int setMaxVisibileDatasets() {
        this.mService.setMaxVisibleDatasets(java.lang.Integer.parseInt(getNextArgRequired()));
        return 0;
    }

    private int getFieldClassificationScore(final java.io.PrintWriter pw) {
        java.lang.String algorithm;
        java.lang.String value1;
        java.lang.String nextArg = getNextArgRequired();
        if ("--algorithm".equals(nextArg)) {
            algorithm = getNextArgRequired();
            value1 = getNextArgRequired();
        } else {
            algorithm = null;
            value1 = nextArg;
        }
        java.lang.String value2 = getNextArgRequired();
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        this.mService.calculateScore(algorithm, value1, value2, new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.autofill.AutofillManagerServiceShellCommand$$ExternalSyntheticLambda0
            public final void onResult(android.os.Bundle bundle) {
                com.android.server.autofill.AutofillManagerServiceShellCommand.lambda$getFieldClassificationScore$0(pw, latch, bundle);
            }
        }));
        return waitForLatch(pw, latch);
    }

    static /* synthetic */ void lambda$getFieldClassificationScore$0(java.io.PrintWriter pw, java.util.concurrent.CountDownLatch latch, android.os.Bundle result) {
        android.service.autofill.AutofillFieldClassificationService.Scores scores = (android.service.autofill.AutofillFieldClassificationService.Scores) result.getParcelable("scores", android.service.autofill.AutofillFieldClassificationService.Scores.class);
        if (scores == null) {
            pw.println("no score");
        } else {
            pw.println(scores.scores[0][0]);
        }
        latch.countDown();
    }

    private int getFullScreenMode(java.io.PrintWriter pw) {
        java.lang.Boolean mode = this.mService.getFullScreenMode();
        if (mode == null) {
            pw.println("default");
            return 0;
        }
        if (mode.booleanValue()) {
            pw.println("true");
            return 0;
        }
        pw.println("false");
        return 0;
    }

    private int setFullScreenMode(java.io.PrintWriter pw) {
        byte b;
        java.lang.String mode = getNextArgRequired();
        java.lang.String lowerCase = mode.toLowerCase();
        switch (lowerCase.hashCode()) {
            case 3569038:
                b = !lowerCase.equals("true") ? (byte) -1 : (byte) 0;
                break;
            case 97196323:
                b = !lowerCase.equals("false") ? (byte) -1 : (byte) 1;
                break;
            case 1544803905:
                b = !lowerCase.equals("default") ? (byte) -1 : (byte) 2;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                this.mService.setFullScreenMode(java.lang.Boolean.TRUE);
                return 0;
            case 1:
                this.mService.setFullScreenMode(java.lang.Boolean.FALSE);
                return 0;
            case 2:
                this.mService.setFullScreenMode(null);
                return 0;
            default:
                pw.println("Invalid mode: " + mode);
                return -1;
        }
    }

    private int getBindInstantService(java.io.PrintWriter pw) {
        if (this.mService.getAllowInstantService()) {
            pw.println("true");
            return 0;
        }
        pw.println("false");
        return 0;
    }

    private int setBindInstantService(java.io.PrintWriter pw) {
        byte b;
        java.lang.String mode = getNextArgRequired();
        java.lang.String lowerCase = mode.toLowerCase();
        switch (lowerCase.hashCode()) {
            case 3569038:
                b = !lowerCase.equals("true") ? (byte) -1 : (byte) 0;
                break;
            case 97196323:
                b = !lowerCase.equals("false") ? (byte) -1 : (byte) 1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                this.mService.setAllowInstantService(true);
                return 0;
            case 1:
                this.mService.setAllowInstantService(false);
                return 0;
            default:
                pw.println("Invalid mode: " + mode);
                return -1;
        }
    }

    private int setTemporaryDetectionService(java.io.PrintWriter pw) {
        int userId = getNextIntArgRequired();
        java.lang.String serviceName = getNextArg();
        if (serviceName == null) {
            this.mService.resetTemporaryDetectionService(userId);
            return 0;
        }
        int duration = getNextIntArgRequired();
        if (duration <= 0) {
            this.mService.resetTemporaryDetectionService(userId);
            return 0;
        }
        this.mService.setTemporaryDetectionService(userId, serviceName, duration);
        pw.println("Autofill Detection Service temporarily set to " + serviceName + " for " + duration + "ms");
        return 0;
    }

    private int isFieldDetectionServiceEnabled(java.io.PrintWriter pw) {
        int userId = getNextIntArgRequired();
        boolean enabled = this.mService.isFieldDetectionServiceEnabledForUser(userId);
        pw.println(enabled);
        return 0;
    }

    private int setTemporaryAugmentedService(java.io.PrintWriter pw) {
        int userId = getNextIntArgRequired();
        java.lang.String serviceName = getNextArg();
        if (serviceName == null) {
            this.mService.resetTemporaryAugmentedAutofillService(userId);
            return 0;
        }
        int duration = getNextIntArgRequired();
        this.mService.setTemporaryAugmentedAutofillService(userId, serviceName, duration);
        pw.println("AugmentedAutofillService temporarily set to " + serviceName + " for " + duration + "ms");
        return 0;
    }

    private int getDefaultAugmentedServiceEnabled(java.io.PrintWriter pw) {
        int userId = getNextIntArgRequired();
        boolean enabled = this.mService.isDefaultAugmentedServiceEnabled(userId);
        pw.println(enabled);
        return 0;
    }

    private int setDefaultAugmentedServiceEnabled(java.io.PrintWriter pw) {
        int userId = getNextIntArgRequired();
        boolean enabled = java.lang.Boolean.parseBoolean(getNextArgRequired());
        boolean changed = this.mService.setDefaultAugmentedServiceEnabled(userId, enabled);
        if (!changed) {
            pw.println("already " + enabled);
            return 0;
        }
        return 0;
    }

    private int getSavedPasswordCount(final java.io.PrintWriter pw) {
        int userId = getNextIntArgRequired();
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        com.android.internal.os.IResultReceiver resultReceiver = new com.android.internal.os.IResultReceiver.Stub() { // from class: com.android.server.autofill.AutofillManagerServiceShellCommand.1
            public void send(int resultCode, android.os.Bundle resultData) {
                pw.println("resultCode=" + resultCode);
                if (resultCode == 0 && resultData != null) {
                    pw.println("value=" + resultData.getInt("result"));
                }
                latch.countDown();
            }
        };
        if (this.mService.requestSavedPasswordCount(userId, resultReceiver)) {
            waitForLatch(pw, latch);
            return 0;
        }
        return 0;
    }

    private int requestDestroy(java.io.PrintWriter pw) {
        if (!isNextArgSessions(pw)) {
            return -1;
        }
        final int userId = getUserIdFromArgsOrAllUsers();
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        final com.android.internal.os.IResultReceiver.Stub stub = new com.android.internal.os.IResultReceiver.Stub() { // from class: com.android.server.autofill.AutofillManagerServiceShellCommand.2
            public void send(int resultCode, android.os.Bundle resultData) {
                latch.countDown();
            }
        };
        return requestSessionCommon(pw, latch, new java.lang.Runnable() { // from class: com.android.server.autofill.AutofillManagerServiceShellCommand$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$requestDestroy$1(userId, stub);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestDestroy$1(int userId, com.android.internal.os.IResultReceiver receiver) {
        this.mService.removeAllSessions(userId, receiver);
    }

    private int requestList(final java.io.PrintWriter pw) {
        if (!isNextArgSessions(pw)) {
            return -1;
        }
        final int userId = getUserIdFromArgsOrAllUsers();
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        final com.android.internal.os.IResultReceiver.Stub stub = new com.android.internal.os.IResultReceiver.Stub() { // from class: com.android.server.autofill.AutofillManagerServiceShellCommand.3
            public void send(int resultCode, android.os.Bundle resultData) {
                java.util.ArrayList<java.lang.String> sessions = resultData.getStringArrayList("sessions");
                for (java.lang.String session : sessions) {
                    pw.println(session);
                }
                latch.countDown();
            }
        };
        return requestSessionCommon(pw, latch, new java.lang.Runnable() { // from class: com.android.server.autofill.AutofillManagerServiceShellCommand$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$requestList$2(userId, stub);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestList$2(int userId, com.android.internal.os.IResultReceiver receiver) {
        this.mService.listSessions(userId, receiver);
    }

    private boolean isNextArgSessions(java.io.PrintWriter pw) {
        java.lang.String type = getNextArgRequired();
        if (!type.equals("sessions")) {
            pw.println("Error: invalid list type");
            return false;
        }
        return true;
    }

    private int requestSessionCommon(java.io.PrintWriter pw, java.util.concurrent.CountDownLatch latch, java.lang.Runnable command) {
        command.run();
        return waitForLatch(pw, latch);
    }

    private int waitForLatch(java.io.PrintWriter pw, java.util.concurrent.CountDownLatch latch) {
        try {
            boolean received = latch.await(5L, java.util.concurrent.TimeUnit.SECONDS);
            if (!received) {
                pw.println("Timed out after 5 seconds");
                return -1;
            }
            return 0;
        } catch (java.lang.InterruptedException e) {
            pw.println("System call interrupted");
            java.lang.Thread.currentThread().interrupt();
            return -1;
        }
    }

    private int requestReset() {
        this.mService.reset();
        return 0;
    }

    private int getUserIdFromArgsOrAllUsers() {
        if ("--user".equals(getNextArg())) {
            return android.os.UserHandle.parseUserArg(getNextArgRequired());
        }
        return -1;
    }

    private int getNextIntArgRequired() {
        return java.lang.Integer.parseInt(getNextArgRequired());
    }
}
