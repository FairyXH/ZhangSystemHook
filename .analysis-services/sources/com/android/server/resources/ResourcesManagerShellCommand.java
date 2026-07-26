package com.android.server.resources;

/* JADX INFO: loaded from: classes3.dex */
public class ResourcesManagerShellCommand extends android.os.ShellCommand {
    private static final java.lang.String TAG = "ResourcesManagerShellCommand";
    private final android.content.res.IResourcesManager mInterface;

    public ResourcesManagerShellCommand(android.content.res.IResourcesManager anInterface) {
        this.mInterface = anInterface;
    }

    public int onCommand(java.lang.String cmd) {
        byte b;
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        java.io.PrintWriter err = getErrPrintWriter();
        try {
            switch (cmd.hashCode()) {
                case 3095028:
                    if (cmd.equals("dump")) {
                        b = 0;
                        break;
                    }
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    return dumpResources();
                default:
                    return handleDefaultCommands(cmd);
            }
        } catch (android.os.RemoteException e) {
            err.println("Remote exception: " + e);
            return -1;
        } catch (java.lang.IllegalArgumentException e2) {
            err.println("Error: " + e2.getMessage());
            return -1;
        }
    }

    private int dumpResources() throws android.os.RemoteException {
        java.lang.String processId = getNextArgRequired();
        try {
            android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.dup(getOutFileDescriptor());
            try {
                final android.os.ConditionVariable lock = new android.os.ConditionVariable();
                android.os.RemoteCallback finishCallback = new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.resources.ResourcesManagerShellCommand$$ExternalSyntheticLambda0
                    public final void onResult(android.os.Bundle bundle) {
                        lock.open();
                    }
                }, (android.os.Handler) null);
                if (!this.mInterface.dumpResources(processId, pfd, finishCallback)) {
                    getErrPrintWriter().println("RESOURCES DUMP FAILED on process " + processId);
                    if (pfd != null) {
                        pfd.close();
                    }
                    return -1;
                }
                lock.block(5000L);
                if (pfd != null) {
                    pfd.close();
                    return 0;
                }
                return 0;
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Exception while dumping resources", e);
            getErrPrintWriter().println("Exception while dumping resources: " + e.getMessage());
            return -1;
        }
    }

    public void onHelp() {
        java.io.PrintWriter out = getOutPrintWriter();
        out.println("Resources manager commands:");
        out.println("  help");
        out.println("    Print this help text.");
        out.println("  dump <PROCESS>");
        out.println("    Dump the Resources objects in use as well as the history of Resources");
    }
}
