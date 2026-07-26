package com.android.server.companion;

/* JADX INFO: loaded from: classes.dex */
class CompanionDeviceShellCommand extends android.os.ShellCommand {
    private static final java.lang.String TAG = "CDM_CompanionDeviceShellCommand";
    private final com.android.server.companion.association.AssociationRequestsProcessor mAssociationRequestsProcessor;
    private final com.android.server.companion.association.AssociationStore mAssociationStore;
    private final com.android.server.companion.BackupRestoreProcessor mBackupRestoreProcessor;
    private final com.android.server.companion.devicepresence.DevicePresenceProcessor mDevicePresenceProcessor;
    private final com.android.server.companion.association.DisassociationProcessor mDisassociationProcessor;
    private final com.android.server.companion.CompanionDeviceManagerService mService;
    private final com.android.server.companion.datatransfer.SystemDataTransferProcessor mSystemDataTransferProcessor;
    private final com.android.server.companion.transport.CompanionTransportManager mTransportManager;

    CompanionDeviceShellCommand(com.android.server.companion.CompanionDeviceManagerService service, com.android.server.companion.association.AssociationStore associationStore, com.android.server.companion.devicepresence.DevicePresenceProcessor devicePresenceProcessor, com.android.server.companion.transport.CompanionTransportManager transportManager, com.android.server.companion.datatransfer.SystemDataTransferProcessor systemDataTransferProcessor, com.android.server.companion.association.AssociationRequestsProcessor associationRequestsProcessor, com.android.server.companion.BackupRestoreProcessor backupRestoreProcessor, com.android.server.companion.association.DisassociationProcessor disassociationProcessor) {
        this.mService = service;
        this.mAssociationStore = associationStore;
        this.mDevicePresenceProcessor = devicePresenceProcessor;
        this.mTransportManager = transportManager;
        this.mSystemDataTransferProcessor = systemDataTransferProcessor;
        this.mAssociationRequestsProcessor = associationRequestsProcessor;
        this.mBackupRestoreProcessor = backupRestoreProcessor;
        this.mDisassociationProcessor = disassociationProcessor;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Not initialized variable reg: 34, insn: 0x04af: MOVE (r4 I:??[OBJECT, ARRAY]) = (r34 I:??[OBJECT, ARRAY] A[D('out' java.io.PrintWriter)]), block:B:167:0x04af */
    /* JADX WARN: Removed duplicated region for block: B:92:0x016a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r36) {
        /*
            Method dump skipped, instruction units count: 1956
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.companion.CompanionDeviceShellCommand.onCommand(java.lang.String):int");
    }

    private int getNextIntArgRequired() {
        return java.lang.Integer.parseInt(getNextArgRequired());
    }

    private boolean getNextBooleanArgRequired() {
        java.lang.String arg = getNextArgRequired();
        if ("true".equalsIgnoreCase(arg) || "false".equalsIgnoreCase(arg)) {
            return java.lang.Boolean.parseBoolean(arg);
        }
        throw new java.lang.IllegalArgumentException("Expected a boolean argument but was: " + arg);
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Companion Device Manager (companiondevice) commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  list USER_ID");
        pw.println("      List all Associations for a user.");
        pw.println("  associate USER_ID PACKAGE MAC_ADDRESS [DEVICE_PROFILE]");
        pw.println("      Create a new Association.");
        pw.println("  disassociate USER_ID PACKAGE MAC_ADDRESS");
        pw.println("      Remove an existing Association.");
        pw.println("  disassociate-all USER_ID");
        pw.println("      Remove all Associations for a user.");
        pw.println("  refresh-cache");
        pw.println("      Clear the in-memory association cache and reload all association ");
        pw.println("      information from disk. USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
        pw.println("  simulate-device-appeared ASSOCIATION_ID");
        pw.println("      Make CDM act as if the given companion device has appeared.");
        pw.println("      I.e. bind the associated companion application's");
        pw.println("      CompanionDeviceService(s) and trigger onDeviceAppeared() callback.");
        pw.println("      The CDM will consider the devices as present for 60 seconds and then");
        pw.println("      will act as if device disappeared, unless 'simulate-device-disappeared'");
        pw.println("      or 'simulate-device-appeared' is called again before 60 seconds run out.");
        pw.println("      USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
        pw.println("  simulate-device-disappeared ASSOCIATION_ID");
        pw.println("      Make CDM act as if the given companion device has disappeared.");
        pw.println("      I.e. unbind the associated companion application's");
        pw.println("      CompanionDeviceService(s) and trigger onDeviceDisappeared() callback.");
        pw.println("      NOTE: This will only have effect if 'simulate-device-appeared' was");
        pw.println("      invoked for the same device (same ASSOCIATION_ID) no longer than");
        pw.println("      60 seconds ago.");
        pw.println("  get-backup-payload USER_ID");
        pw.println("      Generate backup payload for the given user and print its content");
        pw.println("      encoded to a Base64 string.");
        pw.println("      USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
        pw.println("  apply-restored-payload USER_ID PAYLOAD");
        pw.println("      Apply restored backup payload for the given user.");
        pw.println("      USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
        if (com.android.internal.hidden_from_bootclasspath.android.companion.Flags.devicePresence()) {
            pw.println("  simulate-device-event ASSOCIATION_ID EVENT");
            pw.println("  Simulate the companion device event changes:");
            pw.println("    Case(0): ");
            pw.println("      Make CDM act as if the given companion device has appeared.");
            pw.println("      I.e. bind the associated companion application's");
            pw.println("      CompanionDeviceService(s) and trigger onDeviceAppeared() callback.");
            pw.println("      The CDM will consider the devices as present for60 seconds and then");
            pw.println("      will act as if device disappeared, unless'simulate-device-disappeared'");
            pw.println("      or 'simulate-device-appeared' is called again before 60 secondsrun out.");
            pw.println("    Case(1): ");
            pw.println("      Make CDM act as if the given companion device has disappeared.");
            pw.println("      I.e. unbind the associated companion application's");
            pw.println("      CompanionDeviceService(s) and trigger onDeviceDisappeared()callback.");
            pw.println("      NOTE: This will only have effect if 'simulate-device-appeared' was");
            pw.println("      invoked for the same device (same ASSOCIATION_ID) no longer than");
            pw.println("      60 seconds ago.");
            pw.println("    Case(2): ");
            pw.println("      Make CDM act as if the given companion device is BT connected ");
            pw.println("    Case(3): ");
            pw.println("      Make CDM act as if the given companion device is BT disconnected ");
            pw.println("      USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
            pw.println("  simulate-device-uuid-event UUID PACKAGE USERID EVENT");
            pw.println("  Simulate the companion device event changes:");
            pw.println("    Case(2): ");
            pw.println("      Make CDM act as if the given DEVICE is BT connected baseon the UUID");
            pw.println("    Case(3): ");
            pw.println("      Make CDM act as if the given DEVICE is BT disconnected baseon the UUID");
            pw.println("      USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
            pw.println("  simulate-device-event-device-locked ASSOCIATION_ID USER_ID DEVICE_EVENT PARCEL_UUID");
            pw.println("  Simulate device event when the device is locked");
            pw.println("  USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
            pw.println("  simulate-device-event-device-unlocked USER_ID");
            pw.println("  Simulate device unlocked for given user. This will send corresponding");
            pw.println("  callback after simulate-device-event-device-locked");
            pw.println("  command has been called.");
            pw.println("  USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
            pw.println("  start-observing-device-presence-uuid USER_ID PACKAGE_NAME UUID");
            pw.println("  Start observing device presence base on the UUID.");
            pw.println("  USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
            pw.println("  stop-observing-device-presence-uuid USER_ID PACKAGE_NAME UUID");
            pw.println("  Stop observing device presence base on the UUID.");
            pw.println("  USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
        }
        pw.println("  remove-inactive-associations");
        pw.println("      Remove self-managed associations that have not been active ");
        pw.println("      for a long time (90 days or as configured via ");
        pw.println("      \"debug.cdm.cdmservice.removal_time_window\" system property). ");
        pw.println("      USE FOR DEBUGGING AND/OR TESTING PURPOSES ONLY.");
        pw.println("  create-emulated-transport <ASSOCIATION_ID>");
        pw.println("      Create an EmulatedTransport for testing purposes only");
        pw.println("  enable-perm-sync <ASSOCIATION_ID>");
        pw.println("      Enable perm sync for the association.");
        pw.println("  disable-perm-sync <ASSOCIATION_ID>");
        pw.println("      Disable perm sync for the association.");
        pw.println("  get-perm-sync-state <ASSOCIATION_ID>");
        pw.println("      Get perm sync state for the association.");
        pw.println("  remove-perm-sync-state <ASSOCIATION_ID>");
        pw.println("      Remove perm sync state for the association.");
    }
}
