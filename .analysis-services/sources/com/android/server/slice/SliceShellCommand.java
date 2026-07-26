package com.android.server.slice;

/* JADX INFO: loaded from: classes3.dex */
public class SliceShellCommand extends android.os.ShellCommand {
    private final com.android.server.slice.SliceManagerService mService;

    public SliceShellCommand(com.android.server.slice.SliceManagerService service) {
        this.mService = service;
    }

    public int onCommand(java.lang.String cmd) {
        byte b;
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        switch (cmd.hashCode()) {
            case -185318259:
                if (cmd.equals("get-permissions")) {
                    b = 0;
                    break;
                }
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return runGetPermissions(getNextArgRequired());
            default:
                return 0;
        }
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Status bar commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("");
        pw.println("  get-permissions <authority>");
        pw.println("    List the pkgs that have permission to an authority.");
        pw.println("");
    }

    private int runGetPermissions(java.lang.String authority) {
        if (android.os.Binder.getCallingUid() != 2000 && android.os.Binder.getCallingUid() != 0) {
            getOutPrintWriter().println("Only shell can get permissions");
            return -1;
        }
        android.content.Context context = this.mService.getContext();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.net.Uri uri = new android.net.Uri.Builder().scheme(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT).authority(authority).build();
            if (!"vnd.android.slice".equals(context.getContentResolver().getType(uri))) {
                getOutPrintWriter().println(authority + " is not a slice provider");
                return -1;
            }
            android.os.Bundle b = context.getContentResolver().call(uri, "get_permissions", (java.lang.String) null, (android.os.Bundle) null);
            if (b == null) {
                getOutPrintWriter().println("An error occurred getting permissions");
                return -1;
            }
            java.lang.String[] permissions = b.getStringArray("result");
            java.io.PrintWriter pw = getOutPrintWriter();
            java.util.Set<java.lang.String> listedPackages = new android.util.ArraySet<>();
            if (permissions != null && permissions.length != 0) {
                java.util.List<android.content.pm.PackageInfo> apps = context.getPackageManager().getPackagesHoldingPermissions(permissions, 0);
                for (android.content.pm.PackageInfo app : apps) {
                    pw.println(app.packageName);
                    listedPackages.add(app.packageName);
                }
            }
            for (java.lang.String pkg : this.mService.getAllPackagesGranted(authority)) {
                if (!listedPackages.contains(pkg)) {
                    pw.println(pkg);
                    listedPackages.add(pkg);
                }
            }
            return 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }
}
