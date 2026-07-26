package com.android.server.webkit;

/* JADX INFO: loaded from: classes3.dex */
class WebViewUpdateServiceShellCommand2 extends android.os.ShellCommand {
    final android.webkit.IWebViewUpdateService mInterface;

    WebViewUpdateServiceShellCommand2(android.webkit.IWebViewUpdateService service) {
        this.mInterface = service;
    }

    public int onCommand(java.lang.String cmd) {
        byte b;
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            switch (cmd.hashCode()) {
                case -1381305903:
                    if (cmd.equals("set-webview-implementation")) {
                        b = 0;
                        break;
                    }
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    return setWebViewImplementation();
                default:
                    return handleDefaultCommands(cmd);
            }
        } catch (android.os.RemoteException e) {
            pw.println("Remote exception: " + e);
            return -1;
        }
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
        pw.println(android.text.TextUtils.formatSimple("Failed to switch to %s, the WebView implementation is now provided by %s.", new java.lang.Object[]{shellChosenPackage, newPackage}));
        return 1;
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
        pw.println();
    }
}
