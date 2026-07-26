package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
public class WallpaperManagerShellCommand extends android.os.ShellCommand {
    private static final java.lang.String TAG = "WallpaperManagerShellCommand";
    private final com.android.server.wallpaper.WallpaperManagerService mService;

    public WallpaperManagerShellCommand(com.android.server.wallpaper.WallpaperManagerService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0042  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r4) {
        /*
            r3 = this;
            r0 = 1
            if (r4 != 0) goto L7
            r3.onHelp()
            return r0
        L7:
            int r1 = r4.hashCode()
            r2 = 0
            switch(r1) {
                case -1462105208: goto L37;
                case -296046994: goto L2e;
                case 1499: goto L24;
                case 3198785: goto L1a;
                case 309630996: goto L10;
                default: goto Lf;
            }
        Lf:
            goto L42
        L10:
            java.lang.String r0 = "get-dim-amount"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto Lf
            r0 = 2
            goto L43
        L1a:
            java.lang.String r0 = "help"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto Lf
            r0 = 4
            goto L43
        L24:
            java.lang.String r0 = "-h"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto Lf
            r0 = 3
            goto L43
        L2e:
            java.lang.String r1 = "dim-with-uid"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto Lf
            goto L43
        L37:
            java.lang.String r0 = "set-dim-amount"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r2
            goto L43
        L42:
            r0 = -1
        L43:
            switch(r0) {
                case 0: goto L59;
                case 1: goto L54;
                case 2: goto L4f;
                case 3: goto L4b;
                case 4: goto L4b;
                default: goto L46;
            }
        L46:
            int r0 = r3.handleDefaultCommands(r4)
            return r0
        L4b:
            r3.onHelp()
            return r2
        L4f:
            int r0 = r3.getWallpaperDimAmount()
            return r0
        L54:
            int r0 = r3.setDimmingWithUid()
            return r0
        L59:
            int r0 = r3.setWallpaperDimAmount()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wallpaper.WallpaperManagerShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Wallpaper manager commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println();
        pw.println("  set-dim-amount DIMMING");
        pw.println("    Sets the current dimming value to DIMMING (a number between 0 and 1).");
        pw.println();
        pw.println("  dim-with-uid UID DIMMING");
        pw.println("    Sets the wallpaper dim amount to DIMMING as if an app with uid, UID, called it.");
        pw.println();
        pw.println("  get-dim-amount");
        pw.println("    Get the current wallpaper dim amount.");
    }

    private int setWallpaperDimAmount() {
        float dimAmount = java.lang.Float.parseFloat(getNextArgRequired());
        try {
            this.mService.setWallpaperDimAmount(dimAmount);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Can't set wallpaper dim amount");
        }
        getOutPrintWriter().println("Dimming the wallpaper to: " + dimAmount);
        return 0;
    }

    private int getWallpaperDimAmount() {
        float dimAmount = this.mService.getWallpaperDimAmount();
        getOutPrintWriter().println("The current wallpaper dim amount is: " + dimAmount);
        return 0;
    }

    private int setDimmingWithUid() {
        int mockUid = java.lang.Integer.parseInt(getNextArgRequired());
        float mockDimAmount = java.lang.Float.parseFloat(getNextArgRequired());
        this.mService.setWallpaperDimAmountForUid(mockUid, mockDimAmount);
        getOutPrintWriter().println("Dimming the wallpaper for UID: " + mockUid + " to: " + mockDimAmount);
        return 0;
    }
}
