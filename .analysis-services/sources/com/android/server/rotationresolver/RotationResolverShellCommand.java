package com.android.server.rotationresolver;

/* JADX INFO: loaded from: classes3.dex */
final class RotationResolverShellCommand extends android.os.ShellCommand {
    private static final int INITIAL_RESULT_CODE = -1;
    static final com.android.server.rotationresolver.RotationResolverShellCommand.TestableRotationCallbackInternal sTestableRotationCallbackInternal = new com.android.server.rotationresolver.RotationResolverShellCommand.TestableRotationCallbackInternal();
    private final com.android.server.rotationresolver.RotationResolverManagerService mService;

    RotationResolverShellCommand(com.android.server.rotationresolver.RotationResolverManagerService service) {
        this.mService = service;
    }

    static class TestableRotationCallbackInternal implements android.rotationresolver.RotationResolverInternal.RotationResolverCallbackInternal {
        private int mLastCallbackResultCode = -1;

        TestableRotationCallbackInternal() {
        }

        public void onSuccess(int result) {
            this.mLastCallbackResultCode = result;
        }

        public void onFailure(int error) {
            this.mLastCallbackResultCode = error;
        }

        public void reset() {
            this.mLastCallbackResultCode = -1;
        }

        public int getLastCallbackCode() {
            return this.mLastCallbackResultCode;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0039  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r2) {
        /*
            r1 = this;
            if (r2 != 0) goto L7
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L7:
            int r0 = r2.hashCode()
            switch(r0) {
                case -2084150080: goto L2f;
                case 384662079: goto L24;
                case 1104883342: goto L19;
                case 1820466124: goto Lf;
                default: goto Le;
            }
        Le:
            goto L39
        Lf:
            java.lang.String r0 = "get-last-resolution"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 1
            goto L3a
        L19:
            java.lang.String r0 = "set-temporary-service"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 3
            goto L3a
        L24:
            java.lang.String r0 = "resolve-rotation"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 0
            goto L3a
        L2f:
            java.lang.String r0 = "get-bound-package"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 2
            goto L3a
        L39:
            r0 = -1
        L3a:
            switch(r0) {
                case 0: goto L51;
                case 1: goto L4c;
                case 2: goto L47;
                case 3: goto L42;
                default: goto L3d;
            }
        L3d:
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L42:
            int r0 = r1.setTemporaryService()
            return r0
        L47:
            int r0 = r1.getBoundPackageName()
            return r0
        L4c:
            int r0 = r1.getLastResolution()
            return r0
        L51:
            int r0 = r1.runResolveRotation()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.rotationresolver.RotationResolverShellCommand.onCommand(java.lang.String):int");
    }

    private int getBoundPackageName() {
        java.io.PrintWriter out = getOutPrintWriter();
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        android.content.ComponentName componentName = this.mService.getComponentNameShellCommand(userId);
        out.println(componentName == null ? "" : componentName.getPackageName());
        return 0;
    }

    private int setTemporaryService() {
        java.io.PrintWriter out = getOutPrintWriter();
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String serviceName = getNextArg();
        if (serviceName == null) {
            this.mService.resetTemporaryService(userId);
            out.println("RotationResolverService temporary reset. ");
            return 0;
        }
        int duration = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setTemporaryService(userId, serviceName, duration);
        out.println("RotationResolverService temporarily set to " + serviceName + " for " + duration + "ms");
        return 0;
    }

    private int runResolveRotation() {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        android.service.rotationresolver.RotationResolutionRequest request = new android.service.rotationresolver.RotationResolutionRequest("", 0, 0, true, 2000L);
        this.mService.resolveRotationShellCommand(userId, sTestableRotationCallbackInternal, request);
        return 0;
    }

    private int getLastResolution() {
        java.io.PrintWriter out = getOutPrintWriter();
        out.println(sTestableRotationCallbackInternal.getLastCallbackCode());
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Rotation Resolver commands: ");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println();
        pw.println("  resolve-rotation USER_ID: request a rotation resolution.");
        pw.println("  get-last-resolution: show the last rotation resolution result.");
        pw.println("  get-bound-package USER_ID:");
        pw.println("    Print the bound package that implements the service.");
        pw.println("  set-temporary-service USER_ID [COMPONENT_NAME DURATION]");
        pw.println("    Temporarily (for DURATION ms) changes the service implementation.");
        pw.println("    To reset, call with just the USER_ID argument.");
    }
}
