package com.android.server.ondeviceintelligence;

/* JADX INFO: loaded from: classes2.dex */
final class OnDeviceIntelligenceShellCommand extends android.os.ShellCommand {
    private static final java.lang.String TAG = com.android.server.ondeviceintelligence.OnDeviceIntelligenceShellCommand.class.getSimpleName();
    private final com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService mService;

    OnDeviceIntelligenceShellCommand(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003a  */
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
                case -2091542783: goto L2f;
                case -365435659: goto L25;
                case -108354651: goto L1a;
                case 1159014577: goto Lf;
                default: goto Le;
            }
        Le:
            goto L3a
        Lf:
            java.lang.String r0 = "set-deviceconfig-namespace"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 3
            goto L3b
        L1a:
            java.lang.String r0 = "set-temporary-services"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 0
            goto L3b
        L25:
            java.lang.String r0 = "get-services"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 1
            goto L3b
        L2f:
            java.lang.String r0 = "set-model-broadcasts"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 2
            goto L3b
        L3a:
            r0 = -1
        L3b:
            switch(r0) {
                case 0: goto L52;
                case 1: goto L4d;
                case 2: goto L48;
                case 3: goto L43;
                default: goto L3e;
            }
        L3e:
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L43:
            int r0 = r1.setDeviceConfigNamespace()
            return r0
        L48:
            int r0 = r1.setBroadcastKeys()
            return r0
        L4d:
            int r0 = r1.getConfiguredServices()
            return r0
        L52:
            int r0 = r1.setTemporaryServices()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.ondeviceintelligence.OnDeviceIntelligenceShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("OnDeviceIntelligenceShellCommand commands: ");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println();
        pw.println("  set-temporary-services [IntelligenceServiceComponentName] [InferenceServiceComponentName] [DURATION]");
        pw.println("    Temporarily (for DURATION ms) changes the service implementations.");
        pw.println("    To reset, call without any arguments.");
        pw.println("  get-services To get the names of services that are currently being used.");
        pw.println("  set-model-broadcasts [ModelLoadedBroadcastKey] [ModelUnloadedBroadcastKey] [ReceiverPackageName] [DURATION] To set the names of broadcast intent keys that are to be emitted for cts tests.");
        pw.println("  set-deviceconfig-namespace [DeviceConfigNamespace] [DURATION] To set the device config namespace to use for cts tests.");
    }

    private int setTemporaryServices() {
        java.io.PrintWriter out = getOutPrintWriter();
        java.lang.String intelligenceServiceName = getNextArg();
        java.lang.String inferenceServiceName = getNextArg();
        if (getRemainingArgsCount() == 0 && intelligenceServiceName == null && inferenceServiceName == null) {
            com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.enforceShellOnly(android.os.Binder.getCallingUid(), "resetTemporaryServices");
            this.mService.resetTemporaryServices();
            out.println("OnDeviceIntelligenceManagerService temporary reset. ");
            return 0;
        }
        java.util.Objects.requireNonNull(intelligenceServiceName);
        java.util.Objects.requireNonNull(inferenceServiceName);
        int duration = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setTemporaryServices(new java.lang.String[]{intelligenceServiceName, inferenceServiceName}, duration);
        out.println("OnDeviceIntelligenceService temporarily set to " + intelligenceServiceName + " \n and \n OnDeviceTrustedInferenceService set to " + inferenceServiceName + " for " + duration + "ms");
        return 0;
    }

    private int getConfiguredServices() {
        java.io.PrintWriter out = getOutPrintWriter();
        java.lang.String[] services = this.mService.getServiceNames();
        out.println("OnDeviceIntelligenceService set to :  " + services[0] + " \n and \n OnDeviceTrustedInferenceService set to : " + services[1]);
        return 0;
    }

    private int setBroadcastKeys() {
        java.io.PrintWriter out = getOutPrintWriter();
        java.lang.String modelLoadedKey = getNextArgRequired();
        java.lang.String modelUnloadedKey = getNextArgRequired();
        java.lang.String receiverPackageName = getNextArg();
        int duration = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setModelBroadcastKeys(new java.lang.String[]{modelLoadedKey, modelUnloadedKey}, receiverPackageName, duration);
        out.println("OnDeviceIntelligence Model Loading broadcast keys temporarily set to " + modelLoadedKey + " \n and \n OnDeviceTrustedInferenceService set to " + modelUnloadedKey + "\n and Package name set to : " + receiverPackageName + " for " + duration + "ms");
        return 0;
    }

    private int setDeviceConfigNamespace() {
        java.io.PrintWriter out = getOutPrintWriter();
        java.lang.String configNamespace = getNextArg();
        int duration = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setTemporaryDeviceConfigNamespace(configNamespace, duration);
        out.println("OnDeviceIntelligence DeviceConfig Namespace temporarily set to " + configNamespace + " for " + duration + "ms");
        return 0;
    }
}
