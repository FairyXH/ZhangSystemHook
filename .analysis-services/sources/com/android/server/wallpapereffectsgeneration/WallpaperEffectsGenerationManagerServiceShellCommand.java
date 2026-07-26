package com.android.server.wallpapereffectsgeneration;

/* JADX INFO: loaded from: classes3.dex */
public class WallpaperEffectsGenerationManagerServiceShellCommand extends android.os.ShellCommand {
    private static final java.lang.String TAG = com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerServiceShellCommand.class.getSimpleName();
    private final com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService mService;

    public WallpaperEffectsGenerationManagerServiceShellCommand(com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService service) {
        this.mService = service;
    }

    public int onCommand(java.lang.String cmd) {
        byte b;
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        java.io.PrintWriter pw = getOutPrintWriter();
        byte b2 = -1;
        switch (cmd.hashCode()) {
            case 113762:
                if (cmd.equals("set")) {
                    b = 0;
                    break;
                }
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                java.lang.String what = getNextArgRequired();
                switch (what.hashCode()) {
                    case 2003978041:
                        if (what.equals("temporary-service")) {
                            b2 = 0;
                        }
                        break;
                }
                switch (b2) {
                    case 0:
                        int userId = java.lang.Integer.parseInt(getNextArgRequired());
                        java.lang.String serviceName = getNextArg();
                        if (serviceName == null) {
                            this.mService.resetTemporaryService(userId);
                            pw.println("WallpaperEffectsGenerationService temporarily reset. ");
                            break;
                        } else {
                            int duration = java.lang.Integer.parseInt(getNextArgRequired());
                            this.mService.setTemporaryService(userId, serviceName, duration);
                            pw.println("WallpaperEffectsGenerationService temporarily set to " + serviceName + " for " + duration + "ms");
                            break;
                        }
                    default:
                        return 0;
                }
                break;
        }
        return handleDefaultCommands(cmd);
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            pw.println("WallpaperEffectsGenerationService commands:");
            pw.println("  help");
            pw.println("    Prints this help text.");
            pw.println("");
            pw.println("  set temporary-service USER_ID [COMPONENT_NAME DURATION]");
            pw.println("    Temporarily (for DURATION ms) changes the service implemtation.");
            pw.println("    To reset, call with just the USER_ID argument.");
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
}
