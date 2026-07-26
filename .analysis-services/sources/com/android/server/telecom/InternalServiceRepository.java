package com.android.server.telecom;

/* JADX INFO: loaded from: classes3.dex */
public class InternalServiceRepository extends com.android.internal.telecom.IInternalServiceRetriever.Stub {
    private final com.android.server.DeviceIdleInternal mDeviceIdleController;
    private final com.android.internal.telecom.IDeviceIdleControllerAdapter.Stub mDeviceIdleControllerAdapter = new com.android.internal.telecom.IDeviceIdleControllerAdapter.Stub() { // from class: com.android.server.telecom.InternalServiceRepository.1
        public void exemptAppTemporarilyForEvent(java.lang.String packageName, long duration, int userHandle, java.lang.String reason) {
            com.android.server.telecom.InternalServiceRepository.this.mDeviceIdleController.addPowerSaveTempWhitelistApp(android.os.Process.myUid(), packageName, duration, userHandle, true, 0, reason);
        }
    };

    public InternalServiceRepository(com.android.server.DeviceIdleInternal deviceIdleController) {
        this.mDeviceIdleController = deviceIdleController;
    }

    public com.android.internal.telecom.IDeviceIdleControllerAdapter getDeviceIdleController() {
        ensureSystemProcess();
        return this.mDeviceIdleControllerAdapter;
    }

    private void ensureSystemProcess() {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("SYSTEM ONLY API.");
        }
    }
}
