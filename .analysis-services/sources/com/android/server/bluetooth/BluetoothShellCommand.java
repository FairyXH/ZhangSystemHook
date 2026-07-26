package com.android.server.bluetooth;

/* JADX INFO: loaded from: classes.dex */
class BluetoothShellCommand extends com.android.modules.utils.BasicShellCommandHandler {
    private static final java.lang.String TAG = com.android.server.bluetooth.BluetoothShellCommand.class.getSimpleName();
    final com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand[] mBluetoothCommands = {new com.android.server.bluetooth.BluetoothShellCommand.Enable(), new com.android.server.bluetooth.BluetoothShellCommand.EnableBle(), new com.android.server.bluetooth.BluetoothShellCommand.Disable(), new com.android.server.bluetooth.BluetoothShellCommand.DisableBle(), new com.android.server.bluetooth.BluetoothShellCommand.WaitForAdapterState()};
    private final com.android.server.bluetooth.BluetoothManagerService mManagerService;

    static abstract class BluetoothCommand {
        final boolean mIsPrivileged;
        final java.lang.String mName;

        abstract int exec(java.lang.String str) throws android.os.RemoteException;

        abstract void onHelp(java.io.PrintWriter printWriter);

        BluetoothCommand(boolean isPrivileged, java.lang.String name) {
            this.mIsPrivileged = isPrivileged;
            this.mName = (java.lang.String) java.util.Objects.requireNonNull(name, "Command name cannot be null");
        }

        java.lang.String getName() {
            return this.mName;
        }

        boolean isMatch(java.lang.String cmd) {
            return this.mName.equals(cmd);
        }

        boolean isPrivileged() {
            return this.mIsPrivileged;
        }
    }

