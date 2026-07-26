package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
class ColorDisplayShellCommand extends android.os.ShellCommand {
    private static final int ERROR = -1;
    private static final int SUCCESS = 0;
    private static final java.lang.String USAGE = "usage: cmd color_display SUBCOMMAND [ARGS]\n    help\n      Shows this message.\n    set-saturation LEVEL\n      Sets the device saturation to the given LEVEL, 0-100 inclusive.\n    set-layer-saturation LEVEL CALLER_PACKAGE TARGET_PACKAGE\n      Sets the saturation LEVEL for all layers of the TARGET_PACKAGE, attributed\n      to the CALLER_PACKAGE. The lowest LEVEL from any CALLER_PACKAGE is applied.\n";
    private final com.android.server.display.color.ColorDisplayService mService;

    ColorDisplayShellCommand(com.android.server.display.color.ColorDisplayService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0025  */
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
                case 245833689: goto L1a;
                case 726170141: goto Lf;
                default: goto Le;
            }
        Le:
            goto L25
        Lf:
            java.lang.String r0 = "set-saturation"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 0
            goto L26
        L1a:
            java.lang.String r0 = "set-layer-saturation"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 1
            goto L26
        L25:
            r0 = -1
        L26:
            switch(r0) {
                case 0: goto L33;
                case 1: goto L2e;
                default: goto L29;
            }
        L29:
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L2e:
            int r0 = r1.setLayerSaturation()
            return r0
        L33:
            int r0 = r1.setSaturation()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.color.ColorDisplayShellCommand.onCommand(java.lang.String):int");
    }

    private int setSaturation() {
        int level = getLevel();
        if (level == -1) {
            return -1;
        }
        this.mService.setSaturationLevelInternal(level);
        return 0;
    }

    private int setLayerSaturation() {
        int level = getLevel();
        if (level == -1) {
            return -1;
        }
        java.lang.String callerPackageName = getPackageName();
        if (callerPackageName == null) {
            getErrPrintWriter().println("Error: CALLER_PACKAGE must be an installed package name");
            return -1;
        }
        java.lang.String targetPackageName = getPackageName();
        if (targetPackageName == null) {
            getErrPrintWriter().println("Error: TARGET_PACKAGE must be an installed package name");
            return -1;
        }
        this.mService.setAppSaturationLevelInternal(callerPackageName, targetPackageName, level);
        return 0;
    }

    private java.lang.String getPackageName() {
        java.lang.String packageNameArg = getNextArg();
        if (((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).getPackage(packageNameArg) == null) {
            return null;
        }
        return packageNameArg;
    }

    private int getLevel() {
        java.lang.String levelArg = getNextArg();
        if (levelArg == null) {
            getErrPrintWriter().println("Error: Required argument LEVEL is unspecified");
            return -1;
        }
        try {
            int level = java.lang.Integer.parseInt(levelArg);
            if (level < 0 || level > 100) {
                getErrPrintWriter().println("Error: LEVEL argument must be an integer between 0 and 100");
                return -1;
            }
            return level;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: LEVEL argument is not an integer");
            return -1;
        }
    }

    public void onHelp() {
        getOutPrintWriter().print(USAGE);
    }
}
