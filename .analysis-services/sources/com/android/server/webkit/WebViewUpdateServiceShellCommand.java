package com.android.server.webkit;

/* JADX INFO: loaded from: classes3.dex */
class WebViewUpdateServiceShellCommand extends android.os.ShellCommand {
    final android.webkit.IWebViewUpdateService mInterface;

    WebViewUpdateServiceShellCommand(android.webkit.IWebViewUpdateService service) {
        this.mInterface = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0015  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            r5 = this;
            if (r6 != 0) goto L7
            int r0 = r5.handleDefaultCommands(r6)
            return r0
        L7:
            java.io.PrintWriter r0 = r5.getOutPrintWriter()
            r1 = -1
            int r2 = r6.hashCode()     // Catch: android.os.RemoteException -> L4e
            r3 = 0
            r4 = 1
            switch(r2) {
                case -1857752288: goto L2b;
                case -1381305903: goto L20;
                case 436183515: goto L16;
                default: goto L15;
            }     // Catch: android.os.RemoteException -> L4e
        L15:
            goto L35
        L16:
            java.lang.String r2 = "disable-multiprocess"
            boolean r2 = r6.equals(r2)     // Catch: android.os.RemoteException -> L4e
            if (r2 == 0) goto L15
            r2 = 2
            goto L36
        L20:
            java.lang.String r2 = "set-webview-implementation"
            boolean r2 = r6.equals(r2)     // Catch: android.os.RemoteException -> L4e
            if (r2 == 0) goto L15
            r2 = r3
            goto L36
        L2b:
            java.lang.String r2 = "enable-multiprocess"
            boolean r2 = r6.equals(r2)     // Catch: android.os.RemoteException -> L4e
            if (r2 == 0) goto L15
            r2 = r4
            goto L36
        L35:
            r2 = r1
        L36:
            switch(r2) {
                case 0: goto L48;
                case 1: goto L43;
                case 2: goto L3e;
                default: goto L39;
            }     // Catch: android.os.RemoteException -> L4e
        L39:
            int r1 = r5.handleDefaultCommands(r6)     // Catch: android.os.RemoteException -> L4e
            goto L4d
        L3e:
            int r1 = r5.enableMultiProcess(r3)     // Catch: android.os.RemoteException -> L4e
            return r1
        L43:
            int r1 = r5.enableMultiProcess(r4)     // Catch: android.os.RemoteException -> L4e
            return r1
        L48:
            int r1 = r5.setWebViewImplementation()     // Catch: android.os.RemoteException -> L4e
            return r1
        L4d:
            return r1
        L4e:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Remote exception: "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r2)
            java.lang.String r3 = r3.toString()
            r0.println(r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.webkit.WebViewUpdateServiceShellCommand.onCommand(java.lang.String):int");
    }

    private int setWebViewImplementation() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String shellChosenPackage = getNextArg();
        if (shellChosenPackage == null) {
            pw.println("Failed to switch, no PACKAGE provided.");
            pw.println("");
            helpSetWebViewImplementation();
            return 1;
        }
        java.lang.String newPackage = this.mInterface.changeProviderAndSetting(shellChosenPackage);
        if (shellChosenPackage.equals(newPackage)) {
            pw.println("Success");
            return 0;
        }
        pw.println(java.lang.String.format("Failed to switch to %s, the WebView implementation is now provided by %s.", shellChosenPackage, newPackage));
        return 1;
    }

    private int enableMultiProcess(boolean enable) throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        this.mInterface.enableMultiProcess(enable);
        pw.println("Success");
        return 0;
    }

    public void helpSetWebViewImplementation() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("  set-webview-implementation PACKAGE");
        pw.println("    Set the WebView implementation to the specified package.");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("WebView updater commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("");
        helpSetWebViewImplementation();
        pw.println("  enable-multiprocess");
        pw.println("    Enable multi-process mode for WebView");
        pw.println("  disable-multiprocess");
        pw.println("    Disable multi-process mode for WebView");
        pw.println();
    }
}
