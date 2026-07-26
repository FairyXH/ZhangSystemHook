package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
class PowerManagerShellCommand extends android.os.ShellCommand {
    private static final int LOW_POWER_MODE_ON = 1;
    private final android.content.Context mContext;
    private android.util.SparseArray<android.os.PowerManager.WakeLock> mProxWakelocks = new android.util.SparseArray<>();
    private final com.android.server.power.PowerManagerService.BinderService mService;

    PowerManagerShellCommand(android.content.Context context, com.android.server.power.PowerManagerService.BinderService service) {
        this.mContext = context;
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            Method dump skipped, instruction units count: 214
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.PowerManagerShellCommand.onCommand(java.lang.String):int");
    }

    private int runSetAdaptiveEnabled() throws android.os.RemoteException {
        this.mService.setAdaptivePowerSaveEnabled(java.lang.Boolean.parseBoolean(getNextArgRequired()));
        return 0;
    }

    private int runSetMode() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            int mode = java.lang.Integer.parseInt(getNextArgRequired());
            this.mService.setPowerSaveModeEnabled(mode == 1);
            return 0;
        } catch (java.lang.RuntimeException ex) {
            pw.println("Error: " + ex.toString());
            return -1;
        }
    }

    private int runSetFixedPerformanceModeEnabled() throws android.os.RemoteException {
        boolean success = this.mService.setPowerModeChecked(3, java.lang.Boolean.parseBoolean(getNextArgRequired()));
        if (!success) {
            java.io.PrintWriter ew = getErrPrintWriter();
            ew.println("Failed to set FIXED_PERFORMANCE mode");
            ew.println("This is likely because Power HAL AIDL is not implemented on this device");
        }
        return success ? 0 : -1;
    }

    private int runSuppressAmbientDisplay() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            java.lang.String token = getNextArgRequired();
            boolean enabled = java.lang.Boolean.parseBoolean(getNextArgRequired());
            this.mService.suppressAmbientDisplay(token, enabled);
            return 0;
        } catch (java.lang.RuntimeException ex) {
            pw.println("Error: " + ex.toString());
            return -1;
        }
    }

    private int runListAmbientDisplaySuppressionTokens() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        java.util.List<java.lang.String> tokens = this.mService.getAmbientDisplaySuppressionTokens();
        if (tokens.isEmpty()) {
            pw.println("none");
            return 0;
        }
        pw.println(java.lang.String.format("[%s]", java.lang.String.join(", ", tokens)));
        return 0;
    }

    private int runSetProx() throws android.os.RemoteException {
        byte b;
        boolean acquire;
        java.lang.String idStr;
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String lowerCase = getNextArgRequired().toLowerCase();
        switch (lowerCase.hashCode()) {
            case -1164222250:
                b = !lowerCase.equals("acquire") ? (byte) -1 : (byte) 1;
                break;
            case 3322014:
                b = !lowerCase.equals("list") ? (byte) -1 : (byte) 0;
                break;
            case 1090594823:
                b = !lowerCase.equals("release") ? (byte) -1 : (byte) 2;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                pw.println("Wakelocks:");
                pw.println(this.mProxWakelocks);
                return 0;
            case 1:
                acquire = true;
                break;
            case 2:
                acquire = false;
                break;
            default:
                pw.println("Error: Allowed options are 'list' 'enable' and 'disable'.");
                return -1;
        }
        int displayId = -1;
        java.lang.String displayOption = getNextArg();
        if ("-d".equals(displayOption) && (displayId = java.lang.Integer.parseInt((idStr = getNextArg()))) < 0) {
            pw.println("Error: Specified displayId (" + idStr + ") must a non-negative int.");
            return -1;
        }
        int wakelockIndex = displayId + 1;
        android.os.PowerManager.WakeLock wakelock = this.mProxWakelocks.get(wakelockIndex);
        if (wakelock == null) {
            android.os.PowerManager pm = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
            wakelock = pm.newWakeLock(32, "PowerManagerShellCommand[" + displayId + "]", displayId);
            this.mProxWakelocks.put(wakelockIndex, wakelock);
        }
        if (acquire) {
            wakelock.acquire();
        } else {
            wakelock.release();
        }
        pw.println(wakelock);
        return 0;
    }

    private int runSetFaceDownDetector() {
        try {
            this.mService.setUseFaceDownDetector(java.lang.Boolean.parseBoolean(getNextArgRequired()));
            return 0;
        } catch (java.lang.Exception e) {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("Error: " + e);
            return -1;
        }
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Power manager (power) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("");
        pw.println("  set-adaptive-power-saver-enabled [true|false]");
        pw.println("    enables or disables adaptive power saver.");
        pw.println("  set-mode MODE");
        pw.println("    sets the power mode of the device to MODE.");
        pw.println("    1 turns low power mode on and 0 turns low power mode off.");
        pw.println("  set-fixed-performance-mode-enabled [true|false]");
        pw.println("    enables or disables fixed performance mode");
        pw.println("    note: this will affect system performance and should only be used");
        pw.println("          during development");
        pw.println("  suppress-ambient-display <token> [true|false]");
        pw.println("    suppresses the current ambient display configuration and disables");
        pw.println("    ambient display");
        pw.println("  list-ambient-display-suppression-tokens");
        pw.println("    prints the tokens used to suppress ambient display");
        pw.println("  set-prox [list|acquire|release] (-d <display_id>)");
        pw.println("    Acquires the proximity sensor wakelock. Wakelock is associated with");
        pw.println("    a specific display if specified. 'list' lists wakelocks previously");
        pw.println("    created by set-prox including their held status.");
        pw.println("  set-face-down-detector [true|false]");
        pw.println("    sets whether we use face down detector timeouts or not");
        pw.println();
        android.content.Intent.printIntentArgsHelp(pw, "");
    }
}
