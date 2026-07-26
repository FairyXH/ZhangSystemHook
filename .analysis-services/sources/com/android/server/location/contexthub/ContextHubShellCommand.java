package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
public class ContextHubShellCommand extends android.os.ShellCommand {
    private final android.content.Context mContext;
    private final com.android.server.location.contexthub.ContextHubService mInternal;

    public ContextHubShellCommand(android.content.Context context, com.android.server.location.contexthub.ContextHubService service) {
        this.mInternal = service;
        this.mContext = context;
    }

    public int onCommand(java.lang.String cmd) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_CONTEXT_HUB", "ContextHubShellCommand");
        if ("deny".equals(cmd)) {
            return runDisableAuth();
        }
        return handleDefaultCommands(cmd);
    }

    private int runDisableAuth() {
        int contextHubId = java.lang.Integer.decode(getNextArgRequired()).intValue();
        java.lang.String packageName = getNextArgRequired();
        long nanoAppId = java.lang.Long.decode(getNextArgRequired()).longValue();
        this.mInternal.denyClientAuthState(contextHubId, packageName, nanoAppId);
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("ContextHub commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  deny [contextHubId] [packageName] [nanoAppId]");
        pw.println("    Immediately transitions the package's authentication state to denied so");
        pw.println("    can no longer communciate with the nanoapp.");
    }
}
