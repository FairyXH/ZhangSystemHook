package com.android.server.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
final class IRadioServiceAidlImpl extends android.hardware.radio.IRadioService.Stub {
    private static final java.util.List<java.lang.String> SERVICE_NAMES = java.util.Arrays.asList(android.hardware.broadcastradio.IBroadcastRadio.DESCRIPTOR + "/amfm", android.hardware.broadcastradio.IBroadcastRadio.DESCRIPTOR + "/dab");
    private static final java.lang.String TAG = "BcRadioSrvAidl";
    private final com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl mAidlHalClient;
    private final com.android.server.broadcastradio.BroadcastRadioService mService;

    public static java.util.ArrayList<java.lang.String> getServicesNames() {
        java.util.ArrayList<java.lang.String> serviceList = new java.util.ArrayList<>();
        for (int i = 0; i < SERVICE_NAMES.size(); i++) {
            android.os.IBinder serviceBinder = android.os.ServiceManager.waitForDeclaredService(SERVICE_NAMES.get(i));
            if (serviceBinder != null) {
                serviceList.add(SERVICE_NAMES.get(i));
            }
        }
        return serviceList;
    }

    IRadioServiceAidlImpl(com.android.server.broadcastradio.BroadcastRadioService service, java.util.ArrayList<java.lang.String> serviceList) {
        this(service, new com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl(serviceList));
        com.android.server.utils.Slogf.i(TAG, "Initialize BroadcastRadioServiceAidl(%s)", service);
    }

    IRadioServiceAidlImpl(com.android.server.broadcastradio.BroadcastRadioService service, com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl halAidl) {
        this.mService = (com.android.server.broadcastradio.BroadcastRadioService) java.util.Objects.requireNonNull(service, "Broadcast radio service cannot be null");
        this.mAidlHalClient = (com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl) java.util.Objects.requireNonNull(halAidl, "Broadcast radio service implementation for AIDL HAL cannot be null");
    }

    public java.util.List<android.hardware.radio.RadioManager.ModuleProperties> listModules() {
        this.mService.enforcePolicyAccess();
        return this.mAidlHalClient.listModules();
    }

    public android.hardware.radio.ITuner openTuner(int moduleId, android.hardware.radio.RadioManager.BandConfig bandConfig, boolean withAudio, android.hardware.radio.ITunerCallback callback) throws android.os.RemoteException {
        if (isDebugEnabled()) {
            com.android.server.utils.Slogf.d(TAG, "Opening module %d", java.lang.Integer.valueOf(moduleId));
        }
        this.mService.enforcePolicyAccess();
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("Callback must not be null");
        }
        return this.mAidlHalClient.openSession(moduleId, bandConfig, withAudio, callback);
    }

    public android.hardware.radio.ICloseHandle addAnnouncementListener(int[] enabledTypes, android.hardware.radio.IAnnouncementListener listener) {
        if (isDebugEnabled()) {
            com.android.server.utils.Slogf.d(TAG, "Adding announcement listener for %s", java.util.Arrays.toString(enabledTypes));
        }
        java.util.Objects.requireNonNull(enabledTypes, "Enabled announcement types cannot be null");
        java.util.Objects.requireNonNull(listener, "Announcement listener cannot be null");
        this.mService.enforcePolicyAccess();
        return this.mAidlHalClient.addAnnouncementListener(enabledTypes, listener);
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter printWriter, java.lang.String[] args) {
        if (this.mService.getContext().checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            printWriter.println("Permission Denial: can't dump AIDL BroadcastRadioService from from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " without permission android.permission.DUMP");
            return;
        }
        android.util.IndentingPrintWriter radioPrintWriter = new android.util.IndentingPrintWriter(printWriter);
        radioPrintWriter.printf("BroadcastRadioService\n", new java.lang.Object[0]);
        radioPrintWriter.increaseIndent();
        radioPrintWriter.printf("AIDL HAL client:\n", new java.lang.Object[0]);
        radioPrintWriter.increaseIndent();
        this.mAidlHalClient.dumpInfo(radioPrintWriter);
        radioPrintWriter.decreaseIndent();
        radioPrintWriter.decreaseIndent();
    }

    private static boolean isDebugEnabled() {
        return android.util.Log.isLoggable(TAG, 3);
    }
}
