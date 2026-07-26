package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class HdmiControlShellCommand extends android.os.ShellCommand {
    private static final java.lang.String TAG = "HdmiShellCommand";
    private final android.hardware.hdmi.IHdmiControlService.Stub mBinderService;
    final java.util.concurrent.CountDownLatch mLatch = new java.util.concurrent.CountDownLatch(1);
    java.util.concurrent.atomic.AtomicInteger mCecResult = new java.util.concurrent.atomic.AtomicInteger();
    android.hardware.hdmi.IHdmiControlCallback.Stub mHdmiControlCallback = new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlShellCommand.1
        public void onComplete(int result) {
            com.android.server.hdmi.HdmiControlShellCommand.this.getOutPrintWriter().println(" done (" + com.android.server.hdmi.HdmiControlShellCommand.this.getResultString(result) + ")");
            com.android.server.hdmi.HdmiControlShellCommand.this.mCecResult.set(result);
            com.android.server.hdmi.HdmiControlShellCommand.this.mLatch.countDown();
        }
    };

    HdmiControlShellCommand(android.hardware.hdmi.IHdmiControlService.Stub binderService) {
        this.mBinderService = binderService;
    }

    public int onCommand(java.lang.String cmd) {
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        try {
            return handleShellCommand(cmd);
        } catch (java.lang.Exception e) {
            getErrPrintWriter().println("Caught error for command '" + cmd + "': " + e.getMessage());
            android.util.Slog.e(TAG, "Error handling hdmi_control shell command: " + cmd, e);
            return 1;
        }
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("HdmiControlManager (hdmi_control) commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  onetouchplay, otp");
        pw.println("      Send the \"One Touch Play\" feature from a source to the TV");
        pw.println("  vendorcommand --device_type <originating device type>");
        pw.println("                --destination <destination device>");
        pw.println("                --args <vendor specific arguments>");
        pw.println("                [--id <true if vendor command should be sent with vendor id>]");
        pw.println("      Send a Vendor Command to the given target device");
        pw.println("  cec_setting get <setting name>");
        pw.println("      Get the current value of a CEC setting");
        pw.println("  cec_setting set <setting name> <value>");
        pw.println("      Set the value of a CEC setting");
        pw.println("  setsystemaudiomode, setsam [on|off]");
        pw.println("      Sets the System Audio Mode feature on or off on TV devices");
        pw.println("  setarc [on|off]");
        pw.println("      Sets the ARC feature on or off on TV devices");
        pw.println("  deviceselect <device id>");
        pw.println("      Switch to device with given id");
        pw.println("      The device's id is represented by its logical address.");
        pw.println("  history_size get");
        pw.println("      Gets the number of messages that can be stored in dumpsys history");
        pw.println("  history_size set <new_size>");
        pw.println("      Changes the number of messages that can be stored in dumpsys history to new_size");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:32:0x006e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int handleShellCommand(java.lang.String r3) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 214
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiControlShellCommand.handleShellCommand(java.lang.String):int");
    }

    private int deviceSelect(java.io.PrintWriter pw) throws android.os.RemoteException {
        if (getRemainingArgsCount() != 1) {
            throw new java.lang.IllegalArgumentException("Expected exactly 1 argument.");
        }
        int deviceId = java.lang.Integer.parseInt(getNextArg());
        pw.print("Sending Device Select...");
        this.mBinderService.deviceSelect(deviceId, this.mHdmiControlCallback);
        return (receiveCallback("Device Select") && this.mCecResult.get() == 0) ? 0 : 1;
    }

    private int oneTouchPlay(java.io.PrintWriter pw) throws android.os.RemoteException {
        pw.print("Sending One Touch Play...");
        this.mBinderService.oneTouchPlay(this.mHdmiControlCallback);
        return (receiveCallback("One Touch Play") && this.mCecResult.get() == 0) ? 0 : 1;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x0017. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:34:0x006a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int vendorCommand(java.io.PrintWriter r12) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 274
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiControlShellCommand.vendorCommand(java.io.PrintWriter):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int cecSetting(java.io.PrintWriter r9) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 238
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiControlShellCommand.cecSetting(java.io.PrintWriter):int");
    }

    private int setSystemAudioMode(java.io.PrintWriter pw) throws android.os.RemoteException {
        if (1 > getRemainingArgsCount()) {
            throw new java.lang.IllegalArgumentException("Please indicate if System Audio Mode should be turned \"on\" or \"off\".");
        }
        java.lang.String arg = getNextArg();
        if (arg.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON)) {
            pw.println("Setting System Audio Mode on");
            this.mBinderService.setSystemAudioMode(true, this.mHdmiControlCallback);
        } else if (arg.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF)) {
            pw.println("Setting System Audio Mode off");
            this.mBinderService.setSystemAudioMode(false, this.mHdmiControlCallback);
        } else {
            throw new java.lang.IllegalArgumentException("Please indicate if System Audio Mode should be turned \"on\" or \"off\".");
        }
        return (receiveCallback("Set System Audio Mode") && this.mCecResult.get() == 0) ? 0 : 1;
    }

    private int setArcMode(java.io.PrintWriter pw) throws android.os.RemoteException {
        if (1 > getRemainingArgsCount()) {
            throw new java.lang.IllegalArgumentException("Please indicate if ARC mode should be turned \"on\" or \"off\".");
        }
        java.lang.String arg = getNextArg();
        if (arg.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON)) {
            pw.println("Setting ARC mode on");
            this.mBinderService.setArcMode(true);
        } else if (arg.equals(kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF)) {
            pw.println("Setting ARC mode off");
            this.mBinderService.setArcMode(false);
        } else {
            throw new java.lang.IllegalArgumentException("Please indicate if ARC mode should be turned \"on\" or \"off\".");
        }
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int historySize(java.io.PrintWriter r7) throws android.os.RemoteException {
        /*
            r6 = this;
            int r0 = r6.getRemainingArgsCount()
            r1 = 1
            if (r1 > r0) goto La9
            java.lang.String r0 = r6.getNextArgRequired()
            int r2 = r0.hashCode()
            r3 = 0
            switch(r2) {
                case 102230: goto L1f;
                case 113762: goto L14;
                default: goto L13;
            }
        L13:
            goto L29
        L14:
            java.lang.String r2 = "set"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L13
            r2 = r1
            goto L2a
        L1f:
            java.lang.String r2 = "get"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L13
            r2 = r3
            goto L2a
        L29:
            r2 = -1
        L2a:
            switch(r2) {
                case 0: goto L8c;
                case 1: goto L46;
                default: goto L2d;
            }
        L2d:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Unknown operation: "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r0)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L46:
            java.lang.String r2 = r6.getNextArgRequired()
            int r1 = java.lang.Integer.parseInt(r2)     // Catch: java.lang.NumberFormatException -> L74
            android.hardware.hdmi.IHdmiControlService$Stub r4 = r6.mBinderService
            boolean r4 = r4.setMessageHistorySize(r1)
            if (r4 == 0) goto L6e
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Setting CEC dumpsys message history size to "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r1)
            java.lang.String r4 = r4.toString()
            r7.println(r4)
            goto L73
        L6e:
            java.lang.String r4 = "Message history size not changed, was it lower than the minimum size?"
            r7.println(r4)
        L73:
            return r3
        L74:
            r3 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Cannot set CEC dumpsys message history size to "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r2)
            java.lang.String r4 = r4.toString()
            r7.println(r4)
            return r1
        L8c:
            android.hardware.hdmi.IHdmiControlService$Stub r1 = r6.mBinderService
            int r1 = r1.getMessageHistorySize()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "CEC dumpsys message history size = "
            java.lang.StringBuilder r2 = r2.append(r4)
            java.lang.StringBuilder r2 = r2.append(r1)
            java.lang.String r2 = r2.toString()
            r7.println(r2)
            return r3
        La9:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Use 'set' or 'get' for the command action"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiControlShellCommand.historySize(java.io.PrintWriter):int");
    }

    private boolean receiveCallback(java.lang.String command) {
        try {
            if (!this.mLatch.await(2000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                getErrPrintWriter().println(command + " timed out.");
                return false;
            }
            return true;
        } catch (java.lang.InterruptedException e) {
            getErrPrintWriter().println("Caught InterruptedException");
            java.lang.Thread.currentThread().interrupt();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getResultString(int result) {
        switch (result) {
            case 0:
                return "Success";
            case 1:
                return "Timeout";
            case 2:
                return "Source not available";
            case 3:
                return "Target not available";
            case 4:
            default:
                return java.lang.Integer.toString(result);
            case 5:
                return "Exception";
            case 6:
                return "Incorrect mode";
            case 7:
                return "Communication Failed";
        }
    }
}
