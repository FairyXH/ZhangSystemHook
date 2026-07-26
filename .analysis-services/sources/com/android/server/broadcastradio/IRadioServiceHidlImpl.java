package com.android.server.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
final class IRadioServiceHidlImpl extends android.hardware.radio.IRadioService.Stub {
    private static final java.lang.String TAG = "BcRadioSrvHidl";
    private final com.android.server.broadcastradio.hal1.BroadcastRadioService mHal1Client;
    private final com.android.server.broadcastradio.hal2.BroadcastRadioService mHal2Client;
    private final java.lang.Object mLock;
    private final com.android.server.broadcastradio.BroadcastRadioService mService;
    private final java.util.List<android.hardware.radio.RadioManager.ModuleProperties> mV1Modules;

    IRadioServiceHidlImpl(com.android.server.broadcastradio.BroadcastRadioService service) {
        this.mLock = new java.lang.Object();
        this.mService = (com.android.server.broadcastradio.BroadcastRadioService) java.util.Objects.requireNonNull(service, "broadcast radio service cannot be null");
        this.mHal1Client = new com.android.server.broadcastradio.hal1.BroadcastRadioService();
        this.mV1Modules = this.mHal1Client.loadModules();
        java.util.OptionalInt max = this.mV1Modules.stream().mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.broadcastradio.IRadioServiceHidlImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(java.lang.Object obj) {
                return ((android.hardware.radio.RadioManager.ModuleProperties) obj).getId();
            }
        }).max();
        this.mHal2Client = new com.android.server.broadcastradio.hal2.BroadcastRadioService(max.isPresent() ? max.getAsInt() + 1 : 0);
    }

    IRadioServiceHidlImpl(com.android.server.broadcastradio.BroadcastRadioService service, com.android.server.broadcastradio.hal1.BroadcastRadioService hal1, com.android.server.broadcastradio.hal2.BroadcastRadioService hal2) {
        this.mLock = new java.lang.Object();
        this.mService = (com.android.server.broadcastradio.BroadcastRadioService) java.util.Objects.requireNonNull(service, "Broadcast radio service cannot be null");
        this.mHal1Client = (com.android.server.broadcastradio.hal1.BroadcastRadioService) java.util.Objects.requireNonNull(hal1, "Broadcast radio service implementation for HIDL 1 HAL cannot be null");
        this.mV1Modules = this.mHal1Client.loadModules();
        this.mHal2Client = (com.android.server.broadcastradio.hal2.BroadcastRadioService) java.util.Objects.requireNonNull(hal2, "Broadcast radio service implementation for HIDL 2 HAL cannot be null");
    }

    public java.util.List<android.hardware.radio.RadioManager.ModuleProperties> listModules() {
        java.util.List<android.hardware.radio.RadioManager.ModuleProperties> modules;
        this.mService.enforcePolicyAccess();
        java.util.Collection<android.hardware.radio.RadioManager.ModuleProperties> v2Modules = this.mHal2Client.listModules();
        synchronized (this.mLock) {
            modules = new java.util.ArrayList<>(this.mV1Modules.size() + v2Modules.size());
            modules.addAll(this.mV1Modules);
        }
        modules.addAll(v2Modules);
        return modules;
    }

    public android.hardware.radio.ITuner openTuner(int moduleId, android.hardware.radio.RadioManager.BandConfig bandConfig, boolean withAudio, android.hardware.radio.ITunerCallback callback) throws android.os.RemoteException {
        if (isDebugEnabled()) {
            android.util.Slog.d(TAG, "Opening module " + moduleId);
        }
        this.mService.enforcePolicyAccess();
        java.util.Objects.requireNonNull(callback, "Callback must not be null");
        synchronized (this.mLock) {
            if (this.mHal2Client.hasModule(moduleId)) {
                return this.mHal2Client.openSession(moduleId, bandConfig, withAudio, callback);
            }
            return this.mHal1Client.openTuner(moduleId, bandConfig, withAudio, callback);
        }
    }

    public android.hardware.radio.ICloseHandle addAnnouncementListener(int[] enabledTypes, android.hardware.radio.IAnnouncementListener listener) {
        if (isDebugEnabled()) {
            android.util.Slog.d(TAG, "Adding announcement listener for " + java.util.Arrays.toString(enabledTypes));
        }
        java.util.Objects.requireNonNull(enabledTypes, "Enabled announcement types cannot be null");
        java.util.Objects.requireNonNull(listener, "Announcement listener cannot be null");
        this.mService.enforcePolicyAccess();
        synchronized (this.mLock) {
            if (!this.mHal2Client.hasAnyModules()) {
                android.util.Slog.w(TAG, "There are no HAL 2.0 modules registered");
                return new com.android.server.broadcastradio.hal2.AnnouncementAggregator(listener, this.mLock);
            }
            return this.mHal2Client.addAnnouncementListener(enabledTypes, listener);
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (this.mService.getContext().checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            pw.println("Permission Denial: can't dump HIDL BroadcastRadioService from from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " without permission android.permission.DUMP");
            return;
        }
        android.util.IndentingPrintWriter radioPw = new android.util.IndentingPrintWriter(pw);
        radioPw.printf("BroadcastRadioService\n", new java.lang.Object[0]);
        radioPw.increaseIndent();
        radioPw.printf("HAL1 client: %s\n", new java.lang.Object[]{this.mHal1Client});
        radioPw.increaseIndent();
        synchronized (this.mLock) {
            radioPw.printf("Modules of HAL1 client: %s\n", new java.lang.Object[]{this.mV1Modules});
        }
        radioPw.decreaseIndent();
        radioPw.printf("HAL2 client:\n", new java.lang.Object[0]);
        radioPw.increaseIndent();
        this.mHal2Client.dumpInfo(radioPw);
        radioPw.decreaseIndent();
        radioPw.decreaseIndent();
    }

    private static boolean isDebugEnabled() {
        return android.util.Log.isLoggable(TAG, 3);
    }
}
