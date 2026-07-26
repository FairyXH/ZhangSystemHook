package com.android.server.contentsuggestions;

/* JADX INFO: loaded from: classes.dex */
public class ContentSuggestionsManagerServiceShellCommand extends android.os.ShellCommand {
    private static final java.lang.String TAG = com.android.server.contentsuggestions.ContentSuggestionsManagerServiceShellCommand.class.getSimpleName();
    private final com.android.server.contentsuggestions.ContentSuggestionsManagerService mService;

    public ContentSuggestionsManagerServiceShellCommand(com.android.server.contentsuggestions.ContentSuggestionsManagerService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0029  */
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
                case 102230: goto L1e;
                case 113762: goto L13;
                default: goto L12;
            }
        L12:
            goto L29
        L13:
            java.lang.String r1 = "set"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 0
            goto L2a
        L1e:
            java.lang.String r1 = "get"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 1
            goto L2a
        L29:
            r1 = -1
        L2a:
            switch(r1) {
                case 0: goto L37;
                case 1: goto L32;
                default: goto L2d;
            }
        L2d:
            int r1 = r2.handleDefaultCommands(r3)
            return r1
        L32:
            int r1 = r2.requestGet(r0)
            return r1
        L37:
            int r1 = r2.requestSet(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.contentsuggestions.ContentSuggestionsManagerServiceShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            pw.println("ContentSuggestionsManagerService commands:");
            pw.println("  help");
            pw.println("    Prints this help text.");
            pw.println("");
            pw.println("  set temporary-service USER_ID [COMPONENT_NAME DURATION]");
            pw.println("    Temporarily (for DURATION ms) changes the service implementation.");
            pw.println("    To reset, call with just the USER_ID argument.");
            pw.println("");
            pw.println("  set default-service-enabled USER_ID [true|false]");
            pw.println("    Enable / disable the default service for the user.");
            pw.println("");
            pw.println("  get default-service-enabled USER_ID");
            pw.println("    Checks whether the default service is enabled for the user.");
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

    private int requestSet(java.io.PrintWriter pw) {
        byte b;
        java.lang.String what = getNextArgRequired();
        switch (what.hashCode()) {
            case 529654941:
                b = !what.equals("default-service-enabled") ? (byte) -1 : (byte) 1;
                break;
            case 2003978041:
                b = !what.equals("temporary-service") ? (byte) -1 : (byte) 0;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return setTemporaryService(pw);
            case 1:
                return setDefaultServiceEnabled();
            default:
                pw.println("Invalid set: " + what);
                return -1;
        }
    }

    private int requestGet(java.io.PrintWriter pw) {
        byte b;
        java.lang.String what = getNextArgRequired();
        switch (what.hashCode()) {
            case 529654941:
                if (what.equals("default-service-enabled")) {
                    b = 0;
                    break;
                }
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return getDefaultServiceEnabled(pw);
            default:
                pw.println("Invalid get: " + what);
                return -1;
        }
    }

    private int setTemporaryService(java.io.PrintWriter pw) {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String serviceName = getNextArg();
        if (serviceName == null) {
            this.mService.resetTemporaryService(userId);
            return 0;
        }
        int duration = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setTemporaryService(userId, serviceName, duration);
        pw.println("ContentSuggestionsService temporarily set to " + serviceName + " for " + duration + "ms");
        return 0;
    }

    private int setDefaultServiceEnabled() {
        int userId = getNextIntArgRequired();
        boolean enabled = java.lang.Boolean.parseBoolean(getNextArg());
        this.mService.setDefaultServiceEnabled(userId, enabled);
        return 0;
    }

    private int getDefaultServiceEnabled(java.io.PrintWriter pw) {
        int userId = getNextIntArgRequired();
        boolean enabled = this.mService.isDefaultServiceEnabled(userId);
        pw.println(enabled);
        return 0;
    }

    private int getNextIntArgRequired() {
        return java.lang.Integer.parseInt(getNextArgRequired());
    }
}