    class EnableBle extends com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand {
        EnableBle() {
            super(true, "enableBle");
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public int exec(java.lang.String cmd) throws android.os.RemoteException {
            if (com.android.server.bluetooth.BluetoothShellCommand.this.mManagerService.getBinder().enableBle(android.content.AttributionSource.myAttributionSource(), com.android.server.bluetooth.BluetoothShellCommand.this.mManagerService.getBinder())) {
                return 0;
            }
            return -1;
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public void onHelp(java.io.PrintWriter pw) {
            pw.println("  " + getName());
            pw.println("    Call enableBle to activate ble only mode on this device.");
        }
    }

    class DisableBle extends com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand {
        DisableBle() {
            super(true, "disableBle");
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public int exec(java.lang.String cmd) throws android.os.RemoteException {
            if (com.android.server.bluetooth.BluetoothShellCommand.this.mManagerService.getBinder().disableBle(android.content.AttributionSource.myAttributionSource(), com.android.server.bluetooth.BluetoothShellCommand.this.mManagerService.getBinder())) {
                return 0;
            }
            return -1;
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public void onHelp(java.io.PrintWriter pw) {
            pw.println("  " + getName());
            pw.println("    revoke the call to enableBle. No-op if enableBle wasn't call before");
        }
    }

    class Enable extends com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand {
        Enable() {
            super(false, com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE);
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public int exec(java.lang.String cmd) throws android.os.RemoteException {
            if (com.android.server.bluetooth.BluetoothShellCommand.this.mManagerService.getBinder().enable(android.content.AttributionSource.myAttributionSource())) {
                return 0;
            }
            return -1;
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public void onHelp(java.io.PrintWriter pw) {
            pw.println("  " + getName());
            pw.println("    Enable Bluetooth on this device.");
        }
    }

    class Disable extends com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand {
        Disable() {
            super(false, "disable");
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public int exec(java.lang.String cmd) throws android.os.RemoteException {
            if (com.android.server.bluetooth.BluetoothShellCommand.this.mManagerService.getBinder().disable(android.content.AttributionSource.myAttributionSource(), true)) {
                return 0;
            }
            return -1;
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public void onHelp(java.io.PrintWriter pw) {
            pw.println("  " + getName());
            pw.println("    Disable Bluetooth on this device.");
        }
    }

    class WaitForAdapterState extends com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand {
        WaitForAdapterState() {
            super(false, "wait-for-state");
        }

        private int getWaitingState(java.lang.String in) {
            byte b = -1;
            if (!in.startsWith(getName() + ":")) {
                return -1;
            }
            java.lang.String[] split = in.split(":", 2);
            if (split.length != 2 || !getName().equals(split[0])) {
                java.lang.String msg = getName() + ": Invalid state format: " + in;
                com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothShellCommand.TAG, msg);
                java.io.PrintWriter pw = com.android.server.bluetooth.BluetoothShellCommand.this.getErrPrintWriter();
                pw.println(com.android.server.bluetooth.BluetoothShellCommand.TAG + ": " + msg);
                com.android.server.bluetooth.BluetoothShellCommand.this.printHelp(pw);
                throw new java.lang.IllegalArgumentException();
            }
            java.lang.String str = split[1];
            switch (str.hashCode()) {
                case 701992065:
                    if (str.equals("STATE_OFF")) {
                        b = 0;
                    }
                    break;
                case 2100854893:
                    if (str.equals("STATE_ON")) {
                        b = 1;
                    }
                    break;
            }
            switch (b) {
                case 0:
                    return 10;
                case 1:
                    return 12;
                default:
                    java.lang.String msg2 = getName() + ": Invalid state value: " + split[1] + ". From: " + in;
                    com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothShellCommand.TAG, msg2);
                    java.io.PrintWriter pw2 = com.android.server.bluetooth.BluetoothShellCommand.this.getErrPrintWriter();
                    pw2.println(com.android.server.bluetooth.BluetoothShellCommand.TAG + ": " + msg2);
                    com.android.server.bluetooth.BluetoothShellCommand.this.printHelp(pw2);
                    throw new java.lang.IllegalArgumentException();
            }
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        boolean isMatch(java.lang.String cmd) {
            return getWaitingState(cmd) != -1;
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public int exec(java.lang.String cmd) throws android.os.RemoteException {
            int ret = com.android.server.bluetooth.BluetoothShellCommand.this.mManagerService.waitForManagerState(getWaitingState(cmd)) ? 0 : -1;
            com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothShellCommand.TAG, cmd + ": Return value is " + ret);
            return ret;
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public void onHelp(java.io.PrintWriter pw) {
            pw.println("  " + getName() + ":<STATE>");
            pw.println("    Wait until the adapter state is <STATE>. <STATE> can be one of STATE_OFF | STATE_ON");
            pw.println("    Note: This command can timeout and failed");
        }
    }

    BluetoothShellCommand(com.android.server.bluetooth.BluetoothManagerService managerService) {
        this.mManagerService = managerService;
    }

    public int onCommand(java.lang.String cmd) {
        int uid;
        if (cmd == null) {
            return handleDefaultCommands(null);
        }
        for (com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand bt_cmd : this.mBluetoothCommands) {
            if (bt_cmd.isMatch(cmd)) {
                if (bt_cmd.isPrivileged() && (uid = android.os.Binder.getCallingUid()) != 0) {
                    throw new java.lang.SecurityException("Uid " + uid + " does not have access to " + cmd + " bluetooth command");
                }
                try {
                    getOutPrintWriter().println(TAG + ": Exec " + cmd);
                    com.android.server.bluetooth.Log.d(TAG, "Exec " + cmd);
                    int ret = bt_cmd.exec(cmd);
                    if (ret == 0) {
                        java.lang.String msg = cmd + ": Success";
                        com.android.server.bluetooth.Log.d(TAG, msg);
                        getOutPrintWriter().println(msg);
                    } else {
                        java.lang.String msg2 = cmd + ": Failed with status=" + ret;
                        com.android.server.bluetooth.Log.e(TAG, msg2);
                        getErrPrintWriter().println(TAG + ": " + msg2);
                    }
                    return ret;
                } catch (android.os.RemoteException e) {
                    com.android.server.bluetooth.Log.w(TAG, cmd + ": error\nException: " + e.getMessage());
                    getErrPrintWriter().println(cmd + ": error\nException: " + e.getMessage());
                    e.rethrowFromSystemServer();
                }
            }
        }
        return handleDefaultCommands(cmd);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void printHelp(java.io.PrintWriter pw) {
        pw.println("Bluetooth Manager Commands:");
        pw.println("  help or -h");
        pw.println("    Print this help text.");
        for (com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand bt_cmd : this.mBluetoothCommands) {
            bt_cmd.onHelp(pw);
        }
    }

    public void onHelp() {
        printHelp(getOutPrintWriter());
    }
}
