package com.android.server.contentcapture;

/* JADX INFO: loaded from: classes.dex */
public final class ContentCaptureManagerServiceShellCommand extends android.os.ShellCommand {
    private final com.android.server.contentcapture.ContentCaptureManagerService mService;

    public ContentCaptureManagerServiceShellCommand(com.android.server.contentcapture.ContentCaptureManagerService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003e  */
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
                case 102230: goto L33;
                case 113762: goto L28;
                case 3322014: goto L1d;
                case 1557372922: goto L13;
                default: goto L12;
            }
        L12:
            goto L3e
        L13:
            java.lang.String r1 = "destroy"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 1
            goto L3f
        L1d:
            java.lang.String r1 = "list"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 0
            goto L3f
        L28:
            java.lang.String r1 = "set"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 3
            goto L3f
        L33:
            java.lang.String r1 = "get"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 2
            goto L3f
        L3e:
            r1 = -1
        L3f:
            switch(r1) {
                case 0: goto L56;
                case 1: goto L51;
                case 2: goto L4c;
                case 3: goto L47;
                default: goto L42;
            }
        L42:
            int r1 = r2.handleDefaultCommands(r3)
            return r1
        L47:
            int r1 = r2.requestSet(r0)
            return r1
        L4c:
            int r1 = r2.requestGet(r0)
            return r1
        L51:
            int r1 = r2.requestDestroy(r0)
            return r1
        L56:
            int r1 = r2.requestList(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.contentcapture.ContentCaptureManagerServiceShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            pw.println("ContentCapture Service (content_capture) commands:");
            pw.println("  help");
            pw.println("    Prints this help text.");
            pw.println("");
            pw.println("  get bind-instant-service-allowed");
            pw.println("    Gets whether binding to services provided by instant apps is allowed");
            pw.println("");
            pw.println("  set bind-instant-service-allowed [true | false]");
            pw.println("    Sets whether binding to services provided by instant apps is allowed");
            pw.println("");
            pw.println("  set temporary-service USER_ID [COMPONENT_NAME DURATION]");
            pw.println("    Temporarily (for DURATION ms) changes the service implemtation.");
            pw.println("    To reset, call with just the USER_ID argument.");
            pw.println("");
            pw.println("  set default-service-enabled USER_ID [true|false]");
            pw.println("    Enable / disable the default service for the user.");
            pw.println("");
            pw.println("  get default-service-enabled USER_ID");
            pw.println("    Checks whether the default service is enabled for the user.");
            pw.println("");
            pw.println("  list sessions [--user USER_ID]");
            pw.println("    Lists all pending sessions.");
            pw.println("");
            pw.println("  destroy sessions [--user USER_ID]");
            pw.println("    Destroys all pending sessions.");
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

    private int requestGet(java.io.PrintWriter pw) {
        byte b;
        java.lang.String what = getNextArgRequired();
        switch (what.hashCode()) {
            case 529654941:
                b = !what.equals("default-service-enabled") ? (byte) -1 : (byte) 1;
                break;
            case 809633044:
                b = !what.equals("bind-instant-service-allowed") ? (byte) -1 : (byte) 0;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return getBindInstantService(pw);
            case 1:
                return getDefaultServiceEnabled(pw);
            default:
                pw.println("Invalid set: " + what);
                return -1;
        }
    }

    private int requestSet(java.io.PrintWriter pw) {
        byte b;
        java.lang.String what = getNextArgRequired();
        switch (what.hashCode()) {
            case 529654941:
                b = !what.equals("default-service-enabled") ? (byte) -1 : (byte) 2;
                break;
            case 809633044:
                b = !what.equals("bind-instant-service-allowed") ? (byte) -1 : (byte) 0;
                break;
            case 2003978041:
                b = !what.equals("temporary-service") ? (byte) -1 : (byte) 1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return setBindInstantService(pw);
            case 1:
                return setTemporaryService(pw);
            case 2:
                return setDefaultServiceEnabled(pw);
            default:
                pw.println("Invalid set: " + what);
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

    private int setTemporaryService(java.io.PrintWriter pw) {
        int userId = getNextIntArgRequired();
        java.lang.String serviceName = getNextArg();
        if (serviceName == null) {
            this.mService.resetTemporaryService(userId);
            return 0;
        }
        int duration = getNextIntArgRequired();
        this.mService.setTemporaryService(userId, serviceName, duration);
        pw.println("ContentCaptureService temporarily set to " + serviceName + " for " + duration + "ms");
        return 0;
    }

    private int setDefaultServiceEnabled(java.io.PrintWriter pw) {
        int userId = getNextIntArgRequired();
        boolean enabled = java.lang.Boolean.parseBoolean(getNextArgRequired());
        boolean changed = this.mService.setDefaultServiceEnabled(userId, enabled);
        if (!changed) {
            pw.println("already " + enabled);
            return 0;
        }
        return 0;
    }

    private int getDefaultServiceEnabled(java.io.PrintWriter pw) {
        int userId = getNextIntArgRequired();
        boolean enabled = this.mService.isDefaultServiceEnabled(userId);
        pw.println(enabled);
        return 0;
    }

    private int requestDestroy(java.io.PrintWriter pw) {
        if (!isNextArgSessions(pw)) {
            return -1;
        }
        final int userId = getUserIdFromArgsOrAllUsers();
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        final com.android.internal.os.IResultReceiver.Stub stub = new com.android.internal.os.IResultReceiver.Stub() { // from class: com.android.server.contentcapture.ContentCaptureManagerServiceShellCommand.1
            public void send(int resultCode, android.os.Bundle resultData) {
                latch.countDown();
            }
        };
        return requestSessionCommon(pw, latch, new java.lang.Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerServiceShellCommand$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$requestDestroy$0(userId, stub);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestDestroy$0(int userId, com.android.internal.os.IResultReceiver receiver) {
        this.mService.destroySessions(userId, receiver);
    }

    private int requestList(final java.io.PrintWriter pw) {
        if (!isNextArgSessions(pw)) {
            return -1;
        }
        final int userId = getUserIdFromArgsOrAllUsers();
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        final com.android.internal.os.IResultReceiver.Stub stub = new com.android.internal.os.IResultReceiver.Stub() { // from class: com.android.server.contentcapture.ContentCaptureManagerServiceShellCommand.2
            public void send(int resultCode, android.os.Bundle resultData) {
                java.util.ArrayList<java.lang.String> sessions = resultData.getStringArrayList("sessions");
                for (java.lang.String session : sessions) {
                    pw.println(session);
                }
                latch.countDown();
            }
        };
        return requestSessionCommon(pw, latch, new java.lang.Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerServiceShellCommand$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$requestList$1(userId, stub);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestList$1(int userId, com.android.internal.os.IResultReceiver receiver) {
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
